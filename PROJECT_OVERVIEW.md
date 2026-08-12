# Project status

Where TrikRide stands, what is finished, and what is still open. Written for whoever
picks this up next, including us in a month.

## Finished and on the branch

**Accounts.** Registration with full name, birthdate through a date picker, email, mobile
number, and a password checked against a live rule list as it is typed. Terms and privacy
consent is required before the account is created. Sign-in remembers the email if asked.
Firebase keeps the session, so the app opens straight to the dashboard on the next
launch. Password reset by email.

**First run.** A five-slide carousel on first launch, shown once and never again.
Returning users get the welcome artwork while the session restores.

**Booking.** Pickup from a fixed list of campus points. Destination from a searchable,
zone-filtered list of the 240 posted FeTODAT stops plus the two flat rates. A rate column
switch for regular versus senior, PWD, and student. One to five passengers, luggage
chips, and a free-text note. The fare appears itemised as soon as a destination is
chosen.

**Matching.** The request goes to every online, verified driver with a countdown. The
first to accept gets it and it disappears from everyone else. Unaccepted requests expire
after five minutes.

**The ride.** A shared status timeline through arriving, arrived, in progress, and
completed, driven by the driver and reflected on the passenger's screen without either
refreshing. A completion summary with a rating. History on both sides.

**Driver onboarding.** Licence number, expiry, and tricycle number submitted at
registration. An administrator approves or rejects. An unapproved driver cannot accept
anyone, and the decision arrives as a notification.

**Admin.** Verification queue with a badge. Concerns with a badge, a status, and a note
field. A live monitor of drivers, verification states, and rides. The fare table with
search, zone filters, a review queue, per-entry editing, activation, addition, deletion,
and a dialog for the minimums and flat rates. Monthly, yearly, and all-time exports for
rides, drivers, and concerns.

**Everywhere.** Profile editing with a photo from camera or gallery, dark mode, terms and
privacy text, an in-app notification centre with unread counts, skeleton loading,
pull-to-refresh, and a concern form for passengers and drivers.

## What is not built, and why

**Live maps.** Map panels are styled placeholders. Displaying a Google map on Android
carries no charge, but a billing account is still required to obtain a key, so this waits
until deployment. The fare stops would need coordinates attached first.

**Push to a sleeping phone.** FCM is wired into the app, but delivering a notification
when the app is not running needs a server-side sender. That is a small set of Cloud
Functions, deferred.

**Google Sign-In.** Needs a Firebase console step and a SHA-1 fingerprint. Deferred.

**Online payment.** Out of scope by the study's own limitations. Cash only.

**Release signing.** The project builds debug APKs. A release keystore and a signing
config reading from `.env` are needed before anything is distributed, and the keystore
must be backed up somewhere that is not one laptop.

## What still needs a person, not a commit

**The fare data.** Forty-six of the 240 entries are flagged. Open Fares, tap the "Needs
review" chip, and work through them against the physical posted sheet. Four of them price
the discount above the regular rate, which is backwards and should be fixed before anyone
real uses the app. Three entries are switched off because no rate could be read; they
need a number or removal. One of those, San Roque "Dina/Gabril", looks like a duplicate of
"Gabriel & Dina", which is already in the table at ₱36/₱40.

**Cloud Storage.** Profile photo uploads need it enabled in the Firebase console. New
Firebase projects require the Blaze plan for Storage, which wants a card even though the
free allowance covers this app several times over. If that is not wanted, drop remote
photos and keep the picked image on the device.

**`.env`.** Copy `.env.example` and fill it in. The build reads it.

**Evaluation.** No survey data has been collected. The manuscript's section 4.9 and
Chapter 5 carry the tables and the interpretation scale, with the numbers left blank and
marked as such. Nothing there is invented and nothing should be.

## Documentation

`docs/capstone_manuscript.md` is the written documentation in Markdown, and
`docs/TrikRide_Capstone_Documentation.docx` is the same thing built for submission,
following the department's required format: preliminaries, five chapters, references in
APA 7, and appendices A through Q.

Chapters 1 through 4 are complete. Section 4.9's results tables, Chapter 5's summary and
conclusions, and several appendices (raw data, statistical computation, screenshots,
signed permission letters, photographs of the data gathering) are marked as placeholders
because they depend on work that has not happened yet.

Fourteen figures are generated from text in `docs/figures/`: research flow, conceptual
framework, system architecture, context diagram, DFD level 1, ERD, use case, activity,
sequence, class, database schema, waterfall, screen flow, and the Gantt chart. Changing
one means editing a script, not redrawing a picture.

## Rebuilding the .docx

Edit `docs/capstone_manuscript.md`, then run pandoc against a reference document with the
capstone's page setup. The `[[PB]]` markers in the Markdown expand to Word page breaks,
and pandoc's `[Content_Types].xml` needs a PNG default added afterwards or the file will
not open. The build steps are recorded in the commit that introduced the manuscript.

## Known rough edges

The onboarding slides are around 316 pixels wide and will look soft stretched to a
1080-pixel screen. Slides four and five have a strip of a neighbouring image along the
top edge, cropped from a contact sheet a few pixels low. Replacements can be dropped over
the same filenames in `app/src/main/res/drawable-nodpi/` with no code change.

No automated tests exist. The test tables in the manuscript document tests that were run
by hand. Converting the `FareEngine` and `ReportBuilder` cases into JUnit tests would be
straightforward and worth doing; they are pure functions with no Android dependency.
