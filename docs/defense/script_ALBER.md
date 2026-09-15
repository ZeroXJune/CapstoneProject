# Defense Script — ALBER JUNE M. MUMAR

**Role:** Lead presenter · **Your slides:** 1, 3, 4, 5, 11, 12, 14, 15 · **Your time:** about 11 minutes

You open, you run the demonstration, you present the results and findings, and you close. You also answer most of the Q&A.

| Your slide | What it is | Time |
|---:|:---|---:|
| 1 | Title and introductions | 45 sec |
| 3 | The proposed system | 1 min |
| 4 | Objectives | 1 min |
| 5 | **Live demonstration** | 3 min |
| 11 | Results | 1 min 45 |
| 12 | Findings | 2 min |
| 14 | Conclusion and recommendations | 1 min 30 |
| 15 | Thank you | 20 sec |

[[PB]]

## SLIDE 1 — Title · you open

Good morning to our panel, to our adviser, and to everyone here.

I am Alber June Mumar, and with me are Julebeth Hinlayagan and Mardy Gonzaga. We are presenting our capstone project: **TrikRide, a Smart Tricycle Ride and Driver Onboarding System for Talibon Polytechnic College.**

It is an Android application that connects passengers with verified tricycle drivers around our campus.

**→ Hand off:** *"Julebeth will begin with the problem we set out to address."*

*Julebeth now presents Slide 2 — the background. About 1 min 30.*

[[PB]]

## SLIDE 3 — The Proposed System

> **Your cue.** Julebeth finishes with: *"These four problems are what TrikRide was built to address. Alber will present the system."*

Thank you, Julebeth.

TrikRide is a mobile system that connects passengers and verified tricycle drivers through a structured digital request.

It does three things. **Ride booking** — the passenger requests a ride and sees the fare before committing to it. **Driver onboarding** — a driver submits credentials, and an administrator verifies them. **Ride coordination** — available drivers receive the request, one accepts, and the passenger follows the status until the ride is complete.

One point on fares, because it separates our system from the ride-hailing applications in our literature. We do not estimate fares and we do not compute them by distance. Every fare comes from the schedule published by the Federation of Tricycle Operators and Drivers Association of Talibon — the FeTODAT schedule — with its regular and discounted columns. In Talibon the fare is fixed by the association's published rates, so a system that priced by distance would not be lawful here. Our system follows the posted schedule.

## SLIDE 4 — Objectives

Six objectives guided the work.

One, provide a digital process for requesting rides. Two, register and verify tricycle drivers. Three, connect ride requests with available drivers. Four, help improve passenger–driver coordination and reduce waiting time. Five, maintain ride status and activity records. And six, evaluate the system for usability, efficiency and user satisfaction.

The first five are about what we built. The sixth is about how it was received, and I will come back to it in the results.

Now let me show you the system itself.

[[PB]]

## SLIDE 5 — Live Demonstration · 3 minutes

> **Do not read this aloud.** Play the recorded walkthrough and narrate over it. Cover these points, in this order, in your own words.

This is the passenger side. The passenger signs in and selects a destination from the fare table. Notice that **the fare appears here, before the request is sent** — this is the fare transparency we described.

The passenger submits the request. On the driver's device, the request now appears with a countdown. The driver accepts.

Once accepted, both devices show the same ride. The passenger sees the driver's name, contact and tricycle details. The status moves through acceptance, arrival, in-transit, and completion. Both sides see the same status at the same time, because both are reading the same record.

Now the driver side of onboarding. A new driver registers and submits licence and tricycle details, together with a photograph of the licence.

Here is the administrator's view. The application appears in the verification queue. The administrator reviews the credentials and approves.

This is the part I want the panel to note: **before approval, this driver cannot accept a passenger.** The restriction is enforced in the database rules, not only in the interface — so it holds even if someone modified the app. That is objective two, working.

**→ Hand off:** *"Mardy will take you through the features and the technical design."*

*Mardy now presents Slides 6, 7, 8 — features, roles, architecture. Then Julebeth takes Slide 9, and Mardy takes Slide 10. About 5 minutes total. You are back at Slide 11.*

[[PB]]

## SLIDE 11 — Results

> **Your cue.** Mardy finishes Slide 10 with: *"Alber will present the results."*

Thank you, Mardy.

We evaluated the system against the **ISO/IEC 25010 software quality model**, with **41 respondents** who had used it under real conditions: 20 student passengers, 20 tricycle drivers, and the system administrator.

The treatment was **frequency count, weighted mean, and ranking**, carried out by our statistician.

The results. The **student passengers rated the system 4.30**. The **tricycle drivers, 4.29**. The **administrator, 4.67**. On the five-point scale printed on our questionnaire, where 4.21 to 5.00 is Strongly Agree, all three fall in the **Strongly Agree** band.

Across the three instruments we computed thirteen category means. **Eleven fall in Strongly Agree and two in Agree.** No characteristic for any group fell below Agree, and no respondent chose Disagree or Strongly Disagree on any of the sixty-one items.

I want to be careful about that last point, and I will come back to it.

[[PB]]

## SLIDE 12 — Findings · your most important slide

Four findings.

**First, user acceptance was strong.** All three groups rated the system in the Strongly Agree band.

**Second, usability.** Passengers rated it 4.38, the highest of their six characteristics. They found it easy to operate without instruction, which matters for users who install it themselves and get no training.

**Third, functionality.** Booking, onboarding, fare and monitoring were all positively evaluated. The single highest passenger item was *"The system accurately matches me with an available driver"* at 4.45 — the core function.

