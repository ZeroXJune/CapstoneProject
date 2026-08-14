# R8 / ProGuard rules for TrikRide.
#
# Shrinking is currently switched off in app/build.gradle.kts. These rules are
# kept correct so that turning it on is a one-line change followed by a test,
# rather than a debugging session.
#
# The thing that breaks first under R8 is Firebase deserialization: a snapshot
# is mapped onto a data class by reflection, so the constructor, the fields and
# the getters all have to survive, and none of them look used from bytecode.

# --- Application models -----------------------------------------------------
# Firebase reads and writes these reflectively. Keeping the members matters as
# much as keeping the classes.
-keep class com.tpc.trikride.models.** { *; }
-keepclassmembers class com.tpc.trikride.models.** {
    <init>();
    <init>(...);
    <fields>;
    public *** get*();
    public void set*(***);
}

# Enums stored as strings and read back with valueOf().
-keepclassmembers enum com.tpc.trikride.models.** {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# --- Firebase ---------------------------------------------------------------
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**

# Firebase uses these annotations to decide what to serialize.
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault

# Anything a Firebase annotation touches has to stay put.
-keepclassmembers class * {
    @com.google.firebase.database.PropertyName *;
    @com.google.firebase.database.Exclude *;
    @com.google.firebase.database.IgnoreExtraProperties *;
}
-keep @com.google.firebase.database.IgnoreExtraProperties class * { *; }

# --- Kotlin -----------------------------------------------------------------
-keep class kotlin.Metadata { *; }
-keepclassmembers class **$WhenMappings { <fields>; }
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
-dontwarn kotlin.**
-dontwarn kotlinx.coroutines.**

# Coroutines internals that R8 cannot see are referenced.
-keepclassmembers class kotlinx.coroutines.** { volatile <fields>; }

# --- Compose ----------------------------------------------------------------
# Compose ships its own consumer rules; this only silences the debug tooling
# references that are absent from a release build.
-dontwarn androidx.compose.ui.tooling.**

# --- Google Maps ------------------------------------------------------------
-keep class com.google.android.gms.maps.** { *; }
-keep interface com.google.android.gms.maps.** { *; }

# --- Misc -------------------------------------------------------------------
-dontwarn javax.annotation.**
-dontwarn org.checkerframework.**
-dontwarn com.google.errorprone.annotations.**

# Keep line numbers so a crash report from a tester is readable, while still
# obfuscating names. Retrace the mapping file in app/build/outputs/mapping/.
-keepattributes SourceFile, LineNumberTable
-renamesourcefileattribute SourceFile
