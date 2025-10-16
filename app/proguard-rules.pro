# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep stylus API classes
-keep class com.tqmane.notesapp.stylus.** { *; }

# Keep model classes for JSON serialization
-keep class com.tqmane.notesapp.models.** { *; }

# Gson rules
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }

