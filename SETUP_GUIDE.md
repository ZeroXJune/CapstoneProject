# TrikRide Setup Guide

## Prerequisites

Before you begin, ensure you have:
- Android Studio Ladybug (2024.2.1) or later
- Android SDK platform 35 installed, with a minimum device API of 24
- JDK 17 — use the JDK bundled with Android Studio rather than a system install
- A Firebase account
- A Google Cloud project, only if you are enabling Maps

Kotlin 2.1.0, Android Gradle Plugin 8.7.3, and Gradle 8.11.1 come from the project itself.
The Gradle wrapper is committed, so do not point Android Studio at its own Gradle.

## Step 1: Clone the Repository

```bash
git clone https://github.com/zeroxjune/capstoneproject.git
cd capstoneproject
```

## Step 2: Open in Android Studio

1. Launch Android Studio
2. Select "Open" and navigate to the project directory
3. Wait for Gradle sync to complete

## Step 3: Firebase Setup

### 3.1 Create Firebase Project

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click "Add project"
3. Enter "TrikRide" as project name
4. Enable Google Analytics (optional)
5. Create the project

### 3.2 Register Android App

1. In Firebase Console, click "Add app" → "Android"
2. Enter package name: `com.tpc.trikride`
3. Enter app nickname: `TrikRide`
4. Leave the SHA-1 box empty for now — see below.
5. Register the app and download `google-services.json`
6. Place `google-services.json` in `app/` directory (see `app/google-services.json.template`
   for the expected shape). The build automatically enables the Google Services plugin
   once the file exists — the project still compiles without it.

### 3.2a SHA-1 fingerprints

**You do not need one to run this app.** Email and password sign-in, the Realtime
Database and Cloud Messaging all work without any fingerprint registered. It is needed
only for Google Sign-In, which is not built, and for locking a Google Maps API key to this
app, which Step 5 covers. Add it when you do one of those, not before.

**Getting the debug fingerprint.** From the project directory:

```
gradlew signingReport          # Windows
./gradlew signingReport        # macOS and Linux
```

The output lists every variant. Find the block that says `Variant: debug`, and copy the
line beginning `SHA1:` — forty hexadecimal characters separated by colons. Android Studio
runs the same thing from the Gradle panel: **Tasks → android → signingReport**.

**Getting the release fingerprint.** This one comes out of the keystore, so it does not
exist until you have generated one — see Deployment. Once you have:

```
keytool -list -v -keystore trikride-release.jks -alias trikride
```

**Adding either to Firebase.** Project settings → **General** → scroll to *Your apps* →
select the Android app → **Add fingerprint** → paste → Save. Then **download
`google-services.json` again** and replace the one in `app/`. The file carries the
registered fingerprints, so a stale copy behaves as though you never added it.

Register both, and register them separately. A debug build is signed with the debug key
and a distributed build with the release key, so a fingerprint that covers one does
nothing for the other. If the app is ever published through Play App Signing, Google
re-signs it with a key of their own and that certificate's SHA-1 has to be registered as
well — the Play Console shows it under Setup → App signing.

### 3.3 Enable Firebase Services

In Firebase Console, enable:
- **Authentication**: Email/Password
- **Realtime Database**: Create database in production mode
- **Cloud Messaging**: For push notifications

Cloud Storage is deliberately not used. New Firebase projects need the paid Blaze plan
before a Storage bucket can be provisioned, and Blaze requires a card on file. Profile
photos are shrunk to 256 pixels square, compressed, and written into the Realtime
Database instead, which keeps the whole project on the free Spark plan.

### 3.4 Set the public-facing name

Do this before anyone else uses the app. Project settings → **General** → **Public-facing
name** → `TrikRide`. Set the **Support email** on the same page.

Firebase writes that name into every email it sends, and it is not set for you: a new
project falls back to `project-<project number>`, so a password reset arrives telling the
user to reset their `project-369325950814` password, signed by the
`project-369325950814` team. It looks like a phishing attempt because it reads like one.

The wording itself is under Authentication → **Templates**, where the subject, the body
and the sender's display name can all be edited. The `%APP_NAME%` placeholder in those
templates is what pulls in the public-facing name.

