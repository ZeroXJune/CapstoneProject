# TrikRide User Manual

Version 1.0 · For the TrikRide Android application

This manual covers the three kinds of account — passenger, driver and administrator —
and is written to be read in parts. A passenger needs Part 1 and Part 2 and nothing else.
Part 5 lists the problems people actually run into and what to do about each.

Screenshots of every screen described here are collected in Appendix G of the capstone
documentation.

---

## Part 1 — Installing the app

TrikRide is not on the Play Store. It is distributed as an installation file, an APK,
which you receive from the administrator by link or by direct transfer.

1. **Get the file.** Download it, or accept it from the administrator. It arrives in your
   Downloads folder.
2. **Allow the installation.** Tap the file. Android will refuse the first time and offer
   a settings screen: turn on **Allow from this source** for whichever app you are
   installing from, usually Files or Chrome. Go back and tap the file again. This is
   normal for an app distributed outside the store and only has to be done once.
3. **Install.** Tap Install, then Open.
4. **Permissions.** The app asks for location the first time you use a screen that needs
   it, and for the camera the first time you take a photograph. Both can be declined:
   without location you can still book by choosing your pickup from the list, and without
   the camera you can still choose a photograph from your gallery.

An update is installed the same way, over the top of the existing app. Your account and
your history are on the server, not on the phone, so nothing is lost.

---

## Part 2 — For passengers

### 2.1 First launch

Five introductory slides appear the first time the app opens. They are shown once. Swipe
through or skip.

### 2.2 Creating an account

From the sign-in screen, tap **Register** at the bottom, then fill in:

| Field | Notes |
|:---|:---|
| Full Name | As you want a driver to see it |
| Birthdate | Opens a calendar; you cannot type it |
| Email | The reset link goes here, so use one you can open |
| Phone Number | The driver may call this to find you |
| Password | Checked as you type — see below |
| Confirm Password | Must match |

The password rules appear under the field as you type and tick off one by one:

- at least 8 characters
- an uppercase letter
- a lowercase letter
- a number
- a symbol — recommended, not required

Then tick the box confirming you have read the **Terms and Conditions**, the **Privacy
Policy** and the **Safety and Community Guidelines**. Tap any of the three titles to read
it before you agree; they are also available afterwards from Profile.

Tap **Next**, choose **Passenger**, and the account is created.

### 2.3 Signing in

Enter your email and password and tap **Login**. Tick **Remember me** and the email is
filled in next time; the password never is.

You stay signed in between launches, so the app opens straight to your dashboard. Signing
out is in Profile.

**Forgotten password.** Tap **Forgot Password?**, confirm your email address, and tap
**Send link**. Firebase emails you a link to set a new password. If nothing arrives within
a few minutes, check the spam folder — and check the address is the one you registered
with, because for privacy the app gives the same reply whether or not an account exists.

### 2.4 Agreeing to the documents

If the documents have been revised since you last accepted them, a screen appears between
signing in and your dashboard. Each document has to be ticked separately. The only
alternative is signing out. This also happens the first time an older account opens a
version of the app that records consent.

### 2.5 The dashboard

Four tabs along the bottom: **Home**, **History**, **Support**, **Profile**. The bell at
the top right opens your notifications and carries a count of the unread ones.

### 2.6 Booking a ride

Tap **Book a Ride** on Home.

1. **Pickup.** Tap the pickup field and search the list of posted stops by name or by
   zone. If where you are standing is not on the list, tap **Not on the list? Pin it on
   the map**, move the map until the pin is on the right corner, and tap **Use this
   point**. **Use my current location** moves the pin to where you are now, which is not
   always where you want to be collected.
2. **Destination.** Tap the destination field and search the same way. Type any part of
   the name or the zone — several words all have to match, so "poblacion talibon" and
   "market balintawak" both work. The fare appears against every result.
   If you do not know what the fare sheet calls where you are going, tap **Don't know the
   name? Find it on the map**, move the map to roughly the right place, and the app names
   the nearest posted stop and its fare. Only stops the administrator has given a map
   position can be found this way.
3. **Who is travelling.** Two counters, not one. Count regular passengers on the first
   and seniors, persons with disabilities and students on the second, up to five between
   them. A party can be a mix — two friends and a grandmother is two regular and one
   discounted — and each is charged from its own column of the posted sheet. Bring the
   identification; the driver will ask for it, and the app only records how many of each
   were declared.
