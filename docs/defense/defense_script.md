# TrikRide — Oral Defense Script

**A Smart Tricycle Ride and Driver Onboarding System for Talibon Polytechnic College**
Talibon Polytechnic College · 23 September 2026

**Presenters** — Alber June M. Mumar (lead) · Julebeth Hinlayagan · Mardy Gonzaga

**Target length** — 18 minutes of presentation, then Q&A.

| Speaker | Slides | Approx. time |
|:---|:---|---:|
| **Alber** | 1, 3, 4, 5, 11, 12, 14, 15 | ~11 min |
| **Julebeth** | 2, 9, 13 | ~3.5 min |
| **Mardy** | 6, 7, 8, 10 | ~3.5 min |

Read this aloud twice before the defense. Where a line feels wrong in your mouth, change it — a script you can't say naturally is worse than no script.

[[PB]]

## Slide 1 — Title · ALBER · 45 sec

Good morning to our panel, to our adviser, and to everyone here.

I am Alber June Mumar, and with me are Julebeth Hinlayagan and Mardy Gonzaga. We are presenting our capstone project: **TrikRide, a Smart Tricycle Ride and Driver Onboarding System for Talibon Polytechnic College.**

It is an Android application that connects passengers with verified tricycle drivers around our campus.

Julebeth will begin with the problem we set out to address.

## Slide 2 — Background of the Study · JULEBETH · 1 min 30 sec

Thank you, Alber.

Students, faculty and staff of this college depend on tricycles every day. But the way the service works around the campus has not changed.

A passenger walks to a terminal, or waits at the roadside, with no way of knowing whether a tricycle is coming. A driver circles the area looking for passengers, with no way of knowing who needs a ride. Neither side can see the other.

Look at the current process on the screen. The passenger looks for a driver, waits, asks the fare only when boarding, and then rides. Every step is manual.

That gives us four problems. **Waiting time**, because neither side can see the other. **Manual coordination**, because there is no channel between them. **Fare uncertainty**, because the passenger learns the price at the point of boarding rather than before deciding. And **driver onboarding**, which at present has no systematic way to check who is driving.

These four problems are what TrikRide was built to address. Alber will present the system.

## Slide 3 — The Proposed System · ALBER · 1 min

Thank you, Julebeth.

TrikRide is a mobile system that connects passengers and verified tricycle drivers through a structured digital request.

It does three things. **Ride booking** — the passenger requests a ride and sees the fare before committing to it. **Driver onboarding** — a driver submits credentials, and an administrator verifies them. **Ride coordination** — available drivers receive the request, one accepts, and the passenger follows the status until the ride is complete.

One point on fares, because it separates our system from the ride-hailing applications in our literature. We do not estimate fares and we do not compute them by distance. Every fare comes from the schedule published by the Federation of Tricycle Operators and Drivers Association of Talibon — the FeTODAT schedule — with its regular and discounted columns. In Talibon the fare is fixed by the association's published rates, so a system that priced by distance would not be lawful here. Our system follows the posted schedule.

## Slide 4 — Objectives · ALBER · 1 min

Six objectives guided the work.

One, provide a digital process for requesting rides. Two, register and verify tricycle drivers. Three, connect ride requests with available drivers. Four, help improve passenger–driver coordination and reduce waiting time. Five, maintain ride status and activity records. And six, evaluate the system for usability, efficiency and user satisfaction.

The first five are about what we built. The sixth is about how it was received, and I will come back to it in the results.

Now let me show you the system itself.

## Slide 5 — Live Demonstration · ALBER · 3 min

*[Play the recorded walkthrough. Narrate over it — do not read this word for word, but cover these points in this order.]*

This is the passenger side. The passenger signs in and selects a destination from the fare table. Notice that **the fare appears here, before the request is sent** — this is the fare transparency we described.

The passenger submits the request. On the driver's device, the request now appears with a countdown. The driver accepts.

Once accepted, both devices show the same ride. The passenger sees the driver's name, contact and tricycle details. The status moves through acceptance, arrival, in-transit, and completion. Both sides see the same status at the same time, because both are reading the same record.

Now the driver side of onboarding. A new driver registers and submits licence and tricycle details, together with a photograph of the licence.

Here is the administrator's view. The application appears in the verification queue. The administrator reviews the credentials and approves.

This is the part I want the panel to note: **before approval, this driver cannot accept a passenger.** The restriction is enforced in the database rules, not only in the interface — so it holds even if someone modified the app. That is objective two, working.

Mardy will take you through the features and the technical design.

