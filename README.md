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
activity live, and export a month or a year of it as a spreadsheet.

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
  utils/          FareEngine, FareSeed, ReportBuilder, ReportExporter, PasswordRules
docs/
  capstone_manuscript.md              the written documentation, in Markdown
  TrikRide_Capstone_Documentation.docx  the same, built for submission
  figures/                            generated diagrams
```

## Not in this build

Live maps. Map areas are styled placeholders. Displaying a Google map on Android is not
billed, but a billing account is still needed for a key, so this waits for deployment.

Push notifications to a phone that is not running the app. FCM is wired up, but sending
requires Cloud Functions.

Online payment. Cash only, which is what the study scoped.

Google Sign-In. Email and password only.

## Documentation

The full capstone manuscript is in `docs/`. Chapters 1 through 4 are complete; the
evaluation results in section 4.9 and the conclusions in Chapter 5 are left as marked
placeholders because the data have not been collected. Nothing in there is invented.