Expect Firebase mail to land in spam anyway. It is sent from
`noreply@<project-id>.firebaseapp.com`, which Gmail treats with suspicion whatever the
name says, and the user manual tells people to look there. Marking one message "not spam"
trains Gmail for that account, which is worth doing on your own while testing. Sending
from a domain you own would fix it properly and needs DNS records, so it is out of scope
for the study.

## Step 4: Configure Firebase Security Rules

These rules are what actually enforce access. The app talks to Firebase directly with no
server in between, so anything the rules allow is allowed, whatever the interface shows.

### Realtime Database Rules

```json
{
  "rules": {
    "users": {
      ".read": "auth != null && root.child('users').child(auth.uid).child('userType').val() === 'ADMIN'",
      "$uid": {
        ".read": "auth != null && ($uid === auth.uid || root.child('users').child(auth.uid).child('userType').val() === 'ADMIN')",
        ".write": "auth != null && $uid === auth.uid",
        "userType": {
          ".validate": "newData.val() === 'PASSENGER' || newData.val() === 'DRIVER'"
        }
      }
    },
    "drivers": {
      ".read": "auth != null",
      "$uid": {
        ".write": "auth != null && $uid === auth.uid",
        "verificationStatus": {
          ".write": "auth != null && root.child('users').child(auth.uid).child('userType').val() === 'ADMIN'"
        },
        "hasLicenceImage": {
          ".write": "auth != null && ($uid === auth.uid || root.child('users').child(auth.uid).child('userType').val() === 'ADMIN')"
        }
      }
    },
    "driverRatings": {
      "$driverId": {
        ".read": "auth != null",
        "$raterId": {
          ".write": "auth != null && $raterId === auth.uid",
          ".validate": "newData.isNumber() && newData.val() >= 1 && newData.val() <= 5"
        }
      }
    },
    "driverDocuments": {
      "$uid": {
        ".read": "auth != null && ($uid === auth.uid || root.child('users').child(auth.uid).child('userType').val() === 'ADMIN')",
        ".write": "auth != null && ($uid === auth.uid || root.child('users').child(auth.uid).child('userType').val() === 'ADMIN')"
      }
    },
    "rideRequests": {
      ".read": "auth != null && (root.child('drivers').child(auth.uid).child('verificationStatus').val() === 'APPROVED' || root.child('users').child(auth.uid).child('userType').val() === 'ADMIN')",
      "$requestId": {
        ".read": "auth != null && data.child('passengerId').val() === auth.uid",
        ".write": "auth != null && ((!data.exists() && newData.child('passengerId').val() === auth.uid) || (data.exists() && (data.child('passengerId').val() === auth.uid || root.child('drivers').child(auth.uid).child('verificationStatus').val() === 'APPROVED')))"
      }
    },
    "rides": {
      ".read": "auth != null && (root.child('users').child(auth.uid).child('userType').val() === 'ADMIN' || ((query.orderByChild === 'passengerId' || query.orderByChild === 'driverId') && query.equalTo === auth.uid))",
      ".indexOn": ["passengerId", "driverId"],
      "$rideId": {
        ".read": "auth != null && (data.child('passengerId').val() === auth.uid || data.child('driverId').val() === auth.uid)",
        ".write": "auth != null && ((!data.exists() && newData.child('driverId').val() === auth.uid) || (data.exists() && data.child('driverId').val() === auth.uid))",
        "passengerName": {
          ".write": "auth != null && data.parent().child('passengerId').val() === auth.uid"
        },
        "passengerPhone": {
          ".write": "auth != null && data.parent().child('passengerId').val() === auth.uid"
        }
      }
    },
    "config": {
      ".read": "auth != null",
      ".write": "auth != null && root.child('users').child(auth.uid).child('userType').val() === 'ADMIN'"
    },
    "complaints": {
      ".read": "auth != null && (root.child('users').child(auth.uid).child('userType').val() === 'ADMIN' || (query.orderByChild === 'reporterId' && query.equalTo === auth.uid))",
      ".indexOn": ["reporterId"],
      "$id": {
        ".read": "auth != null && (data.child('reporterId').val() === auth.uid || root.child('users').child(auth.uid).child('userType').val() === 'ADMIN')",
        ".write": "auth != null && ((!data.exists() && newData.child('reporterId').val() === auth.uid) || root.child('users').child(auth.uid).child('userType').val() === 'ADMIN')"
      }
    },
    "notifications": {
      "$uid": {
        ".read": "auth != null && $uid === auth.uid",
        ".write": "auth != null && ($uid === auth.uid || root.child('users').child(auth.uid).child('userType').val() === 'ADMIN' || root.child('drivers').child(auth.uid).child('verificationStatus').val() === 'APPROVED')",
        "$id": {
          ".validate": "newData.hasChildren(['title', 'message'])"
        }
      }
    },
    "profilePhotos": {
      "$uid": {
        ".read": "auth != null",
        ".write": "auth != null && $uid === auth.uid"
      }
    }
  }
}
```

