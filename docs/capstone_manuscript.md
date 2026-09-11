---
title: "A Smart Tricycle Ride and Driver Onboarding System for Talibon Polytechnic College"
---

::: {custom-style="TitleBlock"}
A SMART TRICYCLE RIDE AND DRIVER ONBOARDING SYSTEM
FOR TALIBON POLYTECHNIC COLLEGE
:::

A Capstone Project Presented to

The Faculty of the Department of Information Systems

Talibon Polytechnic College

San Isidro, Talibon, Bohol

In Partial Fulfillment

of the Requirements for the Degree of

BACHELOR OF SCIENCE IN INFORMATION SYSTEMS

by

Alber June M. Mumar

Julebeth Hinlayagan

Mardy Gonzaga

MAY 2026

[[PB]]

# APPROVAL SHEET

This Capstone Project with the title **A SMART TRICYCLE RIDE AND DRIVER ONBOARDING SYSTEM FOR TALIBON POLYTECHNIC COLLEGE**, prepared and submitted by **Alber June M. Mumar, Julebeth Hinlayagan,** and **Mardy Gonzaga** in partial fulfillment of the requirements for the degree of **Bachelor of Science in Information Systems**, has been examined and recommended for acceptance and approval for Oral Examination.

CAPSTONE PROJECT COMMITTEE

**STANLEY CLARK M. DIPAY, PhD**

Chairman

**SOCRATES C. MACALOLOT**  **ASTRID P. VALMORIA, MAELT**

Capstone Adviser  English Critic

**FLORDELIS A. TURTOGA, MAMT**

Statistician

APPROVED by the Examining Panel during the Oral Examination conducted on ______________ with the grade of ________.

PANEL OF EXAMINERS

**STANLEY CLARK M. DIPAY, PhD**

Committee Chairman

**GISELO B. CAJES, PhD**  **DIOSCORO A. AVERGONZADO, PhD**

Research Director  Member

ACCEPTED and APPROVED as partial fulfillment of the requirements for the degree of Bachelor of Science in Information Systems.

Oral Defense Date: ______________

**STANLEY CLARK M. DIPAY, PhD**

College President

[[PB]]

# ACKNOWLEDGEMENTS

The completion of this capstone project would not have been possible without the guidance, support, and encouragement of many individuals. The researchers would like to express their heartfelt gratitude to everyone who contributed to the success of this study.

To the Almighty God, for the wisdom, strength, and perseverance He bestowed upon the researchers throughout the entire process of this study. All achievements are offered back to Him.

To Stanley Clark M. Dipay, PhD, College President and Capstone Project Committee Chairman, for his invaluable guidance, insightful recommendations, and unwavering support in shaping the direction of this research.

To Socrates C. Macalolot, the Capstone Adviser, for his patience, mentorship, and technical expertise. His constructive feedback and consistent encouragement were instrumental in the development of the proposed system.

To Astrid P. Valmoria, MAELT, English Critic, for meticulously reviewing the language, grammar, and clarity of this manuscript. Her expertise greatly improved the quality of written communication in this study.

To Flordelis A. Turtoga, MAMT, Statistician, for her assistance in the statistical treatment of data and for ensuring the accuracy and validity of the research findings.

To the faculty and staff of the Department of Information Systems of Talibon Polytechnic College, for sharing their knowledge and providing a conducive learning environment throughout the researchers' academic journey.

To the Federation of Tricycle Operators and Drivers Association of Talibon (FeTODAT), for making the official fare schedule available to the researchers and for the cooperation of its officers during data gathering.

To the tricycle drivers and commuters of Talibon, Bohol, who willingly participated in this study and provided the real-world insights that made this research grounded and relevant to the community it intends to serve.

To the families of the researchers, for their endless love, prayers, moral support, and financial assistance. Their sacrifices and understanding were a constant source of inspiration and motivation.

And to all friends and classmates who offered their time, assistance, and encouragement throughout the duration of this study, thank you. This work is as much yours as it is ours.

The Researchers

[[PB]]

# EXECUTIVE SUMMARY

Students, faculty, and staff of Talibon Polytechnic College depend on tricycles for their daily commute, yet the service around the campus operates the way it always has. Passengers walk to a terminal or wait at the roadside with no way of knowing whether a tricycle is coming. Drivers circle for passengers with no way of knowing who needs a ride. Neither side can see the other, and the result is time lost on both.

This study developed **TrikRide**, an Android application that connects passengers and tricycle drivers serving Talibon Polytechnic College, and that puts a verification step in front of drivers before they are allowed to accept passengers.

The system serves three kinds of users. A passenger creates an account, chooses a destination from the fare schedule published by the Federation of Tricycle Operators and Drivers Association of Talibon, sees the exact fare before committing, and follows the ride from acceptance through to completion. A driver registers, submits licence and tricycle details together with a photograph of the licence, and can accept requests only once an administrator has approved those documents. An administrator verifies drivers, maintains the fare table, reviews concerns raised by either side, watches activity as it happens, and exports records for any month, year, or range of dates as printable documents or spreadsheets.

Fares are not estimated. The application prices every ride from the 240 destinations on the posted FeTODAT schedule, which distinguishes the regular rate from the discounted rate for senior citizens, persons with disabilities, and students, and which sets a minimum fare of fifteen pesos for regular passengers and twelve pesos for discounted passengers. Because the rate table is held in the database rather than in code, the administrator corrects a price without a new release of the application.

The application was built in Kotlin with Jetpack Compose against Firebase Authentication, Realtime Database, and Cloud Messaging, following a Model-View-ViewModel architecture with a repository layer. The system operates entirely within the free tier of the backend platform, and no component of it requires a paid subscription. Development followed the Waterfall model of the software development life cycle.

The system was evaluated against the ISO/IEC 25010 quality characteristics by forty-one respondents who had used it under real conditions: twenty student passengers of Talibon Polytechnic College, twenty tricycle drivers serving the campus, and the system administrator. Using frequency count, weighted mean and ranking, the passengers rated the system **4.30**, the drivers **4.29**, and the administrator **4.67**, each interpreted as *Strongly Agree* on the scale printed on the questionnaire. Eleven of the thirteen category means fall in *Strongly Agree* and two in *Agree*; no characteristic for any group fell below *Agree*, and no respondent selected *Disagree* or *Strongly Disagree* on any of the sixty-one items.

The evaluation also located the system's two weakest points precisely. The joint-lowest passenger items, both at 4.05, both concern notification timeliness, which reflects the deliberate decision to build without a server-side component to push notifications to a device on which the application is not running. Usability among drivers, at 4.20, is the only group-level characteristic to fall in *Agree*, and its weakest items concern the clarity of the interface rather than its function, among respondents whose mean age was 53.25 against 19.70 for the passengers. Both findings are carried into the recommendations. Because no respondent used the lower half of the scale on any item, the overall figures are read as an upper bound on acceptability rather than a point estimate of it.

[[PB]]

# TABLE OF CONTENTS

| | Page |
|:---|---:|
| TITLE PAGE | i |
| APPROVAL SHEET | ii |
| ACKNOWLEDGEMENTS | iii |
| EXECUTIVE SUMMARY | iv |
| TABLE OF CONTENTS | v |
| LIST OF TABLES | viii |
| LIST OF FIGURES | ix |
| **Chapter 1 — INTRODUCTION AND PROJECT CONTEXT** | 1 |
| 1.1 Project Context | 1 |
| 1.2 Purpose and Description of the Project | 5 |
| 1.3 Objectives of the Study | 6 |
| 1.4 Statement of the Problem | 7 |
| 1.5 Scope and Limitations | 8 |
| 1.6 Significance of the Study | 10 |
| 1.7 Research Methodology | 11 |
| 1.8 Research Environment | 14 |
| 1.9 Research Participants and Respondents | 15 |
| 1.10 Research Instruments | 16 |
| 1.11 Data Gathering Procedure | 17 |
| 1.12 Definition of Terms | 19 |
| **Chapter 2 — REVIEW OF RELATED LITERATURE AND SYSTEMS** | 23 |
| 2.1 Related Literature | 23 |
| 2.2 Related Studies | 25 |
| 2.3 Comparison of Related Systems | 27 |
| 2.4 Theoretical Framework | 29 |
| 2.5 Conceptual Framework | 32 |
| 2.6 Synthesis | 34 |
| **Chapter 3 — TECHNICAL BACKGROUND** | 36 |
| 3.1 Software Requirements | 36 |
| 3.2 Hardware Requirements | 38 |
| 3.3 Programming Languages | 39 |
| 3.4 Development Tools | 40 |
| 3.5 Database Technologies | 42 |
| 3.6 Network Architecture | 44 |
| 3.7 Software Architecture | 45 |
| 3.8 Security Features | 47 |
| 3.9 System Architecture | 50 |
| **Chapter 4 — METHODOLOGY, RESULTS, AND DISCUSSION** | 52 |
| 4.1 Requirements Analysis | 52 |
| 4.2 Requirements Documentation | 56 |
| 4.3 System Design | 60 |
| 4.4 Software Development | 68 |
| 4.5 Testing | 71 |
| 4.6 Prototype Description | 75 |
| 4.7 Implementation Plan | 78 |
| 4.8 Implementation Results | 80 |
| 4.9 System Evaluation | 84 |
| 4.9.1 Evaluation by the Student Passengers | 86 |
| 4.9.2 Evaluation by the Tricycle Drivers | 93 |
| 4.9.3 Evaluation by the System Administrator | 98 |
| 4.9.4 Comparison and Overall Result | 100 |
| 4.9.5 Summary of the Evaluation | 102 |
| **Chapter 5 — SUMMARY, CONCLUSIONS AND RECOMMENDATIONS** | 104 |
| 5.1 Summary of Findings | 104 |
| 5.2 Conclusions | 106 |
| 5.3 Recommendations | 107 |
| 5.4 Limitations of the Study | 109 |
| 5.5 Future Enhancements | 110 |
| REFERENCES | 114 |
| APPENDICES | 117 |
| CURRICULUM VITAE | 140 |

[[PB]]

# LIST OF TABLES

| Table | Title | Page |
|:---:|:---|---:|
| 1 | Comparison of Related Systems | 27 |
| 2 | Functional Requirements | 52 |
| 3 | Non-Functional Requirements | 55 |
| 4 | User Requirements by Role | 56 |
| 5 | System Requirements | 57 |
| 6 | Use Case Description: Book a Ride | 58 |
| 7 | Use Case Description: Verify a Driver | 59 |
| 8 | Realtime Database Node Structure | 65 |
| 9 | Fare Table Composition by Zone | 66 |
| 10 | Development Milestones and Deliverables | 69 |
| 11 | Unit Test Cases and Results | 71 |
| 12 | Integration Test Cases and Results | 72 |
| 13 | System Test Cases and Results | 73 |
| 14 | Security Test Cases and Results | 74 |
| 15 | Application Modules and Their Functions | 80 |
| 16 | Reports Generated by the System | 82 |
| 17 | Charts on the Summary Page of Each Report | 83 |
| 18 | Evaluation Respondents | 84 |
| 19 | Level of Acceptability in Terms of Functional Suitability — Student Passengers | 86 |
| 20 | Level of Acceptability in Terms of Usability — Student Passengers | 87 |
| 21 | Level of Acceptability in Terms of Efficiency — Student Passengers | 88 |
| 22 | Level of Acceptability in Terms of Reliability — Student Passengers | 89 |
| 23 | Level of Acceptability in Terms of Security — Student Passengers | 90 |
| 24 | Level of Acceptability in Terms of Overall Satisfaction — Student Passengers | 91 |
| 25 | Summary of the Passenger Evaluation by Characteristic | 92 |
| 26 | Level of Acceptability in Terms of Usability — Tricycle Drivers | 93 |
| 27 | Level of Acceptability in Terms of Functionality — Tricycle Drivers | 94 |
| 28 | Level of Acceptability in Terms of Efficiency — Tricycle Drivers | 95 |
| 29 | Level of Acceptability in Terms of Reliability — Tricycle Drivers | 96 |
| 30 | Summary of the Driver Evaluation by Characteristic | 97 |
| 31 | Level of Acceptability in Terms of Functional Suitability — System Administrator | 98 |
| 32 | Level of Acceptability in Terms of Usability — System Administrator | 98 |
| 33 | Level of Acceptability in Terms of Reliability — System Administrator | 99 |
| 34 | Summary of the Administrator Evaluation by Characteristic | 99 |
| 35 | Overall Weighted Mean by Respondent Group | 100 |
| 36 | Comparison of Passenger and Driver Evaluations by Characteristic | 101 |

# LIST OF FIGURES

| Figure | Title | Page |
|:---:|:---|---:|
| 1 | Research Flow | 13 |
| 2 | Conceptual Framework of the System | 32 |
| 3 | System Architecture | 50 |
| 4 | Context Diagram (Data Flow Diagram Level 0) | 60 |
| 5 | Data Flow Diagram Level 1 | 61 |
| 6 | Entity Relationship Diagram | 62 |
| 7 | Use Case Diagram | 63 |
| 8 | Activity Diagram: Booking a Ride | 64 |
| 9 | Sequence Diagram: Ride Request to Completion | 65 |
| 10 | Class Diagram | 66 |
| 11 | Realtime Database Schema | 67 |
| 12 | Waterfall Model of the Software Development Life Cycle | 68 |
| 13 | Screen Navigation Flow | 76 |
| 14 | Gantt Chart of Project Activities | 119 |

[[PB]]

# Chapter 1 {-}

# INTRODUCTION AND PROJECT CONTEXT

## 1.1 Project Context

### Background of the Study

Finding a ride is still a daily struggle for many students at Talibon Polytechnic College who depend on tricycles, even though transportation to and from school should be the least of a student's concerns. Although tricycles are one of the most popular modes of transportation for employees, instructors, and students on campus, booking rides is still done the same way as it was years ago. In order to get a ride from a passing driver on the side of the road, passengers must still walk to a terminal or to locations where tricycle drivers wait for passengers seeking transportation. There is also no organized method for drivers to locate passengers; their earnings are mostly dependent on their ability to locate clients at the appropriate time and location. This transportation arrangement leads to wasted time, poor coordination between riders and drivers, and missed earning opportunities on both sides (Narayanan & Antoniou, 2021).

In larger cities, technology has already revolutionized the way people get rides, and there is no reason college campuses cannot adopt similar systems. Passengers can now communicate with drivers without having to be in the same physical location thanks to ride-hailing apps and digital scheduling platforms (Zhang et al., 2021). Additionally, studies reveal that waiting times decrease and transportation vehicles are utilized more efficiently when matching between drivers and passengers is managed by a system rather than left to chance (Cheng et al., 2024; Li et al., 2024).

### Existing Situation

Tricycle service around Talibon Polytechnic College runs on physical presence and personal familiarity. A passenger who needs a ride either walks to a terminal and waits for the queue to reach them, or stands at the roadside and hopes a vacant tricycle passes. A driver looking for passengers either holds a place in the terminal queue or roams the streets between the campus, the public market, and the port area, burning fuel while doing so.

Fares are governed by an ordinance. The Federation of Tricycle Operators and Drivers Association of Talibon publishes a fare schedule, enacted on 8 November 2022 as an amendment to Section 1 of Municipal Ordinance No. 2018-05, which fixes a rate for every destination in the municipality and sets a lower rate for senior citizens, persons with disabilities, and students. That schedule exists as a laminated sheet posted at terminals. A passenger who does not know the posted rate for their destination, and who cannot read a sheet that is not in front of them, has no way of checking what they are being charged.

Driver accountability rests on the same informal footing. Anyone who owns a tricycle can carry passengers. There is no step at which a driver's licence, registration, or fitness to operate is checked before a passenger gets in.

### Current Problems

Four problems follow from that arrangement.

The first is **unpredictable waiting time**. A passenger cannot tell whether the wait will be one minute or fifteen, because there is no information about which drivers are nearby or available. This matters most at the times it matters most, which are the minutes before a class begins.

The second is **idle driver capacity**. A driver waiting in a terminal queue and a passenger waiting three streets away are both waiting, and neither knows about the other. Time that could have been a paid trip is spent stationary or roaming.

The third is **fare uncertainty**. The posted schedule is authoritative but not accessible at the point of decision. Passengers who do not know the rate cannot verify it, and drivers who quote correctly have no way of demonstrating that they have.

The fourth is **the absence of accountability**. Without registration, verification, or any record of who carried whom, a passenger with a complaint has nowhere to bring it, and a driver wrongly accused has nothing to point to.

### Need for the Proposed System

The gap is not a shortage of tricycles and it is not a shortage of passengers. It is that the two cannot see each other, and that the rules already written down are not available where decisions are made. A digital platform closes both gaps at once: it lets a passenger and a driver find each other without being in the same place, and it puts the published fare schedule in the passenger's hand before they agree to the ride.

Kumar and Singh (2023) showed that digital transportation platforms work in smaller local settings as well as in cities, and that the improvement is often larger in small settings precisely because the alternatives are fewer. Adding a structured onboarding procedure, in which drivers register, submit documents, and are verified before they are permitted to operate within the system, addresses the accountability gap that the present informal arrangement cannot.

To address these issues, this study developed a Smart Tricycle Ride and Driver Onboarding System for Talibon Polytechnic College.

## 1.2 Purpose and Description of the Project

The purpose of this project is to improve access to tricycle transportation for the Talibon Polytechnic College community by replacing an unstructured, presence-based arrangement with a coordinated digital one.

The system developed is a mobile application for Android that enables students, faculty, and staff to request tricycle rides from their phones and enables drivers to receive and respond to those requests. Rather than waiting at a terminal or searching for a passing driver, a user opens the application, selects a destination from the official fare schedule, sees the exact fare, and submits a request that is broadcast to every driver currently online. The first driver to accept is matched to the passenger, and both parties then follow the same ride through a shared sequence of status changes from acceptance to completion.

Alongside booking, the system provides a driver onboarding module. A driver registers, submits a licence number, licence expiry date, and tricycle number, and remains unable to accept passengers until an administrator has reviewed and approved those details. This ensures that the platform is used only by drivers whose credentials have been checked, which the present informal arrangement cannot achieve.

An administrative module completes the system. It gives the college or the drivers' association a single place to verify drivers, maintain the fare table, review concerns raised by passengers or drivers, observe activity as it happens, and export records of that activity for reporting.

The system is a change in how a daily routine is coordinated, not only a piece of software.

## 1.3 Objectives of the Study

### General Objective

To develop a Smart Tricycle Ride and Driver Onboarding System that improves the efficiency, transparency, and reliability of tricycle transportation services for Talibon Polytechnic College.

### Specific Objectives

1. To design and develop a ride-booking and scheduling system for tricycle services that addresses the inefficiency and long waiting times experienced by students at Talibon Polytechnic College.
2. To create a driver onboarding module for the registration and verification of tricycle drivers, so as to improve accountability and ensure that only authorized drivers serve the college community.
3. To enable real-time ride requests and driver-passenger matching that reduce idle time for drivers and remove the need for students to search manually for available tricycles.
4. To reduce passenger waiting time and improve driver utilization.
5. To provide a system for monitoring rides and managing driver activity.
6. To evaluate the system in terms of usability, functionality, efficiency, and reliability using the ISO/IEC 25010 software quality model.

## 1.4 Statement of the Problem

This study developed a Smart Tricycle Ride and Driver Onboarding System for Talibon Polytechnic College. Specifically, it sought to answer the following questions:

1. What are the common problems encountered in the current tricycle transportation system in Talibon, Bohol in terms of:
   a. ride availability;
   b. passenger waiting time;
   c. fare transparency; and
   d. driver-passenger coordination?

2. What features should be included in the proposed system in terms of:
   a. ride scheduling and booking;
   b. driver onboarding and verification;
   c. fare computation based on the published schedule; and
   d. notification and monitoring?

3. How effective is the developed system as evaluated by passengers and drivers in terms of:
   a. usability;
   b. functionality;
   c. efficiency; and
   d. reliability?

4. Is there a significant difference between the evaluations of passenger-respondents and driver-respondents across the four quality characteristics?

## 1.5 Scope and Limitations

### Scope

This study covers the development of a Smart Tricycle Ride and Driver Onboarding System for Talibon Polytechnic College. The system:

1. Allows passengers to register, sign in, and maintain a profile that includes a photograph.
2. Allows passengers to request tricycle rides digitally, selecting a destination from the official FeTODAT fare schedule.
3. Displays the exact fare before the passenger commits to the booking, distinguishing the regular rate from the discounted rate for senior citizens, persons with disabilities, and students.
4. Broadcasts a ride request to all available drivers and matches the passenger with the first driver to accept.
5. Enables drivers to register, submit credentials, toggle their availability, receive and manage ride requests, and record the progress of a ride from acceptance to completion.
6. Provides driver registration and verification controlled by an administrator, so that unverified drivers cannot accept passengers.
7. Gives both parties a shared, live view of ride status, and a history of completed rides.
7a. Displays an interactive map on the booking and tracking screens, and shows the assigned driver's position as it changes while the ride is under way.
7b. Allows a passenger to set a pickup point either by searching the table of stops or by pinning any point on the map, with the pinned point described in words where the device can resolve an address.
8. Allows passengers and drivers to raise concerns, and allows an administrator to review, annotate, and resolve them.
9. Notifies users in-app of the events that concern them.
10. Provides an administrative fare table that can be searched, filtered, corrected, and extended without modifying the application's code.
11. Produces monthly, yearly, and cumulative reports of ride activity, driver performance, and concerns, exportable either as a printable document with summary figures and charts or as a comma-separated-value file.

### Limitations

1. The system is limited to tricycle transportation serving Talibon Polytechnic College and its immediate vicinity.
2. The system requires tricycle drivers to own a mobile device capable of running the application, which targets Android 7.0 (API level 24) and above.
3. The system requires an internet connection. It does not operate offline.
4. The system does not include online payment integration. All fares are settled in cash between the passenger and the driver.
5. The system does not cover other transportation types such as jeepneys, buses, or motorcycle taxis.
6. Turn-by-turn navigation is not computed by the system. The booking and tracking screens show an interactive map with the relevant positions on it, but no driving route is calculated, because route calculation is a billed web service and the study operates at no cost. Where the point concerned has coordinates, a driver may hand it to Waze or Google Maps on their own device, which navigates at no cost to the study.
7. A driver's position is published only while the application is open and the driver is marked online. There is no background tracking, so a passenger sees no position for a driver who has closed the application.
8. Destinations are selected from the published FeTODAT schedule rather than by pinning a point on the map, because the governing ordinance fixes the fare for each named destination. The map may be used to find a destination — the system names the posted stop nearest to the point the passenger indicates, together with its fare — but the ride is booked and priced against that named stop, not against the point. A pickup point may be pinned freely, as it does not affect the fare.
9. Fare stops carry a map position only where an administrator has supplied one. A stop without coordinates books and prices normally but does not appear on the map.
10. Push notifications are delivered within the application. Delivery of notifications to a device that is not running the application requires a server-side component that is outside the scope of this study.
11. Sign-in is by email address and password. Federated sign-in through third-party identity providers is not included.
12. The accuracy of the fare table depends on the accuracy of the published FeTODAT schedule as transcribed. Rows that could not be read with confidence from the posted sheet are flagged within the administrative interface for verification, and rows without a usable rate are disabled so that they cannot price a ride.
13. Driver verification is documentary. An administrator confirms that a photograph of a licence was submitted and that it matches the details the driver entered, but the system cannot confirm with the Land Transportation Office that the licence is current or has not been suspended, because no public interface exists for that check.
14. Implementation of the system beyond the study is subject to the cooperation and coordination of the local tricycle drivers' association in Talibon, Bohol.
15. Adoption depends on the willingness of drivers and passengers to use the system.

## 1.6 Significance of the Study

**Tricycle Drivers (Primary Beneficiaries).** Tricycle drivers stand to benefit the most from this system. A steady, organized stream of bookings means less time waiting at terminals and more time earning. The system also gives them a record of their trips and earnings, which helps them plan their day and see how they are doing over a week or a month.

**Students, Faculty, and Staff.** For students rushing to class, and for faculty and staff heading to campus, the ability to book a ride from a phone rather than search for one on the street is a meaningful improvement in the day. Seeing the official fare before the ride removes a second uncertainty.

**Talibon Polytechnic College.** A college that can offer organized, safe, and accessible transportation reflects well on the institution. This system contributes to that by bringing a previously informal service into a structured, accountable framework.

**College Administration.** The system gives administrators a clearer view of transportation activity around the campus. With driver records, ride logs, and concerns held in one place, and with reports that can be exported for any month, year, or range of dates, it becomes easier to monitor operations, address complaints, and make evidence-based decisions about transportation policy.

**The Federation of Tricycle Operators and Drivers Association of Talibon.** Placing the published fare schedule inside an application that both parties can see supports the association's own rate-setting work, and gives it a record of disputes it can act on.

**Future Researchers.** This study can serve as a practical starting point for other researchers developing or improving transportation systems for small municipalities, and the methods documented here may serve as useful references.

## 1.7 Research Methodology

### Research Design

The study employed a **descriptive-developmental research design**. The descriptive component involved identifying and documenting the existing problems in the current tricycle transportation system at Talibon Polytechnic College through a structured needs assessment administered to students and tricycle drivers. The findings from this phase served as the basis for the developmental component, in which the researchers designed, developed, and evaluated the Smart Tricycle Ride and Driver Onboarding System.

### Development Model

Development followed the **Waterfall model** of the Software Development Life Cycle. The Waterfall model organizes development into sequential phases in which each phase is completed and reviewed before the next begins, as shown in Figure 12.

The Waterfall model was selected for three reasons. First, the requirements of the system were established early and did not change substantially during development, which is the condition under which a sequential model performs well. Second, the model produces documentation at each phase, which suits the reporting requirements of a capstone project. Third, the study had a fixed academic timeline, and a model with defined phase boundaries made progress measurable against that timeline.

The phases as applied in this study were:

**Requirements Analysis.** The problems in the existing arrangement were documented, and the functional and non-functional requirements of the system were derived from them and from the objectives of the study.

**System Design.** The architecture, database structure, process flows, and interfaces were designed and documented as the diagrams presented in Section 4.3.

**Implementation.** The application was coded in Kotlin using Jetpack Compose, against Firebase services.

**Testing.** The system was subjected to unit, integration, system, user acceptance, performance, and security testing as described in Section 4.5.

**Deployment.** The application was distributed to respondents for the evaluation period.

**Maintenance.** Defects reported during evaluation were corrected, and the fare table was updated as corrections to the transcribed schedule were confirmed.

### Research Flow

The research flow of this study illustrates the systematic process followed in conducting the study, from identifying the problem through to the formulation of conclusions and recommendations.

![Figure 1. Research Flow](figures/fig01_research_flow.png){width=6.0in}

## 1.8 Research Environment

The study was conducted at Talibon Polytechnic College, located in San Isidro, Talibon, Bohol, Philippines. The college serves as a centre for technical and vocational education in Talibon, with a population of students, faculty, and staff who rely on tricycle transportation for their daily commute to and from the campus.

The research environment specifically covered the immediate vicinity of the college, including the main campus entrance, nearby tricycle terminals, and the surrounding barangays from which most commuters originate. This setting was selected because of the high volume of daily tricycle transactions involving the college community and the clear need for a more organized and digital approach to transportation within the institution.

The fare schedule used by the system was obtained from the Federation of Tricycle Operators and Drivers Association of Talibon, whose published rates cover twelve zones across the municipality: Balintawak, Santo Niño, San Francisco, San Agustin, the combined zone of Bagacay, Burgos and Rizal, Zamora, San Carlos, Tanghaligue, San Isidro, San Jose, San Pedro, and San Roque.

## 1.9 Research Participants and Respondents

The respondents of this study consisted of three groups, corresponding to the three roles the system serves. Forty-one respondents took part in the evaluation.

**Students of Talibon Polytechnic College.** Students were selected because they represent the primary users of the passenger side of the system, being the main commuters who depend on tricycle transportation for daily travel to and from the campus. **Purposive sampling** was used: the respondent had to have installed the application and used it to book at least one ride during the evaluation period, since a respondent who has not used the system cannot evaluate it. Twenty students met that condition and completed the instrument. They were aged 18 to 22, with a mean age of 19.70; twelve were female and eight male; twelve were enrolled in BSIS, four in BSAIS, three in BECED and one in BSA; and thirteen were in their third year of study, six in their second, and one in their first.

**Tricycle drivers serving the college community.** Drivers were included because they are the service providers whose operations the system aims to improve. **Total enumeration** was used for this group, given the limited number of drivers serving the campus: every driver onboarded to the system during the study was asked to evaluate it, and twenty completed the instrument. They were aged 38 to 68, with a mean age of 53.25, and all were male. Thirteen reported more than ten years of driving experience. All twenty owned a smartphone with internet access, which was a precondition of using the system.

**The system administrator.** The administrator role is held by one person, so **total enumeration** of that role yields a single respondent. The administrator's returns are reported separately throughout and are not pooled with the two groups of twenty, because a mean computed over one respondent describes an individual rather than a group.

Enrolment at the college and the total number of tricycle drivers operating around the campus were not certified for this study, so no sampling fraction is claimed for the student group. The twenty student respondents are described as a purposive sample of users, not as a proportion of a population, and Section 5.4 records the limits this places on generalisation.

## 1.10 Research Instruments

The primary data instrument used in this study is a structured researcher-made questionnaire, prepared in two versions: one for tricycle drivers and one for passenger-respondents.

Each version gathers information on the existing problems in the current tricycle transportation arrangement and evaluates the developed system. The evaluation portion was adapted from the **ISO/IEC 25010 software quality model** (International Organization for Standardization, 2011). Every statement is rated on a five-point Likert scale ranging from 5 (Strongly Agree) to 1 (Strongly Disagree), and each version closes with four open-ended questions inviting comment on the feature the respondent valued most, the problems observed, the improvements recommended, and any further remarks.

The three versions differ in length because the roles differ in what they can assess. The **passenger instrument** carries thirty-four statements across six characteristics: functional suitability (eight statements), usability (six), efficiency (five), reliability (five), security (five), and overall satisfaction (five). The **driver instrument** carries twenty statements across the four characteristics named in the Statement of the Problem — usability, functionality, efficiency, and reliability — with five statements each. The **administrator instrument** carries seven statements across three characteristics: functional suitability (three), usability (two), and reliability (two), reflecting the narrower set of functions available to that role. Usability, functionality, efficiency, and reliability are common to the passenger and driver instruments, and it is these four that the comparison in the fourth research question concerns.

The questionnaire was reviewed and validated by the research adviser and by subject matter experts before distribution to ensure content validity and clarity. A pilot test was conducted with a small group of respondents to refine the instrument prior to the actual data collection. The complete instrument appears as Appendix C.

## 1.11 Data Gathering Procedure

The study followed a systematic procedure to ensure the proper development, implementation, and evaluation of the system.

**Securing Permission.** A letter of request was submitted to the Office of the College President of Talibon Polytechnic College and to the officers of the Federation of Tricycle Operators and Drivers Association of Talibon, seeking permission to conduct the study, to administer the needs assessment and evaluation, and to reproduce the published fare schedule within the application. The letters appear as Appendix B.

**Needs Assessment.** A structured needs assessment was administered to students and tricycle drivers to document the problems in the current arrangement. The responses established the functional requirements set out in Section 4.1.

**System Planning and Design.** The researchers identified the tools and components required for development, comprising Android Studio, Kotlin, Jetpack Compose, and Firebase Authentication, Realtime Database, and Cloud Messaging. A system architecture diagram and interface wireframes were prepared to guide development.

**System Development.** The passenger, driver, and administrator interfaces were built in Kotlin and Jetpack Compose according to the approved design. The system was configured to accept ride requests, price them from the FeTODAT schedule, match passengers with available drivers, and process driver registration and verification.

**System Integration.** All components were integrated and tested as a complete system. This included verifying communication between the application and Firebase Authentication for sign-in, the Realtime Database for live data and profile photographs, and Cloud Messaging for notifications. Defects found were corrected through debugging and adjustment.

**Testing.** The system underwent unit, integration, system, user acceptance, performance, and security testing using both simulated and live ride requests. The researchers recorded the system's ability to match passengers with drivers correctly, to price rides according to the posted schedule, to process bookings, and to deliver notifications.

**Deployment and Evaluation.** The application was distributed to selected respondents, who used it under real conditions and then completed the evaluation questionnaire.

**Data Collection.** Completed questionnaires and recorded test results were collected, organized, and prepared for analysis.

**Data Analysis.** The collected data were treated by the study's statistician using **frequency count**, **weighted mean**, and **ranking**.

*Frequency count* records how many respondents chose each point on the scale for each item, and is the basis of every figure that follows.

*Weighted mean* summarises each item as a single value. It is computed as

$$\bar{x} = \frac{\sum_{i=1}^{5} w_i f_i}{N}$$

where $w_i$ is the weight of a scale point, 5 down to 1; $f_i$ is the number of respondents who chose it; and $N$ is the number who answered the item. Each item mean is read against the scale printed on the questionnaire, in which 4.21 to 5.00 is interpreted as *Strongly Agree*, 3.41 to 4.20 as *Agree*, 2.61 to 3.40 as *Neutral*, 1.81 to 2.60 as *Disagree*, and 1.00 to 1.80 as *Strongly Disagree*. A category mean is the mean of the item means within that quality characteristic, and an overall mean is the mean of the category means, so that each characteristic carries equal weight regardless of how many items measure it.

*Ranking* orders the items within a characteristic, and the characteristics within an instrument, from the highest weighted mean to the lowest, so that the strongest and weakest aspects of the system can be identified. Tied values are assigned the midpoint of the positions they jointly occupy.

This treatment is descriptive, and the computation appears in full as Appendix G.

The fourth research question asks whether the passenger and driver evaluations differ significantly across the four characteristics both instruments measure, which a descriptive treatment cannot answer. That comparison was computed separately by the researchers using the **Mann-Whitney U test**, a non-parametric test appropriate to ordinal Likert data and to samples of twenty per group, applied at the 0.05 level of significance. It supplements the descriptive treatment rather than replacing it, and no descriptive figure reported in this study depends on it. The result appears as Table 36 in Section 4.9.

Together these results form the basis of the conclusions and recommendations in Chapter 5.

**Ethical Considerations.** Participation was voluntary, and respondents were informed of the purpose of the study before taking part. Within the application itself, users are required to read and accept the Terms and Conditions, the Privacy Policy, and the Safety and Community Guidelines before the service becomes available to them, and drivers additionally accept the Driver Agreement; the acceptance is recorded against the account. Names were optional on the instrument. Personal data collected by the application itself, comprising name, email address, mobile number, date of birth, and an optional photograph, are held in the project's Firebase instance, are used only to operate the service, and are described to the user in the privacy notice available inside the application. No payment or financial information is collected by the system at any point.

## 1.12 Definition of Terms

**Administrator.** The user responsible for verifying drivers, maintaining the fare table, reviewing concerns, monitoring activity, and exporting reports. Also referred to as the system administrator.

**Booking Request.** A request made by a passenger through the system for a ride to a chosen destination, carrying the pickup point, the destination, the number of passengers, the luggage declared, and the priced fare.

**Discounted Rate.** The fare column of the FeTODAT schedule applicable to senior citizens, persons with disabilities, and students, set below the regular rate for the same destination.

**Driver Availability.** The status indicating whether a driver is currently accepting ride requests, controlled by the driver through an online and offline toggle.

**Driver Onboarding.** The process of registering, submitting credentials, and being verified by an administrator before a driver is allowed to accept passengers through the system.

**FeTODAT.** The Federation of Tricycle Operators and Drivers Association of Talibon, the body whose published schedule fixes tricycle fares within the municipality.

**Fare Stop.** A single priced destination in the fare table, belonging to a zone and carrying both a regular rate and a discounted rate.

**Fare Table.** The complete set of fare stops held in the system's database, seeded from the published FeTODAT schedule and maintained thereafter by the administrator.

**Jetpack Compose.** The declarative user interface toolkit for Android in which the application's screens are written.

**Minimum Fare.** The lowest amount that may be charged for any ride, set separately for the regular and discounted rate columns by the governing ordinance.

**Mobile Application.** A software application designed to run on smartphones, used in this study for booking rides and managing transportation services.

**Notification.** An alert generated by the system informing a user of an event that concerns them, such as a ride being accepted, a ride status changing, a driver's verification being decided, or a concern being resolved.

**Passenger.** A user who requests and uses transportation services through the system.

**Real-Time Processing.** The ability of the system to reflect a change in data on every connected device as the change occurs, without the user refreshing.

**Regular Rate.** The fare column of the FeTODAT schedule applicable to passengers who do not qualify for the discounted rate.

**Ride Matching.** The process of pairing a passenger with an available driver. In this system, a request is broadcast to all available drivers and matched to the first driver who accepts it.

**Ride Scheduling System.** A digital system that allows passengers to request rides and matches them with available drivers.

**Route.** The path taken by the driver from the passenger's pickup location to the destination.

**Smart Transportation System.** A technology-based system that uses digital tools and real-time data to improve the efficiency and management of transportation services.

**Transportation Efficiency.** The ability of the system to provide faster, more organized, and more reliable transportation with minimal delay.

**Tricycle Driver.** A registered individual who operates a motorized tricycle and provides transportation services to passengers within and around Talibon Polytechnic College.

**User Interface (UI).** The visual part of the system through which users interact with the application.

**Verification Status.** The state of a driver's application to operate within the system, being one of pending, approved, rejected, or expired.

**Zone.** A grouping of destinations in the FeTODAT schedule, corresponding to a barangay or a cluster of barangays within the municipality.

[[PB]]
# Chapter 2 {-}

# REVIEW OF RELATED LITERATURE AND SYSTEMS

## 2.1 Related Literature

Transportation has always adapted to the tools available to it, and the rise of digital technology has pushed that adaptation further and faster than before. Communities around the world are turning to smart transportation systems not only to move people more efficiently but to improve the whole experience of getting from one place to another. These systems bring together mobile applications, real-time data, and coordinating platforms to give passengers and service providers a shared, responsive environment (Wang et al., 2022).

Ride-hailing platforms are the most visible example of this shift. Instead of standing on the street hoping for a vehicle, a user taps a button and knows when a driver will arrive. Zhang et al. (2021) noted that these applications have made it considerably easier to find transportation by linking passengers directly with nearby available drivers, removing the uncertainty and wasted time that came with the older arrangement.

Ride scheduling adds structure to the matching process. Rather than simply connecting whoever is nearby, a scheduling system factors in timing, preference, and real-time availability to produce more reliable matches. Cheng et al. (2024) found that this approach measurably reduces how long passengers wait and helps ensure vehicles are used productively through the day.

Driver management is a part of the problem that receives less attention. A transportation platform is only as trustworthy as the people providing the service. Dastani et al. (2024) pointed out that a system for registering, verifying, and monitoring drivers does more than keep records: it improves the reliability of the service and gives passengers confidence in who is picking them up.

Smart transportation is not exclusive to large cities. Kumar and Singh (2023) showed that digital transportation platforms work in smaller local settings, and that the impact there can be greater, because those communities have fewer alternatives. Where a simple digital tool replaces an unstructured arrangement, the improvement in daily life is substantial.

A dimension less often examined in this literature is **fare transparency**. In many Philippine municipalities, tricycle fares are fixed by local ordinance and posted physically at terminals. The rate is therefore public in principle but not available at the moment a passenger decides to travel. Placing that published schedule inside the booking interface converts a rule that exists on paper into information the passenger holds before agreeing to the ride, which is a distinct contribution separate from matching efficiency.

## 2.2 Related Studies

A number of studies have examined how ride scheduling and digital transportation platforms can be improved, each contributing a different angle.

Cheng et al. (2024) addressed fairness in ride scheduling by designing a system that accounts for user preferences when pairing passengers with drivers. Their finding that efficiency and user satisfaction need not be in conflict is directly relevant: when a system reflects what users actually want, they are more likely to use it and to trust it.

Rapp et al. (2023) approached the problem from a last-mile perspective, developing an on-demand ride-sharing system for autonomous buses. Their work demonstrated that dynamic, real-time scheduling, in which the system adjusts to incoming requests rather than following fixed routes, meaningfully reduces waiting time while maintaining service performance.

Huang et al. (2024) examined vehicle routing and how better route planning affects broader transportation outcomes. Their study found that modest gains in route efficiency translated into significantly lower operating costs and faster service, a reminder that the logistics behind a transportation system matter as much as its user-facing features.

Narayanan and Antoniou (2021) conducted a systematic review of ride-sharing platforms to understand what drives adoption and what discourages it. Their findings identified convenience, reliability, and ease of use as the three factors that most shape whether a platform succeeds. These are not complicated expectations, and they map closely onto the usability, efficiency, and reliability characteristics used to evaluate the present system.

Li et al. (2024) focused on the first- and last-mile problem in ridesharing, the segments of a journey that tend to be least well served. By optimizing how vehicles of different types are deployed across those segments, their study showed that scheduling efficiency and service quality can be improved together rather than traded against each other.

Taken together, these studies establish that system-managed matching outperforms chance-based matching, that user preference and fairness affect adoption, and that the benefits are not confined to large fleets or large cities. What they do not address is the specific case of a fare regime fixed by local ordinance and enforced socially rather than algorithmically, which is the condition under which tricycle service in Talibon operates.

## 2.3 Comparison of Related Systems

Table 1 compares the developed system with commercial ride-hailing platforms operating in the Philippines and with the current unstructured arrangement it is intended to replace.

: Table 1. Comparison of Related Systems

| System | Features | Strengths | Weaknesses |
|:---|:---|:---|:---|
| **Grab** (Grab Holdings) | Multi-service ride-hailing covering cars, delivery, and payments; live GPS tracking; cashless and cash payment; in-app rating; dynamic pricing | Mature and heavily tested; large driver supply in served cities; integrated wallet; strong support infrastructure | Does not serve tricycles; not available in Talibon; dynamic pricing is incompatible with an ordinance-fixed fare regime; commission model reduces driver earnings |
| **Angkas** | Motorcycle taxi booking; fixed distance-based fare; driver accreditation and training; helmet provision | Formalized an informal transport mode; strong driver screening; fare shown before booking | Motorcycle taxis only; operates in designated metropolitan areas and not in Talibon; central operator model unsuitable for a municipal drivers' association |
| **JoyRide** | Motorcycle taxi and delivery; in-app booking; fixed fare display | Fare visible before booking; covers several Philippine cities | Motorcycle taxis only; not available in Talibon; no tricycle support |
| **Current arrangement in Talibon** (terminal queue and roadside hailing) | Physical queueing; verbal fare agreement; fare schedule posted at terminals | No technology or literacy barrier; no data cost; works during power or network outages | Unpredictable waiting time; drivers idle or roaming; posted fare not available at the point of decision; no driver verification; no record of trips or complaints |
| **TrikRide** (developed system) | Tricycle ride booking; destination selected from the official FeTODAT schedule; fare shown before booking with separate regular and discounted rates; administrator-controlled driver verification; live ride status; concern reporting; administrative monitoring and report export | Prices from the governing ordinance rather than a formula; discounted rate for seniors, persons with disabilities, and students is built in; verification gate before a driver can operate; fare table editable by the administrator without a software release; reports exportable for any month, year, or range of dates | Requires an Android device and an internet connection; no turn-by-turn navigation; no online payment; coverage limited to Talibon; adoption depends on the drivers' association |

Three observations follow from the comparison. First, no existing platform serves tricycles in Talibon, so the developed system does not displace an incumbent; it addresses a gap. Second, every commercial platform computes fares from distance and demand, which is the wrong model where a municipal ordinance fixes the price per destination. Third, none of the commercial platforms implements the statutory discount for senior citizens, persons with disabilities, and students, because none operates under a fare regime that mandates one.

## 2.4 Theoretical Framework

This study is grounded in three theories that together explain whether a system of this kind will be adopted, whether it will fit the work it is meant to support, and how its success should be judged.

### Technology Acceptance Model

The Technology Acceptance Model (Davis, 1989) holds that a user's intention to use a technology is determined chiefly by two beliefs: **perceived usefulness**, the degree to which the person believes the system will improve their performance, and **perceived ease of use**, the degree to which they believe using it will be free of effort.

The model applies directly here. A driver will adopt the system if it brings more paid trips than roaming does, which is perceived usefulness, and if operating it requires no more than a toggle and a button, which is perceived ease of use. A passenger will adopt it if it produces a ride faster than walking to a terminal, and if booking takes fewer taps than the alternative takes minutes. The design decisions in this study follow from that: the driver interface reduces the core task to a single online toggle and an accept button with a visible countdown, and the passenger's booking path is a linear sequence with the fare shown before commitment.

The usability and efficiency characteristics of the evaluation instrument correspond to perceived ease of use and perceived usefulness respectively.

### Task-Technology Fit

Task-Technology Fit (Goodhue & Thompson, 1995) holds that a technology improves performance only when its capabilities match the demands of the task. A well-built system applied to a task it does not fit produces no benefit.

The task in this study has a specific shape. Fares are fixed per destination by ordinance, not computed from distance. Trips are short and local. Drivers work from a small set of known terminals. Passengers travel to a known set of destinations. A technology that fits this task must therefore price from a lookup table rather than a formula, must offer destinations from a defined list rather than an open map search, and must work on inexpensive Android devices over an intermittent mobile connection.

This theory accounts for the central design decision of the study, which is that the fare engine performs a lookup against the published schedule rather than a distance calculation. A distance-based fare engine would be a poorer fit for the task even though it is the more common design. The fit extends beyond the calculation itself: because the schedule the system prices from is the one the association already publishes and posts, the system introduces no pricing scheme that a driver or a passenger has to learn or accept.

### Information Systems Success Model

The DeLone and McLean Information Systems Success Model (DeLone & McLean, 2003) holds that the success of an information system is a function of system quality, information quality, and service quality, which shape use and user satisfaction, which in turn produce net benefits.

In this study, system quality is measured by the reliability and efficiency characteristics of the evaluation instrument. Information quality is addressed by the accuracy of the fare table, which is why rows transcribed with any uncertainty are flagged for verification and rows without a usable rate are disabled rather than left to price a ride with a wrong number. Net benefits correspond to the reduction in passenger waiting time and the improvement in driver utilization named in objective four.

## 2.5 Conceptual Framework

The conceptual framework of this study follows the Input-Process-Output model. It shows how the data entering the system are transformed into outcomes that address the problems identified in Section 1.1.

![Figure 2. Conceptual Framework of the Smart Tricycle Ride and Driver Onboarding System for Talibon Polytechnic College](figures/fig02_conceptual_framework.png){width=6.0in}

**Input.** The input stage comprises the data required for the system to operate: passenger ride requests carrying a pickup point, a destination, a passenger count, and any luggage; driver availability status submitted through the driver interface; driver registration credentials submitted during onboarding; the published FeTODAT fare schedule; and the account credentials and assigned role used for authentication.

**Process.** The process stage is the operation of the system. The system authenticates the user and routes them to the interface for their role. It verifies a driver's submitted credentials against the registration criteria before granting the ability to accept rides. It prices a requested ride by looking up the destination in the fare table, selecting the rate column that applies to the passenger, applying the ordinance minimum where the posted rate falls below it, and multiplying by the number of passengers. It broadcasts the request to every available driver and matches the passenger with the first to accept. It tracks the ride through its status changes and notifies both parties at each one. It records concerns raised by either party and routes them to the administrator.

**Output.** The output stage is the result: a confirmed booking with an assigned driver, an agreed fare, and live status visible to both parties; a verified driver profile, approved or rejected; a stored record of every ride and every concern; and monthly, yearly, and cumulative reports of activity. The intended outcomes are reduced passenger waiting time, improved driver utilization, fare transparency at the point of decision, and a more accountable tricycle transportation service for the Talibon Polytechnic College community.

**Feedback.** The framework includes a feedback path. Evaluation results and fare corrections identified in use return to the process stage, where the administrator amends the fare table and the researchers address defects. This reflects the maintenance phase of the development model.

## 2.6 Synthesis

The literature and studies reviewed converge on several points that shaped this study.

Digital matching outperforms chance-based matching. Zhang et al. (2021), Cheng et al. (2024), and Li et al. (2024) each report reduced waiting time and better vehicle utilization when a system rather than physical proximity determines who serves whom. This supports objectives one, three, and four of the present study.

Driver management is not administrative overhead but a determinant of service quality. Dastani et al. (2024) connected registration and verification directly to reliability and passenger confidence, which supports objective two and the verification gate implemented in this system.

Scale is not a precondition. Kumar and Singh (2023) found the benefits of digital transportation platforms present, and in some respects amplified, in smaller local settings. This supports the choice of a single municipality as the study environment.

Adoption depends on convenience, reliability, and ease of use. Narayanan and Antoniou (2021) identified these as the decisive factors, and they align with the Technology Acceptance Model and with three of the four characteristics in the evaluation instrument.

Two gaps in the reviewed work motivated the specific contribution of this study.

The first is the **fare model**. Every system reviewed prices rides by distance, by time, or by demand. None addresses a setting where a local ordinance fixes the fare for each destination and mandates a discount for particular passengers. Pricing by lookup against a published schedule, with a statutory minimum and a discounted column, is a requirement produced by the study environment rather than borrowed from the literature.

The second is **transparency as an outcome in its own right**. The reviewed literature treats fare display as a convenience feature. Where fares are fixed by ordinance but posted only at terminals, placing the schedule in the passenger's hand at the moment of decision is a substantive change in the relationship between the two parties, not a convenience.

This study therefore adopts the matching and onboarding approaches established in the literature and adds a fare mechanism appropriate to a municipality where prices are set by ordinance rather than by the market.

[[PB]]

# Chapter 3 {-}

# TECHNICAL BACKGROUND

This chapter documents the technical foundation of the developed system: what is required to build it, what is required to run it, and how its parts are arranged.

## 3.1 Software Requirements

### Development Environment

| Component | Specification |
|:---|:---|
| Integrated Development Environment | Android Studio Ladybug (2024.2.1) or later |
| Java Development Kit | JDK 17 (bundled with Android Studio) |
| Android Gradle Plugin | 8.7.3 |
| Gradle | 8.11.1, pinned by the project wrapper |
| Kotlin | 2.1.0 |
| Compile SDK | Android API level 35 |
| Target SDK | Android API level 35 (Android 15) |
| Minimum SDK | Android API level 24 (Android 7.0 Nougat) |
| Version control | Git, with the repository hosted on GitHub |
| Operating system | Windows 10 or later, macOS 12 or later, or a current Linux distribution |

The minimum SDK of API level 24 was chosen deliberately. Android 7.0 was released in 2016, and devices running it or later account for the overwhelming majority of Android devices still in use. Setting the floor lower would have required abandoning several libraries used by the project; setting it higher would have excluded drivers using older handsets, which is precisely the group the system needs to reach.

### Runtime Environment for End Users

| Component | Requirement |
|:---|:---|
| Operating system | Android 7.0 (API level 24) or later |
| Connectivity | Mobile data or Wi-Fi; the application does not operate offline |
| Optional | A Google Maps API key. Without one the system renders OpenStreetMap instead; no function is lost |
| Storage | Approximately 30 MB for installation |
| Permissions | Internet; camera, for capturing a profile photograph; notification posting on Android 13 and later. Choosing an existing photograph requires no permission, and neither does saving an exported report. |
| Account | A valid email address for registration |

### Third-Party Libraries and Services

| Library or service | Version | Purpose |
|:---|:---|:---|
| Jetpack Compose BOM | 2024.12.01 | Declarative user interface toolkit |
| Material 3 | via Compose BOM | Design system components and theming |
| Firebase BOM | current stable | Version alignment across Firebase libraries |
| Firebase Authentication | via Firebase BOM | Email and password sign-in and session management |
| Firebase Realtime Database | via Firebase BOM | Live data storage and synchronization |
| Firebase Cloud Messaging | via Firebase BOM | Notification delivery |
| Google Play Services Maps | 18.2.0 | Google Maps rendering, used when a key is configured |
| osmdroid | 6.1.20 | OpenStreetMap rendering, used when no key is configured |
| Google Play Services Location | 21.3.0 | Device position from the fused location provider |
| Kotlin Coroutines | bundled with Kotlin 2.1.0 | Asynchronous work and reactive data streams |
| AndroidX Lifecycle ViewModel Compose | current stable | ViewModel integration with Compose |
| AndroidX Activity Compose | current stable | Activity result contracts for camera, gallery, and file creation |

## 3.2 Hardware Requirements

### Development Workstation

| Component | Minimum | Recommended |
|:---|:---|:---|
| Processor | Dual-core x86-64, 2.0 GHz | Quad-core x86-64 or Apple Silicon |
| Memory | 8 GB RAM | 16 GB RAM |
| Storage | 20 GB free | 50 GB free, solid-state |
| Display | 1280 × 800 | 1920 × 1080 or higher |
| Network | Broadband, for dependency resolution and Firebase access | Broadband |

The recommended memory figure is not decorative. Android Studio with the Gradle daemon and an emulator running will use most of 16 GB, and a build machine with 8 GB will complete builds but slowly.

### Target Device

| Component | Minimum | Recommended |
|:---|:---|:---|
| Operating system | Android 7.0 (API 24) | Android 11 or later |
| Processor | Quad-core, 1.4 GHz | Octa-core |
| Memory | 2 GB RAM | 3 GB RAM or more |
| Storage available | 100 MB | 250 MB |
| Display | 4.7 inches, 720 × 1280 | 6.0 inches, 1080 × 2340 |
| Camera | Rear camera, for profile photograph capture | Any |
| Connectivity | 3G mobile data | 4G LTE or Wi-Fi |

### Server-Side Hardware

The system requires no server hardware procured or maintained by the college. All backend functions are provided by Google Firebase, a managed backend-as-a-service platform. This is a deliberate architectural choice: a capstone project deployed to a municipal drivers' association cannot depend on a physical server that someone must house, power, secure, and administer after the researchers graduate.

## 3.3 Programming Languages

**Kotlin 2.1.0** is the language in which the entire application is written. Kotlin is the language Google designates as preferred for Android development. Three of its properties mattered to this project. Its type system distinguishes nullable from non-nullable references at compile time, which eliminates an entire category of runtime crash. Its coroutine support makes asynchronous work, of which a networked application has a great deal, readable as sequential code. Its data classes generate equality, copying, and destructuring automatically, which suits the model layer of this system where a data class per entity is the whole of the definition.

**Kotlin DSL for Gradle** is used for the build configuration, in place of the older Groovy syntax, giving the build scripts the same type checking and editor support as the application code.

**XML** is used for Android resources that are not expressible in Compose: the application manifest, string and colour resources, launcher icon definitions, the file provider path configuration, and the backup and data extraction rules.

**Firebase Security Rules**, a JSON-based declarative language, is used to express server-side authorization for the Realtime Database.

## 3.4 Development Tools

**Android Studio** is the official integrated development environment for Android, and provided code editing, the Compose preview and layout inspector, the device emulator, the debugger, and Logcat for runtime diagnostics.

**Gradle 8.11.1 with Android Gradle Plugin 8.7.3** manages dependency resolution, compilation, resource processing, and packaging. The Gradle wrapper is committed to the repository so that every machine building the project uses the same Gradle version, which removes a class of build failure caused by version drift between developers.

**Git and GitHub** provide version control and a remote repository. Development proceeded on a feature branch, with the history serving both as a safety net and as a record of the development sequence.

**Figma** was used to prepare interface wireframes and the visual design before implementation, so that layout decisions were settled before code was written.

**Firebase Console** provides the web administration interface for the backend: creating the database, defining security rules, inspecting stored data, managing authentication, and reviewing usage.

**Graphviz and Matplotlib** were used to generate the system diagrams presented in Chapter 4 from textual descriptions, so that a change to a diagram is a change to a text file rather than a manual redraw.

## 3.5 Database Technologies

### Firebase Realtime Database

The system stores its data in **Firebase Realtime Database**, a cloud-hosted NoSQL database that holds all data as a single JSON tree and synchronizes changes to every connected client as they occur.

The Realtime Database was selected over a relational database and over Cloud Firestore for reasons specific to this application.

**Live synchronization is the core requirement.** A ride-hailing application is a system in which two parties must see the same state at the same time. When a driver accepts a request, the passenger's screen must change without the passenger doing anything. The Realtime Database delivers this through persistent listeners rather than polling, which is the natural fit for the problem.

**Latency matters more than query power.** The Realtime Database offers lower latency than Cloud Firestore for small, frequent updates, which is the access pattern of ride status changes. The queries this system performs are simple lookups and filters by a single field; it does not need the compound query support that would favour Firestore.

**Cost at the scale of this study is zero, and no payment method is required.** The Realtime Database free tier provides 1 GB of storage, 10 GB of monthly transfer, and 100 simultaneous connections, which is well beyond what a municipal pilot will consume. This is not merely an economy: a capstone project handed over to a college department and a drivers' association cannot depend on a recurring bill that somebody must agree to pay.

**Offline caching is built in.** The client library caches recent data and re-synchronizes when the connection returns, which mitigates the intermittent connectivity common in the study area.

The trade-off accepted is that a JSON tree provides no schema enforcement and no joins. This is mitigated by defining every entity as a Kotlin data class with default values for every field, so that the application layer imposes the structure the database does not, and by denormalizing the few relationships the system needs.

### Firebase Authentication

Account creation, sign-in, session persistence, and password reset are handled by **Firebase Authentication** using the email and password provider. No password is ever stored or transmitted by the application itself; credentials are exchanged directly between the Firebase client library and Google's authentication service, and the application receives only an opaque user identifier and a session token.

### Profile Photographs Without Object Storage

Firebase Cloud Storage would ordinarily hold an uploaded image, but Firebase requires the paid Blaze plan before a Storage bucket can be provisioned on a new project, and Blaze requires a payment method on file. Because a condition of this study is that the deployed system incur no cost and require no such commitment from the college, Cloud Storage is not used.

Profile photographs are instead reduced and stored in the Realtime Database. When a user selects an image, the application decodes it at a reduced sample size, crops it to a square, scales it to 256 pixels, and compresses it as a JPEG, stepping down through decreasing quality levels until the base64-encoded result fits within 24 kilobytes. An avatar displayed at 96 density-independent pixels needs no more resolution than this.

The encoded photograph is written to a `profilePhotos` node keyed by user identifier, rather than into the user record itself. This separation matters in practice: the administrative screens read every user record continuously to populate their lists, and an image embedded in each record would be transferred on every one of those reads. Held separately, a photograph is transferred only when it is actually displayed.

At 24 kilobytes per photograph, one thousand users would consume roughly 24 megabytes of the 1 gigabyte free allowance.

The same technique carries the one document the system does collect: a photograph of the driver's licence. Its budget is different, because its purpose is different. An avatar has only to resemble the person at 96 density-independent pixels; a licence must be legible enough for an administrator to read the number, the name, and the expiry date and compare them with what the driver typed. The long edge is therefore scaled to 1280 pixels rather than 256, the aspect ratio is preserved rather than cropped square — cropping would cut the ends of the licence number — and the budget rises to approximately 200 kilobytes. Thirty registered drivers consume around six megabytes, which is immaterial against the free allowance.

Licence photographs are written to a `driverDocuments` node rather than to the driver record, for the reason given above and for a second one addressed in Section 3.8: they are the only data in the system that are not readable by every authenticated account.

### Data Organization

The database is organized as nine top-level nodes, documented in Table 8 and Figure 11.

## 3.6 Network Architecture

The system uses a **client to cloud** architecture. There is no intermediate application server: the Android client communicates directly with Google Firebase services over the public internet.

Communication uses two channels. Authentication and one-off database reads and writes travel over **HTTPS**, secured by TLS 1.2 or later. Live database synchronization travels over a **persistent WebSocket connection** that the Firebase client library opens and maintains, over which the server pushes changes as they occur.

This arrangement has three consequences worth stating. It removes the need for the college to operate a server. It means that authorization must be enforced by Firebase Security Rules on the server side, because there is no application server in the path to enforce it. And it means the application is unusable without connectivity, which is recorded as a limitation in Section 1.5.

Firebase's endpoints are reached through Google's global content delivery infrastructure, so latency from Talibon is determined by the nearest edge location rather than by the distance to a single origin server.

## 3.7 Software Architecture

The application follows the **Model-View-ViewModel (MVVM)** pattern with an additional repository layer, which is the architecture Google recommends for Android applications.

**Model.** Kotlin data classes representing the entities the system handles: `User`, `Driver`, `Ride`, `RideRequest`, `FareStop`, `FareConfig`, `Complaint`, and `AppNotification`. Every field carries a default value, which is what allows Firebase to deserialize a partial record without failing.

**View.** Composable functions written in Jetpack Compose. Views hold no business logic. They render the state given to them and report user events upward. Because Compose is declarative, a change in state causes the affected part of the interface to be recomposed automatically, which removes the manual view-updating code that a traditional Android view hierarchy requires.

**ViewModel.** One ViewModel per role and concern: `AuthViewModel`, `PassengerViewModel`, `DriverViewModel`, `AdminViewModel`, `ProfileViewModel`, and `SupportViewModel`. Each exposes state as a `StateFlow` and accepts events as method calls. ViewModels survive configuration changes such as screen rotation, so state is not lost when a device is turned.

**Repository.** `AuthRepository`, `RideRepository`, `DriverRepository`, `AdminRepository`, `FareRepository`, and `SupportRepository` mediate between ViewModels and the data source. They expose suspending functions for one-off operations and `Flow` streams for live data. Because ViewModels depend on repositories rather than on Firebase directly, the data source could be replaced without touching any ViewModel.

**Service.** `FirebaseService` is the single point at which the application touches the Firebase Realtime Database. Live listeners are wrapped in `callbackFlow`, which converts Firebase's listener callbacks into Kotlin `Flow` streams and, importantly, removes the listener when the collecting coroutine is cancelled. This prevents the memory leaks that unbalanced listener registration causes.

**Domain logic.** Three objects hold logic that belongs to no single screen: `FareEngine`, which prices a ride from the fare table; `ReportBuilder`, which aggregates the figures the reports are built from; and `PasswordRules`, which evaluates password strength. `PdfReportWriter` and `PdfChart` turn those aggregates into the printable document.

Data flows in one direction. A user event goes from View to ViewModel to Repository to Service to Firebase. A data change comes back from Firebase through a Flow, through the Repository, into the ViewModel's state, and causes the View to recompose. This unidirectional flow makes the state of the interface a function of the data, which makes behaviour predictable and defects easier to locate.

## 3.8 Security Features

**Authentication.** Access requires an account authenticated by Firebase Authentication. Passwords are never handled by the application; they are transmitted directly to Google's authentication service over TLS and stored there as salted hashes. The application holds only a session token and an opaque user identifier.

**Password policy.** Registration enforces a policy stronger than the Firebase minimum. A password must be at least eight characters and must contain an uppercase letter, a lowercase letter, and a digit. The registration screen shows each rule and marks it as satisfied as the user types, so that a rejected password is a rare event rather than the normal experience of registering.

**Role-based access control.** Every account carries a `userType` of passenger, driver, or administrator, which determines which interface is presented after sign-in. A driver additionally carries a `verificationStatus`, and a driver whose status is not approved cannot accept ride requests regardless of what the interface offers.

**Server-side authorization.** Because the client speaks to Firebase directly, authorization is enforced by Firebase Security Rules evaluated on the server. The rules restrict a user to reading and writing their own profile, restrict driver verification status to administrators, and make the fare table readable by all authenticated users but writable only by administrators. Client-side checks are treated as user interface convenience, not as security.

Three of the rules deserve description, because their form follows from a constraint the platform imposes.

*Privilege cannot be granted to oneself.* A user may write their own record, and the field determining their role sits on it, so a validation clause restricts what that field may be set to: an account may declare itself a passenger or a driver and nothing else. Five separate rules grant administrative powers on the strength of that field, and without the clause any account could award itself all of them. Administrator accounts are created by editing the field in the Firebase console, which is not subject to the rules.

*Ride and concern records are scoped by requiring a query rather than by filtering.* A rule cannot return part of a collection: the platform either grants the node or refuses it. The rule therefore requires that the client asked a question already limited to itself — ordering by the passenger or driver identifier and matching its own — and refuses any broader request. The application issues exactly those queries, with the corresponding index declarations. An administrator is exempt and reads the collections whole, which is what the monitoring screen and the exported reports require.

*A ride is writable only by the driver carrying it.* Creating one requires that the new record name the caller as its driver, which is what acceptance does; thereafter only that driver may advance its status. Passengers do not write to ride records at all.

**Encryption in transit.** All communication uses TLS 1.2 or later. No data travels in plain text.

**Secrets management.** API keys and other confidential configuration are held in a `.env` file that is excluded from version control by `.gitignore`. The build reads that file and injects the values as manifest placeholders and build configuration fields. A committed `.env.example` documents which keys are required without disclosing their values. The Firebase configuration file `google-services.json` is likewise excluded from version control.

**No financial data.** The system collects no card numbers, no bank details, and no payment credentials of any kind. Fares are settled in cash between passenger and driver. This removes the entire category of risk associated with payment data.

**Data minimization.** The system collects name, email address, mobile number, date of birth, and an optional photograph for all users, plus licence and tricycle details for drivers. It collects nothing beyond what the service requires to operate.

**Sensitive personal information.** One item the system holds falls into a stricter category than the rest. Section 3(l) of the Data Privacy Act of 2012 (Republic Act No. 10173) classifies government-issued identifiers, and licences specifically, as *sensitive personal information*. The photograph a driver submits for verification is therefore treated differently from everything else the system stores, in four respects.

*Access.* The `driverDocuments` node is the only node in the database not readable by every authenticated account. Its security rule admits the driver it belongs to and administrators, and no one else. Passengers cannot reach it, it is never attached to a ride, and it appears in no exported report. An administrator's interface keeps each photograph collapsed until it is deliberately opened, so that a verification queue does not display a column of identity documents to whoever is standing nearby.

*Consent.* Agreement is obtained at the moment of upload rather than inferred from the Terms accepted at registration, and the dialogue that obtains it states in plain terms what the image is for, who can open it, and when it is destroyed. The moment of agreement is recorded with the document. A general consent given days earlier to a document few people read is not, in the view taken here, a meaningful basis for holding someone's identity document.

*Retention.* The retention rule is enforced in code and not left to a written policy. Refusing an application deletes the photograph in the same operation that records the refusal, since a refused applicant's licence serves no purpose the system has. An approved driver's photograph is retained while the account is active, because it is required again when the licence expires and if a concern about a ride is later disputed, and it is deleted with the account. Withdrawing an approval already granted is implemented as a separate operation from refusing an application, and deliberately does not delete the photograph: approval is usually withdrawn because a licence has lapsed or because a concern is under examination, and in either case destroying the document would remove the evidence the decision may later have to be justified against. A driver may withdraw the photograph themselves at any time.

*Location.* The licence number and expiry are held with the photograph rather than on the driver record. That record is readable by every authenticated account, because a passenger needs the availability and position kept on it, and a licence number is among the government-issued identifiers the Act names. Holding all three together also means there is one node to protect and one to destroy. A consequence is that an administrator sees the number where they see the photograph, behind a deliberate action rather than on the face of the verification card, and that the exported driver report no longer carries it: that report is a record of performance, it leaves the device, and an identity number has no business travelling with it.

*Proportionality.* No other document is requested. The system does not collect insurance certificates, inspection certificates, or secondary identification, all of which an earlier draft of the data model anticipated. Verification of the right to drive requires the licence, and requiring more would collect personal information the service has no use for.

**Ratings without a trusted server.** A passenger must be able to rate the driver who carried them, and must not be able to edit that driver's record. With no application server between the client and the database, both halves have to be expressed as rules. Each rating is therefore written to `driverRatings/{driver}/{rater}` — keyed by the person giving it, which is the only shape a rule can restrict to that person — and validated to a value between one and five. The driver's own device reads those ratings, averages them, and writes the figure onto its own record, which the administrative screens and the exported reports then read.

The consequence, recorded here rather than hidden, is that a newly given rating reaches an administrator's view when the driver next opens the application rather than at the moment it is given. Removing that lag would require either a server-side function, which is outside the scope of this study for the same reason push notification delivery is, or permitting passengers to write to driver records, which would be a materially worse arrangement than a few hours of delay.

**A limit of verification.** The system confirms that a document was presented and that it corresponds to the details the driver entered. It cannot confirm that a licence is current or that it has not been suspended, because the Land Transportation Office publishes no interface against which a licence may be checked. The verification implemented here is documentary, and Section 1.5 records this.

**Session handling.** Sessions persist across application restarts, which is a convenience feature, but signing out clears the session immediately and returns the user to the sign-in screen. A session does not, however, survive the installation. The authentication library keeps it in the application's shared preferences, and Android's default backup behaviour copies those to the user's cloud storage and restores them on reinstallation, with the effect that a user who removed the application returned to it already signed in, and that a restore onto a replacement handset could carry the session with it. Backup and device-to-device transfer are therefore both disabled for this application. The two are configured separately because disabling cloud backup does not, on the devices of every manufacturer, disable transfer. No user data is lost by this: the only information held locally is a remembered email address and a flag recording that the introductory carousel has been seen.

**Recorded consent.** The Terms and Conditions, Privacy Policy, Safety and Community Guidelines, and Driver Agreement are carried inside the application and are readable at any time from the profile screen. No account reaches a dashboard until it has accepted the documents that apply to it, with each one ticked separately after being made available to read in full; the only alternative offered is to sign out. The version accepted and the moment of acceptance are stored on the account, so that consent can be evidenced rather than assumed, an account created before consent was tracked is asked at its next launch, and a revision to the documents asks every user again.

**Audit trail.** Every ride, every verification decision, and every concern is stored with timestamps, producing a record that can be examined after the fact and exported for review.

## 3.9 System Architecture

Figure 3 presents the architecture of the system as four layers: the presentation layer containing the three role-specific interfaces, the application layer containing the ViewModels and domain logic, the data layer containing the repositories and the Firebase service, and the backend layer containing the four Firebase services.

![Figure 3. System Architecture of the Smart Tricycle Ride and Driver Onboarding System](figures/fig03_system_architecture.png){width=6.0in}

The arrangement is deliberately layered so that each layer depends only on the one below it. A screen knows about its ViewModel and nothing further. A ViewModel knows about repositories and not about Firebase. A repository knows about the Firebase service and not about the shape of the interface. This means a change to how data are stored affects one layer, and a change to how a screen looks affects one other, which is what makes a system of this size maintainable by a team of three.

[[PB]]
# Chapter 4 {-}

# METHODOLOGY, RESULTS, AND DISCUSSION

## 4.1 Requirements Analysis

Requirements were derived from three sources: the problems documented in the needs assessment, the objectives set out in Section 1.3, and the published FeTODAT fare schedule, which imposes requirements of its own on how fares must be computed.

### Functional Requirements

: Table 2. Functional Requirements

| ID | Requirement | Role | Objective served |
|:---|:---|:---|:---|
| FR-01 | The system shall allow a user to register with a full name, date of birth, email address, mobile number, and password. | All | 1, 2 |
| FR-02 | The system shall enforce a password of at least eight characters containing an uppercase letter, a lowercase letter, and a digit. | All | 2 |
| FR-03 | The system shall require the user to accept the Terms and Conditions, the Privacy Policy, and the Safety and Community Guidelines before an account is created. | All | 2 |
| FR-03a | The system shall prevent any signed-in account from reaching a dashboard until it has accepted the current version of the applicable documents, and shall record the version accepted and the time of acceptance against the account. | All | 2 |
| FR-03b | The system shall require a driver to accept the Driver Agreement before operating as a driver. | Driver | 2 |
| FR-04 | The system shall authenticate a user by email address and password. | All | 1, 2 |
| FR-05 | The system shall keep a user signed in across application restarts until they sign out. | All | 4 |
| FR-06 | The system shall allow a user to edit their profile and set a photograph from the camera or the gallery. | All | 6 |
| FR-07 | The system shall allow a user to request a password reset by email. | All | 2 |
| FR-08 | The system shall present a first-time user with an introductory carousel, shown once. | All | 6 |
| FR-08a | The system shall allow a driver to submit a photograph of their driver's licence, obtaining a specific consent to hold it at the moment of submission, and shall allow the driver to withdraw it. | Driver | 2 |
| FR-08b | The system shall present that photograph to an administrator during verification, restrict it to the driver and administrators, and delete it when an application is refused. | Administrator | 2 |
| FR-09 | The system shall allow a passenger to select a pickup point by searching the same table of stops used for destinations, or by pinning any point on the map. | Passenger | 1 |
| FR-10 | The system shall allow a passenger to select a destination by searching the fare table by stop name or zone. | Passenger | 1 |
| FR-10a | The system shall allow a passenger to indicate a destination on the map, and shall offer the posted stop nearest to that point, with its fare, as the destination to be booked. | Passenger | 1 |
| FR-11 | The system shall allow a passenger to declare how many of the party pay the regular rate and how many pay the discounted rate, and shall price each from its own column. | Passenger | 1 |
| FR-12 | The system shall allow a passenger to specify between one and five passengers and to declare luggage. | Passenger | 1 |
| FR-13 | The system shall display the computed fare, itemized, before the passenger submits the request. | Passenger | 1 |
| FR-14 | The system shall price a ride from the posted rate for the selected destination, applying the ordinance minimum to each rate column where the posted rate falls below it, and, where fares are charged per head, summing both columns across the declared party. | Passenger | 1 |
| FR-15 | The system shall broadcast a submitted ride request to all available, verified drivers. | Passenger, Driver | 3 |
| FR-16 | The system shall expire an unaccepted ride request after five minutes. | System | 3 |
| FR-17 | The system shall match a request to the first driver who accepts it and withdraw it from all others. | System | 3, 4 |
| FR-18 | The system shall show the passenger the ride status as it changes, from acceptance through to completion. | Passenger | 5 |
| FR-19 | The system shall allow a passenger to rate a completed ride. | Passenger | 6 |
| FR-20 | The system shall show a passenger their history of completed rides. | Passenger | 5 |
| FR-21 | The system shall allow a driver to submit a licence number, licence expiry date, and tricycle number. | Driver | 2 |
| FR-22 | The system shall prevent a driver whose verification status is not approved from accepting ride requests. | Driver | 2 |
| FR-23 | The system shall allow a driver to toggle their availability between online and offline. | Driver | 3 |
| FR-24 | The system shall show a driver the open requests with a visible countdown to expiry. | Driver | 3 |
| FR-25 | The system shall allow a driver to advance a ride through arriving, arrived, in progress, and completed. | Driver | 5 |
| FR-26 | The system shall show a driver their completed rides and total earnings. | Driver | 4, 5 |
| FR-27 | The system shall allow an administrator to view pending driver applications with the submitted credentials. | Administrator | 2 |
| FR-28 | The system shall allow an administrator to approve or reject a driver application. | Administrator | 2 |
| FR-29 | The system shall allow an administrator to load the published FeTODAT schedule into the fare table in a single operation. | Administrator | 1 |
| FR-30 | The system shall allow an administrator to search, filter, edit, add, deactivate, and delete entries in the fare table. | Administrator | 1 |
| FR-31 | The system shall flag fare entries requiring verification and allow an administrator to list only those entries and clear the flag. | Administrator | 1 |
| FR-32 | The system shall allow an administrator to edit the minimum fares, the flat rates, and whether fares are charged per passenger. | Administrator | 1 |
| FR-33 | The system shall allow a passenger or driver to file a concern under a category with a description. | Passenger, Driver | 5 |
| FR-34 | The system shall allow an administrator to review a concern, record a note, and mark it open, in review, or resolved. | Administrator | 5 |
| FR-35 | The system shall notify a user in-app of events concerning them and show a count of unread notifications. | All | 3, 5 |
| FR-36 | The system shall show an administrator live counts of drivers, verification states, active rides, and completed rides. | Administrator | 5 |
| FR-37 | The system shall produce ride activity, driver performance, and concern reports for a selected month, a selected year, the whole record, or a range between two dates chosen by the administrator. | Administrator | 5 |
| FR-38 | The system shall export a report as a comma-separated-value file to a location chosen by the administrator, or share it to another application. | Administrator | 5 |
| FR-39 | The system shall export a report as a portable-document-format file whose first page presents the headline figures and charts for the period and whose remaining pages carry the full record. | Administrator | 5 |

### Non-Functional Requirements

: Table 3. Non-Functional Requirements

| ID | Category | Requirement |
|:---|:---|:---|
| NFR-01 | Usability | A passenger shall be able to complete a booking in no more than six interactions from the home screen. |
| NFR-02 | Usability | Every screen shall be operable in both light and dark themes. |
| NFR-03 | Usability | Lists that load from the network shall display a skeleton placeholder rather than an empty screen. |
| NFR-04 | Usability | Lists shall support pull-to-refresh. |
| NFR-05 | Performance | A ride request shall appear on an available driver's device within three seconds of submission under normal mobile data conditions. |
| NFR-06 | Performance | A destination search across the fare table shall return results without perceptible delay. |
| NFR-07 | Reliability | A database operation that does not complete within twelve seconds shall fail with an actionable message rather than leave the interface waiting. |
| NFR-08 | Reliability | The application shall not lose interface state when the device is rotated. |
| NFR-09 | Security | Authorization shall be enforced by server-side rules, not solely by the interface. |
| NFR-10 | Security | Confidential configuration shall be excluded from version control. |
| NFR-10a | Security | Sensitive personal information as defined by Republic Act No. 10173 shall be readable only by the person it concerns and by an administrator, and shall be destroyed when the purpose for holding it ends. |
| NFR-11 | Maintainability | Fare rates shall be changeable without releasing a new version of the application. |
| NFR-12 | Portability | The application shall run on Android 7.0 and later. |
| NFR-13 | Compatibility | Exported spreadsheets shall open without conversion in Microsoft Excel, Google Sheets, and LibreOffice Calc. |
| NFR-16 | Usability | An exported document shall be legible when printed on Letter or A4 paper without scaling, and shall be readable in greyscale. |
| NFR-14 | Scalability | The system shall operate within the free tier of the backend platform at the scale of the study. |
| NFR-15 | Cost | The system shall require no paid subscription and no payment method on file, so that neither the college nor the drivers' association incurs a recurring commitment. |

## 4.2 Requirements Documentation

### User Requirements

: Table 4. User Requirements by Role

| Role | What the user needs to be able to do |
|:---|:---|
| Passenger | Create an account and stay signed in; find out what a ride will cost before agreeing to it; request a ride without leaving the building; know that a driver has accepted and where the ride stands; keep a record of past rides; report a problem and receive a response |
| Driver | Register and have credentials checked; control when they are receiving requests; see incoming requests with enough information to decide; accept before another driver does; record progress through the ride; see what has been earned |
| Administrator | Check a driver's credentials before allowing them to operate; keep the fare table correct; see what is happening now; answer concerns; produce a record of activity for a month or a year |

### System Requirements

: Table 5. System Requirements

| Category | Requirement |
|:---|:---|
| Platform | Native Android application, API level 24 and above |
| Backend | Firebase Authentication, Realtime Database, and Cloud Messaging, all within the free tier |
| Data synchronization | Persistent listeners delivering changes to connected clients without polling |
| Concurrency | A ride request must be accepted by exactly one driver; acceptance removes the request from all other devices |
| Fare source | The published FeTODAT schedule, held in the database and editable by an administrator |
| Reporting | Portable-document-format and comma-separated-value export, written through the system file picker or shared to another application |
| Offline behaviour | The application requires connectivity; recent data are cached by the client library and re-synchronized on reconnection |

### Use Case Descriptions

: Table 6. Use Case Description: Book a Ride

| Field | Description |
|:---|:---|
| Use case name | Book a Ride |
| Identifier | UC-04 |
| Primary actor | Passenger |
| Secondary actors | Driver, Firebase Realtime Database |
| Preconditions | The passenger is signed in. The fare table has been loaded. At least one verified driver is online. |
| Trigger | The passenger taps Book Ride. |
| Main flow | 1. The passenger selects a pickup point. 2. The passenger searches for and selects a destination stop. 3. The passenger selects the regular or discounted rate column. 4. The passenger sets the number of passengers and declares any luggage. 5. The system displays the itemized fare. 6. The passenger submits the request. 7. The system writes the request and broadcasts it to available drivers. 8. A driver accepts. 9. The system creates the ride, removes the request, and notifies both parties. 10. The passenger's screen switches to ride tracking. |
| Alternative flow A | At step 5 the passenger judges the fare unacceptable and returns to step 2 to select a different destination. |
| Alternative flow B | At step 8 no driver accepts within five minutes; the request expires and the passenger is returned to the booking screen. |
| Alternative flow C | The passenger cancels the request before a driver accepts; the request is removed. |
| Postconditions | A ride record exists with an assigned driver, an agreed fare, and a status of accepted. |
| Exceptions | Connectivity is lost during submission; the operation fails after twelve seconds with a message and the request is not created. |

: Table 7. Use Case Description: Verify a Driver

| Field | Description |
|:---|:---|
| Use case name | Verify a Driver |
| Identifier | UC-16 |
| Primary actor | Administrator |
| Secondary actors | Driver |
| Preconditions | The administrator is signed in. At least one driver application has a status of pending. |
| Trigger | The administrator opens the Verify tab, which carries a badge showing the number of pending applications. |
| Main flow | 1. The system lists pending applications with the driver's name, contact details, licence number, licence expiry, and tricycle number. 2. The administrator reviews the submitted details. 3. The administrator approves the application. 4. The system sets the verification status to approved. 5. The system notifies the driver. 6. The driver becomes able to go online and accept requests. |
| Alternative flow | At step 3 the administrator rejects the application; the status is set to rejected, the driver is notified, and the driver remains unable to accept requests. |
| Postconditions | The driver's verification status is approved or rejected, and the driver has been notified. |
| Exceptions | The write fails; the status is unchanged and an error is displayed. |

## 4.3 System Design

### Context Diagram

The context diagram places the system in relation to everything outside it, showing the four external entities and the data that pass between them and the system.

![Figure 4. Context Diagram (Data Flow Diagram Level 0)](figures/fig04_context_diagram.png){width=6.0in}

### Data Flow Diagram

Decomposing the single process of the context diagram gives six processes and seven data stores.

![Figure 5. Data Flow Diagram Level 1](figures/fig05_dfd_level1.png){width=6.0in}

### Entity Relationship Diagram

Although the Realtime Database is not relational, the entities it holds and the relationships between them can be expressed in the same terms, which is what the following diagram does. Relationships that would be foreign keys in a relational database are stored as identifier fields. A user's profile photograph is held in a separate `profilePhotos` node keyed by the same identifier, and a driver's licence photograph in a separate `driverDocuments` node, for the reasons given in Sections 3.5 and 3.8.

![Figure 6. Entity Relationship Diagram](figures/fig06_erd.png){width=5.89in}

### Use Case Diagram

![Figure 7. Use Case Diagram](figures/fig07_use_case.png){width=6.0in}

### Activity Diagram

The activity diagram traces the booking process, the central transaction of the system, including the points at which it can end without a ride.

![Figure 8. Activity Diagram: Booking a Ride](figures/fig08_activity_booking.png){width=6.0in}

### Sequence Diagram

The sequence diagram shows the same transaction as an exchange between components over time, distinguishing calls made by the application from changes pushed by the database to live listeners. The pushed changes are what make the two devices agree without either polling the other.

![Figure 9. Sequence Diagram: Ride Request to Completion](figures/fig09_sequence_booking.png){width=6.0in}

### Class Diagram

![Figure 10. Class Diagram](figures/fig10_class_diagram.png){width=5.26in}

### Database Design

The database is a JSON tree with ten top-level nodes.

: Table 8. Realtime Database Node Structure

| Node | Key | Contents | Written by |
|:---|:---|:---|:---|
| `users` | user identifier | Email, mobile number, given and family name, date of birth, role, accepted document versions and the time of acceptance, timestamps | The account holder |
| `drivers` | user identifier | Tricycle number, verification status, availability, rating, ride count, and a flag recording whether a licence photograph is on file | The driver; verification status by an administrator |
| `rideRequests` | request identifier | Passenger identifier, pickup, destination, passenger count, luggage, priced fare, fare stop, rate column, requested and expiry timestamps | The passenger; deleted on acceptance or expiry |
| `rides` | ride identifier | Passenger and driver identifiers, pickup, destination, status, fares, fare stop, rate column, lifecycle timestamps, passenger count, luggage, notes | The system on acceptance; status by the driver |
| `config/fare` | fixed | Minimum regular fare, minimum discounted fare, flat rates, per-passenger flag, source citation, seed timestamp | An administrator |
| `config/fareStops` | stop identifier | Zone, name, regular rate, discounted rate, active flag, review flag, transcription confidence, note | An administrator |
| `complaints` | complaint identifier | Reporter identifier, name and role, category, description, status, administrator note, timestamps | The reporter; status and note by an administrator |
| `notifications` | user identifier, then notification identifier | Title, message, type, read flag, timestamp | The system |
| `profilePhotos` | user identifier | Base64 JPEG thumbnail and the time it was set | The account holder |
| `driverDocuments` | user identifier | Licence number and expiry, a base64 JPEG of the licence, the time it was sent, and the time consent was given | The driver; the photograph deleted by an administrator on refusal |
| `driverRatings` | driver identifier, then rater identifier | One star rating, from one to five | The passenger who gave it |

![Figure 11. Realtime Database Schema](figures/fig11_database_schema.png){width=4.83in}

The fare table as seeded from the published FeTODAT schedule comprises 240 destinations across twelve zones.

: Table 9. Fare Table Composition by Zone

| Zone | Stops | Flagged for verification | Disabled |
|:---|---:|---:|---:|
| Balintawak | 31 | 1 | 0 |
| Santo Niño | 38 | 0 | 0 |
| San Francisco | 21 | 8 | 0 |
| San Agustin | 16 | 0 | 0 |
| Bagacay, Burgos and Rizal | 16 | 0 | 0 |
| Zamora | 22 | 0 | 0 |
| San Carlos | 10 | 10 | 0 |
| Tanghaligue | 14 | 3 | 0 |
| San Isidro | 32 | 3 | 1 |
| San Jose | 14 | 14 | 0 |
| San Pedro | 11 | 3 | 0 |
| San Roque | 15 | 4 | 2 |
| **Total** | **240** | **46** | **3** |

Regular rates in the seeded table run from ₱20.00 to ₱150.00 with a mean of ₱41.00, against a minimum fare of ₱25.00 for regular passengers and ₱20.00 for discounted passengers. Twenty-seven entries carry a transcribed rate of ₱20.00, below that minimum, and are raised to it when a ride is priced. Whether those entries record a rate that has since been superseded or were read incorrectly is a question for verification against the posted sheet; either way the system charges the minimum rather than the lower figure. In addition to the 240 destinations, the schedule fixes two flat rates that are not tied to a numbered stop: ₱25.00 for any point within Poblacion — offered under that name because the schedule has no numbered stop for it — and ₱25.00 for the round trip between the Talibon Integrated Bus Terminal and NCBI. Both are offered to passengers as destinations in their own right.

Forty-six entries carry a verification flag. These are rows that could not be read from the posted sheet with full confidence, rows where two transcription passes disagreed, and rows that are internally inconsistent, such as four entries where the discounted rate exceeds the regular rate. Three entries are disabled because no usable rate could be read: one where the fare was cut off at the edge of the photograph, one where only the discounted rate was legible, and one where the transcribed regular rate of ₱740.00 falls so far outside the range of every neighbouring entry that it was treated as a misprint rather than a price. A disabled entry cannot be selected by a passenger and therefore cannot price a ride. The administrative interface lists flagged entries separately so that they can be checked against the physical sheet and cleared.

This treatment is a deliberate design position. The alternative, which is to enter every transcribed number and let the application charge whatever was read, would produce a system that is wrong quietly. Flagging uncertainty and disabling unusable entries makes the system wrong loudly, which is the failure mode that can be corrected.

### Interface Design

The interface follows Material 3 with a green palette derived from the college's colours, and supports both light and dark themes. Navigation is by a bottom bar of four tabs for passengers and drivers and five for administrators, which keeps every primary destination one tap from every other.

![Figure 13. Screen Navigation Flow](figures/fig13_screen_flow.png){width=6.0in}

Several decisions in the interface follow from constraints identified during design. The destination picker is a full-screen searchable list rather than a dropdown, because a dropdown of 240 entries is unusable. The driver's request card carries a countdown, because a driver deciding whether to accept needs to know how long the decision remains available. Lists show skeleton placeholders while loading rather than an empty screen, because an empty screen reads as a failure. The fare is itemized rather than given as a single figure, because a passenger who can see how the number was reached can check it against the posted sheet.

## 4.4 Software Development

### Development Approach

Development proceeded through the phases of the Waterfall model shown in Figure 12, with the implementation phase organized into successive modules so that each could be tested before the next was begun.

![Figure 12. Waterfall Model of the Software Development Life Cycle](figures/fig12_waterfall.png){width=3.7in}

: Table 10. Development Milestones and Deliverables

| Milestone | Deliverable | Verification |
|:---|:---|:---|
| Project scaffolding | Gradle build, Firebase configuration, theme, navigation shell | Application builds and launches |
| Authentication | Registration, sign-in, session persistence, password policy, terms consent | An account can be created and survives a restart |
| Role routing | Account type selection and role-based navigation | Each role reaches its own interface |
| Passenger booking | Pickup and destination selection, fare display, request submission | A request is written to the database |
| Driver matching | Availability toggle, request list with countdown, acceptance | A request accepted by one driver disappears from another's device |
| Ride lifecycle | Status progression, shared tracking view, completion and rating | Both devices show the same status |
| Driver onboarding | Credential submission, administrator verification, gating | An unverified driver cannot accept a request |
| Concerns and notifications | Concern submission, administrative review, notification centre | A concern reaches the administrator and a response reaches the reporter |
| Fare table | Seed import, search, filter, edit, review queue, global rates | 240 stops load and price rides correctly |
| Reporting | Period selection, summary, three report types, export and share in both formats | The spreadsheet opens in a spreadsheet application and the document prints legibly |
| Refinement | Profile photographs, onboarding carousel, skeleton loading, pull-to-refresh, dark theme | Verified by inspection on device |

### Coding Standards

Kotlin's official style guide was followed throughout. Names describe intent rather than type. Comments explain why a decision was made where the reason is not evident from the code, and are omitted where the code already says what it does. Each file holds one concern. Every screen is a composable function that receives state and emits events, holding no logic of its own.

### Version Control

All work was committed to a Git repository hosted on GitHub, on a dedicated development branch. Commits were made at the completion of each coherent unit of work, producing a history in which any change can be located and, if necessary, reverted.

### Integration

Integration was continuous rather than deferred. Each module was connected to Firebase as it was written, so that failures in synchronization, deserialization, or authorization surfaced immediately rather than accumulating to the end of development. Two classes of defect were found this way and would have been considerably harder to locate later: a mismatch between the package name registered in the Firebase console and the package name in the application, which caused the build to fail at the Google Services processing step; and a database write that never completed because the Realtime Database instance had not been created, which the application originally presented as an indefinite loading indicator and which was corrected by introducing a twelve-second timeout with an actionable message.

## 4.5 Testing

Testing was conducted at four levels, followed by performance and security testing.

### Unit Testing

Unit testing verified individual functions in isolation, concentrating on the logic that produces numbers, since an error there produces a wrong fare rather than a visible failure.

: Table 11. Unit Test Cases and Results

| ID | Test case | Expected result | Result |
|:---|:---|:---|:---|
| UT-01 | Price a regular ride to a stop with a posted rate above the minimum | The posted regular rate is returned | Pass |
| UT-02 | Price a discounted ride to the same stop | The posted discounted rate is returned | Pass |
| UT-03 | Price a ride where the posted rate falls below the ordinance minimum | The minimum for the applicable column is returned and flagged as applied | Pass |
| UT-04 | Price a ride for three passengers with per-head charging enabled | The per-passenger rate multiplied by three is returned | Pass |
| UT-05 | Price a ride for three passengers with per-head charging disabled | The per-passenger rate is returned unmultiplied | Pass |
| UT-06 | Evaluate a password of seven characters | Rejected as too short | Pass |
| UT-07 | Evaluate a password of eight characters with no digit | Rejected | Pass |
| UT-08 | Evaluate a compliant password | Accepted | Pass |
| UT-09 | Bucket a ride timestamped in a given month into that month's report period | The ride is included | Pass |
| UT-10 | Bucket a ride with an unparseable timestamp | The ride is excluded rather than assigned to the current period | Pass |
| UT-11 | Escape a report field containing a comma and a quotation mark | The field is quoted and internal quotes are doubled | Pass |
| UT-12 | Derive available report periods from a set of rides | Only periods containing rides are offered, newest first | Pass |

### Integration Testing

Integration testing verified that components work together and that the application and Firebase agree.

: Table 12. Integration Test Cases and Results

| ID | Test case | Expected result | Result |
|:---|:---|:---|:---|
| IT-01 | Register an account and read it back | The stored record matches what was submitted | Pass |
| IT-02 | Sign in, close the application, and reopen it | The session is restored without re-entering credentials | Pass |
| IT-03 | Submit a ride request from one device | The request appears on a second device signed in as an available driver | Pass |
| IT-04 | Accept a request on one driver device | The request disappears from a second driver device | Pass |
| IT-05 | Advance ride status on the driver device | The passenger device reflects the new status without user action | Pass |
| IT-06 | Load the fare table from the administrative interface | 240 stops are written and appear on a passenger device | Pass |
| IT-07 | Edit a fare on the administrative device | The new rate is used by the next booking on a passenger device | Pass |
| IT-08 | Approve a driver on the administrative device | The driver's device gains the ability to go online, and a notification is received | Pass |
| IT-09 | File a concern from a passenger device | The concern appears in the administrative concerns list with a badge | Pass |
| IT-10 | Upload a profile photograph | The image is stored and the URL is written to the user record | Pass |

### System Testing

System testing exercised complete workflows end to end.

: Table 13. System Test Cases and Results

| ID | Scenario | Expected result | Result |
|:---|:---|:---|:---|
| ST-01 | A new passenger installs the application, sees the carousel, registers, and books a ride | The full path completes and a ride record is created | Pass |
| ST-02 | A new driver registers, submits credentials, is rejected, and attempts to go online | The driver cannot accept requests | Pass |
| ST-03 | The same driver is subsequently approved and accepts a ride | The ride proceeds to completion | Pass |
| ST-04 | A ride request receives no acceptance | The request expires after five minutes and the passenger is returned to booking | Pass |
| ST-05 | A ride is carried through to completion and rated | The ride appears in both parties' history with the correct fare | Pass |
| ST-05a | A driver submits a licence photograph and an administrator refuses the application | The photograph is presented for review, and no longer exists in the database once the refusal is recorded | Pass |
| ST-06 | An administrator exports a monthly ride report as a spreadsheet | The file contains every ride in that month and opens in a spreadsheet application | Pass |
| ST-06a | An administrator exports the same report as a document | The first page carries the summary figures and charts for the month and the following pages carry every ride | Pass |
| ST-06b | An administrator selects a range between two dates and exports the ride report | The report covers every ride from the first day to the last, both days included in full | Pass |
| ST-07 | A returning user reopens the application | The welcome screen is shown and the session is restored to the correct role | Pass |
| ST-08 | The device is rotated during booking | Entered values are retained | Pass |
| ST-09 | The application is used with the device in dark theme | Every screen renders legibly | Pass |
| ST-10 | Connectivity is lost during a booking | The operation fails within twelve seconds with an actionable message | Pass |

### User Acceptance Testing

User acceptance testing was conducted with forty-one respondents drawn from the three user roles — twenty student passengers, twenty tricycle drivers, and the system administrator — using the application under real conditions during the evaluation period. Respondents completed representative tasks without assistance: registering an account, booking a ride to a named destination, and for drivers, going online and accepting a request. The full task set is listed in Appendix L.

Every task was completed by the respondents who attempted it. The difficulties observed were confined to reading the interface rather than to operating it, and were concentrated among the driver respondents, whose mean age was 53.25 against 19.70 for the passengers. Several drivers sought confirmation of what a control did before using it. That observation is the same finding the questionnaire subsequently recorded as a driver Usability mean of 4.20, the only group-level characteristic in the study to fall in *Agree*. Task completion and observed difficulty were recorded; task duration was not instrumented, and no claim about timing is made from this testing. The outcomes are reported in full in Section 4.9 together with the questionnaire results.

### Performance Testing

Performance was assessed against the targets in Table 3. Request propagation was measured as the interval between a passenger submitting a request and the request appearing on a driver's device. Destination search was assessed by observation across the full 240-entry table. Application launch time to the first interactive screen was measured on a representative low-specification device.

*[Measured figures are to be recorded following the evaluation period.]*

### Security Testing

: Table 14. Security Test Cases and Results

| ID | Test case | Expected result | Result |
|:---|:---|:---|:---|
| SEC-01 | Attempt to read another user's profile record while signed in as a different user | The read is refused by the database rules | Pass |
| SEC-02 | Attempt to change a driver's verification status while signed in as that driver | The write is refused | Pass |
| SEC-03 | Attempt to write to the fare table while signed in as a passenger | The write is refused | Pass |
| SEC-04 | Attempt to sign in with an incorrect password | Access is denied with a message that does not disclose whether the account exists | Pass |
| SEC-05 | Register with a password that does not meet the policy | Registration is prevented at the interface and the reason is shown | Pass |
| SEC-06 | Inspect the repository for confidential values | No API key, service configuration, or credential is present in version control | Pass |
| SEC-07 | Sign out and attempt to return to a role interface | The user is returned to the sign-in screen | Pass |
| SEC-08 | Inspect network traffic during use | All traffic is encrypted; no plain-text transmission is observed | Pass |
| SEC-09 | Attempt to read another driver's licence photograph while signed in as a passenger | The read is refused by the database rules | Pass |
| SEC-10 | Attempt to read a driver's licence photograph while signed in as a different driver | The read is refused | Pass |
| SEC-11 | Refuse a driver application and then query the document node directly | The photograph has been deleted and the read returns nothing | Pass |
| SEC-12 | Withdraw approval from an approved driver and query the document node | The photograph is retained, since withdrawal is not refusal | Pass |
| SEC-13 | Uninstall the application, reinstall it, and open it | The user is returned to the sign-in screen rather than to a restored session | Pass |
| SEC-14 | Attempt to set one's own account type to administrator | The write is refused by the validation clause on the field | Pass |
| SEC-15 | Attempt to read the ride collection without a query limited to oneself | The read is refused | Pass |
| SEC-16 | Attempt to read a ride belonging to two other users | The read is refused | Pass |
| SEC-17 | Attempt to alter the status of a ride one is not the driver of | The write is refused | Pass |
| SEC-18 | Attempt to read a concern reported by another user | The read is refused | Pass |

## 4.6 Prototype Description

The delivered system is a single Android application that presents one of three interfaces according to the role of the signed-in account.

**On first launch**, a new user is shown an introductory carousel of five screens explaining what the application does. The carousel appears once. A returning user with a stored session sees a welcome screen while the session is restored, and is taken directly to their interface without signing in again.

**A driver who has not yet sent a licence photograph** is asked for one on their dashboard, above everything else on it, since without it they cannot be approved and nothing else on that screen is of use to them. The request moves to the profile screen once a photograph is on file. It is asked for after registration rather than during it, so that a poor connection or a refused camera permission cannot strand someone part-way through creating an account; the verification gate is unaffected, because approval is required either way.

**Before any interface opens**, the application confirms that the account has accepted the current Terms and Conditions, Privacy Policy, and Safety and Community Guidelines, and, for a driver account, the Driver Agreement. Anything outstanding is presented on a consent screen where each document can be opened and read in full and must be ticked individually; the button that continues is disabled until all of them are, and the only other option is to sign out.

**The passenger interface** has four tabs. *Home* shows any ride in progress, or the booking entry point. Booking is a single scrolling screen: pickup point, destination, rate column, passenger count, luggage, and notes, with the itemized fare appearing as soon as a destination is chosen. Submitting a request switches the screen to a searching state, and then to a tracking view with a status timeline once a driver accepts. On completion the passenger is shown a summary and invited to rate the ride. *History* lists completed rides. *Support* provides the concern form and contact details. *Profile* holds profile editing, the photograph picker, the theme switch, the terms and privacy notice, and sign-out.

**The driver interface** has four tabs. *Dashboard* shows the availability toggle, total earnings, and any ride in progress with the button that advances it to its next state. *Requests* lists open requests, each with the route, the fare, the rate column, the passenger count, the declared luggage, and a countdown to expiry. *History* lists completed rides. *Profile* mirrors the passenger's, with the addition of a card showing the driver's credentials and verification status.

**The administrator interface** has five tabs. *Verify* lists driver applications, badged with the number pending, with approve and reject actions. *Concerns* lists reported concerns, badged with the number unresolved, with status and note actions. *Monitor* is divided into a live view showing counts and recent rides, and a reports view offering period selection, a summary, and export in either format. *Fares* presents the fare table with search, zone filters, a review queue, per-entry editing, and the global rates dialog. *Profile* is as for the other roles.

A notification centre is reachable from the passenger and driver home screens, showing an unread count and allowing individual or bulk marking as read.

## 4.7 Implementation Plan

### Deployment

The application is distributed to users as a signed release package. The signing key is generated once and held outside version control; the build reads its location and passwords from the project's environment file and, when they are absent, falls back to the debug key while printing a warning that the resulting package must not be distributed. This arrangement makes it difficult to hand out a build that cannot later be updated, which is the practical consequence of signing with the wrong key.

The keystore is the single artefact of this project that cannot be regenerated. Android will not accept an update signed by a different key, so a lost keystore means every existing installation has to be removed and replaced under a new package name. It is therefore kept in two locations independent of any one researcher's computer, and is excluded from the repository.

The application is distributed through the **Google Play Store**. Users install it and receive updates the same way they do for any other application on their handset, which removes the need to permit installation from outside the store and the support burden that goes with it. The installation instructions in the user manual, reproduced in Appendix I, are correspondingly short.

Distribution through the store also carries the signing arrangement described above: because Play will only accept an update signed by the same key, the keystore discipline is what keeps the published application updatable.

Backend deployment requires no hardware. The Firebase project is created through the web console, the database is initialized, the security rules are published, and the fare table is loaded once through the administrative interface. Authentication, the Realtime Database, and Cloud Messaging are all provided within the platform's free tier; the one service that would require a paid plan, object storage, is not used, for the reason set out in Section 3.5.

### Training

Training is organized by role and kept short, on the reasoning that a system requiring lengthy training in a setting like this one will not be adopted.

**Passengers** receive the introductory carousel within the application and a one-page illustrated guide covering registration and booking.

**Drivers** receive a hands-on orientation session conducted with the cooperation of the drivers' association, covering installation, registration, credential submission, going online, accepting a request, and advancing a ride to completion. Drivers are the group for whom adoption is least certain and for whom in-person orientation matters most.

**Administrators** receive a session covering driver verification, the fare table including the review queue, concern handling, and report export, together with the administrator section of the user manual.

### Maintenance

**Corrective maintenance** addresses defects reported during and after the evaluation. Reports arrive through the in-application concern form under the application problem category, which places them in front of the administrator without a separate channel.

**Data maintenance** is the ongoing correction of the fare table. The forty-six flagged entries are to be checked against the physical posted sheet and cleared, and the three disabled entries are to be given rates or removed. When the drivers' association revises its schedule, the administrator amends the affected entries; no new release of the application is required.

**Adaptive maintenance** covers changes required by new Android versions and by changes to the Firebase platform. Google requires applications distributed through the Play Store to target a recent API level, enforced annually, so the target level has to be raised and the application retested each year for it to remain available.

**Handover.** The source code, this documentation, the database schema, and the administrator credentials are turned over to Talibon Polytechnic College at the conclusion of the study, so that the system does not depend on the continued availability of the researchers.

## 4.8 Implementation Results

### Modules Delivered

: Table 15. Application Modules and Their Functions

| Module | Functions delivered | Requirements met |
|:---|:---|:---|
| Authentication and onboarding | Registration with password policy and document consent, a consent gate that blocks the dashboard until the current documents are accepted, the Driver Agreement for driver accounts, sign-in, session persistence, remembered email, password reset, introductory carousel, welcome screen | FR-01 to FR-05, FR-07, FR-08 |
| Profile | Profile editing, photograph capture and selection, theme preference, terms and privacy notice, sign-out | FR-06 |
| Passenger booking | Pickup selection, searchable destination picker across 240 stops plus two flat rates, destination selection from the map by nearest posted stop, separate counts of regular and discounted passengers, luggage, itemized fare display showing each rate column, request submission and cancellation | FR-09 to FR-14 |
| Matching | Broadcast of requests to available verified drivers, five-minute expiry, first-acceptance matching with withdrawal from other devices | FR-15 to FR-17 |
| Mapping and location | Interactive maps on the booking and tracking screens, rendered by Google Maps or OpenStreetMap according to configuration; live publication of a driver's position while online; the passenger's view of that position; pinning a pickup point on the map with a reverse-geocoded label; naming the posted stop nearest a point the passenger indicates; optional coordinates on fare stops | FR-09, FR-10a, FR-18 |
| Ride lifecycle | Shared status timeline through arriving, arrived, in progress, and completed; completion summary and rating; ride history for both parties | FR-18 to FR-20, FR-25 |
| Driver onboarding | Credential submission, administrative verification with approval and rejection, gating of unverified drivers, notification of the decision | FR-21, FR-22, FR-27, FR-28 |
| Driver operations | Availability toggle, request list with countdown, earnings total | FR-23, FR-24, FR-26 |
| Fare administration | Single-operation import of the published schedule, search, zone filtering, review queue, per-entry editing, activation and deactivation, addition and deletion, global minimums and flat rates | FR-29 to FR-32 |
| Concerns | Categorized concern submission by passengers and drivers, administrative review with status and note, notification of resolution | FR-33, FR-34 |
| Notifications | In-application notification centre with unread count, individual and bulk marking as read | FR-35 |
| Driver verification | Licence photograph submission with consent recorded at the point of upload, administrator review of the photograph against the entered details, approval, refusal with immediate deletion of the photograph, and withdrawal by the driver | FR-08a, FR-08b, NFR-10a |
| Monitoring and reporting | Live counts and recent activity, period selection, summary statistics, three report types, document and spreadsheet export through the file picker and sharing | FR-36 to FR-39 |

All forty-four functional requirements were implemented, as were the seventeen non-functional requirements.

### Reports Generated

: Table 16. Reports Generated by the System

| Report | Period options | Contents |
|:---|:---|:---|
| Ride activity | Any month with rides, any year with rides, the whole record, or a range between two chosen dates | Summary block giving total rides, completions, cancellations, open rides, gross fares, average completed fare, passengers served, and drivers with at least one ride; followed by one line per ride giving both parties, all lifecycle timestamps, route, passenger count, luggage, status, and fares |
| Driver performance | As above | One line per driver with at least one ride in the period, giving contact and vehicle details, verification status, rides accepted, completed, and cancelled, gross and average fares, and rating, ordered by rides accepted; followed by a list of drivers with no rides in the period |
| Concerns | As above | Counts by status, a breakdown by category, and one line per concern giving the reporter, category, description, status, administrator note, and the dates filed and resolved |

Each report is produced in two formats. The comma-separated-value file opens without conversion in Microsoft Excel, Google Sheets, and LibreOffice Calc, and suits an administrator who wants to sort or total the figures themselves. The portable-document-format file is the one to print or hand over: its first page carries the headline figures as labelled tiles and four charts of the period, and the pages behind it carry the same detail as the spreadsheet, laid out as a table with its column headings repeated on every page.

: Table 17. Charts on the Summary Page of Each Report

| Report | Charts |
|:---|:---|
| Ride activity | Rides per day across the month, or per month across a year; rides by hour of the day; the most-booked destinations; and rides by day of the week |
| Driver performance | Rides accepted per driver; gross fares per driver; the share of each driver's accepted rides that were completed; and the registered fleet by approval state |
| Concerns | Concerns by category; concerns filed per month; how long resolved concerns took to close; and the roles of those who raised them |

The months and years offered to the administrator are built from the rides that exist, so no empty period appears in the list. A range between two dates chosen from a calendar covers whatever those two do not, with both days included in full: a range ending at midnight would omit everything that happened on its last day, which is not what is meant by a report running up to a given date.

Charts follow the length of the period rather than its kind. A month, or a range up to nine weeks, is drawn with one bar per day; anything longer is collapsed to one bar per month, since a year of daily bars is unreadable at this page size. Where a period crosses into a new year, the month labels carry the year, so that two Januaries are not shown as the same bar.

The pages are Letter landscape. Landscape was chosen because the ride table carries nine columns, and fitting those onto portrait would mean either a font too small to read or dropping the columns that make the table worth printing.

Every chart draws a single series in one colour. Colouring each bar differently would only repeat, in hue, the quantity the bar's length already shows, and it would introduce the difficulty that several colours side by side present to a reader with colour-vision deficiency or a greyscale printer. Each bar instead carries its own value printed at its end, since a printed page offers nothing to hover over.

Either format may be saved to a location chosen by the administrator through the system file picker, or shared directly to another application. Neither route requires a storage permission. The document is drawn using the `PdfDocument` class in the Android framework, so the feature adds no third-party dependency and no cost.

### Screenshots

System screenshots are presented in Appendix H.

[[PB]]

## 4.9 System Evaluation

### Purpose and Basis of the Evaluation

The completed system was evaluated by the users it was built for, against the quality characteristics of the **ISO/IEC 25010 software product quality model** (International Organization for Standardization, 2011). The evaluation answers the fourth specific objective of the study: to determine the level of acceptability of the developed system as assessed by its intended users.

Three instruments were administered, one to each role the system serves. The passenger instrument carries thirty-four statements across six characteristics — Functional Suitability, Usability, Efficiency, Reliability, Security, and Overall Satisfaction. The driver instrument carries twenty statements across four — Usability, Functionality, Efficiency, and Reliability. The administrator instrument carries seven statements across three — Functional Suitability, Usability, and Reliability. Every statement is rated on a five-point Likert scale, 5 for Strongly Agree down to 1 for Strongly Disagree. The instruments are reproduced as Appendix C, the returns as Appendix F, and the computation as Appendix G.

### Respondents

: Table 18. Evaluation Respondents

| Group | Instrument | Selection | N |
|:---|:---|:---|---:|
| Student passengers of Talibon Polytechnic College | Part I and Part II, thirty-four items | Purposive; students who used the application during the evaluation period | 20 |
| Tricycle drivers serving the college community | Driver instrument, twenty items | Total enumeration of the drivers onboarded to the system | 20 |
| System administrator | Part III, seven items | Total enumeration; the role is held by one person | 1 |
| **Total** | | | **41** |

The twenty student respondents were aged 18 to 22, with a mean age of 19.70; twelve were female and eight male. Twelve were enrolled in BSIS, four in BSAIS, three in BECED, and one in BSA, and thirteen were in their third year, six in their second, and one in their first. Fifteen reported taking between one and five tricycle rides a week.

The twenty driver respondents were aged 38 to 68, with a mean age of 53.25, and all were male. Thirteen reported more than ten years of driving experience and four between seven and ten. All twenty owned a smartphone and had internet access, which was a condition of using the system at all. Nine reported six to ten trips a day and nine reported one to five.

The single administrator respondent was 22 years old and male. The administrator's figures are reported separately throughout and are never pooled with the two groups of twenty, because a mean over one respondent describes an individual and not a group.

### Statistical Treatment

The returns were treated by the study's statistician using **frequency count**, **weighted mean**, and **ranking**, the treatment set out in Section 1.11 and computed in full in Appendix G. Each item's weighted mean was read against the five-point scale printed on the questionnaire, in which 4.21 to 5.00 is *Strongly Agree*, 3.41 to 4.20 is *Agree*, 2.61 to 3.40 is *Neutral*, 1.81 to 2.60 is *Disagree*, and 1.00 to 1.80 is *Strongly Disagree*. A category mean is the mean of the item means it contains; an overall mean is the mean of the category means, so that each quality characteristic carries equal weight regardless of the number of items measuring it.

The statistician's treatment is descriptive: it reports how the system was rated by those who used it. One inferential test, the Mann-Whitney U test comparing the passenger and driver groups across the four characteristics common to both instruments, was computed separately by the researchers to answer the fourth research question, and is reported below as Table 36. Section 5.4 records what this design can and cannot support.

### Results and Interpretation

The returns are presented one characteristic at a time, each table followed by its own
interpretation, and each instrument closed with a summary of its characteristics.
Section 4.9.4 then compares the two groups of twenty and states the overall result.

#### 4.9.1 Evaluation by the Student Passengers

The passenger instrument measures six characteristics across thirty-four statements,
answered by twenty student respondents.

: Table 19. Level of Acceptability in Terms of Functional Suitability — Student Passengers (N = 20)

| # | Indicator | 5 | 4 | 3 | 2 | 1 | WM | Interpretation | Rank |
|:---|:---|---:|---:|---:|---:|---:|---:|:---|---:|
| 1 | The ride booking feature works as expected. | 3 | 16 | 1 | 0 | 0 | 4.10 | Agree | 6.5 |
| 2 | I receive timely notifications about my ride status. | 1 | 19 | 0 | 0 | 0 | 4.05 | Agree | 8 |
| 3 | The system accurately matches me with an available driver. | 10 | 9 | 1 | 0 | 0 | 4.45 | Strongly Agree | 1 |
| 4 | I can view the status of my booking in real time. | 4 | 14 | 2 | 0 | 0 | 4.10 | Agree | 6.5 |
| 5 | The system performs all its intended functions correctly. | 9 | 10 | 1 | 0 | 0 | 4.40 | Strongly Agree | 2 |
| 6 | The fare displayed is accurate. | 5 | 15 | 0 | 0 | 0 | 4.25 | Strongly Agree | 3.5 |
| 7 | Driver information is correctly displayed. | 5 | 15 | 0 | 0 | 0 | 4.25 | Strongly Agree | 3.5 |
| 8 | Ride history is properly recorded. | 4 | 16 | 0 | 0 | 0 | 4.20 | Agree | 5 |
| | **Category Weighted Mean** | | | | | | **4.22** | **Strongly Agree** | |

**Interpretation.** Functional Suitability obtained a weighted mean of **4.22**, interpreted as *Strongly Agree*, which places it sixth of the six characteristics the passengers rated. The respondents therefore judged that the system does what it is meant to do, but rated this aspect lower than every other.

The distribution inside the characteristic explains the ranking. The single highest item, *"The system accurately matches me with an available driver."* at 4.45, is the core of the system: matching a passenger to a driver is the function the study set out to automate, and ten of the twenty respondents rated it 5. *"The system performs all its intended functions correctly."* follows at 4.40. Both concern whether the system does the job.

The four items in the *Agree* band concern whether the passenger can see the job being done. *"I receive timely notifications about my ride status."* is last at 4.05, and nineteen of the twenty respondents chose 4 rather than 5 — an unusually flat distribution that indicates a shared, moderate reservation rather than a few dissatisfied respondents. *"I can view the status of my booking in real time."* and *"The ride booking feature works as expected."* follow at 4.10, and *"Ride history is properly recorded."* at 4.20.

The pattern is consistent: the passengers rated the outcome of the system above their visibility into it. This is what the implementation would predict, since status changes are written to the database and rendered inside the application but are not pushed to a device on which the application is closed. The passengers rated what the system does.

: Table 20. Level of Acceptability in Terms of Usability — Student Passengers (N = 20)

| # | Indicator | 5 | 4 | 3 | 2 | 1 | WM | Interpretation | Rank |
|:---|:---|---:|---:|---:|---:|---:|---:|:---|---:|
| 1 | The system is easy to learn. | 7 | 13 | 0 | 0 | 0 | 4.35 | Strongly Agree | 4.5 |
| 2 | The menus are easy to understand. | 8 | 12 | 0 | 0 | 0 | 4.40 | Strongly Agree | 2.5 |
| 3 | Icons and buttons are properly labeled. | 8 | 12 | 0 | 0 | 0 | 4.40 | Strongly Agree | 2.5 |
| 4 | The interface is visually appealing. | 7 | 12 | 1 | 0 | 0 | 4.30 | Strongly Agree | 6 |
| 5 | I can complete my tasks without confusion. | 9 | 11 | 0 | 0 | 0 | 4.45 | Strongly Agree | 1 |
| 6 | The system is easy to learn. | 7 | 13 | 0 | 0 | 0 | 4.35 | Strongly Agree | 4.5 |
| | **Category Weighted Mean** | | | | | | **4.38** | **Strongly Agree** | |

**Interpretation.** Usability obtained a weighted mean of **4.38**, interpreted as *Strongly Agree*, the highest of the six characteristics the passengers rated. Every item in the characteristic falls in the *Strongly Agree* band, and no respondent selected a rating below 3 on any of them.

*"I can complete my tasks without confusion."* is highest at 4.45, followed by *"The menus are easy to understand."* and *"Icons and buttons are properly labeled."*, both 4.40. Taken together these indicate that the passengers were able to operate the application without instruction, which matters for a system whose users install it themselves and receive no training.

The lowest item, *"The interface is visually appealing."* at 4.30, is also the only item in the characteristic on which any respondent was neutral. Appearance was therefore rated slightly below comprehensibility. For a utility application this ordering is the preferable one, since a passenger books a ride to travel rather than to look at the screen.

One qualification attaches to this characteristic. The statement *"The system is easy to learn."* appears twice in the instrument, as items 1 and 6, so five distinct statements were counted as six. Both administrations are reported. Removing the duplicate moves the category weighted mean from 4.3750 to 4.3800, which changes neither the figure at two decimal places nor the interpretation.

: Table 21. Level of Acceptability in Terms of Efficiency — Student Passengers (N = 20)

| # | Indicator | 5 | 4 | 3 | 2 | 1 | WM | Interpretation | Rank |
|:---|:---|---:|---:|---:|---:|---:|---:|:---|---:|
| 1 | The application loads quickly. | 6 | 14 | 0 | 0 | 0 | 4.30 | Strongly Agree | 2.5 |
| 2 | Booking requests are processed immediately. | 4 | 16 | 0 | 0 | 0 | 4.20 | Agree | 4 |
| 3 | Notifications arrive without delay. | 3 | 15 | 2 | 0 | 0 | 4.05 | Agree | 5 |
| 4 | The system performs smoothly. | 7 | 12 | 1 | 0 | 0 | 4.30 | Strongly Agree | 2.5 |
| 5 | The application consumes minimal mobile resources. | 8 | 12 | 0 | 0 | 0 | 4.40 | Strongly Agree | 1 |
| | **Category Weighted Mean** | | | | | | **4.25** | **Strongly Agree** | |

**Interpretation.** Efficiency obtained a weighted mean of **4.25**, interpreted as *Strongly Agree*, ranking fifth of six.

*"The application consumes minimal mobile resources."* is highest at 4.40. This is consistent with the design of the system, which runs no background service and holds no offline store, and it matters to a student population using mid-range and older handsets on limited data.

*"The application loads quickly."* and *"The system performs smoothly."* follow at 4.30, indicating that the interface itself was not experienced as slow.

The lowest item is again a notification item: *"Notifications arrive without delay."* at 4.05, with two respondents neutral. It is tied with *"I receive timely notifications about my ride status."* in Table 19 as the joint-lowest of all thirty-four passenger items. That two items in two different characteristics, worded differently and separated by fifteen statements on the questionnaire, returned the same weighted mean is strong evidence that the respondents were reporting a real and specific experience rather than answering carelessly. *"Booking requests are processed immediately."* at 4.20 completes the pattern: the request is submitted quickly, and what follows it is what the passengers waited for.

: Table 22. Level of Acceptability in Terms of Reliability — Student Passengers (N = 20)

| # | Indicator | 5 | 4 | 3 | 2 | 1 | WM | Interpretation | Rank |
|:---|:---|---:|---:|---:|---:|---:|---:|:---|---:|
| 1 | The system recovers properly after connection interruptions. | 8 | 12 | 0 | 0 | 0 | 4.40 | Strongly Agree | 1 |
| 2 | I can depend on the system to connect me with a driver when needed. | 4 | 16 | 0 | 0 | 0 | 4.20 | Agree | 4 |
| 3 | The system maintains accurate and up-to-date information. | 5 | 15 | 0 | 0 | 0 | 4.25 | Strongly Agree | 3 |
| 4 | The system performs well even during peak hours. | 3 | 17 | 0 | 0 | 0 | 4.15 | Agree | 5 |
| 5 | I trust the system to provide a safe and reliable ride booking experience. | 6 | 14 | 0 | 0 | 0 | 4.30 | Strongly Agree | 2 |
| | **Category Weighted Mean** | | | | | | **4.26** | **Strongly Agree** | |

**Interpretation.** Reliability obtained a weighted mean of **4.26**, interpreted as *Strongly Agree*, ranking fourth of six. No respondent selected a neutral or lower rating on any item in this characteristic, which is true of only two of the six passenger characteristics.

*"The system recovers properly after connection interruptions."* is highest at 4.40. Connection loss is the ordinary condition of mobile use around the campus, and the application was built to fail visibly rather than to hang, presenting an actionable message within twelve seconds rather than an indefinite loading indicator. The respondents rated that behaviour highly.

*"The system performs well even during peak hours."* is lowest at 4.15, and *"I can depend on the system to connect me with a driver when needed."* at 4.20. Both depend on the number of drivers online at the moment of asking rather than on the software, and neither can exceed what the supply of drivers allows. A passenger who opens the application when few drivers are available experiences that as the system failing to deliver, whatever the cause. This is a limitation of a platform evaluated with twenty onboarded drivers, and it would be expected to improve with fleet size rather than with code.

: Table 23. Level of Acceptability in Terms of Security — Student Passengers (N = 20)

| # | Indicator | 5 | 4 | 3 | 2 | 1 | WM | Interpretation | Rank |
|:---|:---|---:|---:|---:|---:|---:|---:|:---|---:|
| 1 | I feel that my personal information is protected. | 7 | 13 | 0 | 0 | 0 | 4.35 | Strongly Agree | 3 |
| 2 | The login process is secure. | 8 | 12 | 0 | 0 | 0 | 4.40 | Strongly Agree | 1 |
| 3 | Only authorized users can access their accounts. | 6 | 13 | 1 | 0 | 0 | 4.25 | Strongly Agree | 5 |
| 4 | Driver information is securely stored. | 9 | 9 | 2 | 0 | 0 | 4.35 | Strongly Agree | 3 |
| 5 | I trust the system to protect my data. | 7 | 13 | 0 | 0 | 0 | 4.35 | Strongly Agree | 3 |
| | **Category Weighted Mean** | | | | | | **4.34** | **Strongly Agree** | |

**Interpretation.** Security obtained a weighted mean of **4.34**, interpreted as *Strongly Agree*, ranking third of six. Every item falls in the *Strongly Agree* band.

*"The login process is secure."* is highest at 4.40, which is the only part of the security design a passenger directly experiences: a password policy at registration, an emailed reset path, and a persistent session.

*"Driver information is securely stored."* at 4.35 has the widest spread of any item in the characteristic, with nine respondents selecting 5, nine selecting 4 and two neutral. *"Only authorized users can access their accounts."* is lowest at 4.25.

These figures require a specific caution. They measure the passengers' **confidence** in the system's security, not its security. A respondent is not in a position to assess authentication rules, database access control, or whether a modified client could read another user's record. The evidence for those properties is the security testing reported in Section 4.5, where eighteen test cases were executed against the deployed database rules. The two should not be read as confirming one another: users who trusted an insecure system would return the same figures.

: Table 24. Level of Acceptability in Terms of Overall Satisfaction — Student Passengers (N = 20)

| # | Indicator | 5 | 4 | 3 | 2 | 1 | WM | Interpretation | Rank |
|:---|:---|---:|---:|---:|---:|---:|---:|:---|---:|
| 1 | I am satisfied with the overall performance of the system. | 8 | 12 | 0 | 0 | 0 | 4.40 | Strongly Agree | 2 |
| 2 | I would recommend this system to others. | 6 | 14 | 0 | 0 | 0 | 4.30 | Strongly Agree | 4 |
| 3 | I intend to continue using the system. | 6 | 14 | 0 | 0 | 0 | 4.30 | Strongly Agree | 4 |
| 4 | The system meets my expectations. | 9 | 11 | 0 | 0 | 0 | 4.45 | Strongly Agree | 1 |
| 5 | Overall, I rate the system as effective. | 6 | 14 | 0 | 0 | 0 | 4.30 | Strongly Agree | 4 |
| | **Category Weighted Mean** | | | | | | **4.35** | **Strongly Agree** | |

**Interpretation.** Overall Satisfaction obtained a weighted mean of **4.35**, interpreted as *Strongly Agree*, ranking second of six. Every item falls in the *Strongly Agree* band and no respondent selected a neutral or lower rating on any of them.

*"The system meets my expectations."* is highest at 4.45 and *"I am satisfied with the overall performance of the system."* follows at 4.40. Both are evaluative: they ask the respondent to judge the system.

The three remaining items — *"I would recommend this system to others."*, *"I intend to continue using the system."* and *"Overall, I rate the system as effective."* — are all 4.30, and the first two are statements of intended behaviour rather than of judgement. That the behavioural items sit below the evaluative ones by 0.10 to 0.15 is a small but consistent gap, and it is the expected direction: approving of a system commits a respondent to nothing, while recommending it or continuing to use it does. The gap is too small to interpret as reluctance, and it is noted only because the opposite ordering would have been the surprising result.

That satisfaction ranks second while Functional Suitability ranks sixth is worth stating plainly. The passengers were more satisfied with the system than their ratings of its individual functions would suggest, which indicates that the shortcomings they identified — chiefly notification delivery — were experienced as irritations rather than as failures.

: Table 25. Summary of the Passenger Evaluation by Characteristic (N = 20)

| Characteristic | Items | Weighted Mean | Interpretation | Rank |
|:---|---:|---:|:---|---:|
| Functional Suitability | 8 | 4.22 | Strongly Agree | 6 |
| Usability | 6 | 4.38 | Strongly Agree | 1 |
| Efficiency | 5 | 4.25 | Strongly Agree | 5 |
| Reliability | 5 | 4.26 | Strongly Agree | 4 |
| Security | 5 | 4.34 | Strongly Agree | 3 |
| Overall Satisfaction | 5 | 4.35 | Strongly Agree | 2 |
| **Overall Weighted Mean** | **34** | **4.30** | **Strongly Agree** | |

**Interpretation.** The passenger evaluation returned an overall weighted mean of **4.30**, interpreted as *Strongly Agree*. All six characteristics fall in the *Strongly Agree* band, and the spread between the highest and the lowest is 0.15, from Usability at 4.3750 to Functional Suitability at 4.2250.

The ordering is informative. The two characteristics concerning how the system is operated and how it is regarded — Usability and Overall Satisfaction — rank first and second. Security ranks third. The three concerning what the system delivers — Reliability, Efficiency and Functional Suitability — rank fourth, fifth and sixth. The passengers found the system easy to use and were satisfied with it, and were most reserved about aspects that depend on delivery: notification timeliness, which the implementation does not provide outside the application, and driver availability, which depends on how many drivers are online.

No characteristic fell below *Strongly Agree*, and the fourth specific objective is met for this respondent group.

#### 4.9.2 Evaluation by the Tricycle Drivers

The driver instrument measures the four characteristics named in the Statement of the Problem across twenty statements, answered by twenty driver respondents whose mean age was 53.25 years.

: Table 26. Level of Acceptability in Terms of Usability — Tricycle Drivers (N = 20)

| # | Indicator | 5 | 4 | 3 | 2 | 1 | WM | Interpretation | Rank |
|:---|:---|---:|---:|---:|---:|---:|---:|:---|---:|
| 1 | The system is easy to navigate and use. | 8 | 11 | 1 | 0 | 0 | 4.35 | Strongly Agree | 1 |
| 2 | The interface is clean and visually understandable. | 3 | 16 | 1 | 0 | 0 | 4.10 | Agree | 4.5 |
| 3 | I was able to register and set up my account without difficulty. | 6 | 14 | 0 | 0 | 0 | 4.30 | Strongly Agree | 2 |
| 4 | The instructions and labels in the system are clear and easy to understand. | 3 | 16 | 1 | 0 | 0 | 4.10 | Agree | 4.5 |
| 5 | I can comfortably use the system without any technical assistance. | 5 | 13 | 2 | 0 | 0 | 4.15 | Agree | 3 |
| | **Category Weighted Mean** | | | | | | **4.20** | **Agree** | |

**Interpretation.** Usability obtained a weighted mean of **4.20**, interpreted as **Agree**. This is the lowest characteristic recorded by any group in the study and the only group-level characteristic that did not reach *Strongly Agree*. Three of its five items fall in the *Agree* band.

The internal pattern is sharp and consistent. The two highest items are about **doing**: *"The system is easy to navigate and use."* at 4.35 and *"I was able to register and set up my account without difficulty."* at 4.30, both *Strongly Agree*. Registration is the more demanding of the two, requiring a driver to enter licence and tricycle details and photograph a document, and it was still rated highly.

The three lowest items are about **reading**. *"The interface is clean and visually understandable."* and *"The instructions and labels in the system are clear and easy to understand."* are tied last at 4.10, and *"I can comfortably use the system without any technical assistance."* is 4.15, with two respondents neutral — the largest neutral count in the characteristic.

The drivers could work the system but found it harder to read. The same interface was rated 4.38 for Usability by the passenger group, whose mean age was 19.70 against 53.25 for the drivers, and that difference of 0.18 is the largest between the two groups on any characteristic. It is consistent with an application designed and tested by developers in their twenties presenting type, contrast and wording that are harder for users in their fifties and sixties, and it agrees with what was observed during acceptance testing, where several drivers sought confirmation of what a control did before using it.

The remedy this points to is a legibility pass — type size, contrast, touch target size and plainer labelling — and not a new feature. Section 5.3 records it as a recommendation. The comparison in Section 4.9.4 establishes that the difference between the groups is not statistically significant, so this is treated as a design signal rather than as a demonstrated difference between populations.

: Table 27. Level of Acceptability in Terms of Functionality — Tricycle Drivers (N = 20)

| # | Indicator | 5 | 4 | 3 | 2 | 1 | WM | Interpretation | Rank |
|:---|:---|---:|---:|---:|---:|---:|---:|:---|---:|
| 1 | The ride request feature works as expected. | 6 | 14 | 0 | 0 | 0 | 4.30 | Strongly Agree | 2.5 |
| 2 | I receive timely notifications about incoming ride requests. | 8 | 11 | 1 | 0 | 0 | 4.35 | Strongly Agree | 1 |
| 3 | The system accurately shows my availability status to passengers. | 5 | 15 | 0 | 0 | 0 | 4.25 | Strongly Agree | 4 |
| 4 | I can manage and track my rides through the system in real time. | 5 | 14 | 1 | 0 | 0 | 4.20 | Agree | 5 |
| 5 | The system performs all its intended functions correctly. | 8 | 10 | 2 | 0 | 0 | 4.30 | Strongly Agree | 2.5 |
| | **Category Weighted Mean** | | | | | | **4.28** | **Strongly Agree** | |

**Interpretation.** Functionality obtained a weighted mean of **4.28**, interpreted as *Strongly Agree*, ranking joint second among the four driver characteristics.

The highest item is *"I receive timely notifications about incoming ride requests."* at 4.35. This deserves attention because it is the mirror image of the passenger result: the equivalent passenger items were the joint-lowest of the thirty-four, at 4.05. The same notification mechanism was rated 0.30 higher by the drivers than by the passengers.

The explanation lies in how each role uses the application. A driver working a shift keeps the application open and in the foreground, waiting for requests, and in that state a database-backed notification appears immediately. A passenger books a ride and puts the phone away, and in that state no notification arrives until the application is reopened, because no server-side component pushes it. The two groups rated the same feature accurately from two different positions, and the divergence is evidence that both were reporting real experience.

*"The ride request feature works as expected."* and *"The system performs all its intended functions correctly."* follow at 4.30, the latter with two respondents neutral. The lowest item is *"I can manage and track my rides through the system in real time."* at 4.20, the only item in the characteristic in the *Agree* band.

: Table 28. Level of Acceptability in Terms of Efficiency — Tricycle Drivers (N = 20)

| # | Indicator | 5 | 4 | 3 | 2 | 1 | WM | Interpretation | Rank |
|:---|:---|---:|---:|---:|---:|---:|---:|:---|---:|
| 1 | The system reduces my idle time waiting for passengers. | 5 | 15 | 0 | 0 | 0 | 4.25 | Strongly Agree | 4 |
| 2 | Managing ride requests through the system is faster than the manual process. | 7 | 13 | 0 | 0 | 0 | 4.35 | Strongly Agree | 1.5 |
| 3 | The system responds quickly to my inputs and actions. | 4 | 15 | 1 | 0 | 0 | 4.15 | Agree | 5 |
| 4 | The system helps me serve more passengers throughout the day. | 8 | 11 | 1 | 0 | 0 | 4.35 | Strongly Agree | 1.5 |
| 5 | Overall, the system improves my daily work experience as a driver. | 7 | 12 | 1 | 0 | 0 | 4.30 | Strongly Agree | 3 |
| | **Category Weighted Mean** | | | | | | **4.28** | **Strongly Agree** | |

**Interpretation.** Efficiency obtained a weighted mean of **4.28**, interpreted as *Strongly Agree*, tied with Functionality for second among the four driver characteristics.

The two highest items are tied at 4.35: *"Managing ride requests through the system is faster than the manual process."* and *"The system helps me serve more passengers throughout the day."* Both compare the system against the arrangement it replaces, and both bear on the fifth specific objective, which concerns the reduction of driver idle time. *"The system reduces my idle time waiting for passengers."* at 4.25 and *"Overall, the system improves my daily work experience as a driver."* at 4.30 point the same way.

These are perceptual measures and must be read as such. The drivers report that the system reduced their idle time; the study did not measure idle time before and after deployment, so the objective is addressed through what users believe occurred rather than through an observed reduction. Section 5.4 records this limitation.

The lowest item, *"The system responds quickly to my inputs and actions."* at 4.15, is the only one in the characteristic that concerns the interface rather than the work. It repeats the division seen in Table 26: the drivers rated the business outcome of the system above their moment-to-moment experience of operating it.

: Table 29. Level of Acceptability in Terms of Reliability — Tricycle Drivers (N = 20)

| # | Indicator | 5 | 4 | 3 | 2 | 1 | WM | Interpretation | Rank |
|:---|:---|---:|---:|---:|---:|---:|---:|:---|---:|
| 1 | The system works consistently without errors or crashes. | 10 | 10 | 0 | 0 | 0 | 4.50 | Strongly Agree | 1 |
| 2 | I can depend on the system to deliver ride requests accurately. | 7 | 13 | 0 | 0 | 0 | 4.35 | Strongly Agree | 3 |
| 3 | The system maintains accurate and up-to-date information. | 6 | 14 | 0 | 0 | 0 | 4.30 | Strongly Agree | 4.5 |
| 4 | The system performs well even during busy hours. | 7 | 12 | 1 | 0 | 0 | 4.30 | Strongly Agree | 4.5 |
| 5 | I trust the system to support my work as a registered driver. | 9 | 11 | 0 | 0 | 0 | 4.45 | Strongly Agree | 2 |
| | **Category Weighted Mean** | | | | | | **4.38** | **Strongly Agree** | |

**Interpretation.** Reliability obtained a weighted mean of **4.38**, interpreted as *Strongly Agree*, the highest characteristic recorded by either group of twenty. Every item is 4.30 or above, which is true of only two other characteristics among the forty respondents, both of them on the passenger instrument.

*"The system works consistently without errors or crashes."* at 4.50 is the highest single item recorded by either group of twenty, with the twenty respondents divided evenly between 5 and 4 and none below. *"I trust the system to support my work as a registered driver."* follows at 4.45.

The contrast with the passenger group is instructive. The passengers ranked Reliability fourth of six at 4.26; the drivers ranked it first of four at 4.38. The two groups mean different things by the word. A driver's session is long and continuous — the application is open for hours while working — so reliability is a question of whether the software holds up under sustained use. A passenger's contact is brief, so reliability is a question of whether a driver arrives, which depends on how many drivers are online rather than on the software. The drivers were assessing the application; the passengers were largely assessing the service.

For a system on which a driver's income depends during a shift, sustained stability is the property that matters most, and it is the property this group rated highest.

: Table 30. Summary of the Driver Evaluation by Characteristic (N = 20)

| Characteristic | Items | Weighted Mean | Interpretation | Rank |
|:---|---:|---:|:---|---:|
| Usability | 5 | 4.20 | Agree | 4 |
| Functionality | 5 | 4.28 | Strongly Agree | 2.5 |
| Efficiency | 5 | 4.28 | Strongly Agree | 2.5 |
| Reliability | 5 | 4.38 | Strongly Agree | 1 |
| **Overall Weighted Mean** | **20** | **4.29** | **Strongly Agree** | |

**Interpretation.** The driver evaluation returned an overall weighted mean of **4.29**, interpreted as *Strongly Agree*. Three of the four characteristics fall in *Strongly Agree* and one, Usability, in *Agree*. The spread from Reliability at 4.38 to Usability at 4.20 is 0.18.

The ordering separates cleanly. Reliability, Functionality and Efficiency — what the system does for the driver's work — occupy the top three positions within 0.10 of each other. Usability — how the system presents itself to the driver — stands alone at the bottom, 0.08 below the next characteristic.

The drivers found the system valuable and found it harder to read than the passengers did. Those two findings are compatible, and together they identify the single most useful change available to this system for this user group, which does not require a new feature.

#### 4.9.3 Evaluation by the System Administrator

The administrator instrument measures three characteristics across seven statements. The role is held by one person, so total enumeration of it yields a single respondent. The tables below describe that individual assessment. No inference beyond the individual is available from N = 1, and these figures are reported separately throughout rather than pooled with the two groups of twenty.

: Table 31. Level of Acceptability in Terms of Functional Suitability — System Administrator (N = 1)

| # | Indicator | 5 | 4 | 3 | 2 | 1 | WM | Interpretation | Rank |
|:---|:---|---:|---:|---:|---:|---:|---:|:---|---:|
| 1 | I can approve driver applications. | 1 | 0 | 0 | 0 | 0 | 5.00 | Strongly Agree | 2 |
| 2 | I can monitor ride activities. | 1 | 0 | 0 | 0 | 0 | 5.00 | Strongly Agree | 2 |
| 3 | I can manage user accounts. | 1 | 0 | 0 | 0 | 0 | 5.00 | Strongly Agree | 2 |
| | **Category Weighted Mean** | | | | | | **5.00** | **Strongly Agree** | |

**Interpretation.** Functional Suitability was rated **5.00**, *Strongly Agree*, with all three statements at the maximum. The administrator reported being able to approve driver applications, monitor ride activities and manage user accounts. These are the three functions on which the administrative role depends, and the verification gate in particular is what allows the third specific objective to be met in operation rather than only in design: a driver who has not been approved cannot accept a passenger.

: Table 32. Level of Acceptability in Terms of Usability — System Administrator (N = 1)

| # | Indicator | 5 | 4 | 3 | 2 | 1 | WM | Interpretation | Rank |
|:---|:---|---:|---:|---:|---:|---:|---:|:---|---:|
| 1 | The dashboard is easy to use. | 1 | 0 | 0 | 0 | 0 | 5.00 | Strongly Agree | 1.5 |
| 2 | Reports are easy to generate. | 1 | 0 | 0 | 0 | 0 | 5.00 | Strongly Agree | 1.5 |
| | **Category Weighted Mean** | | | | | | **5.00** | **Strongly Agree** | |

**Interpretation.** Usability was rated **5.00**, *Strongly Agree*, with both statements at the maximum. The administrator reported that the dashboard is easy to use and that reports are easy to generate. Report generation is the more substantial of the two, involving a choice of period, a choice of report, and export in either of two formats.

: Table 33. Level of Acceptability in Terms of Reliability — System Administrator (N = 1)

| # | Indicator | 5 | 4 | 3 | 2 | 1 | WM | Interpretation | Rank |
|:---|:---|---:|---:|---:|---:|---:|---:|:---|---:|
| 1 | Records are accurate. | 0 | 1 | 0 | 0 | 0 | 4.00 | Agree | 1.5 |
| 2 | Data updates correctly. | 0 | 1 | 0 | 0 | 0 | 4.00 | Agree | 1.5 |
| | **Category Weighted Mean** | | | | | | **4.00** | **Agree** | |

**Interpretation.** Reliability was rated **4.00**, interpreted as **Agree**. Both statements — *"Records are accurate."* and *"Data updates correctly."* — were rated 4, and this is the only administrator characteristic not at the maximum.

The finding is worth recording despite resting on one respondent, because of the position that respondent occupies. The administrator is the only user who sees the whole data set rather than one account's view of it: the ride record across all users, the driver register, the concern log, and the exported reports. A passenger or driver who encountered an inaccurate record would see one instance and might not recognise it as such; the administrator reviews records in aggregate and is the user best placed to notice inaccuracy. That the only rating below the maximum falls on accuracy and updating, from the only respondent positioned to judge them, is a signal to verify rather than a result to report.

It is a signal and nothing more. One respondent rating two items 4 instead of 5 supports no conclusion, and the honest reading is that data accuracy warrants attention in continued operation.

: Table 34. Summary of the Administrator Evaluation by Characteristic (N = 1)

| Characteristic | Items | Weighted Mean | Interpretation | Rank |
|:---|---:|---:|:---|---:|
| Functional Suitability | 3 | 5.00 | Strongly Agree | 1.5 |
| Usability | 2 | 5.00 | Strongly Agree | 1.5 |
| Reliability | 2 | 4.00 | Agree | 3 |
| **Overall Weighted Mean** | **7** | **4.67** | **Strongly Agree** | |

**Interpretation.** The administrator evaluation returned an overall weighted mean of **4.67**, interpreted as *Strongly Agree*, the highest of the three groups. Functional Suitability and Usability were rated at the maximum and Reliability at 4.00.

This figure is reported for completeness. It is the assessment of one person, it is not comparable with the figures from the two groups of twenty, and it is not pooled with them at any point in this study.

#### 4.9.4 Comparison and Overall Result

: Table 35. Overall Weighted Mean by Respondent Group

| Respondent group | N | Overall Weighted Mean | Interpretation |
|:---|---:|---:|:---|
| Student passengers | 20 | 4.30 | Strongly Agree |
| Tricycle drivers | 20 | 4.29 | Strongly Agree |
| System administrator | 1 | 4.67 | Strongly Agree |

**Interpretation.** All three groups rated the system in the *Strongly Agree* band. The two groups of twenty are within 0.02 of each other — 4.3000 against 4.2850 — which is a closer agreement than the evaluation was designed to detect and should not be read as meaningful precision. Of the thirteen category means computed across the three instruments, eleven fall in *Strongly Agree* and two in *Agree* — Usability among drivers at 4.20 and Reliability for the administrator at 4.00. No characteristic for any group fell below *Agree*, and no respondent selected *Disagree* or *Strongly Disagree* on any of the sixty-one items.

The fourth research question asks whether a significant difference exists between the passenger and driver evaluations across the four characteristics both instruments measure. Answering it requires an inferential test, which the descriptive treatment above does not provide. The comparison was therefore computed separately by the researchers using the **Mann-Whitney U test**, a non-parametric test appropriate to ordinal Likert data and to samples of this size, applied at the 0.05 level of significance. Each respondent's score on a characteristic is the mean of that respondent's answers to the items measuring it, giving twenty scores per group per characteristic. This computation supplements the statistician's treatment in Appendix G; it does not replace it, and none of the descriptive figures reported above depend on it.

: Table 36. Comparison of Passenger and Driver Evaluations by Characteristic

| Characteristic | Passengers (N = 20) M ± SD | Drivers (N = 20) M ± SD | U | *p* | Decision at α = .05 |
|:---|:---|:---|---:|---:|:---|
| Usability | 4.38 ± 0.36 | 4.20 ± 0.36 | 243.0 | .241 | Not significant |
| Functionality | 4.22 ± 0.25 | 4.28 ± 0.38 | 195.5 | .913 | Not significant |
| Efficiency | 4.25 ± 0.35 | 4.28 ± 0.34 | 194.0 | .878 | Not significant |
| Reliability | 4.26 ± 0.25 | 4.38 ± 0.30 | 155.0 | .216 | Not significant |

**Interpretation.** No significant difference was found on any of the four characteristics, and the null hypothesis is retained in every case. The two groups, despite using different sides of the system through different interfaces and differing in mean age by more than thirty years, did not differ significantly in how they rated it.

This bears directly on the Usability finding in Section 4.9.2. The 0.18 gap between the passenger and driver Usability means is the largest difference between the groups on any characteristic, and it is not statistically significant (*U* = 243.0, *p* = .241, effect size *r* = .18). The gap is therefore a description of these forty respondents and a signal worth acting on — it agrees with what was observed during acceptance testing and it points at a specific and inexpensive remedy — but it is not established as a difference between the populations from which they were drawn. The recommendation in Section 5.3 for a legibility pass on the driver screens rests on the observations and on the content of the items, not on a demonstrated population difference.

Two qualifications limit what this test can establish. First, the two instruments do not use the same items: the passenger and driver questionnaires measure each shared characteristic with different statements suited to each role. A comparison across them compares how each group rated its own experience, not how two groups rated the same thing, and a difference — had one been found — could as easily reflect the wording of the items as the perceptions of the respondents. Second, with twenty respondents per group the test has limited power, so failing to reject the null hypothesis is not evidence that no difference exists. Both points are carried into Section 5.4.

#### 4.9.5 Summary of the Evaluation

The system was rated **4.30** by the student passengers, **4.29** by the tricycle drivers and **4.67** by the system administrator, each interpreted as *Strongly Agree*. The fourth specific objective, to determine the level of acceptability of the developed system as assessed by its intended users, is met.

Three findings emerged consistently across the characteristic-by-characteristic analysis and are carried into Chapter 5.

**Notification delivery is the system's weakest point for passengers, and its position for drivers proves the diagnosis.** The two joint-lowest passenger items of thirty-four, both 4.05, concern notification timeliness, and they sit in two different characteristics. The equivalent driver item was rated 4.35, the highest in its characteristic. A driver keeps the application open while working and receives notifications immediately; a passenger closes it and receives nothing until reopening, because no server-side component pushes them. The divergence is not a contradiction in the data but a precise localisation of the defect.

**The interface is harder to read for the driver group than for the passenger group.** Driver Usability at 4.20 is the only group-level characteristic below *Strongly Agree*, and its two lowest items concern the clarity of the interface and its labels rather than the availability of any function, while the two items about performing tasks rank at the top of the same characteristic. The difference from the passenger group is not statistically significant and is treated as a design signal.

**The two groups value different things and both are satisfied.** The drivers ranked Reliability first and Usability last; the passengers ranked Usability first and Functional Suitability last. Each group rated most highly the property its own use depends on — sustained stability across a working shift for the driver, ease of operation in a brief interaction for the passenger — and neither group rated any characteristic below *Agree*.

One qualification applies to every figure above. No respondent selected *Disagree* or *Strongly Disagree* on any of the sixty-one items, and the item means span only 4.05 to 4.50. A distribution this narrow is consistent with genuine approval and equally with acquiescence bias, and the design of this study cannot distinguish between them. The overall means are accordingly read as an upper bound on acceptability rather than as point estimates of it. Section 5.4 records this and the other limitations in full.

### User Acceptance Testing

The user acceptance test tasks listed in Appendix L were performed by respondents from each group during the evaluation period, without assistance, before the questionnaire was administered. Every task in the passenger and driver sets was completed by the respondents who attempted it; the administrator set was completed by the single administrator. The difficulties observed were consistent with what the questionnaire subsequently reported: drivers took longest on tasks requiring them to read labels on screen, and several asked for confirmation of what a control did before using it, which is the same legibility finding that Usability at 4.20 records numerically.

Objective, instrumented figures for task completion time were not collected. The evaluation captured completion and observed difficulty, not timing, and no claim about task duration is made from it.

[[PB]]

# Chapter 5 {-}

# SUMMARY, CONCLUSIONS AND RECOMMENDATIONS

## 5.1 Summary of Findings

This study set out to develop and evaluate TrikRide, an Android application connecting passenger and tricycle driver around Talibon Polytechnic College under the fare schedule published by the Federation of Tricycle Operators and Drivers Association of Talibon. The findings are summarised against the specific objectives stated in Section 1.3.

**On the development of the system.** A working Android application was built and deployed, comprising the seventeen modules listed in Table 15 and serving three roles. A passenger books a ride against a named destination and is shown the exact posted fare before committing. A driver registers, submits licence and tricycle details with a photograph of the licence, and can accept requests only after an administrator has approved them. An administrator verifies drivers, maintains the fare table, handles concerns, and exports records for any period as spreadsheets or printable documents. The system was built in Kotlin and Jetpack Compose against Firebase Authentication and Realtime Database, and operates entirely within the free tier of that platform.

**On pricing rides from the published schedule.** All 240 destinations on the posted FeTODAT sheet were transcribed into a database-held fare table distinguishing the regular from the discounted rate and enforcing the statutory minimums of fifteen and twelve pesos. Of the 241 rows on the posted sheet, forty-six could not be read with full confidence and are flagged for verification, and three carry no usable rate and are disabled rather than priced. Because the table is data and not code, an administrator corrects a rate without a new release.

**On the driver verification gate.** Registration and acceptance were separated: a driver account exists from registration, but the ability to accept a passenger is withheld until an administrator has reviewed the submitted credentials. The gate is enforced in the database access rules as well as in the interface, so that it cannot be bypassed by a modified client.

**On the acceptability of the system to its intended users.** Forty-one respondents evaluated the completed system against the ISO/IEC 25010 quality characteristics. The overall weighted means were **4.30 for the twenty student passengers**, **4.29 for the twenty tricycle drivers**, and **4.67 for the single system administrator**, each interpreted as *Strongly Agree* on the scale printed on the questionnaire. Of the thirteen category means computed across the three instruments, eleven fall in *Strongly Agree* and two in *Agree*. No characteristic for any group fell below *Agree*, and no respondent selected *Disagree* or *Strongly Disagree* on any of the sixty-one items.

**On where the system was rated weakest.** Three findings emerged consistently.

First, the two joint-lowest passenger items, both at 4.05, both concern notification timeliness — *"I receive timely notifications about my ride status."* and *"Notifications arrive without delay."* They sit in different characteristics, so the agreement between them is not an artefact of the questionnaire's structure, and both correspond to a known limitation: notifications are written to the database and rendered inside the application, and no server-side component pushes them to a device on which the application is not running.

Second, **Usability among drivers, at 4.20, is the only group-level characteristic in the study to fall in *Agree***. Its two weakest items, both 4.10, concern the clarity of the interface and of its labels rather than the availability of any function. The same interface was rated 4.38 for Usability by the passenger group, whose mean age was 19.70 against 53.25 for the drivers. The difference is consistent with a legibility problem for older users rather than a missing feature, and the observations recorded during user acceptance testing point the same way.

Third, **Reliability for the administrator, at 4.00, likewise falls in *Agree***, and is the only administrator characteristic not rated at ceiling. It rests on one respondent and is reported as such.

**On the comparison between the two groups.** No significant difference was found between the passenger and driver evaluations on any of the four characteristics both instruments measure (Mann-Whitney U, α = .05: Usability *p* = .241, Functionality *p* = .913, Efficiency *p* = .878, Reliability *p* = .216). The fourth research question is answered in the negative: the two groups, differing by more than thirty years in mean age and using different sides of the system through different interfaces, did not differ significantly in how they rated it. The 0.18 gap in Usability, the largest between them, is not statistically significant and is treated in this study as a signal for design attention rather than as an established population difference.

**On the character of the returns.** No respondent used the lower half of the scale on any item, and the sixty-one item means span only 4.05 to 4.50. A distribution this narrow and this uniformly positive is consistent with genuine approval and equally with acquiescence bias, and the design of this study cannot distinguish between them.

## 5.2 Conclusions

**The system is acceptable to the users it was built for.** Both groups of twenty rated it in the *Strongly Agree* band after using it under real conditions, and no characteristic for any group fell below *Agree*. The fourth specific objective is met at the level of overall acceptability. The qualification in Section 5.4 on the narrowness of the response distribution applies to this conclusion and does not overturn it.

**An ordinance-fixed fare regime can be represented faithfully in a ride-hailing application.** The reviewed literature describes systems that price by distance, time, or demand, none of which is lawful where a municipal ordinance fixes the price per destination. Implementing the published schedule as a maintainable data table, with statutory minimums and a discounted rate column, demonstrates that a digital platform can operate inside such a regime rather than around it. The passenger item *"The fare displayed is accurate."*, at 4.25, indicates that the users on the paying side of the transaction found the result correct.

**Transcription of a physically posted fare schedule is a data quality problem that must be handled explicitly.** Of the 241 rows on the posted sheet, forty-six could not be read with full confidence and three carried no usable rate. Designing the system to flag the former and disable the latter, rather than to accept every transcribed number, means that uncertainty in the source data does not become a wrong fare charged to a passenger. This is a design conclusion rather than an evaluation result, and it holds independently of how the system was rated.

**Notification delivery, not matching, is the limiting factor in the passenger experience.** The passengers rated matching highest of their thirty-four items, at 4.45, and notification timeliness jointly lowest, at 4.05 on two separate items in two separate characteristics. The system finds a driver well and tells the passenger about it poorly, and the second of these is a consequence of the deliberate decision to build without a server-side component. That decision kept the system inside the free tier, which was a scope constraint of the study, and its cost is visible in the evaluation.

**A two-sided platform must be evaluated from both sides.** The same screens drew 4.38 for Usability from respondents averaging 19.70 years of age and 4.20 from respondents averaging 53.25 — the only group-level characteristic in the study to fall in *Agree* — with the driver criticism falling specifically on legibility and wording rather than on function. The difference is not statistically significant at the 0.05 level, so no claim is made that the populations differ; what is claimed is narrower and sufficient. Only the driver group produced a characteristic below *Strongly Agree*, only the driver group named clarity as the problem, and the same difficulty was observed independently during acceptance testing. An evaluation conducted with passengers alone would have returned 4.30 across the board and surfaced none of it. The finding is offered as a methodological point as much as a product one.

## 5.3 Recommendations


Based on the development and evaluation of the system, the following are recommended.

**To the Federation of Tricycle Operators and Drivers Association of Talibon.** Verify the forty-six flagged entries in the fare table against the original ordinance and against the physical posted sheet, and resolve the three entries that carry no usable rate. Four entries currently price the discounted rate above the regular rate, which is the reverse of what the ordinance intends and should be corrected before the system is used beyond the study. Consider supplying the schedule in digital form, which would remove transcription from the process entirely.

**To Talibon Polytechnic College.** Designate a member of staff as system administrator, with responsibility for driver verification, fare maintenance, and concern handling. The system is designed so that this is a light and occasional duty rather than a role, but it does require a named person; a system with no administrator will accumulate unverified drivers and unanswered concerns.

**To tricycle drivers.** Keep availability status current. The value of the system to a passenger depends on the online list reflecting who is actually available, and a driver who remains online while not driving degrades the experience for everyone, themselves included.

**To passengers.** Report fare discrepancies through the concern form rather than settling them at the roadside. A reported discrepancy is a correction to the fare table; an unreported one is a recurring dispute.

**To the researchers who maintain the system.** Two changes follow directly from the evaluation and should be made before any wider deployment. First, add a server-side component to push notifications to devices on which the application is not running; the two joint-lowest passenger items in the study both concern notification timeliness, and no change to the client alone can address them. Second, carry out a legibility pass over the driver screens — type size, contrast, touch target size, and plainer wording on labels and instructions — since driver Usability at 4.20 is the weakest characteristic measured and its two lowest items concern clarity rather than function, among respondents whose mean age was 53.25. Neither change requires a new feature; both address what the users who were rated lowest actually reported.

**To future researchers.** Conduct the evaluation over a longer period than a capstone timeline usually permits. Adoption of a transportation platform is not immediate, and a short evaluation window measures first impressions rather than sustained use. Where possible, collect objective measures of waiting time before and after deployment alongside the perceptual measures the questionnaire provides, since objectives four and five concern actual reductions in waiting and idle time and a Likert scale can only report whether users believe those reductions occurred.

## 5.4 Limitations of the Study

The findings above should be read against the following limitations, which are stated so that a reader can judge what the study supports rather than infer it.

**The response distribution is narrow and uniformly positive.** No respondent selected *Disagree* or *Strongly Disagree* on any of the sixty-one items, and the item means span only 4.05 to 4.50. This is consistent with genuine approval. It is equally consistent with acquiescence bias, and with respondents who knew the researchers and were disposed to rate favourably. The instrument was administered by the researchers to a population within their own college, and the design contains no attention check, no reverse-scored item and no independent administration that would let these explanations be separated. The overall means should therefore be read as an upper bound on acceptability rather than as a point estimate of it.

**The evaluation is almost entirely descriptive.** The study evaluates one system as rated by its users. It does not compare the system against the existing arrangement, against another system, or across time. The single inferential test applied, the Mann-Whitney U comparison in Table 36, found no significant difference between the two groups on any characteristic; with twenty respondents per group that test has limited power, so its result is a failure to detect a difference and not evidence that none exists. The two instruments also measure each shared characteristic with different statements, so the comparison sets each group's rating of its own experience against the other's rather than two ratings of the same thing. The difference of 0.18 between driver and passenger Usability is accordingly reported as a description of these forty respondents and as a design signal, not as an established difference between populations.

**The student sample is purposive and no sampling fraction is claimed.** Respondents had to have used the application to be able to evaluate it, which necessarily excludes students who declined to install it or tried and abandoned it — the group whose assessment would be least favourable. Enrolment at the college was not certified for this study, so the twenty student respondents are not characterised as a proportion of any population, and the results do not generalise beyond respondents of this kind at this institution.

**The administrator findings rest on one respondent.** Total enumeration of a role held by one person yields N = 1. The administrator's means and ranks are reported for completeness and describe an individual assessment.

**The passenger instrument contains a duplicated item.** *"The system is easy to learn."* appears as both item B1 and item B6 of Part II, so five distinct statements were counted as six in the Usability characteristic. Appendix G records that removing the duplicate moves the Usability mean from 4.3750 to 4.3800 and the overall passenger mean from 4.3000 to 4.3008, changing no figure at two decimal places and no interpretation. The effect is negligible because the duplicate happened to draw the same distribution as the original, which is fortune rather than design.

**Objective performance figures were not instrumented.** The evaluation recorded task completion and observed difficulty, not task duration, request propagation time, or launch time under field conditions. Objectives concerning reductions in waiting and idle time are therefore addressed only through the perceptual measures the questionnaire provides: the returns report whether users believe those reductions occurred, not whether they did.

**The evaluation period was short.** Adoption of a transportation platform is not immediate, and an evaluation conducted within a capstone timeline measures first impressions rather than sustained use. Ratings collected in the first weeks of use may not survive months of it, in either direction.

## 5.5 Future Enhancements

The following extensions are outside the scope of this study and are offered to researchers who take the work further.

**Turn-by-turn navigation.** The system shows positions but not routes. Adding guidance would require a routing service, which is billed by every major provider, or a self-hosted open-source routing engine. For journeys of this length the benefit is modest, but it would help a driver unfamiliar with an outlying sitio.

**Coordinates for the whole fare table.** A passenger can already point at the map and be offered the nearest posted stop, but only stops an administrator has positioned can be offered that way, and few have been. Surveying the coordinates of all 240, which a group of students with phones could do in a few afternoons, would make the whole schedule reachable from the map instead of only from the list.

**Background position for drivers.** A driver's position is published only while the application is open. Publishing while it is backgrounded would need a foreground service and a persistent notification, and a considered answer on battery use and on what drivers are willing to have tracked.

**Server-side push notifications.** The application registers with Firebase Cloud Messaging, but delivering a notification to a device on which the application is not running requires a server-side component. A small set of Cloud Functions triggered by database writes would deliver ride requests to drivers whose phones are in their pockets, which is where a driver's phone usually is.

**Object storage for images.** Should a paid plan become available, moving profile photographs to Cloud Storage would allow full-resolution images and would make it practical to require photographs of a driver's licence and registration during onboarding. The present design deliberately avoids this dependency.

**Cashless payment.** Integration with a Philippine payment provider would remove the need for exact change, which is a recurring friction in tricycle transactions. This was excluded from the present study by scope and would require attention to the regulatory obligations that handling payments imposes.

**Scheduled and recurring bookings.** A student with a fixed class timetable takes the same trip at the same time several days a week. Allowing a booking to be placed in advance, or to repeat, would serve that pattern directly.

**Ride sharing between passengers with the same destination.** A tricycle carrying five passengers to the same zone is more efficient for everyone than five separate trips, and the fare table already prices per head.

**SMS fallback.** Not every prospective user owns a smartphone. An SMS interface for requesting a ride would extend the system to feature phones, which remain common among the older drivers and passengers the study encountered.

**Analytics for the drivers' association.** The ride records already collected would support analysis of demand by hour, by day, and by zone. That analysis would let the association position drivers where passengers actually are, which is a use of the data beyond the reporting the present system provides.

**Biometric or document-image verification.** Driver verification currently rests on typed credentials reviewed by an administrator. Requiring photographs of the licence and the registration, and checking them against the typed values, would strengthen the onboarding gate.

**Wider release.** The application is published to the Google Play Store for the study population. Offering it beyond Talibon would mean transcribing the fare schedule of each additional municipality, since the ordinance that fixes the rates is local, and agreeing the arrangement with each drivers' association.

[[PB]]
# REFERENCES

Cheng, Y., Protopapas, N., Yazdanpanah, V., Gerding, E., & Stein, S. (2024). Fair and efficient ride-scheduling: A preference-driven approach. *Autonomous Agents and Multi-Agent Systems*. https://doi.org/10.1007/s10458-024-09625-5

Dastani, Z., Koosha, H., Karimi, H., & Moghaddam, A. (2024). User preferences in ride-sharing mathematical models for enhanced matching. *Scientific Reports, 14*. https://doi.org/10.1038/s41598-024-78469-1

Davis, F. D. (1989). Perceived usefulness, perceived ease of use, and user acceptance of information technology. *MIS Quarterly, 13*(3), 319–340. https://doi.org/10.2307/249008

DeLone, W. H., & McLean, E. R. (2003). The DeLone and McLean model of information systems success: A ten-year update. *Journal of Management Information Systems, 19*(4), 9–30. https://doi.org/10.1080/07421222.2003.11045748

Federation of Tricycle Operators and Drivers Association of Talibon. (2022). *Ordinance amending Section 1 of Municipal Ordinance No. 2018-05, the revised ordinance fixing the adjusted fare rates of all tricycles operating within the territorial jurisdiction of the Municipality of Talibon*. Municipality of Talibon, Bohol.

Goodhue, D. L., & Thompson, R. L. (1995). Task-technology fit and individual performance. *MIS Quarterly, 19*(2), 213–236. https://doi.org/10.2307/249689

Huang, X., Li, Z., & Chen, Y. (2024). Optimizing routing and scheduling of shared autonomous electric taxis considering capacity constrained parking facilities. *Sustainable Cities and Society, 111*, 105557. https://doi.org/10.1016/j.scs.2024.105557

International Organization for Standardization. (2011). *ISO/IEC 25010:2011 — Systems and software engineering — Systems and software Quality Requirements and Evaluation (SQuaRE) — System and software quality models*. ISO.

Kumar, P., & Singh, R. (2023). Digital ride-hailing platforms and urban transportation sustainability. *Journal of Urban Mobility, 5*, 100074. https://doi.org/10.1016/j.urbmob.2023.100074

Li, Y., Zhang, H., & Wang, S. (2024). Optimizing first- and last-mile ridesharing services with heterogeneous vehicle fleets. *Transportation Research Part E*. https://doi.org/10.1016/j.tre.2024.103642

Narayanan, S., & Antoniou, C. (2021). A systematic literature review of ride-sharing platforms, user factors and barriers. *European Transport Research Review, 13*(61). https://doi.org/10.1186/s12544-021-00522-1

Rapp, D., Bräunl, T., & Collett, T. (2023). On-demand ride sharing: Scheduling of an autonomous bus fleet for last-mile travel. *Robotics and Autonomous Systems, 170*, 104559. https://doi.org/10.1016/j.robot.2023.104559

Republic of the Philippines. (2012). *Republic Act No. 10173: An act protecting individual personal information in information and communications systems in the government and the private sector, creating for this purpose a National Privacy Commission, and for other purposes* (Data Privacy Act of 2012). Official Gazette. https://www.officialgazette.gov.ph/2012/08/15/republic-act-no-10173/

Wang, H., Zhang, J., & Li, Q. (2022). Intelligent transportation systems and smart mobility solutions for urban transportation management. *IEEE Access, 10*, 49792–49805. https://doi.org/10.1109/ACCESS.2022.3172017

Zhang, L., Li, Y., & Chen, X. (2021). Development of mobile ride-hailing platforms and their impact on urban mobility. *Journal of Transportation Technologies, 11*(3), 432–445. https://doi.org/10.4236/jtts.2021.113028

[[PB]]

# APPENDICES

## Appendix A — Research Instruments
The instruments used in this study are the needs assessment questionnaire, administered before development to document the problems in the current arrangement, and the system evaluation questionnaire, administered after a period of use to evaluate the developed system. The evaluation questionnaire is reproduced in full as Appendix C. The needs assessment questionnaire is reproduced below.

**NEEDS ASSESSMENT QUESTIONNAIRE**

*A Smart Tricycle Ride and Driver Onboarding System for Talibon Polytechnic College*

**General Instruction.** This questionnaire gathers data on the current tricycle transportation arrangement serving Talibon Polytechnic College. Please answer honestly. Responses are confidential and are used for research purposes only.

*Part I — Respondent Profile*

Role: Passenger ☐  Driver ☐   Sex: Male ☐ Female ☐   Age: ______

For passengers — How often do you take a tricycle to or from the campus?
Daily ☐  Three to four times a week ☐  Once or twice a week ☐  Rarely ☐

For drivers — How many years have you been driving a tricycle in Talibon? ______

*Part II — Current Situation*

1. On a typical day, how long do you wait for a tricycle (passengers) or for a passenger (drivers)?
   Under 5 minutes ☐  5 to 10 minutes ☐  11 to 20 minutes ☐  Over 20 minutes ☐

2. How do you usually find a ride, or find passengers?
   Terminal queue ☐  Roadside ☐  Calling a driver you know ☐  Other: ____________

3. Do you know the official FeTODAT fare for the destinations you travel to most often?
   Yes, all of them ☐  Some ☐  No ☐

4. Have you ever been unsure whether the fare charged was correct?
   Often ☐  Sometimes ☐  Never ☐

5. For passengers — Do you know whether the driver carrying you holds a valid licence and registration?
   Always ☐  Sometimes ☐  Never ☐

6. Would you use a mobile application to book a tricycle if one were available?
   Yes ☐  No ☐  Unsure ☐

7. What would most discourage you from using such an application?
   Cost of mobile data ☐  Difficulty using a phone application ☐  Preference for the current arrangement ☐  Other: ____________

*Part III — Open Response*

8. What is the single biggest problem with tricycle transportation around the campus?

9. What would you most want a booking application to do?

[[PB]]

## Appendix B — Letter of Permission
*[Two letters are required and are to be inserted here as signed copies.]*

**Letter 1 — To the College President, Talibon Polytechnic College**, requesting permission to conduct the study within the institution, to administer the needs assessment and evaluation questionnaires to students, and to deploy the application to student respondents for the evaluation period.

**Letter 2 — To the President, Federation of Tricycle Operators and Drivers Association of Talibon**, requesting permission to conduct the study among member drivers, to administer the questionnaires, to deploy the application to driver respondents, and to reproduce the association's published fare schedule within the application.

## Appendix C — Survey Questionnaire
**SYSTEM EVALUATION QUESTIONNAIRE**

*A Smart Tricycle Ride and Driver Onboarding System for Talibon Polytechnic College*

**General Instruction.** This questionnaire gathers data for the study titled "A Smart Tricycle Ride and Driver Onboarding System for Talibon Polytechnic College." Please answer all items honestly and completely. Your responses will be treated with strict confidentiality and used for research purposes only.

**Rating Scale**

| Weight | Weighted Mean | Response |
|:---:|:---|:---|
| 5 | 4.21 – 5.00 | Strongly Agree |
| 4 | 3.41 – 4.20 | Agree |
| 3 | 2.61 – 3.40 | Neutral |
| 2 | 1.81 – 2.60 | Disagree |
| 1 | 1.00 – 1.80 | Strongly Disagree |

### I — For Passengers (Students)

Name (Optional): _____________________________    Role: Passenger

Sex: Male ☐  Female ☐    Age: _________

**Directions.** For each statement below, indicate the extent of your agreement by placing a check in the appropriate column. There is no right or wrong answer. Responses are confidential and used for research purposes only. This questionnaire evaluates the system against ISO/IEC 25010 software quality characteristics. Rate each statement based on your actual experience using the system.

**Part II — System Evaluation**

**A. Usability**

| No. | Statement | 5 | 4 | 3 | 2 | 1 |
|:---:|:---|:-:|:-:|:-:|:-:|:-:|
| 1 | The system is easy to navigate and use. | | | | | |
| 2 | The interface is clean and visually understandable. | | | | | |
| 3 | I was able to book a ride without difficulty. | | | | | |
| 4 | The instructions and labels in the system are clear and easy to understand. | | | | | |
| 5 | I can comfortably use the system without any technical assistance. | | | | | |

**B. Functionality**

| No. | Statement | 5 | 4 | 3 | 2 | 1 |
|:---:|:---|:-:|:-:|:-:|:-:|:-:|
| 1 | The ride booking feature works as expected. | | | | | |
| 2 | I receive timely notifications about my ride status. | | | | | |
| 3 | The system accurately matches me with an available driver. | | | | | |
| 4 | I can view the status of my booking in real time. | | | | | |
| 5 | The fare shown matches the official FeTODAT rate for my destination. | | | | | |

**C. Efficiency**

| No. | Statement | 5 | 4 | 3 | 2 | 1 |
|:---:|:---|:-:|:-:|:-:|:-:|:-:|
| 1 | The system reduces my waiting time for a tricycle ride. | | | | | |
| 2 | Booking a ride through the system is faster than the manual process. | | | | | |
| 3 | The system responds quickly to my inputs and requests. | | | | | |
| 4 | The system helps me get a ride more conveniently. | | | | | |
| 5 | Overall, the system improves my transportation experience. | | | | | |

**D. Reliability**

| No. | Statement | 5 | 4 | 3 | 2 | 1 |
|:---:|:---|:-:|:-:|:-:|:-:|:-:|
| 1 | The system works consistently without errors or crashes. | | | | | |
| 2 | I can depend on the system to connect me with a driver when needed. | | | | | |
| 3 | The system maintains accurate and up-to-date information. | | | | | |
| 4 | The system performs well even during peak hours. | | | | | |
| 5 | I trust the system to provide a safe and reliable ride booking experience. | | | | | |

**Part III — Open-Ended Questions**

1. What feature do you like most about the system?
2. What problems or limitations did you observe?
3. What improvements would you recommend?
4. Other comments or suggestions:

[[PB]]

### II — For Tricycle Drivers

Name (Optional): _____________________________    Role: Driver

Sex: Male ☐  Female ☐    Age: _________

**Directions.** For each statement below, indicate the extent of your agreement by placing a check in the appropriate column. There are no right or wrong answers. Responses are confidential and used for research purposes only. This questionnaire evaluates the system against ISO/IEC 25010 software quality characteristics. Rate each statement based on your actual experience using the system.

**Part II — System Evaluation**

**A. Usability**

| No. | Statement | 5 | 4 | 3 | 2 | 1 |
|:---:|:---|:-:|:-:|:-:|:-:|:-:|
| 1 | The system is easy to navigate and use. | | | | | |
| 2 | The interface is clean and visually understandable. | | | | | |
| 3 | I was able to register and set up my account without difficulty. | | | | | |
| 4 | The instructions and labels in the system are clear and easy to understand. | | | | | |
| 5 | I can comfortably use the system without any technical assistance. | | | | | |

**B. Functionality**

| No. | Statement | 5 | 4 | 3 | 2 | 1 |
|:---:|:---|:-:|:-:|:-:|:-:|:-:|
| 1 | The ride request feature works as expected. | | | | | |
| 2 | I receive timely notifications about incoming ride requests. | | | | | |
| 3 | The system accurately shows my availability status to passengers. | | | | | |
| 4 | I can manage and track my rides through the system in real time. | | | | | |
| 5 | The verification process for my credentials worked as expected. | | | | | |

**C. Efficiency**

| No. | Statement | 5 | 4 | 3 | 2 | 1 |
|:---:|:---|:-:|:-:|:-:|:-:|:-:|
| 1 | The system reduces my idle time waiting for passengers. | | | | | |
| 2 | Managing ride requests through the system is faster than the manual process. | | | | | |
| 3 | The system responds quickly to my inputs and actions. | | | | | |
| 4 | The system helps me serve more passengers throughout the day. | | | | | |
| 5 | Overall, the system improves my daily work experience as a driver. | | | | | |

**D. Reliability**

| No. | Statement | 5 | 4 | 3 | 2 | 1 |
|:---:|:---|:-:|:-:|:-:|:-:|:-:|
| 1 | The system works consistently without errors or crashes. | | | | | |
| 2 | I can depend on the system to deliver ride requests accurately. | | | | | |
| 3 | The system maintains accurate and up-to-date information. | | | | | |
| 4 | The system performs well even during busy hours. | | | | | |
| 5 | I trust the system to support my work as a registered driver. | | | | | |

**Part III — Open-Ended Questions**

1. What feature do you like most about the system?
2. What problems or limitations did you observe?
3. What improvements would you recommend?
4. Other comments or suggestions:

[[PB]]

## Appendix D — Interview Guide
Semi-structured interviews are to be conducted with officers of the drivers' association and with a subset of driver-respondents, to obtain context that a rating scale cannot capture.

**For officers of the Federation of Tricycle Operators and Drivers Association of Talibon**

1. How is the current fare schedule set, published, and revised?
2. How are fare disputes between a driver and a passenger resolved at present?
3. Is there any existing register of drivers operating in the municipality, and who maintains it?
4. What would the association need to see before endorsing an application of this kind to its members?
5. What concerns would members raise about a system that records their trips?

**For driver-respondents**

1. Walk me through a typical day. Where do you wait, and how do you decide where to go next?
2. Roughly how much of your working day is spent without a passenger?
3. How do you handle a passenger who disputes the fare?
4. What would make you stop using an application like this one?
5. Is there anything the application asks you to do that gets in the way while you are driving?

**For passenger-respondents**

1. Describe the last time you had difficulty getting a tricycle to or from the campus.
2. Before this study, did you know there was an official fare schedule?
3. What would make you go back to hailing at the roadside instead of using the application?

## Appendix E — Evaluation Forms
The evaluation form administered to respondents is the System Evaluation Questionnaire reproduced as Appendix C. The content validation form completed by the research adviser and subject matter experts, and the pilot test feedback form, are to be inserted here as signed copies.

## Appendix F — Raw Data
The responses exactly as they were entered, one row per questionnaire. Item columns carry the weight the respondent ticked, 1 to 5; a blank means the respondent skipped that item and it is excluded from that item's mean rather than counted as a zero. No cell was blank in this administration. Names were optional and none were given, so the respondent number is the only identifier.

Forty-one questionnaires were returned: twenty from student passengers, twenty from tricycle drivers, and one from the system administrator.

This appendix is the transcription of the returns and is independent of the treatment applied to them. The statistical treatment, carried out by the study's statistician, appears as Appendix G; every figure there was recomputed from the responses transcribed here and reconciles with them exactly.

[[PB]]

### F.1 Student passengers — respondent profile

| No. | Age | Gender | Course | Year level | Frequency of tricycle use | Average rides per week |
|:---|---:|:---|:---|:---|:---|:---|
| 1 | 18 | Female | BSAIS | 2nd Year | Sometimes | 1 – 5 |
| 2 | 20 | Female | BSA | 3rd Year | Sometimes | 11 – 15 |
| 3 | 19 | Female | BSAIS | 2nd Year | Sometimes | 1 – 5 |
| 4 | 19 | Female | BSAIS | 2nd Year | Often | 1 – 5 |
| 5 | 18 | Female | BECED | 2nd Year | Rarely | 1 – 5 |
| 6 | 18 | Male | BECED | 1st Year | Rarely | 1 – 5 |
| 7 | 19 | Female | BECED | 2nd Year | Every Day | 1 – 5 |
| 8 | 21 | Male | BSIS | 3rd Year | Rarely | 1 – 5 |
| 9 | 20 | Male | BSIS | 3rd Year | Every Day | 16 - 20 |
| 10 | 19 | Male | BSIS | 3rd Year | Rarely | 1 – 5 |
| 11 | 22 | Male | BSIS | 3rd Year | Sometimes | 1 – 5 |
| 12 | 21 | Male | BSIS | 3rd Year | Rarely | 1 – 5 |
| 13 | 20 | Female | BSIS | 3rd Year | Sometimes | 1 – 5 |
| 14 | 19 | Male | BSIS | 3rd Year | Sometimes | 6 – 10 |
| 15 | 21 | Female | BSIS | 3rd Year | Sometimes | 1 – 5 |
| 16 | 20 | Female | BSIS | 3rd Year | Sometimes | 1 – 5 |
| 17 | 20 | Female | BSIS | 3rd Year | Every Day | 11 – 15 |
| 18 | 21 | Female | BSIS | 3rd Year | Rarely | 1 – 5 |
| 19 | 20 | Male | BSIS | 3rd Year | Rarely | 1 – 5 |
| 20 | 19 | Female | BSAIS | 2nd Year | Every Day | 6 – 10 |

### F.2 Student passengers — item responses

**A. Functional Suitability**

| No. | A1 | A2 | A3 | A4 | A5 | A6 | A7 | A8 |
|:---|---:|---:|---:|---:|---:|---:|---:|---:|
| 1 | 5 | 4 | 5 | 5 | 5 | 4 | 4 | 4 |
| 2 | 4 | 4 | 5 | 5 | 5 | 5 | 5 | 5 |
| 3 | 5 | 4 | 4 | 4 | 5 | 4 | 4 | 4 |
| 4 | 4 | 4 | 4 | 4 | 3 | 4 | 4 | 4 |
| 5 | 4 | 4 | 5 | 4 | 4 | 4 | 4 | 4 |
| 6 | 4 | 4 | 3 | 4 | 4 | 4 | 4 | 4 |
| 7 | 4 | 4 | 5 | 3 | 4 | 4 | 4 | 4 |
| 8 | 4 | 4 | 4 | 4 | 5 | 4 | 4 | 5 |
| 9 | 4 | 4 | 4 | 3 | 4 | 4 | 4 | 4 |
| 10 | 3 | 4 | 4 | 4 | 4 | 5 | 4 | 4 |
| 11 | 4 | 4 | 5 | 4 | 5 | 4 | 4 | 4 |
| 12 | 4 | 4 | 5 | 4 | 5 | 4 | 5 | 5 |
| 13 | 4 | 4 | 5 | 5 | 4 | 4 | 5 | 4 |
| 14 | 4 | 5 | 5 | 4 | 5 | 4 | 4 | 4 |
| 15 | 4 | 4 | 4 | 4 | 4 | 4 | 4 | 4 |
| 16 | 4 | 4 | 4 | 4 | 4 | 5 | 4 | 4 |
| 17 | 4 | 4 | 5 | 4 | 4 | 5 | 4 | 4 |
| 18 | 5 | 4 | 4 | 4 | 4 | 4 | 4 | 4 |
| 19 | 4 | 4 | 4 | 4 | 5 | 4 | 5 | 5 |
| 20 | 4 | 4 | 5 | 5 | 5 | 5 | 5 | 4 |

**B. Usability**

| No. | B1 | B2 | B3 | B4 | B5 | B6 |
|:---|---:|---:|---:|---:|---:|---:|
| 1 | 5 | 5 | 5 | 5 | 5 | 5 |
| 2 | 4 | 4 | 5 | 5 | 5 | 4 |
| 3 | 4 | 4 | 4 | 4 | 4 | 4 |
| 4 | 4 | 4 | 4 | 4 | 4 | 4 |
| 5 | 4 | 4 | 4 | 4 | 4 | 4 |
| 6 | 5 | 4 | 4 | 3 | 4 | 4 |
| 7 | 5 | 5 | 4 | 5 | 5 | 4 |
| 8 | 5 | 5 | 4 | 5 | 4 | 5 |
| 9 | 4 | 5 | 4 | 4 | 4 | 4 |
| 10 | 4 | 4 | 4 | 4 | 5 | 4 |
| 11 | 4 | 5 | 5 | 4 | 5 | 4 |
| 12 | 4 | 4 | 4 | 4 | 4 | 4 |
| 13 | 4 | 4 | 4 | 4 | 4 | 4 |
| 14 | 4 | 5 | 5 | 5 | 5 | 5 |
| 15 | 4 | 4 | 4 | 4 | 4 | 4 |
| 16 | 4 | 4 | 5 | 4 | 5 | 4 |
| 17 | 5 | 4 | 4 | 5 | 4 | 5 |
| 18 | 5 | 5 | 5 | 4 | 5 | 5 |
| 19 | 5 | 5 | 5 | 5 | 5 | 5 |
| 20 | 4 | 4 | 5 | 4 | 4 | 5 |

**C. Efficiency**

| No. | C1 | C2 | C3 | C4 | C5 |
|:---|---:|---:|---:|---:|---:|
| 1 | 4 | 4 | 4 | 5 | 5 |
| 2 | 4 | 4 | 3 | 3 | 4 |
| 3 | 4 | 4 | 4 | 4 | 4 |
| 4 | 5 | 4 | 4 | 4 | 5 |
| 5 | 4 | 4 | 4 | 4 | 4 |
| 6 | 4 | 4 | 4 | 4 | 4 |
| 7 | 5 | 4 | 4 | 5 | 5 |
| 8 | 5 | 5 | 4 | 5 | 4 |
| 9 | 4 | 4 | 4 | 4 | 4 |
| 10 | 4 | 4 | 3 | 4 | 4 |
| 11 | 4 | 4 | 4 | 5 | 5 |
| 12 | 4 | 4 | 4 | 4 | 4 |
| 13 | 5 | 5 | 5 | 4 | 4 |
| 14 | 5 | 4 | 4 | 5 | 5 |
| 15 | 4 | 4 | 4 | 4 | 4 |
| 16 | 4 | 4 | 4 | 4 | 4 |
| 17 | 4 | 5 | 4 | 4 | 4 |
| 18 | 4 | 4 | 5 | 5 | 5 |
| 19 | 5 | 5 | 5 | 5 | 5 |
| 20 | 4 | 4 | 4 | 4 | 5 |

**D. Reliability**

| No. | D1 | D2 | D3 | D4 | D5 |
|:---|---:|---:|---:|---:|---:|
| 1 | 4 | 5 | 4 | 4 | 4 |
| 2 | 5 | 4 | 4 | 4 | 5 |
| 3 | 4 | 4 | 4 | 4 | 4 |
| 4 | 4 | 5 | 4 | 4 | 4 |
| 5 | 4 | 4 | 4 | 4 | 4 |
| 6 | 4 | 4 | 4 | 4 | 4 |
| 7 | 5 | 4 | 5 | 5 | 5 |
| 8 | 5 | 5 | 4 | 5 | 4 |
| 9 | 4 | 4 | 4 | 4 | 4 |
| 10 | 5 | 5 | 4 | 4 | 5 |
| 11 | 5 | 4 | 5 | 4 | 4 |
| 12 | 4 | 4 | 4 | 4 | 4 |
| 13 | 4 | 4 | 4 | 4 | 4 |
| 14 | 5 | 4 | 4 | 5 | 4 |
| 15 | 4 | 4 | 4 | 4 | 5 |
| 16 | 4 | 4 | 4 | 4 | 4 |
| 17 | 4 | 4 | 5 | 4 | 4 |
| 18 | 5 | 4 | 4 | 4 | 5 |
| 19 | 5 | 4 | 5 | 4 | 5 |
| 20 | 4 | 4 | 5 | 4 | 4 |

**E. Security**

| No. | E1 | E2 | E3 | E4 | E5 |
|:---|---:|---:|---:|---:|---:|
| 1 | 5 | 5 | 5 | 5 | 5 |
| 2 | 4 | 4 | 3 | 3 | 4 |
| 3 | 4 | 4 | 4 | 4 | 4 |
| 4 | 4 | 5 | 5 | 5 | 5 |
| 5 | 4 | 4 | 4 | 4 | 4 |
| 6 | 4 | 4 | 4 | 4 | 4 |
| 7 | 4 | 4 | 5 | 5 | 4 |
| 8 | 5 | 5 | 4 | 4 | 5 |
| 9 | 4 | 4 | 4 | 5 | 4 |
| 10 | 4 | 4 | 5 | 4 | 4 |
| 11 | 4 | 5 | 4 | 5 | 5 |
| 12 | 4 | 4 | 4 | 4 | 4 |
| 13 | 4 | 4 | 4 | 4 | 4 |
| 14 | 5 | 5 | 4 | 5 | 4 |
| 15 | 5 | 5 | 4 | 4 | 5 |
| 16 | 4 | 4 | 4 | 3 | 4 |
| 17 | 5 | 4 | 4 | 5 | 5 |
| 18 | 5 | 5 | 5 | 5 | 4 |
| 19 | 5 | 5 | 5 | 5 | 5 |
| 20 | 4 | 4 | 4 | 4 | 4 |

**F. Overall Satisfaction**

| No. | F1 | F2 | F3 | F4 | F5 |
|:---|---:|---:|---:|---:|---:|
| 1 | 5 | 5 | 5 | 5 | 5 |
| 2 | 5 | 5 | 5 | 5 | 5 |
| 3 | 5 | 5 | 5 | 5 | 5 |
| 4 | 4 | 4 | 4 | 5 | 4 |
| 5 | 4 | 4 | 4 | 4 | 4 |
| 6 | 4 | 4 | 4 | 4 | 4 |
| 7 | 5 | 4 | 4 | 4 | 4 |
| 8 | 5 | 4 | 4 | 5 | 5 |
| 9 | 4 | 4 | 4 | 4 | 4 |
| 10 | 4 | 4 | 4 | 5 | 5 |
| 11 | 4 | 5 | 5 | 4 | 4 |
| 12 | 4 | 4 | 4 | 4 | 4 |
| 13 | 4 | 4 | 4 | 4 | 4 |
| 14 | 5 | 5 | 4 | 5 | 5 |
| 15 | 4 | 4 | 4 | 5 | 4 |
| 16 | 4 | 4 | 4 | 4 | 4 |
| 17 | 4 | 4 | 5 | 4 | 4 |
| 18 | 5 | 5 | 5 | 5 | 4 |
| 19 | 5 | 4 | 4 | 4 | 4 |
| 20 | 4 | 4 | 4 | 4 | 4 |

[[PB]]

### F.3 Tricycle drivers — respondent profile

| No. | Age | Gender | Years driving | Owns smartphone | Internet access | Average trips per day |
|:---|---:|:---|:---|:---|:---|:---|
| 1 | 50 | Male | More than 10 years | Yes | Yes | 21 or more |
| 2 | 41 | Male | 7 - 10 years | Yes | Yes | 11 – 15 |
| 3 | 58 | Male | More than 10 years | Yes | Yes | 6 – 10 |
| 4 | 45 | Male | 7 - 10 years | Yes | Yes | 6 – 10 |
| 5 | 64 | Male | More than 10 years | Yes | Yes | 1 – 5 |
| 6 | 54 | Male | More than 10 years | Yes | Yes | 6 – 10 |
| 7 | 48 | Male | More than 10 years | Yes | Yes | 6 – 10 |
| 8 | 45 | Male | 1 - 3 years | Yes | Yes | 1 – 5 |
| 9 | 68 | Male | More than 10 years | Yes | Yes | 1 – 5 |
| 10 | 56 | Male | More than 10 years | Yes | Yes | 1 – 5 |
| 11 | 45 | Male | 7 - 10 years | Yes | Yes | 1 – 5 |
| 12 | 67 | Male | More than 10 years | Yes | Yes | 1 – 5 |
| 13 | 42 | Male | More than 10 years | Yes | Yes | 6 – 10 |
| 14 | 50 | Male | More than 10 years | Yes | Yes | 1 – 5 |
| 15 | 60 | Male | More than 10 years | Yes | Yes | 6 – 10 |
| 16 | 59 | Male | Less than 1 year | Yes | Yes | 6 – 10 |
| 17 | 67 | Male | More than 10 years | Yes | Yes | 1 – 5 |
| 18 | 56 | Male | 4 - 6 years | Yes | Yes | 6 – 10 |
| 19 | 38 | Male | 7 - 10 years | Yes | Yes | 1 – 5 |
| 20 | 52 | Male | More than 10 years | Yes | Yes | 6 – 10 |

### F.4 Tricycle drivers — item responses

**A. Usability**

| No. | A1 | A2 | A3 | A4 | A5 |
|:---|---:|---:|---:|---:|---:|
| 1 | 4 | 4 | 5 | 4 | 5 |
| 2 | 3 | 3 | 4 | 4 | 4 |
| 3 | 4 | 4 | 4 | 4 | 4 |
| 4 | 5 | 4 | 5 | 4 | 3 |
| 5 | 5 | 4 | 4 | 4 | 4 |
| 6 | 5 | 5 | 5 | 5 | 5 |
| 7 | 4 | 4 | 4 | 4 | 4 |
| 8 | 5 | 4 | 4 | 4 | 4 |
| 9 | 5 | 4 | 5 | 4 | 4 |
| 10 | 5 | 4 | 4 | 4 | 4 |
| 11 | 4 | 4 | 4 | 4 | 4 |
| 12 | 4 | 4 | 4 | 4 | 4 |
| 13 | 4 | 4 | 5 | 4 | 5 |
| 14 | 4 | 4 | 4 | 4 | 5 |
| 15 | 4 | 4 | 4 | 3 | 3 |
| 16 | 4 | 5 | 4 | 5 | 4 |
| 17 | 4 | 4 | 4 | 4 | 4 |
| 18 | 4 | 4 | 4 | 4 | 4 |
| 19 | 5 | 5 | 5 | 5 | 5 |
| 20 | 5 | 4 | 4 | 4 | 4 |

**B. Functionality**

| No. | B1 | B2 | B3 | B4 | B5 |
|:---|---:|---:|---:|---:|---:|
| 1 | 5 | 5 | 4 | 5 | 4 |
| 2 | 4 | 3 | 4 | 4 | 5 |
| 3 | 4 | 5 | 4 | 4 | 5 |
| 4 | 5 | 5 | 5 | 4 | 5 |
| 5 | 4 | 4 | 4 | 4 | 4 |
| 6 | 5 | 5 | 5 | 5 | 5 |
| 7 | 4 | 4 | 4 | 4 | 4 |
| 8 | 4 | 4 | 4 | 4 | 4 |
| 9 | 4 | 5 | 5 | 4 | 3 |
| 10 | 4 | 4 | 4 | 3 | 4 |
| 11 | 4 | 4 | 4 | 4 | 4 |
| 12 | 4 | 4 | 4 | 4 | 4 |
| 13 | 4 | 4 | 5 | 4 | 5 |
| 14 | 5 | 5 | 4 | 4 | 3 |
| 15 | 4 | 4 | 4 | 5 | 5 |
| 16 | 5 | 5 | 4 | 5 | 5 |
| 17 | 4 | 4 | 4 | 4 | 4 |
| 18 | 4 | 4 | 4 | 4 | 4 |
| 19 | 5 | 5 | 5 | 5 | 5 |
| 20 | 4 | 4 | 4 | 4 | 4 |

**C. Efficiency**

| No. | C1 | C2 | C3 | C4 | C5 |
|:---|---:|---:|---:|---:|---:|
| 1 | 5 | 4 | 5 | 4 | 5 |
| 2 | 4 | 5 | 4 | 5 | 4 |
| 3 | 4 | 4 | 4 | 5 | 4 |
| 4 | 5 | 4 | 4 | 5 | 4 |
| 5 | 4 | 4 | 4 | 4 | 4 |
| 6 | 5 | 5 | 5 | 5 | 5 |
| 7 | 4 | 4 | 4 | 4 | 4 |
| 8 | 4 | 4 | 4 | 5 | 5 |
| 9 | 4 | 5 | 4 | 4 | 3 |
| 10 | 4 | 4 | 4 | 4 | 5 |
| 11 | 4 | 4 | 4 | 4 | 4 |
| 12 | 4 | 4 | 4 | 4 | 4 |
| 13 | 4 | 5 | 5 | 5 | 4 |
| 14 | 5 | 5 | 4 | 4 | 5 |
| 15 | 4 | 4 | 3 | 3 | 5 |
| 16 | 4 | 5 | 4 | 4 | 4 |
| 17 | 4 | 4 | 4 | 4 | 4 |
| 18 | 4 | 4 | 4 | 4 | 4 |
| 19 | 5 | 5 | 5 | 5 | 5 |
| 20 | 4 | 4 | 4 | 5 | 4 |

**D. Reliability**

| No. | D1 | D2 | D3 | D4 | D5 |
|:---|---:|---:|---:|---:|---:|
| 1 | 4 | 5 | 4 | 5 | 5 |
| 2 | 5 | 4 | 5 | 3 | 5 |
| 3 | 5 | 4 | 4 | 5 | 4 |
| 4 | 5 | 5 | 4 | 4 | 5 |
| 5 | 4 | 4 | 4 | 4 | 5 |
| 6 | 5 | 5 | 5 | 5 | 5 |
| 7 | 4 | 4 | 4 | 4 | 4 |
| 8 | 5 | 5 | 4 | 4 | 5 |
| 9 | 4 | 4 | 5 | 4 | 5 |
| 10 | 5 | 4 | 4 | 4 | 4 |
| 11 | 4 | 4 | 4 | 4 | 4 |
| 12 | 4 | 4 | 4 | 4 | 5 |
| 13 | 4 | 4 | 4 | 5 | 4 |
| 14 | 5 | 4 | 4 | 4 | 4 |
| 15 | 5 | 4 | 4 | 5 | 4 |
| 16 | 5 | 5 | 5 | 4 | 4 |
| 17 | 4 | 4 | 4 | 4 | 4 |
| 18 | 4 | 4 | 4 | 4 | 4 |
| 19 | 5 | 5 | 5 | 5 | 5 |
| 20 | 4 | 5 | 5 | 5 | 4 |

### F.5 System administrator — profile and item responses

| No. | Age | Gender | A1 | A2 | A3 | B1 | B2 | C1 | C2 |
|:---|---:|:---|---:|---:|---:|---:|---:|---:|---:|
| 1 | 22 | Male | 5 | 5 | 5 | 5 | 5 | 4 | 4 |

## Appendix G — Statistical Computation

The statistical treatment of the returns in Appendix F was carried out by the study's
statistician, Flordelis A. Turtoga, MAMT. The computation sheets she supplied — one
workbook for each respondent group — give, for every item, the frequency of each
response, the composite products, the total, the weighted mean, its verbal
interpretation, and the item's rank within its characteristic. Those figures are
reproduced below and were independently recomputed from the raw responses in
Appendix F; all sixty-one frequency distributions, all sixty-one weighted means, all
thirteen category means, all three overall means and every rank reconcile exactly with
the returns.

Every weighted mean below is read against the interpretation legend printed at the
head of those sheets and reproduced overleaf.

[[PB]]

Each item was scored on the five-point scale printed on the questionnaire. The weighted mean of an item is the sum of the weights given to it divided by the number of respondents who answered it:

$$\bar{x} = \frac{\sum_{i=1}^{5} w_i f_i}{N}$$

where $w_i$ is the weight, 5 down to 1; $f_i$ the number of respondents who chose it; and $N$ the number who answered the item. Each resulting mean is read against the same scale.

| Weight | Weighted mean | Response |
|---:|:---|:---|
| 5 | 4.21 – 5.00 | Strongly Agree |
| 4 | 3.41 – 4.20 | Agree |
| 3 | 2.61 – 3.40 | Neutral |
| 2 | 1.81 – 2.60 | Disagree |
| 1 | 1.00 – 1.80 | Strongly Disagree |

A category mean is the mean of the item means within that category. The overall weighted mean is the mean of the category means, so that each quality characteristic counts equally regardless of how many items measure it. Averaging every item equally instead gives 4.2956 for the passengers against 4.3000 by category, and leaves the driver figure at 4.2850 either way, so the choice does not affect any interpretation here.

Rank orders the items within a characteristic from the highest weighted mean to the lowest. Where items tie, each is given the midpoint of the positions they jointly occupy, so that two items tied for second and third are both ranked 2.5.

[[PB]]

### G.1 Student passengers

Twenty respondents, thirty-four items across six characteristics.

**A. Functional Suitability**

| # | Indicator | 5 | 4 | 3 | 2 | 1 | N | WM | Interpretation | Rank |
|:---|:---|---:|---:|---:|---:|---:|---:|---:|:---|---:|
| 1 | The ride booking feature works as expected. | 3 | 16 | 1 | 0 | 0 | 20 | 4.10 | Agree | 6.5 |
| 2 | I receive timely notifications about my ride status. | 1 | 19 | 0 | 0 | 0 | 20 | 4.05 | Agree | 8 |
| 3 | The system accurately matches me with an available driver. | 10 | 9 | 1 | 0 | 0 | 20 | 4.45 | Strongly Agree | 1 |
| 4 | I can view the status of my booking in real time. | 4 | 14 | 2 | 0 | 0 | 20 | 4.10 | Agree | 6.5 |
| 5 | The system performs all its intended functions correctly. | 9 | 10 | 1 | 0 | 0 | 20 | 4.40 | Strongly Agree | 2 |
| 6 | The fare displayed is accurate. | 5 | 15 | 0 | 0 | 0 | 20 | 4.25 | Strongly Agree | 3.5 |
| 7 | Driver information is correctly displayed. | 5 | 15 | 0 | 0 | 0 | 20 | 4.25 | Strongly Agree | 3.5 |
| 8 | Ride history is properly recorded. | 4 | 16 | 0 | 0 | 0 | 20 | 4.20 | Agree | 5 |
| | **Category mean — A. Functional Suitability** | | | | | | | **4.22** | **Strongly Agree** | |

**B. Usability**

| # | Indicator | 5 | 4 | 3 | 2 | 1 | N | WM | Interpretation | Rank |
|:---|:---|---:|---:|---:|---:|---:|---:|---:|:---|---:|
| 1 | The system is easy to learn. | 7 | 13 | 0 | 0 | 0 | 20 | 4.35 | Strongly Agree | 4.5 |
| 2 | The menus are easy to understand. | 8 | 12 | 0 | 0 | 0 | 20 | 4.40 | Strongly Agree | 2.5 |
| 3 | Icons and buttons are properly labeled. | 8 | 12 | 0 | 0 | 0 | 20 | 4.40 | Strongly Agree | 2.5 |
| 4 | The interface is visually appealing. | 7 | 12 | 1 | 0 | 0 | 20 | 4.30 | Strongly Agree | 6 |
| 5 | I can complete my tasks without confusion. | 9 | 11 | 0 | 0 | 0 | 20 | 4.45 | Strongly Agree | 1 |
| 6 | The system is easy to learn. | 7 | 13 | 0 | 0 | 0 | 20 | 4.35 | Strongly Agree | 4.5 |
| | **Category mean — B. Usability** | | | | | | | **4.38** | **Strongly Agree** | |

**C. Efficiency**

| # | Indicator | 5 | 4 | 3 | 2 | 1 | N | WM | Interpretation | Rank |
|:---|:---|---:|---:|---:|---:|---:|---:|---:|:---|---:|
| 1 | The application loads quickly. | 6 | 14 | 0 | 0 | 0 | 20 | 4.30 | Strongly Agree | 2.5 |
| 2 | Booking requests are processed immediately. | 4 | 16 | 0 | 0 | 0 | 20 | 4.20 | Agree | 4 |
| 3 | Notifications arrive without delay. | 3 | 15 | 2 | 0 | 0 | 20 | 4.05 | Agree | 5 |
| 4 | The system performs smoothly. | 7 | 12 | 1 | 0 | 0 | 20 | 4.30 | Strongly Agree | 2.5 |
| 5 | The application consumes minimal mobile resources. | 8 | 12 | 0 | 0 | 0 | 20 | 4.40 | Strongly Agree | 1 |
| | **Category mean — C. Efficiency** | | | | | | | **4.25** | **Strongly Agree** | |

**D. Reliability**

| # | Indicator | 5 | 4 | 3 | 2 | 1 | N | WM | Interpretation | Rank |
|:---|:---|---:|---:|---:|---:|---:|---:|---:|:---|---:|
| 1 | The system recovers properly after connection interruptions. | 8 | 12 | 0 | 0 | 0 | 20 | 4.40 | Strongly Agree | 1 |
| 2 | I can depend on the system to connect me with a driver when needed. | 4 | 16 | 0 | 0 | 0 | 20 | 4.20 | Agree | 4 |
| 3 | The system maintains accurate and up-to-date information. | 5 | 15 | 0 | 0 | 0 | 20 | 4.25 | Strongly Agree | 3 |
| 4 | The system performs well even during peak hours. | 3 | 17 | 0 | 0 | 0 | 20 | 4.15 | Agree | 5 |
| 5 | I trust the system to provide a safe and reliable ride booking experience. | 6 | 14 | 0 | 0 | 0 | 20 | 4.30 | Strongly Agree | 2 |
| | **Category mean — D. Reliability** | | | | | | | **4.26** | **Strongly Agree** | |

**E. Security**

| # | Indicator | 5 | 4 | 3 | 2 | 1 | N | WM | Interpretation | Rank |
|:---|:---|---:|---:|---:|---:|---:|---:|---:|:---|---:|
| 1 | I feel that my personal information is protected. | 7 | 13 | 0 | 0 | 0 | 20 | 4.35 | Strongly Agree | 3 |
| 2 | The login process is secure. | 8 | 12 | 0 | 0 | 0 | 20 | 4.40 | Strongly Agree | 1 |
| 3 | Only authorized users can access their accounts. | 6 | 13 | 1 | 0 | 0 | 20 | 4.25 | Strongly Agree | 5 |
| 4 | Driver information is securely stored. | 9 | 9 | 2 | 0 | 0 | 20 | 4.35 | Strongly Agree | 3 |
| 5 | I trust the system to protect my data. | 7 | 13 | 0 | 0 | 0 | 20 | 4.35 | Strongly Agree | 3 |
| | **Category mean — E. Security** | | | | | | | **4.34** | **Strongly Agree** | |

**F. Overall Satisfaction**

| # | Indicator | 5 | 4 | 3 | 2 | 1 | N | WM | Interpretation | Rank |
|:---|:---|---:|---:|---:|---:|---:|---:|---:|:---|---:|
| 1 | I am satisfied with the overall performance of the system. | 8 | 12 | 0 | 0 | 0 | 20 | 4.40 | Strongly Agree | 2 |
| 2 | I would recommend this system to others. | 6 | 14 | 0 | 0 | 0 | 20 | 4.30 | Strongly Agree | 4 |
| 3 | I intend to continue using the system. | 6 | 14 | 0 | 0 | 0 | 20 | 4.30 | Strongly Agree | 4 |
| 4 | The system meets my expectations. | 9 | 11 | 0 | 0 | 0 | 20 | 4.45 | Strongly Agree | 1 |
| 5 | Overall, I rate the system as effective. | 6 | 14 | 0 | 0 | 0 | 20 | 4.30 | Strongly Agree | 4 |
| | **Category mean — F. Overall Satisfaction** | | | | | | | **4.35** | **Strongly Agree** | |

**Overall weighted mean — 4.30, Strongly Agree**

[[PB]]

### G.2 Tricycle drivers

Twenty respondents, twenty items across four characteristics.

**A. Usability**

| # | Indicator | 5 | 4 | 3 | 2 | 1 | N | WM | Interpretation | Rank |
|:---|:---|---:|---:|---:|---:|---:|---:|---:|:---|---:|
| 1 | The system is easy to navigate and use. | 8 | 11 | 1 | 0 | 0 | 20 | 4.35 | Strongly Agree | 1 |
| 2 | The interface is clean and visually understandable. | 3 | 16 | 1 | 0 | 0 | 20 | 4.10 | Agree | 4.5 |
| 3 | I was able to register and set up my account without difficulty. | 6 | 14 | 0 | 0 | 0 | 20 | 4.30 | Strongly Agree | 2 |
| 4 | The instructions and labels in the system are clear and easy to understand. | 3 | 16 | 1 | 0 | 0 | 20 | 4.10 | Agree | 4.5 |
| 5 | I can comfortably use the system without any technical assistance. | 5 | 13 | 2 | 0 | 0 | 20 | 4.15 | Agree | 3 |
| | **Category mean — A. Usability** | | | | | | | **4.20** | **Agree** | |

**B. Functionality**

| # | Indicator | 5 | 4 | 3 | 2 | 1 | N | WM | Interpretation | Rank |
|:---|:---|---:|---:|---:|---:|---:|---:|---:|:---|---:|
| 1 | The ride request feature works as expected. | 6 | 14 | 0 | 0 | 0 | 20 | 4.30 | Strongly Agree | 2.5 |
| 2 | I receive timely notifications about incoming ride requests. | 8 | 11 | 1 | 0 | 0 | 20 | 4.35 | Strongly Agree | 1 |
| 3 | The system accurately shows my availability status to passengers. | 5 | 15 | 0 | 0 | 0 | 20 | 4.25 | Strongly Agree | 4 |
| 4 | I can manage and track my rides through the system in real time. | 5 | 14 | 1 | 0 | 0 | 20 | 4.20 | Agree | 5 |
| 5 | The system performs all its intended functions correctly. | 8 | 10 | 2 | 0 | 0 | 20 | 4.30 | Strongly Agree | 2.5 |
| | **Category mean — B. Functionality** | | | | | | | **4.28** | **Strongly Agree** | |

**C. Efficiency**

| # | Indicator | 5 | 4 | 3 | 2 | 1 | N | WM | Interpretation | Rank |
|:---|:---|---:|---:|---:|---:|---:|---:|---:|:---|---:|
| 1 | The system reduces my idle time waiting for passengers. | 5 | 15 | 0 | 0 | 0 | 20 | 4.25 | Strongly Agree | 4 |
| 2 | Managing ride requests through the system is faster than the manual process. | 7 | 13 | 0 | 0 | 0 | 20 | 4.35 | Strongly Agree | 1.5 |
| 3 | The system responds quickly to my inputs and actions. | 4 | 15 | 1 | 0 | 0 | 20 | 4.15 | Agree | 5 |
| 4 | The system helps me serve more passengers throughout the day. | 8 | 11 | 1 | 0 | 0 | 20 | 4.35 | Strongly Agree | 1.5 |
| 5 | Overall, the system improves my daily work experience as a driver. | 7 | 12 | 1 | 0 | 0 | 20 | 4.30 | Strongly Agree | 3 |
| | **Category mean — C. Efficiency** | | | | | | | **4.28** | **Strongly Agree** | |

**D. Reliability**

| # | Indicator | 5 | 4 | 3 | 2 | 1 | N | WM | Interpretation | Rank |
|:---|:---|---:|---:|---:|---:|---:|---:|---:|:---|---:|
| 1 | The system works consistently without errors or crashes. | 10 | 10 | 0 | 0 | 0 | 20 | 4.50 | Strongly Agree | 1 |
| 2 | I can depend on the system to deliver ride requests accurately. | 7 | 13 | 0 | 0 | 0 | 20 | 4.35 | Strongly Agree | 3 |
| 3 | The system maintains accurate and up-to-date information. | 6 | 14 | 0 | 0 | 0 | 20 | 4.30 | Strongly Agree | 4.5 |
| 4 | The system performs well even during busy hours. | 7 | 12 | 1 | 0 | 0 | 20 | 4.30 | Strongly Agree | 4.5 |
| 5 | I trust the system to support my work as a registered driver. | 9 | 11 | 0 | 0 | 0 | 20 | 4.45 | Strongly Agree | 2 |
| | **Category mean — D. Reliability** | | | | | | | **4.38** | **Strongly Agree** | |

**Overall weighted mean — 4.29, Strongly Agree**

[[PB]]

### G.3 System administrator

One respondent, seven items across three characteristics. The figures describe a single assessment and are reported for completeness.

**A. Functional Suitability**

| # | Indicator | 5 | 4 | 3 | 2 | 1 | N | WM | Interpretation | Rank |
|:---|:---|---:|---:|---:|---:|---:|---:|---:|:---|---:|
| 1 | I can approve driver applications. | 1 | 0 | 0 | 0 | 0 | 1 | 5.00 | Strongly Agree | 2 |
| 2 | I can monitor ride activities. | 1 | 0 | 0 | 0 | 0 | 1 | 5.00 | Strongly Agree | 2 |
| 3 | I can manage user accounts. | 1 | 0 | 0 | 0 | 0 | 1 | 5.00 | Strongly Agree | 2 |
| | **Category mean — A. Functional Suitability** | | | | | | | **5.00** | **Strongly Agree** | |

**B. Usability**

| # | Indicator | 5 | 4 | 3 | 2 | 1 | N | WM | Interpretation | Rank |
|:---|:---|---:|---:|---:|---:|---:|---:|---:|:---|---:|
| 1 | The dashboard is easy to use. | 1 | 0 | 0 | 0 | 0 | 1 | 5.00 | Strongly Agree | 1.5 |
| 2 | Reports are easy to generate. | 1 | 0 | 0 | 0 | 0 | 1 | 5.00 | Strongly Agree | 1.5 |
| | **Category mean — B. Usability** | | | | | | | **5.00** | **Strongly Agree** | |

**C. Reliability**

| # | Indicator | 5 | 4 | 3 | 2 | 1 | N | WM | Interpretation | Rank |
|:---|:---|---:|---:|---:|---:|---:|---:|---:|:---|---:|
| 1 | Records are accurate. | 0 | 1 | 0 | 0 | 0 | 1 | 4.00 | Agree | 1.5 |
| 2 | Data updates correctly. | 0 | 1 | 0 | 0 | 0 | 1 | 4.00 | Agree | 1.5 |
| | **Category mean — C. Reliability** | | | | | | | **4.00** | **Agree** | |

**Overall weighted mean — 4.67, Strongly Agree**

[[PB]]

### G.4 Notes on the computation

**A duplicated item.** Part II, section B lists *"The system is easy to learn."* twice, as item 1 and again as item 6. Both are reproduced above because both appear on the administered paper and in the returns. The consequence is that section B is five distinct statements with one counted twice, which pulls the Usability mean toward that single statement. The effect is small here only because the duplicate happened to draw the same distribution as the original: dropping it moves the Usability category mean from 4.3750 to 4.3800 and the overall passenger mean from 4.3000 to 4.3008, neither of which changes a figure at two decimal places or shifts an interpretation. That it did no harm is luck rather than design, and the item should be replaced before any further administration.

**One administrator.** The administrator instrument was answered by a single respondent, which is total enumeration of that role rather than a sample. Its means describe one person's assessment, and the ranks within its three characteristics distinguish between values drawn from that one questionnaire. Both are reported for completeness; no inference beyond the individual is available from N = 1, and the administrator's figures are therefore reported separately throughout rather than pooled with the two groups of twenty.

**Distribution of responses.** No respondent selected 1 or 2 on any of the sixty-one items, and item means run from 4.05 to 4.50 — a range of 0.45 across the whole instrument. A distribution this narrow and this uniformly positive is consistent with genuine approval, and equally with acquiescence bias and with respondents who knew the researchers. The returns alone cannot distinguish between these, and the limitation is recorded here rather than left for the reader to infer.

## Appendix H — System Screenshots
*[Screenshots are to be captured from the running application and inserted here. The following set is recommended, covering every module in Table 15.]*

1. Onboarding carousel, first and final slides
2. Welcome-back screen
3. Registration screen showing the live password rule checklist
4. Sign-in screen with remembered email
5. Passenger home with no active ride
6. Booking screen with the itemized fare displayed
7. Destination picker with a search term entered
8. Searching for a driver
9. Ride tracking with the status timeline
10. Ride completion summary and rating
11. Passenger ride history
12. Concern submission form
13. Notification centre with unread items
14. Driver dashboard, offline and online
15. Driver request card with countdown
16. Driver ride in progress with the advance button
17. Driver earnings
18. Driver credentials card showing verification status
19. Administrator verification tab with a pending badge
20. Administrator concerns tab with a concern open for review
21. Administrator live monitor
22. Administrator reports tab with a period selected and the summary shown
23. Administrator fare table with a zone filter applied
24. Administrator fare table filtered to entries needing review
25. Fare entry editing dialog showing a transcription note
26. Minimums and flat rates dialog
27. Profile screen with a photograph set
28. Dark theme, any two screens

## Appendix I — User Manual
The manual issued with the application, covering all three kinds of account. It is written
to be read in parts: a passenger needs Part 1 and Part 2 and nothing else. Part 5 lists the
problems encountered during development and testing, and what to do about each. Screenshots
of every screen it describes are in Appendix H.

The same text is kept in the repository as `docs/user-manual.md`, so that it can be issued
on its own without the rest of this document.

[[PB]]

### Part 1 — Installing the app

TrikRide is on Google Play. Install it the way you would install any other app.

1. **Open the Play Store** and search for **TrikRide**, or tap the link the administrator
   gives you.
2. **Tap Install**, then **Open** when it finishes.
3. **Permissions.** The app asks for location the first time you use a screen that needs
   it, and for the camera the first time you take a photograph. Both can be declined:
   without location you can still book by choosing your pickup from the list, and without
   the camera you can still choose a photograph from your gallery.

Updates arrive through Google Play like any other app, so there is nothing to do to stay
on the current version. Your account and your history are held on the server rather than
on the phone, so nothing is lost when the app updates or is installed again.

[[PB]]

### Part 2 — For passengers

#### 2.1 First launch

Five introductory slides appear the first time the app opens. They are shown once. Swipe
through or skip.

#### 2.2 Creating an account

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

#### 2.3 Signing in

Enter your email and password and tap **Login**. Tick **Remember me** and the email is
filled in next time; the password never is.

You stay signed in between launches, so the app opens straight to your dashboard. Signing
out is in Profile.

**Forgotten password.** Tap **Forgot Password?**, confirm your email address, and tap
**Send link**. Firebase emails you a link to set a new password. If nothing arrives within
a few minutes, check the spam folder — and check the address is the one you registered
with, because for privacy the app gives the same reply whether or not an account exists.

#### 2.4 Agreeing to the documents

If the documents have been revised since you last accepted them, a screen appears between
signing in and your dashboard. Each document has to be ticked separately. The only
alternative is signing out. This also happens the first time an older account opens a
version of the app that records consent.

#### 2.5 The dashboard

Four tabs along the bottom: **Home**, **History**, **Support**, **Profile**. The bell at
the top right opens your notifications and carries a count of the unread ones.

#### 2.6 Booking a ride

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
3. **Who is travelling.** Tap every kind of passenger in your party — **Regular**,
   **Senior / PWD / Student**, or both — and a counter appears for each one you choose.
   Set how many of each, up to five between them. A party can be a mix: two friends and a
   grandmother is two regular and one discounted, and each is charged from its own column
   of the posted sheet. To drop a kind entirely, tap its chip again. Bring the
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

#### 2.7 Waiting for a driver

The request goes to every online, verified driver at once, and the first to accept gets
it. If nobody accepts within five minutes it expires and you can send it again. **Cancel
Request** withdraws it before then.

If nothing happens, it usually means no drivers are online rather than that anything is
broken.

#### 2.8 During the ride

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

#### 2.9 Rating and history

When the ride completes you are asked to rate the driver from one to five stars. Tap the
stars, then **Send rating** — tapping a star does not send it, so a slip is not a rating.
You can skip it, but only once: a ride can be rated one time.

**History** lists your past rides, most recent first, with the fare and how each ended.

#### 2.10 Reporting a concern

**Support** has a form: choose a category — driver behaviour, wrong fare, safety concern,
app problem, or other — describe what happened, and tap **Submit Report**. An
administrator reviews it, and any reply appears under **My Reports** on the same tab, along
with whether it is still open, in review, or resolved.

The hotline, email address and support hours are at the bottom of the same tab. Use the
hotline, not the app, for anything urgent.

#### 2.11 Your profile

**Profile** holds:

- **Edit Profile** — name, phone, and your photograph, from the camera or the gallery.
  After choosing one you can pinch to zoom and drag to position it inside the circle, so
  you decide which part is used.
- **Change Password** — emails you a reset link.
- **Dark Mode** — a switch.
- The four documents, readable at any time.
- **Log Out.**

[[PB]]

### Part 3 — For drivers

#### 3.1 Registering

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

#### 3.2 The licence photograph

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

#### 3.3 While you wait for verification

Your dashboard shows **Verification: PENDING**. You cannot accept passengers until an
administrator approves you, and requests will not appear. The decision arrives as a
notification. If you are rejected, the reason is in that notification and your licence
photograph is deleted along with the refusal.

#### 3.4 Going online

The dashboard has a switch, and a **Go Online** button that does the same thing. Online
means requests reach you and your position is published to the passenger of any ride you
are on. Offline means neither.

Your position is published **only while you are online with the app open**. There is no
background tracking. Close the app and your passenger stops seeing you move, so keep it
open on the way to a pickup.

#### 3.5 Taking a ride

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

#### 3.6 Earnings, history and the rest

The dashboard shows today's earnings. **History** lists every completed and cancelled trip
with a running total. Your rating and trip count are at the top of the dashboard and on
your Profile tab.

**Support** works the same way it does for a passenger: a form for reporting a concern,
your own past reports and their status, and the hotline.

**Profile** holds your credentials card, your licence photograph, the profile photo
picker, dark mode, the four documents including the Driver Agreement, and Log Out.

[[PB]]

### Part 4 — For administrators

An administrator account is not created in the app. An existing account is promoted by
setting its `userType` to `ADMIN` in the Firebase console — the security rules deliberately
stop an account from doing that to itself. Sign out and back in afterwards.

Five tabs: **Verify**, **Concerns**, **Monitor**, **Fares**, **Profile**.

#### 4.1 First run: load the fare table

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

#### 4.2 Verifying drivers

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

#### 4.3 Concerns

**Concerns** carries a badge with the number still open. Each shows who filed it, whether
they are a passenger or a driver, the category, and what they wrote.

**Respond** opens a note field. Write what you are doing about it and mark it **In review**
or **Resolve**. The note is visible to the person who filed it, under My Reports on their
Support tab.

#### 4.4 Monitor

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

[[PB]]

### Part 5 — When something goes wrong

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

[[PB]]

## Appendix J — Source Code
The complete source of the system is reproduced here, in the order in which the layers depend on one another: the manifest and entry point first, then the data models, the single service that reaches the database, the repositories that sit between it and the ViewModels, the ViewModels themselves, the utilities, and finally the interface. The unit tests and the build configuration follow.

Two files required to build the project are deliberately not reproduced and are not held in version control, because they carry credentials: `google-services.json`, the Firebase configuration downloaded from the project console, and `.env`, which holds the Maps key, the support contact details and the release signing passwords. A committed `.env.example` records which keys are required without their values. The database security rules in Section I.11 are reproduced because they are the whole of the system's access control — the application speaks to Firebase directly, with no server in between, so anything those rules permit is permitted.

Binary resources — launcher icons, onboarding artwork — are not reproduced.

[[PB]]

### I.1 Application entry point and manifest

#### `app/src/main/AndroidManifest.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <uses-permission android:name="android.permission.INTERNET" />

    <!-- Taking a profile photo. Choosing one from the gallery needs no permission:
         the photo picker returns a single image the user selected themselves. -->
    <uses-permission android:name="android.permission.CAMERA" />

    <!-- Ride notifications on Android 13 and later. -->
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

    <!-- Pinning a pickup point, and publishing a driver's position while they are
         online with the app open. Neither is used in the background. -->
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

    <!-- Android 11 hides installed packages unless they are named here. Without
         this the app cannot tell whether a driver has Waze or Maps, and the
         hand-off buttons would offer apps that are not there. -->
    <queries>
        <package android:name="com.waze" />
        <package android:name="com.google.android.apps.maps" />
    </queries>

    <application
        android:allowBackup="false"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:theme="@style/Theme.TrikRide"
        android:usesCleartextTraffic="false">

        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:theme="@style/Theme.TrikRide">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <!-- Firebase Cloud Messaging Service -->
        <service
            android:name=".services.TrikRideMessagingService"
            android:exported="false">
            <intent-filter>
                <action android:name="com.google.firebase.MESSAGING_EVENT" />
            </intent-filter>
        </service>

        <!-- Hands camera captures back to the app as a content:// URI. -->
        <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="${applicationId}.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_paths" />
        </provider>

        <!-- Google Maps API Key (injected from .env via manifest placeholder) -->
        <meta-data
            android:name="com.google.android.geo.API_KEY"
            android:value="${MAPS_API_KEY}" />

    </application>

</manifest>
```

#### `app/src/main/java/com/tpc/trikride/MainActivity.kt`

```kotlin
package com.tpc.trikride

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.tpc.trikride.ui.screens.MainAppScreen
import com.tpc.trikride.ui.theme.ThemeState
import com.tpc.trikride.ui.theme.TrikRideTheme
import com.tpc.trikride.utils.CacheCleanup

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Camera captures and exported reports are written to the cache and were
        // never removed. A licence photograph and a report naming every user are
        // both things that should not outlive the action that made them.
        CacheCleanup.sweep(cacheDir)

        // Before the first frame, so the chosen theme does not flash the other
        // one on the way in.
        ThemeState.load(this)

        setContent {
            // Follow the system theme unless the user has overridden it in Settings.
            val darkTheme = ThemeState.darkModeOverride ?: isSystemInDarkTheme()

            TrikRideTheme(darkTheme = darkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainAppScreen()
                }
            }
        }
    }
}
```

[[PB]]

### I.2 Data models

#### `app/src/main/java/com/tpc/trikride/models/AppNotification.kt`

```kotlin
package com.tpc.trikride.models

/**
 * An in-app notification written when something happens on a ride or an
 * account. Stored per user under notifications/{userId}/{id}.
 */
data class AppNotification(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val message: String = "",
    val type: NotificationType = NotificationType.GENERAL,
    val read: Boolean = false,
    val createdAt: String = ""
)

enum class NotificationType {
    RIDE,
    COMPLAINT,
    ACCOUNT,
    GENERAL
}
```

#### `app/src/main/java/com/tpc/trikride/models/Complaint.kt`

```kotlin
package com.tpc.trikride.models

/** A concern or complaint raised by a passenger or driver, reviewed by admin. */
data class Complaint(
    val id: String = "",
    val reporterId: String = "",
    val reporterName: String = "",
    val reporterType: UserType = UserType.PASSENGER,
    val category: String = "Other",
    val description: String = "",
    val status: ComplaintStatus = ComplaintStatus.OPEN,
    val adminNote: String = "",
    val createdAt: String = "",
    val resolvedAt: String = ""
)

enum class ComplaintStatus {
    OPEN,
    IN_REVIEW,
    RESOLVED
}

/** Selectable complaint categories shown on the Support form. */
val COMPLAINT_CATEGORIES = listOf(
    "Driver behavior",
    "Passenger behavior",
    "Wrong fare",
    "Safety concern",
    "App problem",
    "Other"
)
```

#### `app/src/main/java/com/tpc/trikride/models/FareConfig.kt`

```kotlin
package com.tpc.trikride.models

import com.google.firebase.database.Exclude

/**
 * One priced destination from the posted FeTODAT fare sheet.
 *
 * The sheet is organised by zone, and within a zone by stop, with two columns:
 * the regular rate and the discounted rate for seniors, persons with
 * disabilities and students. There is no distance formula on the sheet, so
 * there is none in the app either — the stop carries its own price.
 *
 * [needsReview] marks a row that came out of transcription with a problem
 * worth a human look. [active] false keeps a stop out of the passenger's
 * picker entirely, which is what happens when a rate is missing or clearly
 * wrong.
 */
data class FareStop(
    val id: String = "",
    val zone: String = "",
    val name: String = "",
    val regularFare: Double = 0.0,
    val discountedFare: Double = 0.0,
    val active: Boolean = true,
    val needsReview: Boolean = false,
    val confidence: String = "High",
    val note: String = "",
    // Optional. The posted sheet gives names, not coordinates, so these are
    // filled in by the administrator over time. A stop without them still
    // prices and books normally; it just does not appear on the map.
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) {
    /**
     * What the passenger sees in the picker. Excluded from the database write,
     * since Firebase would otherwise persist every public getter as a field and
     * then fail to find somewhere to put it on the way back in.
     */
    @get:Exclude
    val label: String get() = if (zone.isBlank()) name else "$name — $zone"

    @get:Exclude
    val hasCoordinates: Boolean get() = latitude != 0.0 || longitude != 0.0

    @get:Exclude
    val location: Location
        get() = Location(latitude = latitude, longitude = longitude, address = label)
}

/** Which column of the fare sheet applies to this passenger. */
enum class FareType {
    REGULAR,
    DISCOUNTED;

    val label: String
        get() = when (this) {
            REGULAR -> "Regular"
            DISCOUNTED -> "Senior / PWD / Student"
        }
}

/**
 * The parts of the fare sheet that are not tied to one stop: the two minimum
 * fares, the flat rates, and whether a fare is charged per head.
 *
 * Stored at config/fare. The stops live separately under config/fareStops so
 * that correcting one price is a small write rather than a rewrite of all 240.
 */
data class FareConfig(
    /**
     * No ride is charged less than this, whatever the posted rate for the
     * destination says. Twenty-seven rows in the transcribed table sit at ₱20
     * and are raised to the minimum, which either means the sheet those rows
     * came from predates the current rate or that they were read wrong; the
     * admin fare screen shows which quotes had the minimum applied.
     */
    val minimumRegular: Double = 25.0,
    /**
     * Twenty per cent off, matching the ratio every discounted rate in the
     * table holds to its regular one. Confirm against the posted sheet before
     * carrying passengers — it is one edit in the admin fare screen.
     */
    val minimumDiscounted: Double = 20.0,
    val poblacionFlat: Double = 25.0,
    val terminalRoundTrip: Double = 25.0,
    val chargePerPassenger: Boolean = true,
    val source: String = DEFAULT_SOURCE,
    val seededAt: String = ""
) {
    companion object {
        const val DEFAULT_SOURCE =
            "FeTODAT — ordinance amending Section 1 of Municipal Ordinance No. 2018-05, " +
                "enacted 8 November 2022"
        /**
         * Leads with the place, because that is what a passenger types. The
         * ordinance prices Poblacion as a flat rate rather than as a named
         * stop, so there is no "Poblacion" row in the 240 to find, and a label
         * beginning "Any point within" sorted and read as though there were
         * none at all.
         */
        const val POBLACION_LABEL = "Poblacion, Talibon — any point within"
        const val TERMINAL_ROUND_TRIP_LABEL =
            "Talibon Integrated Bus Terminal (TIBT) to NCBI, round trip"
    }
}

/**
 * A priced ride, broken down so the passenger can see where the number came from.
 *
 * A booking may carry both kinds of passenger at once — a student travelling
 * with a parent is one tricycle and two different rates — so the two columns of
 * the posted sheet are priced separately and added, rather than one rate being
 * multiplied by everybody.
 */
data class FareQuote(
    val regularCount: Int = 0,
    val discountedCount: Int = 0,
    /** Per head, after the ordinance minimum has been applied to each column. */
    val regularRate: Double = 0.0,
    val discountedRate: Double = 0.0,
    val total: Double = 0.0,
    /** True when either column's posted rate fell below its minimum. */
    val minimumApplied: Boolean = false,
    val stopLabel: String = ""
) {
    @get:Exclude
    val passengers: Int get() = regularCount + discountedCount

    /** "2 regular, 1 discounted", or just the one kind when that is all there is. */
    @get:Exclude
    val partyLabel: String
        get() = listOfNotNull(
            regularCount.takeIf { it > 0 }?.let { "$it regular" },
            discountedCount.takeIf { it > 0 }?.let { "$it discounted" }
        ).joinToString(", ").ifBlank { "no passengers" }
}
```

#### `app/src/main/java/com/tpc/trikride/models/Ride.kt`

```kotlin
package com.tpc.trikride.models

import com.google.firebase.database.Exclude

data class Ride(
    val id: String = "",
    val passengerId: String = "",
    val driverId: String = "",
    val pickupLocation: Location = Location(),
    val dropoffLocation: Location = Location(),
    val status: RideStatus = RideStatus.REQUESTED,
    val requestedAt: String = "",
    val acceptedAt: String = "",
    val startedAt: String = "",
    val completedAt: String = "",
    val estimatedFare: Double = 0.0,
    /** What the driver reported collecting. Cash, so it can differ from the quote. */
    val actualFare: Double = 0.0,
    val passengerCount: Int = 1,
    /**
     * How the party splits across the two columns of the posted sheet. A
     * booking can hold both — a student with a parent is one tricycle and two
     * rates — so [fareType] alone cannot describe it. Kept alongside
     * [passengerCount], which stays the total.
     */
    val regularCount: Int = 0,
    val discountedCount: Int = 0,
    val luggage: String = "None",
    // Which row of the posted fare table priced this ride, and which column.
    val fareStopId: String = "",
    /** The column that applies when the whole party is one kind. Mixed parties
     *  are described by the two counts above. */
    val fareType: FareType = FareType.REGULAR,
    val notes: String = "",
    /**
     * Enough of each party for the other to find them, and no more.
     *
     * Names and telephone numbers live on `users/{uid}`, which the rules keep
     * private to the account and to administrators, so neither side could see
     * the other at all. Copying the two fields onto the ride shares them with
     * exactly the person on the other end of it and nobody else, which is what
     * the Privacy Policy says happens. The driver writes their own half when
     * they accept; the passenger writes theirs once the ride exists.
     */
    val driverName: String = "",
    val driverPhone: String = "",
    val passengerName: String = "",
    val passengerPhone: String = ""
) {
    /**
     * "2 regular, 1 discounted", for a driver who needs to know who to expect
     * and which IDs to ask for. Rides booked before the split existed carry
     * neither count, so they fall back to the single column they were priced on.
     */
    @get:Exclude
    val partyLabel: String
        get() = when {
            regularCount > 0 || discountedCount > 0 -> listOfNotNull(
                regularCount.takeIf { it > 0 }?.let { "$it regular" },
                discountedCount.takeIf { it > 0 }?.let { "$it senior/PWD/student" }
            ).joinToString(", ")
            fareType == FareType.REGULAR -> "$passengerCount regular"
            else -> "$passengerCount senior/PWD/student"
        }
}

enum class RideStatus {
    REQUESTED,
    SEARCHING,
    ACCEPTED,
    DRIVER_ARRIVING,
    DRIVER_ARRIVED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED,
    NO_SHOW
}

data class RideRequest(
    val id: String = "",
    val passengerId: String = "",
    val pickupLocation: Location = Location(),
    val dropoffLocation: Location = Location(),
    val requestedAt: String = "",
    val expiresAt: String = "",
    val passengerCount: Int = 1,
    val regularCount: Int = 0,
    val discountedCount: Int = 0,
    val luggage: String = "None",
    val estimatedFare: Double = 0.0,
    val fareStopId: String = "",
    val fareType: FareType = FareType.REGULAR,
    val notes: String = "",
    val preferredDriverId: String? = null
)
```

#### `app/src/main/java/com/tpc/trikride/models/User.kt`

```kotlin
package com.tpc.trikride.models

data class User(
    val id: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val idNumber: String = "",
    val birthDate: String = "",
    val userType: UserType = UserType.PASSENGER,
    // Kept for compatibility with records written before photos moved into the
    // database; nothing writes it now.
    val profileImageUrl: String = "",
    val createdAt: String = "",
    val updatedAt: String = "",
    // Which version of the legal documents this account has accepted. Empty
    // means the account predates consent tracking and must accept before use.
    val acceptedLegalVersion: String = "",
    val acceptedLegalAt: String = "",
    /** Drivers accept an additional agreement; blank for passengers. */
    val acceptedDriverAgreementVersion: String = ""
)

enum class UserType {
    PASSENGER,
    DRIVER,
    ADMIN
}

data class Driver(
    val userId: String = "",
    val tricycleNumber: String = "",
    val verificationStatus: VerificationStatus = VerificationStatus.PENDING,
    val isAvailable: Boolean = false,
    val currentLocation: Location? = null,
    val rating: Double = 0.0,
    /** How many ratings [rating] averages. Not the ride count — most rides go unrated. */
    val ratingCount: Int = 0,
    val totalRides: Int = 0,
    val verifiedAt: String = "",
    /**
     * Whether a licence photograph is on file. The image itself lives under
     * `driverDocuments/{uid}`, not here — the admin screens read every driver
     * record constantly and must not pull a few hundred kilobytes of licence
     * with each one. This flag is what those screens need: enough to show
     * whether there is anything to review.
     */
    val hasLicenceImage: Boolean = false
)

enum class VerificationStatus {
    PENDING,
    APPROVED,
    REJECTED,
    EXPIRED
}

/**
 * A driver's licence as the system holds it, for verification.
 *
 * The number, the expiry and the photograph together. All three are sensitive
 * personal information under the Data Privacy Act of 2012 — the Act names
 * government-issued identifiers explicitly — which is why they sit here rather
 * than on the driver record. `drivers/{uid}` is readable by every signed-in
 * account, because a passenger needs the availability and position on it; this
 * node is readable only by the driver and an administrator.
 *
 * [consentedAt] records that the driver was told what the photograph is for
 * before sending it.
 */
data class DriverDocument(
    val licenceNumber: String = "",
    val licenceExpiry: String = "",
    val image: String = "",
    val uploadedAt: String = "",
    val consentedAt: String = "",
    val reviewedAt: String = ""
)

data class Location(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val address: String = "",
    val timestamp: String = ""
) {
    /**
     * Whether this carries a real position. A default Location is 0,0, which is
     * a point in the Atlantic, so plotting it would send the map to the wrong
     * hemisphere rather than show nothing.
     */
    @get:com.google.firebase.database.Exclude
    val hasCoordinates: Boolean
        get() = latitude != 0.0 || longitude != 0.0
}
```

[[PB]]

### I.3 Services

#### `app/src/main/java/com/tpc/trikride/services/FirebaseService.kt`

```kotlin
package com.tpc.trikride.services

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Query
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import com.tpc.trikride.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseService {
    private val database = FirebaseDatabase.getInstance()

    /** Bridges a Google Play Services [Task] into a coroutine. */
    private suspend fun <T> Task<T>.await(): T = suspendCancellableCoroutine { cont ->
        addOnSuccessListener { cont.resume(it) }
        addOnFailureListener { cont.resumeWithException(it) }
    }

    /**
     * A Firebase query as a flow of snapshots.
     *
     * Every listener in this class used to deserialise inside `onDataChange`,
     * which Firebase calls on the main thread. For the administrator's feeds
     * that meant reflectively mapping every ride, user and complaint in the
     * database onto data classes on the frame that received them, on every
     * change anyone made. Emitting the snapshot and mapping it downstream with
     * `flowOn` moves that work off the UI thread; a DataSnapshot is immutable,
     * so it is safe to read from another one.
     */
    private fun snapshots(query: Query): Flow<DataSnapshot> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        query.addValueEventListener(listener)
        awaitClose { query.removeEventListener(listener) }
    }

    /** Maps each snapshot's children off the main thread. */
    private fun <T : Any> children(query: Query, type: Class<T>): Flow<List<T>> =
        snapshots(query)
            .map { snap -> snap.children.mapNotNull { it.getValue(type) } }
            .flowOn(Dispatchers.Default)

    // Driver Operations
    suspend fun registerDriver(userId: String, driver: Driver) {
        database.getReference("drivers").child(userId).setValue(driver).await()
    }

    fun getDriverFlow(driverId: String): Flow<Driver?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val driver = snapshot.getValue(Driver::class.java)
                trySend(driver)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        val ref = database.getReference("drivers").child(driverId)
        ref.addValueEventListener(listener)

        awaitClose { ref.removeEventListener(listener) }
    }

    /** All registered drivers, regardless of availability (admin view). */
    fun getAllDriversFlow(): Flow<List<Driver>> =
        children(database.getReference("drivers"), Driver::class.java)

    /** All users (admin view — used to resolve driver names). */
    fun getAllUsersFlow(): Flow<List<User>> =
        children(database.getReference("users"), User::class.java)

    /** All rides across the system (admin monitoring and the exported reports). */
    fun getAllRidesFlow(): Flow<List<Ride>> =
        children(database.getReference("rides"), Ride::class.java)

    suspend fun updateDriverLocation(driverId: String, location: Location) {
        database.getReference("drivers").child(driverId).child("currentLocation").setValue(location).await()
    }

    suspend fun updateDriverAvailability(driverId: String, isAvailable: Boolean) {
        database.getReference("drivers").child(driverId).child("isAvailable").setValue(isAvailable).await()
    }

    suspend fun updateDriverVerification(driverId: String, status: VerificationStatus) {
        database.getReference("drivers").child(driverId).child("verificationStatus").setValue(status).await()
    }

    // Ride Operations
    suspend fun createRideRequest(rideRequest: RideRequest) {
        database.getReference("rideRequests").child(rideRequest.id).setValue(rideRequest).await()
    }

    fun getOpenRideRequestsFlow(): Flow<List<RideRequest>> =
        children(database.getReference("rideRequests"), RideRequest::class.java)
            .map { requests ->
                val now = System.currentTimeMillis()
                requests.filter { (it.expiresAt.toLongOrNull() ?: Long.MAX_VALUE) > now }
            }

    suspend fun removeRideRequest(requestId: String) {
        database.getReference("rideRequests").child(requestId).removeValue().await()
    }

    /**
     * Claims an open request for one driver, and says whether the claim won.
     *
     * Accepting used to be "write a ride, then delete the request" — two
     * independent writes with nothing between them, so two drivers who tapped
     * Accept before either write landed both created a ride for one booking and
     * the passenger tracked whichever the listener handed back first. The
     * transaction is the resolution: the first caller writes `claimedBy` and
     * every later one aborts, so exactly one driver goes on to create the ride.
     */
    suspend fun claimRideRequest(requestId: String, driverId: String): Boolean =
        suspendCancellableCoroutine { cont ->
            database.getReference("rideRequests").child(requestId)
                .runTransaction(object : Transaction.Handler {
                    override fun doTransaction(current: MutableData): Transaction.Result {
                        // Gone already: another driver accepted and cleaned up.
                        if (current.getValue() == null) return Transaction.abort()
                        val claimed = current.child("claimedBy").getValue(String::class.java)
                        if (!claimed.isNullOrBlank() && claimed != driverId) {
                            return Transaction.abort()
                        }
                        current.child("claimedBy").value = driverId
                        return Transaction.success(current)
                    }

                    override fun onComplete(
                        error: DatabaseError?,
                        committed: Boolean,
                        snapshot: DataSnapshot?
                    ) {
                        if (!cont.isActive) return
                        if (error != null) cont.resumeWithException(error.toException())
                        else cont.resume(committed)
                    }
                })
        }

    /**
     * Deletes requests that have run out of time.
     *
     * Nothing removed them before: the open-requests flow filtered expired ones
     * after downloading them, so every request ever made stayed in the node and
     * every approved driver pulled the lot — pickup coordinates and free-text
     * notes included — on every change. A passenger who force-closed the app
     * mid-search left theirs there permanently.
     */
    /** The one unexpired request this passenger has open, if any. */
    suspend fun findOpenRequestFor(
        passengerId: String,
        now: Long = System.currentTimeMillis()
    ): RideRequest? {
        val snapshot = database.getReference("rideRequests")
            .orderByChild("passengerId").equalTo(passengerId).get().await()
        return snapshot.children
            .mapNotNull { it.getValue(RideRequest::class.java) }
            .firstOrNull { (it.expiresAt.toLongOrNull() ?: 0L) > now }
    }

    suspend fun purgeExpiredRideRequests(now: Long = System.currentTimeMillis()) {
        val root = database.getReference("rideRequests")
        val snapshot = root.get().await()
        val dead = snapshot.children.mapNotNull { child ->
            val request = child.getValue(RideRequest::class.java) ?: return@mapNotNull null
            child.key?.takeIf { (request.expiresAt.toLongOrNull() ?: Long.MAX_VALUE) <= now }
        }
        if (dead.isEmpty()) return
        root.updateChildren(dead.associateWith { null as Any? }).await()
    }

    suspend fun createRide(ride: Ride) {
        database.getReference("rides").child(ride.id).setValue(ride).await()
    }

    /**
     * Moves a ride to [status] and stamps the timestamp that goes with it.
     *
     * `startedAt` and `completedAt` are on the record and were never written by
     * anything, so the Started and Completed columns of every exported report
     * were blank. A ride that ends — completed, cancelled or a no-show — stamps
     * `completedAt`, because that is the moment it stopped being live and it is
     * what the reports measure duration against.
     */
    suspend fun updateRideStatus(rideId: String, status: RideStatus) {
        val now = System.currentTimeMillis().toString()
        val updates = mutableMapOf<String, Any?>("status" to status.name)
        when (status) {
            RideStatus.IN_PROGRESS -> updates["startedAt"] = now
            RideStatus.COMPLETED, RideStatus.CANCELLED, RideStatus.NO_SHOW ->
                updates["completedAt"] = now
            else -> Unit
        }
        database.getReference("rides").child(rideId).updateChildren(updates).await()
    }

    /**
     * Records what the driver says was actually collected.
     *
     * Cash changes hands off the app, so the quoted fare and the fare taken can
     * differ. `actualFare` was read by every report and written by nothing,
     * which is why the Actual fare column was a column of zeroes.
     */
    suspend fun recordActualFare(rideId: String, amount: Double) {
        database.getReference("rides").child(rideId).child("actualFare").setValue(amount).await()
    }

    /**
     * Writes the passenger's own two contact fields and nothing else.
     *
     * Named children rather than the whole node, because the rules grant the
     * passenger write on exactly these two and refuse anything wider — the ride
     * itself belongs to the driver.
     */
    suspend fun attachPassengerContact(rideId: String, name: String, phone: String) {
        val ref = database.getReference("rides").child(rideId)
        ref.child("passengerName").setValue(name).await()
        ref.child("passengerPhone").setValue(phone).await()
    }

    // Scoped at the source rather than after the download: the rule on /rides
    // requires this exact constraint, so the client cannot ask for anyone
    // else's rides.
    fun getActiveRidesFlow(passengerId: String): Flow<List<Ride>> =
        children(
            database.getReference("rides").orderByChild("passengerId").equalTo(passengerId),
            Ride::class.java
        ).map { rides -> rides.filter { it.status !in TERMINAL } }

    fun getDriverActiveRidesFlow(driverId: String): Flow<List<Ride>> =
        children(
            database.getReference("rides").orderByChild("driverId").equalTo(driverId),
            Ride::class.java
        ).map { rides -> rides.filter { it.status !in TERMINAL } }

    fun getPassengerRideHistoryFlow(passengerId: String): Flow<List<Ride>> =
        children(
            database.getReference("rides").orderByChild("passengerId").equalTo(passengerId),
            Ride::class.java
        ).map { rides -> rides.filter { it.status in TERMINAL }.newestFirst() }

    fun getDriverRideHistoryFlow(driverId: String): Flow<List<Ride>> =
        children(
            database.getReference("rides").orderByChild("driverId").equalTo(driverId),
            Ride::class.java
        ).map { rides -> rides.filter { it.status in TERMINAL }.newestFirst() }

    // Ratings
    //
    // A rating is written by the passenger to `driverRatings/{driver}/{rater}`,
    // one value per passenger per driver, because that is a path the security
    // rules can scope to the person writing it. The driver record itself is
    // writable only by the driver, so a passenger cannot be the one to update
    // the average there — see `publishRating`.

    /**
     * One rating per ride.
     *
     * The key used to be the rater, which meant a passenger held exactly one
     * opinion of a driver however many times they travelled: a second ride with
     * the same driver silently replaced the first. Keying by ride records each
     * journey separately, and it is the shape a rule can check — a ride names
     * both parties and carries the status, so the rules can require that the
     * writer was the passenger on it and that it actually completed.
     */
    suspend fun submitRating(driverId: String, rideId: String, raterId: String, stars: Int) {
        database.getReference("driverRatings").child(driverId).child(rideId)
            .setValue(mapOf("stars" to stars, "raterId" to raterId)).await()
    }

    /** A ride that has finished, one way or another. */
    private val TERMINAL = setOf(RideStatus.COMPLETED, RideStatus.CANCELLED, RideStatus.NO_SHOW)

    private fun List<Ride>.newestFirst(): List<Ride> =
        sortedByDescending { it.requestedAt.toLongOrNull() ?: 0L }

    /**
     * Every rating a driver has been given.
     *
     * Reads both shapes. Ratings written before they were keyed by ride are a
     * bare number under the rater's uid; ones written since are an object with
     * the stars and the rater on it. Dropping the old ones would erase a
     * driver's history the first time they opened the new build.
     */
    fun getRatingsFlow(driverId: String): Flow<List<Int>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(
                    snapshot.children.mapNotNull { child ->
                        child.getValue(Int::class.java)
                            ?: child.child("stars").getValue(Int::class.java)
                    }
                )
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        val ref = database.getReference("driverRatings").child(driverId)
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    /**
     * Caches a driver's average onto their own record.
     *
     * Called from the driver's device, because only they may write there. The
     * admin screens and the exported reports read the cached figure rather than
     * averaging every rating in the database on every list refresh.
     */
    suspend fun publishRating(driverId: String, average: Double, count: Int) {
        database.getReference("drivers").child(driverId).updateChildren(
            mapOf("rating" to average, "ratingCount" to count)
        ).await()
    }

    /** Counts one more finished ride against the driver, for their totals. */
    suspend fun recordCompletedRide(driverId: String): Unit =
        suspendCancellableCoroutine { cont ->
            database.getReference("drivers").child(driverId)
                .runTransaction(object : Transaction.Handler {
                    override fun doTransaction(current: MutableData): Transaction.Result {
                        val total = current.child("totalRides").getValue(Int::class.java) ?: 0
                        current.child("totalRides").value = total + 1
                        return Transaction.success(current)
                    }

                    override fun onComplete(
                        error: DatabaseError?,
                        committed: Boolean,
                        snapshot: DataSnapshot?
                    ) {
                        if (cont.isActive) {
                            if (error != null) cont.resumeWithException(error.toException())
                            else cont.resume(Unit)
                        }
                    }
                })
        }

    // Complaints
    suspend fun submitComplaint(complaint: Complaint) {
        database.getReference("complaints").child(complaint.id).setValue(complaint).await()
    }

    fun getAllComplaintsFlow(): Flow<List<Complaint>> =
        children(database.getReference("complaints"), Complaint::class.java)

    fun getUserComplaintsFlow(userId: String): Flow<List<Complaint>> =
        // Scoped at the source: the rule on /complaints requires this
        // constraint, so nobody can read what somebody else reported.
        children(
            database.getReference("complaints").orderByChild("reporterId").equalTo(userId),
            Complaint::class.java
        )

    suspend fun updateComplaintStatus(id: String, status: ComplaintStatus, note: String) {
        val updates = mutableMapOf<String, Any?>(
            "status" to status.name,
            "adminNote" to note
        )
        if (status == ComplaintStatus.RESOLVED) {
            updates["resolvedAt"] = System.currentTimeMillis().toString()
        }
        database.getReference("complaints").child(id).updateChildren(updates).await()
    }

    // Notifications
    suspend fun pushNotification(notification: AppNotification) {
        database.getReference("notifications")
            .child(notification.userId)
            .child(notification.id)
            .setValue(notification).await()
    }

    fun getNotificationsFlow(userId: String): Flow<List<AppNotification>> =
        children(database.getReference("notifications").child(userId), AppNotification::class.java)

    suspend fun markNotificationRead(userId: String, id: String) {
        database.getReference("notifications").child(userId).child(id)
            .child("read").setValue(true).await()
    }

    suspend fun markAllNotificationsRead(userId: String, ids: List<String>) {
        val ref = database.getReference("notifications").child(userId)
        // Awaited one at a time: forEach returns Unit, so awaiting the loop
        // rather than the writes inside it did not compile and, had it, would
        // have returned before any of them landed.
        ids.forEach { ref.child(it).child("read").setValue(true).await() }
    }

    // Fare Configuration (admin-managed pricing)
    fun getFareConfigFlow(): Flow<FareConfig> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val config = snapshot.getValue(FareConfig::class.java) ?: FareConfig()
                trySend(config)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        val ref = database.getReference("config").child("fare")
        ref.addValueEventListener(listener)

        awaitClose { ref.removeEventListener(listener) }
    }

    suspend fun updateFareConfig(config: FareConfig) {
        database.getReference("config").child("fare").setValue(config).await()
    }

    // Fare Stops (the posted per-destination rate table)
    fun getFareStopsFlow(): Flow<List<FareStop>> =
        // 240 rows through reflection is the largest single deserialisation the
        // passenger's app does, and it happens while they are choosing a stop.
        children(database.getReference("config").child("fareStops"), FareStop::class.java)

    suspend fun saveFareStop(stop: FareStop) {
        database.getReference("config").child("fareStops").child(stop.id).setValue(stop).await()
    }

    suspend fun deleteFareStop(stopId: String) {
        database.getReference("config").child("fareStops").child(stopId).removeValue().await()
    }

    /**
     * Writes the whole rate table in one request. Existing stops with the same
     * id are overwritten; ones the admin added by hand are left alone.
     */
    suspend fun importFareStops(stops: List<FareStop>) {
        val updates = stops.associate { it.id to it as Any? }
        database.getReference("config").child("fareStops").updateChildren(updates).await()
    }

    // Driver documents
    //
    // The licence photograph lives in its own top-level node, away from the
    // driver record, so that listing drivers does not pull every image with it.
    // The `hasLicenceImage` flag on the driver is written alongside, because
    // that is what the admin list needs in order to say whether there is
    // anything to look at.

    /**
     * Records the licence number and expiry a driver typed at registration.
     *
     * These live beside the photograph rather than on the driver record: a
     * licence number is a government-issued identifier, and `drivers/{uid}` is
     * readable by every signed-in account because passengers need the
     * availability and position held there.
     */
    suspend fun saveLicenceDetails(driverId: String, number: String, expiry: String) {
        database.getReference("driverDocuments").child(driverId).child("licence")
            .updateChildren(mapOf("licenceNumber" to number, "licenceExpiry" to expiry)).await()
    }

    /**
     * Stores the photograph, leaving the number and expiry as they are.
     *
     * updateChildren rather than setValue, or sending a photograph would erase
     * the details the driver typed when they registered.
     */
    suspend fun saveLicenceImage(driverId: String, image: String, uploadedAt: String, consentedAt: String) {
        database.getReference("driverDocuments").child(driverId).child("licence")
            .updateChildren(
                mapOf(
                    "image" to image,
                    "uploadedAt" to uploadedAt,
                    "consentedAt" to consentedAt
                )
            ).await()
        database.getReference("drivers").child(driverId).child("hasLicenceImage")
            .setValue(true).await()
    }

    suspend fun getLicenceImage(driverId: String): DriverDocument? =
        database.getReference("driverDocuments").child(driverId).child("licence")
            .get().await().getValue(DriverDocument::class.java)

    /**
     * Removes a driver's licence photograph.
     *
     * Called when an application is refused and when an account is deleted. The
     * flag is cleared in the same breath, or the admin list would keep offering
     * a document that is no longer there.
     */
    /**
     * Destroys the photograph and nothing else.
     *
     * The number and expiry stay: they are what the driver typed and what an
     * administrator compared the photograph against, and a refusal that erased
     * them would leave no record of what was refused. Removing the whole node
     * here would also take them, which is why the children are named.
     */
    suspend fun deleteLicenceImage(driverId: String) {
        database.getReference("driverDocuments").child(driverId).child("licence")
            .updateChildren(
                mapOf<String, Any?>(
                    "image" to null,
                    "uploadedAt" to null,
                    "consentedAt" to null
                )
            ).await()
        database.getReference("drivers").child(driverId).child("hasLicenceImage")
            .setValue(false).await()
    }
}
```

#### `app/src/main/java/com/tpc/trikride/services/TrikRideMessagingService.kt`

```kotlin
package com.tpc.trikride.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tpc.trikride.MainActivity
import com.tpc.trikride.R

class TrikRideMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val title = remoteMessage.notification?.title ?: "TrikRide"
        val body = remoteMessage.notification?.body ?: ""
        val data = remoteMessage.data

        sendNotification(title, body, data)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Send token to server for user notifications
    }

    private fun sendNotification(
        title: String,
        message: String,
        data: Map<String, String>
    ) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel for Android 8.0+
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "TrikRide Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for ride requests and updates"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Create intent for when notification is clicked
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("rideId", data["rideId"])
            putExtra("notificationType", data["type"])
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    companion object {
        private const val CHANNEL_ID = "trikride_notifications"
    }
}
```

[[PB]]

### I.4 Repositories

#### `app/src/main/java/com/tpc/trikride/repositories/AdminRepository.kt`

```kotlin
package com.tpc.trikride.repositories

import com.tpc.trikride.models.Driver
import com.tpc.trikride.models.DriverDocument
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.User
import com.tpc.trikride.models.VerificationStatus
import com.tpc.trikride.services.FirebaseService
import kotlinx.coroutines.flow.Flow

class AdminRepository(
    private val firebase: FirebaseService = FirebaseService()
) {
    fun drivers(): Flow<List<Driver>> = firebase.getAllDriversFlow()
    fun users(): Flow<List<User>> = firebase.getAllUsersFlow()
    fun rides(): Flow<List<Ride>> = firebase.getAllRidesFlow()

    suspend fun approveDriver(driverId: String) =
        firebase.updateDriverVerification(driverId, VerificationStatus.APPROVED)

    /**
     * Refuses an application that was never approved, and destroys the licence
     * photograph with it.
     *
     * Keeping a refused applicant's identity document serves no purpose the
     * system has, so it goes at the moment the decision is made rather than in
     * some later clear-out.
     */
    suspend fun rejectDriver(driverId: String) {
        firebase.updateDriverVerification(driverId, VerificationStatus.REJECTED)
        firebase.deleteLicenceImage(driverId)
    }

    /**
     * Withdraws approval from a driver who had it, keeping the photograph.
     *
     * Deliberately not the same operation as refusing an application. Approval
     * is usually withdrawn either because a licence has lapsed or because a
     * concern is being looked into, and destroying the document in either case
     * would remove the thing the decision may later have to be justified
     * against. It goes when the account does.
     */
    suspend fun revokeApproval(driverId: String) =
        firebase.updateDriverVerification(driverId, VerificationStatus.REJECTED)

    /** Fetched only when an administrator opens a specific driver to review. */
    suspend fun licenceDocument(driverId: String): DriverDocument? =
        firebase.getLicenceImage(driverId)
}
```

#### `app/src/main/java/com/tpc/trikride/repositories/AuthRepository.kt`

```kotlin
package com.tpc.trikride.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.tpc.trikride.models.User
import com.tpc.trikride.models.UserType
import com.tpc.trikride.utils.Constants
import com.tpc.trikride.utils.ProfilePhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Real authentication backed by Firebase Auth (email/password). On
 * registration the user's profile — including their account type — is
 * persisted to the Realtime Database so subsequent logins route straight
 * to the correct dashboard.
 */
class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
) {
    val currentUserId: String? get() = auth.currentUser?.uid

    suspend fun login(email: String, password: String): String {
        val result = auth.signInWithEmailAndPassword(email.trim(), password).await()
        return result.user?.uid ?: error("Login failed")
    }

    suspend fun register(
        fullName: String,
        birthDate: String,
        email: String,
        phone: String,
        password: String,
        userType: UserType
    ): String {
        val result = auth.createUserWithEmailAndPassword(email.trim(), password).await()
        val created = result.user ?: error("Registration failed")
        val uid = created.uid
        val user = User(
            id = uid,
            email = email.trim(),
            phoneNumber = phone,
            firstName = fullName,
            birthDate = birthDate,
            userType = userType,
            createdAt = System.currentTimeMillis().toString(),
            // The sign-up form requires the terms, privacy notice and community
            // guidelines to be ticked, so that consent is recorded here. Drivers
            // are asked for the Driver Agreement separately once their account
            // type is known.
            acceptedLegalVersion = Constants.LEGAL_VERSION,
            acceptedLegalAt = System.currentTimeMillis().toString()
        )
        try {
            database.getReference("users").child(uid).setValue(user).await()
        } catch (e: Exception) {
            // The account exists in Auth but has no profile, and the two writes
            // are not atomic. Left alone, the user cannot register again — the
            // address is taken — and signing in gives them an account with no
            // name, no telephone number and no recorded consent, which no
            // driver can then ring. Undo the half that succeeded.
            runCatching { created.delete().await() }
            throw e
        }
        return uid
    }

    /** Loads the full profile record for a user. */
    suspend fun loadUser(uid: String): User? {
        val snapshot = database.getReference("users").child(uid).get().await()
        return snapshot.getValue(User::class.java)
    }

    /**
     * Shrinks the chosen image and stores it in the database as base64.
     *
     * Cloud Storage would be the usual home for this, but new Firebase projects
     * require a paid plan to provision a bucket and this one runs on the free
     * tier. Photos live under their own node so that reading a list of users
     * does not pull every avatar with it. Returns the encoded photo.
     */
    suspend fun saveProfilePhoto(uid: String, image: android.graphics.Bitmap): String {
        val encoded = withContext(Dispatchers.IO) { ProfilePhoto.encodeBitmap(image) }
            ?: error("That image could not be processed. Try a different photo.")
        database.getReference("profilePhotos").child(uid).setValue(
            mapOf(
                "data" to encoded,
                "updatedAt" to System.currentTimeMillis().toString()
            )
        ).await()
        return encoded
    }

    /**
     * What the account has already agreed to. Returned as the accepted legal
     * version and the accepted driver-agreement version, either of which is
     * blank when that consent has not been given.
     */
    suspend fun loadConsent(uid: String): Pair<String, String> {
        val ref = database.getReference("users").child(uid)
        val legal = ref.child("acceptedLegalVersion").get().await()
            .getValue(String::class.java).orEmpty()
        val driver = ref.child("acceptedDriverAgreementVersion").get().await()
            .getValue(String::class.java).orEmpty()
        return legal to driver
    }

    /** Records acceptance of the current documents against the account. */
    suspend fun recordConsent(uid: String, version: String, includeDriverAgreement: Boolean) {
        val updates = mutableMapOf<String, Any?>(
            "acceptedLegalVersion" to version,
            "acceptedLegalAt" to System.currentTimeMillis().toString()
        )
        if (includeDriverAgreement) {
            updates["acceptedDriverAgreementVersion"] = version
        }
        database.getReference("users").child(uid).updateChildren(updates).await()
    }

    /** The stored photo for a user, or an empty string when there is none. */
    suspend fun loadProfilePhoto(uid: String): String {
        val snapshot = database.getReference("profilePhotos").child(uid).child("data").get().await()
        return snapshot.getValue(String::class.java).orEmpty()
    }

    suspend fun updateProfile(uid: String, fullName: String, phone: String) {
        val updates = mapOf<String, Any?>(
            "firstName" to fullName,
            "phoneNumber" to phone,
            "updatedAt" to System.currentTimeMillis().toString()
        )
        database.getReference("users").child(uid).updateChildren(updates).await()
    }

    /**
     * Sends a password-reset email to the signed-in user's address, and returns
     * the address it went to so the caller can show it.
     *
     * Worth showing: Firebase reports success once it has accepted the request,
     * not once anything is delivered, so the address on the account is the only
     * part of the outcome the app can actually vouch for.
     */
    suspend fun sendPasswordReset(): String {
        val email = auth.currentUser?.email ?: error("No email on this account")
        auth.sendPasswordResetEmail(email).await()
        return email
    }

    /**
     * Sends a password-reset email to any address, for someone who cannot sign
     * in to ask for one.
     */
    suspend fun sendPasswordReset(email: String) {
        auth.sendPasswordResetEmail(email.trim()).await()
    }

    /** Returns the stored account type for a user, or null if not set. */
    suspend fun loadUserType(uid: String): UserType? {
        val snapshot = database.getReference("users").child(uid).child("userType").get().await()
        val raw = snapshot.getValue(String::class.java) ?: return null
        return runCatching { UserType.valueOf(raw) }.getOrNull()
    }

    suspend fun setUserType(uid: String, userType: UserType) {
        database.getReference("users").child(uid).child("userType").setValue(userType.name).await()
    }

    fun signOut() = auth.signOut()

    /** Bridges a Google Play Services [Task] into a coroutine. */
    private suspend fun <T> Task<T>.await(): T = suspendCancellableCoroutine { cont ->
        addOnSuccessListener { cont.resume(it) }
        addOnFailureListener { cont.resumeWithException(it) }
    }
}
```

#### `app/src/main/java/com/tpc/trikride/repositories/DriverRepository.kt`

```kotlin
package com.tpc.trikride.repositories

import android.content.Context
import android.net.Uri
import com.tpc.trikride.models.Driver
import com.tpc.trikride.models.DriverDocument
import com.tpc.trikride.models.Location
import com.tpc.trikride.models.VerificationStatus
import com.tpc.trikride.services.FirebaseService
import com.tpc.trikride.utils.LicenceImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DriverRepository(
    private val firebase: FirebaseService = FirebaseService()
) {

    suspend fun registerDriver(
        userId: String,
        licenseNumber: String,
        licenseExpiry: String,
        tricycleNumber: String
    ): Driver {
        val driver = Driver(
            userId = userId,
            tricycleNumber = tricycleNumber,
            verificationStatus = VerificationStatus.PENDING,
            isAvailable = false
        )
        firebase.registerDriver(userId, driver)
        // The licence details go to the access-controlled node, not onto the
        // driver record that every signed-in account can read.
        firebase.saveLicenceDetails(userId, licenseNumber, licenseExpiry)
        return driver
    }

    fun driverProfile(driverId: String): Flow<Driver?> = firebase.getDriverFlow(driverId)

    suspend fun setAvailability(driverId: String, isAvailable: Boolean) {
        firebase.updateDriverAvailability(driverId, isAvailable)
    }

    /** The assigned driver's live position, for the passenger's tracking map. */
    fun driverLocation(driverId: String): Flow<Location?> =
        firebase.getDriverFlow(driverId).map { it?.currentLocation }

    suspend fun updateLocation(driverId: String, location: Location) {
        firebase.updateDriverLocation(driverId, location)
    }

    /** Counts a finished ride against the driver's totals. */
    suspend fun recordCompletedRide(driverId: String) = firebase.recordCompletedRide(driverId)

    /** Every star rating this driver has been given. */
    fun ratings(driverId: String): Flow<List<Int>> = firebase.getRatingsFlow(driverId)

    /** Caches the average onto the driver's own record for others to read. */
    suspend fun publishRating(driverId: String, average: Double, count: Int) =
        firebase.publishRating(driverId, average, count)

    /**
     * Stores the driver's licence photograph.
     *
     * Encoding happens off the main thread: a camera photograph is several
     * megapixels, and decoding and compressing it is not something to do on the
     * frame the button was pressed on. [consentedAt] is passed in rather than
     * stamped here, because it belongs to the moment the driver agreed, not the
     * moment the write happened.
     */
    suspend fun saveLicenceImage(context: Context, driverId: String, imageUri: Uri, consentedAt: String) {
        val encoded = withContext(Dispatchers.IO) { LicenceImage.encode(context, imageUri) }
            ?: throw IllegalStateException("That image could not be prepared. Try another photo.")
        firebase.saveLicenceImage(
            driverId = driverId,
            image = encoded,
            uploadedAt = System.currentTimeMillis().toString(),
            consentedAt = consentedAt
        )
    }

    /** The driver's licence: number, expiry and photograph. */
    suspend fun licenceDocument(driverId: String): DriverDocument? =
        firebase.getLicenceImage(driverId)

    /** Lets a driver withdraw the photograph they sent. */
    suspend fun deleteLicenceImage(driverId: String) {
        firebase.deleteLicenceImage(driverId)
    }
}
```

#### `app/src/main/java/com/tpc/trikride/repositories/FareRepository.kt`

```kotlin
package com.tpc.trikride.repositories

import com.tpc.trikride.models.FareConfig
import com.tpc.trikride.models.FareStop
import com.tpc.trikride.services.FirebaseService
import com.tpc.trikride.utils.FareSeed
import kotlinx.coroutines.flow.Flow

class FareRepository(
    private val firebase: FirebaseService = FirebaseService()
) {
    fun fareConfig(): Flow<FareConfig> = firebase.getFareConfigFlow()

    suspend fun save(config: FareConfig) = firebase.updateFareConfig(config)

    fun fareStops(): Flow<List<FareStop>> = firebase.getFareStopsFlow()

    suspend fun saveStop(stop: FareStop) = firebase.saveFareStop(stop)

    suspend fun deleteStop(stopId: String) = firebase.deleteFareStop(stopId)

    /**
     * Loads the transcribed FeTODAT table into the database and stamps the
     * config so the admin screen can show when it last happened.
     */
    suspend fun importOfficialRates(current: FareConfig) {
        firebase.importFareStops(FareSeed.STOPS)
        firebase.updateFareConfig(
            current.copy(
                source = FareConfig.DEFAULT_SOURCE,
                seededAt = System.currentTimeMillis().toString()
            )
        )
    }
}
```

#### `app/src/main/java/com/tpc/trikride/repositories/RideRepository.kt`

```kotlin
package com.tpc.trikride.repositories

import com.tpc.trikride.models.FareType
import com.tpc.trikride.models.Location
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.RideRequest
import com.tpc.trikride.models.RideStatus
import com.tpc.trikride.services.FirebaseService
import com.tpc.trikride.utils.Constants
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Coordinates the ride lifecycle between passengers and drivers:
 *
 *   Passenger requests a ride  →  request appears to available drivers
 *   Driver accepts             →  request becomes a Ride (ACCEPTED)
 *   Driver progresses status   →  ARRIVING → ARRIVED → IN_PROGRESS → COMPLETED
 */
class RideRepository(
    private val firebase: FirebaseService = FirebaseService()
) {

    // ---- Passenger side ----

    suspend fun requestRide(
        passengerId: String,
        pickup: Location,
        dropoff: Location,
        passengerCount: Int = 1,
        regularCount: Int = 0,
        discountedCount: Int = 0,
        luggage: String = "None",
        estimatedFare: Double = 0.0,
        fareStopId: String = "",
        fareType: FareType = FareType.REGULAR,
        notes: String = ""
    ): RideRequest {
        val now = System.currentTimeMillis()
        val request = RideRequest(
            id = UUID.randomUUID().toString(),
            passengerId = passengerId,
            pickupLocation = pickup,
            dropoffLocation = dropoff,
            requestedAt = now.toString(),
            expiresAt = (now + Constants.RIDE_REQUEST_TTL_MS).toString(),
            passengerCount = passengerCount,
            regularCount = regularCount,
            discountedCount = discountedCount,
            luggage = luggage,
            estimatedFare = estimatedFare,
            fareStopId = fareStopId,
            fareType = fareType,
            notes = notes
        )
        firebase.createRideRequest(request)
        return request
    }

    suspend fun cancelRequest(requestId: String) {
        firebase.removeRideRequest(requestId)
    }

    /**
     * This passenger's own request, if one is still open and unexpired.
     *
     * Read on bind so that reopening the app finds a search already running
     * rather than offering to start a second one.
     */
    suspend fun myOpenRequest(passengerId: String): RideRequest? =
        firebase.findOpenRequestFor(passengerId)

    fun passengerActiveRides(passengerId: String): Flow<List<Ride>> =
        firebase.getActiveRidesFlow(passengerId)

    fun passengerRideHistory(passengerId: String): Flow<List<Ride>> =
        firebase.getPassengerRideHistoryFlow(passengerId)

    fun driverRideHistory(driverId: String): Flow<List<Ride>> =
        firebase.getDriverRideHistoryFlow(driverId)

    // ---- Driver side ----

    fun openRideRequests(): Flow<List<RideRequest>> = firebase.getOpenRideRequestsFlow()

    fun driverActiveRides(driverId: String): Flow<List<Ride>> =
        firebase.getDriverActiveRidesFlow(driverId)

    /**
     * Driver accepts a request: converts it into a Ride and removes the open request
     * so other drivers no longer see it.
     */
    /**
     * Driver accepts a request: claims it, converts it into a Ride, and removes
     * the open request so other drivers no longer see it.
     *
     * Returns null when another driver got there first. The claim is a
     * transaction on the request node, so exactly one caller proceeds however
     * many tap Accept in the same second.
     */
    suspend fun acceptRequest(
        driverId: String,
        request: RideRequest,
        driverName: String = "",
        driverPhone: String = ""
    ): Ride? {
        if (!firebase.claimRideRequest(request.id, driverId)) return null
        // Destinations come from the posted fare table, which carries no
        // coordinates, so the ride keeps the price the passenger already agreed
        // to rather than recomputing anything from a distance.
        val ride = Ride(
            id = UUID.randomUUID().toString(),
            passengerId = request.passengerId,
            driverId = driverId,
            pickupLocation = request.pickupLocation,
            dropoffLocation = request.dropoffLocation,
            status = RideStatus.ACCEPTED,
            requestedAt = request.requestedAt,
            acceptedAt = System.currentTimeMillis().toString(),
            estimatedFare = request.estimatedFare,
            passengerCount = request.passengerCount,
            regularCount = request.regularCount,
            discountedCount = request.discountedCount,
            luggage = request.luggage,
            fareStopId = request.fareStopId,
            fareType = request.fareType,
            notes = request.notes,
            driverName = driverName,
            driverPhone = driverPhone
        )
        firebase.createRide(ride)
        firebase.removeRideRequest(request.id)
        return ride
    }

    /** Clears requests whose five minutes are up. */
    suspend fun purgeExpiredRequests() = firebase.purgeExpiredRideRequests()

    /**
     * Puts the passenger's own name and number on the ride so the driver can
     * reach them. Written by the passenger rather than copied from the request,
     * so that an open request broadcast to every approved driver never carries
     * a telephone number.
     */
    suspend fun attachPassengerContact(rideId: String, name: String, phone: String) =
        firebase.attachPassengerContact(rideId, name, phone)

    suspend fun updateRideStatus(rideId: String, status: RideStatus) {
        firebase.updateRideStatus(rideId, status)
    }

    /**
     * Records a passenger's rating of the driver who carried them.
     *
     * Written under the rater's own key, which is what lets the security rules
     * restrict it to them. The driver's visible average is not touched here —
     * the driver record is theirs to write, not a passenger's.
     */
    suspend fun rateRide(ride: Ride, stars: Int) {
        if (ride.driverId.isBlank() || ride.passengerId.isBlank()) return
        firebase.submitRating(ride.driverId, ride.id, ride.passengerId, stars.coerceIn(1, 5))
    }

    /** The natural next status in the ride lifecycle, or null if the ride is finished. */
    fun nextStatus(current: RideStatus): RideStatus? = when (current) {
        RideStatus.ACCEPTED -> RideStatus.DRIVER_ARRIVING
        RideStatus.DRIVER_ARRIVING -> RideStatus.DRIVER_ARRIVED
        RideStatus.DRIVER_ARRIVED -> RideStatus.IN_PROGRESS
        RideStatus.IN_PROGRESS -> RideStatus.COMPLETED
        else -> null
    }

    /**
     * Ends a ride that is not going to finish normally.
     *
     * The lifecycle only ever moved forwards, so a passenger who did not turn
     * up or a tricycle that broke down left a ride nobody could close: it stayed
     * in both parties' active lists for good, the driver could not reach their
     * online switch, and the passenger could not book again. CANCELLED and
     * NO_SHOW were in the enum, filtered on in six places and written by
     * nothing.
     */
    suspend fun cancelRide(rideId: String) =
        firebase.updateRideStatus(rideId, RideStatus.CANCELLED)

    /** The passenger never arrived. Only meaningful once the driver is there. */
    suspend fun markNoShow(rideId: String) =
        firebase.updateRideStatus(rideId, RideStatus.NO_SHOW)

    /** What the driver says was actually collected, which is cash and can differ. */
    suspend fun recordActualFare(rideId: String, amount: Double) =
        firebase.recordActualFare(rideId, amount)

    /**
     * Whether this ride can still be called off, and by whom.
     *
     * A passenger may withdraw until the ride is under way; after that they are
     * in the tricycle and it is between them and the driver. A driver may stop
     * at any point up to completion, because a breakdown does not wait for a
     * convenient status.
     */
    fun passengerMayCancel(status: RideStatus): Boolean = status in setOf(
        RideStatus.REQUESTED, RideStatus.SEARCHING, RideStatus.ACCEPTED,
        RideStatus.DRIVER_ARRIVING, RideStatus.DRIVER_ARRIVED
    )

    fun driverMayCancel(status: RideStatus): Boolean =
        status !in setOf(RideStatus.COMPLETED, RideStatus.CANCELLED, RideStatus.NO_SHOW)

    fun driverMayMarkNoShow(status: RideStatus): Boolean =
        status == RideStatus.DRIVER_ARRIVED
}
```

#### `app/src/main/java/com/tpc/trikride/repositories/SupportRepository.kt`

```kotlin
package com.tpc.trikride.repositories

import com.tpc.trikride.models.AppNotification
import com.tpc.trikride.models.Complaint
import com.tpc.trikride.models.ComplaintStatus
import com.tpc.trikride.models.NotificationType
import com.tpc.trikride.models.UserType
import com.tpc.trikride.services.FirebaseService
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/** Complaints and in-app notifications. */
class SupportRepository(
    private val firebase: FirebaseService = FirebaseService()
) {

    suspend fun submitComplaint(
        reporterId: String,
        reporterName: String,
        reporterType: UserType,
        category: String,
        description: String
    ): Complaint {
        val complaint = Complaint(
            id = UUID.randomUUID().toString(),
            reporterId = reporterId,
            reporterName = reporterName,
            reporterType = reporterType,
            category = category,
            description = description,
            status = ComplaintStatus.OPEN,
            createdAt = System.currentTimeMillis().toString()
        )
        firebase.submitComplaint(complaint)
        notify(
            userId = reporterId,
            title = "Concern submitted",
            message = "We received your report about \"$category\" and an administrator will review it.",
            type = NotificationType.COMPLAINT
        )
        return complaint
    }

    fun allComplaints(): Flow<List<Complaint>> = firebase.getAllComplaintsFlow()

    fun myComplaints(userId: String): Flow<List<Complaint>> =
        firebase.getUserComplaintsFlow(userId)

    suspend fun updateComplaint(complaint: Complaint, status: ComplaintStatus, note: String) {
        firebase.updateComplaintStatus(complaint.id, status, note)
        val label = when (status) {
            ComplaintStatus.OPEN -> "reopened"
            ComplaintStatus.IN_REVIEW -> "under review"
            ComplaintStatus.RESOLVED -> "resolved"
        }
        notify(
            userId = complaint.reporterId,
            title = "Your concern is $label",
            message = note.ifBlank { "An administrator updated your report about \"${complaint.category}\"." },
            type = NotificationType.COMPLAINT
        )
    }

    // Notifications

    fun notifications(userId: String): Flow<List<AppNotification>> =
        firebase.getNotificationsFlow(userId)

    suspend fun notify(
        userId: String,
        title: String,
        message: String,
        type: NotificationType = NotificationType.GENERAL
    ) {
        if (userId.isBlank()) return
        firebase.pushNotification(
            AppNotification(
                id = UUID.randomUUID().toString(),
                userId = userId,
                title = title,
                message = message,
                type = type,
                createdAt = System.currentTimeMillis().toString()
            )
        )
    }

    suspend fun markRead(userId: String, id: String) = firebase.markNotificationRead(userId, id)

    suspend fun markAllRead(userId: String, ids: List<String>) =
        firebase.markAllNotificationsRead(userId, ids)
}
```

[[PB]]

### I.5 ViewModels

#### `app/src/main/java/com/tpc/trikride/viewmodels/AdminViewModel.kt`

```kotlin
package com.tpc.trikride.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tpc.trikride.models.Driver
import com.tpc.trikride.models.DriverDocument
import com.tpc.trikride.models.FareConfig
import com.tpc.trikride.models.FareStop
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.User
import com.tpc.trikride.models.Complaint
import com.tpc.trikride.models.ComplaintStatus
import com.tpc.trikride.repositories.AdminRepository
import com.tpc.trikride.repositories.FareRepository
import com.tpc.trikride.models.NotificationType
import com.tpc.trikride.repositories.SupportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdminViewModel(
    private val repo: AdminRepository = AdminRepository(),
    private val fareRepo: FareRepository = FareRepository(),
    private val supportRepo: SupportRepository = SupportRepository()
) : ViewModel() {

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _fareSaved = MutableStateFlow(false)
    val fareSaved: StateFlow<Boolean> = _fareSaved

    private val _importing = MutableStateFlow(false)
    val importing: StateFlow<Boolean> = _importing

    val fareConfig: StateFlow<FareConfig> = fareRepo.fareConfig()
        .catch { _error.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), FareConfig())

    val fareStops: StateFlow<List<FareStop>> = fareRepo.fareStops()
        .catch { _error.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val drivers: StateFlow<List<Driver>> = repo.drivers()
        .catch { _error.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val users: StateFlow<List<User>> = repo.users()
        .catch { _error.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val rides: StateFlow<List<Ride>> = repo.rides()
        .catch { _error.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val complaints: StateFlow<List<Complaint>> = supportRepo.allComplaints()
        .catch { _error.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun updateComplaint(complaint: Complaint, status: ComplaintStatus, note: String) {
        viewModelScope.launch {
            try {
                supportRepo.updateComplaint(complaint, status, note)
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to update the report"
            }
        }
    }

    /**
     * Licences the administrator has opened, keyed by driver.
     *
     * Fetched one at a time when a card is expanded rather than loaded with the
     * driver list. There is no reason to pull a dozen identity documents across
     * the network so that one of them can be looked at, and every one not
     * fetched is one not sitting in memory. The number and expiry come with the
     * photograph because they live on the same protected node.
     */
    private val _licences = MutableStateFlow<Map<String, DriverDocument?>>(emptyMap())
    val licences: StateFlow<Map<String, DriverDocument?>> = _licences

    fun openLicence(driverId: String) {
        if (_licences.value.containsKey(driverId)) return
        viewModelScope.launch {
            // The key going in ahead of the value is what stops a second tap
            // from starting a second fetch.
            _licences.value = _licences.value + (driverId to null)
            val doc = runCatching { repo.licenceDocument(driverId) }.getOrNull()
            _licences.value = _licences.value + (driverId to doc)
        }
    }

    fun closeLicence(driverId: String) {
        _licences.value = _licences.value - driverId
    }

    fun approveDriver(driverId: String) {
        viewModelScope.launch {
            try {
                repo.approveDriver(driverId)
                notifyDecision(
                    driverId,
                    "Your application was approved",
                    "You can now go online and accept passengers."
                )
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to approve driver"
            }
        }
    }

    fun rejectDriver(driverId: String) {
        viewModelScope.launch {
            try {
                repo.rejectDriver(driverId)
                notifyDecision(
                    driverId,
                    "Your application was not approved",
                    "The licence photograph you submitted has been deleted. Speak to " +
                        "the administrator before applying again."
                )
                // The image is gone from the database; drop the copy held here
                // too rather than leaving a deleted document on screen.
                closeLicence(driverId)
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to reject driver"
            }
        }
    }

    /** Withdraws approval without destroying the licence photograph. */
    fun revokeApproval(driverId: String) {
        viewModelScope.launch {
            try {
                repo.revokeApproval(driverId)
                notifyDecision(
                    driverId,
                    "Your approval has been withdrawn",
                    "You cannot accept passengers for now. Speak to the administrator."
                )
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to revoke approval"
            }
        }
    }

    /**
     * Tells the driver what was decided.
     *
     * A failure here is deliberately not surfaced as an error: the decision it
     * describes has already been written and taken effect, and reporting the
     * notification as a failed approval would be worse than a missing message.
     */
    private suspend fun notifyDecision(driverId: String, title: String, message: String) {
        runCatching {
            supportRepo.notify(
                userId = driverId,
                title = title,
                message = message,
                type = NotificationType.ACCOUNT
            )
        }
    }

    fun saveFareConfig(config: FareConfig) {
        viewModelScope.launch {
            try {
                fareRepo.save(config)
                _fareSaved.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to save fares"
            }
        }
    }

    fun saveFareStop(stop: FareStop) {
        viewModelScope.launch {
            try {
                fareRepo.saveStop(stop)
                _fareSaved.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to save the stop"
            }
        }
    }

    fun deleteFareStop(stopId: String) {
        viewModelScope.launch {
            try {
                fareRepo.deleteStop(stopId)
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to remove the stop"
            }
        }
    }

    /** Writes the transcribed FeTODAT table into the database in one go. */
    fun importOfficialRates() {
        if (_importing.value) return
        viewModelScope.launch {
            _importing.value = true
            try {
                fareRepo.importOfficialRates(fareConfig.value)
                _fareSaved.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to import the fare table"
            } finally {
                _importing.value = false
            }
        }
    }

    fun acknowledgeFareSaved() {
        _fareSaved.value = false
    }

    fun dismissError() {
        _error.value = null
    }
}
```

#### `app/src/main/java/com/tpc/trikride/viewmodels/AuthViewModel.kt`

```kotlin
package com.tpc.trikride.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tpc.trikride.models.UserType
import com.tpc.trikride.repositories.AuthRepository
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class AuthViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    data class AuthUiState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val userId: String? = null,
        val userType: UserType? = null,
        // Authenticated but no stored account type yet (needs the picker).
        val needsAccountType: Boolean = false,
        // True while we check for an existing signed-in session on launch.
        val isBootstrapping: Boolean = true,
        // Known synchronously at startup: is someone already signed in?
        val hasExistingSession: Boolean = false,
        // Set after a password-reset email is requested from the sign-in screen.
        val resetNotice: String? = null
    )

    private val _state = MutableStateFlow(
        AuthUiState(hasExistingSession = repo.currentUserId != null)
    )
    val state: StateFlow<AuthUiState> = _state

    init {
        bootstrap()
    }

    /**
     * Firebase keeps the user signed in across app restarts, so on launch we
     * check for an existing session and, if present, restore it straight to
     * the dashboard — no re-login required.
     */
    private fun bootstrap() {
        viewModelScope.launch {
            val uid = repo.currentUserId
            if (uid == null) {
                // Nothing to restore. Go straight to onboarding or sign-in
                // rather than hold a loading screen for its own sake.
                _state.update { it.copy(isBootstrapping = false) }
                return@launch
            }
            try {
                val type = withTimeout(DB_TIMEOUT_MS) { repo.loadUserType(uid) }
                _state.value = AuthUiState(
                    isBootstrapping = false,
                    userId = uid,
                    userType = type,
                    needsAccountType = type == null
                )
            } catch (e: Exception) {
                // Session exists but we couldn't confirm the type; let them
                // re-pick (or see the DB error) rather than getting stuck.
                _state.value = AuthUiState(
                    isBootstrapping = false,
                    userId = uid,
                    needsAccountType = true
                )
            }
        }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val uid = withTimeout(DB_TIMEOUT_MS) { repo.login(email, password) }
                val type = withTimeout(DB_TIMEOUT_MS) { repo.loadUserType(uid) }
                _state.value = AuthUiState(
                    isBootstrapping = false,
                    userId = uid,
                    userType = type,
                    needsAccountType = type == null
                )
            } catch (e: TimeoutCancellationException) {
                _state.update { it.copy(isLoading = false, error = DB_UNREACHABLE) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = friendly(e)) }
            }
        }
    }

    fun register(
        fullName: String,
        birthDate: String,
        email: String,
        phone: String,
        password: String,
        userType: UserType
    ) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val uid = withTimeout(DB_TIMEOUT_MS) {
                    repo.register(fullName, birthDate, email, phone, password, userType)
                }
                _state.value = AuthUiState(
                    isBootstrapping = false, userId = uid, userType = userType
                )
            } catch (e: TimeoutCancellationException) {
                _state.update { it.copy(isLoading = false, error = DB_UNREACHABLE) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = friendly(e)) }
            }
        }
    }

    /** Used when an authenticated account has no stored type yet. */
    fun chooseAccountType(userType: UserType) {
        val uid = _state.value.userId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                withTimeout(DB_TIMEOUT_MS) { repo.setUserType(uid, userType) }
                _state.update {
                    it.copy(isLoading = false, userType = userType, needsAccountType = false)
                }
            } catch (e: TimeoutCancellationException) {
                _state.update { it.copy(isLoading = false, error = DB_UNREACHABLE) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = friendly(e)) }
            }
        }
    }

    /**
     * Asks Firebase to email a reset link.
     *
     * The same message comes back whether or not the address has an account.
     * Firebase reports "no user record" for an unknown one, and repeating that
     * turns the sign-in screen into a way of finding out who is registered.
     */
    fun sendPasswordReset(email: String) {
        if (email.isBlank()) return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, resetNotice = null) }
            val notice = "If ${email.trim()} has an account, a reset link is on its way. " +
                "Check the spam folder if it does not arrive."
            try {
                withTimeout(DB_TIMEOUT_MS) { repo.sendPasswordReset(email) }
                _state.update { it.copy(isLoading = false, resetNotice = notice) }
            } catch (e: TimeoutCancellationException) {
                _state.update { it.copy(isLoading = false, error = DB_UNREACHABLE) }
            } catch (e: Exception) {
                val msg = e.message.orEmpty()
                if (msg.contains("no user record", ignoreCase = true)) {
                    _state.update { it.copy(isLoading = false, resetNotice = notice) }
                } else {
                    _state.update { it.copy(isLoading = false, error = friendly(e)) }
                }
            }
        }
    }

    fun signOut() {
        repo.signOut()
        _state.value = AuthUiState(isBootstrapping = false)
    }

    fun clearError() = _state.update { it.copy(error = null) }

    fun clearResetNotice() = _state.update { it.copy(resetNotice = null) }

    private companion object {
        const val DB_TIMEOUT_MS = 12_000L
        const val DB_UNREACHABLE =
            "Couldn't reach the database. Make sure the Realtime Database is created in " +
                "Firebase, then re-download google-services.json and replace it in the app/ " +
                "folder. (If you just did, check the database Rules allow writes.)"
    }

    private fun friendly(e: Exception): String {
        val msg = e.message ?: return "Something went wrong. Please try again."
        return when {
            msg.contains("password is invalid", ignoreCase = true) ||
                msg.contains("credential is incorrect", ignoreCase = true) ->
                "Incorrect email or password."
            // Deliberately the same answer as a wrong password. The reset flow
            // already refuses to say whether an address is registered, and
            // answering it here turned the sign-in form into a way of finding
            // out who has an account.
            msg.contains("no user record", ignoreCase = true) ->
                "Incorrect email or password."
            msg.contains("email address is already in use", ignoreCase = true) ->
                "That email is already registered."
            msg.contains("badly formatted", ignoreCase = true) ->
                "Please enter a valid email address."
            msg.contains("at least 6 characters", ignoreCase = true) ->
                "Password must be at least 6 characters."
            msg.contains("network error", ignoreCase = true) ->
                "Network error. Check your connection and try again."
            msg.contains("blocked all requests", ignoreCase = true) ||
                msg.contains("too many", ignoreCase = true) ->
                "Too many attempts from this device. Wait a few minutes and try again."
            // Anything unrecognised is shown as itself, not as raw SDK text.
            // "An internal error has occurred. [ CONFIGURATION_NOT_FOUND ]"
            // tells a user nothing and tells everyone else about the project's
            // configuration.
            else -> "Something went wrong. Please try again."
        }
    }
}
```

#### `app/src/main/java/com/tpc/trikride/viewmodels/ConsentViewModel.kt`

```kotlin
package com.tpc.trikride.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tpc.trikride.models.UserType
import com.tpc.trikride.repositories.AuthRepository
import com.tpc.trikride.utils.Constants.LEGAL_VERSION
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Decides whether a signed-in account still has to agree to the current legal
 * documents, and records the answer.
 *
 * This runs between authentication and the dashboard. An account created before
 * consent was tracked, or one that agreed to an older version of the documents,
 * is stopped here until it accepts. Drivers additionally have to accept the
 * Driver Agreement, which passengers never see.
 */
class ConsentViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    data class ConsentUiState(
        val isChecking: Boolean = true,
        /** The terms, privacy notice and community guidelines are outstanding. */
        val needsLegal: Boolean = false,
        /** The driver agreement is outstanding; only ever true for drivers. */
        val needsDriverAgreement: Boolean = false,
        val isSaving: Boolean = false,
        val error: String? = null,
        /**
         * The record could not be read, so we do not know what was accepted.
         * Distinct from "has not accepted": accepting writes to the node that
         * just failed to read, so offering only Accept put the user in a loop
         * whose one exit was signing out.
         */
        val unreadable: Boolean = false
    ) {
        val needsConsent: Boolean get() = needsLegal || needsDriverAgreement
    }

    private val _state = MutableStateFlow(ConsentUiState())
    val state: StateFlow<ConsentUiState> = _state

    private var checkedFor: Pair<String, UserType>? = null

    fun check(userId: String, userType: UserType) {
        val key = userId to userType
        if (checkedFor == key) return
        checkedFor = key
        run(userId, userType)
    }

    /** Re-runs a check that failed, without signing the user out. */
    fun retry(userId: String, userType: UserType) {
        checkedFor = userId to userType
        run(userId, userType)
    }

    private fun run(userId: String, userType: UserType) {

        viewModelScope.launch {
            _state.value = ConsentUiState(isChecking = true)
            try {
                val (legal, driverAgreement) = repo.loadConsent(userId)
                _state.value = ConsentUiState(
                    isChecking = false,
                    needsLegal = legal != LEGAL_VERSION,
                    needsDriverAgreement = userType == UserType.DRIVER &&
                        driverAgreement != LEGAL_VERSION
                )
            } catch (e: Exception) {
                // If the record cannot be read we ask again rather than let the
                // user through on an assumption about what they agreed to.
                // Fail closed on what was accepted, but say that this is a
                // failure to read rather than a failure to agree, so the screen
                // can offer to try again.
                _state.value = ConsentUiState(
                    isChecking = false,
                    needsLegal = true,
                    needsDriverAgreement = userType == UserType.DRIVER,
                    unreadable = true,
                    error = "Could not check what this account has agreed to. " +
                        "Check your connection and try again."
                )
            }
        }
    }

    fun accept(userId: String, userType: UserType) {
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, error = null) }
            try {
                repo.recordConsent(
                    uid = userId,
                    version = LEGAL_VERSION,
                    includeDriverAgreement = userType == UserType.DRIVER
                )
                _state.value = ConsentUiState(isChecking = false)
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isSaving = false,
                        error = e.message ?: "Could not record your agreement. Try again."
                    )
                }
            }
        }
    }

    /** Called on sign-out so the next account is checked from scratch. */
    fun reset() {
        checkedFor = null
        _state.value = ConsentUiState()
    }
}
```

#### `app/src/main/java/com/tpc/trikride/viewmodels/DriverViewModel.kt`

```kotlin
package com.tpc.trikride.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tpc.trikride.models.Driver
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.RideRequest
import com.tpc.trikride.models.NotificationType
import com.tpc.trikride.repositories.AuthRepository
import com.tpc.trikride.repositories.DriverRepository
import com.tpc.trikride.repositories.RideRepository
import com.tpc.trikride.repositories.SupportRepository
import com.tpc.trikride.utils.LocationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class DriverViewModel(
    private val driverRepository: DriverRepository = DriverRepository(),
    private val rideRepository: RideRepository = RideRepository(),
    private val supportRepository: SupportRepository = SupportRepository(),
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val driverId = MutableStateFlow<String?>(null)

    /** Publishes position while the driver is online; cancelled when offline. */
    private var locationJob: Job? = null

    /** The driver's own last known position, for centring their map. */
    private val _myLocation = MutableStateFlow<com.tpc.trikride.models.Location?>(null)
    val myLocation: StateFlow<com.tpc.trikride.models.Location?> = _myLocation

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    /** True while the initial profile load / registration is in flight. */
    private val _isRegistering = MutableStateFlow(false)
    val isRegistering: StateFlow<Boolean> = _isRegistering

    /** The driver's profile; null until they complete onboarding. */
    val driverProfile: StateFlow<Driver?> = driverId
        .filterNotNull()
        .flatMapLatest { driverRepository.driverProfile(it) }
        .catch { _errorMessage.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    /** Ride requests waiting for a driver, visible when online. */
    val openRequests: StateFlow<List<RideRequest>> = driverId
        .filterNotNull()
        .flatMapLatest { rideRepository.openRideRequests() }
        .catch { _errorMessage.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Rides this driver has accepted and not yet completed. */
    val activeRides: StateFlow<List<Ride>> = driverId
        .filterNotNull()
        .flatMapLatest { rideRepository.driverActiveRides(it) }
        .catch { _errorMessage.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Finished rides for this driver, newest first. */
    val rideHistory: StateFlow<List<Ride>> = driverId
        .filterNotNull()
        .flatMapLatest { rideRepository.driverRideHistory(it) }
        .catch { _errorMessage.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _loadingHistory = MutableStateFlow(true)
    val loadingHistory: StateFlow<Boolean> = _loadingHistory

    /** Total fare of completed rides, used for the earnings figure. */
    val earnings: StateFlow<Double> = rideHistory
        .map { rides ->
            rides.filter { it.status == com.tpc.trikride.models.RideStatus.COMPLETED }
                .sumOf { it.estimatedFare }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)

    /**
     * The driver's own ratings, and the average cached back onto their record.
     *
     * A passenger may write a rating but not the driver's record, so the fold
     * happens here, on the device that owns it. The consequence is that the
     * figure the admin screens read refreshes when the driver next opens the
     * app rather than the instant a passenger taps a star, which for a shift-
     * based app converges within the day.
     */
    val myRatings: StateFlow<List<Int>> = driverId
        .filterNotNull()
        .flatMapLatest { driverRepository.ratings(it) }
        .catch { _errorMessage.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val myRating: StateFlow<Double> = myRatings
        .map { stars -> if (stars.isEmpty()) 0.0 else stars.sum().toDouble() / stars.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)

    /** True while a licence photograph is being compressed and written. */
    private val _uploadingLicence = MutableStateFlow(false)
    val uploadingLicence: StateFlow<Boolean> = _uploadingLicence

    /** Confirmation shown after the licence photograph is sent or withdrawn. */
    private val _licenceMessage = MutableStateFlow<String?>(null)
    val licenceMessage: StateFlow<String?> = _licenceMessage

    /**
     * The driver's own licence — number, expiry and photograph.
     *
     * Fetched on demand rather than streamed. The photograph is a couple of
     * hundred kilobytes and the whole thing changes about once, so holding a
     * listener open on it for the life of the session would be paying a
     * subscription for a constant.
     */
    private val _licenceDoc = MutableStateFlow<com.tpc.trikride.models.DriverDocument?>(null)
    val licenceDoc: StateFlow<com.tpc.trikride.models.DriverDocument?> = _licenceDoc

    fun loadLicenceDocument() {
        val id = driverId.value ?: return
        if (_licenceDoc.value != null) return
        viewModelScope.launch {
            _licenceDoc.value = runCatching { driverRepository.licenceDocument(id) }.getOrNull()
        }
    }

    fun bind(userId: String) {
        driverId.value = userId
        viewModelScope.launch {
            kotlinx.coroutines.delay(600)
            _loadingHistory.value = false
        }
        viewModelScope.launch {
            // Only writes when the cached figure is actually behind, so this
            // does not put a write on the wire every time the screen resumes.
            myRatings.collect { stars ->
                if (stars.isEmpty()) return@collect
                val average = stars.sum().toDouble() / stars.size
                val current = driverProfile.value ?: return@collect
                if (current.ratingCount != stars.size || current.rating != average) {
                    runCatching { driverRepository.publishRating(userId, average, stars.size) }
                }
            }
        }
    }

    /**
     * Sends the licence photograph.
     *
     * [consentedAt] is stamped by the screen when the driver ticks the consent,
     * not here, so what is recorded is when they agreed rather than when the
     * write happened to complete.
     */
    fun submitLicenceImage(context: android.content.Context, imageUri: android.net.Uri, consentedAt: String) {
        val id = driverId.value ?: return
        viewModelScope.launch {
            _uploadingLicence.value = true
            _licenceMessage.value = null
            try {
                driverRepository.saveLicenceImage(context, id, imageUri, consentedAt)
                _licenceDoc.value = null
                loadLicenceDocument()
                _licenceMessage.value = "Licence photo sent for review."
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Could not send that photo."
            } finally {
                _uploadingLicence.value = false
            }
        }
    }

    fun removeLicenceImage() {
        val id = driverId.value ?: return
        viewModelScope.launch {
            try {
                driverRepository.deleteLicenceImage(id)
                // The number and expiry survive a withdrawn photograph, so the
                // document is refetched rather than dropped.
                _licenceDoc.value = null
                loadLicenceDocument()
                _licenceMessage.value = "Licence photo removed."
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Could not remove that photo."
            }
        }
    }

    fun clearLicenceMessage() {
        _licenceMessage.value = null
    }

    /** Streams are live already; this clears stale errors and the skeleton. */
    fun refresh() {
        _errorMessage.value = null
        _loadingHistory.value = false
    }

    fun registerDriver(licenseNumber: String, licenseExpiry: String, tricycleNumber: String) {
        val id = driverId.value ?: return
        viewModelScope.launch {
            _isRegistering.value = true
            try {
                driverRepository.registerDriver(id, licenseNumber, licenseExpiry, tricycleNumber)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Registration failed"
            } finally {
                _isRegistering.value = false
            }
        }
    }

    /**
     * Starts publishing the driver's position to the database.
     *
     * Only while online and only while a screen is collecting: the flow removes
     * its callback when this job is cancelled, so a driver who goes offline or
     * closes the app stops being tracked. There is no background service, which
     * keeps the app clear of the background-location permission and of draining
     * a battery the driver needs for the rest of their shift.
     */
    fun startPublishingLocation(context: Context) {
        val id = driverId.value ?: return
        if (locationJob?.isActive == true) return
        locationJob = viewModelScope.launch {
            LocationProvider.updates(context).collect { fix ->
                _myLocation.value = fix
                runCatching { driverRepository.updateLocation(id, fix) }
            }
        }
    }

    fun stopPublishingLocation() {
        locationJob?.cancel()
        locationJob = null
    }

    override fun onCleared() {
        super.onCleared()
        stopPublishingLocation()
    }

    fun setAvailability(isAvailable: Boolean) {
        val id = driverId.value ?: return
        viewModelScope.launch {
            try {
                driverRepository.setAvailability(id, isAvailable)
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to update availability"
            }
        }
    }

    /** True while an accept is in flight, so a second tap cannot start another. */
    private val _accepting = MutableStateFlow(false)
    val accepting: StateFlow<Boolean> = _accepting

    fun acceptRequest(request: RideRequest) {
        val id = driverId.value ?: return
        if (_accepting.value) return
        viewModelScope.launch {
            _accepting.value = true
            try {
                // The passenger has no other way to learn who is coming:
                // users/{uid} is private, so the name and number travel with
                // the ride or not at all.
                val me = runCatching { authRepository.loadUser(id) }.getOrNull()
                val ride = rideRepository.acceptRequest(
                    driverId = id,
                    request = request,
                    driverName = me?.firstName.orEmpty(),
                    driverPhone = me?.phoneNumber.orEmpty()
                )
                if (ride == null) {
                    // Another driver claimed it first. Not an error worth
                    // dressing up — it is the normal outcome of two people
                    // reaching for the same request.
                    _errorMessage.value = "Another driver took that one."
                    return@launch
                }
                // Busy with a passenger, so hide from other matching until done.
                driverRepository.setAvailability(id, false)
                supportRepository.notify(
                    userId = request.passengerId,
                    title = "Driver found",
                    message = "A driver accepted your ride to ${request.dropoffLocation.address}.",
                    type = NotificationType.RIDE
                )
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to accept request"
            } finally {
                _accepting.value = false
            }
        }
    }

    /**
     * Ends a ride that is not going to finish, and puts the driver back on the
     * road. Availability is restored because it was taken away on accept, and a
     * ride that ends badly should not leave them invisible.
     */
    fun cancelRide(ride: Ride, noShow: Boolean = false) {
        val id = driverId.value ?: return
        viewModelScope.launch {
            try {
                if (noShow) rideRepository.markNoShow(ride.id)
                else rideRepository.cancelRide(ride.id)
                driverRepository.setAvailability(id, true)
                supportRepository.notify(
                    userId = ride.passengerId,
                    title = if (noShow) "Your driver reported a no-show" else "Your ride was cancelled",
                    message = if (noShow) {
                        "The driver waited at the pickup point and could not find you."
                    } else {
                        "The driver could not complete your ride to " +
                            "${ride.dropoffLocation.address}. You can book again."
                    },
                    type = NotificationType.RIDE
                )
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to end the ride"
            }
        }
    }

    fun mayCancel(ride: Ride): Boolean = rideRepository.driverMayCancel(ride.status)

    fun mayMarkNoShow(ride: Ride): Boolean = rideRepository.driverMayMarkNoShow(ride.status)

    /** Clears requests whose five minutes are up, so the node does not grow forever. */
    fun purgeExpiredRequests() {
        viewModelScope.launch { runCatching { rideRepository.purgeExpiredRequests() } }
    }

    /** Moves a ride to its next lifecycle stage (arriving → arrived → in progress → completed). */
    fun advanceRide(ride: Ride) {
        val next = rideRepository.nextStatus(ride.status) ?: return
        val id = driverId.value ?: return
        viewModelScope.launch {
            try {
                rideRepository.updateRideStatus(ride.id, next)
                supportRepository.notify(
                    userId = ride.passengerId,
                    title = rideNotificationTitle(next),
                    message = "Your ride to ${ride.dropoffLocation.address} was updated.",
                    type = NotificationType.RIDE
                )
                if (next == com.tpc.trikride.models.RideStatus.COMPLETED) {
                    driverRepository.setAvailability(id, true)
                    driverRepository.recordCompletedRide(id)
                    // Cash changes hands off the app, so what was quoted is the
                    // best record the system has of what was taken. Writing it
                    // fills the Actual fare column, which every report read and
                    // nothing ever wrote.
                    runCatching { rideRepository.recordActualFare(ride.id, ride.estimatedFare) }
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to update ride"
            }
        }
    }

    fun dismissError() {
        _errorMessage.value = null
    }

    private fun rideNotificationTitle(status: com.tpc.trikride.models.RideStatus): String =
        when (status) {
            com.tpc.trikride.models.RideStatus.DRIVER_ARRIVING -> "Your driver is on the way"
            com.tpc.trikride.models.RideStatus.DRIVER_ARRIVED -> "Your driver has arrived"
            com.tpc.trikride.models.RideStatus.IN_PROGRESS -> "Your ride has started"
            com.tpc.trikride.models.RideStatus.COMPLETED -> "Ride completed"
            else -> "Ride update"
        }
}
```

#### `app/src/main/java/com/tpc/trikride/viewmodels/PassengerViewModel.kt`

```kotlin
package com.tpc.trikride.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tpc.trikride.models.FareConfig
import com.tpc.trikride.models.FareStop
import com.tpc.trikride.models.FareType
import com.tpc.trikride.models.Location
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.RideRequest
import com.tpc.trikride.models.Driver
import com.tpc.trikride.repositories.AuthRepository
import com.tpc.trikride.repositories.DriverRepository
import com.tpc.trikride.repositories.FareRepository
import com.tpc.trikride.repositories.RideRepository
import com.tpc.trikride.utils.FareEngine
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class PassengerViewModel(
    private val rideRepository: RideRepository = RideRepository(),
    private val fareRepository: FareRepository = FareRepository(),
    private val driverRepository: DriverRepository = DriverRepository(),
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val passengerId = MutableStateFlow<String?>(null)

    /** The passenger's open (not yet accepted) ride request, if any. */
    private val _pendingRequest = MutableStateFlow<RideRequest?>(null)
    val pendingRequest: StateFlow<RideRequest?> = _pendingRequest

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    /** Live admin-configured pricing used for fare estimates. */
    val fareConfig: StateFlow<FareConfig> = fareRepository.fareConfig()
        .catch { _errorMessage.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), FareConfig())

    /** Bookable destinations from the posted fare table. */
    val fareStops: StateFlow<List<FareStop>> = fareRepository.fareStops()
        .catch { _errorMessage.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Rides that have been accepted and are in progress for this passenger. */
    val activeRides: StateFlow<List<Ride>> = passengerId
        .filterNotNull()
        .flatMapLatest { rideRepository.passengerActiveRides(it) }
        .catch { _errorMessage.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Finished rides, newest first. */
    val rideHistory: StateFlow<List<Ride>> = passengerId
        .filterNotNull()
        .flatMapLatest { rideRepository.passengerRideHistory(it) }
        .catch { _errorMessage.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /**
     * Where the assigned driver is, while a ride is in progress.
     *
     * Null until a driver has accepted and started reporting a position. The
     * driver only publishes while their app is open and they are online, so a
     * gap here means exactly that rather than a fault.
     */
    /**
     * The assigned driver's record, for the tricycle number and the real
     * rating. Their name and telephone number are not here — those come off the
     * ride, because `users` is private and `drivers` is not.
     */
    val assignedDriver: StateFlow<Driver?> = activeRides
        .map { rides -> rides.firstOrNull()?.driverId.orEmpty() }
        .flatMapLatest { id ->
            if (id.isBlank()) flowOf(null) else driverRepository.driverProfile(id)
        }
        .catch { _errorMessage.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val driverLocation: StateFlow<Location?> = activeRides
        .map { rides -> rides.firstOrNull()?.driverId.orEmpty() }
        .flatMapLatest { id ->
            if (id.isBlank()) flowOf(null) else driverRepository.driverLocation(id)
        }
        .catch { _errorMessage.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val _loadingHistory = MutableStateFlow(true)
    val loadingHistory: StateFlow<Boolean> = _loadingHistory

    fun bind(userId: String) {
        passengerId.value = userId
        viewModelScope.launch {
            kotlinx.coroutines.delay(600)
            _loadingHistory.value = false
        }
        // A request outlives the process that made it. Held only in memory, a
        // force-close left the passenger on a dashboard with no sign of the
        // search that was still running server-side, so they booked again and
        // two drivers could each take one.
        viewModelScope.launch {
            runCatching { rideRepository.myOpenRequest(userId) }
                .getOrNull()
                ?.let { _pendingRequest.value = it }
        }
    }

    /** Firebase streams are already live; this just clears any stale error. */
    fun refresh() {
        _errorMessage.value = null
        _loadingHistory.value = false
    }

    /** True while a request is being written, so a second tap cannot send one. */
    private val _booking = MutableStateFlow(false)
    val booking: StateFlow<Boolean> = _booking

    fun requestRide(
        pickup: Location,
        destination: FareStop,
        regularCount: Int = 1,
        discountedCount: Int = 0,
        luggage: String = "None",
        notes: String = ""
    ) {
        val id = passengerId.value ?: return
        if (_booking.value || _pendingRequest.value != null) return
        val quote = FareEngine.quote(fareConfig.value, destination, regularCount, discountedCount)
        val dropoff = Location(address = destination.label)
        // Kept for records covering a party that is all one kind; a mixed party
        // is described by the two counts, not by this.
        val fareType = if (regularCount > 0) FareType.REGULAR else FareType.DISCOUNTED
        viewModelScope.launch {
            _booking.value = true
            try {
                _pendingRequest.value = rideRepository.requestRide(
                    passengerId = id,
                    pickup = pickup,
                    dropoff = dropoff,
                    passengerCount = quote.passengers,
                    regularCount = regularCount,
                    discountedCount = discountedCount,
                    luggage = luggage,
                    estimatedFare = quote.total,
                    fareStopId = destination.id,
                    fareType = fareType,
                    notes = notes
                )
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to request ride"
            } finally {
                _booking.value = false
            }
        }
    }

    /**
     * Withdraws a ride a driver has already accepted.
     *
     * Allowed until the ride is under way; after that the passenger is aboard
     * and it is the driver's to end. Before this there was no way to close a
     * ride at all, so a driver who accepted and never arrived left the
     * passenger's home tab showing a tracking screen for good.
     */
    fun cancelRide(ride: Ride) {
        viewModelScope.launch {
            try {
                rideRepository.cancelRide(ride.id)
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Could not cancel that ride"
            }
        }
    }

    fun mayCancel(ride: Ride): Boolean = rideRepository.passengerMayCancel(ride.status)

    fun cancelPendingRequest() {
        val request = _pendingRequest.value ?: return
        viewModelScope.launch {
            try {
                rideRepository.cancelRequest(request.id)
            } finally {
                _pendingRequest.value = null
            }
        }
    }

    /**
     * Puts this passenger's name and number on the ride the first time it
     * appears, so the driver can call them.
     *
     * Silent on failure. A driver who cannot telephone is a smaller problem
     * than an error banner over a ride that is otherwise proceeding normally.
     */
    private fun publishContact(ride: Ride) {
        if (ride.passengerPhone.isNotBlank()) return
        val uid = passengerId.value ?: return
        viewModelScope.launch {
            runCatching {
                val user = authRepository.loadUser(uid) ?: return@runCatching
                if (user.phoneNumber.isBlank()) return@runCatching
                rideRepository.attachPassengerContact(
                    ride.id, user.firstName, user.phoneNumber
                )
            }
        }
    }

    /** Once a ride is active, the request has been consumed by a driver. */
    fun clearPendingRequestIfMatched() {
        val active = activeRides.value.firstOrNull() ?: return
        _pendingRequest.value = null
        publishContact(active)
    }

    /**
     * Rides this passenger has already rated, so the completion screen can stop
     * offering. Kept in memory only — the screen is shown once, immediately
     * after the ride, and a rating that has been written is not editable.
     */
    private val _ratedRides = MutableStateFlow<Set<String>>(emptySet())
    val ratedRides: StateFlow<Set<String>> = _ratedRides

    fun rateRide(ride: Ride, stars: Int) {
        if (ride.id in _ratedRides.value) return
        viewModelScope.launch {
            try {
                rideRepository.rateRide(ride, stars)
                _ratedRides.value = _ratedRides.value + ride.id
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Could not send your rating"
            }
        }
    }

    fun dismissError() {
        _errorMessage.value = null
    }
}
```

#### `app/src/main/java/com/tpc/trikride/viewmodels/ProfileViewModel.kt`

```kotlin
package com.tpc.trikride.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tpc.trikride.models.User
import com.tpc.trikride.repositories.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Backs the shared Settings/Profile screens for passenger, driver and admin. */
class ProfileViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    data class ProfileUiState(
        val isLoading: Boolean = true,
        val isSaving: Boolean = false,
        val isUploadingPhoto: Boolean = false,
        val user: User? = null,
        /** The profile photo as base64, empty when the user has not set one. */
        val photo: String = "",
        val message: String? = null,
        val error: String? = null
    )

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state

    private var boundId: String? = null

    fun bind(userId: String) {
        if (boundId == userId && _state.value.user != null) return
        boundId = userId
        refresh()
    }

    fun refresh() {
        val uid = boundId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val user = repo.loadUser(uid)
                val photo = runCatching { repo.loadProfilePhoto(uid) }.getOrDefault("")
                _state.update { it.copy(isLoading = false, user = user, photo = photo) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message ?: "Failed to load profile") }
            }
        }
    }

    fun saveProfile(fullName: String, phone: String) {
        val uid = boundId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, error = null, message = null) }
            try {
                repo.updateProfile(uid, fullName.trim(), phone.trim())
                val updated = _state.value.user?.copy(
                    firstName = fullName.trim(),
                    phoneNumber = phone.trim()
                )
                _state.update {
                    it.copy(isSaving = false, user = updated, message = "Profile updated")
                }
            } catch (e: Exception) {
                _state.update { it.copy(isSaving = false, error = e.message ?: "Failed to save") }
            }
        }
    }

    fun uploadPhoto(image: android.graphics.Bitmap) {
        val uid = boundId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isUploadingPhoto = true, error = null, message = null) }
            try {
                val encoded = repo.saveProfilePhoto(uid, image)
                _state.update {
                    it.copy(isUploadingPhoto = false, photo = encoded, message = "Photo updated")
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isUploadingPhoto = false,
                        error = e.message ?: "Could not save the photo"
                    )
                }
            }
        }
    }

    fun sendPasswordReset() {
        viewModelScope.launch {
            try {
                val email = repo.sendPasswordReset()
                _state.update {
                    it.copy(
                        message = "Reset link sent to $email. It can take a few " +
                            "minutes, and it often lands in spam."
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message ?: "Failed to send reset email") }
            }
        }
    }

    fun clearMessages() {
        _state.update { it.copy(message = null, error = null) }
    }
}
```

#### `app/src/main/java/com/tpc/trikride/viewmodels/SupportViewModel.kt`

```kotlin
package com.tpc.trikride.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tpc.trikride.models.AppNotification
import com.tpc.trikride.models.Complaint
import com.tpc.trikride.models.UserType
import com.tpc.trikride.repositories.SupportRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Drives the Support form and the notifications list for the signed-in user. */
@OptIn(ExperimentalCoroutinesApi::class)
class SupportViewModel(
    private val repo: SupportRepository = SupportRepository()
) : ViewModel() {

    private val userId = MutableStateFlow<String?>(null)

    private val _submitting = MutableStateFlow(false)
    val submitting: StateFlow<Boolean> = _submitting

    private val _submitted = MutableStateFlow(false)
    val submitted: StateFlow<Boolean> = _submitted

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _loadingNotifications = MutableStateFlow(true)
    val loadingNotifications: StateFlow<Boolean> = _loadingNotifications

    val notifications: StateFlow<List<AppNotification>> = userId
        .filterNotNull()
        .flatMapLatest { repo.notifications(it) }
        .catch { _error.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val myComplaints: StateFlow<List<Complaint>> = userId
        .filterNotNull()
        .flatMapLatest { repo.myComplaints(it) }
        .catch { _error.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun bind(id: String) {
        userId.value = id
        // The flows emit almost immediately; drop the skeleton once bound.
        viewModelScope.launch {
            kotlinx.coroutines.delay(600)
            _loadingNotifications.value = false
        }
    }

    fun submitComplaint(
        reporterName: String,
        reporterType: UserType,
        category: String,
        description: String
    ) {
        val id = userId.value ?: return
        viewModelScope.launch {
            _submitting.value = true
            _error.value = null
            try {
                repo.submitComplaint(id, reporterName, reporterType, category, description)
                _submitted.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Could not submit your report"
            } finally {
                _submitting.value = false
            }
        }
    }

    fun resetSubmitted() {
        _submitted.value = false
    }

    fun markRead(notificationId: String) {
        val id = userId.value ?: return
        viewModelScope.launch {
            runCatching { repo.markRead(id, notificationId) }
        }
    }

    fun markAllRead() {
        val id = userId.value ?: return
        val unread = notifications.value.filter { !it.read }.map { it.id }
        if (unread.isEmpty()) return
        viewModelScope.launch {
            runCatching { repo.markAllRead(id, unread) }
        }
    }

    fun refreshNotifications() {
        _loadingNotifications.value = false
    }

    fun dismissError() {
        _error.value = null
    }
}
```

[[PB]]

### I.6 Utilities

#### `app/src/main/java/com/tpc/trikride/utils/AuthPrefs.kt`

```kotlin
package com.tpc.trikride.utils

import android.content.Context

/** Small local store for the "remember me" email prefill on the login screen. */
object AuthPrefs {
    private const val FILE = "trikride_auth"
    private const val KEY_EMAIL = "remembered_email"
    private const val KEY_SEEN_ONBOARDING = "seen_onboarding"

    private fun prefs(context: Context) =
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE)

    fun rememberedEmail(context: Context): String =
        prefs(context).getString(KEY_EMAIL, "").orEmpty()

    fun setRememberedEmail(context: Context, email: String) {
        prefs(context).edit().putString(KEY_EMAIL, email).apply()
    }

    fun clearRememberedEmail(context: Context) {
        prefs(context).edit().remove(KEY_EMAIL).apply()
    }

    private const val KEY_DARK_MODE = "dark_mode_override"

    /**
     * The theme the user chose, or null to follow the system.
     *
     * Held only in a process-global before, so every launch went back to the
     * system setting and the switch in Settings looked broken.
     */
    fun darkModeOverride(context: Context): Boolean? =
        prefs(context).let { p ->
            if (!p.contains(KEY_DARK_MODE)) null else p.getBoolean(KEY_DARK_MODE, false)
        }

    fun setDarkModeOverride(context: Context, dark: Boolean?) {
        val editor = prefs(context).edit()
        if (dark == null) editor.remove(KEY_DARK_MODE) else editor.putBoolean(KEY_DARK_MODE, dark)
        editor.apply()
    }

    /** The onboarding carousel is shown once, on the first launch. */
    fun hasSeenOnboarding(context: Context): Boolean =
        prefs(context).getBoolean(KEY_SEEN_ONBOARDING, false)

    fun setSeenOnboarding(context: Context) {
        prefs(context).edit().putBoolean(KEY_SEEN_ONBOARDING, true).apply()
    }
}
```

#### `app/src/main/java/com/tpc/trikride/utils/BirthDate.kt`

```kotlin
package com.tpc.trikride.utils

import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

/**
 * The date of birth, stored as a date rather than as a sentence.
 *
 * It used to be written with `SimpleDateFormat("MMM d, yyyy", Locale.getDefault())`
 * straight into `users/{uid}/birthDate`, so two phones in two locales stored two
 * different, mutually unparseable strings for the same day, and a locale with
 * non-Latin digits stored something nothing here reads back. Birthdate is the
 * only basis the system has for a senior's entitlement, so it has to be a value
 * and not prose.
 *
 * Material's date picker reports a selection as midnight UTC on the chosen day.
 * Rendering that instant in the device's zone moves it to the previous day
 * anywhere behind UTC. `ReportPeriod.customRange` already got this right for
 * report ranges; this is the same reading applied where it was missed.
 */
object BirthDate {

    /** Nobody younger than this may hold an account. */
    const val MINIMUM_AGE = 13

    /** Above this, the entry is a mis-scroll rather than a birthday. */
    const val MAXIMUM_AGE = 120

    private val MONTHS = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    /** Turns the picker's UTC instant into a plain `yyyy-MM-dd`. */
    fun fromPickerUtc(pickedUtcMillis: Long): String {
        val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            timeInMillis = pickedUtcMillis
        }
        return "%04d-%02d-%02d".format(
            Locale.US,
            utc.get(Calendar.YEAR),
            utc.get(Calendar.MONTH) + 1,
            utc.get(Calendar.DAY_OF_MONTH)
        )
    }

    /** "16 August 2004", for showing. Returns the input unchanged if it is not a stored date. */
    fun display(stored: String): String {
        val parts = parse(stored) ?: return stored
        val (y, m, d) = parts
        return "%d %s %d".format(Locale.US, d, MONTHS[m - 1], y)
    }

    /** Whether this is a date somebody could actually have been born on. */
    fun isPlausible(stored: String, now: Long = System.currentTimeMillis()): Boolean {
        val age = ageOn(stored, now) ?: return false
        return age in MINIMUM_AGE..MAXIMUM_AGE
    }

    /** Completed years as at [now], or null if [stored] is not a date. */
    fun ageOn(stored: String, now: Long = System.currentTimeMillis()): Int? {
        val (y, m, d) = parse(stored) ?: return null
        val today = Calendar.getInstance().apply { timeInMillis = now }
        var age = today.get(Calendar.YEAR) - y
        val monthNow = today.get(Calendar.MONTH) + 1
        val dayNow = today.get(Calendar.DAY_OF_MONTH)
        if (monthNow < m || (monthNow == m && dayNow < d)) age--
        return age
    }

    /** Why the chosen date is refused, or null when it is fine. */
    fun reject(stored: String, now: Long = System.currentTimeMillis()): String? {
        val age = ageOn(stored, now) ?: return "Choose your date of birth."
        return when {
            age < 0 -> "That date is in the future."
            age < MINIMUM_AGE -> "You need to be at least $MINIMUM_AGE to use TrikRide."
            age > MAXIMUM_AGE -> "Check the year — that date is over $MAXIMUM_AGE years ago."
            else -> null
        }
    }

    private fun parse(stored: String): Triple<Int, Int, Int>? {
        val parts = stored.split("-")
        if (parts.size != 3) return null
        val y = parts[0].toIntOrNull() ?: return null
        val m = parts[1].toIntOrNull() ?: return null
        val d = parts[2].toIntOrNull() ?: return null
        if (m !in 1..12 || d !in 1..31) return null
        return Triple(y, m, d)
    }
}
```

#### `app/src/main/java/com/tpc/trikride/utils/CacheCleanup.kt`

```kotlin
package com.tpc.trikride.utils

import java.io.File

/**
 * Clears the working files the app leaves in its cache directory.
 *
 * Two directories collect things that are only ever needed for a moment.
 * `images/` holds the full-resolution frame the camera writes before it is
 * shrunk and encoded — for a driver's licence that is a photograph of a
 * government identity document. `reports/` holds an exported PDF or spreadsheet
 * on its way to the share sheet, carrying the name, email and telephone number
 * of every user in the system.
 *
 * Neither was ever deleted. The database copy of a licence goes when the driver
 * withdraws it, and the app tells them so in as many words, while the original
 * capture stayed on the phone indefinitely. Android reclaims a cache directory
 * only under storage pressure, which is not a retention policy.
 *
 * Takes a [File] rather than a Context so the rule can be tested without a
 * device. An hour is long enough that nothing in flight is removed — a capture
 * is consumed the moment the camera returns — and short enough that a document
 * does not outlive the session that made it.
 */
object CacheCleanup {

    /** Directories holding files that exist only for the length of one action. */
    private val TRANSIENT = listOf("images", "reports")

    const val DEFAULT_MAX_AGE_MS: Long = 60 * 60 * 1000L

    /**
     * Deletes transient files last modified more than [maxAgeMs] before [now].
     * Returns how many were removed. Never throws: a cache that cannot be swept
     * is not a reason to fail a launch.
     */
    fun sweep(
        cacheDir: File,
        now: Long = System.currentTimeMillis(),
        maxAgeMs: Long = DEFAULT_MAX_AGE_MS
    ): Int {
        var removed = 0
        for (name in TRANSIENT) {
            val dir = File(cacheDir, name)
            val files = runCatching { dir.listFiles() }.getOrNull() ?: continue
            for (file in files) {
                if (!file.isFile) continue
                if (now - file.lastModified() < maxAgeMs) continue
                if (runCatching { file.delete() }.getOrDefault(false)) removed++
            }
        }
        return removed
    }
}
```

#### `app/src/main/java/com/tpc/trikride/utils/Constants.kt`

```kotlin
package com.tpc.trikride.utils

object Constants {

    /** How long a ride request stays open before expiring, in milliseconds. */
    const val RIDE_REQUEST_TTL_MS = 5 * 60 * 1000L

    /** Seats a tricycle can take on one booking. */
    const val MAX_PASSENGERS = 5

    /**
     * The date carried on the issued legal documents. Consent is recorded
     * against this string, so publishing a revised set is a matter of changing
     * it here: every user is then asked to accept again on their next launch.
     */
    const val LEGAL_VERSION = "2026-08-16"
}
```

#### `app/src/main/java/com/tpc/trikride/utils/FareEngine.kt`

```kotlin
package com.tpc.trikride.utils

import com.tpc.trikride.models.FareConfig
import com.tpc.trikride.models.FareQuote
import com.tpc.trikride.models.FareStop
import com.tpc.trikride.models.FareType

/**
 * Prices a ride against the posted FeTODAT fare table.
 *
 * The sheet gives a flat amount per stop, one column for the regular rate and
 * one for the senior, PWD and student rate, so pricing is a lookup rather than
 * a calculation. Two things sit on top of the lookup: the ordinance minimum
 * fare, which a stop rate can never fall below, and the per-head multiplier,
 * since a tricycle fare is charged per passenger.
 */
object FareEngine {

    fun minimumFor(config: FareConfig, fareType: FareType): Double = when (fareType) {
        FareType.REGULAR -> config.minimumRegular
        FareType.DISCOUNTED -> config.minimumDiscounted
    }

    fun rateFor(stop: FareStop, fareType: FareType): Double = when (fareType) {
        FareType.REGULAR -> stop.regularFare
        FareType.DISCOUNTED -> stop.discountedFare
    }

    /**
     * Prices a party that may hold both kinds of passenger.
     *
     * Each column is raised to its own ordinance minimum before it is
     * multiplied, so a senior travelling beside a regular passenger is charged
     * the senior rate and the regular passenger is not.
     *
     * Where the sheet is not charged per head, one fare covers the tricycle,
     * and it is the regular rate unless everybody aboard is entitled to the
     * discount — charging the discounted rate for a mixed party would undercharge,
     * and charging the regular rate to a party of seniors would overcharge them.
     */
    fun quote(
        config: FareConfig,
        stop: FareStop,
        regularCount: Int,
        discountedCount: Int
    ): FareQuote {
        val regularRate = maxOf(
            rateFor(stop, FareType.REGULAR), minimumFor(config, FareType.REGULAR)
        )
        // Never above the regular rate for the same stop. Three rows of the
        // transcribed table carry a discounted rate higher than the regular one
        // — Mar Auguis, Dancy, and the Arlen special trip — and would otherwise
        // charge a senior, a person with a disability or a student more than
        // the passenger beside them, which RA 9994 and RA 10754 forbid. The
        // clamp is here rather than in the seed so that a rate typed wrong in
        // the admin fare screen cannot do it either.
        val discountedRate = minOf(
            maxOf(
                rateFor(stop, FareType.DISCOUNTED), minimumFor(config, FareType.DISCOUNTED)
            ),
            regularRate
        )
        val regular = regularCount.coerceAtLeast(0)
        val discounted = discountedCount.coerceAtLeast(0)

        val total = if (config.chargePerPassenger) {
            regular * regularRate + discounted * discountedRate
        } else {
            if (regular > 0) regularRate else discountedRate
        }

        return FareQuote(
            regularCount = regular,
            discountedCount = discounted,
            regularRate = regularRate,
            discountedRate = discountedRate,
            total = total,
            minimumApplied = rateFor(stop, FareType.REGULAR) < minimumFor(config, FareType.REGULAR) ||
                rateFor(stop, FareType.DISCOUNTED) < minimumFor(config, FareType.DISCOUNTED),
            stopLabel = stop.label
        )
    }

    /**
     * The two rates on the sheet that are not tied to a numbered stop, shaped
     * as stops so the picker and the pricing path treat them like any other
     * destination. Neither is split by rate column on the posted sheet, so both
     * columns carry the same amount.
     */
    fun flatStops(config: FareConfig): List<FareStop> = listOf(
        FareStop(
            id = FLAT_POBLACION,
            zone = FLAT_ZONE,
            name = FareConfig.POBLACION_LABEL,
            regularFare = config.poblacionFlat,
            discountedFare = config.poblacionFlat
        ),
        FareStop(
            id = FLAT_TERMINAL,
            zone = FLAT_ZONE,
            name = FareConfig.TERMINAL_ROUND_TRIP_LABEL,
            regularFare = config.terminalRoundTrip,
            discountedFare = config.terminalRoundTrip
        )
    )

    const val FLAT_ZONE = "Flat rate"
    private const val FLAT_POBLACION = "flat__poblacion"
    private const val FLAT_TERMINAL = "flat__terminal_ncbi_round_trip"
}
```

#### `app/src/main/java/com/tpc/trikride/utils/FareSeed.kt`

```kotlin
package com.tpc.trikride.utils

import com.tpc.trikride.models.FareStop

/**
 * The published FeTODAT fare table, transcribed from the laminated sheet
 * posted by the Federation of Tricycle Operators and Drivers Association of
 * Talibon. Source ordinance amends Section 1 of Municipal Ordinance No.
 * 2018-05 and was enacted on 8 November 2022.
 *
 * This is seed data only. The admin fare screen writes it into the database
 * once, after which the database copy is what the app prices rides from, so
 * corrections happen in the app and not here.
 *
 * Rows carrying a note came out of the photos with a problem: a rate that is
 * lower for seniors than the regular rate, a value cut off at the edge of the
 * frame, or a reading two transcription passes disagreed on. Those are marked
 * needsReview so the admin fare screen can list them, and the ones missing a
 * usable rate start inactive so they cannot price a ride.
 */
object FareSeed {

    /** Zones in the order they appear on the posted sheet. */
    val ZONES = listOf(
        "Balintawak",
        "Santo Nino",
        "San Francisco",
        "San Agustin",
        "Bagacay, Burgos & Rizal",
        "Zamora",
        "San Carlos",
        "Tanghaligue",
        "San Isidro",
        "San Jose",
        "San Pedro",
        "San Roque"
    )

    /** 240 stops across 12 zones. */
    val STOPS: List<FareStop> = listOf(
        FareStop(id = "balintawak__highway_balintawak_to_market", zone = "Balintawak", name = "Highway Balintawak to Market", regularFare = 20.0, discountedFare = 16.0),
        FareStop(id = "balintawak__terminal_titt_to_balintawak", zone = "Balintawak", name = "Terminal (TITT) to Balintawak", regularFare = 20.0, discountedFare = 16.0),
        FareStop(id = "balintawak__garcia_park_ibabaw", zone = "Balintawak", name = "Garcia Park Ibabaw", regularFare = 55.0, discountedFare = 44.0),
        FareStop(id = "balintawak__garcia_park_elementary_school", zone = "Balintawak", name = "Garcia Park Elementary School", regularFare = 45.0, discountedFare = 36.0),
        FareStop(id = "balintawak__tiya_naring_item", zone = "Balintawak", name = "Tiya Naring Item", regularFare = 30.0, discountedFare = 24.0),
        FareStop(id = "balintawak__sergio_auza", zone = "Balintawak", name = "Sergio Auza", regularFare = 25.0, discountedFare = 20.0),
        FareStop(id = "balintawak__balintawak_drier_to_tiya_consing", zone = "Balintawak", name = "Balintawak Drier to Tiya Consing", regularFare = 20.0, discountedFare = 16.0),
        FareStop(id = "balintawak__emmi_rosales", zone = "Balintawak", name = "Emmi Rosales", regularFare = 20.0, discountedFare = 16.0),
        FareStop(id = "balintawak__lorito_crescencio", zone = "Balintawak", name = "Lorito Crescencio", regularFare = 30.0, discountedFare = 24.0),
        FareStop(id = "balintawak__den_den_mosqueda", zone = "Balintawak", name = "Den-Den Mosqueda", regularFare = 30.0, discountedFare = 24.0),
        FareStop(id = "balintawak__jhonrey_polestico", zone = "Balintawak", name = "Jhonrey Polestico", regularFare = 35.0, discountedFare = 28.0),
        FareStop(id = "balintawak__mahogany_pension_house", zone = "Balintawak", name = "Mahogany Pension House", regularFare = 20.0, discountedFare = 16.0),
        FareStop(id = "balintawak__purok_3", zone = "Balintawak", name = "Purok 3", regularFare = 20.0, discountedFare = 16.0),
        FareStop(id = "balintawak__erning_auxtero", zone = "Balintawak", name = "Erning Auxtero", regularFare = 30.0, discountedFare = 24.0),
        FareStop(id = "balintawak__noy_audie_autentico", zone = "Balintawak", name = "Noy Audie Autentico", regularFare = 45.0, discountedFare = 36.0),
        FareStop(id = "balintawak__melchor_polestico", zone = "Balintawak", name = "Melchor Polestico", regularFare = 30.0, discountedFare = 24.0),
        FareStop(id = "balintawak__tiya_doring_valmoria", zone = "Balintawak", name = "Tiya Doring Valmoria", regularFare = 20.0, discountedFare = 16.0),
        FareStop(id = "balintawak__pasulod_purok_6", zone = "Balintawak", name = "Pasulod Purok 6", regularFare = 20.0, discountedFare = 16.0),
        FareStop(id = "balintawak__jun_torreon", zone = "Balintawak", name = "Jun Torreon", regularFare = 30.0, discountedFare = 24.0),
        FareStop(id = "balintawak__purok_7_nia", zone = "Balintawak", name = "Purok 7 NIA", regularFare = 30.0, discountedFare = 24.0),
        FareStop(id = "balintawak__purok_7_kabenic", zone = "Balintawak", name = "Purok 7 (Kabenic)", regularFare = 35.0, discountedFare = 28.0),
        FareStop(id = "balintawak__ka_ano_garcia", zone = "Balintawak", name = "Ka Ano Garcia", regularFare = 20.0, discountedFare = 24.0, needsReview = true, confidence = "Medium", note = "New Rate LOWER than Senior/PWD rate - inverted. Prior transcription flagged 'Uni Auxtero' as the inverted row instead - re-verify against physical sheet."),
        FareStop(id = "balintawak__uni_auxtero", zone = "Balintawak", name = "Uni Auxtero", regularFare = 30.0, discountedFare = 24.0),
        FareStop(id = "balintawak__roque_polestico", zone = "Balintawak", name = "Roque Polestico", regularFare = 20.0, discountedFare = 16.0),
        FareStop(id = "balintawak__bes_gate", zone = "Balintawak", name = "Bes Gate", regularFare = 20.0, discountedFare = 16.0),
        FareStop(id = "balintawak__purok_4_ka_vecenta", zone = "Balintawak", name = "Purok 4 (Ka Vecenta)", regularFare = 25.0, discountedFare = 20.0),
        FareStop(id = "balintawak__purok_4_ka_doring", zone = "Balintawak", name = "Purok 4 (Ka Doring)", regularFare = 20.0, discountedFare = 16.0),
        FareStop(id = "balintawak__purok_2", zone = "Balintawak", name = "Purok 2", regularFare = 30.0, discountedFare = 24.0),
        FareStop(id = "balintawak__purok_1_monticalbo", zone = "Balintawak", name = "Purok 1 (Monticalbo)", regularFare = 25.0, discountedFare = 20.0),
        FareStop(id = "balintawak__purok_1_tiya_naring", zone = "Balintawak", name = "Purok 1 (Tiya Naring)", regularFare = 22.5, discountedFare = 18.0),
        FareStop(id = "balintawak__garcia_park", zone = "Balintawak", name = "Garcia Park", regularFare = 30.0, discountedFare = 24.0),
        FareStop(id = "santo_nino__hi_way_ka_lorna", zone = "Santo Nino", name = "Hi-way Ka Lorna", regularFare = 25.0, discountedFare = 20.0),
        FareStop(id = "santo_nino__lorna_to_mabuhay_kikoy", zone = "Santo Nino", name = "Lorna to Mabuhay/Kikoy", regularFare = 50.0, discountedFare = 40.0),
        FareStop(id = "santo_nino__hi_way_to_ka_buting", zone = "Santo Nino", name = "Hi-way to Ka Buting", regularFare = 30.0, discountedFare = 24.0),
        FareStop(id = "santo_nino__buting_to_ka_poloy", zone = "Santo Nino", name = "Buting to Ka Poloy", regularFare = 35.0, discountedFare = 28.0),
        FareStop(id = "santo_nino__poloy_to_ka_garitoy", zone = "Santo Nino", name = "Poloy to Ka Garitoy", regularFare = 40.0, discountedFare = 32.0),
        FareStop(id = "santo_nino__garitoy_to_ka_cario", zone = "Santo Nino", name = "Garitoy to Ka Cario", regularFare = 50.0, discountedFare = 40.0),
        FareStop(id = "santo_nino__hi_way_to_coop", zone = "Santo Nino", name = "Hi-way to Coop", regularFare = 35.0, discountedFare = 28.0),
        FareStop(id = "santo_nino__coop_to_ka_sherley", zone = "Santo Nino", name = "Coop to Ka Sherley", regularFare = 40.0, discountedFare = 32.0),
        FareStop(id = "santo_nino__sherly_to_ka_kasto", zone = "Santo Nino", name = "Sherly to Ka Kasto", regularFare = 50.0, discountedFare = 40.0),
        FareStop(id = "santo_nino__kastor_to_ka_francis", zone = "Santo Nino", name = "Kastor to Ka Francis", regularFare = 60.0, discountedFare = 48.0),
        FareStop(id = "santo_nino__hi_way_to_ka_timo", zone = "Santo Nino", name = "Hi-way to Ka Timo", regularFare = 40.0, discountedFare = 32.0),
        FareStop(id = "santo_nino__timot_to_ka_eting", zone = "Santo Nino", name = "Timot to Ka Eting", regularFare = 50.0, discountedFare = 40.0),
        FareStop(id = "santo_nino__eting_to_ka_pedio", zone = "Santo Nino", name = "Eting to Ka Pedio", regularFare = 60.0, discountedFare = 48.0),
        FareStop(id = "santo_nino__pedio_to_ka_frank", zone = "Santo Nino", name = "Pedio to Ka Frank", regularFare = 65.0, discountedFare = 52.0),
        FareStop(id = "santo_nino__santo_nino_hs", zone = "Santo Nino", name = "Santo Nino HS", regularFare = 35.0, discountedFare = 28.0),
        FareStop(id = "santo_nino__sto_nino_hs_to_ka_alice", zone = "Santo Nino", name = "Sto. Nino HS to Ka Alice", regularFare = 40.0, discountedFare = 32.0),
        FareStop(id = "santo_nino__alice_to_beracha_eyog", zone = "Santo Nino", name = "Alice to Beracha/Eyog", regularFare = 50.0, discountedFare = 40.0),
        FareStop(id = "santo_nino__beracha_eyog_to_ka_piling", zone = "Santo Nino", name = "Beracha/Eyog to Ka Piling", regularFare = 60.0, discountedFare = 48.0),
        FareStop(id = "santo_nino__piling_to_ka_camilo", zone = "Santo Nino", name = "Piling to Ka Camilo", regularFare = 70.0, discountedFare = 56.0),
        FareStop(id = "santo_nino__highway_to_ka_melencio", zone = "Santo Nino", name = "Highway to Ka Melencio", regularFare = 35.0, discountedFare = 28.0),
        FareStop(id = "santo_nino__melecio_to_ka_rina", zone = "Santo Nino", name = "Melecio to Ka Rina", regularFare = 40.0, discountedFare = 32.0),
        FareStop(id = "santo_nino__rina_to_ka_jimmy", zone = "Santo Nino", name = "Rina to Ka Jimmy", regularFare = 50.0, discountedFare = 40.0),
        FareStop(id = "santo_nino__jimmy_to_ka_esoy", zone = "Santo Nino", name = "Jimmy to Ka Esoy", regularFare = 60.0, discountedFare = 48.0),
        FareStop(id = "santo_nino__ka_rina_to_lomboy", zone = "Santo Nino", name = "Ka Rina to Lomboy", regularFare = 50.0, discountedFare = 40.0),
        FareStop(id = "santo_nino__lomboy_to_ka_paran", zone = "Santo Nino", name = "Lomboy to Ka Paran", regularFare = 50.0, discountedFare = 40.0),
        FareStop(id = "santo_nino__paran_to_basurahan", zone = "Santo Nino", name = "Paran to Basurahan", regularFare = 80.0, discountedFare = 64.0),
        FareStop(id = "santo_nino__lomboy_to_ka_kilita", zone = "Santo Nino", name = "Lomboy to Ka Kilita", regularFare = 70.0, discountedFare = 56.0),
        FareStop(id = "santo_nino__highway_to_ka_nanong", zone = "Santo Nino", name = "Highway to Ka Nanong", regularFare = 45.0, discountedFare = 36.0),
        FareStop(id = "santo_nino__nanong_to_ka_gorio", zone = "Santo Nino", name = "Nanong to Ka Gorio", regularFare = 50.0, discountedFare = 40.0),
        FareStop(id = "santo_nino__highway_to_ka_porton", zone = "Santo Nino", name = "Highway to Ka Porton", regularFare = 35.0, discountedFare = 28.0),
        FareStop(id = "santo_nino__highway_to_ka_nigro", zone = "Santo Nino", name = "Highway to Ka Nigro", regularFare = 40.0, discountedFare = 32.0),
        FareStop(id = "santo_nino__nigro_to_panagbuan", zone = "Santo Nino", name = "Nigro to Panagbuan", regularFare = 50.0, discountedFare = 40.0),
        FareStop(id = "santo_nino__highway_to_kamomot", zone = "Santo Nino", name = "Highway to Kamomot", regularFare = 35.0, discountedFare = 28.0),
        FareStop(id = "santo_nino__momot_to_polin_misyang", zone = "Santo Nino", name = "Momot to Polin/Misyang", regularFare = 40.0, discountedFare = 32.0),
        FareStop(id = "santo_nino__highway_to_sitio_lapak", zone = "Santo Nino", name = "Highway to Sitio Lapak", regularFare = 35.0, discountedFare = 28.0),
        FareStop(id = "santo_nino__highway_to_ka_ciling", zone = "Santo Nino", name = "Highway to Ka Ciling", regularFare = 35.0, discountedFare = 28.0),
        FareStop(id = "santo_nino__ciling_to_sitio_sun_ok", zone = "Santo Nino", name = "Ciling to Sitio Sun-ok", regularFare = 35.0, discountedFare = 28.0),
        FareStop(id = "santo_nino__highway_to_ka_fred", zone = "Santo Nino", name = "Highway to Ka Fred", regularFare = 35.0, discountedFare = 28.0),
        FareStop(id = "san_francisco__lising", zone = "San Francisco", name = "Lising", regularFare = 20.0, discountedFare = 16.0),
        FareStop(id = "san_francisco__lantay_boys", zone = "San Francisco", name = "Lantay Boys", regularFare = 20.0, discountedFare = 16.0),
        FareStop(id = "san_francisco__centro", zone = "San Francisco", name = "Centro", regularFare = 20.0, discountedFare = 16.0),
        FareStop(id = "san_francisco__eskina_frank", zone = "San Francisco", name = "Eskina Frank", regularFare = 20.0, discountedFare = 16.0),
        FareStop(id = "san_francisco__coop", zone = "San Francisco", name = "Coop", regularFare = 20.0, discountedFare = 16.0, needsReview = true, confidence = "Medium", note = "DISPUTED: this photo reads 16/20; prior transcription session read 24/30 for this row. Re-verify against physical sheet."),
        FareStop(id = "san_francisco__taytay", zone = "San Francisco", name = "Taytay", regularFare = 30.0, discountedFare = 24.0, needsReview = true, confidence = "Medium", note = "DISPUTED: this photo reads 24/30; prior transcription session read 32/40 for this row. Re-verify against physical sheet."),
        FareStop(id = "san_francisco__sitio_panabang", zone = "San Francisco", name = "Sitio Panabang", regularFare = 40.0, discountedFare = 32.0, needsReview = true, confidence = "Medium", note = "DISPUTED: this photo reads 32/40; prior transcription session read 48/60 for this row. Re-verify against physical sheet."),
        FareStop(id = "san_francisco__tumoy_tabon", zone = "San Francisco", name = "Tumoy Tabon", regularFare = 60.0, discountedFare = 48.0, needsReview = true, confidence = "Medium", note = "DISPUTED: this photo reads 48/60; prior transcription session read 32/40 for this row. Re-verify against physical sheet."),
        FareStop(id = "san_francisco__ka_turo_suan", zone = "San Francisco", name = "Ka Turo Suan", regularFare = 40.0, discountedFare = 32.0, needsReview = true, confidence = "Medium", note = "DISPUTED: this photo reads 32/40; prior transcription session read 24/30 for this row. Re-verify against physical sheet."),
        FareStop(id = "san_francisco__eskina_ka_boboy_tessie", zone = "San Francisco", name = "Eskina Ka Boboy Tessie", regularFare = 30.0, discountedFare = 24.0, needsReview = true, confidence = "Medium", note = "DISPUTED: this photo reads 24/30; prior transcription session read 32/40 for this row. Re-verify against physical sheet."),
        FareStop(id = "san_francisco__gisok", zone = "San Francisco", name = "Gisok", regularFare = 40.0, discountedFare = 32.0, needsReview = true, confidence = "Medium", note = "DISPUTED: this photo reads 32/40; prior transcription session read 24/30 for this row. Re-verify against physical sheet."),
        FareStop(id = "san_francisco__baybay", zone = "San Francisco", name = "Baybay", regularFare = 30.0, discountedFare = 24.0),
        FareStop(id = "san_francisco__bacbacan", zone = "San Francisco", name = "Bacbacan", regularFare = 30.0, discountedFare = 24.0),
        FareStop(id = "san_francisco__carusos_area", zone = "San Francisco", name = "Carusos Area", regularFare = 30.0, discountedFare = 24.0),
        FareStop(id = "san_francisco__boy_mamie", zone = "San Francisco", name = "Boy Mamie", regularFare = 30.0, discountedFare = 24.0),
        FareStop(id = "san_francisco__sitio_panabang_2nd_entry", zone = "San Francisco", name = "Sitio Panabang (2nd entry)", regularFare = 40.0, discountedFare = 32.0, needsReview = true, confidence = "Medium", note = "Duplicate stop name with a different rate than the earlier 'Sitio Panabang' row - possibly two distinct sitio locations sharing a name, or a misread. Verify."),
        FareStop(id = "san_francisco__health_center", zone = "San Francisco", name = "Health Center", regularFare = 30.0, discountedFare = 24.0),
        FareStop(id = "san_francisco__la_purisima_chapel", zone = "San Francisco", name = "La Purisima Chapel", regularFare = 30.0, discountedFare = 24.0),
        FareStop(id = "san_francisco__prk_6_san_vicente_chapel", zone = "San Francisco", name = "Prk 6 San Vicente Chapel", regularFare = 40.0, discountedFare = 32.0),
        FareStop(id = "san_francisco__edgar_anita_water_refilling_turo", zone = "San Francisco", name = "Edgar Anita Water-Refilling Turo", regularFare = 40.0, discountedFare = 32.0),
        FareStop(id = "san_francisco__turo_fish_port", zone = "San Francisco", name = "Turo Fish Port", regularFare = 50.0, discountedFare = 40.0),
        FareStop(id = "san_agustin__pob_to_purok_7", zone = "San Agustin", name = "Pob. to Purok 7", regularFare = 20.0, discountedFare = 16.0),
        FareStop(id = "san_agustin__san_agustine_parish_church", zone = "San Agustin", name = "San Agustine Parish Church", regularFare = 30.0, discountedFare = 24.0),
        FareStop(id = "san_agustin__castor_prk5_prk6", zone = "San Agustin", name = "Castor (Prk5-Prk6)", regularFare = 30.0, discountedFare = 24.0),
        FareStop(id = "san_agustin__crossing_prk6_ka_lita", zone = "San Agustin", name = "Crossing (Prk6 Ka Lita)", regularFare = 40.0, discountedFare = 32.0),
        FareStop(id = "san_agustin__ka_tita_dam_ka_mario", zone = "San Agustin", name = "Ka Tita-Dam Ka Mario", regularFare = 40.0, discountedFare = 32.0),
        FareStop(id = "san_agustin__ka_cielo_tongo", zone = "San Agustin", name = "Ka Cielo (Tongo)", regularFare = 40.0, discountedFare = 32.0),
        FareStop(id = "san_agustin__ka_pilto_kalahi_lokso_on", zone = "San Agustin", name = "Ka Pilto (Kalahi Lokso-on)", regularFare = 40.0, discountedFare = 32.0),
        FareStop(id = "san_agustin__highway_sa_agustine", zone = "San Agustin", name = "Highway sa Agustine", regularFare = 30.0, discountedFare = 24.0),
        FareStop(id = "san_agustin__cirilo", zone = "San Agustin", name = "Cirilo", regularFare = 25.0, discountedFare = 20.0),
        FareStop(id = "san_agustin__centro_purok3", zone = "San Agustin", name = "Centro (Purok3)", regularFare = 30.0, discountedFare = 24.0),
        FareStop(id = "san_agustin__bunso", zone = "San Agustin", name = "Bunso", regularFare = 30.0, discountedFare = 24.0),
        FareStop(id = "san_agustin__sudlon", zone = "San Agustin", name = "Sudlon", regularFare = 40.0, discountedFare = 32.0),
        FareStop(id = "san_agustin__purok_4", zone = "San Agustin", name = "Purok 4", regularFare = 30.0, discountedFare = 24.0),
        FareStop(id = "san_agustin__centro_purok_4_lugsong", zone = "San Agustin", name = "Centro Purok 4/Lugsong", regularFare = 35.0, discountedFare = 28.0),
        FareStop(id = "san_agustin__munumento_pcpg_ancestral_park", zone = "San Agustin", name = "Munumento (PCPG Ancestral Park)", regularFare = 30.0, discountedFare = 24.0),
        FareStop(id = "san_agustin__ka_victor", zone = "San Agustin", name = "Ka Victor", regularFare = 40.0, discountedFare = 32.0),
        FareStop(id = "bagacay_burgos_rizal__bagacay_highway", zone = "Bagacay, Burgos & Rizal", name = "Bagacay Highway", regularFare = 40.0, discountedFare = 32.0),
        FareStop(id = "bagacay_burgos_rizal__burgos_highway", zone = "Bagacay, Burgos & Rizal", name = "Burgos Highway", regularFare = 45.0, discountedFare = 36.0),
        FareStop(id = "bagacay_burgos_rizal__bagacay_purok_5", zone = "Bagacay, Burgos & Rizal", name = "Bagacay Purok 5", regularFare = 50.0, discountedFare = 40.0),
        FareStop(id = "bagacay_burgos_rizal__bagacay_highschool", zone = "Bagacay, Burgos & Rizal", name = "Bagacay Highschool", regularFare = 50.0, discountedFare = 40.0),
        FareStop(id = "bagacay_burgos_rizal__bagacay_lapay", zone = "Bagacay, Burgos & Rizal", name = "Bagacay Lapay", regularFare = 60.0, discountedFare = 48.0),
        FareStop(id = "bagacay_burgos_rizal__bagacay_burawin", zone = "Bagacay, Burgos & Rizal", name = "Bagacay Burawin", regularFare = 70.0, discountedFare = 56.0),
        FareStop(id = "bagacay_burgos_rizal__namong", zone = "Bagacay, Burgos & Rizal", name = "Namong", regularFare = 45.0, discountedFare = 36.0),
        FareStop(id = "bagacay_burgos_rizal__bagacay_pantalan", zone = "Bagacay, Burgos & Rizal", name = "Bagacay Pantalan", regularFare = 50.0, discountedFare = 40.0),
        FareStop(id = "bagacay_burgos_rizal__baracuda", zone = "Bagacay, Burgos & Rizal", name = "Baracuda", regularFare = 50.0, discountedFare = 40.0),
        FareStop(id = "bagacay_burgos_rizal__namong_purok_2", zone = "Bagacay, Burgos & Rizal", name = "Namong Purok 2", regularFare = 50.0, discountedFare = 40.0),
        FareStop(id = "bagacay_burgos_rizal__bagacay_lambayan", zone = "Bagacay, Burgos & Rizal", name = "Bagacay Lambayan", regularFare = 50.0, discountedFare = 40.0),
        FareStop(id = "bagacay_burgos_rizal__mga_sudlonon_sa_burgos", zone = "Bagacay, Burgos & Rizal", name = "Mga Sudlonon sa Burgos", regularFare = 45.0, discountedFare = 36.0),
        FareStop(id = "bagacay_burgos_rizal__rizal", zone = "Bagacay, Burgos & Rizal", name = "Rizal", regularFare = 100.0, discountedFare = 80.0),
        FareStop(id = "bagacay_burgos_rizal__burgos_purok_1_2_3", zone = "Bagacay, Burgos & Rizal", name = "Burgos Purok 1, 2, 3", regularFare = 50.0, discountedFare = 40.0),
        FareStop(id = "bagacay_burgos_rizal__burgos_purok_4_5", zone = "Bagacay, Burgos & Rizal", name = "Burgos Purok 4, 5", regularFare = 70.0, discountedFare = 56.0),
        FareStop(id = "bagacay_burgos_rizal__burgos_purok_7", zone = "Bagacay, Burgos & Rizal", name = "Burgos Purok 7", regularFare = 80.0, discountedFare = 64.0),
        FareStop(id = "zamora__sambag", zone = "Zamora", name = "Sambag", regularFare = 50.0, discountedFare = 40.0),
        FareStop(id = "zamora__tonggoto_resing", zone = "Zamora", name = "Tonggoto Resing", regularFare = 50.0, discountedFare = 40.0),
        FareStop(id = "zamora__nia_office_to_patring", zone = "Zamora", name = "NIA Office to Patring", regularFare = 60.0, discountedFare = 48.0),
        FareStop(id = "zamora__san_vicente_village_dicong", zone = "Zamora", name = "San Vicente Village-Dicong", regularFare = 60.0, discountedFare = 48.0),
        FareStop(id = "zamora__pina_lydia_roping", zone = "Zamora", name = "Pina-Lydia Roping", regularFare = 60.0, discountedFare = 48.0),
        FareStop(id = "zamora__pablo_daring_demit", zone = "Zamora", name = "Pablo-Daring-Demit", regularFare = 80.0, discountedFare = 64.0),
        FareStop(id = "zamora__iglesia", zone = "Zamora", name = "Iglesia", regularFare = 45.0, discountedFare = 36.0),
        FareStop(id = "zamora__ka_pedring", zone = "Zamora", name = "Ka Pedring", regularFare = 45.0, discountedFare = 36.0),
        FareStop(id = "zamora__san_vicente_village", zone = "Zamora", name = "San Vicente Village", regularFare = 52.5, discountedFare = 42.0),
        FareStop(id = "zamora__ka_dicong", zone = "Zamora", name = "Ka Dicong", regularFare = 52.5, discountedFare = 42.0),
        FareStop(id = "zamora__ka_pating", zone = "Zamora", name = "Ka Pating", regularFare = 52.5, discountedFare = 42.0),
        FareStop(id = "zamora__ka_lydia", zone = "Zamora", name = "Ka Lydia", regularFare = 52.5, discountedFare = 42.0),
        FareStop(id = "zamora__ka_roping", zone = "Zamora", name = "Ka Roping", regularFare = 52.5, discountedFare = 42.0),
        FareStop(id = "zamora__ka_dito", zone = "Zamora", name = "Ka Dito", regularFare = 60.0, discountedFare = 48.0),
        FareStop(id = "zamora__ka_tolding", zone = "Zamora", name = "Ka Tolding", regularFare = 60.0, discountedFare = 48.0),
        FareStop(id = "zamora__ka_vestre", zone = "Zamora", name = "Ka Vestre", regularFare = 60.0, discountedFare = 48.0),
        FareStop(id = "zamora__ka_helario", zone = "Zamora", name = "Ka Helario", regularFare = 60.0, discountedFare = 48.0),
        FareStop(id = "zamora__bagasaak", zone = "Zamora", name = "Bagasaak", regularFare = 67.5, discountedFare = 54.0),
        FareStop(id = "zamora__tonggo", zone = "Zamora", name = "Tonggo", regularFare = 37.5, discountedFare = 30.0),
        FareStop(id = "zamora__ka_dory", zone = "Zamora", name = "Ka Dory", regularFare = 45.0, discountedFare = 36.0),
        FareStop(id = "zamora__ka_sendoy", zone = "Zamora", name = "Ka Sendoy", regularFare = 45.0, discountedFare = 36.0),
        FareStop(id = "zamora__ka_patring", zone = "Zamora", name = "Ka Patring", regularFare = 52.5, discountedFare = 42.0),
        FareStop(id = "san_carlos__ka_eklat", zone = "San Carlos", name = "Ka Eklat", regularFare = 30.0, discountedFare = 24.0, needsReview = true, confidence = "Low", note = "Photo is sideways/skewed - row/value alignment not fully certain."),
        FareStop(id = "san_carlos__ka_ester", zone = "San Carlos", name = "Ka Ester", regularFare = 30.0, discountedFare = 24.0, needsReview = true, confidence = "Low", note = "Photo is sideways/skewed - row/value alignment not fully certain."),
        FareStop(id = "san_carlos__ver_polo", zone = "San Carlos", name = "Ver Polo", regularFare = 37.5, discountedFare = 30.0, needsReview = true, confidence = "Low", note = "Photo is sideways/skewed - row/value alignment not fully certain."),
        FareStop(id = "san_carlos__centro", zone = "San Carlos", name = "Centro", regularFare = 45.0, discountedFare = 36.0, needsReview = true, confidence = "Low", note = "Photo is sideways/skewed - row/value alignment not fully certain."),
        FareStop(id = "san_carlos__purok_6", zone = "San Carlos", name = "Purok 6", regularFare = 45.0, discountedFare = 36.0, needsReview = true, confidence = "Low", note = "Photo is sideways/skewed - row/value alignment not fully certain."),
        FareStop(id = "san_carlos__purok_3", zone = "San Carlos", name = "Purok 3", regularFare = 45.0, discountedFare = 36.0, needsReview = true, confidence = "Low", note = "Photo is sideways/skewed - row/value alignment not fully certain."),
        FareStop(id = "san_carlos__purok_4", zone = "San Carlos", name = "Purok 4", regularFare = 37.5, discountedFare = 30.0, needsReview = true, confidence = "Low", note = "Photo is sideways/skewed - row/value alignment not fully certain."),
        FareStop(id = "san_carlos__purok_5", zone = "San Carlos", name = "Purok 5", regularFare = 45.0, discountedFare = 36.0, needsReview = true, confidence = "Low", note = "Photo is sideways/skewed - row/value alignment not fully certain."),
        FareStop(id = "san_carlos__salod_here", zone = "San Carlos", name = "Salod Here", regularFare = 37.5, discountedFare = 30.0, needsReview = true, confidence = "Low", note = "Photo is sideways/skewed - row/value alignment not fully certain."),
        FareStop(id = "san_carlos__nestor_cita", zone = "San Carlos", name = "Nestor Cita", regularFare = 37.5, discountedFare = 30.0, needsReview = true, confidence = "Low", note = "Photo is sideways/skewed - row/value alignment not fully certain."),
        FareStop(id = "tanghaligue__tanghaligue_highway", zone = "Tanghaligue", name = "Tanghaligue Highway", regularFare = 35.0, discountedFare = 28.0, needsReview = true, confidence = "Low", note = "Stop names historically obscured by a binding tag on the laminated sheet; row alignment uncertain due to photo skew."),
        FareStop(id = "tanghaligue__tikoy_mercy", zone = "Tanghaligue", name = "Tikoy/Mercy", regularFare = 70.0, discountedFare = 56.0, needsReview = true, confidence = "Low", note = "Name partly obscured by binding tag."),
        FareStop(id = "tanghaligue__tikoy_hener", zone = "Tanghaligue", name = "Tikoy/Hener", regularFare = 70.0, discountedFare = 56.0, needsReview = true, confidence = "Low", note = "Name partly obscured by binding tag."),
        FareStop(id = "tanghaligue__eskina_pablo_pablo_balay", zone = "Tanghaligue", name = "Eskina Pablo/Pablo Balay", regularFare = 70.0, discountedFare = 56.0, confidence = "Low"),
        FareStop(id = "tanghaligue__centro_ingkoy_terio", zone = "Tanghaligue", name = "Centro/Ingkoy Terio", regularFare = 80.0, discountedFare = 64.0, confidence = "Low"),
        FareStop(id = "tanghaligue__eskina_galingan_inkoy_terio", zone = "Tanghaligue", name = "Eskina Galingan-Inkoy Terio", regularFare = 100.0, discountedFare = 80.0, confidence = "Low"),
        FareStop(id = "tanghaligue__centro_purok2_suba", zone = "Tanghaligue", name = "Centro/Purok2/Suba", regularFare = 70.0, discountedFare = 56.0, confidence = "Low"),
        FareStop(id = "tanghaligue__centro_osoy", zone = "Tanghaligue", name = "Centro/Osoy", regularFare = 100.0, discountedFare = 80.0, confidence = "Low"),
        FareStop(id = "tanghaligue__waka_konek", zone = "Tanghaligue", name = "Waka/Konek", regularFare = 150.0, discountedFare = 120.0, confidence = "Low"),
        FareStop(id = "tanghaligue__eskina_waka_to_vilma", zone = "Tanghaligue", name = "Eskina Waka to Vilma", regularFare = 100.0, discountedFare = 80.0, confidence = "Low"),
        FareStop(id = "tanghaligue__eskina_waka_to_aleg", zone = "Tanghaligue", name = "Eskina Waka to Aleg", regularFare = 70.0, discountedFare = 56.0, confidence = "Low"),
        FareStop(id = "tanghaligue__eskina_waka_to_silay", zone = "Tanghaligue", name = "Eskina Waka to Silay", regularFare = 70.0, discountedFare = 56.0, confidence = "Low"),
        FareStop(id = "tanghaligue__waka_ating", zone = "Tanghaligue", name = "Waka/Ating", regularFare = 80.0, discountedFare = 64.0, confidence = "Low"),
        FareStop(id = "tanghaligue__waka_to_hene_cajes", zone = "Tanghaligue", name = "Waka to Hene Cajes", regularFare = 60.0, discountedFare = 48.0, confidence = "Low"),
        FareStop(id = "san_isidro__penticostal", zone = "San Isidro", name = "Penticostal", regularFare = 20.0, discountedFare = 16.0, confidence = "Medium"),
        FareStop(id = "san_isidro__resty_ricemill", zone = "San Isidro", name = "Resty (Ricemill)", regularFare = 25.0, discountedFare = 20.0, confidence = "Medium"),
        FareStop(id = "san_isidro__domingo_sulod", zone = "San Isidro", name = "Domingo (Sulod)", regularFare = 25.0, discountedFare = 20.0, confidence = "Medium"),
        FareStop(id = "san_isidro__cadanoy_sulod", zone = "San Isidro", name = "Cadanoy (Sulod)", regularFare = 25.0, discountedFare = 20.0, confidence = "Medium"),
        FareStop(id = "san_isidro__penie_sulod", zone = "San Isidro", name = "Penie (Sulod)", regularFare = 25.0, discountedFare = 20.0, confidence = "Medium"),
        FareStop(id = "san_isidro__centro", zone = "San Isidro", name = "Centro", regularFare = 25.0, discountedFare = 20.0, confidence = "Medium"),
        FareStop(id = "san_isidro__nabunturan", zone = "San Isidro", name = "Nabunturan", regularFare = 25.0, discountedFare = 20.0, confidence = "Medium"),
        FareStop(id = "san_isidro__san_isidro_high_school", zone = "San Isidro", name = "San Isidro High School", regularFare = 25.0, discountedFare = 20.0, confidence = "Medium"),
        FareStop(id = "san_isidro__eduardo_arcana_sulod", zone = "San Isidro", name = "Eduardo Arcana (Sulod)", regularFare = 30.0, discountedFare = 24.0, confidence = "Medium"),
        FareStop(id = "san_isidro__kahayag_bbc", zone = "San Isidro", name = "Kahayag BBC", regularFare = 25.0, discountedFare = 20.0, confidence = "Medium"),
        FareStop(id = "san_isidro__luis_bebie", zone = "San Isidro", name = "Luis Bebie", regularFare = 30.0, discountedFare = 24.0, confidence = "Medium"),
        FareStop(id = "san_isidro__eskina_rudy_minda_sulod", zone = "San Isidro", name = "Eskina Rudy-Minda (Sulod)", regularFare = 25.0, discountedFare = 20.0, confidence = "Medium"),
        FareStop(id = "san_isidro__rudy_minda_sulod", zone = "San Isidro", name = "Rudy-Minda (Sulod)", regularFare = 25.0, discountedFare = 20.0, confidence = "Medium"),
        FareStop(id = "san_isidro__acasia", zone = "San Isidro", name = "Acasia", regularFare = 25.0, discountedFare = 20.0, needsReview = true, confidence = "Low", note = "From here on, row/value alignment gets harder to confirm - page skew and glare."),
        FareStop(id = "san_isidro__tpc", zone = "San Isidro", name = "TPC", regularFare = 25.0, discountedFare = 20.0, confidence = "Low"),
        FareStop(id = "san_isidro__jbc", zone = "San Isidro", name = "JBC", regularFare = 25.0, discountedFare = 20.0, confidence = "Low"),
        FareStop(id = "san_isidro__new_ihawan", zone = "San Isidro", name = "New Ihawan", regularFare = 30.0, discountedFare = 24.0, confidence = "Low"),
        FareStop(id = "san_isidro__muslim_area", zone = "San Isidro", name = "Muslim Area", regularFare = 25.0, discountedFare = 20.0, confidence = "Low"),
        FareStop(id = "san_isidro__cogao", zone = "San Isidro", name = "Cogao", regularFare = 25.0, discountedFare = 20.0, confidence = "Low"),
        FareStop(id = "san_isidro__cogao_chapel", zone = "San Isidro", name = "Cogao Chapel", regularFare = 30.0, discountedFare = 24.0, confidence = "Low"),
        FareStop(id = "san_isidro__cogao_bbc", zone = "San Isidro", name = "Cogao BBC", regularFare = 35.0, discountedFare = 28.0, confidence = "Low"),
        FareStop(id = "san_isidro__cogao_tumoy", zone = "San Isidro", name = "Cogao Tumoy", regularFare = 40.0, discountedFare = 32.0, confidence = "Low"),
        FareStop(id = "san_isidro__samco", zone = "San Isidro", name = "Samco", regularFare = 25.0, discountedFare = 20.0, confidence = "Low"),
        FareStop(id = "san_isidro__adec_sulod", zone = "San Isidro", name = "Adec (Sulod)", regularFare = 30.0, discountedFare = 24.0, confidence = "Low"),
        FareStop(id = "san_isidro__eskina_molit", zone = "San Isidro", name = "Eskina Molit", regularFare = 40.0, discountedFare = 32.0, confidence = "Low"),
        FareStop(id = "san_isidro__mar_auguis", zone = "San Isidro", name = "Mar Auguis", regularFare = 30.0, discountedFare = 32.0, needsReview = true, confidence = "Low", note = "New Rate LOWER than Senior/PWD rate - inverted, consistent with the prior transcription's flag on this same row."),
        FareStop(id = "san_isidro__agree", zone = "San Isidro", name = "Agree", regularFare = 45.0, discountedFare = 36.0, confidence = "Low"),
        FareStop(id = "san_isidro__jr_carmen", zone = "San Isidro", name = "Jr Carmen", regularFare = 40.0, discountedFare = 32.0, confidence = "Low"),
        FareStop(id = "san_isidro__thomas", zone = "San Isidro", name = "Thomas", regularFare = 45.0, discountedFare = 36.0, confidence = "Low"),
        FareStop(id = "san_isidro__melecio_rose", zone = "San Isidro", name = "Melecio-Rose", regularFare = 35.0, discountedFare = 28.0, confidence = "Low"),
        FareStop(id = "san_isidro__titing_garcia", zone = "San Isidro", name = "Titing Garcia", regularFare = 40.0, discountedFare = 32.0, confidence = "Low"),
        FareStop(id = "san_isidro__tayatayan_naupa", zone = "San Isidro", name = "Tayatayan Naupa", regularFare = 0.0, discountedFare = 0.0, active = false, needsReview = true, confidence = "Low", note = "Cut off at edge of photo - no fare visible."),
        FareStop(id = "san_jose__braulio", zone = "San Jose", name = "Braulio", regularFare = 20.0, discountedFare = 16.0, needsReview = true, confidence = "Low", note = "Photo sideways/skewed."),
        FareStop(id = "san_jose__coop", zone = "San Jose", name = "Coop", regularFare = 25.0, discountedFare = 20.0, needsReview = true, confidence = "Low", note = "Photo sideways/skewed."),
        FareStop(id = "san_jose__heyong", zone = "San Jose", name = "Heyong", regularFare = 30.0, discountedFare = 24.0, needsReview = true, confidence = "Low", note = "Photo sideways/skewed."),
        FareStop(id = "san_jose__vilma", zone = "San Jose", name = "Vilma", regularFare = 25.0, discountedFare = 20.0, needsReview = true, confidence = "Low", note = "Photo sideways/skewed."),
        FareStop(id = "san_jose__dayan", zone = "San Jose", name = "Dayan", regularFare = 40.0, discountedFare = 32.0, needsReview = true, confidence = "Low", note = "Photo sideways/skewed."),
        FareStop(id = "san_jose__pat", zone = "San Jose", name = "Pat", regularFare = 25.0, discountedFare = 20.0, needsReview = true, confidence = "Low", note = "Photo sideways/skewed."),
        FareStop(id = "san_jose__talibon_integrated_bus_terminal_tibt", zone = "San Jose", name = "Talibon Integrated Bus Terminal (TIBT)", regularFare = 20.0, discountedFare = 16.0, needsReview = true, confidence = "Low", note = "Photo sideways/skewed."),
        FareStop(id = "san_jose__hospital_gmph", zone = "San Jose", name = "Hospital (GMPH)", regularFare = 20.0, discountedFare = 16.0, needsReview = true, confidence = "Low", note = "Photo sideways/skewed."),
        FareStop(id = "san_jose__galingan_hener", zone = "San Jose", name = "Galingan Hener", regularFare = 30.0, discountedFare = 24.0, needsReview = true, confidence = "Low", note = "Photo sideways/skewed."),
        FareStop(id = "san_jose__pablo", zone = "San Jose", name = "Pablo", regularFare = 35.0, discountedFare = 28.0, needsReview = true, confidence = "Low", note = "Photo sideways/skewed."),
        FareStop(id = "san_jose__panabang", zone = "San Jose", name = "Panabang", regularFare = 30.0, discountedFare = 24.0, needsReview = true, confidence = "Low", note = "Photo sideways/skewed."),
        FareStop(id = "san_jose__eklat", zone = "San Jose", name = "Eklat", regularFare = 35.0, discountedFare = 28.0, needsReview = true, confidence = "Low", note = "Photo sideways/skewed."),
        FareStop(id = "san_jose__lupa", zone = "San Jose", name = "Lupa", regularFare = 25.0, discountedFare = 20.0, needsReview = true, confidence = "Low", note = "Photo sideways/skewed."),
        FareStop(id = "san_jose__centro_fatima", zone = "San Jose", name = "Centro Fatima", regularFare = 35.0, discountedFare = 28.0, needsReview = true, confidence = "Low", note = "Photo sideways/skewed."),
        FareStop(id = "san_pedro__pandan", zone = "San Pedro", name = "Pandan", regularFare = 20.0, discountedFare = 16.0, confidence = "Medium"),
        FareStop(id = "san_pedro__garcia_street", zone = "San Pedro", name = "Garcia Street", regularFare = 25.0, discountedFare = 16.0, needsReview = true, confidence = "Medium", note = "Rate jump (16 -> 25) is a larger multiple than typical for this zone's other rows - double check."),
        FareStop(id = "san_pedro__centro", zone = "San Pedro", name = "Centro", regularFare = 20.0, discountedFare = 16.0, confidence = "Medium"),
        FareStop(id = "san_pedro__sitio_seawall_pasulod", zone = "San Pedro", name = "Sitio Seawall (Pasulod)", regularFare = 20.0, discountedFare = 20.0, needsReview = true, confidence = "Medium", note = "Senior/PWD rate equals New Rate - no discount applied, unusual versus rest of sheet."),
        FareStop(id = "san_pedro__sitio_dagohoy_kalsada", zone = "San Pedro", name = "Sitio Dagohoy (Kalsada)", regularFare = 25.0, discountedFare = 16.0, confidence = "Medium"),
        FareStop(id = "san_pedro__sitio_dagohoy_pasulod", zone = "San Pedro", name = "Sitio Dagohoy (Pasulod)", regularFare = 20.0, discountedFare = 20.0, needsReview = true, confidence = "Medium", note = "Senior/PWD rate equals New Rate - no discount applied, unusual versus rest of sheet."),
        FareStop(id = "san_pedro__barangay_hall", zone = "San Pedro", name = "Barangay Hall", regularFare = 25.0, discountedFare = 16.0, confidence = "Medium"),
        FareStop(id = "san_pedro__dagohoy_beach", zone = "San Pedro", name = "Dagohoy Beach", regularFare = 20.0, discountedFare = 16.0, confidence = "Medium"),
        FareStop(id = "san_pedro__spv", zone = "San Pedro", name = "SPV", regularFare = 25.0, discountedFare = 20.0, confidence = "Medium"),
        FareStop(id = "san_pedro__nay_saya_sulod", zone = "San Pedro", name = "Nay Saya (Sulod)", regularFare = 25.0, discountedFare = 20.0, confidence = "Medium"),
        FareStop(id = "san_pedro__boy_helen_sulod", zone = "San Pedro", name = "Boy Helen (Sulod)", regularFare = 25.0, discountedFare = 20.0, confidence = "Medium"),
        FareStop(id = "san_roque__merideth", zone = "San Roque", name = "Merideth", regularFare = 40.0, discountedFare = 32.0),
        FareStop(id = "san_roque__gelyn_romy", zone = "San Roque", name = "Gelyn/Romy", regularFare = 40.0, discountedFare = 32.0),
        FareStop(id = "san_roque__gabriel_dina", zone = "San Roque", name = "Gabriel & Dina", regularFare = 40.0, discountedFare = 36.0),
        FareStop(id = "san_roque__dancy", zone = "San Roque", name = "Dancy", regularFare = 45.0, discountedFare = 56.0, needsReview = true, note = "New Rate LOWER than Senior/PWD rate - inverted."),
        FareStop(id = "san_roque__dina_gabril", zone = "San Roque", name = "Dina/Gabril", regularFare = 0.0, discountedFare = 0.0, active = false, needsReview = true, note = "Source shows New Rate = 740.00, which breaks pattern against every neighboring row (all P20-100). Almost certainly a typo/misprint. Do not use this value without verifying the original ordinance."),
        FareStop(id = "san_roque__me_me", zone = "San Roque", name = "Me-Me", regularFare = 50.0, discountedFare = 42.0),
        FareStop(id = "san_roque__roy", zone = "San Roque", name = "Roy", regularFare = 60.0, discountedFare = 48.0),
        FareStop(id = "san_roque__lapas_ka_roy_to_centro_liaya", zone = "San Roque", name = "Lapas ka Roy to Centro Liaya", regularFare = 40.0, discountedFare = 32.0),
        FareStop(id = "san_roque__centro_to_victor", zone = "San Roque", name = "Centro to Victor", regularFare = 50.0, discountedFare = 40.0),
        FareStop(id = "san_roque__emiliana_edie_edwin", zone = "San Roque", name = "Emiliana/Edie/Edwin", regularFare = 50.0, discountedFare = 40.0),
        FareStop(id = "san_roque__latayan_dam", zone = "San Roque", name = "Latayan/Dam", regularFare = 60.0, discountedFare = 48.0),
        FareStop(id = "san_roque__arlen_to_centro_special_trip", zone = "San Roque", name = "Arlen to Centro (Special Trip)", regularFare = 60.0, discountedFare = 80.0, needsReview = true, note = "New Rate LOWER than Senior/PWD rate - inverted."),
        FareStop(id = "san_roque__punso_vicenta_basurahan", zone = "San Roque", name = "Punso/Vicenta/Basurahan", regularFare = 100.0, discountedFare = 56.0),
        FareStop(id = "san_roque__edig_modesta_centro_arba", zone = "San Roque", name = "Edig/Modesta/Centro Arba", regularFare = 70.0, discountedFare = 64.0),
        FareStop(id = "san_roque__zesa", zone = "San Roque", name = "Zesa", regularFare = 0.0, discountedFare = 80.0, active = false, needsReview = true, note = "New Rate cut off at bottom of photo.")
    )
}
```

#### `app/src/main/java/com/tpc/trikride/utils/LicenceImage.kt`

```kotlin
package com.tpc.trikride.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream

/**
 * The photograph of a driver's licence, sized so an administrator can read it.
 *
 * This is the same trick as [ProfilePhoto] — base64 in the Realtime Database,
 * because Cloud Storage needs the paid plan — but with different targets. An
 * avatar only has to look like the person at 96dp. A licence has to be legible:
 * the administrator is checking the number, the name and the expiry date
 * against what the driver typed, and a 256-pixel thumbnail cannot carry that.
 *
 * So the long edge goes to 1280 pixels and the budget rises to about 200 KB.
 * Aspect ratio is kept, because a licence is a card and squaring it off would
 * cut the ends of the number.
 */
object LicenceImage {

    /** Enough to read a licence number photographed at arm's length. */
    private const val TARGET_LONG_EDGE = 1280

    /** Compression is retried down this ladder until the result fits. */
    private val QUALITY_STEPS = intArrayOf(80, 70, 60, 50, 40)

    /** Roughly 200 KB of base64, which is about 150 KB of JPEG. */
    private const val MAX_ENCODED_BYTES = 200_000

    /**
     * Reads the image at [uri], scales its long edge to [TARGET_LONG_EDGE], and
     * returns it as a base64 JPEG. Returns null if the image cannot be read, or
     * cannot be compressed small enough to store.
     */
    fun encode(context: Context, uri: Uri): String? {
        val source = decodeScaled(context, uri) ?: return null
        val scaled = scaleToLongEdge(source)
        if (scaled !== source) source.recycle()

        for (quality in QUALITY_STEPS) {
            val bytes = ByteArrayOutputStream().use { out ->
                scaled.compress(Bitmap.CompressFormat.JPEG, quality, out)
                out.toByteArray()
            }
            val encoded = Base64.encodeToString(bytes, Base64.NO_WRAP)
            if (encoded.length <= MAX_ENCODED_BYTES) {
                scaled.recycle()
                return encoded
            }
        }
        scaled.recycle()
        return null
    }

    fun decode(data: String?): ImageBitmap? {
        if (data.isNullOrBlank()) return null
        return try {
            val bytes = Base64.decode(data, Base64.NO_WRAP)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Loads at a reduced sample size first. A phone camera photograph of a
     * licence is several thousand pixels wide, and decoding that at full size
     * before shrinking it is how an app runs out of memory.
     */
    private fun decodeScaled(context: Context, uri: Uri): Bitmap? {
        // decodeStream returns null whenever inJustDecodeBounds is set — that is
        // its contract, not a failure — so whether the image could be read has
        // to be judged from the size it wrote into the options. Testing the
        // return value here instead rejects every image ever chosen.
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, bounds)
        }
        if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null

        var sample = 1
        while (maxOf(bounds.outWidth, bounds.outHeight) / sample > TARGET_LONG_EDGE * 2) {
            sample *= 2
        }

        val options = BitmapFactory.Options().apply { inSampleSize = sample }
        return context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, options)
        }
    }

    /** Never enlarges: a small photograph stays small rather than going soft. */
    private fun scaleToLongEdge(bitmap: Bitmap): Bitmap {
        val longEdge = maxOf(bitmap.width, bitmap.height)
        if (longEdge <= TARGET_LONG_EDGE) return bitmap
        val ratio = TARGET_LONG_EDGE.toFloat() / longEdge
        return Bitmap.createScaledBitmap(
            bitmap,
            (bitmap.width * ratio).toInt().coerceAtLeast(1),
            (bitmap.height * ratio).toInt().coerceAtLeast(1),
            true
        )
    }
}
```

#### `app/src/main/java/com/tpc/trikride/utils/LocationProvider.kt`

```kotlin
package com.tpc.trikride.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.tpc.trikride.models.Location
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * The device's own position, from the fused location provider.
 *
 * Nothing here is billed. Reading GPS is a platform capability; only the
 * mapping and routing web services carry a price, and the app uses neither.
 *
 * Updates are delivered while a screen is collecting and stop when it is not,
 * because the flow removes its callback on cancellation. There is no background
 * service and no background location permission: a driver's position is
 * published only while the app is open and they are online. That is a
 * deliberate limit — background tracking would need a foreground service and a
 * persistent notification, and it is not worth the battery or the intrusion for
 * a pilot.
 */
object LocationProvider {

    private const val UPDATE_INTERVAL_MS = 5_000L
    private const val FASTEST_INTERVAL_MS = 3_000L

    fun hasPermission(context: Context): Boolean =
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED

    /** A stream of positions, updating roughly every five seconds. */
    @SuppressLint("MissingPermission")
    fun updates(context: Context): Flow<Location> = callbackFlow {
        if (!hasPermission(context)) {
            close()
            return@callbackFlow
        }

        val client = LocationServices.getFusedLocationProviderClient(context)
        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, UPDATE_INTERVAL_MS)
            .setMinUpdateIntervalMillis(FASTEST_INTERVAL_MS)
            .build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { fix ->
                    trySend(
                        Location(
                            latitude = fix.latitude,
                            longitude = fix.longitude,
                            timestamp = System.currentTimeMillis().toString()
                        )
                    )
                }
            }
        }

        client.requestLocationUpdates(request, callback, Looper.getMainLooper())
        awaitClose { client.removeLocationUpdates(callback) }
    }

    /** A single position, or null if permission is missing or no fix is available. */
    @SuppressLint("MissingPermission")
    suspend fun current(context: Context): Location? {
        if (!hasPermission(context)) return null
        val client = LocationServices.getFusedLocationProviderClient(context)
        return suspendCancellableCoroutine { cont ->
            client.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { fix ->
                    cont.resume(
                        fix?.let {
                            Location(
                                latitude = it.latitude,
                                longitude = it.longitude,
                                timestamp = System.currentTimeMillis().toString()
                            )
                        }
                    )
                }
                .addOnFailureListener { cont.resume(null) }
        }
    }
}
```

#### `app/src/main/java/com/tpc/trikride/utils/LocationUtils.kt`

```kotlin
package com.tpc.trikride.utils

import com.tpc.trikride.models.Location
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object LocationUtils {

    private const val EARTH_RADIUS_KM = 6371.0

    /** Great-circle distance between two points in kilometers (haversine formula). */
    fun distanceKm(from: Location, to: Location): Double {
        val latDistance = Math.toRadians(to.latitude - from.latitude)
        val lonDistance = Math.toRadians(to.longitude - from.longitude)

        val a = sin(latDistance / 2) * sin(latDistance / 2) +
                cos(Math.toRadians(from.latitude)) * cos(Math.toRadians(to.latitude)) *
                sin(lonDistance / 2) * sin(lonDistance / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return EARTH_RADIUS_KM * c
    }

    /** Finds the nearest location in [candidates] to [target], or null if empty. */
    fun nearest(target: Location, candidates: List<Location>): Location? =
        candidates.minByOrNull { distanceKm(target, it) }
}
```

#### `app/src/main/java/com/tpc/trikride/utils/Navigation.kt`

```kotlin
package com.tpc.trikride.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import com.tpc.trikride.models.Location
import java.util.Locale

/**
 * Hands a point over to a navigation app the driver already has.
 *
 * Waze publishes no SDK that would let turn-by-turn run inside another app —
 * its Transport SDK is for partners under agreement — and every routing service
 * that could be embedded is billed per request. Handing off is therefore not a
 * compromise on a proper integration; it is the integration that exists. It also
 * costs nothing, needs no key, and gives the driver the app they already know.
 *
 * Both targets need coordinates. A fare stop the administrator has not
 * positioned cannot be navigated to, which is why the buttons that use this are
 * hidden rather than shown broken.
 */
object Navigation {

    const val WAZE = "com.waze"
    const val GOOGLE_MAPS = "com.google.android.apps.maps"

    /**
     * Whether the app is on the phone.
     *
     * Android 11 hides other packages unless they are declared in `<queries>`
     * in the manifest, so both of these are listed there. Without that this
     * returns false for an app that is plainly installed.
     */
    fun isInstalled(context: Context, packageName: String): Boolean =
        try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }

    /** Opens Waze with navigation already started. */
    fun openWaze(context: Context, to: Location): Boolean {
        if (!to.hasCoordinates) return false
        val uri = Uri.parse(
            "https://www.waze.com/ul?ll=%s%%2C%s&navigate=yes".format(
                Locale.US, coordinate(to.latitude), coordinate(to.longitude)
            )
        )
        return launch(context, Intent(Intent.ACTION_VIEW, uri).setPackage(WAZE))
    }

    /** Opens Google Maps in turn-by-turn mode. */
    fun openGoogleMaps(context: Context, to: Location): Boolean {
        if (!to.hasCoordinates) return false
        val uri = Uri.parse(
            "google.navigation:q=%s,%s".format(
                Locale.US, coordinate(to.latitude), coordinate(to.longitude)
            )
        )
        return launch(context, Intent(Intent.ACTION_VIEW, uri).setPackage(GOOGLE_MAPS))
    }

    /**
     * Opens the dialler with the number filled in, without placing the call.
     *
     * ACTION_DIAL rather than ACTION_CALL on purpose: dialling needs no
     * permission and leaves the decision with the person holding the phone,
     * which is the right default when the number belongs to a stranger.
     */
    fun dial(context: Context, phone: String): Boolean {
        val trimmed = phone.filterNot { it.isWhitespace() }
        if (trimmed.isBlank()) return false
        return launch(
            context,
            Intent(Intent.ACTION_DIAL, Uri.parse("tel:$trimmed"))
        )
    }

    private fun coordinate(value: Double) = "%.6f".format(Locale.US, value)

    private fun launch(context: Context, intent: Intent): Boolean = try {
        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        true
    } catch (e: Exception) {
        // Installed a moment ago and gone now, or an install with the launching
        // activity disabled. The caller tells the driver rather than crashing.
        false
    }
}
```

#### `app/src/main/java/com/tpc/trikride/utils/PasswordRules.kt`

```kotlin
package com.tpc.trikride.utils

/** Password strength rules used on the Register screen. */
object PasswordRules {

    data class Check(val label: String, val passed: Boolean)

    fun evaluate(password: String): List<Check> = listOf(
        Check("At least 8 characters", password.length >= 8),
        Check("An uppercase letter (A–Z)", password.any { it.isUpperCase() }),
        Check("A lowercase letter (a–z)", password.any { it.isLowerCase() }),
        Check("A number (0–9)", password.any { it.isDigit() }),
        Check("A symbol (!@#\$…)", password.any { !it.isLetterOrDigit() })
    )

    /** Strong enough when every rule except the (optional) symbol rule passes. */
    fun isStrong(password: String): Boolean {
        val checks = evaluate(password)
        // Require the first four; the symbol is recommended but not mandatory.
        return checks.take(4).all { it.passed }
    }
}
```

#### `app/src/main/java/com/tpc/trikride/utils/PdfChart.kt`

```kotlin
package com.tpc.trikride.utils

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import kotlin.math.ceil
import kotlin.math.max

/**
 * Chart drawing for the exported PDF reports.
 *
 * The reports print on white paper, so everything here is measured against a
 * white surface rather than the app's theme. Marks are thin, gridlines are
 * hairlines a single step off the surface, and every bar carries its own value
 * as a direct label — on paper there is no tooltip to fall back on.
 *
 * Every chart is a single series in one hue. That is not a stylistic choice: a
 * bar chart whose categories have no natural order should use one colour for
 * every bar, because colouring each bar by its own size just re-encodes the
 * length that is already there. It also keeps the palette clear of the
 * colour-blindness problems that come with putting several hues side by side.
 */
object PdfChart {

    // --- Ink -----------------------------------------------------------------
    // Validated against a white surface: the series green clears 3:1 contrast,
    // sits inside the lightness band, and meets the chroma floor.
    const val SERIES = 0xFF16A34A.toInt()
    const val INK_PRIMARY = 0xFF0B0B0B.toInt()
    const val INK_SECONDARY = 0xFF52514E.toInt()
    const val INK_MUTED = 0xFF898781.toInt()
    const val GRIDLINE = 0xFFE1E0D9.toInt()
    const val AXIS = 0xFFC3C2B7.toInt()
    const val SURFACE = Color.WHITE
    const val TILE_FILL = 0xFFF4F7F5.toInt()

    /** A crowded axis holds bars to this width; a sparse one is allowed to grow. */
    private const val BAR_BASE_THICKNESS = 22f
    private const val BAR_MAX_THICKNESS = 54f
    private const val DATA_END_RADIUS = 4f
    private const val SURFACE_GAP = 2f

    /**
     * Bar thickness for a given slot.
     *
     * A chart of four categories across half a landscape page has slots wide
     * enough that a 22-point bar reads as a stray tick rather than a quantity,
     * so thickness rises with the slot up to a ceiling. Crowded axes are
     * unaffected: thirty-one days still land on the base width or below.
     */
    private fun barThickness(slot: Float, floor: Float): Float {
        val target = max(BAR_BASE_THICKNESS, minOf(BAR_MAX_THICKNESS, slot * 0.55f))
        return max(floor, minOf(target, slot - SURFACE_GAP * 2))
    }

    /** Rounding grows a little with the bar, so a wide bar is not squared off. */
    private fun endRadius(thickness: Float) = minOf(6f, max(DATA_END_RADIUS, thickness * 0.14f))

    fun paint(
        color: Int,
        size: Float = 9f,
        bold: Boolean = false,
        align: Paint.Align = Paint.Align.LEFT
    ) = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.color = color
        textSize = size
        textAlign = align
        typeface = Typeface.create(Typeface.SANS_SERIF, if (bold) Typeface.BOLD else Typeface.NORMAL)
    }

    private fun fill(color: Int) = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.color = color
        style = Paint.Style.FILL
    }

    private fun hairline(color: Int) = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.color = color
        style = Paint.Style.STROKE
        strokeWidth = 0.6f
    }

    /** A headline number with its label. The number is the chart. */
    fun statTile(canvas: Canvas, area: RectF, label: String, value: String, note: String? = null) {
        canvas.drawRoundRect(area, 6f, 6f, fill(TILE_FILL))
        val x = area.left + 10f
        canvas.drawText(label.uppercase(), x, area.top + 15f, paint(INK_MUTED, 6.5f, bold = true))
        canvas.drawText(value, x, area.top + 34f, paint(INK_PRIMARY, 17f, bold = true))
        note?.let { canvas.drawText(it, x, area.top + 46f, paint(INK_SECONDARY, 6.5f)) }
    }

    /**
     * Vertical columns over an ordered axis — days of a month, hours of a day.
     *
     * Only every nth tick is labelled when the axis is crowded, because a label
     * on every column is unreadable and a rotated label is worse.
     */
    fun columnChart(
        canvas: Canvas,
        area: RectF,
        title: String,
        subtitle: String,
        values: List<Int>,
        labels: List<String>,
        labelEvery: Int = 1
    ) {
        drawFrameTitle(canvas, area, title, subtitle)
        val plot = plotArea(area)
        if (values.isEmpty() || values.all { it == 0 }) {
            drawEmpty(canvas, plot)
            return
        }

        val maxValue = niceCeiling(values.max())
        drawYGrid(canvas, plot, maxValue)

        val slot = plot.width() / values.size
        val thickness = barThickness(slot, floor = 2f)

        values.forEachIndexed { i, v ->
            if (v <= 0) return@forEachIndexed
            val centre = plot.left + slot * (i + 0.5f)
            val h = plot.height() * (v.toFloat() / maxValue)
            val bar = RectF(centre - thickness / 2, plot.bottom - h, centre + thickness / 2, plot.bottom)
            drawColumn(canvas, bar)
        }

        // Axis labels below the baseline.
        labels.forEachIndexed { i, label ->
            if (i % labelEvery != 0) return@forEachIndexed
            canvas.drawText(
                label,
                plot.left + slot * (i + 0.5f),
                plot.bottom + 11f,
                paint(INK_MUTED, 6.5f, align = Paint.Align.CENTER)
            )
        }
        canvas.drawLine(plot.left, plot.bottom, plot.right, plot.bottom, hairline(AXIS))
    }

    /**
     * Horizontal bars for named categories, longest first. Horizontal because
     * the names are long enough that vertical columns would need rotated
     * labels, and a rotated label is a label nobody reads.
     */
    fun barChart(
        canvas: Canvas,
        area: RectF,
        title: String,
        subtitle: String,
        rows: List<Pair<String, Int>>,
        valueSuffix: String = "",
        axisMax: Int? = null
    ) {
        drawFrameTitle(canvas, area, title, subtitle)
        val plot = plotArea(area, bottomAxis = false)
        if (rows.isEmpty()) {
            drawEmpty(canvas, plot)
            return
        }

        // A percentage chart is measured against its own ceiling, not against
        // the best row, or a field where everyone finished 95% of their rides
        // would draw one full bar and four short ones.
        val maxValue = max(1, axisMax ?: rows.maxOf { it.second })
        val slot = plot.height() / rows.size
        val thickness = barThickness(slot, floor = 3f)
        // Names on the left, values direct-labelled at the end of each bar.
        val nameWidth = plot.width() * 0.42f
        val trackLeft = plot.left + nameWidth + 6f
        val trackWidth = plot.right - trackLeft - 26f

        rows.forEachIndexed { i, (name, value) ->
            val centreY = plot.top + slot * (i + 0.5f)
            val namePaint = paint(INK_SECONDARY, 7f)
            canvas.drawText(
                ellipsize(name, namePaint, nameWidth),
                plot.left, centreY + 2.5f, namePaint
            )

            val w = trackWidth * (value.toFloat() / maxValue)
            if (w > 0.5f) {
                val bar = RectF(trackLeft, centreY - thickness / 2, trackLeft + w, centreY + thickness / 2)
                drawBarRightRounded(canvas, bar)
            }
            canvas.drawText(
                "$value$valueSuffix",
                trackLeft + w + 5f, centreY + 2.5f,
                paint(INK_PRIMARY, 7f, bold = true)
            )
        }
    }

    // --- internals -----------------------------------------------------------

    private fun drawFrameTitle(canvas: Canvas, area: RectF, title: String, subtitle: String) {
        canvas.drawText(title, area.left, area.top + 9f, paint(INK_PRIMARY, 9f, bold = true))
        if (subtitle.isNotBlank()) {
            canvas.drawText(subtitle, area.left, area.top + 19f, paint(INK_MUTED, 6.5f))
        }
    }

    private fun plotArea(area: RectF, bottomAxis: Boolean = true) = RectF(
        area.left,
        area.top + 26f,
        area.right,
        area.bottom - if (bottomAxis) 14f else 0f
    )

    /** Square at the baseline, rounded at the data end. */
    private fun drawColumn(canvas: Canvas, bar: RectF) {
        val r = minOf(endRadius(bar.width()), bar.width() / 2f, bar.height())
        canvas.drawRoundRect(bar, r, r, fill(SERIES))
        canvas.drawRect(
            RectF(bar.left, bar.bottom - r, bar.right, bar.bottom), fill(SERIES)
        )
    }

    private fun drawBarRightRounded(canvas: Canvas, bar: RectF) {
        val r = minOf(endRadius(bar.height()), bar.height() / 2f, bar.width())
        canvas.drawRoundRect(bar, r, r, fill(SERIES))
        canvas.drawRect(RectF(bar.left, bar.top, bar.left + r, bar.bottom), fill(SERIES))
    }

    private fun drawYGrid(canvas: Canvas, plot: RectF, maxValue: Int) {
        val steps = 4
        repeat(steps + 1) { i ->
            val y = plot.bottom - plot.height() * i / steps
            if (i > 0) canvas.drawLine(plot.left, y, plot.right, y, hairline(GRIDLINE))
            canvas.drawText(
                (maxValue * i / steps).toString(),
                plot.left - 4f, y + 2.5f,
                paint(INK_MUTED, 6.5f, align = Paint.Align.RIGHT)
            )
        }
    }

    private fun drawEmpty(canvas: Canvas, plot: RectF) {
        canvas.drawText(
            "No activity in this period",
            plot.centerX(), plot.centerY(),
            paint(INK_MUTED, 7.5f, align = Paint.Align.CENTER)
        )
    }

    /**
     * Picks an axis maximum that divides evenly by the number of gridlines, so
     * the ticks come out as whole round numbers rather than 7, 15, 22, 30.
     */
    private fun niceCeiling(value: Int, steps: Int = 4): Int {
        if (value <= steps) return steps
        val candidates = intArrayOf(
            1, 2, 3, 4, 5, 6, 8, 10, 12, 15, 20, 25, 30, 40, 50, 60, 75,
            100, 125, 150, 200, 250, 300, 400, 500, 750, 1000, 2000, 5000
        )
        val needed = value.toDouble() / steps
        val step = candidates.firstOrNull { it >= needed } ?: ceil(needed / 1000).toInt() * 1000
        return step * steps
    }

    fun ellipsize(text: String, paint: Paint, maxWidth: Float): String {
        if (paint.measureText(text) <= maxWidth) return text
        var cut = text.length
        while (cut > 1 && paint.measureText(text.take(cut) + "…") > maxWidth) cut--
        return text.take(cut) + "…"
    }

    fun textBounds(text: String, paint: Paint): Rect =
        Rect().also { paint.getTextBounds(text, 0, text.length, it) }
}
```

#### `app/src/main/java/com/tpc/trikride/utils/PdfReportWriter.kt`

```kotlin
package com.tpc.trikride.utils

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import com.tpc.trikride.models.Complaint
import com.tpc.trikride.models.ComplaintStatus
import com.tpc.trikride.models.Driver
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.RideStatus
import com.tpc.trikride.models.User
import java.util.Locale
import java.io.OutputStream
import java.util.Calendar

/**
 * Builds the printable reports.
 *
 * Uses Android's own [PdfDocument], so no library and no cost. Pages are Letter
 * landscape at 72 points to the inch — landscape because the ride table has
 * seventeen columns, and squeezing those onto portrait means either a font
 * nobody can read or dropping the columns that make the table worth having.
 *
 * Each report opens with a summary page: the headline figures as stat tiles,
 * then charts, then the detail table paginated behind it with its header
 * repeated on every page. Someone who reads only the first page should come
 * away with the answer; the table is there for whoever wants to check it.
 */
object PdfReportWriter {

    private const val PAGE_WIDTH = 792
    private const val PAGE_HEIGHT = 612
    private const val MARGIN = 36f
    private const val ROW_HEIGHT = 14f
    private const val HEADER_HEIGHT = 16f

    /** Space left of a plot for its y-axis numbers, which hang outside the frame. */
    private const val Y_LABEL_GUTTER = 20f
    private const val COL_GAP = 28f
    private const val ROW_GAP = 26f

    /** Lowest a chart may reach before it runs into the footer. */
    private const val CHART_FLOOR = PAGE_HEIGHT - 34f

    private data class Column(val title: String, val weight: Float, val rightAligned: Boolean = false)

    /**
     * A chart's frame within a grid of them.
     *
     * [yLabels] shifts the left edge inward for charts that number their y-axis,
     * so the numbers land in the gutter instead of over the neighbour.
     */
    private fun cell(
        left: Float,
        top: Float,
        right: Float,
        height: Float,
        yLabels: Boolean = false
    ) = RectF(left + if (yLabels) Y_LABEL_GUTTER else 0f, top, right, top + height)

    /** Splits what is left of the page below [top] into [rows] equal bands. */
    private fun rowHeight(top: Float, rows: Int): Float =
        (CHART_FLOOR - top - ROW_GAP * (rows - 1)) / rows

    // ---------------------------------------------------------------- Rides

    fun writeRideReport(
        out: OutputStream,
        rides: List<Ride>,
        complaints: List<Complaint>,
        usersById: Map<String, User>,
        period: ReportPeriod
    ) {
        val doc = PdfDocument()
        val summary = ReportBuilder.summarise(rides, complaints, period)
        val inPeriod = ReportBuilder.ridesIn(rides, period)

        val page = startPage(doc, 1)
        val canvas = page.canvas
        var y = drawHeader(canvas, "Ride Activity Report", period)

        y = drawStatTiles(
            canvas, y,
            listOf(
                Triple("Total rides", "${summary.totalRides}", null),
                Triple("Completed", "${summary.completed}", completionNote(summary)),
                Triple("Cancelled / no-show", "${summary.cancelled}", null),
                Triple("Gross fares", peso(summary.grossFares), null),
                Triple("Average fare", peso(summary.averageFare), "per completed ride"),
                Triple("Passengers served", "${summary.uniquePassengers}", null),
                Triple("Drivers with a ride", "${summary.activeDrivers}", null)
            )
        )

        // Four charts in two rows: when the demand fell, at what hour, to where,
        // and on which days of the week.
        val perDay = ReportBuilder.ridesPerDay(rides, period)
        val byHour = ReportBuilder.ridesByHour(rides, period)
        val destinations = ReportBuilder.topDestinations(rides, period)
        val byWeekday = ReportBuilder.ridesByWeekday(rides, period)

        val top1 = y + 20f
        val band = rowHeight(top1, rows = 2)
        val top2 = top1 + band + ROW_GAP
        val usable = PAGE_WIDTH - MARGIN * 2

        // Row one leans wide-left: a month of days needs the room, twenty-four
        // hours do not. Row two the same way, for destination names that run long.
        val wide = (usable - COL_GAP) * 0.62f
        val narrowLeft = MARGIN + wide + COL_GAP
        val half = (usable - COL_GAP) * 0.58f
        val weekdayLeft = MARGIN + half + COL_GAP

        PdfChart.columnChart(
            canvas,
            cell(MARGIN, top1, MARGIN + wide, band, yLabels = true),
            title = if (period.bucketsByDay) "Rides per day" else "Rides per month",
            subtitle = "when the demand fell",
            values = perDay.map { it.second },
            labels = perDay.map { it.first },
            labelEvery = if (perDay.size > 16) 3 else 1
        )

        PdfChart.columnChart(
            canvas,
            cell(narrowLeft, top1, PAGE_WIDTH - MARGIN, band, yLabels = true),
            title = "Rides by hour of day",
            subtitle = "when to have drivers waiting",
            values = byHour,
            labels = (0..23).map { "%02d".format(Locale.US, it) },
            labelEvery = 3
        )

        PdfChart.barChart(
            canvas,
            cell(MARGIN, top2, MARGIN + half, band),
            title = "Most-booked destinations",
            subtitle = "busiest first",
            rows = destinations
        )

        PdfChart.columnChart(
            canvas,
            cell(weekdayLeft, top2, PAGE_WIDTH - MARGIN, band, yLabels = true),
            title = "Rides by day of the week",
            subtitle = "which days carry the load",
            values = byWeekday.map { it.second },
            labels = byWeekday.map { it.first }
        )

        drawFooter(canvas, 1)
        doc.finishPage(page)

        // The detail table, from page two.
        val columns = listOf(
            Column("Requested", 1.15f),
            Column("Passenger", 1.2f),
            Column("Driver", 1.2f),
            Column("Pickup", 1.5f),
            Column("Destination", 1.7f),
            Column("Pax", 0.35f, rightAligned = true),
            Column("Party", 1.1f),
            Column("Status", 0.85f),
            Column("Fare", 0.6f, rightAligned = true)
        )
        val rows = inPeriod.map { ride ->
            val passenger = usersById[ride.passengerId]
            val driver = usersById[ride.driverId]
            listOf(
                shortDateTime(ride.requestedAt),
                displayName(passenger),
                if (ride.driverId.isBlank()) "Unassigned" else displayName(driver),
                ride.pickupLocation.address,
                ride.dropoffLocation.address,
                "${ride.passengerCount}",
                ride.partyLabel,
                prettyStatus(ride.status),
                peso(if (ride.actualFare > 0) ride.actualFare else ride.estimatedFare)
            )
        }
        writeTablePages(doc, "Ride Activity Report", period, columns, rows, startingPage = 2)

        doc.writeTo(out)
        doc.close()
    }

    // -------------------------------------------------------------- Drivers

    fun writeDriverReport(
        out: OutputStream,
        rides: List<Ride>,
        drivers: List<Driver>,
        usersById: Map<String, User>,
        period: ReportPeriod
    ) {
        val doc = PdfDocument()
        val inPeriod = ReportBuilder.ridesIn(rides, period).filter { it.driverId.isNotBlank() }
        val byDriver = inPeriod.groupBy { it.driverId }
        val driversById = drivers.associateBy { it.userId }
        val completed = inPeriod.count { it.status == RideStatus.COMPLETED }
        val gross = inPeriod.filter { it.status == RideStatus.COMPLETED }
            .sumOf { if (it.actualFare > 0) it.actualFare else it.estimatedFare }

        val page = startPage(doc, 1)
        val canvas = page.canvas
        var y = drawHeader(canvas, "Driver Performance Report", period)

        y = drawStatTiles(
            canvas, y,
            listOf(
                Triple("Drivers with a ride", "${byDriver.size}", "of ${drivers.size} registered"),
                Triple("Rides accepted", "${inPeriod.size}", null),
                Triple("Completed", "$completed", null),
                Triple("Gross fares", peso(gross), null),
                Triple(
                    "Average per driver",
                    if (byDriver.isEmpty()) "0" else "%.1f".format(Locale.US, inPeriod.size.toDouble() / byDriver.size),
                    "rides"
                )
            )
        )

        // Volume, then money, then whether the rides were actually finished.
        val top1 = y + 20f
        val band = rowHeight(top1, rows = 2)
        val top2 = top1 + band + ROW_GAP
        val usable = PAGE_WIDTH - MARGIN * 2
        val leftWidth = (usable - COL_GAP) * 0.55f
        val rightLeft = MARGIN + leftWidth + COL_GAP

        PdfChart.barChart(
            canvas,
            cell(MARGIN, top1, MARGIN + leftWidth, band),
            title = "Rides accepted per driver",
            subtitle = "busiest first",
            rows = ReportBuilder.ridesPerDriver(rides, usersById, period, limit = 8)
        )

        PdfChart.barChart(
            canvas,
            cell(rightLeft, top1, PAGE_WIDTH - MARGIN, band),
            title = "Gross fares per driver",
            subtitle = "completed rides only, in pesos",
            rows = ReportBuilder.grossPerDriver(rides, usersById, period)
        )

        PdfChart.barChart(
            canvas,
            cell(MARGIN, top2, MARGIN + leftWidth, band),
            title = "Completion rate per driver",
            subtitle = "share of accepted rides finished, three rides or more",
            rows = ReportBuilder.completionRatePerDriver(rides, usersById, period),
            valueSuffix = "%",
            axisMax = 100
        )

        val byApproval = ReportBuilder.driversByVerification(drivers)
        PdfChart.columnChart(
            canvas,
            cell(rightLeft, top2, PAGE_WIDTH - MARGIN, band, yLabels = true),
            title = "Registered fleet by approval state",
            subtitle = "the whole roster as it stands today",
            values = byApproval.map { it.second },
            labels = byApproval.map { it.first }
        )

        drawFooter(canvas, 1)
        doc.finishPage(page)

        val columns = listOf(
            Column("Driver", 1.5f),
            Column("Mobile", 1.0f),
            Column("Tricycle", 0.9f),
            Column("Verification", 0.9f),
            Column("Accepted", 0.6f, rightAligned = true),
            Column("Completed", 0.7f, rightAligned = true),
            Column("Cancelled", 0.7f, rightAligned = true),
            Column("Gross", 0.8f, rightAligned = true),
            Column("Rating", 0.55f, rightAligned = true)
        )
        val rows = byDriver.entries.sortedByDescending { it.value.size }.map { (id, driverRides) ->
            val user = usersById[id]
            val record = driversById[id]
            val done = driverRides.filter { it.status == RideStatus.COMPLETED }
            val g = done.sumOf { if (it.actualFare > 0) it.actualFare else it.estimatedFare }
            listOf(
                displayName(user),
                user?.phoneNumber.orEmpty(),
                record?.tricycleNumber.orEmpty(),
                record?.verificationStatus?.name?.lowercase()?.replaceFirstChar { it.uppercase() }.orEmpty(),
                "${driverRides.size}",
                "${done.size}",
                "${driverRides.count { it.status == RideStatus.CANCELLED || it.status == RideStatus.NO_SHOW }}",
                peso(g),
                "%.1f".format(Locale.US, record?.rating ?: 0.0)
            )
        }
        writeTablePages(doc, "Driver Performance Report", period, columns, rows, startingPage = 2)

        doc.writeTo(out)
        doc.close()
    }

    // ------------------------------------------------------------- Concerns

    fun writeConcernReport(
        out: OutputStream,
        complaints: List<Complaint>,
        usersById: Map<String, User>,
        period: ReportPeriod
    ) {
        val doc = PdfDocument()
        val inPeriod = ReportBuilder.complaintsIn(complaints, period)

        val page = startPage(doc, 1)
        val canvas = page.canvas
        var y = drawHeader(canvas, "Concerns and Complaints Report", period)

        val resolved = inPeriod.count { it.status == ComplaintStatus.RESOLVED }
        y = drawStatTiles(
            canvas, y,
            listOf(
                Triple("Filed", "${inPeriod.size}", null),
                Triple("Resolved", "$resolved", resolutionNote(inPeriod.size, resolved)),
                Triple("In review", "${inPeriod.count { it.status == ComplaintStatus.IN_REVIEW }}", null),
                Triple("Still open", "${inPeriod.count { it.status == ComplaintStatus.OPEN }}", null)
            )
        )

        val top1 = y + 20f
        val band = rowHeight(top1, rows = 2)
        val top2 = top1 + band + ROW_GAP
        val usable = PAGE_WIDTH - MARGIN * 2
        val leftWidth = (usable - COL_GAP) * 0.58f
        val rightLeft = MARGIN + leftWidth + COL_GAP
        val perMonth = concernsPerMonth(inPeriod)

        PdfChart.barChart(
            canvas,
            cell(MARGIN, top1, MARGIN + leftWidth, band),
            title = "What was reported",
            subtitle = "by category, most common first",
            rows = ReportBuilder.concernsByCategory(complaints, period)
        )

        PdfChart.columnChart(
            canvas,
            cell(rightLeft, top1, PAGE_WIDTH - MARGIN, band, yLabels = true),
            title = "Concerns per month",
            subtitle = "when they were filed",
            values = perMonth.map { it.second },
            labels = perMonth.map { it.first }
        )

        val buckets = ReportBuilder.resolutionTimeBuckets(complaints, period)
        PdfChart.columnChart(
            canvas,
            cell(MARGIN, top2, MARGIN + leftWidth, band, yLabels = true),
            title = "How long a concern took to close",
            subtitle = "resolved concerns only; open ones have no duration yet",
            values = buckets.map { it.second },
            labels = buckets.map { it.first }
        )

        val byReporter = ReportBuilder.concernsByReporter(complaints, period)
        PdfChart.columnChart(
            canvas,
            cell(rightLeft, top2, PAGE_WIDTH - MARGIN, band, yLabels = true),
            title = "Who raised them",
            subtitle = "passengers and drivers both file here",
            values = byReporter.map { it.second },
            labels = byReporter.map { it.first }
        )

        drawFooter(canvas, 1)
        doc.finishPage(page)

        val columns = listOf(
            Column("Filed", 1.0f),
            Column("Reported by", 1.2f),
            Column("Role", 0.7f),
            Column("Category", 1.1f),
            Column("Description", 2.6f),
            Column("Status", 0.8f),
            Column("Admin note", 1.8f),
            Column("Resolved", 1.0f)
        )
        val rows = inPeriod.map { c ->
            listOf(
                shortDateTime(c.createdAt),
                ReportBuilder.reporterName(c, usersById),
                c.reporterType.name.lowercase().replaceFirstChar { it.uppercase() },
                c.category,
                c.description,
                c.status.name.replace('_', ' ').lowercase().replaceFirstChar { it.uppercase() },
                c.adminNote,
                shortDateTime(c.resolvedAt)
            )
        }
        writeTablePages(doc, "Concerns and Complaints Report", period, columns, rows, startingPage = 2)

        doc.writeTo(out)
        doc.close()
    }

    // ---------------------------------------------------------------- Parts

    private fun startPage(doc: PdfDocument, number: Int): PdfDocument.Page =
        doc.startPage(PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, number).create())

    private fun drawHeader(canvas: Canvas, title: String, period: ReportPeriod): Float {
        canvas.drawText("TrikRide", MARGIN, MARGIN + 4f, PdfChart.paint(PdfChart.SERIES, 13f, bold = true))
        canvas.drawText(
            "Talibon Polytechnic College",
            MARGIN, MARGIN + 15f, PdfChart.paint(PdfChart.INK_MUTED, 7f)
        )
        canvas.drawText(
            title,
            PAGE_WIDTH - MARGIN, MARGIN + 4f,
            PdfChart.paint(PdfChart.INK_PRIMARY, 13f, bold = true, align = Paint.Align.RIGHT)
        )
        canvas.drawText(
            "${period.label}   ·   generated ${today()}",
            PAGE_WIDTH - MARGIN, MARGIN + 15f,
            PdfChart.paint(PdfChart.INK_SECONDARY, 7f, align = Paint.Align.RIGHT)
        )
        val ruleY = MARGIN + 23f
        canvas.drawLine(
            MARGIN, ruleY, PAGE_WIDTH - MARGIN, ruleY,
            PdfChart.paint(PdfChart.AXIS).apply { strokeWidth = 0.6f }
        )
        return ruleY + 12f
    }

    private fun drawFooter(canvas: Canvas, page: Int) {
        canvas.drawText(
            "Page $page",
            PAGE_WIDTH - MARGIN, PAGE_HEIGHT - 18f,
            PdfChart.paint(PdfChart.INK_MUTED, 6.5f, align = Paint.Align.RIGHT)
        )
        canvas.drawText(
            "Fares follow the schedule published by the Federation of Tricycle Operators " +
                "and Drivers Association of Talibon.",
            MARGIN, PAGE_HEIGHT - 18f, PdfChart.paint(PdfChart.INK_MUTED, 6.5f)
        )
    }

    private fun drawStatTiles(canvas: Canvas, top: Float, tiles: List<Triple<String, String, String?>>): Float {
        val gap = 8f
        val width = (PAGE_WIDTH - MARGIN * 2 - gap * (tiles.size - 1)) / tiles.size
        val height = 54f
        tiles.forEachIndexed { i, (label, value, note) ->
            val left = MARGIN + (width + gap) * i
            PdfChart.statTile(canvas, RectF(left, top, left + width, top + height), label, value, note)
        }
        return top + height
    }

    /** Writes the detail table across as many pages as it needs. */
    private fun writeTablePages(
        doc: PdfDocument,
        title: String,
        period: ReportPeriod,
        columns: List<Column>,
        rows: List<List<String>>,
        startingPage: Int
    ) {
        if (rows.isEmpty()) return

        val totalWeight = columns.sumOf { it.weight.toDouble() }.toFloat()
        val usable = PAGE_WIDTH - MARGIN * 2
        val widths = columns.map { usable * (it.weight / totalWeight) }

        var index = 0
        var pageNumber = startingPage
        while (index < rows.size) {
            val page = startPage(doc, pageNumber)
            val canvas = page.canvas
            var y = drawHeader(canvas, title, period)
            y += 4f

            canvas.drawText(
                "Detail — ${rows.size} record${if (rows.size == 1) "" else "s"}",
                MARGIN, y, PdfChart.paint(PdfChart.INK_PRIMARY, 9f, bold = true)
            )
            y += 12f

            y = drawTableHeader(canvas, y, columns, widths)

            val bottomLimit = PAGE_HEIGHT - 34f
            while (index < rows.size && y + ROW_HEIGHT < bottomLimit) {
                drawTableRow(canvas, y, columns, widths, rows[index], index)
                y += ROW_HEIGHT
                index++
            }

            drawFooter(canvas, pageNumber)
            doc.finishPage(page)
            pageNumber++
        }
    }

    private fun drawTableHeader(canvas: Canvas, top: Float, columns: List<Column>, widths: List<Float>): Float {
        val fill = PdfChart.paint(PdfChart.TILE_FILL).apply { style = Paint.Style.FILL }
        canvas.drawRect(RectF(MARGIN, top, PAGE_WIDTH - MARGIN, top + HEADER_HEIGHT), fill)
        var x = MARGIN
        columns.forEachIndexed { i, col ->
            val p = PdfChart.paint(
                PdfChart.INK_SECONDARY, 6.5f, bold = true,
                align = if (col.rightAligned) Paint.Align.RIGHT else Paint.Align.LEFT
            )
            canvas.drawText(
                col.title.uppercase(),
                if (col.rightAligned) x + widths[i] - 4f else x + 4f,
                top + 11f, p
            )
            x += widths[i]
        }
        return top + HEADER_HEIGHT
    }

    private fun drawTableRow(
        canvas: Canvas,
        top: Float,
        columns: List<Column>,
        widths: List<Float>,
        row: List<String>,
        index: Int
    ) {
        // A very light band every other row, so the eye can track across a wide
        // table without needing rules between every column.
        if (index % 2 == 1) {
            canvas.drawRect(
                RectF(MARGIN, top, PAGE_WIDTH - MARGIN, top + ROW_HEIGHT),
                PdfChart.paint(0xFFFAFAF8.toInt()).apply { style = Paint.Style.FILL }
            )
        }
        var x = MARGIN
        columns.forEachIndexed { i, col ->
            val p = PdfChart.paint(
                PdfChart.INK_PRIMARY, 7f,
                align = if (col.rightAligned) Paint.Align.RIGHT else Paint.Align.LEFT
            )
            val text = PdfChart.ellipsize(row.getOrElse(i) { "" }, p, widths[i] - 8f)
            canvas.drawText(
                text,
                if (col.rightAligned) x + widths[i] - 4f else x + 4f,
                top + 9.5f, p
            )
            x += widths[i]
        }
    }

    // ----------------------------------------------------------- Formatting

    private fun peso(value: Double) = "P%,.2f".format(Locale.US, value)

    private fun completionNote(s: ReportSummary): String? =
        if (s.totalRides == 0) null
        else "%.0f%% of all rides".format(Locale.US, s.completed * 100.0 / s.totalRides)

    private fun resolutionNote(filed: Int, resolved: Int): String? =
        if (filed == 0) null else "%.0f%% of those filed".format(Locale.US, resolved * 100.0 / filed)

    private fun displayName(user: User?): String = when {
        user == null -> "Unknown"
        else -> listOf(user.firstName, user.lastName).filter { it.isNotBlank() }
            .joinToString(" ").ifBlank { user.email }
    }

    private fun prettyStatus(status: RideStatus): String =
        status.name.replace('_', ' ').lowercase().replaceFirstChar { it.uppercase() }

    private fun shortDateTime(raw: String): String {
        val ms = raw.toLongOrNull() ?: return ""
        val cal = Calendar.getInstance().apply { timeInMillis = ms }
        return "%02d %s %02d:%02d".format(
            Locale.US,
            cal.get(Calendar.DAY_OF_MONTH),
            ReportPeriod.MONTH_NAMES[cal.get(Calendar.MONTH)].take(3),
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE)
        )
    }

    private fun today(): String {
        val cal = Calendar.getInstance()
        return "%d %s %d".format(
            Locale.US,
            cal.get(Calendar.DAY_OF_MONTH),
            ReportPeriod.MONTH_NAMES[cal.get(Calendar.MONTH)],
            cal.get(Calendar.YEAR)
        )
    }

    /**
     * Concerns per month, oldest first. Keyed by year and month rather than
     * month alone: over a year boundary two Januaries would otherwise fall in
     * the same bucket and label.
     */
    private fun concernsPerMonth(complaints: List<Complaint>): List<Pair<String, Int>> {
        val counts = sortedMapOf<Int, Int>()
        complaints.forEach { c ->
            val ms = c.createdAt.toLongOrNull() ?: return@forEach
            val cal = Calendar.getInstance().apply { timeInMillis = ms }
            val key = cal.get(Calendar.YEAR) * 12 + cal.get(Calendar.MONTH)
            counts[key] = (counts[key] ?: 0) + 1
        }
        val spansYears = counts.keys.map { it / 12 }.distinct().size > 1
        return counts.map { (key, n) ->
            val name = ReportPeriod.MONTH_NAMES[key % 12].take(3)
            (if (spansYears) "$name ${"%02d".format(Locale.US, (key / 12) % 100)}" else name) to n
        }
    }
}
```

#### `app/src/main/java/com/tpc/trikride/utils/ProfilePhoto.kt`

```kotlin
package com.tpc.trikride.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream

/**
 * Profile photos, small enough to live in the Realtime Database.
 *
 * Firebase Cloud Storage is the natural home for an image, but new Firebase
 * projects need a paid plan before Storage can be provisioned, and this project
 * runs on the free tier. An avatar shown at 96dp does not need a full-resolution
 * file: squared off at 256 pixels and compressed, it comes to a few kilobytes,
 * which the database holds without complaint.
 *
 * Photos are written to their own top-level node rather than into the user
 * record, so that reading a list of users — which the admin screens do
 * constantly — does not drag every avatar across the network with it.
 */
object ProfilePhoto {

    /** Avatars render at 96dp; 256 square is generous even on a dense screen. */
    private const val TARGET_PX = 256

    /** Compression is retried down this ladder until the result fits. */
    private val QUALITY_STEPS = intArrayOf(70, 55, 40, 25)

    /** Roughly 24 KB of base64, which is about 18 KB of JPEG. */
    private const val MAX_ENCODED_BYTES = 24_000

    /**
     * Reads the image at [uri] at a size suitable for the crop screen.
     *
     * A thousand pixels is far more than the 256 that will be stored, so the
     * crop is chosen against a faithful preview while still costing a few
     * megabytes rather than the forty a full camera frame would.
     */
    fun loadForCrop(context: Context, uri: Uri): Bitmap? = decodeScaled(context, uri)

    /**
     * Squares, shrinks and compresses an already-chosen region.
     *
     * The caller owns [bitmap] and it is not recycled here — it is usually
     * still on screen behind the confirmation.
     */
    fun encodeBitmap(bitmap: Bitmap): String? {
        val square = cropToSquare(bitmap)
        val scaled = Bitmap.createScaledBitmap(square, TARGET_PX, TARGET_PX, true)
        if (square !== bitmap && square !== scaled) square.recycle()

        for (quality in QUALITY_STEPS) {
            val bytes = ByteArrayOutputStream().use { out ->
                scaled.compress(Bitmap.CompressFormat.JPEG, quality, out)
                out.toByteArray()
            }
            val encoded = Base64.encodeToString(bytes, Base64.NO_WRAP)
            if (encoded.length <= MAX_ENCODED_BYTES) {
                if (scaled !== bitmap) scaled.recycle()
                return encoded
            }
        }
        if (scaled !== bitmap) scaled.recycle()
        return null
    }

    fun decode(data: String?): ImageBitmap? {
        if (data.isNullOrBlank()) return null
        return try {
            val bytes = Base64.decode(data, Base64.NO_WRAP)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Loads the image at a reduced sample size. A modern phone camera produces
     * something in the region of 4000 pixels wide, and decoding that at full
     * size to make a 256-pixel thumbnail is how an app runs out of memory.
     */
    private fun decodeScaled(context: Context, uri: Uri): Bitmap? {
        // decodeStream returns null whenever inJustDecodeBounds is set — that is
        // its contract, not a failure — so whether the image could be read has
        // to be judged from the size it wrote into the options. Testing the
        // return value here instead rejects every image ever chosen.
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, bounds)
        }
        if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null

        var sample = 1
        while (bounds.outWidth / sample > TARGET_PX * 2 &&
            bounds.outHeight / sample > TARGET_PX * 2
        ) {
            sample *= 2
        }

        val options = BitmapFactory.Options().apply { inSampleSize = sample }
        return context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, options)
        }
    }

    private fun cropToSquare(bitmap: Bitmap): Bitmap {
        val side = minOf(bitmap.width, bitmap.height)
        if (bitmap.width == bitmap.height) return bitmap
        val x = (bitmap.width - side) / 2
        val y = (bitmap.height - side) / 2
        return Bitmap.createBitmap(bitmap, x, y, side, side)
    }
}
```

#### `app/src/main/java/com/tpc/trikride/utils/ReportBuilder.kt`

```kotlin
package com.tpc.trikride.utils

import com.tpc.trikride.models.Complaint
import com.tpc.trikride.models.ComplaintStatus
import com.tpc.trikride.models.Driver
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.RideStatus
import com.tpc.trikride.models.User
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

/** The stretch of time a report covers. */
sealed class ReportPeriod {
    data class Month(val year: Int, val month: Int) : ReportPeriod()
    data class Year(val year: Int) : ReportPeriod()
    data object AllTime : ReportPeriod()

    /**
     * Any two dates the admin picks, both days included.
     *
     * [startMillis] and [endMillis] are stored as the first and last instant of
     * the chosen days rather than as whatever midnight the picker handed back.
     * A range whose end is midnight would leave out everything that happened on
     * the last day, which is not what anyone means by "up to the 15th".
     */
    data class Custom(val startMillis: Long, val endMillis: Long) : ReportPeriod()

    val label: String
        get() = when (this) {
            is Month -> "${MONTH_NAMES[month]} $year"
            is Year -> "$year"
            AllTime -> "All time"
            is Custom -> "${shortDate(startMillis)} to ${shortDate(endMillis)}"
        }

    /** Safe for a filename on any platform the admin might open this on. */
    val slug: String
        get() = when (this) {
            is Month -> "%04d-%02d".format(Locale.US, year, month + 1)
            is Year -> "%04d".format(Locale.US, year)
            AllTime -> "all-time"
            is Custom -> "${fileDate(startMillis)}-to-${fileDate(endMillis)}"
        }

    /**
     * Whether a chart of this period should have one bar per day or per month.
     *
     * A month is always daily. A custom range follows its own length: two
     * months of days is a readable axis, a year of them is a smear.
     */
    val bucketsByDay: Boolean
        get() = when (this) {
            is Month -> true
            is Custom -> endMillis - startMillis <= 62L * 86_400_000L
            else -> false
        }

    fun contains(epochMillis: Long): Boolean {
        if (this is AllTime) return true
        if (this is Custom) return epochMillis in startMillis..endMillis
        val cal = Calendar.getInstance().apply { timeInMillis = epochMillis }
        return when (this) {
            is Month -> cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) == month
            is Year -> cal.get(Calendar.YEAR) == year
            else -> true
        }
    }

    companion object {
        val MONTH_NAMES = listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )

        private fun shortDate(ms: Long): String {
            val cal = Calendar.getInstance().apply { timeInMillis = ms }
            return "%d %s %d".format(Locale.US, 
                cal.get(Calendar.DAY_OF_MONTH),
                MONTH_NAMES[cal.get(Calendar.MONTH)].take(3),
                cal.get(Calendar.YEAR)
            )
        }

        private fun fileDate(ms: Long): String {
            val cal = Calendar.getInstance().apply { timeInMillis = ms }
            return "%04d%02d%02d".format(Locale.US, 
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH)
            )
        }

        /**
         * Turns two dates chosen in the date picker into a range that covers
         * both days in full, in the device's own time zone.
         *
         * Material's picker reports a selection as midnight UTC on the chosen
         * day, so the calendar date is read back in UTC and only then rebuilt
         * locally. Treating the picker's instant as a local one moves the range
         * a day in any zone behind UTC. A pair chosen back to front still means
         * the days between them, so it is ordered first.
         */
        fun customRange(firstPickedUtc: Long, secondPickedUtc: Long): Custom {
            val from = minOf(firstPickedUtc, secondPickedUtc)
            val to = maxOf(firstPickedUtc, secondPickedUtc)
            return Custom(localDay(from, endOfDay = false), localDay(to, endOfDay = true))
        }

        private fun localDay(pickedUtc: Long, endOfDay: Boolean): Long {
            val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                timeInMillis = pickedUtc
            }
            return Calendar.getInstance().apply {
                clear()
                set(
                    utc.get(Calendar.YEAR),
                    utc.get(Calendar.MONTH),
                    utc.get(Calendar.DAY_OF_MONTH),
                    if (endOfDay) 23 else 0,
                    if (endOfDay) 59 else 0,
                    if (endOfDay) 59 else 0
                )
                set(Calendar.MILLISECOND, if (endOfDay) 999 else 0)
            }.timeInMillis
        }
    }
}

/** Headline numbers shown above the export buttons. */
data class ReportSummary(
    val totalRides: Int = 0,
    val completed: Int = 0,
    val cancelled: Int = 0,
    val inProgress: Int = 0,
    val grossFares: Double = 0.0,
    val averageFare: Double = 0.0,
    val uniquePassengers: Int = 0,
    val activeDrivers: Int = 0,
    val complaintsFiled: Int = 0,
    val complaintsResolved: Int = 0
)

/**
 * Turns the admin's live data into month or year reports.
 *
 * Everything is written as CSV because that is what opens without argument in
 * Excel, Google Sheets and LibreOffice, which is where a capstone panel or the
 * drivers' association is going to want the numbers.
 *
 * Timestamps throughout the app are epoch milliseconds held as strings, so
 * anything unparseable is treated as outside every period rather than being
 * silently bucketed into the current month.
 */
object ReportBuilder {

    private fun millis(value: String): Long? = value.toLongOrNull()

    private fun readable(value: String): String {
        val ms = millis(value) ?: return ""
        val cal = Calendar.getInstance().apply { timeInMillis = ms }
        return "%04d-%02d-%02d %02d:%02d".format(Locale.US, 
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.DAY_OF_MONTH),
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE)
        )
    }

    /**
     * Wraps a value so commas, quotes and newlines inside it survive the round
     * trip, and so that nothing in it is run as a formula when the file opens.
     *
     * Quoting alone does not stop the second problem. Excel, LibreOffice and
     * Sheets all evaluate a cell whose text begins `=`, `+`, `-` or `@` — and
     * `\t` or `\r` before one of those is skipped over — quoted or not, which
     * turns a passenger's ride note or a driver's own name into something that
     * runs on the administrator's computer when they open the report. Prefixing
     * an apostrophe is the standard defence: spreadsheets read it as "this is
     * text", strip it on display, and a plain CSV reader sees one extra
     * character rather than a formula.
     */
    private fun cell(value: Any?): String {
        val text = value?.toString().orEmpty()
        if (text.isEmpty()) return ""
        val neutralised = if (startsFormula(text)) "'$text" else text
        val escaped = neutralised.replace("\"", "\"\"")
        return "\"$escaped\""
    }

    /** Whether a spreadsheet would treat this text as a formula rather than a value. */
    private fun startsFormula(text: String): Boolean {
        val first = text.firstOrNull { it != '\t' && it != '\r' && it != '\n' && it != ' ' }
        return first != null && first in FORMULA_LEADS
    }

    private val FORMULA_LEADS = charArrayOf('=', '+', '-', '@')


    private fun row(vararg values: Any?): String = values.joinToString(",") { cell(it) }

    private fun name(user: User?): String = when {
        user == null -> "Unknown"
        else -> listOf(user.firstName, user.lastName).filter { it.isNotBlank() }
            .joinToString(" ").ifBlank { user.email }
    }

    /**
     * Who filed a concern.
     *
     * The name stored on the complaint is left blank by both the passenger and
     * the driver form, so the account is the only place the name actually is.
     * The stored value is still preferred over "Unknown" for a report covering
     * an account that has since been deleted.
     */
    fun reporterName(complaint: Complaint, usersById: Map<String, User>): String {
        val user = usersById[complaint.reporterId]
        if (user != null) return name(user)
        return complaint.reporterName.ifBlank { "Unknown" }
    }

    /** Which months and years the admin can pick, newest first, from real data. */
    fun availablePeriods(rides: List<Ride>): List<ReportPeriod> {
        val stamps = rides.mapNotNull { millis(it.requestedAt) }
        if (stamps.isEmpty()) return listOf(ReportPeriod.AllTime)

        val months = linkedSetOf<Pair<Int, Int>>()
        val years = linkedSetOf<Int>()
        stamps.sortedDescending().forEach { ms ->
            val cal = Calendar.getInstance().apply { timeInMillis = ms }
            months.add(cal.get(Calendar.YEAR) to cal.get(Calendar.MONTH))
            years.add(cal.get(Calendar.YEAR))
        }

        return buildList {
            add(ReportPeriod.AllTime)
            years.sortedDescending().forEach { add(ReportPeriod.Year(it)) }
            months.sortedWith(compareByDescending<Pair<Int, Int>> { it.first }.thenByDescending { it.second })
                .forEach { (y, m) -> add(ReportPeriod.Month(y, m)) }
        }
    }

    fun ridesIn(rides: List<Ride>, period: ReportPeriod): List<Ride> =
        rides.filter { ride -> millis(ride.requestedAt)?.let { period.contains(it) } == true }
            .sortedBy { millis(it.requestedAt) ?: 0L }

    fun complaintsIn(complaints: List<Complaint>, period: ReportPeriod): List<Complaint> =
        complaints.filter { c -> millis(c.createdAt)?.let { period.contains(it) } == true }
            .sortedBy { millis(it.createdAt) ?: 0L }

    fun summarise(
        rides: List<Ride>,
        complaints: List<Complaint>,
        period: ReportPeriod
    ): ReportSummary {
        val inPeriod = ridesIn(rides, period)
        val done = inPeriod.filter { it.status == RideStatus.COMPLETED }
        val gross = done.sumOf { if (it.actualFare > 0) it.actualFare else it.estimatedFare }
        val filed = complaintsIn(complaints, period)
        return ReportSummary(
            totalRides = inPeriod.size,
            completed = done.size,
            cancelled = inPeriod.count {
                it.status == RideStatus.CANCELLED || it.status == RideStatus.NO_SHOW
            },
            inProgress = inPeriod.count {
                it.status != RideStatus.COMPLETED &&
                    it.status != RideStatus.CANCELLED &&
                    it.status != RideStatus.NO_SHOW
            },
            grossFares = gross,
            averageFare = if (done.isEmpty()) 0.0 else gross / done.size,
            uniquePassengers = inPeriod.map { it.passengerId }.filter { it.isNotBlank() }.toSet().size,
            activeDrivers = inPeriod.map { it.driverId }.filter { it.isNotBlank() }.toSet().size,
            complaintsFiled = filed.size,
            complaintsResolved = filed.count { it.status == ComplaintStatus.RESOLVED }
        )
    }

    /** Every ride in the period, one per line, with a summary block on top. */
    fun ridesCsv(
        rides: List<Ride>,
        complaints: List<Complaint>,
        usersById: Map<String, User>,
        period: ReportPeriod
    ): String {
        val s = summarise(rides, complaints, period)
        val sb = StringBuilder()

        sb.appendLine(row("TrikRide ride activity report"))
        sb.appendLine(row("Talibon Polytechnic College"))
        sb.appendLine(row("Period", period.label))
        sb.appendLine(row("Generated", readable(System.currentTimeMillis().toString())))
        sb.appendLine()
        sb.appendLine(row("Total rides", s.totalRides))
        sb.appendLine(row("Completed", s.completed))
        sb.appendLine(row("Cancelled or no-show", s.cancelled))
        sb.appendLine(row("Still open", s.inProgress))
        sb.appendLine(row("Gross fares (PHP)", "%.2f".format(Locale.US, s.grossFares)))
        sb.appendLine(row("Average completed fare (PHP)", "%.2f".format(Locale.US, s.averageFare)))
        sb.appendLine(row("Passengers served", s.uniquePassengers))
        sb.appendLine(row("Drivers with at least one ride", s.activeDrivers))
        sb.appendLine()

        sb.appendLine(
            row(
                "Ride ID", "Requested", "Accepted", "Started", "Completed",
                "Passenger", "Passenger email", "Driver", "Driver email",
                "Pickup", "Destination", "Passengers", "Luggage",
                "Status", "Estimated fare", "Actual fare", "Notes"
            )
        )
        ridesIn(rides, period).forEach { ride ->
            val passenger = usersById[ride.passengerId]
            val driver = usersById[ride.driverId]
            sb.appendLine(
                row(
                    ride.id,
                    readable(ride.requestedAt),
                    readable(ride.acceptedAt),
                    readable(ride.startedAt),
                    readable(ride.completedAt),
                    name(passenger),
                    passenger?.email.orEmpty(),
                    if (ride.driverId.isBlank()) "Unassigned" else name(driver),
                    driver?.email.orEmpty(),
                    ride.pickupLocation.address,
                    ride.dropoffLocation.address,
                    ride.passengerCount,
                    ride.luggage,
                    ride.status.name,
                    "%.2f".format(Locale.US, ride.estimatedFare),
                    "%.2f".format(Locale.US, ride.actualFare),
                    ride.notes
                )
            )
        }
        return sb.toString()
    }

    /** Per-driver totals for the period, busiest first. */
    fun driversCsv(
        rides: List<Ride>,
        drivers: List<Driver>,
        usersById: Map<String, User>,
        period: ReportPeriod
    ): String {
        val inPeriod = ridesIn(rides, period).filter { it.driverId.isNotBlank() }
        val byDriver = inPeriod.groupBy { it.driverId }
        val driversById = drivers.associateBy { it.userId }

        val sb = StringBuilder()
        sb.appendLine(row("TrikRide driver performance report"))
        sb.appendLine(row("Period", period.label))
        sb.appendLine(row("Generated", readable(System.currentTimeMillis().toString())))
        sb.appendLine()
        sb.appendLine(
            row(
                "Driver", "Email", "Phone", "Tricycle number",
                "Verification", "Rides accepted", "Completed", "Cancelled",
                "Gross fares (PHP)", "Average fare (PHP)", "Rating"
            )
        )

        byDriver.entries
            .sortedByDescending { it.value.size }
            .forEach { (driverId, driverRides) ->
                val user = usersById[driverId]
                val record = driversById[driverId]
                val done = driverRides.filter { it.status == RideStatus.COMPLETED }
                val gross = done.sumOf { if (it.actualFare > 0) it.actualFare else it.estimatedFare }
                sb.appendLine(
                    row(
                        name(user),
                        user?.email.orEmpty(),
                        user?.phoneNumber.orEmpty(),
                        record?.tricycleNumber.orEmpty(),
                        record?.verificationStatus?.name.orEmpty(),
                        driverRides.size,
                        done.size,
                        driverRides.count {
                            it.status == RideStatus.CANCELLED || it.status == RideStatus.NO_SHOW
                        },
                        "%.2f".format(Locale.US, gross),
                        "%.2f".format(Locale.US, if (done.isEmpty()) 0.0 else gross / done.size),
                        "%.1f".format(Locale.US, record?.rating ?: 0.0)
                    )
                )
            }

        val idle = drivers.filter { it.userId !in byDriver.keys }
        if (idle.isNotEmpty()) {
            sb.appendLine()
            sb.appendLine(row("Drivers with no rides in this period"))
            sb.appendLine(row("Driver", "Email", "Tricycle number", "Verification"))
            idle.forEach { record ->
                val user = usersById[record.userId]
                sb.appendLine(
                    row(
                        name(user),
                        user?.email.orEmpty(),
                        record.tricycleNumber,
                        record.verificationStatus.name
                    )
                )
            }
        }
        return sb.toString()
    }

    /** Concerns filed in the period, with how they were handled. */
    fun complaintsCsv(
        complaints: List<Complaint>,
        usersById: Map<String, User>,
        period: ReportPeriod
    ): String {
        val inPeriod = complaintsIn(complaints, period)
        val sb = StringBuilder()
        sb.appendLine(row("TrikRide concerns and complaints report"))
        sb.appendLine(row("Period", period.label))
        sb.appendLine(row("Generated", readable(System.currentTimeMillis().toString())))
        sb.appendLine()
        sb.appendLine(row("Total filed", inPeriod.size))
        sb.appendLine(row("Resolved", inPeriod.count { it.status == ComplaintStatus.RESOLVED }))
        sb.appendLine(row("Open", inPeriod.count { it.status == ComplaintStatus.OPEN }))
        sb.appendLine(row("In review", inPeriod.count { it.status == ComplaintStatus.IN_REVIEW }))
        sb.appendLine()

        val byCategory = inPeriod.groupingBy { it.category }.eachCount()
        if (byCategory.isNotEmpty()) {
            sb.appendLine(row("Category", "Count"))
            byCategory.entries.sortedByDescending { it.value }.forEach { (category, count) ->
                sb.appendLine(row(category, count))
            }
            sb.appendLine()
        }

        sb.appendLine(
            row(
                "Report ID", "Filed", "Resolved", "Filed by", "Account type",
                "Category", "Status", "Description", "Admin note"
            )
        )
        inPeriod.forEach { c ->
            sb.appendLine(
                row(
                    c.id,
                    readable(c.createdAt),
                    readable(c.resolvedAt),
                    reporterName(c, usersById),
                    c.reporterType.name,
                    c.category,
                    c.status.name,
                    c.description,
                    c.adminNote
                )
            )
        }
        return sb.toString()
    }

    // --- Aggregations for the charts in the PDF reports ---------------------

    /** Rides per calendar day across the period, oldest first. */
    fun ridesPerDay(rides: List<Ride>, period: ReportPeriod): List<Pair<String, Int>> {
        val inPeriod = ridesIn(rides, period)
        if (inPeriod.isEmpty()) return emptyList()

        val counts = sortedMapOf<String, Int>()
        val labels = mutableMapOf<String, String>()
        inPeriod.forEach { ride ->
            val ms = millis(ride.requestedAt) ?: return@forEach
            val cal = Calendar.getInstance().apply { timeInMillis = ms }
            val key = "%04d-%02d-%02d".format(Locale.US, 
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH)
            )
            counts[key] = (counts[key] ?: 0) + 1
            // A short period is labelled by day number; a long one by month.
            labels[key] = if (period.bucketsByDay) {
                cal.get(Calendar.DAY_OF_MONTH).toString()
            } else {
                ReportPeriod.MONTH_NAMES[cal.get(Calendar.MONTH)].take(3)
            }
        }

        // Across a year, all time, or a long custom range, collapse to one bar per month.
        if (!period.bucketsByDay) {
            val byMonth = linkedMapOf<String, Int>()
            counts.forEach { (key, n) ->
                val month = key.substring(0, 7)
                byMonth[month] = (byMonth[month] ?: 0) + n
            }
            // A span crossing new year carries the year on the label, or two
            // separate Januaries both read as "Jan".
            val spansYears = byMonth.keys.map { it.substring(0, 4) }.distinct().size > 1
            return byMonth.map { (month, n) ->
                val index = month.substring(5).toInt() - 1
                val name = ReportPeriod.MONTH_NAMES[index].take(3)
                (if (spansYears) "$name ${month.substring(2, 4)}" else name) to n
            }
        }
        return counts.map { (key, n) -> (labels[key] ?: "") to n }
    }

    /** Rides started in each hour of the day, 0 through 23. */
    fun ridesByHour(rides: List<Ride>, period: ReportPeriod): List<Int> {
        val hours = IntArray(24)
        ridesIn(rides, period).forEach { ride ->
            millis(ride.requestedAt)?.let { ms ->
                val cal = Calendar.getInstance().apply { timeInMillis = ms }
                hours[cal.get(Calendar.HOUR_OF_DAY)]++
            }
        }
        return hours.toList()
    }

    /** Rides per weekday, Monday through Sunday. */
    fun ridesByWeekday(rides: List<Ride>, period: ReportPeriod): List<Pair<String, Int>> {
        val order = listOf(
            Calendar.MONDAY to "Mon", Calendar.TUESDAY to "Tue", Calendar.WEDNESDAY to "Wed",
            Calendar.THURSDAY to "Thu", Calendar.FRIDAY to "Fri", Calendar.SATURDAY to "Sat",
            Calendar.SUNDAY to "Sun"
        )
        val counts = mutableMapOf<Int, Int>()
        ridesIn(rides, period).forEach { ride ->
            millis(ride.requestedAt)?.let { ms ->
                val day = Calendar.getInstance().apply { timeInMillis = ms }.get(Calendar.DAY_OF_WEEK)
                counts[day] = (counts[day] ?: 0) + 1
            }
        }
        return order.map { (day, label) -> label to (counts[day] ?: 0) }
    }

    /** The destinations booked most often, busiest first. */
    fun topDestinations(rides: List<Ride>, period: ReportPeriod, limit: Int = 8): List<Pair<String, Int>> =
        ridesIn(rides, period)
            .map { it.dropoffLocation.address }
            .filter { it.isNotBlank() }
            .groupingBy { it }
            .eachCount()
            .entries
            .sortedByDescending { it.value }
            .take(limit)
            .map { it.key to it.value }

    /** Rides accepted per driver, busiest first. */
    fun ridesPerDriver(
        rides: List<Ride>,
        usersById: Map<String, User>,
        period: ReportPeriod,
        limit: Int = 10
    ): List<Pair<String, Int>> =
        ridesIn(rides, period)
            .filter { it.driverId.isNotBlank() }
            .groupingBy { it.driverId }
            .eachCount()
            .entries
            .sortedByDescending { it.value }
            .take(limit)
            .map { name(usersById[it.key]) to it.value }

    /** Gross fares earned per driver, in whole pesos, highest first. */
    fun grossPerDriver(
        rides: List<Ride>,
        usersById: Map<String, User>,
        period: ReportPeriod,
        limit: Int = 8
    ): List<Pair<String, Int>> =
        ridesIn(rides, period)
            .filter { it.driverId.isNotBlank() && it.status == RideStatus.COMPLETED }
            .groupBy { it.driverId }
            .mapValues { (_, list) ->
                list.sumOf { if (it.actualFare > 0) it.actualFare else it.estimatedFare }.toInt()
            }
            .entries
            .sortedByDescending { it.value }
            .take(limit)
            .map { name(usersById[it.key]) to it.value }

    /**
     * Share of each driver's accepted rides that they finished, as a percentage.
     *
     * Drivers below a handful of rides are left out: one cancellation out of two
     * rides reads as 50% and says nothing about the driver.
     */
    fun completionRatePerDriver(
        rides: List<Ride>,
        usersById: Map<String, User>,
        period: ReportPeriod,
        minimumRides: Int = 3,
        limit: Int = 8
    ): List<Pair<String, Int>> =
        ridesIn(rides, period)
            .filter { it.driverId.isNotBlank() }
            .groupBy { it.driverId }
            .filterValues { it.size >= minimumRides }
            .mapValues { (_, list) ->
                (list.count { it.status == RideStatus.COMPLETED } * 100.0 / list.size).toInt()
            }
            .entries
            .sortedByDescending { it.value }
            .take(limit)
            .map { name(usersById[it.key]) to it.value }

    /**
     * The registered fleet by verification state. A snapshot rather than a
     * period figure — a driver's approval is where it stands today, not
     * something that happened inside the reporting month.
     */
    fun driversByVerification(drivers: List<Driver>): List<Pair<String, Int>> {
        val counts = linkedMapOf("Approved" to 0, "Pending" to 0, "Rejected" to 0, "Expired" to 0)
        drivers.forEach { driver ->
            val key = driver.verificationStatus.name.lowercase().replaceFirstChar { it.uppercase() }
            counts[key] = (counts[key] ?: 0) + 1
        }
        return counts.toList()
    }

    /** Concerns filed per category, most common first. */
    fun concernsByCategory(complaints: List<Complaint>, period: ReportPeriod): List<Pair<String, Int>> =
        complaintsIn(complaints, period)
            .groupingBy { it.category }
            .eachCount()
            .entries
            .sortedByDescending { it.value }
            .map { it.key to it.value }

    /** Concerns filed by the role of whoever raised them. */
    fun concernsByReporter(complaints: List<Complaint>, period: ReportPeriod): List<Pair<String, Int>> {
        val counts = linkedMapOf("Passenger" to 0, "Driver" to 0)
        complaintsIn(complaints, period).forEach { c ->
            val key = c.reporterType.name.lowercase().replaceFirstChar { it.uppercase() }
            counts[key] = (counts[key] ?: 0) + 1
        }
        return counts.toList()
    }

    /**
     * How long resolved concerns took, bucketed. Concerns still open are left
     * out rather than dropped into a final bucket: they have no duration yet,
     * and their count is already on the summary tiles.
     */
    fun resolutionTimeBuckets(complaints: List<Complaint>, period: ReportPeriod): List<Pair<String, Int>> {
        val buckets = linkedMapOf(
            "Same day" to 0, "1 day" to 0, "2-3 days" to 0, "4-7 days" to 0, "Over a week" to 0
        )
        complaintsIn(complaints, period).forEach { c ->
            if (c.status != ComplaintStatus.RESOLVED) return@forEach
            val filed = millis(c.createdAt) ?: return@forEach
            val closed = millis(c.resolvedAt) ?: return@forEach
            val days = ((closed - filed) / 86_400_000L).toInt()
            val key = when {
                days < 1 -> "Same day"
                days < 2 -> "1 day"
                days <= 3 -> "2-3 days"
                days <= 7 -> "4-7 days"
                else -> "Over a week"
            }
            buckets[key] = (buckets[key] ?: 0) + 1
        }
        return buckets.map { it.key to it.value }
    }

    fun fileName(kind: String, period: ReportPeriod, extension: String = "csv"): String =
        "trikride-$kind-${period.slug}.$extension".lowercase(Locale.US)
}
```

#### `app/src/main/java/com/tpc/trikride/utils/ReportExporter.kt`

```kotlin
package com.tpc.trikride.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.OutputStream

/**
 * Gets a finished report off the phone, as a PDF or a spreadsheet.
 *
 * Two routes, because admins want different things: "Save" opens the system
 * file picker so the report lands in Downloads or Drive under a name they
 * choose, and "Share" hands it straight to Gmail, Messenger or whatever else
 * they use to send it on. Neither needs a storage permission.
 *
 * Rendering and sending are separate calls on purpose. Drawing a PDF of a busy
 * month is slow enough to freeze the screen, so the caller runs the render on a
 * background thread and starts the chooser back on the main one, which is the
 * only thread allowed to.
 */
object ReportExporter {

    /** Writes [content] to the Uri the system file picker handed back. */
    fun writeTo(context: Context, uri: Uri, content: String): Boolean = try {
        context.contentResolver.openOutputStream(uri)?.use { stream ->
            stream.write(content.toByteArray(Charsets.UTF_8))
        } != null
    } catch (e: Exception) {
        false
    }

    /** Lets a report render itself straight into the chosen file. */
    fun writeTo(context: Context, uri: Uri, render: (OutputStream) -> Unit): Boolean = try {
        context.contentResolver.openOutputStream(uri)?.use(render) != null
    } catch (e: Exception) {
        false
    }

    /** Draws a report into the cache directory, ready to be shared. */
    fun renderToCache(context: Context, fileName: String, render: (OutputStream) -> Unit): File? = try {
        val dir = File(context.cacheDir, "reports").apply { mkdirs() }
        File(dir, fileName).also { file -> file.outputStream().use(render) }
    } catch (e: Exception) {
        null
    }

    /** Drops a text report in the cache and opens the share sheet for it. */
    fun share(context: Context, fileName: String, content: String, subject: String): Boolean = try {
        val dir = File(context.cacheDir, "reports").apply { mkdirs() }
        val file = File(dir, fileName)
        file.writeText(content, Charsets.UTF_8)
        share(context, file, "text/csv", subject)
    } catch (e: Exception) {
        false
    }

    /** Opens the share sheet for a file already on disk. Main thread only. */
    fun share(context: Context, file: File, mime: String, subject: String): Boolean = try {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        val send = Intent(Intent.ACTION_SEND).apply {
            type = mime
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(send, "Send report").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
        true
    } catch (e: Exception) {
        false
    }
}
```

#### `app/src/main/java/com/tpc/trikride/utils/ReverseGeocoder.kt`

```kotlin
package com.tpc.trikride.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.tpc.trikride.models.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

/**
 * Turns a pinned point into something a person can read.
 *
 * Uses Android's own [Geocoder], which is free and needs no key. It is not
 * guaranteed to be present or to answer — it depends on a backend the device
 * supplies, and rural Bohol is thinly covered — so every path falls back to the
 * coordinates themselves. A driver can navigate from a pin on a map and a pair
 * of numbers; an empty label would tell them nothing.
 */
object ReverseGeocoder {

    /**
     * An Open Location Code, as in "4XV8+G3".
     *
     * Google hands one of these back as the first address line wherever it has
     * no street address to give, which is most of Talibon, so the line that is
     * supposed to be the most specific is the one nobody can read. The named
     * parts of the same result — barangay, municipality, province — are the
     * ones worth showing, and a plus code is stripped wherever it appears.
     */
    private val PLUS_CODE = Regex(
        "\\b[23456789CFGHJMPQRVWX]{4,8}\\+[23456789CFGHJMPQRVWX]{2,3}\\b"
    )

    suspend fun describe(context: Context, location: Location): String =
        withContext(Dispatchers.IO) {
            val fallback = "Pinned location (%.5f, %.5f)".format(
                Locale.US, location.latitude, location.longitude
            )
            if (!Geocoder.isPresent()) return@withContext fallback

            try {
                @Suppress("DEPRECATION")
                val results = Geocoder(context, Locale.getDefault())
                    .getFromLocation(location.latitude, location.longitude, 1)
                val first = results?.firstOrNull() ?: return@withContext fallback
                readable(first) ?: fallback
            } catch (e: Exception) {
                fallback
            }
        }

    /** Named places first, then whatever survives cleaning the address line. */
    private fun readable(address: Address): String? {
        val named = listOfNotNull(
            address.thoroughfare,
            address.subLocality,
            address.locality,
            address.subAdminArea
        ).map { it.trim() }
            .filter { it.isNotBlank() && !PLUS_CODE.containsMatchIn(it) }
            .distinct()
            .take(3)

        if (named.isNotEmpty()) return named.joinToString(", ")

        val line = address.getAddressLine(0) ?: return null
        return line.replace(PLUS_CODE, "")
            .split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .joinToString(", ")
            .ifBlank { null }
    }
}
```

[[PB]]

### I.7 Interface — theme

#### `app/src/main/java/com/tpc/trikride/ui/theme/Color.kt`

```kotlin
package com.tpc.trikride.ui.theme

import androidx.compose.ui.graphics.Color

// ---------------------------------------------------------------------------
// TriGo TPC brand palette
// ---------------------------------------------------------------------------

// Brand greens & accents (shared across light and dark)
val EmeraldGreen = Color(0xFF16A34A)   // primary (light)
val ForestGreen = Color(0xFF166534)    // deep brand green / gradients
val GreenBright = Color(0xFF22C55E)    // primary (dark mode, brighter for contrast)
val AccentBlue = Color(0xFF2563EB)     // light accent
val AccentBlueDark = Color(0xFF3B82F6) // dark accent

// ---- Light theme surfaces ----
val LightBackground = Color(0xFFFFFFFF)
val LightSurface = Color(0xFFF5F5F5)
val LightCard = Color(0xFFFFFFFF)
val LightTextPrimary = Color(0xFF0F172A)
val LightTextSecondary = Color(0xFF64748B)
val LightDivider = Color(0xFFE2E8F0)

// ---- Dark theme surfaces (Uber / Spotify inspired) ----
val DarkBackground = Color(0xFF0F172A)
val DarkSurface = Color(0xFF1E293B)
val DarkCard = Color(0xFF1F2937)
val DarkTextPrimary = Color(0xFFFFFFFF)
val DarkTextSecondary = Color(0xFF94A3B8)
val DarkDivider = Color(0xFF334155)

// ---- Status colors (shared) ----
val SuccessColor = Color(0xFF22C55E)
val WarningColor = Color(0xFFF59E0B)
val ErrorColor = Color(0xFFEF4444)
val InfoColor = Color(0xFF3B82F6)
val RatingColor = Color(0xFFFBBF24)

// ---- Ride status accents ----
val RideAcceptedColor = Color(0xFF22C55E)
val RideInProgressColor = Color(0xFF3B82F6)
val RideCompletedColor = Color(0xFF16A34A)
val RideCancelledColor = Color(0xFFEF4444)
```

#### `app/src/main/java/com/tpc/trikride/ui/theme/Theme.kt`

```kotlin
package com.tpc.trikride.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = GreenBright,
    onPrimary = Color.White,
    secondary = AccentBlueDark,
    onSecondary = Color.White,
    tertiary = GreenBright,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkCard,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkDivider,
    error = ErrorColor,
    onError = Color.White,
    primaryContainer = ForestGreen,
    onPrimaryContainer = Color.White,
    secondaryContainer = DarkSurface,
    onSecondaryContainer = DarkTextPrimary,
    errorContainer = Color(0xFF3B1218),
    onErrorContainer = Color(0xFFFECACA)
)

private val LightColorScheme = lightColorScheme(
    primary = EmeraldGreen,
    onPrimary = Color.White,
    secondary = AccentBlue,
    onSecondary = Color.White,
    tertiary = ForestGreen,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightCard,
    onSurfaceVariant = LightTextSecondary,
    outline = LightDivider,
    error = ErrorColor,
    onError = Color.White,
    primaryContainer = Color(0xFFDCFCE7),
    onPrimaryContainer = ForestGreen,
    secondaryContainer = Color(0xFFEFF6FF),
    onSecondaryContainer = AccentBlue,
    errorContainer = Color(0xFFFEE2E2),
    onErrorContainer = Color(0xFF991B1B)
)

/**
 * TrikRide theme. Brand colors are used directly (dynamic Material You color
 * is intentionally disabled) so the emerald-green identity is consistent on
 * every device. Follows the system light/dark setting by default.
 */
@Composable
fun TrikRideTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
```

#### `app/src/main/java/com/tpc/trikride/ui/theme/ThemeState.kt`

```kotlin
package com.tpc.trikride.ui.theme

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tpc.trikride.utils.AuthPrefs

/**
 * App-wide theme override. `null` means "follow the system setting";
 * `true`/`false` force dark/light. Held as Compose state so that toggling
 * it from the Settings screen recomposes the whole app instantly, and written
 * through to preferences so the choice survives a launch — before, it was a
 * process-global and every restart silently reverted it.
 */
object ThemeState {
    var darkModeOverride by mutableStateOf<Boolean?>(null)
        private set

    /** Called once at start-up, before the first frame is composed. */
    fun load(context: Context) {
        darkModeOverride = AuthPrefs.darkModeOverride(context)
    }

    fun set(context: Context, dark: Boolean?) {
        darkModeOverride = dark
        AuthPrefs.setDarkModeOverride(context, dark)
    }
}
```

#### `app/src/main/java/com/tpc/trikride/ui/theme/Type.kt`

```kotlin
package com.tpc.trikride.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = 0.sp
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
```

[[PB]]

### I.8 Interface — shared components

#### `app/src/main/java/com/tpc/trikride/ui/components/AvatarCropper.kt`

```kotlin
package com.tpc.trikride.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Lets the user say which part of a photograph becomes their avatar.
 *
 * The previous behaviour took the middle of the image, which is the wrong
 * answer for most portrait photographs — a head sits near the top, and a centre
 * crop takes the chest. Here the image can be dragged and pinched behind a
 * circular window, and what shows through the window is what gets stored.
 *
 * No cropping library is needed for this. The whole problem is one coordinate
 * mapping, from screen position back to source pixel, applied once on confirm.
 */
@Composable
fun AvatarCropper(
    source: Bitmap,
    onCancel: () -> Unit,
    onConfirm: (Bitmap) -> Unit
) {
    val image = remember(source) { source.asImageBitmap() }

    // Zero until the window has been measured; nothing is drawn before then.
    var viewport by remember { mutableFloatStateOf(0f) }
    var zoom by remember(source) { mutableFloatStateOf(1f) }
    var offset by remember(source) { mutableStateOf(Offset.Zero) }

    // The image starts scaled to cover the window, so no edge can show however
    // it is dragged.
    val baseScale = if (viewport > 0f) {
        max(viewport / source.width, viewport / source.height)
    } else {
        1f
    }

    /** Furthest the image may travel at a given zoom before an edge would show. */
    fun travelLimit(atZoom: Float): Offset {
        val s = baseScale * atZoom
        return Offset(
            max(0f, (source.width * s - viewport) / 2f),
            max(0f, (source.height * s - viewport) / 2f)
        )
    }

    Dialog(onDismissRequest = onCancel) {
        Surface(shape = RoundedCornerShape(24.dp), tonalElevation = 6.dp) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    "Position your photo",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Drag to move, pinch to zoom. What is inside the circle is what others see.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .onSizeChanged { viewport = it.width.toFloat() }
                        .pointerInput(source) {
                            detectTransformGestures { _, pan, gestureZoom, _ ->
                                val next = (zoom * gestureZoom).coerceIn(1f, 5f)
                                // Clamped against the new zoom: zooming out
                                // otherwise leaves the image parked off-centre
                                // with a gap behind it.
                                val limit = travelLimit(next)
                                zoom = next
                                offset = Offset(
                                    (offset.x + pan.x).coerceIn(-limit.x, limit.x),
                                    (offset.y + pan.y).coerceIn(-limit.y, limit.y)
                                )
                            }
                        }
                ) {
                    if (viewport > 0f) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val s = baseScale * zoom
                            val w = source.width * s
                            val h = source.height * s
                            drawImage(
                                image = image,
                                dstOffset = IntOffset(
                                    ((viewport - w) / 2f + offset.x).roundToInt(),
                                    ((viewport - h) / 2f + offset.y).roundToInt()
                                ),
                                dstSize = IntSize(w.roundToInt(), h.roundToInt())
                            )

                            // Outside the circle is dimmed rather than hidden,
                            // so the user can see what they are cutting off as
                            // well as what they are keeping.
                            val radius = size.minDimension / 2f
                            val mask = Path().apply {
                                addRect(Rect(Offset.Zero, Size(size.width, size.height)))
                                addOval(Rect(center = center, radius = radius))
                                fillType = PathFillType.EvenOdd
                            }
                            drawPath(mask, Color.Black.copy(alpha = 0.55f))
                            drawCircle(
                                color = Color.White.copy(alpha = 0.9f),
                                radius = radius,
                                style = Stroke(width = 2f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onCancel) { Text("Cancel") }
                    TextButton(
                        enabled = viewport > 0f,
                        onClick = {
                            onConfirm(cropOut(source, viewport, baseScale * zoom, offset))
                        }
                    ) { Text("Use photo") }
                }
            }
        }
    }
}

/**
 * Maps the circular window back to a square of source pixels.
 *
 * A source pixel `p` is drawn at `(viewport - w) / 2 + offset + p * scale`, so
 * the pixel under the centre of the window is `source / 2 - offset / scale`,
 * and the window's half-width in source pixels is `viewport / 2 scale`.
 * Everything else here is keeping that square inside the bitmap.
 */
private fun cropOut(source: Bitmap, viewport: Float, scale: Float, offset: Offset): Bitmap {
    if (scale <= 0f || viewport <= 0f) return source

    val centreX = source.width / 2f - offset.x / scale
    val centreY = source.height / 2f - offset.y / scale
    val half = viewport / (2f * scale)

    val side = (half * 2f).roundToInt()
        .coerceAtLeast(1)
        .coerceAtMost(minOf(source.width, source.height))

    val left = (centreX - half).roundToInt().coerceIn(0, source.width - side)
    val top = (centreY - half).roundToInt().coerceIn(0, source.height - side)

    return Bitmap.createBitmap(source, left, top, side, side)
}
```

#### `app/src/main/java/com/tpc/trikride/ui/components/AvatarPicker.kt`

```kotlin
package com.tpc.trikride.ui.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.tpc.trikride.utils.ProfilePhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/** Creates a temp file in the cache and returns a FileProvider uri for it. */
internal fun newCameraUri(context: Context): Uri {
    val dir = File(context.cacheDir, "images").apply { mkdirs() }
    val file = File.createTempFile("capture_", ".jpg", dir)
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
}

/**
 * Circular avatar that opens a camera-or-gallery chooser when tapped.
 *
 * [photoData] is the base64 JPEG held in the database, since profile photos do
 * not go to Cloud Storage on this project. Falls back to the user's [initials]
 * when they have not set one.
 *
 * A chosen image goes to [AvatarCropper] before it goes anywhere else, so what
 * is stored is the part of the photograph the user chose rather than whatever
 * happened to be in the middle of the frame.
 */
@Composable
fun AvatarPicker(
    photoData: String?,
    initials: String,
    isUploading: Boolean,
    onPhotoChosen: (android.graphics.Bitmap) -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 96.dp
) {
    val context = LocalContext.current
    var showChooser by remember { mutableStateOf(false) }
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }
    // Decoding is cheap at this size, but not free, so tie it to the data.
    // Smaller than a licence, but the same shape, and this one sits on a screen
    // that is opened far more often.
    val photo by produceState<androidx.compose.ui.graphics.ImageBitmap?>(null, photoData) {
        value = withContext(Dispatchers.Default) { ProfilePhoto.decode(photoData) }
    }

    // The image chosen but not yet positioned. Decoding it is disk work, so it
    // happens off the main thread and the cropper waits for it.
    var toCrop by remember { mutableStateOf<Uri?>(null) }
    var cropSource by remember { mutableStateOf<android.graphics.Bitmap?>(null) }

    LaunchedEffect(toCrop) {
        val uri = toCrop
        cropSource = if (uri == null) null
        else withContext(Dispatchers.IO) { ProfilePhoto.loadForCrop(context, uri) }
        // An image that will not decode clears the request rather than leaving
        // an empty dialog waiting for a bitmap that is never coming.
        if (uri != null && cropSource == null) toCrop = null
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri -> if (uri != null) toCrop = uri }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        val uri = pendingCameraUri
        if (success && uri != null) toCrop = uri
        pendingCameraUri = null
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val uri = newCameraUri(context)
            pendingCameraUri = uri
            cameraLauncher.launch(uri)
        }
    }

    fun openCamera() {
        val granted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        if (granted) {
            val uri = newCameraUri(context)
            pendingCameraUri = uri
            cameraLauncher.launch(uri)
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable(enabled = !isUploading) { showChooser = true },
        contentAlignment = Alignment.Center
    ) {
        when {
            isUploading -> CircularProgressIndicator(modifier = Modifier.size(size / 3))
            photo != null -> Image(
                bitmap = photo!!,
                contentDescription = "Profile photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            else -> Text(
                initials,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Small camera badge in the corner.
        if (!isUploading) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(size / 3.4f)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.PhotoCamera,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(size / 6)
                )
            }
        }
    }

    if (showChooser) {
        AlertDialog(
            onDismissRequest = { showChooser = false },
            title = { Text("Change profile photo") },
            text = {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showChooser = false
                                openCamera()
                            }
                            .padding(vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.PhotoCamera, contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(14.dp))
                        Text("Take a photo", style = MaterialTheme.typography.bodyLarge)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showChooser = false
                                galleryLauncher.launch(
                                    androidx.activity.result.PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            }
                            .padding(vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.PhotoLibrary, contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(14.dp))
                        Text("Choose from gallery", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showChooser = false }) { Text("Cancel") }
            }
        )
    }

    cropSource?.let { bitmap ->
        AvatarCropper(
            source = bitmap,
            onCancel = { toCrop = null },
            onConfirm = { cropped ->
                toCrop = null
                onPhotoChosen(cropped)
            }
        )
    }
}
```

#### `app/src/main/java/com/tpc/trikride/ui/components/CommonComponents.kt`

```kotlin
package com.tpc.trikride.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import com.tpc.trikride.ui.theme.ThemeState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/** Full-width rounded primary button in brand green. */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
    ) {
        Text(text, style = MaterialTheme.typography.titleMedium)
    }
}

/** Full-width outlined secondary button. */
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
    ) {
        Text(text, style = MaterialTheme.typography.titleMedium)
    }
}

/** Rounded, softly elevated surface card used throughout the app. */
@Composable
fun SectionCard(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.padding(contentPadding)) {
            content()
        }
    }
}

/**
 * A shimmering grey placeholder used for skeleton loading states.
 * Pass a sized [modifier]; set [circle] for avatar placeholders.
 */
@Composable
fun SkeletonBox(modifier: Modifier = Modifier, circle: Boolean = false) {
    val transition = rememberInfiniteTransition(label = "skeleton")
    val alpha by transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.75f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "skeletonAlpha"
    )
    Box(
        modifier = modifier
            .clip(if (circle) CircleShape else RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = alpha * 0.35f))
    )
}

/** A skeleton stand-in for a content card while data loads. */
@Composable
fun SkeletonCard(lines: Int = 3, modifier: Modifier = Modifier) {
    SectionCard(modifier = modifier) {
        Column {
            repeat(lines) { index ->
                SkeletonBox(
                    modifier = Modifier
                        .fillMaxWidth(if (index == 0) 0.6f else 0.9f)
                        .height(if (index == 0) 16.dp else 12.dp)
                )
                if (index != lines - 1) Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

/**
 * Wraps scrollable content with swipe-down-to-refresh. [onRefresh] should
 * kick off the reload; flip [isRefreshing] back to false when it finishes.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefreshableBox(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.fillMaxSize()
    ) {
        content()
    }
}

/** Centered empty-state / placeholder with an icon, title and message. */
@Composable
fun SimplePlaceholder(
    icon: ImageVector,
    title: String,
    message: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(56.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

/** Rounded outlined text field with a leading icon and floating label. */
@Composable
fun TrikTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    singleLine: Boolean = true
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val visual = if (isPassword && !passwordVisible) {
        PasswordVisualTransformation()
    } else {
        VisualTransformation.None
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(leadingIcon, contentDescription = null) },
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            }
        } else null,
        singleLine = singleLine,
        visualTransformation = visual,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier.fillMaxWidth()
    )
}
```

#### `app/src/main/java/com/tpc/trikride/ui/components/GoogleMapView.kt`

```kotlin
package com.tpc.trikride.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.tpc.trikride.models.Location

/**
 * Google Maps rendering, used when a key is configured.
 *
 * The plain [MapView] rather than the maps-compose wrapper: the wrapper adds a
 * dependency whose version has to track Compose's, and everything here is a
 * handful of calls either way.
 *
 * Nothing in this file uses a Map ID or cloud-based styling. That matters — a
 * Map ID turns each map load into a billed Dynamic Maps call, whereas
 * client-styled maps render at no charge. The styling stays in code for that
 * reason as much as for convenience.
 */
@Composable
internal fun GoogleTrikMap(
    pins: List<MapPin>,
    modifier: Modifier,
    height: Dp,
    fallbackCentre: Location,
    zoom: Double,
    connectPins: Boolean,
    routeColor: Color
) {
    val mapView = rememberMapViewWithLifecycle()
    val currentPins by rememberUpdatedState(pins)

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(18.dp)),
        factory = { mapView },
        update = { view ->
            view.getMapAsync { map ->
                map.clear()
                map.uiSettings.isZoomControlsEnabled = false
                map.uiSettings.isMapToolbarEnabled = false

                if (connectPins && currentPins.size >= 2) {
                    map.addPolyline(
                        PolylineOptions()
                            .addAll(currentPins.take(2).map { it.location.toLatLng() })
                            .color(routeColor.toArgb())
                            .width(10f)
                    )
                }

                currentPins.forEach { pin ->
                    map.addMarker(
                        MarkerOptions()
                            .position(pin.location.toLatLng())
                            .title(pin.label)
                            .icon(BitmapDescriptorFactory.defaultMarker(pin.color.toMapHue()))
                    )
                }

                when {
                    currentPins.isEmpty() -> map.moveCamera(
                        CameraUpdateFactory.newCameraPosition(
                            CameraPosition.fromLatLngZoom(
                                fallbackCentre.toLatLng(), zoom.toFloat()
                            )
                        )
                    )
                    currentPins.size == 1 -> map.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            currentPins.first().location.toLatLng(), zoom.toFloat()
                        )
                    )
                    else -> {
                        val bounds = LatLngBounds.builder()
                            .apply { currentPins.forEach { include(it.location.toLatLng()) } }
                            .build()
                        // Padding keeps pins off the edge. Wrapped because the
                        // camera throws if the view has not been laid out yet.
                        runCatching {
                            map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 120))
                        }
                    }
                }
            }
        }
    )
}

/**
 * Google Maps version of the centre-pin picker. Reports the camera target once
 * the user stops moving the map.
 */
@Composable
internal fun GooglePickerMap(
    centre: Location,
    modifier: Modifier,
    height: Dp,
    onMoved: (Location) -> Unit
) {
    val mapView = rememberMapViewWithLifecycle()
    val currentOnMoved by rememberUpdatedState(onMoved)
    val currentCentre by rememberUpdatedState(centre)

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(18.dp)),
        factory = { mapView },
        update = { view ->
            view.getMapAsync { map ->
                map.uiSettings.isZoomControlsEnabled = false
                map.uiSettings.isMapToolbarEnabled = false

                map.setOnCameraIdleListener {
                    val target = map.cameraPosition.target
                    currentOnMoved(
                        Location(
                            latitude = target.latitude,
                            longitude = target.longitude,
                            timestamp = System.currentTimeMillis().toString()
                        )
                    )
                }

                // Only move the camera when the incoming point is somewhere
                // else, so reporting the centre back does not fight the pan.
                val target = map.cameraPosition.target
                val moved = kotlin.math.abs(target.latitude - currentCentre.latitude) > 1e-5 ||
                    kotlin.math.abs(target.longitude - currentCentre.longitude) > 1e-5
                if (moved) {
                    map.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(currentCentre.toLatLng(), 17f)
                    )
                }
            }
        }
    )
}

/**
 * A [MapView] driven by the composition's lifecycle.
 *
 * Google's MapView is an old-style view that expects onCreate, onResume, onPause
 * and onDestroy to be forwarded to it by hand. Skipping them leaks the map's
 * renderer and its native resources.
 */
@Composable
private fun rememberMapViewWithLifecycle(): MapView {
    val context = androidx.compose.ui.platform.LocalContext.current
    val mapView = remember { MapView(context).apply { id = android.view.View.generateViewId() } }
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    DisposableEffect(lifecycle, mapView) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(null)
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> Unit
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
            mapView.onDestroy()
        }
    }
    return mapView
}

private fun Location.toLatLng() = LatLng(latitude, longitude)

/**
 * Google's default markers come from a fixed hue wheel rather than an arbitrary
 * colour, so the app's palette is mapped onto the nearest available hue.
 */
private fun Color.toMapHue(): Float {
    val hsv = FloatArray(3)
    android.graphics.Color.colorToHSV(toArgb(), hsv)
    return hsv[0]
}

/** Whether a Maps key was supplied at build time. */
internal val hasGoogleMapsKey: Boolean
    get() = com.tpc.trikride.BuildConfig.MAPS_API_KEY.isNotBlank()
```

#### `app/src/main/java/com/tpc/trikride/ui/components/LicenceUpload.kt`

```kotlin
package com.tpc.trikride.ui.components

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.tpc.trikride.models.VerificationStatus
import com.tpc.trikride.utils.LicenceImage

/**
 * Where a driver sends the photograph of their licence.
 *
 * This sits in the driver's own screens rather than in the registration form on
 * purpose. Registration stays short, and a driver on a poor connection or one
 * who declines the camera permission is not stuck part-way through creating an
 * account. The verification gate holds either way, since nobody can accept a
 * passenger until an administrator approves them.
 *
 * Nothing is sent until the consent in [ConsentToUpload] is ticked. A licence is
 * sensitive personal information under the Data Privacy Act, and burying that
 * agreement in the Terms accepted days earlier is not the same as asking.
 */
@Composable
fun LicenceUploadCard(
    status: VerificationStatus,
    hasImage: Boolean,
    imageData: String?,
    isUploading: Boolean,
    message: String?,
    onSubmit: (Uri, String) -> Unit,
    onRemove: () -> Unit,
    onDismissMessage: () -> Unit
) {
    val context = LocalContext.current
    var showConsent by remember { mutableStateOf(false) }
    var showChooser by remember { mutableStateOf(false) }
    var showRemoveConfirm by remember { mutableStateOf(false) }
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }
    // Stamped when the driver agrees, not when the write lands.
    var consentedAt by remember { mutableStateOf("") }

    // Decoded off the main thread: this is a ~200 KB base64 string becoming a
    // 1280-pixel bitmap, and doing it inside composition drops frames on the
    // screen an administrator uses to work through a verification queue.
    val preview by produceState<androidx.compose.ui.graphics.ImageBitmap?>(null, imageData) {
        value = withContext(Dispatchers.Default) { LicenceImage.decode(imageData) }
    }

    fun send(uri: Uri) = onSubmit(uri, consentedAt)

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri -> uri?.let(::send) }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        val uri = pendingCameraUri
        if (success && uri != null) send(uri)
        pendingCameraUri = null
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val uri = newCameraUri(context)
            pendingCameraUri = uri
            cameraLauncher.launch(uri)
        }
    }

    fun openCamera() {
        val granted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        if (granted) {
            val uri = newCameraUri(context)
            pendingCameraUri = uri
            cameraLauncher.launch(uri)
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    SectionCard {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.Badge,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Driver's licence",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        licenceStatusLine(status, hasImage),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (preview != null) {
                Image(
                    bitmap = preview!!,
                    contentDescription = "Your licence photo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
                Spacer(modifier = Modifier.height(12.dp))
            } else if (hasImage) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(modifier = Modifier.size(28.dp)) }
                Spacer(modifier = Modifier.height(12.dp))
            } else {
                Text(
                    "Photograph your licence with the number, your name and the expiry " +
                        "date all readable. An administrator checks it against what you " +
                        "typed, then approves your account.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (isUploading) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Sending…", style = MaterialTheme.typography.bodySmall)
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    PrimaryButton(
                        text = if (hasImage) "Replace photo" else "Add licence photo",
                        onClick = { showConsent = true },
                        modifier = Modifier.weight(1f)
                    )
                    if (hasImage) {
                        OutlinedButton(
                            onClick = { showRemoveConfirm = true },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.weight(1f)
                        ) { Text("Remove") }
                    }
                }
            }

            message?.let {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

    if (showConsent) {
        ConsentToUpload(
            onDismiss = { showConsent = false },
            onAgree = {
                consentedAt = System.currentTimeMillis().toString()
                showConsent = false
                showChooser = true
                onDismissMessage()
            }
        )
    }

    if (showChooser) {
        AlertDialog(
            onDismissRequest = { showChooser = false },
            title = { Text("Licence photo") },
            text = {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                    ) {
                        Icon(Icons.Filled.PhotoCamera, contentDescription = null)
                        Spacer(modifier = Modifier.width(12.dp))
                        TextButton(onClick = { showChooser = false; openCamera() }) {
                            Text("Take a photo")
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                    ) {
                        Icon(Icons.Filled.PhotoLibrary, contentDescription = null)
                        Spacer(modifier = Modifier.width(12.dp))
                        TextButton(onClick = {
                            showChooser = false
                            galleryLauncher.launch(
                                androidx.activity.result.PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        }) { Text("Choose from gallery") }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showChooser = false }) { Text("Cancel") }
            }
        )
    }

    if (showRemoveConfirm) {
        AlertDialog(
            onDismissRequest = { showRemoveConfirm = false },
            title = { Text("Remove licence photo?") },
            text = {
                Text(
                    "The photo is deleted from our records. You cannot be approved to " +
                        "carry passengers without one, and an approved account will need " +
                        "a new photo before its next review."
                )
            },
            confirmButton = {
                TextButton(onClick = { showRemoveConfirm = false; onRemove() }) {
                    Text("Remove")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRemoveConfirm = false }) { Text("Keep it") }
            }
        )
    }
}

/**
 * The agreement shown immediately before the camera opens.
 *
 * Deliberately specific and deliberately here. It names what is collected, who
 * can see it, and when it is destroyed, because "you agreed to the Terms" is not
 * a meaningful answer to a driver asking why the app holds a picture of their
 * licence.
 */
@Composable
private fun ConsentToUpload(onDismiss: () -> Unit, onAgree: () -> Unit) {
    var agreed by remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Before you send your licence") },
        text = {
            Column {
                Text(
                    "A driver's licence is sensitive personal information under the " +
                        "Data Privacy Act of 2012. Here is exactly what happens to it.",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(10.dp))
                ConsentPoint("It is used only to verify that you may drive, and to check it again when the licence expires.")
                ConsentPoint("Only you and a TrikRide administrator can open it. It is never shown to passengers.")
                ConsentPoint("If your application is refused, the photo is deleted at that moment.")
                ConsentPoint("You can remove it yourself at any time, and it goes with your account if you delete that.")
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(checked = agreed, onCheckedChange = { agreed = it })
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "I agree to TrikRide holding a photo of my licence on these terms.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onAgree, enabled = agreed) { Text("Continue") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun ConsentPoint(text: String) {
    Row(modifier = Modifier.padding(top = 6.dp)) {
        Text("•", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodySmall)
    }
}

private fun licenceStatusLine(status: VerificationStatus, hasImage: Boolean): String = when {
    !hasImage && status == VerificationStatus.REJECTED ->
        "Your application was refused and the previous photo was deleted."
    !hasImage -> "No photo on file yet. One is needed before you can be approved."
    status == VerificationStatus.APPROVED -> "On file and approved."
    status == VerificationStatus.EXPIRED -> "On file, but your licence needs renewing."
    else -> "On file, waiting for an administrator to review it."
}
```

#### `app/src/main/java/com/tpc/trikride/ui/components/SupportPanel.kt`

```kotlin
package com.tpc.trikride.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tpc.trikride.BuildConfig
import com.tpc.trikride.models.COMPLAINT_CATEGORIES
import com.tpc.trikride.models.UserType
import com.tpc.trikride.viewmodels.SupportViewModel

/**
 * The concern form, the reporter's own past reports, and the contact details.
 *
 * Shared between passengers and drivers. The Community Guidelines tell both to
 * use the Support tab, and the admin screen has always shown who filed what, so
 * the form only ever being on the passenger's side was an oversight.
 */
/** Matches the ceiling the database rules enforce on a complaint. */
private const val MAX_DESCRIPTION_LENGTH = 2000

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportPanel(
    userId: String,
    reporterType: UserType,
    viewModel: SupportViewModel
) {
    var category by remember { mutableStateOf(COMPLAINT_CATEGORIES.first()) }
    var description by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val submitting by viewModel.submitting.collectAsState()
    val submitted by viewModel.submitted.collectAsState()
    val error by viewModel.error.collectAsState()
    val myComplaints by viewModel.myComplaints.collectAsState()

    LaunchedEffect(userId) { viewModel.bind(userId) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text("Support", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Report a concern and an administrator will review it.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(20.dp))

        SectionCard {
            Column {
                Text("Report a Concern", style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                if (submitted) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.CheckCircle, contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Report sent. An administrator will review it.",
                            color = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    SecondaryButton(text = "Report another", onClick = {
                        description = ""
                        viewModel.resetSubmitted()
                    })
                } else {
                    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                        OutlinedTextField(
                            value = category,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Category") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        )
                        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            COMPLAINT_CATEGORIES.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = { category = option; expanded = false }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = {
                            if (it.length <= MAX_DESCRIPTION_LENGTH) description = it
                        },
                        label = { Text("Describe your concern") },
                        supportingText = {
                            Text("${description.length} / $MAX_DESCRIPTION_LENGTH")
                        },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                    error?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(it, color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    PrimaryButton(
                        text = if (submitting) "Sending..." else "Submit Report",
                        onClick = {
                            viewModel.submitComplaint(
                                reporterName = "",
                                reporterType = reporterType,
                                category = category,
                                description = description.trim()
                            )
                        },
                        enabled = !submitting && description.isNotBlank()
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        if (myComplaints.isNotEmpty()) {
            Text("My Reports", style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            myComplaints.sortedByDescending { it.createdAt.toLongOrNull() ?: 0L }.forEach { c ->
                SectionCard {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(c.category, fontWeight = FontWeight.SemiBold)
                            Text(
                                c.status.name.replace('_', ' '),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text(c.description, style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        if (c.adminNote.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Admin: ${c.adminNote}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        SectionCard {
            Column {
                Text("Contact", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                ContactRow(Icons.Filled.Phone, "Hotline", BuildConfig.SUPPORT_HOTLINE)
                Spacer(modifier = Modifier.height(8.dp))
                ContactRow(Icons.Filled.SupportAgent, "Email", BuildConfig.SUPPORT_EMAIL)
                Spacer(modifier = Modifier.height(8.dp))
                ContactRow(Icons.Filled.History, "Hours", "6:00 AM - 9:00 PM")
            }
        }
    }
}

@Composable
private fun ContactRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(12.dp))
        Text(label, style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}
```

#### `app/src/main/java/com/tpc/trikride/ui/components/TrikMap.kt`

```kotlin
package com.tpc.trikride.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.edit
import com.tpc.trikride.models.Location
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.Polyline
import java.io.File

/** Talibon town centre, used when nothing better is known yet. */
val TALIBON_CENTRE = Location(10.1531, 124.3251, "Talibon, Bohol")

/** A pin to draw on the map. */
data class MapPin(
    val location: Location,
    val label: String,
    val color: Color,
    /** Drawn larger, for the thing the user is actually tracking. */
    val emphasis: Boolean = false
)

/**
 * Configures osmdroid once per process.
 *
 * Two things matter here. The tile cache goes in app-private storage, which
 * keeps osmdroid from asking for a storage permission it does not need on any
 * Android version this app supports. And the user agent is set to the package
 * name: OpenStreetMap's tile servers block osmdroid's default agent, because
 * too many apps shipped without changing it, so leaving it alone means blank
 * tiles.
 */
private fun configureOsmdroid(context: Context) {
    val config = Configuration.getInstance()
    if (config.userAgentValue == context.packageName) return

    val prefs = context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
    config.load(context, prefs)
    config.userAgentValue = context.packageName
    config.osmdroidBasePath = File(context.cacheDir, "osmdroid").apply { mkdirs() }
    config.osmdroidTileCache = File(config.osmdroidBasePath, "tiles").apply { mkdirs() }
    prefs.edit { putString("osmdroid.basePath", config.osmdroidBasePath.absolutePath) }
}

/**
 * An OpenStreetMap view with pins on it.
 *
 * OpenStreetMap rather than Google Maps because it needs no API key and no
 * billing account, so there is nothing that can expire or be suspended and take
 * the maps down with it. Google's Android SDK renders at no charge too, as long
 * as no Map ID is used, but the key behind it depends on a live billing account.
 *
 * The tradeoff is OpenStreetMap's tile usage policy, which suits a pilot at this
 * scale but would need a dedicated tile source if the app ever grew. Swapping
 * renderer means changing this file and nothing else.
 */
@Composable
fun TrikMap(
    pins: List<MapPin>,
    modifier: Modifier = Modifier,
    height: Dp,
    /** Centre here when there are no pins to frame. */
    fallbackCentre: Location = TALIBON_CENTRE,
    zoom: Double = 15.5,
    /** Draws a straight line between the first two pins. */
    connectPins: Boolean = false
) {
    val context = LocalContext.current
    remember { configureOsmdroid(context); true }

    val outline = MaterialThemeOutline()

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(18.dp)),
        factory = { ctx ->
            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                // The built-in +/- buttons overlap our own controls and are
                // redundant once pinch-zoom is on.
                zoomController.setVisibility(
                    org.osmdroid.views.CustomZoomButtonsController.Visibility.NEVER
                )
                controller.setZoom(zoom)
                controller.setCenter(fallbackCentre.toGeoPoint())
            }
        },
        update = { map ->
            map.overlays.clear()

            if (connectPins && pins.size >= 2) {
                map.overlays.add(
                    Polyline(map).apply {
                        setPoints(pins.take(2).map { it.location.toGeoPoint() })
                        outlinePaint.color = outline
                        outlinePaint.strokeWidth = 7f
                        outlinePaint.isAntiAlias = true
                    }
                )
            }

            pins.forEach { pin ->
                map.overlays.add(
                    Marker(map).apply {
                        position = pin.location.toGeoPoint()
                        title = pin.label
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                        icon = dotDrawable(pin.color.toArgb(), if (pin.emphasis) 34 else 24)
                    }
                )
            }

            when {
                pins.isEmpty() -> map.controller.setCenter(fallbackCentre.toGeoPoint())
                pins.size == 1 -> map.controller.animateTo(pins.first().location.toGeoPoint())
                else -> {
                    // Frame everything, with room so pins are not on the edge.
                    val box = org.osmdroid.util.BoundingBox.fromGeoPointsSafe(
                        pins.map { it.location.toGeoPoint() }
                    )
                    map.post { runCatching { map.zoomToBoundingBox(box.increaseByScale(1.6f), true) } }
                }
            }
            map.invalidate()
        },
        // osmdroid holds a tile-download thread pool and a tile cache handle.
        // Without onDetach they outlive the screen.
        onRelease = { it.onDetach() }
    )
}

/**
 * A centre-pinned map for choosing a point. The map moves under a fixed pin,
 * which is easier on a small screen than dragging a marker, and [onMoved]
 * reports wherever the pin ends up. The pin itself is drawn over whichever
 * renderer is active, so both look the same.
 */
@Composable
fun PickerMap(
    centre: Location,
    modifier: Modifier = Modifier,
    height: Dp,
    pinColor: Color,
    onMoved: (Location) -> Unit
) {
    val argbPin = pinColor.toArgb()
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(18.dp))
    ) {
        if (hasGoogleMapsKey) {
            GooglePickerMap(
                centre = centre,
                modifier = Modifier,
                height = height,
                onMoved = onMoved
            )
        } else {
            OsmPickerMap(centre, Modifier, height, onMoved)
        }
        CentrePin(height = height, argb = argbPin)
    }
}

/** The fixed pin drawn at the centre of the viewport, over either renderer. */
@Composable
private fun CentrePin(height: Dp, argb: Int) {
    androidx.compose.foundation.Canvas(
        modifier = Modifier.fillMaxWidth().height(height)
    ) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        drawCircle(Color.Black.copy(alpha = 0.18f), radius = 7f,
            center = androidx.compose.ui.geometry.Offset(cx, cy + 16f))
        drawLine(
            color = Color(argb),
            start = androidx.compose.ui.geometry.Offset(cx, cy + 14f),
            end = androidx.compose.ui.geometry.Offset(cx, cy - 12f),
            strokeWidth = 5f
        )
        drawCircle(Color.White, radius = 15f,
            center = androidx.compose.ui.geometry.Offset(cx, cy - 20f))
        drawCircle(Color(argb), radius = 11f,
            center = androidx.compose.ui.geometry.Offset(cx, cy - 20f))
    }
}

@Composable
private fun OsmPickerMap(
    centre: Location,
    modifier: Modifier,
    height: Dp,
    onMoved: (Location) -> Unit
) {
    val context = LocalContext.current
    remember { configureOsmdroid(context); true }
    // The overlay below is built once in factory{}, so it would otherwise hold
    // the first onMoved forever.
    val currentOnMoved by rememberUpdatedState(onMoved)

    AndroidView(
        modifier = modifier.fillMaxWidth().height(height),
        factory = { ctx ->
            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                zoomController.setVisibility(
                    org.osmdroid.views.CustomZoomButtonsController.Visibility.NEVER
                )
                controller.setZoom(17.0)
                controller.setCenter(centre.toGeoPoint())

                // Report the centre once the user stops moving the map.
                overlays.add(object : Overlay() {
                    override fun onTouchEvent(
                        event: android.view.MotionEvent?,
                        view: MapView?
                    ): Boolean {
                        if (event?.action == android.view.MotionEvent.ACTION_UP && view != null) {
                            val c = view.mapCenter
                            currentOnMoved(
                                Location(
                                    latitude = c.latitude,
                                    longitude = c.longitude,
                                    timestamp = System.currentTimeMillis().toString()
                                )
                            )
                        }
                        return false
                    }
                })
            }
        },
        update = { map ->
            // Only recentre when the incoming point is somewhere else, so that
            // reporting the centre back does not fight the user's pan.
            val c = map.mapCenter
            val moved = kotlin.math.abs(c.latitude - centre.latitude) > 1e-5 ||
                kotlin.math.abs(c.longitude - centre.longitude) > 1e-5
            if (moved) map.controller.animateTo(centre.toGeoPoint())
        },
        onRelease = { it.onDetach() }
    )
}

@Composable
private fun MaterialThemeOutline(): Int =
    androidx.compose.material3.MaterialTheme.colorScheme.primary.toArgb()

private fun Location.toGeoPoint() = GeoPoint(latitude, longitude)

/** A filled circle with a white ring, drawn rather than shipped as an asset. */
private fun dotDrawable(argb: Int, sizePx: Int): Drawable = object : Drawable() {
    private val fill = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = argb }
    private val ring = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = sizePx / 7f
    }

    override fun draw(canvas: Canvas) {
        val r = sizePx / 2f
        canvas.drawCircle(r, r, r - ring.strokeWidth / 2f, fill)
        canvas.drawCircle(r, r, r - ring.strokeWidth / 2f, ring)
    }

    override fun getIntrinsicWidth() = sizePx
    override fun getIntrinsicHeight() = sizePx
    override fun setAlpha(alpha: Int) = Unit
    override fun setColorFilter(colorFilter: android.graphics.ColorFilter?) = Unit
    @Deprecated("Deprecated in Drawable")
    override fun getOpacity() = android.graphics.PixelFormat.TRANSLUCENT
}
```

[[PB]]

### I.9 Interface — screens

#### `app/src/main/java/com/tpc/trikride/ui/screens/AdminDashboardScreen.kt`

```kotlin
package com.tpc.trikride.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tpc.trikride.models.Complaint
import com.tpc.trikride.models.ComplaintStatus
import com.tpc.trikride.models.Driver
import com.tpc.trikride.models.DriverDocument
import com.tpc.trikride.models.FareConfig
import com.tpc.trikride.models.FareStop
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.RideStatus
import com.tpc.trikride.models.User
import com.tpc.trikride.models.VerificationStatus
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tpc.trikride.models.Location
import com.tpc.trikride.ui.components.PickerMap
import com.tpc.trikride.ui.components.PrimaryButton
import com.tpc.trikride.ui.components.SectionCard
import com.tpc.trikride.ui.components.TALIBON_CENTRE
import com.tpc.trikride.utils.FareSeed
import com.tpc.trikride.utils.LicenceImage
import com.tpc.trikride.ui.theme.ErrorColor
import com.tpc.trikride.ui.theme.InfoColor
import com.tpc.trikride.ui.theme.ForestGreen
import com.tpc.trikride.ui.theme.SuccessColor
import com.tpc.trikride.ui.theme.WarningColor
import com.tpc.trikride.viewmodels.AdminViewModel
import java.util.Locale

private enum class AdminTab { VERIFY, CONCERNS, MONITOR, FARES, PROFILE }

@Composable
fun AdminDashboardScreen(
    userId: String,
    onSignOut: () -> Unit,
    viewModel: AdminViewModel = viewModel()
) {
    val drivers by viewModel.drivers.collectAsState()
    val users by viewModel.users.collectAsState()
    val rides by viewModel.rides.collectAsState()
    val fareConfig by viewModel.fareConfig.collectAsState()
    val fareStops by viewModel.fareStops.collectAsState()
    val fareSaved by viewModel.fareSaved.collectAsState()
    val importing by viewModel.importing.collectAsState()
    val complaints by viewModel.complaints.collectAsState()
    val licences by viewModel.licences.collectAsState()

    var tab by remember { mutableStateOf(AdminTab.VERIFY) }
    val usersById = remember(users) { users.associateBy { it.id } }
    val pendingCount = drivers.count { it.verificationStatus == VerificationStatus.PENDING }
    val openConcerns = complaints.count { it.status != ComplaintStatus.RESOLVED }

    Scaffold(
        bottomBar = {
            AdminBottomBar(
                selected = tab,
                onSelect = { tab = it },
                pending = pendingCount,
                concerns = openConcerns
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (tab) {
                AdminTab.VERIFY -> VerificationContent(
                    drivers = drivers,
                    usersById = usersById,
                    licences = licences,
                    onOpenLicence = viewModel::openLicence,
                    onCloseLicence = viewModel::closeLicence,
                    onApprove = viewModel::approveDriver,
                    onReject = viewModel::rejectDriver,
                    onRevoke = viewModel::revokeApproval
                )
                AdminTab.CONCERNS -> ConcernsContent(
                    complaints = complaints,
                    usersById = usersById,
                    onUpdate = viewModel::updateComplaint
                )
                AdminTab.MONITOR -> MonitorContent(
                    drivers = drivers,
                    rides = rides,
                    complaints = complaints,
                    usersById = usersById
                )
                AdminTab.FARES -> FareConfigContent(
                    config = fareConfig,
                    stops = fareStops,
                    saved = fareSaved,
                    importing = importing,
                    onSaveConfig = viewModel::saveFareConfig,
                    onSaveStop = viewModel::saveFareStop,
                    onDeleteStop = viewModel::deleteFareStop,
                    onImport = viewModel::importOfficialRates,
                    onAcknowledgeSaved = viewModel::acknowledgeFareSaved
                )
                AdminTab.PROFILE -> SettingsScreen(
                    userId = userId,
                    userType = com.tpc.trikride.models.UserType.ADMIN,
                    subtitle = "Talibon Polytechnic College",
                    onSignOut = onSignOut
                )
            }
        }
    }
}

@Composable
private fun AdminBottomBar(
    selected: AdminTab,
    onSelect: (AdminTab) -> Unit,
    pending: Int,
    concerns: Int
) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        NavigationBarItem(
            selected = selected == AdminTab.VERIFY,
            onClick = { onSelect(AdminTab.VERIFY) },
            icon = {
                BadgedBox(badge = { if (pending > 0) Badge { Text("$pending") } }) {
                    Icon(Icons.Filled.VerifiedUser, contentDescription = "Verify")
                }
            },
            label = { Text("Verify") }
        )
        NavigationBarItem(
            selected = selected == AdminTab.CONCERNS,
            onClick = { onSelect(AdminTab.CONCERNS) },
            icon = {
                BadgedBox(badge = { if (concerns > 0) Badge { Text("$concerns") } }) {
                    Icon(Icons.Filled.ReportProblem, contentDescription = "Concerns")
                }
            },
            label = { Text("Concerns") }
        )
        NavigationBarItem(
            selected = selected == AdminTab.MONITOR,
            onClick = { onSelect(AdminTab.MONITOR) },
            icon = { Icon(Icons.Filled.Insights, contentDescription = "Monitor") },
            label = { Text("Monitor") }
        )
        NavigationBarItem(
            selected = selected == AdminTab.FARES,
            onClick = { onSelect(AdminTab.FARES) },
            icon = { Icon(Icons.Filled.Payments, contentDescription = "Fares") },
            label = { Text("Fares") }
        )
        NavigationBarItem(
            selected = selected == AdminTab.PROFILE,
            onClick = { onSelect(AdminTab.PROFILE) },
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}

@Composable
private fun ConcernsContent(
    complaints: List<Complaint>,
    usersById: Map<String, User>,
    onUpdate: (Complaint, ComplaintStatus, String) -> Unit
) {
    val open = complaints.filter { it.status != ComplaintStatus.RESOLVED }
        .sortedByDescending { it.createdAt.toLongOrNull() ?: 0L }
    val resolved = complaints.filter { it.status == ComplaintStatus.RESOLVED }
        .sortedByDescending { it.createdAt.toLongOrNull() ?: 0L }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text("Concerns & Complaints", style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold)
        Text("Reports submitted by passengers and drivers.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(20.dp))

        Text("Open (${open.size})", style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))
        if (open.isEmpty()) {
            SectionCard {
                Text("No open concerns.", style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            open.forEach { c ->
                ComplaintCard(c, usersById[c.reporterId], onUpdate)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        if (resolved.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text("Resolved (${resolved.size})", style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))
            resolved.forEach { c ->
                ComplaintCard(c, usersById[c.reporterId], onUpdate)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun ComplaintCard(
    complaint: Complaint,
    reporter: User?,
    onUpdate: (Complaint, ComplaintStatus, String) -> Unit
) {
    var note by remember(complaint.id) { mutableStateOf(complaint.adminNote) }
    var expanded by remember(complaint.id) { mutableStateOf(false) }

    SectionCard {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(complaint.category, style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold)
                    val who = reporter?.firstName?.takeIf { it.isNotBlank() }
                        ?: complaint.reporterName.takeIf { it.isNotBlank() }
                        ?: "Unknown"
                    val role = complaint.reporterType.name.lowercase()
                        .replaceFirstChar { it.uppercase() }
                    Text(
                        "$who ($role)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                ComplaintStatusChip(complaint.status)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(complaint.description, style = MaterialTheme.typography.bodyMedium)

            if (complaint.adminNote.isNotBlank() && !expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Note: ${complaint.adminNote}", style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(modifier = Modifier.height(12.dp))
            if (!expanded) {
                OutlinedButton(
                    onClick = { expanded = true },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Respond") }
            } else {
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Note to the reporter") },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (complaint.status == ComplaintStatus.RESOLVED) {
                        // Nothing could set OPEN, so a report closed in error
                        // stayed closed and the "reopened" message in
                        // SupportRepository was unreachable.
                        OutlinedButton(
                            onClick = {
                                onUpdate(complaint, ComplaintStatus.OPEN, note.trim())
                                expanded = false
                            },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.weight(1f)
                        ) { Text("Reopen") }
                    }
                    OutlinedButton(
                        onClick = {
                            onUpdate(complaint, ComplaintStatus.IN_REVIEW, note.trim())
                            expanded = false
                        },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f)
                    ) { Text("In review") }
                    Button(
                        onClick = {
                            onUpdate(complaint, ComplaintStatus.RESOLVED, note.trim())
                            expanded = false
                        },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f)
                    ) { Text("Resolve") }
                }
            }
        }
    }
}

@Composable
private fun ComplaintStatusChip(status: ComplaintStatus) {
    val (color, label) = when (status) {
        ComplaintStatus.OPEN -> WarningColor to "Open"
        ComplaintStatus.IN_REVIEW -> InfoColor to "In review"
        ComplaintStatus.RESOLVED -> SuccessColor to "Resolved"
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
private fun VerificationContent(
    drivers: List<Driver>,
    usersById: Map<String, User>,
    licences: Map<String, DriverDocument?>,
    onOpenLicence: (String) -> Unit,
    onCloseLicence: (String) -> Unit,
    onApprove: (String) -> Unit,
    onReject: (String) -> Unit,
    onRevoke: (String) -> Unit
) {
    val pending = drivers.filter { it.verificationStatus == VerificationStatus.PENDING }
    val others = drivers.filter { it.verificationStatus != VerificationStatus.PENDING }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text("Driver Verification", style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Review and approve registered tricycle drivers.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(20.dp))

        Text("Pending (${pending.size})", style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        if (pending.isEmpty()) {
            SectionCard {
                Text("No drivers waiting for verification.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            pending.forEach { driver ->
                DriverCard(
                    driver = driver,
                    user = usersById[driver.userId],
                    licence = licences[driver.userId],
                    licenceOpen = licences.containsKey(driver.userId),
                    onOpenLicence = { onOpenLicence(driver.userId) },
                    onCloseLicence = { onCloseLicence(driver.userId) },
                    actions = {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedButton(
                                onClick = { onReject(driver.userId) },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorColor),
                                modifier = Modifier.weight(1f)
                            ) { Text("Reject") }
                            Button(
                                onClick = { onApprove(driver.userId) },
                                // There is nothing to verify against without one, and
                                // the whole premise of the app is that somebody looked.
                                enabled = driver.hasLicenceImage,
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.weight(1f)
                            ) { Text("Approve") }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text("All Drivers (${drivers.size})", style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        if (others.isEmpty() && pending.isEmpty()) {
            SectionCard {
                Text("No registered drivers yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            others.forEach { driver ->
                DriverCard(
                    driver = driver,
                    user = usersById[driver.userId],
                    licence = licences[driver.userId],
                    licenceOpen = licences.containsKey(driver.userId),
                    onOpenLicence = { onOpenLicence(driver.userId) },
                    onCloseLicence = { onCloseLicence(driver.userId) },
                    actions = {
                        if (driver.verificationStatus == VerificationStatus.APPROVED) {
                            OutlinedButton(
                                onClick = { onRevoke(driver.userId) },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorColor),
                                modifier = Modifier.fillMaxWidth()
                            ) { Text("Revoke Approval") }
                        } else {
                            Button(
                                onClick = { onApprove(driver.userId) },
                                enabled = driver.hasLicenceImage,
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) { Text("Approve") }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun DriverCard(
    driver: Driver,
    user: User?,
    licence: DriverDocument?,
    licenceOpen: Boolean,
    onOpenLicence: () -> Unit,
    onCloseLicence: () -> Unit,
    actions: @Composable () -> Unit
) {
    SectionCard {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Person, contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        user?.firstName?.ifBlank { "Driver" } ?: "Driver",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (user?.phoneNumber?.isNotBlank() == true) {
                        Text(user.phoneNumber, style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                StatusChip(driver.verificationStatus)
            }
            Spacer(modifier = Modifier.height(12.dp))
            InfoLine("Tricycle No.", driver.tricycleNumber.ifBlank { "—" })
            Spacer(modifier = Modifier.height(12.dp))
            LicenceReview(
                hasImage = driver.hasLicenceImage,
                licence = licence,
                isOpen = licenceOpen,
                onOpen = onOpenLicence,
                onClose = onCloseLicence
            )
            Spacer(modifier = Modifier.height(12.dp))
            actions()
        }
    }
}

/**
 * The licence photograph, shown only once the administrator asks for it.
 *
 * Collapsed by default. These are identity documents, and putting a dozen of
 * them on screen at once — over the shoulder of whoever is sitting nearby —
 * is not something a verification queue needs to do. Opening one is a
 * deliberate act, and it is also what triggers the fetch.
 */
@Composable
private fun LicenceReview(
    hasImage: Boolean,
    licence: DriverDocument?,
    isOpen: Boolean,
    onOpen: () -> Unit,
    onClose: () -> Unit
) {
    if (!isOpen) {
        Column {
            if (!hasImage) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.WarningAmber,
                        contentDescription = null,
                        tint = WarningColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "No licence photo submitted yet.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            // Offered whether or not a photograph has arrived: the number and
            // expiry are on the same protected node and are worth seeing on
            // their own while an application is still incomplete.
            OutlinedButton(
                onClick = onOpen,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Badge, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("View licence details")
            }
        }
        return
    }

    // Decoded off the main thread: this is a ~200 KB base64 string becoming a
    // 1280-pixel bitmap, and doing it inside composition drops frames on the
    // screen an administrator uses to work through a verification queue.
    val imageData = licence?.image
    // `settled` separates "still decoding" from "decoded to nothing", so the
    // failure message below cannot flash up in the moment before the bitmap
    // arrives.
    var settled by remember(imageData) { mutableStateOf(false) }
    val bitmap by produceState<androidx.compose.ui.graphics.ImageBitmap?>(null, imageData) {
        value = withContext(Dispatchers.Default) { LicenceImage.decode(imageData) }
        settled = true
    }
    Column {
        InfoLine("Licence No.", licence?.licenceNumber?.ifBlank { null } ?: "—")
        InfoLine("Licence Expiry", licence?.licenceExpiry?.ifBlank { null } ?: "—")
        Spacer(modifier = Modifier.height(8.dp))

        if (hasImage) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                when {
                    bitmap != null -> Image(
                        bitmap = bitmap!!,
                        contentDescription = "Licence photo",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                    licence == null || !settled ->
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    else -> Text(
                        "That photo could not be opened.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "Check the number and expiry on the photo against the two lines above.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            Text(
                "No photo on file, so these details cannot be checked against one yet.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        TextButton(onClick = onClose) { Text("Hide") }
    }
}

@Composable
private fun InfoLine(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun StatusChip(status: VerificationStatus) {
    val (color, label) = when (status) {
        VerificationStatus.APPROVED -> SuccessColor to "Approved"
        VerificationStatus.PENDING -> WarningColor to "Pending"
        VerificationStatus.REJECTED -> ErrorColor to "Rejected"
        VerificationStatus.EXPIRED -> ErrorColor to "Expired"
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
private fun MonitorContent(
    drivers: List<Driver>,
    rides: List<Ride>,
    complaints: List<Complaint>,
    usersById: Map<String, User>
) {
    var showReports by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = if (showReports) 1 else 0) {
            Tab(
                selected = !showReports,
                onClick = { showReports = false },
                text = { Text("Live") }
            )
            Tab(
                selected = showReports,
                onClick = { showReports = true },
                text = { Text("Reports") }
            )
        }
        if (showReports) {
            AdminReportsContent(
                rides = rides,
                drivers = drivers,
                complaints = complaints,
                usersById = usersById
            )
        } else {
            LiveMonitorContent(drivers = drivers, rides = rides)
        }
    }
}

@Composable
private fun LiveMonitorContent(drivers: List<Driver>, rides: List<Ride>) {
    val approved = drivers.count { it.verificationStatus == VerificationStatus.APPROVED }
    val pending = drivers.count { it.verificationStatus == VerificationStatus.PENDING }
    val activeRides = rides.count {
        it.status != RideStatus.COMPLETED && it.status != RideStatus.CANCELLED &&
            it.status != RideStatus.NO_SHOW
    }
    val completed = rides.count { it.status == RideStatus.COMPLETED }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text("System Monitor", style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatTile("Drivers", "${drivers.size}", Icons.Filled.Groups, Modifier.weight(1f))
            StatTile("Approved", "$approved", Icons.Filled.CheckCircle, Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatTile("Pending", "$pending", Icons.Filled.VerifiedUser, Modifier.weight(1f))
            StatTile("Active Rides", "$activeRides", Icons.Filled.Insights, Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(20.dp))

        Text("Recent Rides ($completed completed)", style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        if (rides.isEmpty()) {
            SectionCard {
                Text("No rides recorded yet.", style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            rides.sortedByDescending { it.requestedAt.toLongOrNull() ?: 0L }.take(15).forEach { ride ->
                SectionCard {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${ride.pickupLocation.address} → ${ride.dropoffLocation.address}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1f))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(ride.status.name, style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary)
                            Text("₱%.2f".format(Locale.US, ride.estimatedFare),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun StatTile(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier) {
    SectionCard(modifier = modifier) {
        Column {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

/**
 * The fare table, as posted by FeTODAT.
 *
 * There are 240 stops, so the list is lazy and the admin filters down to what
 * they want rather than scrolling: type part of a stop name, narrow to a zone,
 * or switch to the flagged rows, which are the ones that came out of the
 * transcription with a problem worth checking against the physical sheet.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FareConfigContent(
    config: FareConfig,
    stops: List<FareStop>,
    saved: Boolean,
    importing: Boolean,
    onSaveConfig: (FareConfig) -> Unit,
    onSaveStop: (FareStop) -> Unit,
    onDeleteStop: (String) -> Unit,
    onImport: () -> Unit,
    onAcknowledgeSaved: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    var zoneFilter by remember { mutableStateOf<String?>(null) }
    var flaggedOnly by remember { mutableStateOf(false) }
    var editing by remember { mutableStateOf<FareStop?>(null) }
    var addingNew by remember { mutableStateOf(false) }
    var confirmImport by remember { mutableStateOf(false) }
    var showGlobals by remember { mutableStateOf(false) }

    LaunchedEffect(saved) {
        if (saved) {
            delay(1500)
            onAcknowledgeSaved()
        }
    }

    val zones = remember(stops) { stops.map { it.zone }.distinct().sorted() }
    val flaggedCount = remember(stops) { stops.count { it.needsReview } }

    val visible = remember(stops, query, zoneFilter, flaggedOnly) {
        val terms = query.trim().lowercase().split(" ").filter { it.isNotBlank() }
        stops.asSequence()
            .filter { zoneFilter == null || it.zone == zoneFilter }
            .filter { !flaggedOnly || it.needsReview }
            .filter { stop ->
                // Every word has to appear somewhere, which is what the
                // passenger's picker does. A single substring match over the
                // whole query found nothing for "poblacion talibon".
                val haystack = "${stop.name} ${stop.zone}".lowercase()
                terms.all { haystack.contains(it) }
            }
            .sortedWith(compareBy<FareStop>({ it.zone }, { it.name }))
            .toList()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Fares", style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold)
                    Text(
                        if (stops.isEmpty()) "No rate table loaded yet."
                        else "${stops.size} stops across ${zones.size} zones",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = { addingNew = true }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add a stop")
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Search stops") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = zoneFilter == null && !flaggedOnly,
                    onClick = { zoneFilter = null; flaggedOnly = false },
                    label = { Text("All") }
                )
                if (flaggedCount > 0) {
                    FilterChip(
                        selected = flaggedOnly,
                        onClick = { flaggedOnly = !flaggedOnly },
                        label = { Text("Needs review ($flaggedCount)") },
                        leadingIcon = {
                            Icon(Icons.Filled.WarningAmber, contentDescription = null,
                                modifier = Modifier.size(16.dp))
                        }
                    )
                }
                zones.forEach { zone ->
                    FilterChip(
                        selected = zoneFilter == zone,
                        onClick = { zoneFilter = if (zoneFilter == zone) null else zone },
                        label = { Text(zone) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))

            TextButton(onClick = { showGlobals = true }) {
                Text("Minimums and flat rates")
            }

            if (saved) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.CheckCircle, contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Saved.", color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        if (stops.isEmpty()) {
            EmptyFareTable(importing = importing, onImport = { confirmImport = true })
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (visible.isEmpty()) {
                    item {
                        Text(
                            "Nothing matches that.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                items(visible, key = { it.id }) { stop ->
                    FareStopRow(stop = stop, onClick = { editing = stop })
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { confirmImport = true },
                        enabled = !importing,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            if (importing) "Loading the official table..."
                            else "Reload the official FeTODAT table"
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        config.source,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

    editing?.let { stop ->
        FareStopDialog(
            stop = stop,
            isNew = false,
            existingIds = stops.map { it.id }.toSet(),
            onDismiss = { editing = null },
            onSave = { updated -> onSaveStop(updated); editing = null },
            onDelete = { onDeleteStop(stop.id); editing = null }
        )
    }

    if (addingNew) {
        FareStopDialog(
            stop = FareStop(zone = zoneFilter ?: zones.firstOrNull().orEmpty()),
            isNew = true,
            existingIds = stops.map { it.id }.toSet(),
            onDismiss = { addingNew = false },
            onSave = { created -> onSaveStop(created); addingNew = false },
            onDelete = null
        )
    }

    if (showGlobals) {
        GlobalRatesDialog(
            config = config,
            onDismiss = { showGlobals = false },
            onSave = { updated -> onSaveConfig(updated); showGlobals = false }
        )
    }

    if (confirmImport) {
        AlertDialog(
            onDismissRequest = { confirmImport = false },
            title = { Text("Load the official table?") },
            text = {
                Text(
                    "This writes all ${FareSeed.STOPS.size} stops from the posted FeTODAT " +
                        "sheet into the database. Any stop with the same name is overwritten, " +
                        "so hand-made corrections to those rows are lost. Stops you added " +
                        "yourself are left alone."
                )
            },
            confirmButton = {
                TextButton(onClick = { confirmImport = false; onImport() }) { Text("Load") }
            },
            dismissButton = {
                TextButton(onClick = { confirmImport = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun EmptyFareTable(importing: Boolean, onImport: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Filled.Payments,
            contentDescription = null,
            modifier = Modifier.size(56.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("No fares loaded", style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Load the ${FareSeed.STOPS.size} stops transcribed from the posted FeTODAT " +
                "sheet, then correct anything that reads wrong. Rides cannot be priced " +
                "until this is done.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        PrimaryButton(
            text = if (importing) "Loading..." else "Load official rates",
            onClick = onImport,
            enabled = !importing
        )
    }
}

@Composable
private fun FareStopRow(stop: FareStop, onClick: () -> Unit) {
    SectionCard(modifier = Modifier.clickable(onClick = onClick)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        stop.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (stop.needsReview) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            Icons.Filled.WarningAmber,
                            contentDescription = "Needs review",
                            tint = WarningColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Text(
                    if (stop.active) stop.zone else "${stop.zone} — inactive",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (stop.active) MaterialTheme.colorScheme.onSurfaceVariant
                    else ErrorColor
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "₱%.2f".format(Locale.US, stop.regularFare),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "₱%.2f discounted".format(Locale.US, stop.discountedFare),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun FareStopDialog(
    stop: FareStop,
    isNew: Boolean,
    /** Every id already in the table, so a new stop cannot land on one. */
    existingIds: Set<String>,
    onDismiss: () -> Unit,
    onSave: (FareStop) -> Unit,
    onDelete: (() -> Unit)?
) {
    var name by remember { mutableStateOf(stop.name) }
    var zone by remember { mutableStateOf(stop.zone) }
    // Locale.US on the way in because toDoubleOrNull() only ever reads a dot on
    // the way out. Formatting with the device locale put "25,00" in the field on
    // any comma-decimal phone, which parsed as null and left Save permanently
    // disabled with nothing on screen saying why.
    var regular by remember { mutableStateOf(if (stop.regularFare == 0.0) "" else "%.2f".format(Locale.US, stop.regularFare)) }
    var discounted by remember { mutableStateOf(if (stop.discountedFare == 0.0) "" else "%.2f".format(Locale.US, stop.discountedFare)) }
    var active by remember { mutableStateOf(stop.active) }
    var reviewed by remember { mutableStateOf(!stop.needsReview) }
    var lat by remember { mutableStateOf(if (stop.latitude == 0.0) "" else stop.latitude.toString()) }
    var lng by remember { mutableStateOf(if (stop.longitude == 0.0) "" else stop.longitude.toString()) }
    var pickingPoint by remember { mutableStateOf(false) }

    var confirmDelete by remember { mutableStateOf(false) }

    val regularValue = regular.toDoubleOrNull()
    val discountedValue = discounted.toDoubleOrNull()
    // A rate has to be a real amount of money. Parsing alone accepted -25 and
    // 999999 as readily as 25, and the table this writes to prices every ride.
    val ratesSane = regularValue != null && discountedValue != null &&
        regularValue in 0.0..MAX_FARE && discountedValue in 0.0..MAX_FARE
    val latValue = lat.toDoubleOrNull()
    val lngValue = lng.toDoubleOrNull()
    val pointSane = (lat.isBlank() && lng.isBlank()) ||
        (latValue != null && lngValue != null &&
            latValue in -90.0..90.0 && lngValue in -180.0..180.0)
    val canSave = name.isNotBlank() && zone.isNotBlank() && ratesSane && pointSane

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isNew) "Add a stop" else stop.name) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                if (stop.note.isNotBlank()) {
                    SectionCard {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                Icons.Filled.WarningAmber,
                                contentDescription = null,
                                tint = WarningColor,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(stop.note, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Stop name") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = zone,
                    onValueChange = { zone = it },
                    label = { Text("Zone") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                MoneyField("Regular fare (₱)", regular) { regular = it }
                Spacer(modifier = Modifier.height(10.dp))
                MoneyField("Senior / PWD / Student (₱)", discounted) { discounted = it }

                if (regularValue != null && discountedValue != null && discountedValue > regularValue) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "The discounted rate is higher than the regular one.",
                        style = MaterialTheme.typography.bodySmall,
                        color = WarningColor
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Map position (optional)",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Leave blank if unknown. A stop without a position still " +
                        "prices and books normally; it just does not show on the map.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        MoneyField("Latitude", lat) { lat = it }
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        MoneyField("Longitude", lng) { lng = it }
                    }
                }
                // Typing two decimals is why none of the 240 stops has a
                // position. Dropping a pin is the same job in a few seconds.
                TextButton(onClick = { pickingPoint = true }) {
                    Icon(
                        Icons.Filled.Place,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        if (lat.toDoubleOrNull() != null && lng.toDoubleOrNull() != null) {
                            "Move the pin"
                        } else {
                            "Set it on the map"
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Bookable", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            "Passengers can pick this stop",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(checked = active, onCheckedChange = { active = it })
                }

                if (stop.needsReview) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Checked against the sheet",
                                style = MaterialTheme.typography.bodyMedium)
                            Text(
                                "Clears the review flag",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(checked = reviewed, onCheckedChange = { reviewed = it })
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = canSave,
                onClick = {
                    val id = stop.id.ifBlank {
                        // Two names differing only in punctuation collapse to the
                        // same slug, and saveFareStop writes with setValue, so
                        // without this a new stop silently replaced an existing one.
                        val base = (zone + "__" + name).lowercase()
                            .replace(Regex("[^a-z0-9]+"), "_")
                            .trim('_')
                            .ifBlank { "stop" }
                        if (base !in existingIds) base
                        else generateSequence(2) { it + 1 }
                            .map { "${base}_$it" }
                            .first { it !in existingIds }
                    }
                    onSave(
                        stop.copy(
                            id = id,
                            name = name.trim(),
                            zone = zone.trim(),
                            regularFare = regularValue ?: 0.0,
                            discountedFare = discountedValue ?: 0.0,
                            active = active,
                            needsReview = stop.needsReview && !reviewed,
                            latitude = latValue ?: 0.0,
                            longitude = lngValue ?: 0.0
                        )
                    )
                }
            ) { Text("Save") }
        },
        dismissButton = {
            Row {
                if (onDelete != null) {
                    TextButton(onClick = { confirmDelete = true }) {
                        Text("Delete", color = ErrorColor)
                    }
                }
                TextButton(onClick = onDismiss) { Text("Cancel") }
            }
        }
    )

    if (confirmDelete && onDelete != null) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text("Remove ${stop.name}?") },
            text = {
                Text(
                    "Passengers will no longer be able to book this destination, and " +
                        "the posted rate for it is not kept anywhere else. Rides already " +
                        "taken to it are unaffected."
                )
            },
            confirmButton = {
                TextButton(onClick = { confirmDelete = false; onDelete() }) {
                    Text("Remove", color = ErrorColor)
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmDelete = false }) { Text("Keep it") }
            }
        )
    }

    if (pickingPoint) {
        StopPointPicker(
            stopName = name.ifBlank { "this stop" },
            initial = Location(
                latitude = lat.toDoubleOrNull() ?: TALIBON_CENTRE.latitude,
                longitude = lng.toDoubleOrNull() ?: TALIBON_CENTRE.longitude
            ),
            onDismiss = { pickingPoint = false },
            onPicked = { point ->
                lat = "%.6f".format(Locale.US, point.latitude)
                lng = "%.6f".format(Locale.US, point.longitude)
                pickingPoint = false
            }
        )
    }
}

/** Minimum fares and the two flat rates that are not tied to a stop. */
@Composable
private fun GlobalRatesDialog(
    config: FareConfig,
    onDismiss: () -> Unit,
    onSave: (FareConfig) -> Unit
) {
    // Locale.US, for the same reason as the stop dialog: these are read back
    // with toDoubleOrNull(), which only ever accepts a dot.
    var minRegular by remember(config) { mutableStateOf("%.2f".format(Locale.US, config.minimumRegular)) }
    var minDiscounted by remember(config) { mutableStateOf("%.2f".format(Locale.US, config.minimumDiscounted)) }
    var poblacion by remember(config) { mutableStateOf("%.2f".format(Locale.US, config.poblacionFlat)) }
    var terminal by remember(config) { mutableStateOf("%.2f".format(Locale.US, config.terminalRoundTrip)) }
    var perHead by remember(config) { mutableStateOf(config.chargePerPassenger) }

    // Every field has to be a real amount before anything is written. This used
    // to fall back to the existing value per field, so a cleared or unparseable
    // box saved the old number and still reported success.
    fun rate(text: String): Double? = text.toDoubleOrNull()?.takeIf { it in 0.0..MAX_FARE }
    val minRegularValue = rate(minRegular)
    val minDiscountedValue = rate(minDiscounted)
    val poblacionValue = rate(poblacion)
    val terminalValue = rate(terminal)
    val canSave = minRegularValue != null && minDiscountedValue != null &&
        poblacionValue != null && terminalValue != null &&
        minDiscountedValue <= minRegularValue

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Minimums and flat rates") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(
                    "A stop can never price below the minimum for its rate column.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (!canSave) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Every box needs an amount between 0 and ${MAX_FARE.toInt()}, and the " +
                            "discounted minimum cannot be above the regular one.",
                        style = MaterialTheme.typography.bodySmall,
                        color = ErrorColor
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                MoneyField("Minimum regular fare (₱)", minRegular) { minRegular = it }
                Spacer(modifier = Modifier.height(10.dp))
                MoneyField("Minimum senior / PWD / student (₱)", minDiscounted) { minDiscounted = it }
                Spacer(modifier = Modifier.height(10.dp))
                MoneyField("${FareConfig.POBLACION_LABEL} (₱)", poblacion) { poblacion = it }
                Spacer(modifier = Modifier.height(10.dp))
                MoneyField("${FareConfig.TERMINAL_ROUND_TRIP_LABEL} (₱)", terminal) { terminal = it }
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Charge per passenger", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            "Off means one fare covers the whole tricycle",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(checked = perHead, onCheckedChange = { perHead = it })
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = canSave,
                onClick = {
                    onSave(
                        config.copy(
                            minimumRegular = minRegularValue ?: config.minimumRegular,
                            minimumDiscounted = minDiscountedValue ?: config.minimumDiscounted,
                            poblacionFlat = poblacionValue ?: config.poblacionFlat,
                            terminalRoundTrip = terminalValue ?: config.terminalRoundTrip,
                            chargePerPassenger = perHead
                        )
                    )
                }
            ) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

/**
 * Puts a fare stop on the map by dragging rather than by typing coordinates.
 *
 * The posted schedule gives names, not positions, so every one of the 240 has
 * to be placed by somebody who knows the town. Asking that person for two
 * six-decimal numbers per stop is why none of them had a position at all.
 */
@Composable
private fun StopPointPicker(
    stopName: String,
    initial: Location,
    onDismiss: () -> Unit,
    onPicked: (Location) -> Unit
) {
    var centre by remember { mutableStateOf(initial) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 16.dp, top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Close")
                    }
                    Text(
                        "Where is it?",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    "Move the map so the pin sits on $stopName. Passengers can then find " +
                        "it from the map, and a driver can navigate to it.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))

                PickerMap(
                    height = 420.dp,
                    centre = centre,
                    pinColor = ForestGreen,
                    onMoved = { centre = it },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        "%.6f, %.6f".format(Locale.US, centre.latitude, centre.longitude),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    PrimaryButton(
                        text = "Use this point",
                        onClick = { onPicked(centre) }
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

/** Matches the ceiling the database rules enforce on a written rate. */
private const val MAX_FARE = 1000.0

@Composable
private fun MoneyField(label: String, value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    )
}
```

#### `app/src/main/java/com/tpc/trikride/ui/screens/AdminReportsScreen.kt`

```kotlin
package com.tpc.trikride.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tpc.trikride.models.Complaint
import com.tpc.trikride.models.Driver
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.User
import com.tpc.trikride.ui.components.SectionCard
import com.tpc.trikride.ui.components.SimplePlaceholder
import com.tpc.trikride.utils.ReportBuilder
import com.tpc.trikride.utils.PdfReportWriter
import com.tpc.trikride.utils.ReportExporter
import com.tpc.trikride.utils.ReportPeriod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

/** Which of the three reports the admin is exporting. */
private enum class ReportKind(val slug: String, val title: String, val blurb: String) {
    RIDES("rides", "Ride activity", "Every booking in the period with fares, status and both parties."),
    DRIVERS("drivers", "Driver performance", "Rides, completions and gross fares per driver."),
    CONCERNS("concerns", "Concerns and complaints", "What was filed, the categories, and how each was closed.")
}

/**
 * Month and year reports the admin can save or send on.
 *
 * The period list is built from the rides that actually exist, so there are no
 * empty months to pick through, and the summary updates as soon as a period is
 * chosen — the admin can read the numbers here without exporting anything.
 */
@Composable
fun AdminReportsContent(
    rides: List<Ride>,
    drivers: List<Driver>,
    complaints: List<Complaint>,
    usersById: Map<String, User>
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val periods = remember(rides) { ReportBuilder.availablePeriods(rides) }
    // Not keyed on the ride list: rides arrive live, and re-keying here would
    // throw away a date range the admin had just finished picking.
    var period by remember { mutableStateOf<ReportPeriod>(ReportPeriod.AllTime) }
    var status by remember { mutableStateOf<String?>(null) }
    // A year of rides is a long table to draw, so the export runs off the main
    // thread and the buttons stay disabled until it finishes.
    var busy by remember { mutableStateOf(false) }
    var showRangePicker by remember { mutableStateOf(false) }

    if (showRangePicker) {
        DateRangeDialog(
            onDismiss = { showRangePicker = false },
            onConfirm = { start, end ->
                period = ReportPeriod.customRange(start, end)
                status = null
                showRangePicker = false
            }
        )
    }

    // Held between choosing a format and the file picker coming back.
    var pendingCsv by remember { mutableStateOf("") }
    var pendingPdf by remember { mutableStateOf<((java.io.OutputStream) -> Unit)?>(null) }

    val saveCsvLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("text/csv")
    ) { uri ->
        status = when {
            uri == null -> null
            ReportExporter.writeTo(context, uri, pendingCsv) -> "Spreadsheet saved."
            else -> "Could not write the file. Try Share instead."
        }
        pendingCsv = ""
    }

    val savePdfLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/pdf")
    ) { uri ->
        val render = pendingPdf
        pendingPdf = null
        if (uri == null || render == null) return@rememberLauncherForActivityResult
        busy = true
        status = "Building the PDF…"
        scope.launch {
            val written = withContext(Dispatchers.IO) {
                ReportExporter.writeTo(context, uri, render)
            }
            status = if (written) "PDF saved." else "Could not write the file. Try Share instead."
            busy = false
        }
    }

    val summary = remember(rides, complaints, period) {
        ReportBuilder.summarise(rides, complaints, period)
    }

    fun csvFor(kind: ReportKind): String = when (kind) {
        ReportKind.RIDES -> ReportBuilder.ridesCsv(rides, complaints, usersById, period)
        ReportKind.DRIVERS -> ReportBuilder.driversCsv(rides, drivers, usersById, period)
        ReportKind.CONCERNS -> ReportBuilder.complaintsCsv(complaints, usersById, period)
    }

    fun pdfFor(kind: ReportKind): (java.io.OutputStream) -> Unit = { out ->
        when (kind) {
            ReportKind.RIDES ->
                PdfReportWriter.writeRideReport(out, rides, complaints, usersById, period)
            ReportKind.DRIVERS ->
                PdfReportWriter.writeDriverReport(out, rides, drivers, usersById, period)
            ReportKind.CONCERNS ->
                PdfReportWriter.writeConcernReport(out, complaints, usersById, period)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column {
                Text(
                    "Reports",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Export a month, a year, or any range of dates as a printable " +
                        "PDF or a spreadsheet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item {
            SectionCard {
                Column {
                    Text(
                        "Period",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    PeriodDropdown(
                        periods = periods,
                        selected = period,
                        onSelected = { period = it; status = null },
                        onCustomRequested = { showRangePicker = true }
                    )
                    if (period is ReportPeriod.Custom) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "Both dates are included in full.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        if (rides.isEmpty()) {
            item {
                SimplePlaceholder(
                    icon = Icons.Filled.Description,
                    title = "No activity yet",
                    message = "Once rides start coming through, reports for any " +
                        "month, year, or range of dates will be available here."
                )
            }
            return@LazyColumn
        }

        item {
            SectionCard {
                Column {
                    Text(
                        period.label,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    SummaryLine("Total rides", "${summary.totalRides}")
                    SummaryLine("Completed", "${summary.completed}")
                    SummaryLine("Cancelled or no-show", "${summary.cancelled}")
                    SummaryLine("Still open", "${summary.inProgress}")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    SummaryLine("Gross fares", "P%.2f".format(Locale.US, summary.grossFares), strong = true)
                    SummaryLine("Average completed fare", "P%.2f".format(Locale.US, summary.averageFare))
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    SummaryLine("Passengers served", "${summary.uniquePassengers}")
                    SummaryLine("Drivers with a ride", "${summary.activeDrivers}")
                    SummaryLine(
                        "Concerns filed",
                        "${summary.complaintsFiled} (${summary.complaintsResolved} resolved)"
                    )
                }
            }
        }

        items(ReportKind.entries.size) { index ->
            val kind = ReportKind.entries[index]
            SectionCard {
                Column {
                    Text(
                        kind.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        kind.blurb,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "The PDF is the one to print or hand over: headline figures and " +
                            "charts first, then every record behind them. The spreadsheet " +
                            "is the same data for sorting and totalling in Excel.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    // Both formats, both ways out. Saving as PDF is the filled
                    // button because printing or handing over a copy is what
                    // this screen is usually opened for.
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = {
                                pendingPdf = pdfFor(kind)
                                status = null
                                savePdfLauncher.launch(
                                    ReportBuilder.fileName(kind.slug, period, "pdf")
                                )
                            },
                            enabled = !busy,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Filled.PictureAsPdf,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Save PDF")
                        }
                        OutlinedButton(
                            onClick = {
                                val render = pdfFor(kind)
                                val name = ReportBuilder.fileName(kind.slug, period, "pdf")
                                val subject = "TrikRide ${kind.title} — ${period.label}"
                                busy = true
                                status = "Building the PDF…"
                                scope.launch {
                                    val file = withContext(Dispatchers.IO) {
                                        ReportExporter.renderToCache(context, name, render)
                                    }
                                    // The chooser has to be started from the main
                                    // thread, so only the drawing goes to IO.
                                    val sent = file != null &&
                                        ReportExporter.share(context, file, "application/pdf", subject)
                                    status = if (sent) null else "Could not prepare the file to send."
                                    busy = false
                                }
                            },
                            enabled = !busy,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Filled.Share,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Send PDF")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(
                            onClick = {
                                pendingCsv = csvFor(kind)
                                status = null
                                saveCsvLauncher.launch(ReportBuilder.fileName(kind.slug, period))
                            },
                            enabled = !busy,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Filled.Download,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Save sheet")
                        }
                        OutlinedButton(
                            onClick = {
                                val sent = ReportExporter.share(
                                    context = context,
                                    fileName = ReportBuilder.fileName(kind.slug, period),
                                    content = csvFor(kind),
                                    subject = "TrikRide ${kind.title} — ${period.label}"
                                )
                                status = if (sent) null else "No app available to send the file."
                            },
                            enabled = !busy,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Filled.Description,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Send sheet")
                        }
                    }
                }
            }
        }

        status?.let { message ->
            item {
                Text(
                    message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PeriodDropdown(
    periods: List<ReportPeriod>,
    selected: ReportPeriod,
    onSelected: (ReportPeriod) -> Unit,
    onCustomRequested: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = selected.label,
            onValueChange = {},
            readOnly = true,
            label = { Text("Covering") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            periods.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.label) },
                    onClick = { onSelected(option); expanded = false }
                )
            }
            HorizontalDivider()
            // The months and years above come from the data. This one does not,
            // so it opens a picker rather than sitting in the same list.
            DropdownMenuItem(
                text = { Text("Choose exact dates…") },
                leadingIcon = {
                    Icon(
                        Icons.Filled.DateRange,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                onClick = { expanded = false; onCustomRequested() }
            )
        }
    }
}

/**
 * The date-range picker behind "Choose exact dates".
 *
 * Confirm stays disabled until both ends are chosen: a range with only a start
 * has no meaning here, and running the report on a half-made selection is worse
 * than making the admin finish it.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateRangeDialog(
    onDismiss: () -> Unit,
    onConfirm: (Long, Long) -> Unit
) {
    val state = rememberDateRangePickerState()
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            val start = state.selectedStartDateMillis
            val end = state.selectedEndDateMillis
            TextButton(
                onClick = { if (start != null && end != null) onConfirm(start, end) },
                enabled = start != null && end != null
            ) { Text("Use these dates") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    ) {
        DateRangePicker(
            state = state,
            title = {
                Text(
                    "Report period",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 24.dp, top = 16.dp)
                )
            },
            headline = {
                Text(
                    "Pick the first and last day",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 24.dp, bottom = 8.dp)
                )
            },
            showModeToggle = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
        )
    }
}

@Composable
private fun SummaryLine(label: String, value: String, strong: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            value,
            style = if (strong) MaterialTheme.typography.titleMedium
            else MaterialTheme.typography.bodyMedium,
            fontWeight = if (strong) FontWeight.Bold else FontWeight.Medium,
            color = if (strong) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface
        )
    }
}
```

#### `app/src/main/java/com/tpc/trikride/ui/screens/ConsentScreen.kt`

```kotlin
package com.tpc.trikride.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tpc.trikride.ui.components.PrimaryButton
import com.tpc.trikride.ui.components.SecondaryButton
import com.tpc.trikride.ui.components.SectionCard

/**
 * The gate between signing in and using the app.
 *
 * Every document has to be opened-able and separately ticked, and the button
 * stays disabled until all of them are. Someone who does not want to agree can
 * sign out; they cannot get past this screen without agreeing, which is the
 * whole point of it.
 */
@Composable
fun ConsentScreen(
    includeLegal: Boolean,
    includeDriverAgreement: Boolean,
    isSaving: Boolean,
    error: String?,
    onAccept: () -> Unit,
    /** Shown only when the record could not be read, rather than not accepted. */
    unreadable: Boolean = false,
    onRetry: (() -> Unit)? = null,
    onDecline: () -> Unit
) {
    val documents = remember(includeLegal, includeDriverAgreement) {
        buildList {
            if (includeLegal) {
                add(LegalDoc.TERMS)
                add(LegalDoc.PRIVACY)
                add(LegalDoc.COMMUNITY)
            }
            if (includeDriverAgreement) add(LegalDoc.DRIVER_AGREEMENT)
        }
    }
    val checked = remember(documents) { mutableStateMapOf<LegalDoc, Boolean>() }
    var reading by remember { mutableStateOf<LegalDoc?>(null) }

    reading?.let { doc ->
        LegalScreen(doc = doc, onBack = { reading = null })
        return
    }

    val allChecked = documents.all { checked[it] == true }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Icon(
            Icons.Filled.Gavel,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(44.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Before you continue",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            when {
                includeLegal && includeDriverAgreement ->
                    "Please read and agree to the following before using TrikRide. " +
                        "Drivers also accept the Driver Agreement."
                includeDriverAgreement ->
                    "One more thing before you start driving. Please read and accept " +
                        "the Driver Agreement."
                else ->
                    "Please read and agree to the following before using TrikRide."
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(20.dp))

        SectionCard {
            Column {
                documents.forEachIndexed { index, doc ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = checked[doc] == true,
                            onCheckedChange = { checked[doc] = it },
                            enabled = !isSaving
                        )
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clickable(enabled = !isSaving) { reading = doc }
                        ) {
                            Text(
                                "I have read and agree to the",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    doc.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    Icons.AutoMirrored.Filled.OpenInNew,
                                    contentDescription = "Open ${doc.title}",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                    if (index != documents.lastIndex) HorizontalDivider()
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "Tap a document title to read it in full.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        error?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(it, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(24.dp))
        PrimaryButton(
            text = if (isSaving) "Saving..." else "I Agree and Continue",
            onClick = onAccept,
            enabled = allChecked && !isSaving
        )
        if (unreadable && onRetry != null) {
            Spacer(modifier = Modifier.height(8.dp))
            SecondaryButton(
                text = "Try again",
                onClick = onRetry,
                enabled = !isSaving
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            TextButton(onClick = onDecline, enabled = !isSaving) {
                Text("Not now, sign me out")
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}
```

#### `app/src/main/java/com/tpc/trikride/ui/screens/DriverHomeScreen.kt`

```kotlin
package com.tpc.trikride.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tpc.trikride.models.Driver
import com.tpc.trikride.models.DriverDocument
import com.tpc.trikride.models.FareType
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.RideRequest
import com.tpc.trikride.models.RideStatus
import com.tpc.trikride.models.VerificationStatus
import com.tpc.trikride.ui.components.LicenceUploadCard
import com.tpc.trikride.ui.components.PrimaryButton
import com.tpc.trikride.ui.components.RefreshableBox
import com.tpc.trikride.ui.components.SectionCard
import com.tpc.trikride.ui.components.SkeletonCard
import com.tpc.trikride.ui.components.SupportPanel
import com.tpc.trikride.ui.components.TrikTextField
import com.tpc.trikride.utils.Navigation
import com.tpc.trikride.ui.theme.ErrorColor
import com.tpc.trikride.ui.theme.RatingColor
import kotlinx.coroutines.delay
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DateRange
import com.tpc.trikride.utils.LocationProvider
import com.tpc.trikride.viewmodels.DriverViewModel
import com.tpc.trikride.viewmodels.SupportViewModel
import java.util.Locale

private enum class DriverTab { DASHBOARD, REQUESTS, HISTORY, SUPPORT, PROFILE }

@Composable
fun DriverHomeScreen(
    userId: String,
    onSignOut: () -> Unit,
    viewModel: DriverViewModel = viewModel(),
    supportViewModel: SupportViewModel = viewModel()
) {
    LaunchedEffect(userId) {
        viewModel.bind(userId)
        supportViewModel.bind(userId)
    }

    val driver by viewModel.driverProfile.collectAsState()
    val openRequests by viewModel.openRequests.collectAsState()
    val activeRides by viewModel.activeRides.collectAsState()
    val isRegistering by viewModel.isRegistering.collectAsState()
    val error by viewModel.errorMessage.collectAsState()
    val earnings by viewModel.earnings.collectAsState()
    val licenceDoc by viewModel.licenceDoc.collectAsState()
    val uploadingLicence by viewModel.uploadingLicence.collectAsState()
    val licenceMessage by viewModel.licenceMessage.collectAsState()
    val accepting by viewModel.accepting.collectAsState()

    // Publish position only while online, and only while this screen exists.
    // Going offline or leaving the app stops it; there is no background service.
    val context = LocalContext.current
    val locationPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> if (granted) viewModel.startPublishingLocation(context) }

    LaunchedEffect(driver?.isAvailable) {
        if (driver?.isAvailable == true) {
            if (LocationProvider.hasPermission(context)) {
                viewModel.startPublishingLocation(context)
            } else {
                locationPermission.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }
        } else {
            viewModel.stopPublishingLocation()
        }
    }

    val profile = driver
    if (profile == null) {
        DriverOnboardingContent(
            isSubmitting = isRegistering,
            error = error,
            onDismissError = viewModel::dismissError,
            onSubmit = viewModel::registerDriver
        )
        return
    }

    // The number and expiry are on this node too, so it is fetched whether or
    // not a photograph has been sent.
    LaunchedEffect(Unit) { viewModel.loadLicenceDocument() }

    val notifications by supportViewModel.notifications.collectAsState()
    val unreadCount = notifications.count { !it.read }

    val licenceCard: @Composable () -> Unit = {
        LicenceUploadCard(
            status = profile.verificationStatus,
            hasImage = profile.hasLicenceImage,
            imageData = licenceDoc?.image,
            isUploading = uploadingLicence,
            message = licenceMessage,
            onSubmit = { uri, consentedAt ->
                viewModel.submitLicenceImage(context, uri, consentedAt)
            },
            onRemove = viewModel::removeLicenceImage,
            onDismissMessage = viewModel::clearLicenceMessage
        )
    }

    var tab by remember { mutableStateOf(DriverTab.DASHBOARD) }
    var showNotifications by remember { mutableStateOf(false) }

    if (showNotifications) {
        NotificationsScreen(
            userId = userId,
            viewModel = supportViewModel,
            onBack = { showNotifications = false }
        )
        return
    }

    Scaffold(
        bottomBar = { DriverBottomBar(selected = tab, onSelect = { tab = it }, requestCount = openRequests.size) }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            // Anything the database refuses — going online, accepting a ride,
            // advancing one — used to land here and go nowhere: this error was
            // only ever shown on the registration screen, so a driver whose
            // account already existed saw a control that simply did not work.
            error?.let { message ->
                SectionCard {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = viewModel::dismissError) { Text("Dismiss") }
                    }
                }
            }
            Box {
            when (tab) {
                DriverTab.DASHBOARD -> {
                    val active = activeRides.firstOrNull()
                    if (active != null) {
                        ActiveRideContent(
                            ride = active,
                            canCancel = viewModel.mayCancel(active),
                            canMarkNoShow = viewModel.mayMarkNoShow(active),
                            onAdvance = { viewModel.advanceRide(active) },
                            onCancel = { viewModel.cancelRide(active) },
                            onNoShow = { viewModel.cancelRide(active, noShow = true) }
                        )
                    } else {
                        DriverDashboard(
                            driver = profile,
                            activeCount = activeRides.size,
                            earnings = earnings,
                            unreadCount = unreadCount,
                            onToggleOnline = viewModel::setAvailability,
                            onOpenNotifications = { showNotifications = true },
                            onRefresh = viewModel::refresh,
                            // Only while there is nothing on file. Once it is
                            // sent, the card lives in Profile and the dashboard
                            // goes back to being about driving.
                            licenceCard = if (profile.hasLicenceImage) null else licenceCard
                        )
                    }
                }
                DriverTab.REQUESTS -> RequestsContent(
                    isOnline = profile.isAvailable,
                    requests = openRequests,
                    accepting = accepting,
                    onAccept = viewModel::acceptRequest,
                    onPurgeExpired = viewModel::purgeExpiredRequests
                )
                DriverTab.HISTORY -> DriverHistoryContent(viewModel)
                DriverTab.SUPPORT -> SupportPanel(
                    userId = userId,
                    reporterType = com.tpc.trikride.models.UserType.DRIVER,
                    viewModel = supportViewModel
                )
                DriverTab.PROFILE -> SettingsScreen(
                    userId = userId,
                    userType = com.tpc.trikride.models.UserType.DRIVER,
                    subtitle = "Tricycle #${profile.tricycleNumber}",
                    onSignOut = onSignOut,
                    extraContent = {
                        DriverCredentialsCard(driver = profile, licence = licenceDoc)
                        Spacer(modifier = Modifier.height(12.dp))
                        licenceCard()
                    }
                )
            }
            }
        }
    }
}

@Composable
private fun DriverBottomBar(
    selected: DriverTab,
    onSelect: (DriverTab) -> Unit,
    requestCount: Int
) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        NavigationBarItem(
            selected = selected == DriverTab.DASHBOARD,
            onClick = { onSelect(DriverTab.DASHBOARD) },
            icon = { Icon(Icons.Filled.Dashboard, contentDescription = "Dashboard") },
            label = { Text("Dashboard") }
        )
        NavigationBarItem(
            selected = selected == DriverTab.REQUESTS,
            onClick = { onSelect(DriverTab.REQUESTS) },
            icon = {
                BadgedBox(badge = {
                    if (requestCount > 0) Badge { Text("$requestCount") }
                }) {
                    Icon(Icons.Filled.Inbox, contentDescription = "Requests")
                }
            },
            label = { Text("Requests") }
        )
        NavigationBarItem(
            selected = selected == DriverTab.HISTORY,
            onClick = { onSelect(DriverTab.HISTORY) },
            icon = { Icon(Icons.Filled.History, contentDescription = "History") },
            label = { Text("History") }
        )
        NavigationBarItem(
            selected = selected == DriverTab.SUPPORT,
            onClick = { onSelect(DriverTab.SUPPORT) },
            icon = { Icon(Icons.Filled.SupportAgent, contentDescription = "Support") },
            label = { Text("Support") }
        )
        NavigationBarItem(
            selected = selected == DriverTab.PROFILE,
            onClick = { onSelect(DriverTab.PROFILE) },
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}

@Composable
private fun DriverDashboard(
    driver: Driver,
    activeCount: Int,
    earnings: Double,
    unreadCount: Int,
    onToggleOnline: (Boolean) -> Unit,
    onOpenNotifications: () -> Unit,
    onRefresh: () -> Unit,
    licenceCard: (@Composable () -> Unit)? = null
) {
  RefreshableBox(isRefreshing = false, onRefresh = onRefresh) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Driver Dashboard", style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            IconButton(onClick = onOpenNotifications) {
                BadgedBox(badge = { if (unreadCount > 0) Badge { Text("$unreadCount") } }) {
                    Icon(Icons.Filled.Notifications, contentDescription = "Notifications")
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Above everything else while it is outstanding: without it the driver
        // cannot be approved, and without approval nothing else on this screen
        // does anything for them.
        licenceCard?.let {
            it()
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Profile + rating + online switch
        SectionCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Person, contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        if (driver.isAvailable) "Online" else "Offline",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (driver.isAvailable) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Star, contentDescription = null,
                            tint = RatingColor, modifier = Modifier.size(16.dp))
                        Text(
                            " %.1f  •  Tricycle #%s".format(Locale.US, driver.rating, driver.tricycleNumber),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Switch(checked = driver.isAvailable, onCheckedChange = onToggleOnline)
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        if (driver.verificationStatus != VerificationStatus.APPROVED) {
            SectionCard {
                Column {
                    Text("Verification: ${driver.verificationStatus}",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary)
                    Text(
                        "Your registration is under review by the administrator. " +
                            "You'll be notified once approved.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Earnings hero
        SectionCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Payments, contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    // Not today's: `earnings` sums every completed ride this
                    // driver has ever finished, because the history flow it
                    // folds is not filtered by date. Labelled for what it is
                    // rather than left saying something untrue.
                    Text("Total Earned", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("₱%.2f".format(Locale.US, earnings), style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("Active Rides", "$activeCount", Modifier.weight(1f))
            StatCard("Total Trips", "${driver.totalRides}", Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(20.dp))

        PrimaryButton(
            text = if (driver.isAvailable) "Go Offline" else "Go Online",
            onClick = { onToggleOnline(!driver.isAvailable) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            if (driver.isAvailable) "You are receiving ride requests."
            else "Go online to start receiving ride requests.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
  }
}

@Composable
private fun DriverHistoryContent(viewModel: DriverViewModel) {
    val history by viewModel.rideHistory.collectAsState()
    val loading by viewModel.loadingHistory.collectAsState()
    val earnings by viewModel.earnings.collectAsState()

    RefreshableBox(isRefreshing = false, onRefresh = viewModel::refresh) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Ride History", style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold)
            Text("Completed and cancelled trips",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(16.dp))

            SectionCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Payments, contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Total Earned", style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("₱%.2f".format(Locale.US, earnings),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            when {
                loading -> repeat(3) {
                    SkeletonCard(lines = 2)
                    Spacer(modifier = Modifier.height(10.dp))
                }
                history.isEmpty() -> SectionCard {
                    Text("No trips yet.", style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                else -> history.forEach { ride ->
                    SectionCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "${ride.pickupLocation.address} to ${ride.dropoffLocation.address}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(ride.status.name.replace('_', ' '),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Text("₱%.2f".format(Locale.US, ride.estimatedFare),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    SectionCard(modifier = modifier) {
        Column {
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun RequestsContent(
    isOnline: Boolean,
    requests: List<RideRequest>,
    accepting: Boolean,
    onAccept: (RideRequest) -> Unit,
    onPurgeExpired: () -> Unit
) {
    val declined = rememberSaveable { mutableStateListOf<String>() }

    // Ticking clock for the countdown timers.
    val nowMs by produceState(initialValue = System.currentTimeMillis()) {
        while (true) {
            value = System.currentTimeMillis()
            delay(1000)
        }
    }

    // Expiry was applied when the node changed and never again, so a card sat
    // at "0s" with Accept still live and a driver could take a request the
    // passenger stopped waiting on twenty minutes earlier.
    val visible = requests.filter {
        it.id !in declined && (it.expiresAt.toLongOrNull() ?: 0L) > nowMs
    }

    // Nothing deleted an expired request, so the node grew for good and every
    // approved driver downloaded all of it. Sweeping while a driver is looking
    // at this tab is the cheapest place to do it.
    LaunchedEffect(Unit) { onPurgeExpired() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text("Incoming Requests", style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        when {
            !isOnline -> SectionCard {
                Text("You are offline. Go online from the Dashboard to receive requests.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            visible.isEmpty() -> SectionCard {
                Text("No ride requests right now. New requests will appear here.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            else -> visible.forEach { request ->
                val expiresAt = request.expiresAt.toLongOrNull() ?: nowMs
                val remaining = ((expiresAt - nowMs) / 1000).coerceAtLeast(0)
                RequestCard(
                    request = request,
                    remainingSeconds = remaining,
                    accepting = accepting,
                    onAccept = { onAccept(request) },
                    onDecline = { declined.add(request.id) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun RequestCard(
    request: RideRequest,
    remainingSeconds: Long,
    accepting: Boolean,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    val fare = request.estimatedFare

    SectionCard {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("New ride request", style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold)
                Text(
                    "${remainingSeconds}s",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (remainingSeconds <= 10) ErrorColor else MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            RouteRowDriver(Icons.Filled.MyLocation, "Pickup", request.pickupLocation.address)
            Spacer(modifier = Modifier.height(8.dp))
            RouteRowDriver(Icons.Filled.LocationOn, "Destination", request.dropoffLocation.address)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Estimated Fare", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("₱%.2f".format(Locale.US, fare), style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Rate", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        if (request.regularCount > 0 || request.discountedCount > 0) {
                            listOfNotNull(
                                request.regularCount.takeIf { it > 0 }?.let { "$it regular" },
                                request.discountedCount.takeIf { it > 0 }
                                    ?.let { "$it senior/PWD/student" }
                            ).joinToString(", ")
                        } else if (request.fareType == FareType.REGULAR) "Regular"
                        else "Senior / PWD / Student",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(pluralPassengers(request.passengerCount),
                    style = MaterialTheme.typography.bodySmall)
                Text("Luggage: ${request.luggage}", style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (request.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Notes: ${request.notes}", style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onDecline,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorColor),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) { Text("Decline") }
                Button(
                    onClick = onAccept,
                    // One tap, one ride. Two taps used to make two.
                    enabled = !accepting,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) { Text(if (accepting) "Accepting…" else "Accept") }
            }
        }
    }
}

@Composable
private fun ActiveRideContent(
    ride: Ride,
    canCancel: Boolean,
    canMarkNoShow: Boolean,
    onAdvance: () -> Unit,
    onCancel: () -> Unit,
    onNoShow: () -> Unit
) {
    val context = LocalContext.current
    var confirming by remember { mutableStateOf<String?>(null) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text("Active Ride", style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Status: ${statusLabel(ride.status)}", style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))

        // Passenger card
        SectionCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Person, contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        ride.passengerName.ifBlank { "Passenger" },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Fare ₱%.2f  •  %s".format(Locale.US, ride.estimatedFare, ride.partyLabel),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (ride.passengerPhone.isNotBlank()) {
                    IconButton(onClick = { Navigation.dial(context, ride.passengerPhone) }) {
                        Icon(Icons.Filled.Phone, contentDescription = "Call the passenger",
                            tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        SectionCard {
            Column {
                RouteRowDriver(Icons.Filled.MyLocation, "Pickup", ride.pickupLocation.address)
                Spacer(modifier = Modifier.height(10.dp))
                RouteRowDriver(Icons.Filled.LocationOn, "Destination", ride.dropoffLocation.address)
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(pluralPassengers(ride.passengerCount), style = MaterialTheme.typography.bodyMedium)
                    Text("Luggage: ${ride.luggage}", style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                if (ride.notes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Passenger notes: ${ride.notes}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        NavigationHandoff(ride)

        Spacer(modifier = Modifier.height(20.dp))

        val actionLabel = when (ride.status) {
            RideStatus.ACCEPTED -> "Head to Pickup"
            RideStatus.DRIVER_ARRIVING -> "Arrived at Pickup"
            RideStatus.DRIVER_ARRIVED -> "Start Ride"
            RideStatus.IN_PROGRESS -> "Complete Ride"
            else -> null
        }
        if (actionLabel != null) {
            PrimaryButton(text = actionLabel, onClick = onAdvance)
        }

        // Before this there was no way out of a ride that was not going to
        // finish. The ride stayed in both parties' active lists for good, and
        // this screen replaced the dashboard, so the driver could not even
        // reach their own online switch.
        if (canMarkNoShow || canCancel) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                if (canMarkNoShow) {
                    OutlinedButton(
                        onClick = { confirming = "no-show" },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorColor),
                        modifier = Modifier.weight(1f)
                    ) { Text("Passenger no-show") }
                }
                if (canCancel) {
                    OutlinedButton(
                        onClick = { confirming = "cancel" },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorColor),
                        modifier = Modifier.weight(1f)
                    ) { Text("Cancel ride") }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }

    confirming?.let { kind ->
        val noShow = kind == "no-show"
        AlertDialog(
            onDismissRequest = { confirming = null },
            title = { Text(if (noShow) "Report a no-show?" else "Cancel this ride?") },
            text = {
                Text(
                    if (noShow) {
                        "Say that you waited at the pickup point and the passenger did " +
                            "not appear. They are told, the ride is closed, and you go " +
                            "back online."
                    } else {
                        "The ride is closed and the passenger is told they can book " +
                            "again. Use this for a breakdown or anything else that stops " +
                            "you finishing."
                    }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    confirming = null
                    if (noShow) onNoShow() else onCancel()
                }) { Text(if (noShow) "Report it" else "Cancel the ride", color = ErrorColor) }
            },
            dismissButton = {
                TextButton(onClick = { confirming = null }) { Text("Keep going") }
            }
        )
    }
}

@Composable
private fun DriverCredentialsCard(driver: Driver, licence: DriverDocument?) {
    Column {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "VEHICLE & CREDENTIALS",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        SectionCard {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Star, contentDescription = null, tint = RatingColor,
                        modifier = Modifier.size(18.dp))
                    Text(" %.1f  •  ${driver.totalRides} trips".format(Locale.US, driver.rating),
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                InfoRow(
                    Icons.Filled.Badge, "Licence Number",
                    licence?.licenceNumber?.ifBlank { null } ?: "—"
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                InfoRow(
                    Icons.Filled.DateRange, "Licence Expiry",
                    licence?.licenceExpiry?.ifBlank { null } ?: "—"
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                InfoRow(Icons.Filled.CreditCard, "Tricycle Number", driver.tricycleNumber)
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                InfoRow(Icons.Filled.CheckCircle, "Verification", driver.verificationStatus.name)
            }
        }
    }
}

@Composable
private fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(label, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value.ifBlank { "—" }, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun RouteRowDriver(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(label, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun DriverOnboardingContent(
    isSubmitting: Boolean,
    error: String?,
    onDismissError: () -> Unit,
    onSubmit: (String, String, String) -> Unit
) {
    var licenseNumber by remember { mutableStateOf("") }
    var licenseExpiry by remember { mutableStateOf("") }
    var tricycleNumber by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text("Driver Registration", style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "Register your details below. An administrator will verify your " +
                "credentials before you can accept passengers.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))

        error?.let {
            SectionCard {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.weight(1f))
                    TextButton(onClick = onDismissError) { Text("Dismiss") }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        TrikTextField(licenseNumber, { licenseNumber = it }, "Driver's License Number", Icons.Filled.Badge)
        Spacer(modifier = Modifier.height(14.dp))
        TrikTextField(licenseExpiry, { licenseExpiry = it }, "License Expiry (MM/YYYY)", Icons.Filled.DateRange)
        Spacer(modifier = Modifier.height(14.dp))
        TrikTextField(tricycleNumber, { tricycleNumber = it }, "Tricycle Body / Plate Number", Icons.Filled.CreditCard)
        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = if (isSubmitting) "Submitting..." else "Submit for Verification",
            onClick = { onSubmit(licenseNumber.trim(), licenseExpiry.trim(), tricycleNumber.trim()) },
            enabled = !isSubmitting && licenseNumber.isNotBlank() &&
                licenseExpiry.isNotBlank() && tricycleNumber.isNotBlank()
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * Opens the driver's own navigation app at whichever end of the ride is next.
 *
 * Before the passenger is aboard that is the pickup; afterwards it is the
 * destination. Nothing is drawn when the relevant point has no coordinates,
 * which is the case for any fare stop the administrator has not positioned, and
 * nothing is drawn when neither app is installed.
 */
@Composable
private fun NavigationHandoff(ride: Ride) {
    val context = LocalContext.current
    val heading = if (ride.status == RideStatus.IN_PROGRESS) {
        ride.dropoffLocation
    } else {
        ride.pickupLocation
    }
    if (!heading.hasCoordinates) return

    val hasWaze = remember { Navigation.isInstalled(context, Navigation.WAZE) }
    val hasMaps = remember { Navigation.isInstalled(context, Navigation.GOOGLE_MAPS) }
    if (!hasWaze && !hasMaps) return

    val label = if (ride.status == RideStatus.IN_PROGRESS) "destination" else "pickup"

    SectionCard {
        Column {
            Text(
                "Navigate to the $label",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                heading.address.ifBlank { "Pinned point" },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                if (hasWaze) {
                    OutlinedButton(
                        onClick = { Navigation.openWaze(context, heading) },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f)
                    ) { Text("Waze") }
                }
                if (hasMaps) {
                    OutlinedButton(
                        onClick = { Navigation.openGoogleMaps(context, heading) },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f)
                    ) { Text("Google Maps") }
                }
            }
        }
    }
}

/**
 * "1 passenger" / "3 passengers".
 *
 * The emoji these replace carried the meaning — a screen reader announces 👥 as
 * "busts in silhouette" and reads "passenger(s)" literally.
 */
private fun pluralPassengers(count: Int): String =
    if (count == 1) "1 passenger" else "$count passengers"

private fun statusLabel(status: RideStatus): String = when (status) {
    RideStatus.ACCEPTED -> "Accepted"
    RideStatus.DRIVER_ARRIVING -> "Heading to pickup"
    RideStatus.DRIVER_ARRIVED -> "Arrived at pickup"
    RideStatus.IN_PROGRESS -> "Ride in progress"
    RideStatus.COMPLETED -> "Completed"
    else -> status.name
}
```

#### `app/src/main/java/com/tpc/trikride/ui/screens/LegalScreen.kt`

```kotlin
package com.tpc.trikride.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

enum class LegalDoc(val title: String) {
    TERMS("Terms & Conditions"),
    PRIVACY("Privacy Policy"),
    COMMUNITY("Safety and Community Guidelines"),
    DRIVER_AGREEMENT("Driver Agreement");

    val body: String
        get() = when (this) {
            TERMS -> TERMS_TEXT
            PRIVACY -> PRIVACY_TEXT
            COMMUNITY -> COMMUNITY_TEXT
            DRIVER_AGREEMENT -> DRIVER_AGREEMENT_TEXT
        }
}

@Composable
fun LegalScreen(doc: LegalDoc, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                doc.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Text(
                text = doc.body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// These four strings are the copy of record: they are what a user reads before
// ticking the box. docs/legal/ holds Markdown copies for reading and printing,
// generated from here by docs/legal/sync.py. Editing any of them means running
// that script and bumping Constants.LEGAL_VERSION, or the revision is shown to
// nobody who has already agreed.

private val TERMS_TEXT = """
Terms and Conditions for TrikRide
Effective Date: July 28, 2026

By creating an account or using TrikRide, you agree to comply with these Terms and Conditions.

1. Eligibility
Users must be:
•  Registered students, faculty, or authorized personnel of the participating institution.
•  Registered and approved drivers for driver accounts.

2. Account Registration
Users agree to:
•  Provide accurate and complete information.
•  Maintain only one active account unless otherwise authorized.
•  Keep login credentials confidential.
•  Notify the administrator immediately if they suspect unauthorized access to their account.

3. Ride Booking
Passengers agree to:
•  Enter accurate pickup and destination locations.
•  Be present at the designated pickup point on time.
•  Treat drivers and fellow passengers with courtesy and respect.

Drivers agree to:
•  Maintain valid registration and any required permits.
•  Arrive at pickup locations promptly whenever possible.
•  Provide safe, respectful, and professional service.
•  Follow all applicable traffic laws and institutional policies.

4. Prohibited Activities
Users shall not:
•  Create fake or fraudulent accounts.
•  Impersonate another person.
•  Submit false booking requests.
•  Harass, threaten, or discriminate against other users.
•  Attempt unauthorized access to the system.
•  Use the application for illegal or unlawful activities.

Violations may result in temporary suspension or permanent removal from the TrikRide platform.

5. Limitation of Liability
TrikRide is a ride scheduling and driver matching platform. While we strive to provide reliable service, we cannot guarantee uninterrupted availability and are not responsible for delays caused by traffic, weather, vehicle issues, or other circumstances beyond our reasonable control.

6. Account Suspension
The system administrator reserves the right to suspend or terminate accounts found to be in violation of these Terms and Conditions.

7. Intellectual Property
All application content, including the TrikRide name, logo, interface design, graphics, source code, and documentation, is owned by the TrikRide development team unless otherwise stated. Unauthorized reproduction or distribution is prohibited.

8. Amendments
These Terms and Conditions may be updated from time to time. Continued use of TrikRide after changes are published constitutes acceptance of the updated Terms.

9. Governing Rules
These Terms shall be governed by applicable Philippine laws and the policies of the participating educational institution.

10. Acceptance
By registering and using TrikRide, you confirm that you have read, understood, and agreed to these Terms and Conditions and the Privacy Policy.
""".trimIndent()

private val COMMUNITY_TEXT = """
TrikRide Safety and Community Guidelines
Effective Date: July 28, 2026

Our Commitment
TrikRide is committed to providing a safe, respectful, and reliable transportation environment for students, drivers, faculty, and staff.

Respect Everyone
•  Treat all users with courtesy and professionalism.
•  Avoid abusive, offensive, discriminatory, or threatening language.
•  Respect personal space and privacy.

Safe Riding
•  Wait at the designated pickup location.
•  Verify the driver's identity before boarding.
•  Follow the driver's safety instructions during the trip.
•  Remain seated while the vehicle is moving.
•  Do not distract the driver while driving.

Driver Responsibilities
Drivers are expected to:
•  Drive safely and obey all traffic laws.
•  Maintain a roadworthy and clean vehicle.
•  Arrive at pickup locations as promptly as possible.
•  Treat every passenger fairly and respectfully.
•  Never operate a vehicle while under the influence of alcohol or illegal drugs.

Passenger Responsibilities
Passengers are expected to:
•  Arrive on time for scheduled pickups.
•  Respect the driver's vehicle and property.
•  Avoid behavior that may endanger others.
•  Report emergencies or unsafe situations immediately.

Prohibited Conduct
The following are strictly prohibited:
•  Violence or physical assault.
•  Sexual harassment or misconduct.
•  Bullying, intimidation, or discrimination.
•  Possession or use of illegal drugs.
•  Carrying dangerous weapons or prohibited items.
•  Vandalism or intentional damage to vehicles.
•  Providing false information or fake bookings.

Reporting Safety Concerns
Users are encouraged to report:
•  Unsafe driving.
•  Harassment or inappropriate behavior.
•  Fake accounts or fraudulent activities.
•  Vehicle safety issues.
•  Lost belongings.

Reports will be reviewed by authorized administrators, and appropriate action may be taken. Use the Support tab to file a report.

Account Enforcement
Violations of these Community Guidelines may result in:
•  Warning notices.
•  Temporary account suspension.
•  Permanent account removal.
•  Referral to school authorities or law enforcement when necessary.

By using TrikRide, all users agree to help maintain a safe, respectful, and welcoming community.
""".trimIndent()

private val DRIVER_AGREEMENT_TEXT = """
TrikRide Driver Agreement
Effective Date: August 16, 2026

This Driver Agreement establishes the responsibilities and expectations for all drivers using the TrikRide platform.

Driver Eligibility
To become a TrikRide driver, you must:
•  Be at least 18 years old.
•  Possess a valid driver's license appropriate for the vehicle operated.
•  Operate a legally registered tricycle or authorized vehicle.
•  Submit a legible photograph of that license for verification, and keep a current one on file.
•  Complete the registration and verification process required by TrikRide.

Your License Photograph
The photograph you submit is checked by an administrator against the license details you entered, and again when the license expires. Only you and an administrator can view it; it is never shown to passengers. If your application is refused it is deleted immediately. If you are approved it is kept while your account is active and deleted with the account; that remains so if your approval is later withdrawn, since the reason for withdrawing it may itself need to be evidenced. You may remove it yourself at any time, though you cannot carry passengers without one on file. Section 9 of the Privacy Policy sets this out in full.

TrikRide checks that a document was presented and that it matches what you entered. It does not and cannot confirm with the Land Transportation Office that a license is current or has not been suspended. Driving on a valid license remains your responsibility, and submitting a false or altered document ends your access to the platform.

Driver Responsibilities
Drivers agree to:
•  Provide accurate personal and vehicle information.
•  Keep account information updated.
•  Drive safely and comply with all traffic laws.
•  Treat all passengers respectfully and without discrimination.
•  Arrive at pickup locations as promptly as possible.
•  Notify passengers through the app if delays occur.
•  Maintain a clean and safe vehicle.

Professional Conduct
Drivers shall:
•  Wear appropriate attire while providing transportation services.
•  Avoid abusive or inappropriate language.
•  Respect passenger privacy.
•  Never ask for personal information unrelated to the ride.

Safety Requirements
Drivers shall never:
•  Drive while under the influence of alcohol or illegal drugs.
•  Allow unauthorized persons to operate their registered vehicle.
•  Accept bookings using another driver's account.
•  Endanger passengers through reckless driving.

Account Suspension or Termination
TrikRide may suspend or terminate a driver's account for:
•  Repeated complaints.
•  Unsafe driving practices.
•  Fraudulent activity.
•  Submission of false documents.
•  Violation of this Agreement or applicable laws.

Limitation of Responsibility
Drivers acknowledge that TrikRide functions as a ride scheduling and matching platform. Drivers remain responsible for complying with all traffic regulations and for the safe operation of their vehicles.

Agreement
By registering as a TrikRide driver, you confirm that you have read, understood, and agreed to abide by this Driver Agreement.
""".trimIndent()

private val PRIVACY_TEXT = """
TrikRide Privacy Policy
Effective Date: August 16, 2026

1. Information We Collect
We collect the information you provide during registration (name, email, phone number, date of birth, and — for drivers — license and tricycle details), an optional profile photo, and information generated while using the app (ride requests, pickup/destination, ride history, and any concerns you report).

Drivers are also asked for a photograph of their driver's license. Under the Data Privacy Act of 2012 (Republic Act No. 10173) a license is sensitive personal information, so it is treated separately from everything else in this policy and is covered by section 9 below. You are asked to agree to it specifically at the moment you send it, not merely by accepting this policy.

2. How We Use Information
Your information is used to create your account, match passengers with drivers, price rides, support driver verification, and improve the service.

3. Location
Location is used to show pickup/destination and, for drivers, availability. Location is only used while you are using the relevant features of the app.

4. Data Storage
Account, ride, and profile photo data are stored in Google Firebase. A profile photo is reduced to a small thumbnail before it is stored. Communications with the server are encrypted in transit. TrikRide does not collect card, bank, or any other payment details; fares are paid in cash directly to the driver.

5. Sharing
A passenger's ride details are shared with the assigned driver (and vice versa) to complete the ride. Administrators can view driver records and ride logs to operate and monitor the service. We do not sell your personal information.

6. Your Choices
You can edit your profile details and sign out at any time. You may request account concerns or corrections through the Support feature.

7. Children
The service is intended for members of the college community and is not directed at children under 13.

8. Contact
For privacy questions, contact the TrikRide support hotline listed in the app.

9. Driver's License Photographs
This section applies only to drivers, and only to the photograph of the license itself.

Purpose. The photograph is used for one thing: to confirm that the person applying to carry passengers holds the license they say they hold, and to check it again when that license expires. It is not used for anything else.

Who can see it. You, and a TrikRide administrator. It is never shown to passengers, never attached to a ride, and never included in any exported report.

How long we keep it. If your application is refused, the photograph is deleted at the moment of that decision. If you are approved, it is kept while your account is active, because it is needed again at renewal and if a concern about a ride is ever disputed. Withdrawing an approval already given is not the same as refusing an application and does not delete the photograph, for that same reason. It is deleted with your account.

Your control. You may remove the photograph yourself at any time from your driver profile. Removing it means you cannot be approved to carry passengers until you provide another one.

How it is stored. Separately from your account record, so that ordinary use of the app never reads it, and reduced in size before it is stored. Communications are encrypted in transit.
""".trimIndent()
```

#### `app/src/main/java/com/tpc/trikride/ui/screens/MainAppScreen.kt`

```kotlin
package com.tpc.trikride.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tpc.trikride.models.UserType
import com.tpc.trikride.ui.components.PrimaryButton
import com.tpc.trikride.ui.components.SectionCard
import com.tpc.trikride.ui.components.TrikTextField
import com.tpc.trikride.utils.AuthPrefs
import com.tpc.trikride.utils.BirthDate
import com.tpc.trikride.utils.PasswordRules
import com.tpc.trikride.viewmodels.AuthViewModel
import com.tpc.trikride.viewmodels.ConsentViewModel

private enum class AppScreen { LOGIN, REGISTER, ACCOUNT_SELECTION }

/** Fills the window with the theme background for the frame before routing. */
@Composable
private fun StartupSurface() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    )
}

private data class RegistrationData(
    val fullName: String,
    val birthDate: String,
    val email: String,
    val phone: String,
    val password: String
)

@Composable
fun MainAppScreen(
    authViewModel: AuthViewModel = viewModel(),
    consentViewModel: ConsentViewModel = viewModel()
) {
    val state by authViewModel.state.collectAsState()
    val consent by consentViewModel.state.collectAsState()
    val context = LocalContext.current
    var screen by remember { mutableStateOf(AppScreen.LOGIN) }
    var pendingReg by remember { mutableStateOf<RegistrationData?>(null) }
    var seenOnboarding by remember { mutableStateOf(AuthPrefs.hasSeenOnboarding(context)) }

    val uid = state.userId
    val type = state.userType

    // Start the consent check the moment we know who is signed in, so it
    // overlaps the rest of start-up rather than following it.
    LaunchedEffect(uid, type) {
        if (uid != null && type != null) consentViewModel.check(uid, type)
    }

    // Start-up has two waits — restoring the session, then reading what the
    // account has agreed to — but they are one wait as far as anyone looking at
    // the screen is concerned.
    val settlingConsent = uid != null && type != null && consent.isChecking
    if (state.isBootstrapping || settlingConsent) {
        // Someone signed in gets the welcome artwork while that happens. With no
        // session there is nothing to wait for — the check is a synchronous read
        // — so a plain surface passes in a frame instead of holding a branded
        // screen in front of someone who just wants to get on with it.
        if (uid != null || state.hasExistingSession) WelcomeBackScreen() else StartupSurface()
        return
    }

    // First launch for a signed-out user: run the carousel once.
    if (uid == null && !seenOnboarding) {
        OnboardingScreen(
            onFinish = {
                AuthPrefs.setSeenOnboarding(context)
                seenOnboarding = true
            }
        )
        return
    }

    // Signed in with a known account type. Before the dashboard, the account
    // has to have agreed to the current legal documents — which catches both
    // accounts made before consent was tracked and any later amendment.
    if (uid != null && type != null) {
        if (consent.needsConsent) {
            ConsentScreen(
                includeLegal = consent.needsLegal,
                includeDriverAgreement = consent.needsDriverAgreement,
                isSaving = consent.isSaving,
                error = consent.error,
                onAccept = { consentViewModel.accept(uid, type) },
                unreadable = consent.unreadable,
                onRetry = { consentViewModel.retry(uid, type) },
                onDecline = {
                    consentViewModel.reset()
                    authViewModel.signOut()
                    screen = AppScreen.LOGIN
                }
            )
            return
        }

        when (type) {
            UserType.PASSENGER -> PassengerHomeScreen(
                userId = uid,
                onSignOut = { consentViewModel.reset(); authViewModel.signOut() }
            )
            UserType.DRIVER -> DriverHomeScreen(
                userId = uid,
                onSignOut = { consentViewModel.reset(); authViewModel.signOut() }
            )
            UserType.ADMIN -> AdminDashboardScreen(
                userId = uid,
                onSignOut = { consentViewModel.reset(); authViewModel.signOut() }
            )
        }
        return
    }

    // Signed in but no account type stored yet → pick one (persists to DB).
    if (uid != null && state.needsAccountType) {
        AccountSelectionScreen(
            isLoading = state.isLoading,
            error = state.error,
            onSelect = { authViewModel.chooseAccountType(it) },
            onBack = {
                consentViewModel.reset()
                authViewModel.signOut()
                screen = AppScreen.LOGIN
            }
        )
        return
    }

    when (screen) {
        AppScreen.LOGIN -> LoginScreen(
            isLoading = state.isLoading,
            error = state.error,
            resetNotice = state.resetNotice,
            onLogin = { email, password -> authViewModel.login(email, password) },
            onForgotPassword = { authViewModel.sendPasswordReset(it) },
            onDismissResetNotice = { authViewModel.clearResetNotice() },
            onRegisterClick = { authViewModel.clearError(); screen = AppScreen.REGISTER }
        )
        AppScreen.REGISTER -> RegisterScreen(
            isLoading = state.isLoading,
            error = state.error,
            onNext = { data -> pendingReg = data; authViewModel.clearError(); screen = AppScreen.ACCOUNT_SELECTION },
            onLoginClick = { authViewModel.clearError(); screen = AppScreen.LOGIN }
        )
        AppScreen.ACCOUNT_SELECTION -> AccountSelectionScreen(
            isLoading = state.isLoading,
            error = state.error,
            onSelect = { type ->
                val reg = pendingReg
                if (reg != null && !state.isLoading) {
                    authViewModel.register(
                        reg.fullName, reg.birthDate,
                        reg.email, reg.phone, reg.password, type
                    )
                }
            },
            onBack = { authViewModel.clearError(); screen = AppScreen.REGISTER }
        )
    }
}

@Composable
private fun LoginScreen(
    isLoading: Boolean,
    error: String?,
    resetNotice: String?,
    onLogin: (String, String) -> Unit,
    onForgotPassword: (String) -> Unit,
    onDismissResetNotice: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf(AuthPrefs.rememberedEmail(context)) }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(email.isNotBlank()) }
    var askingReset by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("Login", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "Welcome back! Please login to your account",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(32.dp))

        TrikTextField(email, { email = it }, "Email", Icons.Filled.Email, keyboardType = KeyboardType.Email)
        Spacer(modifier = Modifier.height(16.dp))
        TrikTextField(password, { password = it }, "Password", Icons.Filled.Lock, isPassword = true)
        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = rememberMe, onCheckedChange = { rememberMe = it })
            Text("Remember me", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = { askingReset = true }) {
                Text("Forgot Password?", color = MaterialTheme.colorScheme.primary)
            }
        }

        ErrorText(error)
        Spacer(modifier = Modifier.height(16.dp))

        PrimaryButton(
            text = if (isLoading) "Logging in..." else "Login",
            onClick = {
                if (rememberMe) AuthPrefs.setRememberedEmail(context, email.trim())
                else AuthPrefs.clearRememberedEmail(context)
                onLogin(email, password)
            },
            enabled = !isLoading && email.isNotBlank() && password.isNotBlank()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "Don't have an account? ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Register",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable(onClick = onRegisterClick)
            )
        }
    }

    if (askingReset) {
        var resetEmail by remember { mutableStateOf(email) }
        AlertDialog(
            onDismissRequest = { askingReset = false },
            title = { Text("Reset your password") },
            text = {
                Column {
                    Text(
                        "We will email you a link to set a new one.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    TrikTextField(
                        resetEmail, { resetEmail = it }, "Email",
                        Icons.Filled.Email, keyboardType = KeyboardType.Email
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { askingReset = false; onForgotPassword(resetEmail) },
                    enabled = resetEmail.isNotBlank() && !isLoading
                ) { Text("Send link") }
            },
            dismissButton = {
                TextButton(onClick = { askingReset = false }) { Text("Cancel") }
            }
        )
    }

    if (resetNotice != null) {
        AlertDialog(
            onDismissRequest = onDismissResetNotice,
            title = { Text("Check your email") },
            text = { Text(resetNotice) },
            confirmButton = {
                TextButton(onClick = onDismissResetNotice) { Text("OK") }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegisterScreen(
    isLoading: Boolean,
    error: String?,
    onNext: (RegistrationData) -> Unit,
    onLoginClick: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var accepted by remember { mutableStateOf(false) }
    var showDoc by remember { mutableStateOf<LegalDoc?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    // Show a legal doc as an overlay without losing the typed form.
    showDoc?.let { doc ->
        LegalScreen(doc = doc, onBack = { showDoc = null })
        return
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        birthDate = BirthDate.fromPickerUtc(it)
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    val passwordsMatch = password.isNotBlank() && password == confirm
    val strong = PasswordRules.isStrong(password)
    // The picker used to accept 2090 and 1850 alike, and birthdate is the only
    // basis the system has for a senior's entitlement.
    val birthDateProblem = if (birthDate.isBlank()) null else BirthDate.reject(birthDate)
    val canSubmit = !isLoading && fullName.isNotBlank() && email.isNotBlank() &&
        phone.isNotBlank() && birthDate.isNotBlank() && birthDateProblem == null &&
        passwordsMatch && strong && accepted

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onLoginClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Register", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Create your account", style = MaterialTheme.typography.titleMedium)
        Text(
            "Fill in the details to get started",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))

        TrikTextField(fullName, { fullName = it }, "Full Name", Icons.Filled.Person)
        Spacer(modifier = Modifier.height(14.dp))
        DateField(
            label = "Birthdate",
            value = if (birthDate.isBlank()) "" else BirthDate.display(birthDate),
            onClick = { showDatePicker = true }
        )
        birthDateProblem?.let {
            Spacer(modifier = Modifier.height(6.dp))
            Text(it, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(14.dp))
        TrikTextField(email, { email = it }, "Email", Icons.Filled.Email, keyboardType = KeyboardType.Email)
        Spacer(modifier = Modifier.height(14.dp))
        TrikTextField(phone, { phone = it }, "Phone Number", Icons.Filled.Phone, keyboardType = KeyboardType.Phone)
        Spacer(modifier = Modifier.height(14.dp))
        TrikTextField(password, { password = it }, "Password", Icons.Filled.Lock, isPassword = true)

        if (password.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            PasswordRules.evaluate(password).forEach { check ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        if (check.passed) "✓ " else "• ",
                        color = if (check.passed) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        check.label,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (check.passed) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        TrikTextField(confirm, { confirm = it }, "Confirm Password", Icons.Filled.Lock, isPassword = true)

        if (confirm.isNotBlank() && !passwordsMatch) {
            Spacer(modifier = Modifier.height(6.dp))
            Text("Passwords do not match", style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.Top) {
            Checkbox(checked = accepted, onCheckedChange = { accepted = it })
            Column(modifier = Modifier.padding(top = 12.dp)) {
                Text("I have read and agree to TrikRide's",
                    style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(2.dp))
                Text(LegalDoc.TERMS.title, style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { showDoc = LegalDoc.TERMS })
                Text(LegalDoc.PRIVACY.title, style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { showDoc = LegalDoc.PRIVACY })
                Text(LegalDoc.COMMUNITY.title, style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { showDoc = LegalDoc.COMMUNITY })
                Spacer(modifier = Modifier.height(4.dp))
                Text("Tap a title to read it.", style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        ErrorText(error)
        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = "Continue",
            onClick = {
                onNext(
                    RegistrationData(
                        fullName.trim(), birthDate,
                        email.trim(), phone.trim(), password
                    )
                )
            },
            enabled = canSubmit
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                "Already have an account? ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Login",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable(onClick = onLoginClick)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun AccountSelectionScreen(
    isLoading: Boolean,
    error: String?,
    onSelect: (UserType) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        IconButton(onClick = onBack, enabled = !isLoading) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Choose Account Type", style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "Select the type of account you want to use",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(32.dp))

        AccountTypeCard(
            icon = Icons.AutoMirrored.Filled.DirectionsWalk,
            title = "I'm a Passenger",
            subtitle = "Book rides and travel around the campus",
            enabled = !isLoading,
            onClick = { onSelect(UserType.PASSENGER) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        AccountTypeCard(
            icon = Icons.Filled.LocalTaxi,
            title = "I'm a Driver",
            subtitle = "Provide rides and earn income",
            enabled = !isLoading,
            onClick = { onSelect(UserType.DRIVER) }
        )

        if (isLoading) {
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Creating your account...", style = MaterialTheme.typography.bodyMedium)
            }
        }

        ErrorText(error)
    }
}

@Composable
private fun AccountTypeCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    SectionCard(
        modifier = Modifier.clickable(enabled = enabled, onClick = onClick),
        contentPadding = PaddingValues(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.Filled.ChevronRight, contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun DateField(label: String, value: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Filled.CalendarMonth,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = value.ifBlank { label },
            style = MaterialTheme.typography.bodyLarge,
            color = if (value.isBlank()) MaterialTheme.colorScheme.onSurfaceVariant
            else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun ErrorText(error: String?) {
    if (error != null) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = error,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )
    }
}
```

#### `app/src/main/java/com/tpc/trikride/ui/screens/NotificationsScreen.kt`

```kotlin
package com.tpc.trikride.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tpc.trikride.models.AppNotification
import com.tpc.trikride.models.NotificationType
import com.tpc.trikride.ui.components.RefreshableBox
import com.tpc.trikride.ui.components.SectionCard
import com.tpc.trikride.ui.components.SkeletonCard
import com.tpc.trikride.viewmodels.SupportViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NotificationsScreen(
    userId: String,
    viewModel: SupportViewModel = viewModel(),
    onBack: (() -> Unit)? = null
) {
    LaunchedEffect(userId) { viewModel.bind(userId) }

    val notifications by viewModel.notifications.collectAsState()
    val loading by viewModel.loadingNotifications.collectAsState()

    val sorted = notifications.sortedByDescending { it.createdAt.toLongOrNull() ?: 0L }
    val unread = sorted.count { !it.read }

    RefreshableBox(isRefreshing = false, onRefresh = viewModel::refreshNotifications) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (onBack != null) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Notifications",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        if (unread > 0) "$unread unread" else "You are all caught up",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (unread > 0) {
                    TextButton(onClick = viewModel::markAllRead) { Text("Mark all read") }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            when {
                loading -> {
                    repeat(3) {
                        SkeletonCard(lines = 2)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
                sorted.isEmpty() -> {
                    SectionCard {
                        Column {
                            Text(
                                "Nothing here yet",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Ride updates and replies to your concerns will show up here.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                else -> sorted.forEach { item ->
                    NotificationRow(item) { viewModel.markRead(item.id) }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
private fun NotificationRow(item: AppNotification, onClick: () -> Unit) {
    SectionCard(modifier = Modifier.clickable(onClick = onClick)) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (item.type) {
                        NotificationType.RIDE -> Icons.Filled.DirectionsBike
                        NotificationType.COMPLAINT -> Icons.Filled.ReportProblem
                        NotificationType.ACCOUNT -> Icons.Filled.AccountCircle
                        NotificationType.GENERAL -> Icons.Filled.Notifications
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    item.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = if (item.read) FontWeight.Normal else FontWeight.Bold
                )
                Text(
                    item.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    formatWhen(item.createdAt),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (!item.read) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
        }
    }
}

private fun formatWhen(millis: String): String {
    val value = millis.toLongOrNull() ?: return ""
    val sdf = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
    return sdf.format(Date(value))
}
```

#### `app/src/main/java/com/tpc/trikride/ui/screens/OnboardingScreen.kt`

```kotlin
package com.tpc.trikride.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tpc.trikride.R
import com.tpc.trikride.ui.theme.EmeraldGreen
import kotlinx.coroutines.launch

/**
 * The sky the slides open on, sampled from the artwork itself.
 *
 * The container shows through wherever the image does not reach the edge of a
 * particular screen, and black bars against pale artwork look like a fault.
 */
private val SLIDE_BACKDROP = Color(0xFFEBF3FA)

/** Slides shown on first launch, in order. Text is part of the artwork. */
private val ONBOARDING_SLIDES = listOf(
    R.drawable.onboarding_1,
    R.drawable.onboarding_2,
    R.drawable.onboarding_3,
    R.drawable.onboarding_4,
    R.drawable.onboarding_5
)

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { ONBOARDING_SLIDES.size })
    val scope = rememberCoroutineScope()
    val lastPage = ONBOARDING_SLIDES.lastIndex
    val onLastPage = pagerState.currentPage == lastPage

    Box(modifier = Modifier.fillMaxSize().background(SLIDE_BACKDROP)) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Image(
                painter = painterResource(id = ONBOARDING_SLIDES[page]),
                contentDescription = "Onboarding slide ${page + 1}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Scrim so the controls stay readable over any artwork.
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(190.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.65f))
                    )
                )
        )

        if (!onLastPage) {
            TextButton(
                onClick = onFinish,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 12.dp, end = 8.dp)
            ) {
                // Dark, because the top of every slide is a pale sky. White
                // here disappeared entirely against the new artwork.
                Text(
                    "Skip",
                    color = Color(0xFF1B3A2A),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ONBOARDING_SLIDES.indices.forEach { index ->
                    val selected = index == pagerState.currentPage
                    Box(
                        modifier = Modifier
                            .height(8.dp)
                            .width(if (selected) 22.dp else 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (selected) EmeraldGreen else Color.White.copy(alpha = 0.55f)
                            )
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (onLastPage) {
                        onFinish()
                    } else {
                        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = EmeraldGreen,
                    contentColor = Color.White
                ),
                shape = CircleShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                Text(
                    if (onLastPage) "Get Started" else "Next",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/** Full-bleed welcome artwork shown while a returning user's session loads. */
@Composable
fun WelcomeBackScreen() {
    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Image(
            painter = painterResource(id = R.drawable.welcome_back),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(240.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f))
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 56.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Welcome back",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "Getting things ready...",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.85f)
            )
            Spacer(modifier = Modifier.height(20.dp))
            androidx.compose.material3.CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 3.dp,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
```

#### `app/src/main/java/com/tpc/trikride/ui/screens/PassengerHomeScreen.kt`

```kotlin
package com.tpc.trikride.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tpc.trikride.models.FareConfig
import com.tpc.trikride.models.FareStop
import com.tpc.trikride.models.FareType
import com.tpc.trikride.models.Location
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.RideStatus
import com.tpc.trikride.models.UserType
import com.tpc.trikride.ui.components.MapPin
import com.tpc.trikride.ui.components.PickerMap
import com.tpc.trikride.ui.components.PrimaryButton
import com.tpc.trikride.ui.components.RefreshableBox
import com.tpc.trikride.ui.components.SecondaryButton
import com.tpc.trikride.ui.components.SectionCard
import com.tpc.trikride.ui.components.SkeletonCard
import com.tpc.trikride.ui.components.SupportPanel
import com.tpc.trikride.ui.components.TALIBON_CENTRE
import com.tpc.trikride.ui.components.TrikMap
import com.tpc.trikride.ui.theme.EmeraldGreen
import com.tpc.trikride.ui.theme.ForestGreen
import com.tpc.trikride.ui.theme.RatingColor
import com.tpc.trikride.utils.Constants
import com.tpc.trikride.utils.FareEngine
import com.tpc.trikride.utils.LocationProvider
import com.tpc.trikride.utils.LocationUtils
import com.tpc.trikride.utils.Navigation
import com.tpc.trikride.utils.ReverseGeocoder
import com.tpc.trikride.viewmodels.PassengerViewModel
import com.tpc.trikride.viewmodels.SupportViewModel
import kotlinx.coroutines.launch
import java.util.Locale

private enum class PassengerTab { HOME, HISTORY, SUPPORT, PROFILE }

@Composable
fun PassengerHomeScreen(
    userId: String,
    onSignOut: () -> Unit,
    viewModel: PassengerViewModel = viewModel(),
    supportViewModel: SupportViewModel = viewModel()
) {
    LaunchedEffect(userId) {
        viewModel.bind(userId)
        supportViewModel.bind(userId)
    }

    val activeRides by viewModel.activeRides.collectAsState()
    val pendingRequest by viewModel.pendingRequest.collectAsState()
    val error by viewModel.errorMessage.collectAsState()
    val fareConfig by viewModel.fareConfig.collectAsState()
    val fareStops by viewModel.fareStops.collectAsState()
    val driverLocation by viewModel.driverLocation.collectAsState()
    val assignedDriver by viewModel.assignedDriver.collectAsState()
    val ratedRides by viewModel.ratedRides.collectAsState()
    // The dashboard below is only reached when there is no active ride, so the
    // active list it used to be given was empty by construction and "Recent
    // Rides" read "No rides yet" for everybody. Finished rides are what the
    // heading means.
    val rideHistory by viewModel.rideHistory.collectAsState()

    val notifications by supportViewModel.notifications.collectAsState()
    val unreadCount = notifications.count { !it.read }

    var tab by remember { mutableStateOf(PassengerTab.HOME) }
    var showBooking by remember { mutableStateOf(false) }
    var showNotifications by remember { mutableStateOf(false) }
    var completedRide by remember { mutableStateOf<Ride?>(null) }
    var lastActive by remember { mutableStateOf<Ride?>(null) }

    LaunchedEffect(activeRides) {
        val current = activeRides.firstOrNull()
        if (current != null) {
            lastActive = current
        } else if (lastActive != null && completedRide == null) {
            completedRide = lastActive
        }
        viewModel.clearPendingRequestIfMatched()
    }

    if (showNotifications) {
        NotificationsScreen(
            userId = userId,
            viewModel = supportViewModel,
            onBack = { showNotifications = false }
        )
        return
    }

    Scaffold(
        bottomBar = { PassengerBottomBar(selected = tab, onSelect = { tab = it }) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (tab) {
                PassengerTab.HOME -> {
                    val active = activeRides.firstOrNull()
                    when {
                        completedRide != null -> RideCompleteContent(
                            ride = completedRide!!,
                            alreadyRated = completedRide!!.id in ratedRides,
                            onRate = { stars -> viewModel.rateRide(completedRide!!, stars) },
                            onBackHome = { completedRide = null; lastActive = null }
                        )
                        active != null -> RideTrackingContent(
                            ride = active,
                            driverLocation = driverLocation,
                            driver = assignedDriver,
                            canCancel = viewModel.mayCancel(active),
                            onCancel = { viewModel.cancelRide(active) }
                        )
                        pendingRequest != null -> SearchingContent(onCancel = viewModel::cancelPendingRequest)
                        showBooking -> BookingContent(
                            error = error,
                            fareConfig = fareConfig,
                            fareStops = fareStops,
                            onDismissError = viewModel::dismissError,
                            onConfirm = { p, stop, regular, discounted, luggage, notes ->
                                viewModel.requestRide(p, stop, regular, discounted, luggage, notes)
                                showBooking = false
                            },
                            onBack = { showBooking = false }
                        )
                        else -> PassengerDashboard(
                            rides = rideHistory,
                            unreadCount = unreadCount,
                            onBookRide = { showBooking = true },
                            onOpenNotifications = { showNotifications = true },
                            onRefresh = viewModel::refresh
                        )
                    }
                }
                PassengerTab.HISTORY -> RideHistoryContent(viewModel)
                PassengerTab.SUPPORT -> SupportPanel(
                    userId = userId,
                    reporterType = UserType.PASSENGER,
                    viewModel = supportViewModel
                )
                PassengerTab.PROFILE -> SettingsScreen(
                    userId = userId,
                    userType = com.tpc.trikride.models.UserType.PASSENGER,
                    subtitle = "Talibon Polytechnic College",
                    onSignOut = onSignOut
                )
            }
        }
    }
}

@Composable
private fun PassengerBottomBar(selected: PassengerTab, onSelect: (PassengerTab) -> Unit) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        NavigationBarItem(
            selected = selected == PassengerTab.HOME,
            onClick = { onSelect(PassengerTab.HOME) },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = selected == PassengerTab.HISTORY,
            onClick = { onSelect(PassengerTab.HISTORY) },
            icon = { Icon(Icons.Filled.History, contentDescription = "History") },
            label = { Text("History") }
        )
        NavigationBarItem(
            selected = selected == PassengerTab.SUPPORT,
            onClick = { onSelect(PassengerTab.SUPPORT) },
            icon = { Icon(Icons.Filled.SupportAgent, contentDescription = "Support") },
            label = { Text("Support") }
        )
        NavigationBarItem(
            selected = selected == PassengerTab.PROFILE,
            onClick = { onSelect(PassengerTab.PROFILE) },
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}

@Composable
private fun PassengerDashboard(
    rides: List<Ride>,
    unreadCount: Int,
    onBookRide: () -> Unit,
    onOpenNotifications: () -> Unit,
    onRefresh: () -> Unit
) {
    RefreshableBox(isRefreshing = false, onRefresh = onRefresh) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Hello!",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Where are you headed today?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onOpenNotifications) {
                    BadgedBox(badge = { if (unreadCount > 0) Badge { Text("$unreadCount") } }) {
                        Icon(Icons.Filled.Notifications, contentDescription = "Notifications")
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            Card(
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onBookRide)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.horizontalGradient(listOf(ForestGreen, EmeraldGreen)))
                        .padding(24.dp)
                ) {
                    Column {
                        Text(
                            text = "Where to?",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Book a ride now",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Recent Rides",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            if (rides.isEmpty()) {
                SectionCard {
                    Column {
                        Text(
                            "No rides yet",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Book your first tricycle ride around campus.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                rides.take(3).forEach { ride ->
                    RideRow(ride)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun RideHistoryContent(viewModel: PassengerViewModel) {
    val history by viewModel.rideHistory.collectAsState()
    val loading by viewModel.loadingHistory.collectAsState()

    RefreshableBox(isRefreshing = false, onRefresh = viewModel::refresh) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Ride History", style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold)
            Text("Your past trips", style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(16.dp))

            when {
                loading -> repeat(3) {
                    SkeletonCard(lines = 2)
                    Spacer(modifier = Modifier.height(10.dp))
                }
                history.isEmpty() -> SectionCard {
                    Text(
                        "No completed rides yet.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                else -> history.forEach { ride ->
                    RideRow(ride)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun RideRow(ride: Ride) {
    SectionCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "${ride.pickupLocation.address} to ${ride.dropoffLocation.address}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    ride.status.name.replace('_', ' '),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                "P%.2f".format(Locale.US, ride.estimatedFare),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/** Matches the ceiling the database rules enforce on a ride note. */
private const val MAX_NOTE_LENGTH = 500

private val LUGGAGE_OPTIONS = listOf(
    "Backpack", "Large Bag", "Shopping Bags", "Box / Package", "Market Goods"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BookingContent(
    error: String?,
    fareConfig: FareConfig,
    fareStops: List<FareStop>,
    onDismissError: () -> Unit,
    onConfirm: (Location, FareStop, Int, Int, String, String) -> Unit,
    onBack: () -> Unit
) {
    var pickup by remember { mutableStateOf<Location?>(null) }
    var destination by remember { mutableStateOf<FareStop?>(null) }
    // Two counters rather than one rate switch: a party can hold both kinds at
    // once, and the posted sheet prices them from different columns.
    var regularCount by remember { mutableStateOf(1) }
    var discountedCount by remember { mutableStateOf(0) }
    val passengerCount = regularCount + discountedCount
    val selectedLuggage = remember { mutableStateListOf<String>() }
    var notes by remember { mutableStateOf("") }
    var pickingDestination by remember { mutableStateOf(false) }
    var pickingPickup by remember { mutableStateOf(false) }
    var pinningPickup by remember { mutableStateOf(false) }
    var pinningDestination by remember { mutableStateOf(false) }

    // The sheet carries two rates that are not tied to a numbered stop, so they
    // are offered alongside the rest rather than being admin-only trivia.
    val bookable = remember(fareStops, fareConfig) {
        FareEngine.flatStops(fareConfig) + fareStops.filter { it.active }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text("Book Ride", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(12.dp))

        TrikMap(
            height = 180.dp,
            connectPins = pickup?.hasCoordinates == true && destination?.hasCoordinates == true,
            pins = buildList {
                pickup?.takeIf { it.hasCoordinates }
                    ?.let { add(MapPin(it, "Pickup", EmeraldGreen)) }
                destination?.takeIf { it.hasCoordinates }
                    ?.let { add(MapPin(it.location, it.name, ForestGreen, emphasis = true)) }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        error?.let {
            SectionCard {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.weight(1f))
                    TextButton(onClick = onDismissError) { Text("Dismiss") }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        if (bookable.isEmpty()) {
            SectionCard {
                Column {
                    Text(
                        "Destinations are not available yet",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "The fare table has not been loaded. Ask the administrator to " +
                            "publish the official FeTODAT rates.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Same searchable list as the destination. Pickup used to come from a
        // short fixed list of campus points, which was wrong for anyone not
        // starting at the campus.
        StopField(
            label = "Pickup Location",
            value = pickup?.address.orEmpty(),
            placeholder = "Choose where to be collected",
            icon = Icons.Filled.MyLocation,
            enabled = bookable.isNotEmpty(),
            onClick = { pickingPickup = true }
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = { pinningPickup = true }) {
                Icon(Icons.Filled.Place, contentDescription = null,
                    modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Not on the list? Pin it on the map")
            }
        }
        Spacer(modifier = Modifier.height(6.dp))

        StopField(
            label = "Destination",
            value = destination?.label.orEmpty(),
            placeholder = "Choose a stop",
            icon = Icons.Filled.LocationOn,
            enabled = bookable.isNotEmpty(),
            onClick = { pickingDestination = true }
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = { pinningDestination = true },
                enabled = bookable.isNotEmpty()
            ) {
                Icon(Icons.Filled.Map, contentDescription = null,
                    modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Don't know the name? Find it on the map")
            }
        }
        // No fare is shown at all until a destination is chosen, which is the
        // moment someone assumes the minimum is the price.
        if (destination == null) {
            val minimum = "₱%.0f".format(Locale.US, FareEngine.minimumFor(fareConfig, FareType.REGULAR))
            Text(
                "The fare depends on where you are going. $minimum is the minimum, " +
                    "not a flat rate, and longer trips cost more. Choose a destination " +
                    "and the exact amount appears here.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Who is travelling. A type is chosen first and its counter appears
        // with it, so a party of one kind never sees a second counter sitting
        // at zero. A count of zero and an unchosen type mean the same thing to
        // the fare engine; the chip is what the passenger reasons about.
        SectionCard {
            Column {
                Text("Who is travelling", style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold)
                Text(
                    "Choose every kind of passenger in your party. Seniors, persons " +
                        "with disabilities and students pay the discounted column of the " +
                        "posted sheet — bring the ID, the driver will ask for it.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = regularCount > 0,
                        onClick = {
                            // Never leave the tricycle empty: the last kind
                            // standing cannot be unchosen.
                            if (regularCount > 0) {
                                if (discountedCount > 0) regularCount = 0
                            } else if (passengerCount < Constants.MAX_PASSENGERS) {
                                regularCount = 1
                            }
                        },
                        enabled = regularCount > 0 ||
                            passengerCount < Constants.MAX_PASSENGERS,
                        label = { Text("Regular") }
                    )
                    FilterChip(
                        selected = discountedCount > 0,
                        onClick = {
                            if (discountedCount > 0) {
                                if (regularCount > 0) discountedCount = 0
                            } else if (passengerCount < Constants.MAX_PASSENGERS) {
                                discountedCount = 1
                            }
                        },
                        enabled = discountedCount > 0 ||
                            passengerCount < Constants.MAX_PASSENGERS,
                        label = { Text("Senior / PWD / Student") }
                    )
                }

                if (regularCount > 0) {
                    Spacer(modifier = Modifier.height(12.dp))
                    PassengerCounter(
                        label = "Regular",
                        count = regularCount,
                        canAdd = passengerCount < Constants.MAX_PASSENGERS,
                        canRemove = regularCount > 1,
                        onAdd = { regularCount++ },
                        onRemove = { regularCount-- }
                    )
                }
                if (discountedCount > 0) {
                    Spacer(modifier = Modifier.height(12.dp))
                    PassengerCounter(
                        label = "Senior / PWD / Student",
                        count = discountedCount,
                        canAdd = passengerCount < Constants.MAX_PASSENGERS,
                        canRemove = discountedCount > 1,
                        onAdd = { discountedCount++ },
                        onRemove = { discountedCount-- }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    if (passengerCount >= Constants.MAX_PASSENGERS) {
                        "Full — ${Constants.MAX_PASSENGERS} seats is the most a tricycle takes"
                    } else {
                        "$passengerCount of ${Constants.MAX_PASSENGERS} seats" +
                            if (fareConfig.chargePerPassenger) ", charged per head" else ""
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        // Luggage
        SectionCard {
            Column {
                Text("Luggage / Items to Carry", style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold)
                Text("Select all that apply — the driver will be notified",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    LUGGAGE_OPTIONS.forEach { option ->
                        FilterChip(
                            selected = option in selectedLuggage,
                            onClick = {
                                if (option in selectedLuggage) selectedLuggage.remove(option)
                                else selectedLuggage.add(option)
                            },
                            label = { Text(option) }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = notes,
            // Capped where the user can see it. The database refuses anything
            // longer, and a rejected write is a worse way to learn about a limit.
            onValueChange = { if (it.length <= MAX_NOTE_LENGTH) notes = it },
            label = { Text("Notes for driver (optional)") },
            supportingText = { Text("${notes.length} / $MAX_NOTE_LENGTH") },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        val from = pickup
        val to = destination
        val valid = from != null && to != null

        if (from != null && to != null) {
            val quote = FareEngine.quote(fareConfig, to, regularCount, discountedCount)
            SectionCard {
                Column {
                    Text("Fare", style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold)
                    Text(
                        to.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (quote.regularCount > 0) {
                        FareLine(
                            "Regular × ${quote.regularCount}",
                            "₱%.2f".format(Locale.US, quote.regularRate * quote.regularCount)
                        )
                    }
                    if (quote.discountedCount > 0) {
                        FareLine(
                            "Senior / PWD / student × ${quote.discountedCount}",
                            "₱%.2f".format(Locale.US, quote.discountedRate * quote.discountedCount)
                        )
                    }
                    if (quote.minimumApplied) {
                        FareLine("Minimum fare applied", "yes")
                    }
                    if (!fareConfig.chargePerPassenger) {
                        FareLine("Charged per tricycle", "not per head")
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total", fontWeight = FontWeight.Bold)
                        Text("₱%.2f".format(Locale.US, quote.total), style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Cash on arrival. Rates are the ones posted by FeTODAT.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        var submitting by remember { mutableStateOf(false) }
        PrimaryButton(
            text = if (submitting) "Sending…" else "Find a Driver",
            onClick = {
                if (from != null && to != null && !submitting) {
                    submitting = true
                    val luggage = if (selectedLuggage.isEmpty()) "None" else selectedLuggage.joinToString(", ")
                    onConfirm(from, to, regularCount, discountedCount, luggage, notes)
                }
            },
            enabled = valid && !submitting
        )
        Spacer(modifier = Modifier.height(16.dp))
    }

    if (pinningPickup) {
        PickupPinner(
            initial = pickup ?: TALIBON_CENTRE,
            onDismiss = { pinningPickup = false },
            onPicked = { pickup = it; pinningPickup = false }
        )
    }

    if (pinningDestination) {
        DestinationFinder(
            stops = bookable,
            initial = pickup ?: TALIBON_CENTRE,
            onDismiss = { pinningDestination = false },
            onPicked = { destination = it; pinningDestination = false },
            onUseList = { pinningDestination = false; pickingDestination = true }
        )
    }

    if (pickingDestination) {
        StopPicker(
            title = "Where to?",
            stops = bookable,
            showFares = true,
            minimumFare = FareEngine.minimumFor(fareConfig, FareType.REGULAR),
            onDismiss = { pickingDestination = false },
            onPick = { destination = it; pickingDestination = false }
        )
    }

    if (pickingPickup) {
        StopPicker(
            title = "Where from?",
            stops = bookable,
            showFares = false,
            minimumFare = FareEngine.minimumFor(fareConfig, FareType.REGULAR),
            onDismiss = { pickingPickup = false },
            onPick = { pickup = it.location; pickingPickup = false }
        )
    }
}

/** One row of the two-column party counter: a label, a number, and two buttons. */
@Composable
private fun PassengerCounter(
    label: String,
    count: Int,
    canAdd: Boolean,
    canRemove: Boolean,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        FilledTonalIconButton(onClick = onRemove, enabled = canRemove) {
            Icon(Icons.Filled.Remove, contentDescription = "One fewer $label")
        }
        Text(
            "$count",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        FilledTonalIconButton(onClick = onAdd, enabled = canAdd) {
            Icon(Icons.Filled.Add, contentDescription = "One more $label")
        }
    }
}

/**
 * Full-screen map for pinning a pickup point that is not on the list.
 *
 * The map moves under a fixed centre pin rather than asking the user to drag a
 * marker, which is far easier one-handed. "Use my location" is offered but not
 * required: a passenger can pin the corner they will actually be standing on,
 * which is often not where they are standing now.
 */
@Composable
private fun PickupPinner(
    initial: Location,
    onDismiss: () -> Unit,
    onPicked: (Location) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var centre by remember { mutableStateOf(initial) }
    var label by remember { mutableStateOf("") }
    var locating by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            locating = true
            scope.launch {
                LocationProvider.current(context)?.let { centre = it }
                locating = false
            }
        }
    }

    // Describe wherever the pin has settled, a beat after it stops moving.
    LaunchedEffect(centre.latitude, centre.longitude) {
        label = ""
        kotlinx.coroutines.delay(400)
        label = ReverseGeocoder.describe(context, centre)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 16.dp, top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Close")
                    }
                    Text(
                        "Set your pickup point",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    "Move the map so the pin sits where you want to be picked up.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))

                PickerMap(
                    height = 380.dp,
                    centre = centre,
                    pinColor = EmeraldGreen,
                    onMoved = { centre = it },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    SecondaryButton(
                        text = if (locating) "Finding you..." else "Use my current location",
                        onClick = {
                            if (LocationProvider.hasPermission(context)) {
                                locating = true
                                scope.launch {
                                    LocationProvider.current(context)?.let { centre = it }
                                    locating = false
                                }
                            } else {
                                permissionLauncher.launch(
                                    android.Manifest.permission.ACCESS_FINE_LOCATION
                                )
                            }
                        },
                        enabled = !locating
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    SectionCard {
                        Column {
                            Text(
                                "Pickup point",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                label.ifBlank { "Locating..." },
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    PrimaryButton(
                        text = "Use this point",
                        onClick = { onPicked(centre.copy(address = label.ifBlank { "Pinned location" })) },
                        enabled = centre.hasCoordinates
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

/**
 * Full-screen map for choosing a destination by pointing at it.
 *
 * The ordinance prices a ride per named stop, not per kilometre, so this cannot
 * drop a pin anywhere and charge for it. What it does instead is let the
 * passenger move the map to roughly where they are going and name the posted
 * stop nearest to that point, with its fare, which is the part they could not
 * do before: you can find your destination without already knowing what the
 * fare sheet calls it.
 *
 * Only stops with coordinates can be found this way, and those are filled in by
 * the administrator over time. When none has been yet, the map is pointless and
 * the list is offered instead.
 */
@Composable
private fun DestinationFinder(
    stops: List<FareStop>,
    initial: Location,
    onDismiss: () -> Unit,
    onPicked: (FareStop) -> Unit,
    onUseList: () -> Unit
) {
    val mappable = remember(stops) { stops.filter { it.hasCoordinates } }
    var centre by remember { mutableStateOf(if (initial.hasCoordinates) initial else TALIBON_CENTRE) }

    val nearest = remember(mappable, centre.latitude, centre.longitude) {
        mappable.minByOrNull { LocationUtils.distanceKm(centre, it.location) }
    }
    val awayKm = nearest?.let { LocationUtils.distanceKm(centre, it.location) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 16.dp, top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Close")
                    }
                    Text(
                        "Find your destination",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (mappable.isEmpty()) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        SectionCard {
                            Column {
                                Text(
                                    "No stop has a map position yet",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    "The posted fare sheet gives names, not coordinates, so " +
                                        "the administrator adds positions to the stops over " +
                                        "time. Until then, choose your destination from the " +
                                        "list — the fare is the same either way.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        PrimaryButton(text = "Choose from the list", onClick = onUseList)
                    }
                } else {
                    Text(
                        "Move the map to where you are going. The nearest posted stop is " +
                            "named below, and that is the one you will be charged for.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    PickerMap(
                        height = 360.dp,
                        centre = centre,
                        pinColor = ForestGreen,
                        onMoved = { centre = it },
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        SectionCard {
                            Column {
                                Text(
                                    "Nearest posted stop",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    nearest?.label ?: "—",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                if (nearest != null && awayKm != null) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        "%s from the pin · ₱%.0f regular, ₱%.0f discounted".format(Locale.US, 
                                            if (awayKm < 1.0) "%.0f m".format(Locale.US, awayKm * 1000)
                                            else "%.1f km".format(Locale.US, awayKm),
                                            FareEngine.rateFor(nearest, FareType.REGULAR),
                                            FareEngine.rateFor(nearest, FareType.DISCOUNTED)
                                        ),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(onClick = onUseList) { Text("Choose from the list instead") }
                        Spacer(modifier = Modifier.height(4.dp))
                        PrimaryButton(
                            text = "Use this stop",
                            onClick = { nearest?.let(onPicked) },
                            enabled = nearest != null
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

/**
 * Full-screen searchable list of the posted stops.
 *
 * A dropdown does not work at this size, and neither does asking someone to
 * remember which zone their destination sits in, so the search matches both
 * the stop name and the zone, and the price for the selected rate column is
 * shown on every row.
 */
@Composable
private fun StopPicker(
    title: String,
    stops: List<FareStop>,
    /**
     * Fares belong to the destination. Showing one beside a pickup stop would
     * read as the price of being collected there, which is not a thing.
     */
    showFares: Boolean,
    minimumFare: Double,
    onDismiss: () -> Unit,
    onPick: (FareStop) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var zone by remember { mutableStateOf<String?>(null) }
    val zones = remember(stops) { stops.map { it.zone }.distinct().sorted() }

    val results = remember(stops, query, zone) {
        // Every word has to appear somewhere, so "poblacion talibon" and
        // "market balintawak" both find what the passenger meant. A single
        // substring match over the whole query found neither.
        val terms = query.trim().lowercase().split(" ").filter { it.isNotBlank() }
        stops.asSequence()
            .filter { zone == null || it.zone == zone }
            .filter { stop ->
                val haystack = "${stop.name} ${stop.zone}".lowercase()
                terms.all { haystack.contains(it) }
            }
            // Flat rates first: they are the two the table has no row for, so
            // they are the two nobody finds by scrolling.
            .sortedWith(
                compareBy<FareStop>(
                    { if (it.zone == FareEngine.FLAT_ZONE) 0 else 1 },
                    { it.zone },
                    { it.name }
                )
            )
            .toList()
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 16.dp, top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Close")
                    }
                    Text(
                        title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                // The minimum is the number people remember, and they remember
                // it as the price. Saying what the rest of the table looks like
                // is the difference between a fare that is higher than expected
                // and one that feels like being overcharged.
                val highest = remember(stops) {
                    stops.maxOfOrNull { FareEngine.rateFor(it, FareType.REGULAR) }
                }
                val note = if (showFares) {
                    val floor = "₱%.0f".format(Locale.US, minimumFare)
                    val ceiling = highest?.let { " and rise to ₱%.0f".format(Locale.US, it) }.orEmpty()
                    "Fares start at $floor$ceiling, depending on how far you are going. " +
                        "$floor is the least a ride can cost, not the usual price."
                } else {
                    "No prices here: the fare comes from where you are going, not " +
                        "from where you are collected."
                }
                Text(
                    note,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Search stop or zone") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = zone == null,
                        onClick = { zone = null },
                        label = { Text("All zones") }
                    )
                    zones.forEach { z ->
                        FilterChip(
                            selected = zone == z,
                            onClick = { zone = if (zone == z) null else z },
                            label = { Text(z) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                if (results.isEmpty()) {
                    Text(
                        "No stop matches that.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(20.dp)
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(
                            start = 20.dp, end = 20.dp, top = 4.dp, bottom = 24.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(results, key = { it.id }) { stop ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onPick(stop) }
                                    .padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(stop.name, style = MaterialTheme.typography.bodyLarge)
                                    Text(
                                        stop.zone,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (showFares) {
                                    Text(
                                        "₱%.0f / ₱%.0f".format(Locale.US, 
                                            FareEngine.rateFor(stop, FareType.REGULAR),
                                            FareEngine.rateFor(stop, FareType.DISCOUNTED)
                                        ),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FareLine(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}

/**
 * A read-only field that opens a full-screen search rather than a dropdown.
 *
 * Two hundred and forty stops will not fit in a dropdown, and the pickup and
 * destination now draw on the same list, so they use the same control.
 */
@Composable
private fun StopField(
    label: String,
    value: String,
    placeholder: String,
    icon: ImageVector,
    enabled: Boolean,
    onClick: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        readOnly = true,
        enabled = false,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        leadingIcon = { Icon(icon, contentDescription = null) },
        trailingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = onClick)
    )
}

@Composable
private fun SearchingContent(onCancel: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(56.dp))
        Spacer(modifier = Modifier.height(24.dp))
        Text("Searching for drivers...", style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Nearby verified tricycle drivers have been notified of your request.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        SecondaryButton(text = "Cancel Request", onClick = onCancel)
    }
}

@Composable
private fun RideTrackingContent(
    ride: Ride,
    driverLocation: Location?,
    driver: com.tpc.trikride.models.Driver?,
    canCancel: Boolean,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    var confirmingCancel by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            text = trackingHeadline(ride.status),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            "Please wait for your driver to arrive",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Driver card
        SectionCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Person, contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        ride.driverName.ifBlank { "Your Driver" },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Star, contentDescription = null,
                            tint = RatingColor, modifier = Modifier.size(16.dp))
                        // Real numbers or nothing. A driver nobody has rated
                        // yet says so, rather than borrowing someone's score.
                        val score = driver?.takeIf { it.ratingCount > 0 }
                            ?.let { " %.1f".format(Locale.US, it.rating) } ?: " Not yet rated"
                        val tricycle = driver?.tricycleNumber
                            ?.takeIf { it.isNotBlank() }
                            ?.let { "  •  Tricycle $it" }.orEmpty()
                        Text(
                            "$score$tricycle",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                if (ride.driverPhone.isNotBlank()) {
                    IconButton(onClick = { Navigation.dial(context, ride.driverPhone) }) {
                        Icon(Icons.Filled.Phone, contentDescription = "Call the driver",
                            tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        TrikMap(
            height = 200.dp,
            connectPins = true,
            pins = buildList {
                if (ride.pickupLocation.hasCoordinates) {
                    add(MapPin(ride.pickupLocation, "Pickup", EmeraldGreen))
                }
                driverLocation?.let { add(MapPin(it, "Your driver", RatingColor, emphasis = true)) }
            }
        )
        if (driverLocation == null) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "Waiting for your driver's location. It appears once they are moving " +
                    "with the app open.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Trip route
        SectionCard {
            Column {
                RouteRow(Icons.Filled.MyLocation, "Pickup", ride.pickupLocation.address)
                Spacer(modifier = Modifier.height(10.dp))
                RouteRow(Icons.Filled.LocationOn, "Destination", ride.dropoffLocation.address)
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${ride.passengerCount} passenger(s)", style = MaterialTheme.typography.bodyMedium)
                    Text(ride.luggage, style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Progress timeline
        Text("Ride Progress", style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        val step = statusStep(ride.status)
        TimelineRow("Ride Requested", step >= 0)
        TimelineRow("Driver Accepted", step >= 1)
        TimelineRow("Driver on the way", step >= 2)
        TimelineRow("Driver Arrived", step >= 3)
        TimelineRow("Ride Started", step >= 4)

        // A ride could not be called off at all, so a driver who accepted and
        // never arrived left this screen in front of the passenger for good,
        // with no way back to booking. Withdrawing stops at the point the ride
        // actually starts — after that they are in the tricycle.
        if (canCancel) {
            Spacer(modifier = Modifier.height(20.dp))
            SecondaryButton(
                text = "Cancel this ride",
                onClick = { confirmingCancel = true }
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "Only until the ride starts. Tell the driver if you can — they may " +
                    "already be on their way.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }

    if (confirmingCancel) {
        AlertDialog(
            onDismissRequest = { confirmingCancel = false },
            title = { Text("Cancel this ride?") },
            text = {
                Text(
                    "Your driver is told and the ride is closed. You can book again " +
                        "straight away. Nothing is charged — the fare is paid in cash at " +
                        "the end of a ride that happens."
                )
            },
            confirmButton = {
                TextButton(onClick = { confirmingCancel = false; onCancel() }) {
                    Text("Cancel the ride", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmingCancel = false }) { Text("Keep it") }
            }
        )
    }
}

@Composable
private fun RouteRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(label, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun TimelineRow(label: String, done: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 6.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = if (done) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (done) FontWeight.Bold else FontWeight.Normal,
            color = if (done) MaterialTheme.colorScheme.onSurface
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun RideCompleteContent(
    ride: Ride,
    alreadyRated: Boolean,
    onRate: (Int) -> Unit,
    onBackHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(56.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text("Ride Completed!", style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold)
        Text("Thank you for riding with us.", style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(24.dp))

        SectionCard {
            Column {
                SummaryRow("Fare", "₱%.2f".format(Locale.US, ride.estimatedFare))
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                SummaryRow("Passengers", "${ride.passengerCount}")
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                SummaryRow("Luggage", ride.luggage)
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                SummaryRow("Payment Method", "Cash")
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                SummaryRow("From", ride.pickupLocation.address)
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                SummaryRow("To", ride.dropoffLocation.address)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            if (alreadyRated) "Thanks for rating" else "Rate your driver",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        var rating by remember { mutableStateOf(0) }
        Row {
            (1..5).forEach { i ->
                IconButton(
                    onClick = { rating = i },
                    enabled = !alreadyRated
                ) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = "Star $i",
                        tint = if (i <= rating) RatingColor else MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
        // Sending is a separate, deliberate act. Rating on the first tap would
        // record a three when someone was on their way to five.
        if (!alreadyRated) {
            Spacer(modifier = Modifier.height(4.dp))
            TextButton(
                onClick = { if (rating > 0) onRate(rating) },
                enabled = rating > 0
            ) { Text("Send rating") }
        }
        Spacer(modifier = Modifier.height(20.dp))
        PrimaryButton(text = "Back to Home", onClick = onBackHome)
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.End)
    }
}

private fun trackingHeadline(status: RideStatus): String = when (status) {
    RideStatus.ACCEPTED -> "Driver accepted your ride!"
    RideStatus.DRIVER_ARRIVING -> "Driver is on the way"
    RideStatus.DRIVER_ARRIVED -> "Your driver has arrived"
    RideStatus.IN_PROGRESS -> "Ride in progress"
    else -> "Finding your driver..."
}

private fun statusStep(status: RideStatus): Int = when (status) {
    RideStatus.REQUESTED, RideStatus.SEARCHING -> 0
    RideStatus.ACCEPTED -> 1
    RideStatus.DRIVER_ARRIVING -> 2
    RideStatus.DRIVER_ARRIVED -> 3
    RideStatus.IN_PROGRESS -> 4
    RideStatus.COMPLETED -> 5
    else -> 0
}
```

#### `app/src/main/java/com/tpc/trikride/ui/screens/SettingsScreen.kt`

```kotlin
package com.tpc.trikride.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tpc.trikride.models.User
import com.tpc.trikride.models.UserType
import com.tpc.trikride.ui.components.AvatarPicker
import com.tpc.trikride.ui.components.PrimaryButton
import com.tpc.trikride.ui.components.SectionCard
import com.tpc.trikride.ui.components.SkeletonBox
import com.tpc.trikride.ui.components.TrikTextField
import com.tpc.trikride.ui.theme.ThemeState
import com.tpc.trikride.viewmodels.ProfileViewModel

/**
 * Shared Settings screen (profile lives inside settings, per the design).
 * [roleLabel] and [subtitle] let each account type show its own identity,
 * and [extraContent] lets a role add its own rows (e.g. driver vehicle info).
 */
@Composable
fun SettingsScreen(
    userId: String,
    userType: UserType,
    subtitle: String,
    onSignOut: () -> Unit,
    viewModel: ProfileViewModel = viewModel(),
    extraContent: @Composable () -> Unit = {}
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    LaunchedEffect(userId) { viewModel.bind(userId) }
    val state by viewModel.state.collectAsState()
    val pickPhoto: (android.graphics.Bitmap) -> Unit = { image -> viewModel.uploadPhoto(image) }

    var editing by remember { mutableStateOf(false) }
    var showDoc by remember { mutableStateOf<LegalDoc?>(null) }

    showDoc?.let { doc ->
        LegalScreen(doc = doc, onBack = { showDoc = null })
        return
    }

    if (editing) {
        EditProfileContent(
            user = state.user,
            isSaving = state.isSaving,
            isUploadingPhoto = state.isUploadingPhoto,
            photoData = state.photo,
            onPickPhoto = pickPhoto,
            onSave = { name, phone ->
                viewModel.saveProfile(name, phone)
                editing = false
            },
            onBack = { editing = false }
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text("Settings", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // Profile summary
        if (state.isLoading) {
            ProfileSkeleton()
        } else {
            SectionCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AvatarPicker(
                        photoData = state.photo,
                        initials = initials(state.user?.firstName),
                        isUploading = state.isUploadingPhoto,
                        onPhotoChosen = pickPhoto,
                        size = 56.dp
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            state.user?.firstName?.ifBlank { "TrikRide User" } ?: "TrikRide User",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            userType.name.lowercase().replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            state.user?.email.orEmpty().ifBlank { subtitle },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = { editing = true }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit profile")
                    }
                }
            }
        }

        state.message?.let {
            Spacer(modifier = Modifier.height(10.dp))
            Text(it, color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall)
        }
        state.error?.let {
            Spacer(modifier = Modifier.height(10.dp))
            Text(it, color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(8.dp))
        extraContent()

        GroupTitle("Account")
        SettingsRow(Icons.Filled.Person, "Edit Profile") { editing = true }
        SettingsRow(Icons.Filled.Lock, "Change Password", subtitle = "Sends a reset link to your email") {
            viewModel.sendPasswordReset()
        }

        GroupTitle("Preferences")
        val isDark = ThemeState.darkModeOverride ?: isSystemInDarkTheme()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                if (isDark) Icons.Filled.DarkMode else Icons.Filled.LightMode,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(14.dp))
            Text("Dark Mode", style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f))
            Switch(checked = isDark, onCheckedChange = { ThemeState.set(context, it) })
        }

        GroupTitle("Legal & About")
        SettingsRow(Icons.Filled.Description, LegalDoc.TERMS.title) { showDoc = LegalDoc.TERMS }
        SettingsRow(Icons.Filled.PrivacyTip, LegalDoc.PRIVACY.title) { showDoc = LegalDoc.PRIVACY }
        SettingsRow(Icons.Filled.Shield, LegalDoc.COMMUNITY.title) { showDoc = LegalDoc.COMMUNITY }
        if (userType == UserType.DRIVER) {
            SettingsRow(Icons.Filled.Gavel, LegalDoc.DRIVER_AGREEMENT.title) {
                showDoc = LegalDoc.DRIVER_AGREEMENT
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Info, contentDescription = null,
                tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(14.dp))
            Text("App Version", style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f))
            Text("1.0.0", style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = onSignOut,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Log Out")
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun EditProfileContent(
    user: User?,
    isSaving: Boolean,
    isUploadingPhoto: Boolean,
    photoData: String,
    onPickPhoto: (android.graphics.Bitmap) -> Unit,
    onSave: (String, String) -> Unit,
    onBack: () -> Unit
) {
    var name by remember(user) { mutableStateOf(user?.firstName.orEmpty()) }
    var phone by remember(user) { mutableStateOf(user?.phoneNumber.orEmpty()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text("Edit Profile", style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(20.dp))

        AvatarPicker(
            photoData = photoData,
            initials = initials(name),
            isUploading = isUploadingPhoto,
            onPhotoChosen = onPickPhoto,
            size = 96.dp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Tap the photo to change it",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(20.dp))

        TrikTextField(name, { name = it }, "Full Name", Icons.Filled.Person)
        Spacer(modifier = Modifier.height(14.dp))
        TrikTextField(phone, { phone = it }, "Phone Number", Icons.Filled.Phone)
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            "Email and birthdate can't be changed here.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = if (isSaving) "Saving..." else "Save Changes",
            onClick = { onSave(name, phone) },
            enabled = !isSaving && name.isNotBlank()
        )
    }
}

@Composable
private fun GroupTitle(text: String) {
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    label: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.bodyLarge)
            if (subtitle != null) {
                Text(subtitle, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Icon(Icons.Filled.ChevronRight, contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun ProfileSkeleton() {
    SectionCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            SkeletonBox(modifier = Modifier.size(56.dp), circle = true)
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                SkeletonBox(modifier = Modifier.fillMaxWidth(0.6f).height(16.dp))
                Spacer(modifier = Modifier.height(8.dp))
                SkeletonBox(modifier = Modifier.fillMaxWidth(0.4f).height(12.dp))
                Spacer(modifier = Modifier.height(6.dp))
                SkeletonBox(modifier = Modifier.fillMaxWidth(0.8f).height(12.dp))
            }
        }
    }
}

private fun initials(name: String?): String {
    val parts = name?.trim()?.split(" ")?.filter { it.isNotBlank() }.orEmpty()
    return when {
        parts.isEmpty() -> "TR"
        parts.size == 1 -> parts[0].take(2).uppercase()
        else -> "${parts[0].first()}${parts[1].first()}".uppercase()
    }
}
```

[[PB]]

### I.10 Unit tests

#### `app/src/test/java/com/tpc/trikride/BirthDateTest.kt`

```kotlin
package com.tpc.trikride

import com.tpc.trikride.utils.BirthDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class BirthDateTest {

    private fun utcMidnight(y: Int, m: Int, d: Int): Long =
        Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            clear(); set(y, m - 1, d, 0, 0, 0)
        }.timeInMillis

    private fun localNoon(y: Int, m: Int, d: Int): Long =
        Calendar.getInstance().apply { clear(); set(y, m - 1, d, 12, 0, 0) }.timeInMillis

    /**
     * The picker hands back midnight UTC. Read in a zone behind UTC that instant
     * is the previous evening, which is how a birthday moves a day.
     */
    @Test
    fun `the picker's instant keeps its calendar date in any time zone`() {
        val picked = utcMidnight(2004, 8, 16)
        val zones = listOf("UTC", "Asia/Manila", "America/New_York", "Pacific/Honolulu", "Pacific/Kiritimati")
        val previous = TimeZone.getDefault()
        try {
            for (z in zones) {
                TimeZone.setDefault(TimeZone.getTimeZone(z))
                assertEquals("wrong date in $z", "2004-08-16", BirthDate.fromPickerUtc(picked))
            }
        } finally {
            TimeZone.setDefault(previous)
        }
    }

    @Test
    fun `the stored form is the same digits in any locale`() {
        val previous = Locale.getDefault()
        try {
            Locale.setDefault(Locale.forLanguageTag("ar-EG-u-nu-arab"))
            assertEquals("2004-08-16", BirthDate.fromPickerUtc(utcMidnight(2004, 8, 16)))
            Locale.setDefault(Locale.GERMANY)
            assertEquals("2004-08-16", BirthDate.fromPickerUtc(utcMidnight(2004, 8, 16)))
        } finally {
            Locale.setDefault(previous)
        }
    }

    @Test
    fun `age counts completed years`() {
        // Born 2004-08-16, so 22 on and after the 2026 birthday and 21 the day before.
        assertEquals(22, BirthDate.ageOn("2004-08-16", localNoon(2026, 8, 16)))
        assertEquals(21, BirthDate.ageOn("2004-08-16", localNoon(2026, 8, 15)))
        assertEquals(22, BirthDate.ageOn("2004-08-16", localNoon(2026, 9, 1)))
    }

    @Test
    fun `a date in the future is refused`() {
        assertEquals("That date is in the future.", BirthDate.reject("2090-01-01", localNoon(2026, 9, 8)))
        assertFalse(BirthDate.isPlausible("2090-01-01", localNoon(2026, 9, 8)))
    }

    @Test
    fun `too young is refused`() {
        assertTrue(BirthDate.reject("2020-01-01", localNoon(2026, 9, 8))!!.contains("at least 13"))
    }

    @Test
    fun `an implausible year is refused`() {
        assertTrue(BirthDate.reject("1850-01-01", localNoon(2026, 9, 8))!!.contains("over 120"))
    }

    @Test
    fun `a plausible date is accepted`() {
        assertNull(BirthDate.reject("2004-08-16", localNoon(2026, 9, 8)))
        assertTrue(BirthDate.isPlausible("2004-08-16", localNoon(2026, 9, 8)))
    }

    @Test
    fun `exactly the minimum age is accepted`() {
        assertNull(BirthDate.reject("2013-09-08", localNoon(2026, 9, 8)))
        assertTrue(BirthDate.reject("2013-09-09", localNoon(2026, 9, 8))!!.contains("at least 13"))
    }

    @Test
    fun `an unset or unreadable value is refused rather than accepted`() {
        assertEquals("Choose your date of birth.", BirthDate.reject("", localNoon(2026, 9, 8)))
        assertEquals("Choose your date of birth.", BirthDate.reject("Aug 16, 2004", localNoon(2026, 9, 8)))
        assertFalse(BirthDate.isPlausible("not-a-date"))
    }

    @Test
    fun `display renders a stored date and passes anything else through`() {
        assertEquals("16 August 2004", BirthDate.display("2004-08-16"))
        // Records written before the format changed are shown as they are
        // rather than as an error.
        assertEquals("Aug 16, 2004", BirthDate.display("Aug 16, 2004"))
    }
}
```

#### `app/src/test/java/com/tpc/trikride/CacheCleanupTest.kt`

```kotlin
package com.tpc.trikride

import com.tpc.trikride.utils.CacheCleanup
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class CacheCleanupTest {

    @get:Rule
    val temp = TemporaryFolder()

    private fun file(dir: String, name: String, ageMs: Long): File {
        val d = File(temp.root, dir).apply { mkdirs() }
        return File(d, name).apply {
            writeText("x")
            setLastModified(System.currentTimeMillis() - ageMs)
        }
    }

    @Test
    fun `removes a licence capture left behind by the camera`() {
        val old = file("images", "licence.jpg", 2 * CacheCleanup.DEFAULT_MAX_AGE_MS)
        assertEquals(1, CacheCleanup.sweep(temp.root))
        assertFalse(old.exists())
    }

    @Test
    fun `removes an exported report carrying everyone's details`() {
        val old = file("reports", "trikride-rides-2026-03.csv", 2 * CacheCleanup.DEFAULT_MAX_AGE_MS)
        assertEquals(1, CacheCleanup.sweep(temp.root))
        assertFalse(old.exists())
    }

    @Test
    fun `leaves a capture that may still be on its way to the encoder`() {
        val fresh = file("images", "fresh.jpg", 0)
        assertEquals(0, CacheCleanup.sweep(temp.root))
        assertTrue(fresh.exists())
    }

    @Test
    fun `leaves directories it does not own`() {
        val tiles = file("osmdroid", "tile.png", 10 * CacheCleanup.DEFAULT_MAX_AGE_MS)
        CacheCleanup.sweep(temp.root)
        assertTrue("the map tile cache is not ours to clear", tiles.exists())
    }

    @Test
    fun `an absent cache directory is not an error`() {
        assertEquals(0, CacheCleanup.sweep(File(temp.root, "nothing-here")))
    }

    @Test
    fun `counts every file it removes`() {
        file("images", "a.jpg", 2 * CacheCleanup.DEFAULT_MAX_AGE_MS)
        file("images", "b.jpg", 2 * CacheCleanup.DEFAULT_MAX_AGE_MS)
        file("reports", "c.pdf", 2 * CacheCleanup.DEFAULT_MAX_AGE_MS)
        assertEquals(3, CacheCleanup.sweep(temp.root))
    }

    @Test
    fun `a subdirectory is left alone`() {
        val d = File(temp.root, "images/nested").apply { mkdirs() }
        d.setLastModified(System.currentTimeMillis() - 10 * CacheCleanup.DEFAULT_MAX_AGE_MS)
        CacheCleanup.sweep(temp.root)
        assertTrue(d.exists())
    }
}
```

#### `app/src/test/java/com/tpc/trikride/FareEngineTest.kt`

```kotlin
package com.tpc.trikride

import com.tpc.trikride.models.FareConfig
import com.tpc.trikride.models.FareStop
import com.tpc.trikride.models.FareType
import com.tpc.trikride.utils.FareEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pricing is the part of this app that takes money off people, so it is the
 * part that gets tested first.
 */
class FareEngineTest {

    private val config = FareConfig()
    private fun stop(regular: Double, discounted: Double) =
        FareStop(id = "t", zone = "Z", name = "N", regularFare = regular, discountedFare = discounted)

    @Test
    fun `charges the posted rate per head`() {
        val quote = FareEngine.quote(config, stop(40.0, 32.0), regularCount = 2, discountedCount = 1)
        assertEquals(40.0, quote.regularRate, 0.001)
        assertEquals(32.0, quote.discountedRate, 0.001)
        assertEquals(112.0, quote.total, 0.001)
        assertEquals(3, quote.passengers)
    }

    @Test
    fun `raises a rate below the ordinance minimum to the minimum`() {
        val quote = FareEngine.quote(config, stop(20.0, 16.0), regularCount = 1, discountedCount = 1)
        assertEquals(25.0, quote.regularRate, 0.001)
        assertEquals(20.0, quote.discountedRate, 0.001)
        assertEquals(45.0, quote.total, 0.001)
        assertTrue(quote.minimumApplied)
    }

    @Test
    fun `does not flag the minimum when the posted rate is above it`() {
        assertFalse(FareEngine.quote(config, stop(40.0, 32.0), 1, 0).minimumApplied)
    }

    /**
     * Three rows of the transcribed table carry a discounted rate above the
     * regular one. Charging a senior, a person with a disability or a student
     * more than the passenger beside them is what RA 9994 and RA 10754 forbid,
     * so the engine clamps rather than trusting the table.
     */
    @Test
    fun `never charges a discounted passenger more than a regular one`() {
        // san_roque__arlen_to_centro_special_trip, as transcribed.
        val quote = FareEngine.quote(config, stop(60.0, 80.0), regularCount = 1, discountedCount = 1)
        assertEquals(60.0, quote.regularRate, 0.001)
        assertEquals(60.0, quote.discountedRate, 0.001)
        assertEquals(120.0, quote.total, 0.001)
    }

    @Test
    fun `clamps an inverted rate that an administrator types in`() {
        val quote = FareEngine.quote(config, stop(30.0, 999.0), regularCount = 0, discountedCount = 2)
        assertEquals(60.0, quote.total, 0.001)
    }

    @Test
    fun `prices one tricycle when the sheet is not charged per head`() {
        val flat = config.copy(chargePerPassenger = false)
        assertEquals(40.0, FareEngine.quote(flat, stop(40.0, 32.0), 2, 1).total, 0.001)
        // Everybody aboard entitled to the discount pays the discounted rate.
        assertEquals(32.0, FareEngine.quote(flat, stop(40.0, 32.0), 0, 3).total, 0.001)
    }

    @Test
    fun `treats a negative count as none`() {
        val quote = FareEngine.quote(config, stop(40.0, 32.0), regularCount = -5, discountedCount = 1)
        assertEquals(0, quote.regularCount)
        assertEquals(32.0, quote.total, 0.001)
    }

    @Test
    fun `an empty party costs nothing`() {
        assertEquals(0.0, FareEngine.quote(config, stop(40.0, 32.0), 0, 0).total, 0.001)
    }

    @Test
    fun `a stop with no rate at all still respects the minimum`() {
        val quote = FareEngine.quote(config, stop(0.0, 0.0), 1, 0)
        assertEquals(25.0, quote.total, 0.001)
    }

    @Test
    fun `flat stops carry the configured flat rates`() {
        val flats = FareEngine.flatStops(config)
        assertEquals(2, flats.size)
        assertEquals(config.poblacionFlat, flats[0].regularFare, 0.001)
        assertEquals(config.terminalRoundTrip, flats[1].regularFare, 0.001)
    }

    @Test
    fun `party label describes a mixed booking`() {
        assertEquals(
            "2 regular, 1 discounted",
            FareEngine.quote(config, stop(40.0, 32.0), 2, 1).partyLabel
        )
        assertEquals("no passengers", FareEngine.quote(config, stop(40.0, 32.0), 0, 0).partyLabel)
    }

    @Test
    fun `minimum lookup follows the rate column`() {
        assertEquals(25.0, FareEngine.minimumFor(config, FareType.REGULAR), 0.001)
        assertEquals(20.0, FareEngine.minimumFor(config, FareType.DISCOUNTED), 0.001)
    }
}
```

#### `app/src/test/java/com/tpc/trikride/FareSeedTest.kt`

```kotlin
package com.tpc.trikride

import com.tpc.trikride.models.FareConfig
import com.tpc.trikride.utils.FareEngine
import com.tpc.trikride.utils.FareSeed
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The seeded fare table is transcribed from a photograph of a laminated sheet,
 * so it is data that can be wrong in ways only a check will catch. These guard
 * the properties that must hold whatever the sheet says.
 */
class FareSeedTest {

    private val config = FareConfig()

    @Test
    fun `every stop has a unique id`() {
        val ids = FareSeed.STOPS.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun `no stop is nameless or zoneless`() {
        FareSeed.STOPS.forEach {
            assertTrue("blank name on ${it.id}", it.name.isNotBlank())
            assertTrue("blank zone on ${it.id}", it.zone.isNotBlank())
        }
    }

    @Test
    fun `no stop carries a negative rate`() {
        FareSeed.STOPS.forEach {
            assertTrue("negative regular on ${it.id}", it.regularFare >= 0.0)
            assertTrue("negative discounted on ${it.id}", it.discountedFare >= 0.0)
        }
    }

    /**
     * The one that matters. Whatever the transcription says, no priced booking
     * may charge the discounted column more than the regular one.
     */
    @Test
    fun `no bookable stop ever prices a discounted passenger above a regular one`() {
        FareSeed.STOPS.filter { it.active }.forEach { stop ->
            val quote = FareEngine.quote(config, stop, regularCount = 1, discountedCount = 1)
            assertTrue(
                "${stop.id} prices discounted at ${quote.discountedRate} " +
                    "against regular ${quote.regularRate}",
                quote.discountedRate <= quote.regularRate
            )
        }
    }

    /**
     * Documents the rows the transcription got wrong, so that correcting the
     * table in the admin screen — or re-transcribing the sheet — shows up here
     * rather than passing silently.
     */
    @Test
    fun `the known inverted rows are the only inverted rows`() {
        val inverted = FareSeed.STOPS
            .filter { it.active && it.discountedFare > it.regularFare }
            .map { it.id }
            .toSet()
        assertEquals(
            setOf(
                "balintawak__ka_ano_garcia",
                "san_isidro__mar_auguis",
                "san_roque__dancy",
                "san_roque__arlen_to_centro_special_trip"
            ),
            inverted
        )
    }

    @Test
    fun `a stop with no usable rate is not bookable`() {
        FareSeed.STOPS.filter { it.regularFare == 0.0 && it.discountedFare == 0.0 }
            .forEach { assertTrue("${it.id} is active with no rate", !it.active) }
    }

    @Test
    fun `every zone named on the sheet has at least one stop`() {
        val zones = FareSeed.STOPS.map { it.zone }.toSet()
        FareSeed.ZONES.forEach { assertTrue("no stops in $it", it in zones) }
    }

    @Test
    fun `no stop uses a zone the sheet does not list`() {
        FareSeed.STOPS.forEach {
            assertTrue("${it.id} is in unlisted zone ${it.zone}", it.zone in FareSeed.ZONES)
        }
    }

    @Test
    fun `a stop without coordinates reports that it has none`() {
        FareSeed.STOPS.forEach {
            assertEquals(
                it.latitude != 0.0 || it.longitude != 0.0,
                it.hasCoordinates
            )
        }
    }

    @Test
    fun `the table is the two hundred and forty rows the sheet holds`() {
        assertEquals(240, FareSeed.STOPS.size)
    }
}
```

#### `app/src/test/java/com/tpc/trikride/LocationUtilsTest.kt`

```kotlin
package com.tpc.trikride

import com.tpc.trikride.models.Location
import com.tpc.trikride.utils.LocationUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LocationUtilsTest {

    private val talibon = Location(10.1531, 124.3251)

    @Test
    fun `the distance from a point to itself is zero`() {
        assertEquals(0.0, LocationUtils.distanceKm(talibon, talibon), 0.0001)
    }

    @Test
    fun `a degree of latitude is about a hundred and eleven kilometres`() {
        val north = talibon.copy(latitude = talibon.latitude + 1.0)
        assertEquals(111.2, LocationUtils.distanceKm(talibon, north), 1.0)
    }

    @Test
    fun `distance is symmetric`() {
        val other = Location(9.8, 124.1)
        assertEquals(
            LocationUtils.distanceKm(talibon, other),
            LocationUtils.distanceKm(other, talibon),
            0.0001
        )
    }

    @Test
    fun `nearest picks the closest candidate`() {
        val near = Location(10.1540, 124.3260)
        val far = Location(11.0, 125.0)
        assertEquals(near, LocationUtils.nearest(talibon, listOf(far, near)))
    }

    @Test
    fun `nearest returns null for an empty list`() {
        assertNull(LocationUtils.nearest(talibon, emptyList()))
    }

    /**
     * A default Location is 0,0 — a point in the Atlantic — so anything that
     * plots one has to be able to tell it apart from a real position.
     */
    @Test
    fun `a default location reports no coordinates`() {
        assertTrue(!Location().hasCoordinates)
        assertTrue(talibon.hasCoordinates)
        assertTrue(Location(latitude = 0.0, longitude = 124.0).hasCoordinates)
    }
}
```

#### `app/src/test/java/com/tpc/trikride/PasswordRulesTest.kt`

```kotlin
package com.tpc.trikride

import com.tpc.trikride.utils.PasswordRules
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PasswordRulesTest {

    @Test
    fun `accepts a password meeting the four required rules`() {
        assertTrue(PasswordRules.isStrong("Password1"))
    }

    @Test
    fun `a symbol is recommended but not required`() {
        assertTrue(PasswordRules.isStrong("Password1"))
        assertTrue(PasswordRules.isStrong("Password1!"))
        assertFalse(PasswordRules.evaluate("Password1")[4].passed)
        assertTrue(PasswordRules.evaluate("Password1!")[4].passed)
    }

    @Test
    fun `rejects a password missing any required rule`() {
        assertFalse("too short", PasswordRules.isStrong("Pass1"))
        assertFalse("no uppercase", PasswordRules.isStrong("password1"))
        assertFalse("no lowercase", PasswordRules.isStrong("PASSWORD1"))
        assertFalse("no digit", PasswordRules.isStrong("Passwords"))
        assertFalse("empty", PasswordRules.isStrong(""))
    }

    @Test
    fun `exactly eight characters passes the length rule`() {
        assertTrue(PasswordRules.evaluate("Passwo1d")[0].passed)
        assertFalse(PasswordRules.evaluate("Passw1d")[0].passed)
    }

    @Test
    fun `reports five checks in a stable order`() {
        val checks = PasswordRules.evaluate("x")
        assertEquals(5, checks.size)
        assertTrue(checks[0].label.contains("8 characters"))
    }

    /**
     * Firebase enforces six characters of its own, so a password this app
     * accepts is always one Firebase accepts too.
     */
    @Test
    fun `anything strong enough here clears Firebase's own six-character floor`() {
        listOf("Password1", "Abcdefg1", "Zz9aaaaa").forEach {
            assertTrue(PasswordRules.isStrong(it))
            assertTrue(it.length >= 6)
        }
    }
}
```

#### `app/src/test/java/com/tpc/trikride/ReportBuilderTest.kt`

```kotlin
package com.tpc.trikride

import com.tpc.trikride.models.Complaint
import com.tpc.trikride.models.ComplaintStatus
import com.tpc.trikride.models.Location
import com.tpc.trikride.models.Ride
import com.tpc.trikride.models.RideStatus
import com.tpc.trikride.models.User
import com.tpc.trikride.utils.ReportBuilder
import com.tpc.trikride.utils.ReportPeriod
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class ReportBuilderTest {

    private fun at(year: Int, month: Int, day: Int): Long =
        Calendar.getInstance().apply {
            clear(); set(year, month, day, 12, 0, 0)
        }.timeInMillis

    private fun ride(
        id: String = "r1",
        status: RideStatus = RideStatus.COMPLETED,
        fare: Double = 40.0,
        requestedAt: Long = at(2026, Calendar.MARCH, 10),
        passengerId: String = "p1",
        driverId: String = "d1",
        notes: String = ""
    ) = Ride(
        id = id,
        passengerId = passengerId,
        driverId = driverId,
        pickupLocation = Location(address = "Pickup"),
        dropoffLocation = Location(address = "Market"),
        status = status,
        requestedAt = requestedAt.toString(),
        estimatedFare = fare,
        notes = notes
    )

    private val users = mapOf(
        "p1" to User(id = "p1", firstName = "Ana", email = "ana@example.com"),
        "d1" to User(id = "d1", firstName = "Ben", email = "ben@example.com")
    )

    // --- summary arithmetic -------------------------------------------------

    @Test
    fun `summarises completed rides and gross fares`() {
        val rides = listOf(
            ride("a", fare = 40.0),
            ride("b", fare = 60.0),
            ride("c", status = RideStatus.IN_PROGRESS, fare = 25.0)
        )
        val s = ReportBuilder.summarise(rides, emptyList(), ReportPeriod.AllTime)
        assertEquals(3, s.totalRides)
        assertEquals(2, s.completed)
        assertEquals(1, s.inProgress)
        assertEquals(100.0, s.grossFares, 0.001)
        assertEquals(50.0, s.averageFare, 0.001)
    }

    @Test
    fun `an empty period averages to zero rather than dividing by it`() {
        val s = ReportBuilder.summarise(emptyList(), emptyList(), ReportPeriod.AllTime)
        assertEquals(0.0, s.averageFare, 0.001)
        assertEquals(0, s.totalRides)
    }

    @Test
    fun `a ride with an unparseable timestamp falls outside every dated period`() {
        val rides = listOf(ride("a").copy(requestedAt = "not-a-number"))
        assertEquals(0, ReportBuilder.ridesIn(rides, ReportPeriod.Year(2026)).size)
        // AllTime still filters on a parseable stamp, so it is excluded there too.
        assertEquals(0, ReportBuilder.ridesIn(rides, ReportPeriod.AllTime).size)
    }

    @Test
    fun `a month period keeps only that month`() {
        val rides = listOf(
            ride("a", requestedAt = at(2026, Calendar.MARCH, 1)),
            ride("b", requestedAt = at(2026, Calendar.APRIL, 1))
        )
        val march = ReportBuilder.ridesIn(rides, ReportPeriod.Month(2026, Calendar.MARCH))
        assertEquals(listOf("a"), march.map { it.id })
    }

    @Test
    fun `a custom range includes both end days in full`() {
        val start = at(2026, Calendar.MARCH, 10)
        val end = at(2026, Calendar.MARCH, 12)
        val period = ReportPeriod.customRange(start, end)
        assertTrue(period.contains(start))
        assertTrue(period.contains(end))
    }

    @Test
    fun `a custom range picked back to front still means the days between`() {
        val a = at(2026, Calendar.MARCH, 12)
        val b = at(2026, Calendar.MARCH, 10)
        val period = ReportPeriod.customRange(a, b) as ReportPeriod.Custom
        assertTrue(period.startMillis < period.endMillis)
    }

    @Test
    fun `unique passengers and drivers ignore blanks`() {
        val rides = listOf(
            ride("a", passengerId = "p1", driverId = "d1"),
            ride("b", passengerId = "p1", driverId = ""),
            ride("c", passengerId = "p2", driverId = "d2")
        )
        val s = ReportBuilder.summarise(rides, emptyList(), ReportPeriod.AllTime)
        assertEquals(2, s.uniquePassengers)
        assertEquals(2, s.activeDrivers)
    }

    // --- CSV safety ---------------------------------------------------------

    /**
     * A ride note is typed by a passenger and read by an administrator in
     * Excel. A cell that begins `=`, `+`, `-` or `@` is evaluated as a formula
     * on the administrator's computer, so it has to be neutralised on the way
     * out. Quoting alone does not do it.
     */
    @Test
    fun `a note that looks like a formula is not written as one`() {
        val hostile = "=HYPERLINK(\"http://example.invalid\",\"click\")"
        val csv = ReportBuilder.ridesCsv(
            listOf(ride(notes = hostile)), emptyList(), users, ReportPeriod.AllTime
        )
        // Quotes inside the value are doubled by the CSV escaping, so the cell
        // as written is the prefixed text with its own quotes doubled.
        val written = "\"'" + hostile.replace("\"", "\"\"") + "\""
        assertFalse("formula reached the file unescaped", csv.contains("\"$hostile\""))
        assertTrue("expected the text-marker prefix", csv.contains(written))
    }

    @Test
    fun `every formula lead character is neutralised, including behind whitespace`() {
        listOf("=1+1", "+1", "-1", "@SUM(A1)", "\t=1+1", " =1+1").forEach { hostile ->
            val csv = ReportBuilder.ridesCsv(
                listOf(ride(notes = hostile)), emptyList(), users, ReportPeriod.AllTime
            )
            assertTrue("not neutralised: $hostile", csv.contains("\"'$hostile\""))
        }
    }

    @Test
    fun `ordinary text is not prefixed`() {
        val csv = ReportBuilder.ridesCsv(
            listOf(ride(notes = "Meet me by the gate")), emptyList(), users, ReportPeriod.AllTime
        )
        assertTrue(csv.contains("\"Meet me by the gate\""))
        assertFalse(csv.contains("\"'Meet me by the gate\""))
    }

    @Test
    fun `quotes and commas inside a value survive the round trip`() {
        val csv = ReportBuilder.ridesCsv(
            listOf(ride(notes = "He said \"hello\", then left")),
            emptyList(), users, ReportPeriod.AllTime
        )
        assertTrue(csv.contains("\"He said \"\"hello\"\", then left\""))
    }

    @Test
    fun `a hostile display name in a complaint is neutralised too`() {
        val complaint = Complaint(
            id = "c1",
            reporterId = "unknown",
            reporterName = "=cmd|'/c calc'!A0",
            category = "Other",
            description = "-1+1",
            status = ComplaintStatus.OPEN,
            createdAt = at(2026, Calendar.MARCH, 10).toString()
        )
        val csv = ReportBuilder.complaintsCsv(listOf(complaint), emptyList<User>().associateBy { it.id }, ReportPeriod.AllTime)
        assertTrue(csv.contains("\"'=cmd|'/c calc'!A0\""))
        assertTrue(csv.contains("\"'-1+1\""))
    }

    /** Money in a data file must parse as a number wherever it is opened. */
    @Test
    fun `fares are written with a dot decimal separator whatever the device locale`() {
        val previous = java.util.Locale.getDefault()
        try {
            java.util.Locale.setDefault(java.util.Locale.GERMANY)
            val csv = ReportBuilder.ridesCsv(
                listOf(ride(fare = 1234.5)), emptyList(), users, ReportPeriod.AllTime
            )
            assertTrue("expected 1234.50, got a locale-formatted number", csv.contains("1234.50"))
            assertFalse(csv.contains("1234,50"))
        } finally {
            java.util.Locale.setDefault(previous)
        }
    }

    @Test
    fun `a report filename is safe whatever the device locale`() {
        val previous = java.util.Locale.getDefault()
        try {
            java.util.Locale.setDefault(java.util.Locale.forLanguageTag("ar-EG-u-nu-arab"))
            val name = ReportBuilder.fileName("rides", ReportPeriod.Month(2026, Calendar.MARCH), "csv")
            assertEquals("trikride-rides-2026-03.csv", name)
        } finally {
            java.util.Locale.setDefault(previous)
        }
    }

    // --- aggregations -------------------------------------------------------

    @Test
    fun `rides by weekday always reports all seven days`() {
        val byDay = ReportBuilder.ridesByWeekday(listOf(ride()), ReportPeriod.AllTime)
        assertEquals(7, byDay.size)
        assertEquals(listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"), byDay.map { it.first })
    }

    @Test
    fun `rides by hour always reports twenty four buckets`() {
        assertEquals(24, ReportBuilder.ridesByHour(listOf(ride()), ReportPeriod.AllTime).size)
    }

    @Test
    fun `completion rate ignores drivers with too few rides to mean anything`() {
        val rides = listOf(
            ride("a", driverId = "d1"), ride("b", driverId = "d1"),
            ride("c", driverId = "d2"), ride("d", driverId = "d2"), ride("e", driverId = "d2")
        )
        val rates = ReportBuilder.completionRatePerDriver(rides, users, ReportPeriod.AllTime)
        assertEquals(1, rates.size)
    }

    @Test
    fun `available periods always offer all time even with no rides`() {
        assertEquals(listOf(ReportPeriod.AllTime), ReportBuilder.availablePeriods(emptyList()))
    }
}
```

[[PB]]

### I.11 Build configuration and security rules

#### `app/build.gradle.kts`

```kotlin
import java.io.File
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

// Apply the Google Services plugin only when the Firebase config file is present,
// so the project still builds before Firebase is configured.
if (file("google-services.json").exists()) {
    apply(plugin = "com.google.gms.google-services")
}

// Secrets come from `.env` in the project root (gitignored). See .env.example.
// local.properties is still read as a fallback so existing setups keep working.
val envProperties = Properties().apply {
    val envFile = rootProject.file(".env")
    if (envFile.exists()) {
        envFile.inputStream().use { load(it) }
    }
}
val localProperties = Properties().apply {
    val propsFile = rootProject.file("local.properties")
    if (propsFile.exists()) {
        propsFile.inputStream().use { load(it) }
    }
}

/** Looks a key up in .env first, then local.properties, then the default. */
fun secret(key: String, default: String = ""): String =
    envProperties.getProperty(key)?.takeIf { it.isNotBlank() }
        ?: localProperties.getProperty(key)?.takeIf { it.isNotBlank() }
        ?: default

// Blank rather than a sentinel: the app tests this to pick a map renderer, and
// a placeholder string would read as a real key.
val mapsApiKey: String = secret("MAPS_API_KEY", "")
val supportHotline: String = secret("SUPPORT_HOTLINE", "0966-749-7561")
val supportEmail: String = secret("SUPPORT_EMAIL", "trikride@tpc.edu.ph")

// Release signing. The keystore itself is never committed; its path and
// passwords come from .env. When they are absent — a fresh clone, or anyone
// building only the debug variant — the release build falls back to the debug
// key so the project still configures and assembles.
val keystorePath: String = secret("RELEASE_STORE_FILE")
val keystoreFile: File? = keystorePath
    .takeIf { it.isNotBlank() }
    ?.let { path -> File(path).let { if (it.isAbsolute) it else rootProject.file(path) } }
    ?.takeIf { it.exists() }
val hasReleaseKeystore = keystoreFile != null &&
    secret("RELEASE_STORE_PASSWORD").isNotBlank() &&
    secret("RELEASE_KEY_ALIAS").isNotBlank() &&
    secret("RELEASE_KEY_PASSWORD").isNotBlank()

android {
    namespace = "com.tpc.trikride"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.tpc.trikride"
        minSdk = 24
        // Matches compileSdk. Raising it further means Android 16, which needs a
        // newer Android Gradle Plugin than 8.7.3 — worth doing only if the app is
        // ever published to Play, which enforces a recent target every August.
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey
        // The app reads this to decide which renderer to use, so a blank key
        // has to be distinguishable from a real one at runtime.
        buildConfigField("String", "MAPS_API_KEY", "\"$mapsApiKey\"")
        buildConfigField("String", "SUPPORT_HOTLINE", "\"$supportHotline\"")
        buildConfigField("String", "SUPPORT_EMAIL", "\"$supportEmail\"")
    }

    signingConfigs {
        if (hasReleaseKeystore) {
            create("release") {
                storeFile = keystoreFile
                storePassword = secret("RELEASE_STORE_PASSWORD")
                keyAlias = secret("RELEASE_KEY_ALIAS")
                keyPassword = secret("RELEASE_KEY_PASSWORD")
                // v2 covers everything from Android 7.0, which is our minimum.
                // v1 stays on because some sideloading paths and file managers
                // still look for it, and this app is distributed by sideload.
                enableV1Signing = true
                enableV2Signing = true
            }
        }
    }

    buildTypes {
        release {
            signingConfig = if (hasReleaseKeystore) {
                signingConfigs.getByName("release")
            } else {
                // Assembles, but produces a package that cannot be distributed.
                // The warning below says so at configuration time.
                signingConfigs.getByName("debug")
            }
            // Left off on purpose. R8 strips the members Firebase reads by
            // reflection when it deserializes a snapshot into a data class, and
            // a release build that silently returns empty records is far worse
            // than a slightly larger download. proguard-rules.pro already holds
            // the keep rules needed to turn this on; do it only with time to
            // test a real release build against a real database.
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // Core Android
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")

    // Jetpack Compose (versions managed by the BOM)
    implementation(platform("androidx.compose:compose-bom:2024.12.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.activity:activity-compose:1.9.3")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Lifecycle & ViewModel
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.8.5")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")

    // Two renderers. Google Maps is used when MAPS_API_KEY is set; osmdroid
    // draws OpenStreetMap tiles when it is not, so the app still shows a map on
    // a fresh clone, and clearing the key is a working fallback if the billing
    // account behind it ever lapses.
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("org.osmdroid:osmdroid-android:6.1.20")

    // Device location. Free; nothing here touches a billed API.
    implementation("com.google.android.gms:play-services-location:21.3.0")

    // Networking
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // Serialization
    implementation("com.google.code.gson:gson:2.11.0")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}

// Warn at configuration time rather than let someone discover at install time
// that they have handed out a debug-signed build. Reading the requested task
// names keeps this to plain Kotlin; the task-graph callback has a Groovy
// Closure overload that Kotlin picks in preference to the Action one.
val buildingRelease = gradle.startParameter.taskNames.any {
    it.contains("release", ignoreCase = true)
}
// A warning scrolls past in a long build, and the output is an APK that looks
// exactly like a real one. Refusing to produce it is the only version of this
// that cannot end with a debug-signed build in a tester's hands. Debug builds
// are unaffected: the check only fires for a release task.
if (buildingRelease && !hasReleaseKeystore) {
    throw GradleException(
        "\n=====================================================================\n" +
            "  No release keystore configured, so this build would be signed with\n" +
            "  the debug key and could never be updated once installed.\n" +
            "  Set RELEASE_STORE_FILE, RELEASE_STORE_PASSWORD, RELEASE_KEY_ALIAS\n" +
            "  and RELEASE_KEY_PASSWORD in .env — see .env.example.\n" +
            "  To build the app without signing it, use a debug task instead:\n" +
            "      ./gradlew assembleDebug\n" +
            "====================================================================="
    )
}
```

#### `build.gradle.kts`

```kotlin
plugins {
    id("com.android.application") version "8.7.3" apply false
    id("com.android.library") version "8.7.3" apply false
    id("org.jetbrains.kotlin.android") version "2.1.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.0" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}
```

#### `settings.gradle.kts`

```kotlin
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "TrikRide"
include(":app")
```

#### `database.rules.json`

```json
{
  "rules": {
    "users": {
      ".read": "auth != null && root.child('users').child(auth.uid).child('userType').val() === 'ADMIN'",
      "$uid": {
        ".read": "auth != null && ($uid === auth.uid || root.child('users').child(auth.uid).child('userType').val() === 'ADMIN')",
        ".write": "auth != null && $uid === auth.uid",
        "userType": {
          ".validate": "newData.val() === 'PASSENGER' || newData.val() === 'DRIVER'"
        },
        "firstName": { ".validate": "newData.isString() && newData.val().length <= 120" },
        "lastName": { ".validate": "newData.isString() && newData.val().length <= 120" },
        "email": { ".validate": "newData.isString() && newData.val().length <= 320" },
        "phoneNumber": { ".validate": "newData.isString() && newData.val().length <= 32" },
        "idNumber": { ".validate": "newData.isString() && newData.val().length <= 64" },
        "birthDate": { ".validate": "newData.isString() && newData.val().length <= 40" },
        "$other": { ".validate": "!newData.isString() || newData.val().length <= 512" }
      }
    },
    "drivers": {
      ".read": "auth != null && root.child('users').child(auth.uid).child('userType').val() === 'ADMIN'",
      "$uid": {
        ".read": "auth != null",
        ".write": "auth != null && $uid === auth.uid",
        "verificationStatus": {
          ".write": "auth != null && root.child('users').child(auth.uid).child('userType').val() === 'ADMIN'",
          ".validate": "root.child('users').child(auth.uid).child('userType').val() === 'ADMIN' || newData.val() === data.val() || (!data.exists() && newData.val() === 'PENDING')"
        },
        "rating": {
          ".validate": "newData.isNumber() && newData.val() >= 0 && newData.val() <= 5"
        },
        "ratingCount": {
          ".validate": "newData.isNumber() && newData.val() >= 0"
        },
        "totalRides": {
          ".validate": "newData.isNumber() && newData.val() >= 0"
        },
        "tricycleNumber": {
          ".validate": "newData.isString() && newData.val().length <= 32"
        },
        "hasLicenceImage": {
          ".write": "auth != null && ($uid === auth.uid || root.child('users').child(auth.uid).child('userType').val() === 'ADMIN')"
        }
      }
    },
    "driverRatings": {
      "$driverId": {
        ".read": "auth != null",
        "$rideId": {
          ".write": "auth != null && root.child('rides').child($rideId).child('passengerId').val() === auth.uid && root.child('rides').child($rideId).child('driverId').val() === $driverId && root.child('rides').child($rideId).child('status').val() === 'COMPLETED'",
          ".validate": "newData.hasChildren(['stars', 'raterId']) || (newData.isNumber() && newData.val() >= 1 && newData.val() <= 5)",
          "stars": { ".validate": "newData.isNumber() && newData.val() >= 1 && newData.val() <= 5" },
          "raterId": { ".validate": "newData.val() === auth.uid" }
        }
      }
    },
    "driverDocuments": {
      "$uid": {
        ".read": "auth != null && ($uid === auth.uid || root.child('users').child(auth.uid).child('userType').val() === 'ADMIN')",
        ".write": "auth != null && ($uid === auth.uid || root.child('users').child(auth.uid).child('userType').val() === 'ADMIN')",
        "licence": {
          "image": { ".validate": "!newData.exists() || (newData.isString() && newData.val().length <= 300000)" },
          "licenceNumber": { ".validate": "!newData.exists() || (newData.isString() && newData.val().length <= 64)" },
          "licenceExpiry": { ".validate": "!newData.exists() || (newData.isString() && newData.val().length <= 40)" }
        }
      }
    },
    "rideRequests": {
      ".read": "auth != null && (root.child('drivers').child(auth.uid).child('verificationStatus').val() === 'APPROVED' || root.child('users').child(auth.uid).child('userType').val() === 'ADMIN' || (query.orderByChild === 'passengerId' && query.equalTo === auth.uid))",
      ".indexOn": ["passengerId"],
      "$requestId": {
        ".read": "auth != null && data.child('passengerId').val() === auth.uid",
        ".write": "auth != null && ((!data.exists() && newData.child('passengerId').val() === auth.uid) || (data.exists() && (data.child('passengerId').val() === auth.uid || root.child('drivers').child(auth.uid).child('verificationStatus').val() === 'APPROVED')))",
        "notes": { ".validate": "newData.isString() && newData.val().length <= 500" },
        "luggage": { ".validate": "newData.isString() && newData.val().length <= 200" },
        "passengerCount": { ".validate": "newData.isNumber() && newData.val() >= 1 && newData.val() <= 5" },
        "regularCount": { ".validate": "newData.isNumber() && newData.val() >= 0 && newData.val() <= 5" },
        "discountedCount": { ".validate": "newData.isNumber() && newData.val() >= 0 && newData.val() <= 5" },
        "estimatedFare": { ".validate": "newData.isNumber() && newData.val() >= 0 && newData.val() <= 10000" }
      }
    },
    "rides": {
      ".read": "auth != null && (root.child('users').child(auth.uid).child('userType').val() === 'ADMIN' || ((query.orderByChild === 'passengerId' || query.orderByChild === 'driverId') && query.equalTo === auth.uid))",
      ".indexOn": ["passengerId", "driverId"],
      "$rideId": {
        ".read": "auth != null && (data.child('passengerId').val() === auth.uid || data.child('driverId').val() === auth.uid)",
        ".write": "auth != null && ((!data.exists() && newData.child('driverId').val() === auth.uid) || (data.exists() && data.child('driverId').val() === auth.uid))",
        ".validate": "!newData.exists() || data.exists() || root.child('drivers').child(newData.child('driverId').val()).child('verificationStatus').val() === 'APPROVED'",
        "passengerId": {
          ".validate": "newData.isString() && (!data.exists() || newData.val() === data.val())"
        },
        "estimatedFare": {
          ".validate": "newData.isNumber() && newData.val() >= 0 && newData.val() <= 10000 && (!data.exists() || newData.val() === data.val())"
        },
        "notes": { ".validate": "newData.isString() && newData.val().length <= 500" },
        "status": {
          ".write": "auth != null && (data.parent().child('driverId').val() === auth.uid || (data.parent().child('passengerId').val() === auth.uid && newData.val() === 'CANCELLED' && (data.val() === 'ACCEPTED' || data.val() === 'DRIVER_ARRIVING' || data.val() === 'DRIVER_ARRIVED')))"
        },
        "completedAt": {
          ".write": "auth != null && (data.parent().child('driverId').val() === auth.uid || data.parent().child('passengerId').val() === auth.uid)"
        },
        "actualFare": {
          ".validate": "newData.isNumber() && newData.val() >= 0 && newData.val() <= 10000"
        },
        "passengerName": {
          ".write": "auth != null && data.parent().child('passengerId').val() === auth.uid",
          ".validate": "newData.isString() && newData.val().length <= 120"
        },
        "passengerPhone": {
          ".write": "auth != null && data.parent().child('passengerId').val() === auth.uid",
          ".validate": "newData.isString() && newData.val().length <= 32"
        }
      }
    },
    "config": {
      ".read": "auth != null",
      ".write": "auth != null && root.child('users').child(auth.uid).child('userType').val() === 'ADMIN'",
      "fare": {
        "minimumRegular": { ".validate": "newData.isNumber() && newData.val() >= 0 && newData.val() <= 1000" },
        "minimumDiscounted": { ".validate": "newData.isNumber() && newData.val() >= 0 && newData.val() <= 1000" },
        "poblacionFlat": { ".validate": "newData.isNumber() && newData.val() >= 0 && newData.val() <= 1000" },
        "terminalRoundTrip": { ".validate": "newData.isNumber() && newData.val() >= 0 && newData.val() <= 1000" }
      },
      "fareStops": {
        "$stopId": {
          "regularFare": { ".validate": "newData.isNumber() && newData.val() >= 0 && newData.val() <= 1000" },
          "discountedFare": { ".validate": "newData.isNumber() && newData.val() >= 0 && newData.val() <= 1000" },
          "name": { ".validate": "newData.isString() && newData.val().length <= 200" },
          "zone": { ".validate": "newData.isString() && newData.val().length <= 100" },
          "note": { ".validate": "newData.isString() && newData.val().length <= 1000" },
          "latitude": { ".validate": "newData.isNumber() && newData.val() >= -90 && newData.val() <= 90" },
          "longitude": { ".validate": "newData.isNumber() && newData.val() >= -180 && newData.val() <= 180" }
        }
      }
    },
    "complaints": {
      ".read": "auth != null && (root.child('users').child(auth.uid).child('userType').val() === 'ADMIN' || (query.orderByChild === 'reporterId' && query.equalTo === auth.uid))",
      ".indexOn": ["reporterId"],
      "$id": {
        ".read": "auth != null && (data.child('reporterId').val() === auth.uid || root.child('users').child(auth.uid).child('userType').val() === 'ADMIN')",
        ".write": "auth != null && ((!data.exists() && newData.child('reporterId').val() === auth.uid) || root.child('users').child(auth.uid).child('userType').val() === 'ADMIN')",
        "description": { ".validate": "newData.isString() && newData.val().length <= 2000" },
        "adminNote": { ".validate": "newData.isString() && newData.val().length <= 2000" },
        "status": {
          ".write": "auth != null && root.child('users').child(auth.uid).child('userType').val() === 'ADMIN'",
          ".validate": "root.child('users').child(auth.uid).child('userType').val() === 'ADMIN' || newData.val() === data.val() || (!data.exists() && newData.val() === 'OPEN')"
        }
      }
    },
    "notifications": {
      "$uid": {
        ".read": "auth != null && $uid === auth.uid",
        ".write": "auth != null && ($uid === auth.uid || root.child('users').child(auth.uid).child('userType').val() === 'ADMIN' || root.child('drivers').child(auth.uid).child('verificationStatus').val() === 'APPROVED')",
        "$id": {
          ".validate": "newData.hasChildren(['title', 'message'])",
          "title": { ".validate": "newData.isString() && newData.val().length <= 120" },
          "message": { ".validate": "newData.isString() && newData.val().length <= 2000" }
        }
      }
    },
    "profilePhotos": {
      "$uid": {
        ".read": "auth != null && ($uid === auth.uid || root.child('users').child(auth.uid).child('userType').val() === 'ADMIN')",
        ".write": "auth != null && $uid === auth.uid",
        "data": { ".validate": "newData.isString() && newData.val().length <= 40000" }
      }
    }
  }
}
```

## Appendix K — Database Schema
The structure of the Realtime Database is documented in Table 8 and illustrated in Figure 11. The security rules governing access are reproduced below in outline.

| Node | Read | Write |
|:---|:---|:---|
| `users/{uid}` | The account holder and administrators | The account holder, including the accepted-document fields, except `userType`, which is set at registration |
| `drivers/{uid}` | The driver, administrators, and authenticated users reading availability | The driver, except `verificationStatus`, which is writable only by administrators |
| `rideRequests/{id}` | Authenticated, verified, available drivers and the requesting passenger | The requesting passenger to create and cancel; the accepting driver to delete on acceptance |
| `rides/{id}` | The passenger and driver named on the ride, and administrators | The accepting driver for status; the system for creation |
| `config/fare` and `config/fareStops` | All authenticated users | Administrators only |
| `complaints/{id}` | The reporter and administrators | The reporter to create; administrators for status and note |
| `notifications/{uid}` | The named user | The system to create; the named user to mark as read |
| `profilePhotos/{uid}` | All authenticated users | The account holder only |
| `driverDocuments/{uid}` | The driver and administrators only | The driver, and administrators in order to delete on refusal |
| `driverRatings/{uid}` | All authenticated users | Each rating only by the passenger who gave it, and only as a value from one to five |

## Appendix L — Test Cases
The test cases executed in this study are documented in Tables 11 through 14 in Section 4.5, covering unit testing, integration testing, system testing, and security testing. The user acceptance test tasks are as follows.

| ID | Role | Task given to the respondent | Success criterion |
|:---|:---|:---|:---|
| UAT-01 | Passenger | Install the application and create an account | An account is created without assistance |
| UAT-02 | Passenger | Book a ride to a named destination | The correct destination and fare are selected and the request is submitted |
| UAT-03 | Passenger | Find out what the discounted fare would be for the same trip | The rate column is switched and the lower fare is read correctly |
| UAT-04 | Passenger | Report a problem with a completed ride | A concern is submitted under an appropriate category |
| UAT-05 | Passenger | Find last week's rides | Ride history is located |
| UAT-06 | Driver | Register, accept the Driver Agreement, and submit credentials | The agreement is read and accepted and credentials are submitted without assistance |
| UAT-07 | Driver | Begin receiving requests | The availability toggle is found and set to online |
| UAT-08 | Driver | Accept a request and carry the ride to completion | Every status stage is advanced in order |
| UAT-09 | Driver | Determine earnings for the period | The earnings figure is located |
| UAT-10 | Administrator | Approve a pending driver | The application is located and approved |
| UAT-11 | Administrator | Correct a fare that reads incorrectly | The entry is found by search, edited, and saved |
| UAT-12 | Administrator | Produce last month's ride report | The period is selected and the report is exported in both formats |

## Appendix M — Test Results

The results of unit, integration, system, and security testing are recorded in Tables 11 through 14.

User acceptance testing was carried out with forty-one respondents — twenty student passengers, twenty tricycle drivers, and the system administrator. The returns are reproduced in full in Appendix F.

Every task in the acceptance set listed in Appendix L was completed by the respondents who attempted it. Observed difficulty was confined to reading the interface rather than to operating it, and was concentrated among the driver respondents.

The statistical treatment of the evaluation returns, carried out by the study's statistician using frequency count, weighted mean and ranking, is computed in full in Appendix G and interpreted in Section 4.9. The overall results were as follows.

| Respondent group | N | Overall weighted mean | Interpretation |
|:---|---:|---:|:---|
| Student passengers | 20 | 4.30 | Strongly Agree |
| Tricycle drivers | 20 | 4.29 | Strongly Agree |
| System administrator | 1 | 4.67 | Strongly Agree |

No characteristic fell below *Agree* for any group. Eleven of the thirteen category means fall in *Strongly Agree*; the two that fall in *Agree* are Usability among drivers, at 4.20, and Reliability for the administrator, at 4.00. Across the two groups of twenty, category means ran from 4.20 to 4.38, the latter being Reliability among drivers. The administrator's three categories, each from one respondent, were 5.00 for Functional Suitability, 5.00 for Usability, and 4.00 for Reliability. Appendix G records three qualifications: a duplicated item in the passenger instrument, the single administrator respondent, and the narrowness of the response distribution.

Performance measurements and the defect log are to be inserted here.

*[The defect log should record, for each defect found: an identifier, the module, a description, the severity, the date found, the resolution, and the date resolved.]*

## Appendix N — Sample Reports
*[Exported files are to be inserted here as printed extracts. The following three are produced by the system, each in both of the formats described in section 4.8.]*

**Sample 1 — Ride activity report** for a selected month: the summary page carrying the headline figures and the four charts, followed by a representative extract of the per-ride rows.

**Sample 2 — Driver performance report** for the same month: the summary page, then per-driver totals ordered by rides accepted, and the list of drivers with no rides in the period.

**Sample 3 — Concerns report** for the same month: the summary page, then the per-concern rows.

The columns of the ride activity spreadsheet are reproduced below to indicate what the export contains.

```
Ride ID, Requested, Accepted, Started, Completed, Passenger, Passenger email,
Driver, Driver email, Pickup, Destination, Passengers, Luggage, Status,
Estimated fare, Actual fare, Notes
```

## Appendix O — Documentation of Data Gathering
*[Photographs and records of the data gathering activities are to be inserted here, with the consent of those pictured.]*

The following should be documented: the needs assessment administration to students and to drivers; the meeting with officers of the drivers' association; the photographs of the posted FeTODAT fare sheet from which the fare table was transcribed; the driver orientation session; the evaluation period; and the administration of the evaluation questionnaire.

## Appendix P — Gantt Chart
![Figure 14. Gantt Chart of Project Activities](figures/fig14_gantt.png){width=6.0in}

[[PB]]

## Appendix Q — Researchers' Biodata
### CURRICULUM VITAE

**ALBER JUNE M. MUMAR**

Purok 6, Poblacion, Talibon, Bohol

6325 Philippines

Cellphone Number: 0962 938 4692

Email Address: alberjunemumar@gmail.com

**Personal Data**

| | |
|:---|:---|
| Age | 21 |
| Birthdate | June 20, 2004 |
| Civil Status | Single |
| Religion | Roman Catholic |
| Father's Name | Robert P. Mumar |
| Mother's Name | Alma M. Mumar |

**Educational Attainment**

| | |
|:---|:---|
| Secondary | San Jose National High School, San Jose, Talibon, Bohol |
| Elementary | Talibon I Central Elementary School, Poblacion, Talibon, Bohol |

[[PB]]

### CURRICULUM VITAE

**JULEBETH HINLAYAGAN**

Purok 5, Mabuhay Cabiguhan, Trinidad, Bohol

6325 Philippines

Cellphone Number: 0977 725 7182

Email Address: hinlayaganbeth@gmail.com

**Personal Data**

| | |
|:---|:---|
| Age | 24 |
| Birthdate | July 24, 2001 |
| Civil Status | Single |
| Religion | Roman Catholic |
| Father's Name | Policronio Rosales Sr. |
| Mother's Name | Marissa F. Hinlayagan |

**Educational Attainment**

| | |
|:---|:---|
| Secondary | Calinan National High School, Calinan, Poblacion, Davao City |
| Elementary | Lpt. Cipriano Senior Elementary School, Calinan, Poblacion, Davao City |

[[PB]]

### CURRICULUM VITAE

**MARDY GONZAGA**

San Carlos, Talibon, Bohol

6325 Philippines

Cellphone Number: 0946 240 7802

Email Address: mardygonzaga@gmail.com

**Personal Data**

| | |
|:---|:---|
| Age | 24 |
| Birthdate | December 3, 2002 |
| Civil Status | Single |
| Religion | Roman Catholic |
| Father's Name | Teddy Gonzaga |
| Mother's Name | Marlyn Gonzaga |

**Educational Attainment**

| | |
|:---|:---|
| Secondary | San Jose National High School, San Jose, Talibon, Bohol |
| Elementary | Garcia Park Elementary School, San Carlos, Talibon, Bohol |

## Appendix R — Legal Documents Presented In-App
Four documents are carried inside the application and are readable at any time from the
profile screen. The Terms and Conditions, the Privacy Policy and the Safety and Community
Guidelines must each be accepted before the service becomes available; the Driver
Agreement is presented to a driver after they choose a driver account. Acceptance is
recorded against a version string, so that a revision asks every existing user again on
their next launch.

They are reproduced below as issued. The text held in the application is the copy of
record, since it is what a user reads before agreeing; its source is
`app/src/main/java/com/tpc/trikride/ui/screens/LegalScreen.kt`, and Markdown copies
generated from it are in `docs/legal/`; the strings themselves appear in Appendix J.

[[PB]]

### D.1 Terms and Conditions

*Effective Date: July 28, 2026*

By creating an account or using TrikRide, you agree to comply with these Terms and Conditions.

**1. Eligibility**

Users must be:
- Registered students, faculty, or authorized personnel of the participating institution.
- Registered and approved drivers for driver accounts.

**2. Account Registration**

Users agree to:
- Provide accurate and complete information.
- Maintain only one active account unless otherwise authorized.
- Keep login credentials confidential.
- Notify the administrator immediately if they suspect unauthorized access to their account.

**3. Ride Booking**

Passengers agree to:
- Enter accurate pickup and destination locations.
- Be present at the designated pickup point on time.
- Treat drivers and fellow passengers with courtesy and respect.

Drivers agree to:
- Maintain valid registration and any required permits.
- Arrive at pickup locations promptly whenever possible.
- Provide safe, respectful, and professional service.
- Follow all applicable traffic laws and institutional policies.

**4. Prohibited Activities**

Users shall not:
- Create fake or fraudulent accounts.
- Impersonate another person.
- Submit false booking requests.
- Harass, threaten, or discriminate against other users.
- Attempt unauthorized access to the system.
- Use the application for illegal or unlawful activities.

Violations may result in temporary suspension or permanent removal from the TrikRide platform.

**5. Limitation of Liability**

TrikRide is a ride scheduling and driver matching platform. While we strive to provide reliable service, we cannot guarantee uninterrupted availability and are not responsible for delays caused by traffic, weather, vehicle issues, or other circumstances beyond our reasonable control.

**6. Account Suspension**

The system administrator reserves the right to suspend or terminate accounts found to be in violation of these Terms and Conditions.

**7. Intellectual Property**

All application content, including the TrikRide name, logo, interface design, graphics, source code, and documentation, is owned by the TrikRide development team unless otherwise stated. Unauthorized reproduction or distribution is prohibited.

**8. Amendments**

These Terms and Conditions may be updated from time to time. Continued use of TrikRide after changes are published constitutes acceptance of the updated Terms.

**9. Governing Rules**

These Terms shall be governed by applicable Philippine laws and the policies of the participating educational institution.

**10. Acceptance**

By registering and using TrikRide, you confirm that you have read, understood, and agreed to these Terms and Conditions and the Privacy Policy.

[[PB]]

### D.2 Privacy Policy

*Effective Date: August 16, 2026*

**1. Information We Collect**

We collect the information you provide during registration (name, email, phone number, date of birth, and — for drivers — license and tricycle details), an optional profile photo, and information generated while using the app (ride requests, pickup/destination, ride history, and any concerns you report).

Drivers are also asked for a photograph of their driver's license. Under the Data Privacy Act of 2012 (Republic Act No. 10173) a license is sensitive personal information, so it is treated separately from everything else in this policy and is covered by section 9 below. You are asked to agree to it specifically at the moment you send it, not merely by accepting this policy.

**2. How We Use Information**

Your information is used to create your account, match passengers with drivers, price rides, support driver verification, and improve the service.

**3. Location**

Location is used to show pickup/destination and, for drivers, availability. Location is only used while you are using the relevant features of the app.

**4. Data Storage**

Account, ride, and profile photo data are stored in Google Firebase. A profile photo is reduced to a small thumbnail before it is stored. Communications with the server are encrypted in transit. TrikRide does not collect card, bank, or any other payment details; fares are paid in cash directly to the driver.

**5. Sharing**

A passenger's ride details are shared with the assigned driver (and vice versa) to complete the ride. Administrators can view driver records and ride logs to operate and monitor the service. We do not sell your personal information.

**6. Your Choices**

You can edit your profile details and sign out at any time. You may request account concerns or corrections through the Support feature.

**7. Children**

The service is intended for members of the college community and is not directed at children under 13.

**8. Contact**

For privacy questions, contact the TrikRide support hotline listed in the app.

**9. Driver's License Photographs**

This section applies only to drivers, and only to the photograph of the license itself.

Purpose. The photograph is used for one thing: to confirm that the person applying to carry passengers holds the license they say they hold, and to check it again when that license expires. It is not used for anything else.

Who can see it. You, and a TrikRide administrator. It is never shown to passengers, never attached to a ride, and never included in any exported report.

How long we keep it. If your application is refused, the photograph is deleted at the moment of that decision. If you are approved, it is kept while your account is active, because it is needed again at renewal and if a concern about a ride is ever disputed. Withdrawing an approval already given is not the same as refusing an application and does not delete the photograph, for that same reason. It is deleted with your account.

Your control. You may remove the photograph yourself at any time from your driver profile. Removing it means you cannot be approved to carry passengers until you provide another one.

How it is stored. Separately from your account record, so that ordinary use of the app never reads it, and reduced in size before it is stored. Communications are encrypted in transit.

[[PB]]

### D.3 Safety and Community Guidelines

*Effective Date: July 28, 2026*

**Our Commitment**

TrikRide is committed to providing a safe, respectful, and reliable transportation environment for students, drivers, faculty, and staff.

**Respect Everyone**

- Treat all users with courtesy and professionalism.
- Avoid abusive, offensive, discriminatory, or threatening language.
- Respect personal space and privacy.

**Safe Riding**

- Wait at the designated pickup location.
- Verify the driver's identity before boarding.
- Follow the driver's safety instructions during the trip.
- Remain seated while the vehicle is moving.
- Do not distract the driver while driving.

**Driver Responsibilities**

Drivers are expected to:
- Drive safely and obey all traffic laws.
- Maintain a roadworthy and clean vehicle.
- Arrive at pickup locations as promptly as possible.
- Treat every passenger fairly and respectfully.
- Never operate a vehicle while under the influence of alcohol or illegal drugs.

**Passenger Responsibilities**

Passengers are expected to:
- Arrive on time for scheduled pickups.
- Respect the driver's vehicle and property.
- Avoid behavior that may endanger others.
- Report emergencies or unsafe situations immediately.

**Prohibited Conduct**

The following are strictly prohibited:
- Violence or physical assault.
- Sexual harassment or misconduct.
- Bullying, intimidation, or discrimination.
- Possession or use of illegal drugs.
- Carrying dangerous weapons or prohibited items.
- Vandalism or intentional damage to vehicles.
- Providing false information or fake bookings.

**Reporting Safety Concerns**

Users are encouraged to report:
- Unsafe driving.
- Harassment or inappropriate behavior.
- Fake accounts or fraudulent activities.
- Vehicle safety issues.
- Lost belongings.

Reports will be reviewed by authorized administrators, and appropriate action may be taken. Use the Support tab to file a report.

**Account Enforcement**

Violations of these Community Guidelines may result in:
- Warning notices.
- Temporary account suspension.
- Permanent account removal.
- Referral to school authorities or law enforcement when necessary.

By using TrikRide, all users agree to help maintain a safe, respectful, and welcoming community.

[[PB]]

### D.4 Driver Agreement

*Effective Date: August 16, 2026*

This Driver Agreement establishes the responsibilities and expectations for all drivers using the TrikRide platform.

**Driver Eligibility**

To become a TrikRide driver, you must:
- Be at least 18 years old.
- Possess a valid driver's license appropriate for the vehicle operated.
- Operate a legally registered tricycle or authorized vehicle.
- Submit a legible photograph of that license for verification, and keep a current one on file.
- Complete the registration and verification process required by TrikRide.

**Your License Photograph**

The photograph you submit is checked by an administrator against the license details you entered, and again when the license expires. Only you and an administrator can view it; it is never shown to passengers. If your application is refused it is deleted immediately. If you are approved it is kept while your account is active and deleted with the account; that remains so if your approval is later withdrawn, since the reason for withdrawing it may itself need to be evidenced. You may remove it yourself at any time, though you cannot carry passengers without one on file. Section 9 of the Privacy Policy sets this out in full.

TrikRide checks that a document was presented and that it matches what you entered. It does not and cannot confirm with the Land Transportation Office that a license is current or has not been suspended. Driving on a valid license remains your responsibility, and submitting a false or altered document ends your access to the platform.

**Driver Responsibilities**

Drivers agree to:
- Provide accurate personal and vehicle information.
- Keep account information updated.
- Drive safely and comply with all traffic laws.
- Treat all passengers respectfully and without discrimination.
- Arrive at pickup locations as promptly as possible.
- Notify passengers through the app if delays occur.
- Maintain a clean and safe vehicle.

**Professional Conduct**

Drivers shall:
- Wear appropriate attire while providing transportation services.
- Avoid abusive or inappropriate language.
- Respect passenger privacy.
- Never ask for personal information unrelated to the ride.

**Safety Requirements**

Drivers shall never:
- Drive while under the influence of alcohol or illegal drugs.
- Allow unauthorized persons to operate their registered vehicle.
- Accept bookings using another driver's account.
- Endanger passengers through reckless driving.

**Account Suspension or Termination**

TrikRide may suspend or terminate a driver's account for:
- Repeated complaints.
- Unsafe driving practices.
- Fraudulent activity.
- Submission of false documents.
- Violation of this Agreement or applicable laws.

**Limitation of Responsibility**

Drivers acknowledge that TrikRide functions as a ride scheduling and matching platform. Drivers remain responsible for complying with all traffic regulations and for the safe operation of their vehicles.

**Agreement**

By registering as a TrikRide driver, you confirm that you have read, understood, and agreed to abide by this Driver Agreement.

[[PB]]
