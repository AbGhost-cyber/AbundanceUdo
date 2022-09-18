# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# for Google Play Service
-keep public class com.google.android.gms.ads.**{
 public *;
}
# for Old ads classes
-keep public class com.google.ads.**{
 public *;
}

# for Mediation
-keepattributes *Annotation*

# Other clases required by Google for play services
-keep class * extends java.util.ListResourceBundle{
  protected java.lang.Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable{
public static final *** NULL;
}
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class *{
 @com.google.android.gms.common.annotation.KeepName *;
}
-keepnames class * implements android.os.Parcelable{
public static final ** CREATOR;
}
#FCM and Firebase
-keep class com.google.android.gms.** { *; }
-keep class com.google.firebase.** { *; }

#NAV
-keepnames class * extends java.io.Serializable
-keepnames class androidx.navigation.fragment.NavHostFragment
-keep public class * extends java.lang.Exception  # Optional: Keep custom exceptions.

# Keep class names of Hilt injected ViewModels since their name are used as a multibinding map key.
-keepnames @dagger.hilt.android.lifecycle.HiltViewModel class * extends androidx.lifecycle.ViewModel