4. **Luggage.** Tap any that apply. This is information for the driver and does not change
   the fare.
5. **Notes.** Anything the driver should know: a landmark, a gate number, that you are
   waiting under the awning.
6. Read the itemised fare, then tap **Find a Driver**.

**About the fare.** TrikRide does not estimate. It reads the posted FeTODAT rate for your
destination, raises it to the ordinance minimum if the posted rate is lower, and charges
each passenger from the column that applies to them. Two regular passengers and one senior
are priced as two at the regular rate plus one at the discounted rate, and the fare card
shows those two lines separately before you book. Poblacion and the terminal round trip
are flat rates. Nothing is calculated from distance, so the number you are shown before
booking is the number you pay, in cash, directly to the driver.

**₱25 is the minimum, not the price.** It is what the shortest rides cost and what any
posted rate below it is raised to. Further destinations cost more — the highest on the
posted schedule is ₱150 — so a ride is only ₱25 if the schedule says so for the place you
are going. The screen tells you which before you commit to anything, and the picker shows
the price beside every destination.

### 2.7 Waiting for a driver

The request goes to every online, verified driver at once, and the first to accept gets
it. If nobody accepts within five minutes it expires and you can send it again. **Cancel
Request** withdraws it before then.

If nothing happens, it usually means no drivers are online rather than that anything is
broken.

### 2.8 During the ride

Once a driver accepts, the screen becomes a status timeline that both of you see:

**Driver accepted** → **Driver is on the way** → **Your driver has arrived** → **Ride in
progress** → **Completed**

The driver advances it; your screen follows without your doing anything. Their name,
tricycle number and rating are shown, with a button that dials them — useful if you are
hard to find, or if the place you are going is not quite what the fare sheet calls it. A
driver nobody has rated yet says so rather than showing a score. While they are online
with the app open, their position moves on the map.

What you agree by telephone does not change the fare. The app charges the posted rate for
the stop you booked, and that is what you owe.

### 2.9 Rating and history

When the ride completes you are asked to rate the driver from one to five stars. Tap the
stars, then **Send rating** — tapping a star does not send it, so a slip is not a rating.
You can skip it, but only once: a ride can be rated one time.

**History** lists your past rides, most recent first, with the fare and how each ended.

### 2.10 Reporting a concern

**Support** has a form: choose a category — driver behaviour, wrong fare, safety concern,
app problem, or other — describe what happened, and tap **Submit Report**. An
administrator reviews it, and any reply appears under **My Reports** on the same tab, along
with whether it is still open, in review, or resolved.

The hotline, email address and support hours are at the bottom of the same tab. Use the
hotline, not the app, for anything urgent.

### 2.11 Your profile

**Profile** holds:

- **Edit Profile** — name, phone, and your photograph, from the camera or the gallery.
  After choosing one you can pinch to zoom and drag to position it inside the circle, so
  you decide which part is used.
- **Change Password** — emails you a reset link.
- **Dark Mode** — a switch.
- The four documents, readable at any time.
- **Log Out.**

---

## Part 3 — For drivers

### 3.1 Registering

Register exactly as a passenger does, but choose **Driver** at the account type step. You
are then shown the **Driver Agreement**, which has to be accepted before you go any
further.

Next comes the driver form:

| Field | Notes |
|:---|:---|
| Driver's License Number | As printed on the licence |
| License Expiry | MM/YYYY |
| Tricycle Body / Plate Number | The number on your unit |

Tap **Submit for Verification**.

### 3.2 The licence photograph

A card on your dashboard asks for a photograph of your licence. Take one, or choose one
from your gallery, and read the consent step before sending it: it names what the
photograph is for, who can see it, and when it is destroyed. You send it only after
agreeing to that specifically.

What happens to it, in short: only you and an administrator can ever see it; it is never
shown to a passenger and never appears in a report. If your application is refused it is
deleted at that moment. If you are approved it stays while your account is active, because
it is needed again when your licence expires. You can remove it yourself at any time from
your Profile tab, though without one on file you cannot be approved to carry passengers.
Section 9 of the Privacy Policy sets this out in full.

Once the photograph has been sent, the card moves off the dashboard and lives in Profile.

### 3.3 While you wait for verification

