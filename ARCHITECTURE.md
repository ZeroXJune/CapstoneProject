# Architecture

Package: `com.tpc.trikride`. Kotlin 2.1, Jetpack Compose, Firebase. MVVM with a
repository layer, which is what Google recommends and, more to the point, what keeps a
three-person team from stepping on each other.

## Layers

```
Compose screens          render state, emit events, hold no logic
      ↓ events                    ↑ StateFlow
ViewModels               one per role; own the screen state
      ↓ suspend calls             ↑ Flow
Repositories             six of them; the ViewModels' only data source
      ↓
FirebaseService          the single place that touches the database
      ↓ HTTPS / WebSocket
Firebase                 Auth, Realtime Database, Cloud Messaging
```

Each layer knows only about the one below it. A screen knows its ViewModel. A ViewModel
knows repositories, never Firebase. A repository knows `FirebaseService`, never the UI.
Changing where data live touches one layer; changing how a screen looks touches another.

Data flows one way. An event goes down, a change comes back up as a `Flow`, the state
updates, Compose recomposes whatever depended on it. Nothing pushes into a view
directly.

## Where things are

```
models/
  User.kt              User, UserType, Driver, Passenger, VerificationStatus,
                       Document, DocumentType, SavedLocation, Location
  Ride.kt              Ride, RideStatus, RideRequest, RideOffer, RideReview,
                       PaymentMethod, PaymentStatus
  FareConfig.kt        FareStop, FareType, FareConfig, FareQuote
  Complaint.kt         Complaint, ComplaintStatus, COMPLAINT_CATEGORIES
  AppNotification.kt   AppNotification, NotificationType

services/
  FirebaseService.kt           every database read and write
  TrikRideMessagingService.kt  FCM receiver

repositories/
  AuthRepository.kt     sign-up, sign-in, session, profile photo
  RideRepository.kt     request, accept, status progression, history
  DriverRepository.kt   driver record, availability, credentials
  AdminRepository.kt    all drivers, all users, all rides, verification
  FareRepository.kt     fare config, fare stops, one-shot seed import
  SupportRepository.kt  complaints and notifications

viewmodels/
  AuthViewModel        bootstrap, login, register, account type, sign out
  PassengerViewModel   active rides, history, fare stops, booking
  DriverViewModel      open requests, active rides, earnings, status advance
  AdminViewModel       drivers, users, rides, complaints, fare table, import
  ProfileViewModel     profile edit, photo upload, password reset
  SupportViewModel     complaint submission, notification centre

ui/screens/
  MainAppScreen        routing: bootstrap → onboarding → auth → role dashboard
  OnboardingScreen     first-launch carousel and the welcome-back screen
  PassengerHomeScreen  home, history, support, profile
  DriverHomeScreen     dashboard, requests, history, profile
  AdminDashboardScreen verify, concerns, monitor, fares, profile
  AdminReportsScreen   period selection, summary, CSV export
  NotificationsScreen  notification centre
  SettingsScreen       shared profile screen for all three roles
  LegalScreen          terms of service and privacy notice

ui/components/
  CommonComponents.kt  PrimaryButton, SecondaryButton, SectionCard, SkeletonBox,
                       SkeletonCard, RefreshableBox, SimplePlaceholder, TrikTextField
  AvatarPicker.kt      circular avatar with a camera/gallery chooser

utils/
  FareEngine.kt     prices a ride from the table
  FareSeed.kt       the 240 transcribed FeTODAT stops
  ReportBuilder.kt  CSV generation and period bucketing
  ReportExporter.kt writes through the file picker, or shares
  PasswordRules.kt  password policy, evaluated live as the user types
  ProfilePhoto.kt   shrinks and base64-encodes an avatar for the database
  AuthPrefs.kt      remembered email, onboarding-seen flag
  LocationUtils.kt  haversine distance
  Constants.kt      pickup points, request TTL, max passengers
```

## Database

Realtime Database, seven top-level nodes.

