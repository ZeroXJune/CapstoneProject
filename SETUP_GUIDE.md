# TrikRide Setup Guide

## Prerequisites

Before you begin, ensure you have:
- Android Studio 2023.1.1 or later
- Android SDK 24 or higher
- Kotlin 1.9.0 or later
- Java Development Kit (JDK) 17
- Firebase Account
- Google Cloud Project

## Step 1: Clone the Repository

```bash
git clone https://github.com/zeroxjune/capstoneproject.git
cd capstoneproject
```

## Step 2: Open in Android Studio

1. Launch Android Studio
2. Select "Open" and navigate to the project directory
3. Wait for Gradle sync to complete

## Step 3: Firebase Setup

### 3.1 Create Firebase Project

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click "Add project"
3. Enter "TrikRide" as project name
4. Enable Google Analytics (optional)
5. Create the project

### 3.2 Register Android App

1. In Firebase Console, click "Add app" → "Android"
2. Enter package name: `com.tpc.trikride`
3. Enter app nickname: `TrikRide`
4. Get your SHA-1 fingerprint:
   ```bash
   ./gradlew signingReport
   ```
5. Register the app and download `google-services.json`
6. Place `google-services.json` in `app/` directory (see `app/google-services.json.template`
   for the expected shape). The build automatically enables the Google Services plugin
   once the file exists — the project still compiles without it.

### 3.3 Enable Firebase Services

In Firebase Console, enable:
- **Authentication**: Email/Password
- **Realtime Database**: Create database in production mode
- **Cloud Storage**: Create bucket for documents
- **Cloud Messaging**: For push notifications

## Step 4: Configure Firebase Security Rules

### Realtime Database Rules

```json
{
  "rules": {
    "users": {
      "$uid": {
        ".read": "$uid === auth.uid",
        ".write": "$uid === auth.uid"
      }
    },
    "drivers": {
      "$uid": {
        ".read": true,
        ".write": "$uid === auth.uid"
      }
    },
    "rideRequests": {
      "$requestId": {
        ".read": true,
        ".write": "root.child('users').child(auth.uid).exists()"
      }
    },
    "rides": {
      "$rideId": {
        ".read": "root.child('rides').child($rideId).child('passengerId').val() === auth.uid || root.child('rides').child($rideId).child('driverId').val() === auth.uid",
        ".write": "root.child('users').child(auth.uid).exists()"
      }
    }
  }
}
```

## Step 5: Google Maps API Setup

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Enable Maps SDK for Android
3. Create API key with Android restrictions
4. Add the key to `local.properties` in the project root (this file is gitignored,
   so the key never reaches version control):
   ```properties
   MAPS_API_KEY=AIza...your_key_here
   ```
   The build injects it into the manifest automatically via a placeholder.

## Step 6: Build and Run

```bash
# Build the project
./gradlew build

# Run on emulator or device
./gradlew installDebug
```

## Step 7: Configure Local Properties (Optional)

Create `local.properties` file in root directory:

```properties
sdk.dir=/path/to/android/sdk
ndk.dir=/path/to/android/ndk
```

## Environment Configuration

### Development Environment

Update `build.gradle.kts` for development:

```kotlin
buildTypes {
    debug {
        debuggable = true
        minifyEnabled = false
    }
}
```

### Production Environment

```kotlin
buildTypes {
    release {
        debuggable = false
        minifyEnabled = true
        proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
        )
    }
}
```

## Troubleshooting

### Gradle Sync Issues

```bash
./gradlew clean
./gradlew build --refresh-dependencies
```

### Firebase Connection Issues

1. Verify `google-services.json` is in `app/` directory
2. Check Firebase project ID matches app configuration
3. Ensure Firebase services are enabled in console

### Location Permission Issues

1. Grant location permissions in device settings
2. For Android 6.0+, runtime permissions are requested in app
3. Background location requires additional permissions

## Database Structure

```
users/
  ├── {userId}
  │   ├── email
  │   ├── phoneNumber
  │   ├── firstName
  │   ├── lastName
  │   ├── userType (PASSENGER/DRIVER/ADMIN)
  │   └── profileImageUrl

drivers/
  ├── {driverId}
  │   ├── licenseNumber
  │   ├── tricycleNumber
  │   ├── isAvailable
  │   ├── currentLocation
  │   ├── rating
  │   ├── totalRides
  │   └── verificationStatus

rides/
  ├── {rideId}
  │   ├── passengerId
  │   ├── driverId
  │   ├── pickupLocation
  │   ├── dropoffLocation
  │   ├── status
  │   ├── estimatedFare
  │   └── actualFare

rideRequests/
  ├── {requestId}
  │   ├── passengerId
  │   ├── pickupLocation
  │   ├── dropoffLocation
  │   └── requestedAt
```

## API Integration

### Authentication API

- POST `/auth/login` - User login
- POST `/auth/signup` - User registration
- POST `/auth/logout` - User logout
- POST `/auth/refresh` - Refresh token

### Ride API

- POST `/rides/request` - Create ride request
- GET `/rides/{rideId}` - Get ride details
- PUT `/rides/{rideId}/status` - Update ride status
- GET `/rides/history` - Get ride history

### Driver API

- POST `/drivers/register` - Register as driver
- GET `/drivers/{driverId}` - Get driver info
- PUT `/drivers/{driverId}/availability` - Update availability
- POST `/drivers/verify` - Submit verification documents

## Testing

Run tests with:

```bash
./gradlew test
./gradlew connectedAndroidTest
```

## Deployment

### Sign Release APK

```bash
./gradlew bundleRelease
```

### Upload to Play Store

1. Generate signed APK/AAB
2. Create Play Store listing
3. Upload to Google Play Console
4. Configure rollout percentage
5. Monitor crash reports

## Support

For issues or questions:
- Email: support@trikride.com
- GitHub Issues: https://github.com/zeroxjune/capstoneproject/issues

## License

© 2026 Talibon Polytechnic College. All rights reserved.