Your dashboard shows **Verification: PENDING**. You cannot accept passengers until an
administrator approves you, and requests will not appear. The decision arrives as a
notification. If you are rejected, the reason is in that notification and your licence
photograph is deleted along with the refusal.

### 3.4 Going online

The dashboard has a switch, and a **Go Online** button that does the same thing. Online
means requests reach you and your position is published to the passenger of any ride you
are on. Offline means neither.

Your position is published **only while you are online with the app open**. There is no
background tracking. Close the app and your passenger stops seeing you move, so keep it
open on the way to a pickup.

### 3.5 Taking a ride

Requests arrive on the **Requests** tab, which carries a count. Each card shows the
pickup, the destination, the fare, the rate column, how many passengers, what luggage, and
any note. Every online driver sees the same request; the first to accept gets it and it
disappears for everyone else, so accept promptly. An unaccepted request expires after five
minutes.

Once you accept, your Dashboard becomes the active ride. Advance it with the button as you
go:

**Accepted** → **Heading to pickup** → **Arrived at pickup** → **Ride in progress** →
**Completed**

The passenger's screen follows each step. Marking a ride completed records the fare and
asks the passenger to rate you.

The active ride shows the passenger's name and a button that dials them. Use it if you
cannot find them, or if their note describes somewhere the fare sheet does not list and
you need to agree what is actually meant. The fare stays what the app calculated for the
stop they booked — the call is for finding each other, not for renegotiating.

**Navigation.** TrikRide does not give turn-by-turn directions itself. Where the point it
is sending you to has a map position, the active ride shows **Waze** and **Google Maps**
buttons that open whichever of those you have with the route already started — the pickup
before the passenger is aboard, the destination once the ride is under way. The buttons
only appear for apps you actually have installed, and only when the point has coordinates,
which for a destination means the administrator has positioned that stop.

Check identification against the party the request declares. A card reading "2 regular,
1 senior/PWD/student" means one person aboard should be able to show an ID. The app
records what was declared; it cannot verify it.

### 3.6 Earnings, history and the rest

The dashboard shows today's earnings. **History** lists every completed and cancelled trip
with a running total. Your rating and trip count are at the top of the dashboard and on
your Profile tab.

**Support** works the same way it does for a passenger: a form for reporting a concern,
your own past reports and their status, and the hotline.

**Profile** holds your credentials card, your licence photograph, the profile photo
picker, dark mode, the four documents including the Driver Agreement, and Log Out.

---

## Part 4 — For administrators

An administrator account is not created in the app. An existing account is promoted by
setting its `userType` to `ADMIN` in the Firebase console — the security rules deliberately
stop an account from doing that to itself. Sign out and back in afterwards.

Five tabs: **Verify**, **Concerns**, **Monitor**, **Fares**, **Profile**.

### 4.1 First run: load the fare table

Nothing can be booked until this is done once.

1. **Fares** → **Load official rates**. This writes all 240 transcribed FeTODAT stops in
   one request.
2. Open the **minimums and flat rates** dialog and check the four values: minimum regular
   ₱25, minimum discounted ₱20, Poblacion flat ₱25, terminal round trip ₱25. Loading the
   rates leaves these four alone on purpose, so that corrections survive a reload — which
   also means a database seeded earlier keeps whatever it had, and reloading will not put
   it right.
3. Tap the **Needs review** chip. Forty-six entries are flagged as uncertain from the
   transcription and three are switched off because no rate could be read. Each one opens
   with the specific problem at the top of the dialog. Work through them against the
   physical posted sheet before anyone uses the app for real.

You can also search the table by stop or zone, filter by zone, edit any entry, switch one
off, add one, or delete one. Editing an entry clears its review flag.

**Map positions.** Open a stop and tap **Set it on the map**, then drag until the pin sits
on the place and confirm. There is no need to type coordinates. A stop without a position
books and prices normally; it just does not appear on the map, cannot be found through
"Find it on the map", and cannot be handed to a driver's navigation app. Filling in the
common destinations is worth doing, and is what turns those two features on.

### 4.2 Verifying drivers

**Verify** carries a badge with the number waiting.

Each pending driver shows their name, contact details, licence number, expiry and tricycle
number. Tap **View licence details** to see the photograph they submitted alongside the
details they typed, and check that the two agree.