## Slide 6 — Key Features · MARDY · 1 min

Thank you, Alber.

The features are grouped by role, and each role sees only what belongs to it.

The **passenger** has registration and profile, destination selection, fare display, ride request, driver information, ride status and history, and a channel for raising concerns.

The **driver** has registration, credential submission, an availability toggle, incoming ride requests, acceptance, ride management, and history.

The **administrator** has driver verification, fare table management, ride monitoring, concern management, and records and reports.

## Slide 7 — Roles · MARDY · 45 sec

Three roles meet in one shared real-time database.

The **passenger** requests rides, checks fares, views driver information and monitors status. The **driver** registers, submits credentials, goes online once verified, and accepts requests. The **administrator** verifies drivers, manages fares, reviews concerns, monitors records and generates reports.

They see different screens, but they are reading and writing the same data. That is why a status change on one device appears on the other.

## Slide 8 — System Architecture · MARDY · 1 min 15 sec

This is how it is built.

The client is an **Android application written in Kotlin using Jetpack Compose**. It follows the **Model-View-ViewModel** pattern, with a repository layer between the interface and the data.

Why that layer matters: the screens do not talk to the database directly. Everything goes through the repository and one Firebase service class. So if the data source changes, we change one layer, not every screen.

The backend is **Firebase** — Authentication for sign-in, Realtime Database for live data, and Cloud Messaging for notifications. Location and mapping use the Fused Location Provider with OpenStreetMap through osmdroid.

One design decision worth stating: **the system runs entirely within the free tier.** No part of it requires a paid subscription. For a system meant to be maintained by a college after the study ends, that was deliberate.

Julebeth will present the conceptual framework.

## Slide 9 — Conceptual Framework · JULEBETH · 1 min 15 sec

Thank you, Mardy.

Our framework follows the **Input–Process–Output** model.

The **inputs** are account credentials and roles, ride requests, driver availability, driver credentials, the FeTODAT fare schedule, and passenger and driver information.

The **processes** are authentication and role routing, driver verification, fare lookup, broadcasting the ride request, matching the driver and passenger, managing ride status, and handling notifications and concerns.

The **outputs** are a confirmed ride and fare, verified drivers, ride records and reports, fare transparency, improved coordination, and an intended reduction in waiting time.

And there is a **feedback loop**. Evaluation results and reported concerns return to the input and process stages — a fare correction reported by a passenger becomes a change to the fare table.

Mardy will present the methodology.

## Slide 10 — Methodology · MARDY · 1 min

Thank you, Julebeth.

We followed the **Waterfall model** of the software development life cycle, in six stages.

**Requirements** — we identified the transportation and system needs. **Design** — we planned the interface, the database, the architecture and the flow. **Development** — we built the Android application. **Testing** — we checked functionality and usability. **Implementation** — we deployed the system and demonstrated it. And **Evaluation** — we gathered feedback from the intended users.

We chose Waterfall because the scope was fixed early. The fare schedule was published and would not change during the study, and the roles were settled at the design stage.

Alber will present the results.

## Slide 11 — Results · ALBER · 1 min 45 sec

Thank you, Mardy.

We evaluated the system against the **ISO/IEC 25010 software quality model**, with **41 respondents** who had used it under real conditions: 20 student passengers, 20 tricycle drivers, and the system administrator.

The treatment was **frequency count, weighted mean, and ranking**, carried out by our statistician.

The results. The **student passengers rated the system 4.30**. The **tricycle drivers, 4.29**. The **administrator, 4.67**. On the five-point scale printed on our questionnaire, where 4.21 to 5.00 is Strongly Agree, all three fall in the **Strongly Agree** band.

Across the three instruments we computed thirteen category means. **Eleven fall in Strongly Agree and two in Agree.** No characteristic for any group fell below Agree, and no respondent chose Disagree or Strongly Disagree on any of the sixty-one items.

I want to be careful about that last point, and I will come back to it.

## Slide 12 — Findings · ALBER · 2 min

Four findings.

**First, user acceptance was strong.** All three groups rated the system in the Strongly Agree band.

**Second, usability.** Passengers rated it 4.38, the highest of their six characteristics. They found it easy to operate without instruction, which matters for users who install it themselves and get no training.

**Third, functionality.** Booking, onboarding, fare and monitoring were all positively evaluated. The single highest passenger item was *"The system accurately matches me with an available driver"* at 4.45 — the core function.

**Fourth, and this is the finding I most want to put in front of the panel: the weakest areas, and why.**

