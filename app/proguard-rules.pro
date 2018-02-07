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
-keep  class com.simpragma.recipe.recipeapplication1.DataBaseResult {*;}
-keep  class com.simpragma.recipe.recipeapplication1.DataAdapter {*;}
-keep  class com.simpragma.recipe.recipeapplication1.DataBaseAdapter {*;}
-keep  class com.simpragma.recipe.recipeapplication1.Recipie {*;}
-keep  class com.simpragma.recipe.recipeapplication1.Result {*;}

#-keepclassmembers class com.simpragma.recipe.recipeapplication1 {
#   public private *;
#}
-dontwarn okio.**

-ignorewarnings -keep class * { public private *; }
-keepclassmembers class mypackage.** { *; }

-dontwarn org.apache.commons.**
-dontwarn com.google.**
-dontwarn com.j256.ormlite**
-dontwarn org.apache.http**

-keepattributes SourceFile,LineNumberTable
-keep class com.j256.**
-keepclassmembers class com.j256.** { *; }
-keep enum com.j256.**
-keepclassmembers enum com.j256.** { *; }
-keep interface com.j256.**
-keepclassmembers interface com.j256.** { *; }


-assumenosideeffects class android.widget.Toast {
    public static *** makeText(...);
    public *** show();
}


-assumenosideeffects class android.util.Log {
   public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
   public static int e(...);
}