```
users/{uid}                    profile
drivers/{uid}                  credentials, verification, availability, rating
rideRequests/{id}              open requests; deleted on accept or expiry
rides/{id}                     accepted rides through to completion
config/fare                    minimums, flat rates, per-head flag, source
config/fareStops/{stopId}      the 240-entry rate table
complaints/{id}                concerns raised by passengers and drivers
notifications/{uid}/{id}       per-user notification feed
profilePhotos/{uid}            base64 avatar, a few kilobytes
```

Fare stops sit in their own node rather than inside `config/fare` so that correcting one
price is a small write instead of a rewrite of all 240. Profile photos are separated from
`users` for the same kind of reason: the admin screens read every user record constantly,
and an avatar embedded in each one would be pulled down every time.

**No Cloud Storage.** Firebase now requires the paid Blaze plan before a Storage bucket
can be provisioned, and this project runs on Spark with no card. `ProfilePhoto` squares
the chosen image, scales it to 256 pixels, and compresses it down a quality ladder until
the base64 fits in 24 KB. Decoding the source is done with `inSampleSize` first, because
decoding a 4000-pixel camera photo at full size to produce a thumbnail is how an app runs
out of memory.

Every model is a data class with a default for every field. Firebase's deserializer
needs a no-arg constructor, and defaults give it one while also making a partial record
survive a read. `FareStop.label` is computed and carries `@get:Exclude`, because
otherwise Firebase would serialize the getter as a field and then fail to find anywhere
to put it coming back.

## How the pieces work

**Live data.** `FirebaseService` wraps Firebase listeners in `callbackFlow`. That turns
a callback into a `Flow` and, more usefully, removes the listener when the collecting
coroutine is cancelled. Unbalanced listener registration is the standard way to leak
memory in a Firebase app; this makes it structural rather than something to remember.

**Matching.** A request is written to `rideRequests` and every online driver is
listening on that node, so it appears on their devices without polling. The first driver
to accept writes a `rides` entry and deletes the request, which removes it from every
other device. There is no lock; the delete is the resolution.

**Pricing.** `FareEngine.quote()` takes the config, the stop, the rate column, and the
head count. It reads the posted rate, raises it to the ordinance minimum if lower, and
multiplies. The priced fare travels on the `RideRequest`, so the driver sees the same
number the passenger agreed to rather than recomputing it.

**Session.** Firebase keeps the user signed in across restarts. `AuthViewModel`
constructs with `hasExistingSession` set synchronously from `repo.currentUserId`, so the
first frame can already choose between the welcome-back screen and the splash instead of
flashing the wrong one. `bootstrap()` then loads the role, guarded by a twelve-second
timeout: an unreachable database used to leave the app on a spinner forever.

**Reports.** `ReportBuilder` buckets rides by month or year from epoch-millisecond
strings. A timestamp that will not parse is excluded from every period rather than
silently landing in the current one. `ReportExporter` writes through
`ActivityResultContracts.CreateDocument`, so no storage permission is needed, or shares
through a `FileProvider` URI.

## Security

Authorization is enforced by Firebase Security Rules on the server, because there is no
application server in the path to enforce it. Client-side checks are UI convenience and
nothing more.

The rules restrict a user to their own profile, make `verificationStatus` writable only
by administrators, make the fare table readable by all authenticated users and writable
only by administrators, and scope notifications to their owner.

Secrets live in `.env`, which is gitignored; `.env.example` records which keys exist
without their values. `google-services.json` is gitignored too. The build reads `.env`
and injects values as manifest placeholders and `buildConfigField` entries.

No payment data is collected anywhere, which removes that category of risk entirely.

## Build

```
Gradle 8.11.1 (wrapper committed)   AGP 8.7.3   Kotlin 2.1.0   JDK 17
compileSdk 35   targetSdk 34   minSdk 24
compose-bom 2024.12.01
```

The wrapper is committed on purpose. Without it Android Studio supplies its own Gradle
version, and a newer Gradle against this AGP fails with an error that points nowhere
near the cause.

The Google Services plugin is applied conditionally on `google-services.json` existing,
so the project still configures on a machine that has not been given the Firebase file
yet.
