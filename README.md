# TrikRide - Smart Tricycle Ride & Driver Onboarding System

A mobile application for Talibon Polytechnic College that connects students, faculty, and staff with verified tricycle drivers through a digital platform.

## Project Overview

TrikRide is a capstone project by Alber June M. Mumar, Julebeth Hinlayagan, and Mardy Gonzaga from the Department of Information Systems at Talibon Polytechnic College.

**Problem Addressed:**
- Students waste time finding available tricycles manually
- Drivers lack organized ride request management
- No formal driver verification process
- Poor coordination between passengers and drivers

## Key Features

### Passenger Features
- Digital ride booking with location selection
- Real-time driver matching and notifications
- Live ride tracking
- Driver ratings and reviews
- Ride history

### Driver Features
- Driver registration and onboarding
- Document verification process
- Incoming ride notifications
- Ride acceptance/rejection
- Earnings tracking
- Availability management

### Admin Features
- Driver verification dashboard
- System monitoring and analytics
- Ride history and logs
- User management

## Technology Stack

- **Frontend:** Android with Kotlin
- **Backend:** Firebase (Realtime Database & Cloud Functions)
- **Authentication:** Firebase Authentication
- **Maps:** Google Maps API
- **Design Tool:** Figma
- **Version Control:** Git/GitHub

## Project Structure

```
TrikRide/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml
│   │   │   ├── java/
│   │   │   │   └── com/talibon/trikride/
│   │   │   │       ├── models/
│   │   │   │       ├── ui/
│   │   │   │       ├── services/
│   │   │   │       └── MainActivity.kt
│   │   │   └── res/
│   │   │       ├── layout/
│   │   │       ├── drawable/
│   │   │       └── values/
│   │   └── test/
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## Getting Started

### Prerequisites
- Android Studio Electric Eel or later
- Android SDK 24+
- Kotlin 1.9+
- Firebase account

### Installation

1. Clone the repository:
```bash
git clone https://github.com/zeroxjune/capstoneproject.git
cd capstoneproject
```

2. Open in Android Studio and sync Gradle files

3. Configure Firebase:
   - Download `google-services.json` from Firebase Console
   - Place it in `app/` directory

4. Build and run the application

## Development Phases

1. **Planning & Requirements** ✓
2. **System Design** - In Progress
3. **Development**
   - Passenger App
   - Driver App
   - Admin Dashboard
4. **Testing**
5. **Deployment**

## Authors

- **Alber June M. Mumar** - Lead Developer
- **Julebeth Hinlayagan** - Documentation
- **Mardy Gonzaga** - Documentation

## Advisers

- **Socrates C. Macalolot** - Capstone Adviser
- **Astrid P. Valmoria, MAELT** - English Critic
- **Flordelis A. Turtoga, MAMT** - Statistician

## License

This is a capstone project for Talibon Polytechnic College. All rights reserved.

## Status

**Current Phase:** Development Setup
**Target Completion:** May 2026
