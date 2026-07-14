# TrikRide Application Architecture

## Overview

TrikRide follows a modern Android architecture with clear separation of concerns:

```
┌─────────────────────────────────────────────────┐
│           Presentation Layer (UI)               │
│  ┌──────────────────────────────────────────┐   │
│  │  Screens, Composables, ViewModels        │   │
│  └──────────────────────────────────────────┘   │
└──────────────────────┬──────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────┐
│        Business Logic Layer (Services)          │
│  ┌──────────────────────────────────────────┐   │
│  │  FirebaseService, LocationService, etc   │   │
│  └──────────────────────────────────────────┘   │
└──────────────────────┬──────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────┐
│         Data Layer (Repositories & Database)    │
│  ┌──────────────────────────────────────────┐   │
│  │  Firebase Realtime DB, Cloud Storage     │   │
│  └──────────────────────────────────────────┘   │
└──────────────────────┬──────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────┐
│      External Services & APIs                   │
│  ┌──────────────────────────────────────────┐   │
│  │  Google Maps, Firebase Auth, Messaging   │   │
│  └──────────────────────────────────────────┘   │
└─────────────────────────────────────────────────┘
```

## Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── AndroidManifest.xml
│   │   ├── java/com/talibon/trikride/
│   │   │   ├── MainActivity.kt
│   │   │   ├── models/
│   │   │   │   ├── User.kt
│   │   │   │   ├── Ride.kt
│   │   │   │   └── ...
│   │   │   ├── services/
│   │   │   │   ├── FirebaseService.kt
│   │   │   │   ├── LocationService.kt
│   │   │   │   ├── AuthenticationService.kt
│   │   │   │   └── TrikRideMessagingService.kt
│   │   │   ├── repositories/
│   │   │   │   ├── UserRepository.kt
│   │   │   │   ├── RideRepository.kt
│   │   │   │   ├── DriverRepository.kt
│   │   │   │   └── ...
│   │   │   ├── viewmodels/
│   │   │   │   ├── AuthViewModel.kt
│   │   │   │   ├── RideViewModel.kt
│   │   │   │   ├── DriverViewModel.kt
│   │   │   │   └── ...
│   │   │   ├── ui/
│   │   │   │   ├── screens/
│   │   │   │   │   ├── MainAppScreen.kt
│   │   │   │   │   ├── PassengerHomeScreen.kt
│   │   │   │   │   ├── DriverHomeScreen.kt
│   │   │   │   │   ├── BookRideScreen.kt
│   │   │   │   │   ├── RideTrackingScreen.kt
│   │   │   │   │   └── ...
│   │   │   │   ├── components/
│   │   │   │   │   ├── RideCard.kt
│   │   │   │   │   ├── DriverCard.kt
│   │   │   │   │   ├── LocationPicker.kt
│   │   │   │   │   └── ...
│   │   │   │   └── theme/
│   │   │   │       ├── Theme.kt
│   │   │   │       ├── Color.kt
│   │   │   │       └── Type.kt
│   │   │   └── utils/
│   │   │       ├── Constants.kt
│   │   │       ├── LocationUtils.kt
│   │   │       ├── DateTimeUtils.kt
│   │   │       └── ...
│   │   └── res/
│   │       ├── drawable/
│   │       ├── layout/
│   │       ├── values/
│   │       │   ├── strings.xml
│   │       │   ├── colors.xml
│   │       │   ├── styles.xml
│   │       │   └── dimens.xml
│   │       └── xml/
│   │           ├── backup_rules.xml
│   │           └── data_extraction_rules.xml
│   └── test/
│       └── java/com/talibon/trikride/
│           ├── services/
│           ├── repositories/
│           └── viewmodels/
├── build.gradle.kts
└── proguard-rules.pro
```

## Data Flow Architecture

### Passenger Booking Flow

```
PassengerHomeScreen
    │
    ├─→ RideBookingScreen (Compose UI)
    │       │
    │       ├─→ RideViewModel (State Management)
    │       │       │
    │       │       ├─→ RideRepository (Data Logic)
    │       │       │       │
    │       │       │       └─→ FirebaseService (Database)
    │       │       │
    │       │       └─→ LocationService (GPS)
    │       │
    │       └─→ GoogleMapsCompose (Map Display)
    │
    └─→ RideTrackingScreen
            │
            └─→ Real-time updates via Flow<Ride>
```

### Driver Acceptance Flow

```
DriverHomeScreen
    │
    ├─→ DriverViewModel (State)
    │       │
    │       └─→ RideRepository
    │           │
    │           ├─→ FirebaseService (Listen for requests)
    │           │
    │           └─→ TrikRideMessagingService (Notifications)
    │
    └─→ RideRequestCard
        │
        └─→ Accept/Reject Actions