**Fourth, and this is the finding I most want to put in front of the panel: the weakest areas, and why.**

The two joint-lowest passenger items, both 4.05, both concern **notification timeliness**. They sit in two different characteristics and are worded differently, so the agreement between them is not an accident of the questionnaire.

The reason is a design limitation we can name precisely. Notifications are written to the database and shown inside the application, but we have no server-side component to push them to a phone where the app is closed. A passenger whose phone is in their pocket learns of an acceptance when they next open it. **The respondents rated exactly what the system does.**

And the evidence for that is in the driver data. The drivers rated the same notification feature **4.35 — the highest item in its characteristic.** Because a driver keeps the app open while working. Same feature, two positions, and the difference tells us the problem is delivery, not the notification itself.

The other weak area is **driver usability at 4.20** — the only group-level characteristic that falls in Agree. Its two lowest items concern the clarity of the interface and its labels, not any missing function. Our driver respondents averaged 53 years of age; our passengers, 20. We tested that difference and it is **not statistically significant**, so we do not claim the populations differ. But it points at type size, contrast and plainer wording — and that is an inexpensive fix.

**→ Hand off:** *"Julebeth will cover who this matters to."*

*Julebeth presents Slide 13 — significance. About 1 minute.*

[[PB]]

## SLIDE 14 — Conclusion and Recommendations

> **Your cue.** Julebeth finishes with: *"Alber will close."*

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

## SLIDE 15 — Thank You

That concludes our presentation.

On behalf of Julebeth, Mardy and myself, thank you to our panel and to our adviser for your time. We welcome your questions.

[[PB]]

# Your Q&A — Alber

You answer by default. Send methodology questions to Julebeth and architecture questions to Mardy.

**If you do not know a figure, say so.** *"I don't have that with me, but what I can tell you is—"* A wrong number stated confidently is far worse than an admitted gap.

**"Why Mann-Whitney U and not a t-test?"**

> Because Likert data is ordinal, not interval, and with 20 per group we could not assume normality. Mann-Whitney U is the non-parametric equivalent and it is appropriate to both conditions. We applied it at the 0.05 level across the four characteristics both instruments measure, and found no significant difference on any of them.

**"Your overall is 4.30 but driver usability is 4.20. Explain the difference in interpretation."**

> The scale on our questionnaire puts 4.21 to 5.00 in Strongly Agree and 3.41 to 4.20 in Agree. 4.20 falls on the Agree side of that boundary — by 0.01. We report it as Agree because that is what our own scale says, and we did not want to round it up to make the result look better.

**"How did you compute the overall weighted mean?"**

> It is the mean of the category means, not the mean of all items. We did it that way so each quality characteristic counts equally regardless of how many statements measure it — otherwise Functional Suitability, with eight items, would outweigh Reliability with five. For the passengers, averaging all 34 items instead gives 4.2956 against 4.3000, so the choice does not change any interpretation.

**"One administrator is not a sample. Why report it at all?"**

> You are right that it is not a sample, and we do not treat it as one. The role is held by one person, so that is total enumeration of the role rather than a selection from a population. We report it for completeness, we report it separately, and we never pool it with the two groups of twenty. No inference beyond that one individual is available from N equals 1.

**"No one rated anything below 3. Isn't that suspicious?"** — *the sharpest question a good panel will ask. Know this one cold.*

> It is the honest weakness of our evaluation and we state it in the manuscript. The 61 item means run from 4.05 to 4.50 — a very narrow band. That is consistent with genuine approval, and it is equally consistent with acquiescence bias and with respondents who knew us. Our design has no reverse-scored item and no independent administration, so it cannot separate those explanations. That is why we read our figures as an upper bound on acceptability rather than a point estimate of it.

**"Your security characteristic scored 4.34. How do you know the system is secure?"**

> That figure is our users' confidence, not a measurement of security — a passenger is not in a position to assess our database rules. The evidence for security is separate: 18 security test cases run against the deployed Firebase rules, covering whether one user can read another's record and whether an unverified driver can accept a ride. Those are in Chapter 4. The 4.34 and the test results should not be read as confirming each other.

**"What happens if two drivers accept the same request?"**

> Only one can. The acceptance is a database transaction — the first write wins and the request is removed, so the second driver's device shows that the request is no longer available rather than creating a duplicate ride.

**"You have 240 destinations. How accurate is your fare table?"**

> We transcribed 241 rows from the posted FeTODAT sheet. 46 of those we could not read with full confidence, and those are flagged in the system for an administrator to verify. Three carry no usable rate and are disabled rather than priced. We chose to flag uncertainty rather than let the app charge a number we were not sure of.

**"Section B of your passenger questionnaire lists the same statement twice."** — *only if a panelist spots it*

> Yes — *"The system is easy to learn"* appears as both item 1 and item 6 of that section, so five distinct statements were counted as six. It is an instrument error and we should have caught it. We report both because both were administered. Removing the duplicate moves the Usability mean from 4.3750 to 4.3800 and changes no interpretation, but it is a flaw and it should be fixed before any further administration.

**"What is your biggest weakness?"** — *very commonly asked. Prepare it.*

> Notification delivery. The system finds a driver well — that was our highest-rated item — and tells the passenger about it poorly. It is a consequence of building without a server-side component, which kept us inside the free tier. We knew the trade-off when we made it, and the evaluation confirmed the cost.

**If you are asked how the manuscript was written**, answer honestly. That answer has to be yours — it is not scripted here. Check your programme's policy on AI assistance before the defense so you know where you stand.