Five notes on these.

**Nobody can promote themselves.** A user may write their own record, and `userType` lives
on it, so without the `.validate` under `userType` any signed-in account could set itself
to `ADMIN` and immediately gain every administrator power — reading all users, all rides
and all complaints, opening any driver's licence photograph, and rewriting the fare table.
The validate restricts self-assignment to `PASSENGER` or `DRIVER`. Administrator accounts
are made by editing `userType` in the Firebase console, which bypasses rules; see Step 7.

**Rides are readable only by the two people on them.** A rule cannot filter a list per
child — Firebase either hands over the node or refuses it — so the rule instead requires
the client to have *asked* a scoped question, `orderByChild('passengerId').equalTo(<own
uid>)` or the same on `driverId`. `FirebaseService` issues exactly those queries.
Administrators are exempt and read the node whole for the monitor and the reports.
`complaints` works the same way on `reporterId`. This is also why the two `.indexOn`
entries are there: without them Firebase sorts on the client and logs a warning.

**Only the driver on a ride may change it.** Creating one requires the new record's
`driverId` to be the caller, which is what happens when a driver accepts; after that only
that same driver can advance its status. Ride requests are writable by the passenger who
made one and by any approved driver, since accepting is a delete.

The two exceptions are `passengerName` and `passengerPhone`, which the passenger writes
and nobody else can. Names and telephone numbers live on `users/{uid}`, which is private
to the account, so without this neither party on a ride can see or contact the other —
the passenger cannot be told who is coming and the driver cannot ring to say they have
arrived. Putting the two fields on the ride shares them with exactly the other person on
it, since `rides/$rideId` is readable only by those two. The driver's own name and number
travel the same way, written when they accept. Nothing goes on the open request: that is
broadcast to every approved driver, and a telephone number has no business there.

`driverDocuments` is the one node not readable by every signed-in user, holding licence
photographs, which are sensitive personal information under the Data Privacy Act of 2012.
It is scoped to the driver and to administrators, and administrators need write as well as
read because refusing an application deletes the photograph.

`config` covers both `config/fare` and `config/fareStops`, which every signed-in user reads
to price a ride but only an administrator can change.

**What these rules still do not fix.** Notifications are written by one party about
another — a driver tells a passenger their ride was accepted, an administrator tells a
driver the verification decision — so the write cannot be limited to the owner of the
node. It is limited instead to the three kinds of account that legitimately send one: the
owner, an administrator, and an approved driver. That is narrower than any signed-in
account, but an approved driver could still write a notification to a passenger they are
not carrying. Closing it properly means moving the send to a Cloud Function, which is the
same deferred work as server-side push. The `.validate` only enforces the shape.

**If Firebase emails you about insecure rules,** it is telling you the published rules are
more open than these. The most common cause is that the database is still on the test-mode
rules Firebase creates it with, which allow any signed-in account to read and write
everything and expire after thirty days. Open the email: it names the paths it objects to.
Then open Realtime Database → Rules in the console, check that what is published matches
the block above, and publish it if it does not. Test-mode rules are fine while nothing but
your own test accounts exist, and must be gone before the evaluation.

## Step 5: Maps

