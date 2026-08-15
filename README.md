# TrikRide

An Android application that connects passengers with tricycle drivers serving Talibon
Polytechnic College, and that checks a driver's credentials before letting them carry
anyone.

A BSIS capstone project by Alber June M. Mumar, Julebeth Hinlayagan, and Mardy Gonzaga,
Department of Information Systems, Talibon Polytechnic College, San Isidro, Talibon,
Bohol.

## The problem

Tricycle service around the campus runs on physical presence. A passenger walks to a
terminal or waits at the roadside with no idea whether a ride is coming. A driver roams
or queues with no idea who needs one. Both are waiting, neither can see the other.

Fares are fixed by a FeTODAT ordinance and posted on a laminated sheet at the terminal,
which is not where anyone is standing when they decide to travel. And because nobody
checks a licence before a passenger gets in, a complaint has nowhere to go.

## What the app does

**Passengers** pick a destination from the 240 stops on the published fare schedule, see
the exact fare before agreeing to it, and watch the ride from acceptance to arrival.
Senior citizens, persons with disabilities, and students get the discounted rate column
from the same schedule.

**Drivers** register, submit a licence and tricycle number, and wait for an administrator
to approve them. Until then they cannot accept anyone. Once approved they go online,
see requests with a countdown, accept, and move the ride through its stages.

**Administrators** verify drivers, keep the fare table correct, answer concerns, watch
activity live, and export a month, a year, or any range of dates. Each report comes out as a printable
PDF — headline figures and charts on the first page, every record behind it — or as a
spreadsheet for anyone who wants to sort and total it themselves.

## Agreements

Four documents ship inside the app: the Terms and Conditions, the Privacy Policy, the
Safety and Community Guidelines, and a Driver Agreement. The first three are ticked at
sign-up. Drivers are asked for the fourth once they pick a driver account.

Nobody reaches a dashboard without agreeing. A consent screen sits between sign-in and
the app, and it only lets you past when every document has been separately ticked; the
alternative is signing out. What was accepted is stored on the account against a version
string, so an account created before this existed gets asked on its next launch, and
bumping `Constants.LEGAL_VERSION` when the documents are revised asks everyone again.

## Fares

The app does not estimate. It looks up the posted rate for the destination, raises it to
the ordinance minimum if the posted rate is lower (₱15 regular, ₱12 discounted), and
multiplies by the number of passengers.

The rate table lives in the database, not in the code, so a wrong price is fixed in the
admin screen rather than in a new release. `FareSeed.kt` carries the transcribed
schedule, which the administrator loads once through the Fares tab.

Forty-six of the 240 entries are flagged for verification: rows that could not be read
cleanly from the posted sheet, rows two transcription passes disagreed on, and four rows
where the discounted rate came out higher than the regular one. Three entries are
switched off entirely because no usable rate could be read, including one that
transcribed as ₱740 against neighbours in the ₱20–100 range. A flagged entry still
prices rides; a disabled one cannot be selected at all.

## Built with

Kotlin 2.1 and Jetpack Compose with Material 3, against Firebase Authentication,
Realtime Database, and Cloud Messaging. MVVM with a repository layer. Minimum
Android 7.0, target Android 14.

The whole thing runs on Firebase's free Spark plan with no card on file. Cloud Storage
would need the paid plan, so profile photos are shrunk to 256 pixels square, compressed,
and kept in the database instead — a few kilobytes each.

There is no server to run. Firebase provides the backend, which is deliberate: a project
handed to a municipal drivers' association cannot depend on a machine somebody has to
keep alive.

## Releasing

`./gradlew assembleRelease` produces a signed APK once four `RELEASE_*` values are set in
`.env`. Without them it signs with the debug key and Gradle warns that the output must not
be handed out, so nobody ships a build by accident that can never be updated.

Generate the keystore once, back it up somewhere that is not your laptop, and never
commit it — `*.jks` and `*.keystore` are gitignored. Full steps are in
[SETUP_GUIDE.md](SETUP_GUIDE.md#deployment).

## Getting it running

See [SETUP_GUIDE.md](SETUP_GUIDE.md). Two files are not in this repository and you will
need both:

- `app/google-services.json`, downloaded from your Firebase project
- `.env`, copied from `.env.example` and filled in

Without them the build fails at the Google Services step or the app cannot reach the
database.

## Layout

```
app/src/main/java/com/tpc/trikride/
  models/         data classes: User, Ride, FareStop, Complaint, AppNotification
  services/       FirebaseService — the only place that touches the database
  repositories/   six repositories between the ViewModels and the data
  viewmodels/     one per role, plus auth, profile, and support
  ui/screens/     Compose screens for passenger, driver, and admin
  ui/components/  shared components
  ui/theme/       colours, typography, light and dark themes
  utils/          FareEngine, FareSeed, ReportBuilder, PdfReportWriter, PasswordRules
docs/
  capstone_manuscript.md              the written documentation, in Markdown
  TrikRide_Capstone_Documentation.docx  the same, built for submission
  figures/                            generated diagrams
```

## Maps and live tracking

Booking and ride tracking both show a real map. Set `MAPS_API_KEY` in `.env` and it uses
Google Maps; leave it blank and it draws OpenStreetMap through osmdroid, which needs no key
and no billing account. Both are kept because a Google key dies with its billing account,
and clearing one line to fall back beats a blank screen.

A driver publishes their position while they are online and the app is open — no
background service and no background-location permission, so nothing is tracked when they
are off duty. The passenger sees that position move on the tracking screen once a driver
accepts.

Pickup can be chosen from the campus list or pinned anywhere on the map, with the point
reverse-geocoded to a readable label. Destinations stay on the posted fare table, because
the fare is fixed per named stop by ordinance and must not depend on where someone drops a
pin. Stops can carry coordinates so they appear on the map; the admin fills those in over
time and a stop without them still books normally.

## Not in this build

Push notifications to a phone that is not running the app. FCM is wired up, but sending
requires Cloud Functions.

Online payment. Cash only, which is what the study scoped.

Google Sign-In. Email and password only.

## Documentation

The full capstone manuscript is in `docs/`. Chapters 1 through 4 are complete; the
evaluation results in section 4.9 and the conclusions in Chapter 5 are left as marked
placeholders because the data have not been collected. Nothing in there is invented.
