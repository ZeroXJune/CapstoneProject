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
  ConsentViewModel     checks and records agreement to the legal documents

ui/screens/
  MainAppScreen        routing: bootstrap → onboarding → auth → role dashboard
  OnboardingScreen     first-launch carousel and the welcome-back screen
  PassengerHomeScreen  home, history, support, profile
  DriverHomeScreen     dashboard, requests, history, profile
  AdminDashboardScreen verify, concerns, monitor, fares, profile
  AdminReportsScreen   period selection, summary, PDF and spreadsheet export
  NotificationsScreen  notification centre
  SettingsScreen       shared profile screen for all three roles
  ConsentScreen        the gate between sign-in and the dashboard
  LegalScreen          terms, privacy, community guidelines, driver agreement

ui/components/
  CommonComponents.kt  PrimaryButton, SecondaryButton, SectionCard, SkeletonBox,
                       SkeletonCard, RefreshableBox, SimplePlaceholder, TrikTextField
  AvatarPicker.kt      circular avatar with a camera/gallery chooser
  LicenceUpload.kt     licence submission, with its own consent step
  TrikMap.kt           OpenStreetMap view and the centre-pin location picker

utils/
  FareEngine.kt     prices a ride from the table
  FareSeed.kt       the 240 transcribed FeTODAT stops
  ReportBuilder.kt  period bucketing, CSV generation, chart aggregations
  PdfChart.kt       stat tiles, column charts and bar charts on a Canvas
  PdfReportWriter.kt the three printable reports
  ReportExporter.kt writes through the file picker, or shares
  PasswordRules.kt  password policy, evaluated live as the user types
  ProfilePhoto.kt   shrinks and base64-encodes an avatar for the database
  LicenceImage.kt   the same, at a size an administrator can actually read
  LocationProvider.kt  device position from the fused provider, as a Flow
  ReverseGeocoder.kt   turns a pinned point into a readable label
  AuthPrefs.kt      remembered email, onboarding-seen flag
  LocationUtils.kt  haversine distance
  Constants.kt      request TTL, max passengers, legal document version
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
driverDocuments/{uid}/licence  base64 photograph of the driver's licence
driverRatings/{uid}/{raterId}  one star rating per passenger, per driver
```

Fare stops sit in their own node rather than inside `config/fare` so that correcting one
price is a small write instead of a rewrite of all 240. Profile photos are separated from
`users` for the same kind of reason: the admin screens read every user record constantly,
and an avatar embedded in each one would be pulled down every time.

**Licence photographs.** `driverDocuments/{uid}/licence` is the one node not readable by
every signed-in account. A licence is sensitive personal information under the Data
Privacy Act of 2012, so the rules scope it to its owner and to administrators, and the
driver record carries only a `hasLicenceImage` flag — enough for the admin list to say
whether there is anything to review, without dragging a few hundred kilobytes of identity
document into every list read.

`LicenceImage` is `ProfilePhoto` with different targets: 1280 pixels on the long edge and
about 200 KB, aspect ratio kept. An avatar only has to look like the person; a licence has
to be legible enough to check the number and expiry against what the driver typed, and
squaring it off would cut the ends of the number.

Retention is enforced in code rather than left to a policy document.
`AdminRepository.rejectDriver` deletes the photograph in the same call that records the
refusal, because a rejected applicant's identity document serves no purpose the system
has. An approved driver's is kept — it is needed again at licence expiry and if a concern
is ever disputed — and the driver can withdraw it themselves.

Consent is asked at the point of upload, not inherited from the Terms accepted at sign-up,
and `consentedAt` is stamped when the driver ticks the box rather than when the write
lands. `Constants.LEGAL_VERSION` was bumped when this shipped, so every existing account
is asked to accept the revised documents on its next launch.

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

**Maps.** `TrikMap` and `PickerMap` are renderer-agnostic entry points. They dispatch to
`GoogleMapView.kt` when `BuildConfig.MAPS_API_KEY` is non-blank and to the osmdroid path in
`TrikMap.kt` when it is not. Every call site sees the same signatures either way.

Both are kept on purpose. Google has better street data for Talibon, but its key depends on
a live billing account, and a free trial that lapses takes the maps with it. Clearing
`MAPS_API_KEY` falls back to OpenStreetMap, which needs no account and cannot expire — a
one-line recovery rather than a broken app in the hands of a drivers' association.

Neither path uses a Map ID or cloud-based styling: a Map ID makes every map load a billed
Dynamic Maps call, while client-styled maps render at no charge.

Google's `MapView` is an old-style view that needs onCreate, onResume, onPause and onDestroy
forwarded by hand; `rememberMapViewWithLifecycle` does that from the composition's lifecycle,
and skipping it leaks the map renderer.

Two things in `configureOsmdroid` are not optional. The tile cache goes in app-private
storage, or osmdroid asks for a storage permission it does not need. And the user agent is
set to the package name, because OpenStreetMap's servers block osmdroid's default agent
and the symptom is blank tiles rather than an error.

**Location.** A driver publishes position only while online and only while a screen is
collecting: `LocationProvider.updates` is a `callbackFlow` that removes its listener on
cancellation, and `DriverViewModel` cancels the job when availability goes false. There is
no foreground service and no background-location permission. That is a deliberate limit —
background tracking costs battery the driver needs for their shift, and it is not a
reasonable thing to run on someone who has gone off duty.

**Ratings.** A passenger rates a completed ride, and the star lands in
`driverRatings/{driver}/{rater}` — under the rater's own key, which is the only shape a
security rule can restrict to the person writing it. The driver record is writable by the
driver alone, so a passenger cannot be the one to move the average there.

The driver's own app closes the loop: it collects `driverRatings/{self}`, averages it, and
writes `rating` and `ratingCount` back onto its own record, which is what the admin
screens and the exported reports read. That means a rating reaches the admin's view when
the driver next opens the app rather than the instant it is given. The alternative is
letting passengers write to driver records, and a few hours of lag is the better trade.
A server-side function would remove the lag, and is out of scope for the same reason
push-to-a-sleeping-phone is.

`totalRides` has no such problem — the driver is the one completing the ride, so their own
device increments it, under a transaction so two devices finishing at once cannot lose a
count.

**Consent.** `MainAppScreen` will not route to a dashboard until `ConsentViewModel`
confirms the account has accepted the current documents. `users/{uid}` carries
`acceptedLegalVersion` and, for drivers, `acceptedDriverAgreementVersion`, both compared
against `Constants.LEGAL_VERSION`. A read failure is treated as "not accepted" rather
than waved through, since guessing in the permissive direction is the wrong default for a
consent check. Registration writes the first of these, because the sign-up form already
required the tick.

**Session.** Firebase keeps the user signed in across restarts. `AuthViewModel`
constructs with `hasExistingSession` set synchronously from `repo.currentUserId`, so the
first frame can already tell whether to show the welcome artwork instead of flashing the
wrong thing. Signed-out start-up shows no loading screen at all: there is nothing to wait
for, so it routes straight to onboarding or sign-in. `bootstrap()` then loads the role, guarded by a twelve-second
timeout: an unreachable database used to leave the app on a spinner forever.

A session must not outlive the installation. Firebase Auth persists it in shared
preferences, and Android's default backup rules copy those to the cloud and restore them,
so uninstalling and reinstalling used to bring a user back already signed in — and a
cloud restore could carry the session onto a different phone. `allowBackup` is therefore
false and `data_extraction_rules.xml` excludes everything from both cloud backup and
device-to-device transfer. Both are needed: `allowBackup` governs only the cloud path,
and on some manufacturers' devices a D2D transfer proceeds regardless. Nothing is lost —
the only local state is the remembered email and the onboarding-seen flag.

**Reports.** `ReportBuilder` buckets rides by month or year from epoch-millisecond
strings. A timestamp that will not parse is excluded from every period rather than
silently landing in the current one.

The months and years on offer are built from rides that exist, so there are no empty
periods to scroll past. `ReportPeriod.Custom` covers anything those two do not: two dates
from a calendar, both days included in full. Material's range picker reports a selection
as midnight UTC, so `customRange` reads the calendar date back in UTC and rebuilds the day
locally — treating the picker's instant as a local one shifts the range a day in every
zone behind UTC. A range picked back to front is ordered rather than rejected.

Charts follow the period's length rather than its type: `bucketsByDay` puts one bar per
day on a month or a range up to nine weeks, and one per month on anything longer. Labels
carry the year when a span crosses new year, or two Januaries read the same. `ReportExporter` writes through
`ActivityResultContracts.CreateDocument`, so no storage permission is needed, or shares
through a `FileProvider` URI.

Each report exports two ways. The spreadsheet is `ReportBuilder`'s CSV. The PDF is drawn
by `PdfReportWriter` on Android's own `PdfDocument`, which means no library and no cost:
Letter landscape, a summary page of stat tiles and four charts, then the detail table
paginated behind it with its header repeated. Landscape because the ride table has nine
columns and portrait would mean either an unreadable font or dropping columns.

`PdfChart` draws the marks. Every chart is a single series in one hue — a bar chart of
unordered categories has nothing for a second colour to mean, and colouring each bar by
its own size only re-encodes the length already there. That also sidesteps the
colour-blindness problem of several hues side by side. Bars carry their value as a direct
label, since paper has no tooltip. Bar thickness scales with the slot: four categories
across half a landscape page would otherwise draw as stray ticks, while a month of days
still lands on the base width.

Rendering runs on `Dispatchers.IO` with the buttons disabled behind a flag. A busy month
takes long enough to draw that doing it on the main thread would freeze the screen. Only
the share chooser goes back to the main thread, because starting an activity requires
it.

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

Release signing follows the same shape. `build.gradle.kts` reads `RELEASE_STORE_FILE`,
`RELEASE_STORE_PASSWORD`, `RELEASE_KEY_ALIAS` and `RELEASE_KEY_PASSWORD` from `.env`, and
only registers a `release` signing config when all four are present and the keystore file
actually exists. When they are not, the release build type falls back to the debug config
and a `whenReady` hook logs a warning naming the missing variables. The alternative —
failing the build outright — would stop a fresh clone from compiling at all, and the
alternative to that, signing silently with the debug key, is how an undistributable APK
gets handed to testers.

Shrinking is off. R8 removes the constructors and fields Firebase needs to map a snapshot
onto a data class, and the failure is quiet: reads return empty records rather than
crashing. `proguard-rules.pro` carries the keep rules that make it safe, so enabling it is
one line plus a test against a real database.
