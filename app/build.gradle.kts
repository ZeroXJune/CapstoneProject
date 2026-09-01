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
if (buildingRelease && !hasReleaseKeystore) {
    logger.warn(
        "\n=====================================================================\n" +
            "  No release keystore configured. This release build is signed with\n" +
            "  the debug key and MUST NOT be distributed.\n" +
            "  Set RELEASE_STORE_FILE, RELEASE_STORE_PASSWORD, RELEASE_KEY_ALIAS\n" +
            "  and RELEASE_KEY_PASSWORD in .env — see .env.example.\n" +
            "====================================================================="
    )
}