```

## Core Components

### 1. Models (Data Structures)

- `User`: Base user information
- `Driver`: Driver-specific data and verification
- `Passenger`: Passenger preferences
- `Ride`: Active ride information
- `RideRequest`: Incoming ride request
- `Location`: GPS coordinates and address
- `Document`: Driver verification documents
- `RideReview`: Rating and feedback

### 2. Services

#### FirebaseService
- Handles all Firebase Realtime Database operations
- Provides Flow-based reactive data streams
- Manages user, driver, and ride data persistence

#### TrikRideMessagingService
- Receives Firebase Cloud Messages
- Creates and displays notifications
- Handles message data routing

#### AuthenticationService (To Be Implemented)
- Manages user authentication
- Handles token refresh
- Manages session state

#### LocationService (To Be Implemented)
- Provides real-time GPS tracking
- Calculates distances and ETAs
- Integrates with Google Maps

### 3. Repositories (To Be Implemented)

Repository pattern for:
- UserRepository
- DriverRepository
- RideRepository
- ReviewRepository

Acts as data access abstraction layer.

### 4. ViewModels (To Be Implemented)

ViewModel classes for:
- AuthViewModel
- RideViewModel
- DriverViewModel
- HomeViewModel
- ReviewViewModel

Manages UI state and business logic.

### 5. UI Components

Jetpack Compose components organized by:
- **Screens**: Full-page composables
- **Components**: Reusable UI elements
- **Theme**: Design system (colors, typography)

## State Management

TrikRide uses Jetpack Compose for declarative UI with:
- `remember` for local state
- `mutableStateOf` for reactive state
- ViewModels for screen-level state
- Flow<T> for reactive data streams from Firebase

## Database Schema

### Users Collection

```
users/
└── {userId}
    ├── id: String
    ├── email: String
    ├── phoneNumber: String
    ├── firstName: String
    ├── lastName: String
    ├── userType: UserType
    ├── profileImageUrl: String
    ├── createdAt: String
    └── updatedAt: String
```

### Drivers Collection

```
drivers/
└── {driverId}
    ├── userId: String
    ├── licenseNumber: String
    ├── licenseExpiry: String
    ├── tricycleNumber: String
    ├── verificationStatus: VerificationStatus
    ├── isAvailable: Boolean
    ├── currentLocation: {latitude, longitude, address}
    ├── rating: Double
    ├── totalRides: Int
    ├── documents: Document[]
    └── verifiedAt: String
```

### Rides Collection

```
rides/
└── {rideId}
    ├── id: String
    ├── passengerId: String
    ├── driverId: String
    ├── pickupLocation: Location
    ├── dropoffLocation: Location
    ├── status: RideStatus
    ├── requestedAt: String
    ├── acceptedAt: String
    ├── startedAt: String
    ├── completedAt: String
    ├── estimatedDuration: Int
    ├── actualDuration: Int
    ├── estimatedFare: Double
    ├── actualFare: Double
    ├── paymentMethod: PaymentMethod
    ├── paymentStatus: PaymentStatus
    ├── route: Location[]
    └── notes: String
```

## Authentication Flow

```
User Input (Email/Password)
    │
    ├─→ AuthViewModel
    │       │
    │       └─→ FirebaseAuth.signInWithEmailAndPassword()
    │           │
    │           ├─→ Success: Save user data, navigate to home
    │           │
    │           └─→ Error: Display error message
    │
    └─→ Update AppState (isLoggedIn, userType)
```

## Real-time Location Tracking

```
Driver Location Updates
    │
    ├─→ LocationService.requestLocationUpdates()
    │       │
    │       └─→ FusedLocationProviderClient
    │
    └─→ FirebaseService.updateDriverLocation(driverId, location)
        │
        └─→ Ride subscribers notified via Flow
            │
            └─→ PassengerTrackingScreen updates map
```

## Error Handling

Centralized error handling with:
- Try-catch blocks in services
- Error states in ViewModels
- User-friendly error messages
- Retry mechanisms for network failures

## Performance Optimizations

1. **Database Indexing**: Optimize Firebase queries
2. **Image Caching**: Coil for efficient image loading
3. **ProGuard**: Code obfuscation for release builds
4. **Lazy Loading**: Load data on demand
5. **Flow Cancellation**: Proper cleanup in UI lifecycle

## Security Measures

1. **Firebase Security Rules**: Restrict database access
2. **Permissions**: Runtime permissions for location/camera
3. **Data Encryption**: Firebase handles SSL/TLS
4. **Token Management**: Secure token storage
5. **Input Validation**: Client-side validation

## Testing Strategy

### Unit Tests
- Service logic
- Repository operations
- ViewModel state management

### Integration Tests
- Firebase operations
- Database transactions
- API responses

### UI Tests
- Screen navigation
- User interactions
- State updates

## Build Configuration

- **minSdk**: 24 (Android 7.0)
- **targetSdk**: 34 (Latest)
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Build System**: Gradle Kotlin DSL

## Dependencies Management

Core dependencies:
- Jetpack: Core, Compose, Lifecycle, Navigation
- Firebase: Database, Auth, Storage, Messaging
- Google Play Services: Maps, Location
- Networking: Retrofit, OkHttp, Gson
- Image Loading: Coil

See `app/build.gradle.kts` for complete dependency list.

## Continuous Integration

Build checks:
- Gradle build
- Unit tests
- ProGuard rules validation
- Lint analysis

## Future Enhancements

- Push notification enhancements
- In-app messaging
- Payment gateway integration
- Advanced analytics
- Machine learning for ride matching
- Offline support with local caching