The two joint-lowest passenger items, both 4.05, both concern **notification timeliness**. They sit in two different characteristics and are worded differently, so the agreement between them is not an accident of the questionnaire.

The reason is a design limitation we can name precisely. Notifications are written to the database and shown inside the application, but we have no server-side component to push them to a phone where the app is closed. A passenger whose phone is in their pocket learns of an acceptance when they next open it. **The respondents rated exactly what the system does.**

And the evidence for that is in the driver data. The drivers rated the same notification feature **4.35 — the highest item in its characteristic.** Because a driver keeps the app open while working. Same feature, two positions, and the difference tells us the problem is delivery, not the notification itself.

The other weak area is **driver usability at 4.20** — the only group-level characteristic that falls in Agree. Its two lowest items concern the clarity of the interface and its labels, not any missing function. Our driver respondents averaged 53 years of age; our passengers, 20. We tested that difference and it is **not statistically significant**, so we do not claim the populations differ. But it points at type size, contrast and plainer wording — and that is an inexpensive fix.

## Slide 13 — Significance · JULEBETH · 1 min

Thank you, Alber.

For **students and passengers**, a more structured way to request rides, and to see the fare before committing rather than at the point of boarding.

For **tricycle drivers**, a digital channel for onboarding and for receiving requests, instead of circling for passengers.

For **administrators**, centralised driver verification, fare management, concern handling and records that previously had no single place.

And for **Talibon Polytechnic College**, a technology-based approach to a transport problem the college community faces every day — built and maintained within the institution.

Alber will close.

## Slide 14 — Conclusion and Recommendations · ALBER · 1 min 30 sec

Thank you, Julebeth.

**Our conclusion.** TrikRide demonstrates that a mobile system can organise tricycle ride requests and driver onboarding within this college community, and that its intended users find it acceptable — 4.30, 4.29 and 4.67, all Strongly Agree.

We also showed something narrower but, we think, worth stating: **an ordinance-fixed fare regime can be represented faithfully in a ride-hailing application.** The systems in our literature price by distance, time or demand. None of those is lawful where the fare per destination is fixed by a published schedule. Implementing that schedule as a maintainable table, rather than a formula, is how a digital platform operates inside such a regime instead of around it.

**Our recommendations**, in order of what the data supports.

Add a **server-side component for notifications** — this is the change most likely to move the passenger figures, and it addresses the lowest-rated items directly.

Carry out a **legibility pass on the driver screens** — type size, contrast, plainer labels.

Improve **performance and reliability** as the number of users grows.

**Expand mapping** so more of the 240 destinations can be selected from the map, not only from the list.

And consider **integration with additional transport services** if the scope expands beyond the college.

Online payment remains outside our scope.

## Slide 15 — Thank You · ALBER · 20 sec

That concludes our presentation.

On behalf of Julebeth, Mardy and myself, thank you to our panel and to our adviser for your time. We welcome your questions.

[[PB]]

# Question and Answer — Preparation

**Rule for the three of you:** whoever owns the area answers. If you do not know, say *"I don't have that figure with me, but I can tell you what we do know—"* and give what you have. Do not invent a number in front of the panel. A wrong number you stated confidently is much worse than an admitted gap.

**Alber answers by default.** Julebeth takes methodology and significance. Mardy takes architecture and features.

## Likely questions — with the answers

**"What sampling method did you use for the students, and why only 20?"** — *Julebeth*

> The 20 student respondents were chosen purposively: a respondent had to have installed the application and used it to book at least one ride, because a student who has not used the system cannot evaluate it. For the drivers we used total enumeration — every driver we onboarded was asked, and all 20 responded. The administrator role is held by one person, so total enumeration there gives one respondent.

⚠️ **Read the warning below about this question before the defense. Your document does not currently say this.**

**"Why Mann-Whitney U and not a t-test?"** — *Alber*

> Because Likert data is ordinal, not interval, and with 20 per group we could not assume normality. Mann-Whitney U is the non-parametric equivalent and it is appropriate to both conditions. We applied it at the 0.05 level across the four characteristics both instruments measure, and found no significant difference on any of them.

**"Your overall is 4.30 but driver usability is 4.20. Explain the difference in interpretation."** — *Alber*

> The scale on our questionnaire puts 4.21 to 5.00 in Strongly Agree and 3.41 to 4.20 in Agree. 4.20 falls on the Agree side of that boundary — by 0.01. We report it as Agree because that is what our own scale says, and we did not want to round it up to make the result look better.

