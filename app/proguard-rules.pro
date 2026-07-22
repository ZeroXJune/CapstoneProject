# Firebase
-keep class com.google.firebase.** { *; }
-keep class com.google.firebase.database.** { *; }
-keep class com.google.firebase.auth.** { *; }

# Kotlin
-keep class kotlin.** { *; }
-keep class kotlinx.** { *; }

# Models
-keep class com.tpc.trikride.models.** { *; }

# Serialization
-keepclassmembers class com.tpc.trikride.models.** {
    <init>(...);
}

# Gson
-keep class com.google.gson.** { *; }
-keep interface com.google.gson.** { *; }
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Google Maps
-keep class com.google.android.gms.maps.** { *; }
-keep interface com.google.android.gms.maps.** { *; }

# Don't warn about missing rules
-dontwarn javax.annotation.**
-dontwarn sun.misc.**