The app has two map renderers and picks one at build time.

**Leave `MAPS_API_KEY` blank** and it draws OpenStreetMap tiles through osmdroid. No key,
no billing account, nothing to configure — it works on a fresh clone and cannot be taken
down by an expired trial.

**Set `MAPS_API_KEY`** and it uses Google Maps instead, which has better street data for
Talibon. To get a key:

1. Open [console.cloud.google.com](https://console.cloud.google.com) and select your
   existing TrikRide project. Your Firebase project is already a Cloud project — do not
   create a second one, or the billing you enabled will not apply.
2. APIs & Services → Library → **Maps SDK for Android** → Enable. Only that one. Directions,
   Places and Routes are the billed services; leave them off.
3. APIs & Services → Credentials → Create credentials → API key.
4. Restrict it before closing the dialog. *Application restrictions* → **Android apps**,
   then add package `com.tpc.trikride` with a SHA-1. *API restrictions* → **Restrict key**
   → Maps SDK for Android only. An unrestricted key that leaks can be billed to you.
5. You need two SHA-1s, one per signing key. Step 3.2a explains where each comes from.
   Add both: debug builds show a blank map without the first, distributed builds without
   the second.
6. Put it in `.env` as `MAPS_API_KEY=AIza...`. The build injects it into the manifest and
   into `BuildConfig`, and `.env` is gitignored so it never reaches GitHub.

**On cost.** Displaying a map through the Android SDK carries no charge as long as no Map
ID or cloud-based styling is used, and the app uses neither. Route calculation is what
costs money, and the app does not compute routes. Set a budget alert anyway under Billing
→ Budgets & alerts — not because you expect a bill, but because it is how you find out if
something misbehaves.

**On the trial.** A Google Cloud free trial suspends the project when it ends unless you
upgrade. A suspended project means a dead key and blank maps. If that happens, clear
`MAPS_API_KEY` in `.env` and rebuild: the app falls back to OpenStreetMap and keeps
working. That fallback is the reason both renderers are kept.

Location comes from the device's GPS through Google Play Services, which is free either
way. The app requests the location permission the first time a driver goes online and
never asks for background location.

## Step 6: Build and Run

```bash
# Build the project
./gradlew build

# Run on emulator or device
./gradlew installDebug
```

## Step 7: Create the administrator account and load the fares

The app has no bootstrap administrator. Create one by hand the first time.

1. Register an account in the app as normal.
2. Open the Firebase console, go to Realtime Database, find `users/{uid}` for that
   account, and change `userType` from `PASSENGER` to `ADMIN`. This is the only way to
   make an administrator: the security rules stop an account setting its own `userType`
   to anything but `PASSENGER` or `DRIVER`, and console edits bypass rules.
3. Sign out and back in. You will land on the admin dashboard.

Then load the fare table, without which nothing can be booked:

4. Go to the **Fares** tab. It will say no rate table is loaded.
5. Tap **Load official rates**. This writes all 240 stops from the transcribed FeTODAT
   schedule into `config/fareStops` in one request.
6. Open the minimums and flat rates dialog and check the four numbers: minimum regular
   ₱25, minimum discounted ₱20, Poblacion flat ₱25, terminal round trip ₱25. Loading the
   rates leaves these four alone on purpose, so that an administrator's corrections
   survive a reload — which also means a database seeded before these values were fixed
   still holds the old ones, and reloading will not put them right. Edit them here.
7. Tap the **Needs review** chip. Forty-six entries are flagged as uncertain from the
   transcription and three are switched off because no usable rate could be read. Check
   them against the physical posted sheet before anyone uses the app for real. Each one
   opens with the specific problem written at the top of the dialog.

## Step 8: Configure Local Properties (Optional)

Create `local.properties` file in root directory:

```properties
sdk.dir=/path/to/android/sdk
ndk.dir=/path/to/android/ndk
```

## Troubleshooting

Problems we actually hit, and what fixed them.

### Gradle sync fails with "Unable to find method DependencyHandler.module"

Android Studio is using its own Gradle instead of the project's. The wrapper is committed
for exactly this reason. In Settings → Build Tools → Gradle, set **Use Gradle from:
gradle-wrapper.properties**, and set the Gradle JDK to the embedded JDK 17.

### Build fails at processDebugGoogleServices

The package name registered in the Firebase console does not match the app. It must be
`com.tpc.trikride` on both sides. Re-download `google-services.json` after fixing it.

### Duplicate resource errors on the launcher icon

Left-over `.webp` files from a manual Image Asset run colliding with the committed PNGs.
Delete every `.webp` under `app/src/main/res/`.

### "Creating your account" spins forever

The Realtime Database has not been created, or `google-services.json` has no database
URL in it. Create the database in the Firebase console, re-download the file, and replace
it. The app now gives up after twelve seconds with a message saying this rather than
spinning, but the underlying cause is the same.

### No destinations appear when booking

The fare table has not been loaded. Sign in as an administrator and use **Fares → Load
official rates**. See Step 7.

### Profile photo will not save

The app shrinks the image to 256 pixels square and writes it into
`profilePhotos/{uid}` in the database, so this is a database permission problem rather
than a Storage one. Check that the `profilePhotos` rule from Step 4 is published. If the
message says the image could not be processed, the file was not a readable image or it
would not compress below 24 KB even at the lowest quality.

### Licence photo will not send, or an administrator cannot open it

Same shape of problem, different node: check that the `driverDocuments` rule from Step 4
is published. A licence is scaled to 1280 pixels on its long edge and allowed about
200 KB, which is far more than an avatar, because the administrator has to read the number
and the expiry date off it. "That image could not be prepared" means the file was not a
readable image, or it would not come under 200 KB even at the lowest quality — rare for a
photograph, common for a screenshot of a scan.

### Gradle sync issues generally

```bash
./gradlew clean
./gradlew build --refresh-dependencies
```

## Database Structure

```
users/{uid}
  email, phoneNumber, firstName, lastName, birthDate,
  userType (PASSENGER | DRIVER | ADMIN), profileImageUrl,
  createdAt, updatedAt

drivers/{uid}
  licenseNumber, licenseExpiry, tricycleNumber,
  verificationStatus (PENDING | APPROVED | REJECTED | EXPIRED),
  isAvailable, currentLocation, rating, totalRides, verifiedAt, documents[]

rideRequests/{requestId}          open requests; deleted on accept or expiry
  passengerId, pickupLocation, dropoffLocation,
  passengerCount, luggage, estimatedFare,
  fareStopId, fareType (REGULAR | DISCOUNTED),
  notes, requestedAt, expiresAt

rides/{rideId}
  passengerId, driverId, pickupLocation, dropoffLocation,
  status (REQUESTED … COMPLETED | CANCELLED | NO_SHOW),
  estimatedFare, actualFare, fareStopId, fareType,
  passengerCount, luggage, notes,
  requestedAt, acceptedAt, startedAt, completedAt

config/fare                       one record
  minimumRegular, minimumDiscounted, poblacionFlat, terminalRoundTrip,
  chargePerPassenger, source, seededAt

config/fareStops/{stopId}         240 records from the FeTODAT schedule
  zone, name, regularFare, discountedFare,
  active, needsReview, confidence, note

complaints/{complaintId}
  reporterId, reporterName, reporterType, category, description,
  status (OPEN | IN_REVIEW | RESOLVED), adminNote, createdAt, resolvedAt

notifications/{uid}/{notificationId}
  title, message, type, read, createdAt
```

Timestamps throughout are epoch milliseconds held as strings.

## How the app talks to the backend

There is no REST API and no server of ours. The Android client speaks to Firebase
directly. One-off reads and writes go over HTTPS; live data arrive over a persistent
WebSocket that the Firebase client library maintains, which is what makes a driver's
screen change when a passenger books without anyone refreshing anything.

Everything the app does to the database goes through one class,
`services/FirebaseService.kt`. If you are looking for where a piece of data is read or
written, it is in there.

| What you want | Where to look |
|:---|:---|
| Sign-up, sign-in, session, profile photo | `repositories/AuthRepository.kt` |
| Request a ride, accept, advance status, history | `repositories/RideRepository.kt` |
| Driver record, availability, credentials | `repositories/DriverRepository.kt` |
| Verification, all users, all rides | `repositories/AdminRepository.kt` |
| Fare config, fare stops, seed import | `repositories/FareRepository.kt` |
| Complaints and notifications | `repositories/SupportRepository.kt` |

Because there is no server in the path, the security rules in Step 4 are the only thing
enforcing access. Checks in the interface are convenience, not security.

## Testing

```bash
./gradlew test                 # unit tests
./gradlew connectedAndroidTest # instrumented tests, needs a device or emulator
```

There are no automated tests in the repository yet. `FareEngine` and `ReportBuilder` are
pure functions with no Android dependency and are the obvious place to start.

## Deployment

### Generate the signing key, once

```bash
keytool -genkeypair -v \
  -keystore trikride-release.jks \
  -alias trikride \
  -keyalg RSA -keysize 2048 -validity 10000
```

`keytool` ships with the JDK. If it is not on your PATH, it is under the `bin` folder of
the JDK bundled with Android Studio.

Answer the prompts, then **back the `.jks` file up somewhere that is not this laptop**.
Losing it means nobody who installed the app can ever be sent an update: Android will
refuse a package signed by a different key, and the only way out is a new package name
and a fresh install for every user. `*.jks` and `*.keystore` are gitignored, so the file
will not be committed by accident, which also means it will not be backed up by accident.

### Point the build at it

Add these to `.env`:

```properties
RELEASE_STORE_FILE=trikride-release.jks
RELEASE_STORE_PASSWORD=the-store-password
RELEASE_KEY_ALIAS=trikride
RELEASE_KEY_PASSWORD=the-key-password
```

The path is relative to the project root, or absolute if you keep the keystore elsewhere.

### Build

```bash
./gradlew assembleRelease   # APK, for sideloading and App Distribution
./gradlew bundleRelease     # AAB, only needed for the Play Store
```

The APK lands in `app/build/outputs/apk/release/`.

If any of the four values is missing, the build still succeeds but signs with the debug
key and prints a warning saying so. That output installs and runs, and must not be handed
to anyone: a debug-signed build cannot be updated by a properly signed one later.

### Bump the version before every build you hand out

In `app/build.gradle.kts`:

```kotlin
versionCode = 1        // a whole number, raise it every single time
versionName = "1.0.0"  // what people see
```

Android decides whether one build replaces another by `versionCode` alone. Hand out two
builds with the same number and the second will not install over the first — testers get
a failure they cannot explain and you lose an afternoon to it. Raise it for every build
that leaves your machine, even a one-line fix.

### Firebase App Distribution

Free, no card, and it records who installed which version, which is participation
evidence for Chapter 4. Nothing needs adding to the build — the console takes the APK
directly.

1. Firebase Console → **Release & Monitor** → **App Distribution** → **Get started**.
2. **Testers & Groups** tab → **Add group** → name it something like
   `Evaluation respondents`. Add email addresses now or later.
3. **Releases** tab → drag `app/build/outputs/apk/release/app-release.apk` onto the page.
4. Pick the group, write a release note saying what changed, and **Distribute**.

Every tester gets an email. They open it on the phone, accept, install the **Firebase App
Tester** app when prompted, and TrikRide installs from inside it. Later builds appear in
App Tester automatically for the same group — no second invitation. Unlike the Apple
equivalent, no device registration is needed.

The Releases tab then shows, per build, who was invited, who accepted and who installed.
Screenshot that for the evaluation chapter; it is the cleanest record of participation
you will get.

### A GitHub release

Reaches anyone with the link and needs no invitation, which suits a QR code on a poster.

1. `gradlew assembleRelease`, then find the APK in `app/build/outputs/apk/release/`.
2. On GitHub: **Releases** → **Draft a new release**.
3. **Choose a tag** → type `v1.0.0` → *Create new tag on publish*.
4. Title it, describe what testers should try, and drag the APK into the attachments box.
5. **Publish release**, then right-click the attached APK and copy its address. That link
   is what goes into a QR generator.

**The repository has to be public for this to work.** Release attachments on a private
repository require a GitHub account with access, so a respondent tapping the QR code gets
a sign-in page. Either make the repository public first — it is on the list to do before
the defence anyway — or use App Distribution instead.

### Google Drive

The option that needs nothing set up, and the weakest of the three. Use it for the adviser
and the panel; use App Distribution for respondents.

1. Rename the built APK to something meaningful first — `TrikRide-v1.0.0.apk`.
   `app-release.apk` tells the person receiving it nothing, and leaves you unable to tell
   two builds apart later.
2. Upload it, then right-click → **Share** → **Anyone with the link** → **Viewer**. Miss
   this and every respondent gets "Request access" instead of a download.
3. Share the link, or put it through a QR generator.

For a later build, do not upload a second file. Right-click the existing one → **Manage
versions** → **Upload new version**. The link stays the same, so an old QR code still
fetches the current build.

What you give up: Drive records nothing about who downloaded or installed, so the
participation evidence App Distribution collects for Chapter 4 has to be gathered by
asking people. And a respondent has to dismiss Chrome's "this type of file can harm your
device" warning on the way in — a hard thing to ask of a driver you are also asking to
photograph their licence.

Whichever route, people installing outside an app store have to permit it once. Part 1 of
the user manual walks through that, so hand it out with the link.

### The Play Store

Nothing in the project prevents publishing there, but it is the wrong channel for the
evaluation and a poor one for this app even afterwards. Read this before starting, because
the first two items cost weeks, not hours.

**A closed testing period comes first.** A personal developer account has to run a closed
test with twelve testers who keep the app installed for fourteen continuous days before it
may even apply for production access, and the application is then reviewed. An
organisation account skips this but needs a D-U-N-S number for the college, which takes
its own weeks to obtain. Google changes these terms; check what the Console says when you
start.

**Check the API level.** Google requires a new app to target the API level released the
previous year, enforced every 31 August. `targetSdk` is 35, which matches `compileSdk` and
is as high as Android Gradle Plugin 8.7.3 goes. Going to 36 means upgrading the plugin and
the Gradle wrapper with it, so check what the Console demands before assuming 35 is
enough.

Then, in order:

1. **Register.** play.google.com/console. US$25, paid once — not annual, not per app —
   plus identity verification. Publishing itself costs nothing afterwards, and Google
   takes a share only of money that passes through Play, which for a free app settled in
   cash is none. The fee needs a card that accepts international online payments; a
   virtual card from GCash or Maya is the usual way round not having one, though neither
   is guaranteed to be accepted.
2. **Build a bundle, not an APK.** `./gradlew bundleRelease`, output in
   `app/build/outputs/bundle/release/`. Play has not accepted APKs for new apps in years.
3. **Accept Play App Signing.** Google holds the signing key and your keystore becomes the
   *upload* key. This is worth understanding before you agree to it: it changes what
   losing the keystore means, and it cannot be undone later.
4. **Host the privacy policy at a public URL.** The listing requires a link, not a
   document. GitHub Pages serving `docs/legal/privacy-policy.md` is enough.
5. **Fill the Data safety form honestly.** The app collects name, email, phone number,
   date of birth, precise location, a profile photograph, and — for drivers — a photograph
   of a government identity document. All of it has to be declared, and the licence
   photograph is the one that will draw questions, so have Section 9 of the privacy policy
   to hand. A form that does not match what the app does is grounds for removal.
6. **Complete the content rating questionnaire, target audience declaration, and store
   listing** — icon, feature graphic, screenshots, description.

**One thing to think about before any of that.** A Play listing is public, and this app
does nothing useful outside Talibon: it needs an administrator to have loaded the fare
table, and it prices rides against one municipality's ordinance. Anyone who installs it
from a public listing gets an app that cannot book a ride. App Distribution reaches
exactly the people the evaluation needs and no one else, which is both the right audience
and the right evidence.

## Support

For issues or questions:
- Email: alberjunemumar@gmail.com
- GitHub Issues: https://github.com/zeroxjune/capstoneproject/issues

## License

© 2026 Talibon Polytechnic College. All rights reserved.