**"How did you compute the overall weighted mean?"** — *Alber*

> It is the mean of the category means, not the mean of all items. We did it that way so each quality characteristic counts equally regardless of how many statements measure it — otherwise Functional Suitability, with eight items, would outweigh Reliability with five. For the passengers, averaging all 34 items instead gives 4.2956 against 4.3000, so the choice does not change any interpretation.

**"One administrator is not a sample. Why report it at all?"** — *Alber*

> You are right that it is not a sample, and we do not treat it as one. The role is held by one person, so that is total enumeration of the role rather than a selection from a population. We report it for completeness, we report it separately, and we never pool it with the two groups of twenty. No inference beyond that one individual is available from N equals 1.

**"No one rated anything below 3. Isn't that suspicious?"** — *Alber* — **know this one; it is the sharpest question a good panel will ask**

> It is the honest weakness of our evaluation and we state it in the manuscript. The 61 item means run from 4.05 to 4.50 — a very narrow band. That is consistent with genuine approval, and it is equally consistent with acquiescence bias and with respondents who knew us. Our design has no reverse-scored item and no independent administration, so it cannot separate those explanations. That is why we read our figures as an upper bound on acceptability rather than a point estimate of it.

**"How do you know the system reduced waiting time?"** — *Julebeth*

> We do not know it as a measurement. We measured perception: the drivers rated *"The system reduces my idle time"* at 4.25 and *"helps me serve more passengers"* at 4.35. We did not instrument waiting time before and after deployment. So the objective is addressed through what users believe happened, not through an observed reduction, and we state that as a limitation.

**"Your security characteristic scored 4.34. How do you know the system is secure?"** — *Alber*

> That figure is our users' confidence, not a measurement of security — a passenger is not in a position to assess our database rules. The evidence for security is separate: 18 security test cases run against the deployed Firebase rules, covering whether one user can read another's record and whether an unverified driver can accept a ride. Those are in Chapter 4. The 4.34 and the test results should not be read as confirming each other.

**"Why Firebase and not your own server?"** — *Mardy*

> Cost and continuity. The whole system runs inside the free tier, so the college can keep operating it after we graduate without a hosting budget. The trade-off is that we cannot run server-side code, which is exactly why we have no push notifications — the limitation the evaluation picked up.

**"Why Waterfall and not Agile?"** — *Mardy*

> The scope was fixed early. The FeTODAT fare schedule was already published and would not change during the study, and the three roles were settled at design. Waterfall suited a project where the requirements were known at the start. If the requirements had been uncertain, Agile would have been the better choice.

**"What happens if two drivers accept the same request?"** — *Alber*

> Only one can. The acceptance is a database transaction — the first write wins and the request is removed, so the second driver's device shows that the request is no longer available rather than creating a duplicate ride.

**"You have 240 destinations. How accurate is your fare table?"** — *Alber*

> We transcribed 241 rows from the posted FeTODAT sheet. 46 of those we could not read with full confidence, and those are flagged in the system for an administrator to verify. Three carry no usable rate and are disabled rather than priced. We chose to flag uncertainty rather than let the app charge a number we were not sure of.

**"Section B of your passenger questionnaire lists the same statement twice."** — *Alber* — *if a panelist spots it*

> Yes — *"The system is easy to learn"* appears as both item 1 and item 6 of that section, so five distinct statements were counted as six. It is an instrument error and we should have caught it. We report both because both were administered. Removing the duplicate moves the Usability mean from 4.3750 to 4.3800 and changes no interpretation, but it is a flaw and it should be fixed before any further administration.

**"What is your biggest weakness?"** — *Alber* — **prepare this; it is very commonly asked**

> Notification delivery. The system finds a driver well — that was our highest-rated item — and tells the passenger about it poorly. It is a consequence of building without a server-side component, which kept us inside the free tier. We knew the trade-off when we made it, and the evaluation confirmed the cost.

## Two things to fix or prepare for before 23 September

**1. Your document still has a placeholder in the Research Respondents section.** It reads: *"The method of determining the sample size for student respondents is to be set with the guidance of the study's statistician."* Your Chapter 4 then reports the full results from 20 students. A panelist who asks about sampling and turns to that section will find the question unanswered in your own manuscript. **Fix the paragraph before you submit**, or be ready to explain the discrepancy.

**2. Be ready for a question about how the manuscript was written.** If you are asked directly whether AI was used, the honest answer is the only safe one, and it has to come from you — not from a script. Check what your programme's policy actually says before the defense so you know where you stand.
