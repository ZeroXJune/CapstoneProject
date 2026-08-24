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
4. Get your SHA-1 fingerprint:
   ```bash
   ./gradlew signingReport
   ```
5. Register the app and download `google-services.json`
6. Place `google-services.json` in `app/` directory (see `app/google-services.json.template`
   for the expected shape). The build automatically enables the Google Services plugin
   once the file exists — the project still compiles without it.

### 3.3 Enable Firebase Services

In Firebase Console, enable:
- **Authentication**: Email/Password
- **Realtime Database**: Create database in production mode
- **Cloud Messaging**: For push notifications

Cloud Storage is deliberately not used. New Firebase projects need the paid Blaze plan
before a Storage bucket can be provisioned, and Blaze requires a card on file. Profile
photos are shrunk to 256 pixels square, compressed, and written into the Realtime
Database instead, which keeps the whole project on the free Spark plan.

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
        ".write": "auth != null && ((!data.exists() && newData.child('driverId').val() === auth.uid) || (data.exists() && data.child('driverId').val() === auth.uid))"
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
        ".write": "auth != null",
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
that same driver can advance its status. Passengers never write to `rides`. Ride requests
are writable by the passenger who made one and by any approved driver, since accepting is
a delete.

`driverDocuments` is the one node not readable by every signed-in user, holding licence
photographs, which are sensitive personal information under the Data Privacy Act of 2012.
It is scoped to the driver and to administrators, and administrators need write as well as
read because refusing an application deletes the photograph.

`config` covers both `config/fare` and `config/fareStops`, which every signed-in user reads
to price a ride but only an administrator can change.

**Two things these rules do not fix.** A driver's licence *number* still sits on
`drivers/{uid}`, which every signed-in account can read because passengers need the
availability and position on that same record; moving `licenseNumber` and `licenseExpiry`
into `driverDocuments` would close it and is worth doing before any wider deployment. And
`notifications/{uid}` remains writable by any authenticated user, because the app has one
party notify the other and the rules cannot tell a legitimate sender from a spoofed one
without a server; the `.validate` only enforces the shape.

While you are still testing, you may want the looser test-mode rules Firebase offers.
Do not leave them on once real accounts exist.

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
5. You need two SHA-1s, one per signing key:
   ```bash
   gradlew signingReport                                        # debug
   keytool -list -v -keystore trikride-release.jks -alias trikride   # release
   ```
   Add both. Debug builds break without the first, distributed builds without the second.
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
6. Tap the **Needs review** chip. Forty-six entries are flagged as uncertain from the
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

### Getting it to users

**Firebase App Distribution** is on the free tier. Upload the APK, add tester email
addresses, and they receive an install link. Later builds go to the same group. It also
records who installed which version, which is usable evidence of participation for the
evaluation chapter.

**A GitHub release** with the APK attached works for anyone, no invitation needed. Print
the link as a QR code. Users have to allow installation from outside the app store, so
include the illustrated install guide from the user manual.

The Play Store is neither free nor quick: a one-time developer fee, and for new
individual accounts a closed testing period before public release. Nothing in the project
prevents publishing there later.

## Support

For issues or questions:
- Email: alberjunemumar@gmail.com
- GitHub Issues: https://github.com/zeroxjune/capstoneproject/issues

## License

© 2026 Talibon Polytechnic College. All rights reserved.
