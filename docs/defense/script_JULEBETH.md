# Defense Script — JULEBETH HINLAYAGAN

**Your slides:** 2, 9, 13 · **Your time:** about 3 minutes 45 seconds

You speak three times, spread across the presentation. You open the substance of the talk with the problem, you present the conceptual framework in the middle, and you present the significance near the end.

| Your slide | What it is | Time | You speak after |
|---:|:---|---:|:---|
| 2 | Background — the problem | 1 min 30 | Alber (Slide 1) |
| 9 | Conceptual framework | 1 min 15 | Mardy (Slide 8) |
| 13 | Significance of the study | 1 min | Alber (Slide 12) |

[[PB]]

## SLIDE 2 — Background of the Study · 1 min 30

> **Your cue.** Alber opens the presentation and finishes with: *"Julebeth will begin with the problem we set out to address."*

Thank you, Alber.

Students, faculty and staff of this college depend on tricycles every day. But the way the service works around the campus has not changed.

A passenger walks to a terminal, or waits at the roadside, with no way of knowing whether a tricycle is coming. A driver circles the area looking for passengers, with no way of knowing who needs a ride. Neither side can see the other.

Look at the current process on the screen. The passenger looks for a driver, waits, asks the fare only when boarding, and then rides. Every step is manual.

That gives us four problems. **Waiting time**, because neither side can see the other. **Manual coordination**, because there is no channel between them. **Fare uncertainty**, because the passenger learns the price at the point of boarding rather than before deciding. And **driver onboarding**, which at present has no systematic way to check who is driving.

These four problems are what TrikRide was built to address.

**→ Hand off:** *"Alber will present the system."*

*Alber now takes Slides 3, 4 and 5 — the proposed system, the objectives, and the live demonstration. Then Mardy takes Slides 6, 7 and 8. That is about 7 minutes. You are back at Slide 9.*

[[PB]]

## SLIDE 9 — Conceptual Framework · 1 min 15

> **Your cue.** Mardy finishes Slide 8, the architecture, with: *"Julebeth will present the conceptual framework."*

Thank you, Mardy.

Our framework follows the **Input–Process–Output** model.

The **inputs** are account credentials and roles, ride requests, driver availability, driver credentials, the FeTODAT fare schedule, and passenger and driver information.

The **processes** are authentication and role routing, driver verification, fare lookup, broadcasting the ride request, matching the driver and passenger, managing ride status, and handling notifications and concerns.

The **outputs** are a confirmed ride and fare, verified drivers, ride records and reports, fare transparency, improved coordination, and an intended reduction in waiting time.

And there is a **feedback loop**. Evaluation results and reported concerns return to the input and process stages — a fare correction reported by a passenger becomes a change to the fare table.

**→ Hand off:** *"Mardy will present the methodology."*

*Mardy takes Slide 10. Alber then takes Slides 11 and 12 — the results and findings. About 5 minutes. You are back at Slide 13.*

[[PB]]

## SLIDE 13 — Significance of the Study · 1 min

> **Your cue.** Alber finishes Slide 12, the findings, with: *"Julebeth will cover who this matters to."*

Thank you, Alber.

For **students and passengers**, a more structured way to request rides, and to see the fare before committing rather than at the point of boarding.

For **tricycle drivers**, a digital channel for onboarding and for receiving requests, instead of circling for passengers.

For **administrators**, centralised driver verification, fare management, concern handling and records that previously had no single place.

And for **Talibon Polytechnic College**, a technology-based approach to a transport problem the college community faces every day — built and maintained within the institution.

**→ Hand off:** *"Alber will close."*

*Alber takes Slides 14 and 15. You are done presenting — but stay ready for Q&A.*

[[PB]]

# Your Q&A — Julebeth

Alber answers by default. **Two areas are yours: sampling, and the waiting-time claim.** If a question is clearly methodology and Alber looks to you, take it.

**If you do not know a figure, say so.** *"I don't have that with me, but what I can tell you is—"* A wrong number stated confidently is far worse than an admitted gap.

**"What sampling method did you use for the students, and why only 20?"** — *your main question*

> The 20 student respondents were chosen purposively: a respondent had to have installed the application and used it to book at least one ride, because a student who has not used the system cannot evaluate it. For the drivers we used total enumeration — every driver we onboarded was asked, and all 20 responded. The administrator role is held by one person, so total enumeration there gives one respondent.

> ⚠️ **Before the defense, check that your manuscript actually says this.** The version we reviewed still had a placeholder saying the sample size was to be determined later. If it has not been corrected, do not be caught out by a panelist reading that paragraph.

**"How do you know the system reduced waiting time?"** — *your second question. Answer it honestly; do not overclaim.*

> We do not know it as a measurement. We measured perception: the drivers rated *"The system reduces my idle time"* at 4.25 and *"helps me serve more passengers"* at 4.35. We did not instrument waiting time before and after deployment. So the objective is addressed through what users believe happened, not through an observed reduction, and we state that as a limitation.

**"Why did you choose the Input–Process–Output framework?"** — *possible follow-up on your Slide 9*

> Because our study is about a system that takes defined inputs, transforms them through defined processes, and produces defined outputs. IPO maps directly onto that. The feedback loop matters to us as well — evaluation results and reported concerns return to the input stage, which is how the fare table gets corrected in actual use.

**Questions to pass on:** anything about statistics or the results goes to **Alber**. Anything about the architecture, Firebase, or the development model goes to **Mardy**.