- **Approve** — they can go online and start accepting.
- **Reject** — the application is refused and the photograph is deleted with it.

Either way the driver gets a notification. An approved driver can be reversed later with
**Revoke Approval**, which stops them accepting anyone; that does not delete the
photograph, since the reason for revoking may itself need to be evidenced.

Below the queue is every registered driver with their current state.

### 4.3 Concerns

**Concerns** carries a badge with the number still open. Each shows who filed it, whether
they are a passenger or a driver, the category, and what they wrote.

**Respond** opens a note field. Write what you are doing about it and mark it **In review**
or **Resolve**. The note is visible to the person who filed it, under My Reports on their
Support tab.

### 4.4 Monitor

Two sub-tabs.

**Live** shows drivers and their verification states, and recent rides with the route,
fare and status, updating as they happen.

**Reports** is the export screen. Choose what the report covers from the **Covering**
dropdown: all time, any year, any month, or **Choose exact dates…** for a range picked
from a calendar. The list of months and years is built from the rides that exist, so there
are no empty periods to scroll past.

The summary for the chosen period is on the screen — total rides, completed, cancelled,
still open, gross fares, average fare, passengers served, drivers with a ride, and concerns
filed and resolved. You can read it without exporting anything.

Three reports can be exported for that period:

| Report | Contents |
|:---|:---|
| Ride activity | Every booking with fares, status and both parties |
| Driver performance | Rides, completions and gross fares per driver |
| Concerns and complaints | What was filed, the categories, how each was closed |

Each offers four buttons: **Save PDF**, **Send PDF**, **Save sheet**, **Send sheet**. The
PDF is landscape, and leads with the headline figures and four charts before the records
behind them — that is the one to print or hand over. The spreadsheet is the same data for
sorting and totalling.

Saving asks where to put the file. Sending hands it to whatever is on the phone — email,
Messenger, Drive.

---

## Part 5 — When something goes wrong

**"Couldn't reach the database."** The phone has no working connection, or the Realtime
Database has not been created in Firebase yet. Check the connection first. If a fresh
installation shows this on every screen, it is the second one, and the administrator needs
to check the Firebase setup and the security rules.

**No drivers appear to be online.** Almost always exactly that: no driver has gone online.
A driver who has closed the app is not online even if they were a minute ago. An
unverified driver never receives requests.

**The request expired.** Five minutes passed with nobody accepting. Send it again.

**A fare looks wrong.** The app charges the posted rate for the destination, raised to the
minimum where the posted rate is lower. Forty-six entries in the transcribed table are
flagged for checking and twenty-seven sit at ₱20 and are charged at the ₱25 minimum. Report
it under **Wrong fare** with the pickup and destination and the administrator can correct
that entry — no update to the app is needed.

**"That image could not be processed."** The file is not a picture the phone can decode,
or it is a format the gallery listed but cannot open — this happens with some downloaded
images. Take a fresh photograph with the camera instead.

**The licence upload is refused.** Either the security rules for `driverDocuments` have not
been published, which is the administrator's to fix, or the photograph is too large. A
photograph taken with the camera in the app is already reduced; one chosen from the gallery
is reduced too, but a very large file can still fail.

**The map is blank.** Tiles are downloaded, so a slow or absent connection leaves the map
empty while everything else works. It will fill in.

**The pinned pickup shows numbers instead of a place name.** Most of Talibon has no street
address on record, so the app names the barangay and municipality instead. Where even that
is unavailable it falls back to the coordinates, which a driver can still navigate to from
the pin. Add a landmark in the notes if the label is not something you would say aloud.

**The driver's position is not moving.** They have closed the app or gone offline. There is
no background tracking, by design.

**Signed out unexpectedly.** The documents were revised, or the account was signed out on
another device. Sign in again; nothing is lost.

**The password reset email never arrives.** The app confirms that Firebase accepted the
request, which is not the same as the mail reaching you. Check the spam folder first: the
message comes from a `firebaseapp.com` address and is filtered often. Then check the
address the confirmation named is one you can actually open — a mistyped address at
registration will never receive anything. If neither explains it, the administrator should
open Firebase Console, Authentication, Templates, and confirm the password reset template
is enabled for the project.

**Anything urgent.** Use the hotline on the Support tab. Do not wait on a report in the
app.
