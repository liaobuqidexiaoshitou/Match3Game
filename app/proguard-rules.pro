# Add project specific ProGuard rules here.
-keep class com.example.match3game.** { *; }
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.preference.Preference
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# AdMob
-keepattributes SourceFile,LineNumberTable
-keepattributes InnerClasses
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

# Google Ads
-keep class com.google.android.gms.ads.** { *; }
-dontwarn com.google.android.gms.ads.**
