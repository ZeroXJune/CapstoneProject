# Defense Script — MARDY GONZAGA

**Your slides:** 6, 7, 8, 10 · **Your time:** about 4 minutes

You have one continuous block of three slides in the middle — features, roles and architecture — then you return for the methodology after Julebeth's framework slide.

| Your slide | What it is | Time | You speak after |
|---:|:---|---:|:---|
| 6 | Key features | 1 min | Alber (Slide 5, the demo) |
| 7 | Roles | 45 sec | *continues from your Slide 6* |
| 8 | System architecture | 1 min 15 | *continues from your Slide 7* |
| 10 | Methodology | 1 min | Julebeth (Slide 9) |

[[PB]]

## SLIDE 6 — Key Features · 1 min

> **Your cue.** Alber finishes the live demonstration with: *"Mardy will take you through the features and the technical design."*

Thank you, Alber.

The features are grouped by role, and each role sees only what belongs to it.

The **passenger** has registration and profile, destination selection, fare display, ride request, driver information, ride status and history, and a channel for raising concerns.

The **driver** has registration, credential submission, an availability toggle, incoming ride requests, acceptance, ride management, and history.

The **administrator** has driver verification, fare table management, ride monitoring, concern management, and records and reports.

## SLIDE 7 — Roles · 45 sec

Three roles meet in one shared real-time database.

The **passenger** requests rides, checks fares, views driver information and monitors status. The **driver** registers, submits credentials, goes online once verified, and accepts requests. The **administrator** verifies drivers, manages fares, reviews concerns, monitors records and generates reports.

They see different screens, but they are reading and writing the same data. That is why a status change on one device appears on the other.

[[PB]]

## SLIDE 8 — System Architecture · 1 min 15

This is how it is built.

The client is an **Android application written in Kotlin using Jetpack Compose**. It follows the **Model-View-ViewModel** pattern, with a repository layer between the interface and the data.

Why that layer matters: the screens do not talk to the database directly. Everything goes through the repository and one Firebase service class. So if the data source changes, we change one layer, not every screen.

The backend is **Firebase** — Authentication for sign-in, Realtime Database for live data, and Cloud Messaging for notifications. Location and mapping use the Fused Location Provider with OpenStreetMap through osmdroid.

One design decision worth stating: **the system runs entirely within the free tier.** No part of it requires a paid subscription. For a system meant to be maintained by a college after the study ends, that was deliberate.

**→ Hand off:** *"Julebeth will present the conceptual framework."*

*Julebeth presents Slide 9 — the conceptual framework. About 1 min 15. You are back at Slide 10.*

[[PB]]

## SLIDE 10 — Methodology · 1 min

> **Your cue.** Julebeth finishes the framework with: *"Mardy will present the methodology."*

Thank you, Julebeth.

We followed the **Waterfall model** of the software development life cycle, in six stages.

**Requirements** — we identified the transportation and system needs. **Design** — we planned the interface, the database, the architecture and the flow. **Development** — we built the Android application. **Testing** — we checked functionality and usability. **Implementation** — we deployed the system and demonstrated it. And **Evaluation** — we gathered feedback from the intended users.

We chose Waterfall because the scope was fixed early. The fare schedule was published and would not change during the study, and the roles were settled at the design stage.

**→ Hand off:** *"Alber will present the results."*

*Alber takes Slides 11 and 12, Julebeth takes 13, Alber closes with 14 and 15. You are done presenting — but stay ready for Q&A.*

[[PB]]

# Your Q&A — Mardy

Alber answers by default. **The technical questions are yours: architecture, Firebase, the development model.** If a question is clearly about how the system is built and Alber looks to you, take it.

**If you do not know something, say so.** *"I don't have that detail with me, but what I can tell you is—"* A wrong answer stated confidently is far worse than an admitted gap.

**"Why Firebase and not your own server?"** — *your main question*

> Cost and continuity. The whole system runs inside the free tier, so the college can keep operating it after we graduate without a hosting budget. The trade-off is that we cannot run server-side code, which is exactly why we have no push notifications — the limitation the evaluation picked up.

**"Why Waterfall and not Agile?"** — *your second question*

> The scope was fixed early. The FeTODAT fare schedule was already published and would not change during the study, and the three roles were settled at design. Waterfall suited a project where the requirements were known at the start. If the requirements had been uncertain, Agile would have been the better choice.

**"Why MVVM? What does the repository layer actually buy you?"** — *likely follow-up on your Slide 8*

> It separates the screens from the data. Every screen goes through the ViewModel, and the ViewModel goes through the repository, and only the repository talks to Firebase. So the database access lives in one place instead of being scattered across every screen. If we moved off Firebase, we would change that one layer rather than rewrite the interface.

**"How does the system handle losing connection mid-booking?"**

> The operation fails within twelve seconds with a message the user can act on, rather than showing a loading spinner indefinitely. We added that after finding during integration that a write to a database instance that did not exist just hung. It is one of our system test cases.

**"Is the driver verification enforced anywhere other than the app?"** — *pass to Alber if you are unsure, but the answer is:*

> Yes. It is enforced in the Firebase security rules, not only in the interface. An unverified driver cannot write an acceptance to the database even with a modified client. We tested that case specifically.

**Questions to pass on:** anything about the statistics, the evaluation results or the fare table goes to **Alber**. Anything about sampling, respondents or the research framework goes to **Julebeth**.
