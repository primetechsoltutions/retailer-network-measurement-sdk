# --- WorkManager core rules ---
# Keep all ListenableWorker subclasses (base for Worker, CoroutineWorker, etc.)
-keep class androidx.work.ListenableWorker { *; }
-keep class androidx.work.Worker { *; }
-keep class androidx.work.CoroutineWorker { *; }

# Keep your custom worker class (adjust package as needed)
-keep class com.ptsl.network_sdk.** extends androidx.work.ListenableWorker { *; }

# Keep generated Hilt worker factories
-keepclassmembers class ** {
    @dagger.hilt.android.HiltAndroidApp *;
}

# --- Hilt / Dagger rules ---
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-dontwarn dagger.hilt.**
-dontwarn javax.inject.**

# Keep startup provider used by WorkManager and Hilt
-keep class androidx.startup.InitializationProvider { *; }
-keep class androidx.startup.InitializationProvider

# Avoid warnings
-dontwarn androidx.work.**



#----------------------------- uu---------------------------

#####################################
# ✅ GENERAL KEEP CONFIGURATIONS
#####################################

# Keep annotations and signatures
-keepattributes Annotation, Signature,
    RuntimeVisibleAnnotations, RuntimeInvisibleAnnotations,
    RuntimeVisibleParameterAnnotations, RuntimeInvisibleParameterAnnotations,
    AnnotationDefault, EnclosingMethod, InnerClasses

# Keep Kotlin Metadata for reflection
-keep class kotlin.Metadata { *; }

# Kotlin coroutines
-keepclassmembers class kotlin.coroutines.Continuation {
    <fields>;
    <methods>;
}
-keep class kotlin.coroutines.Continuation { *; }
-keep class kotlinx.coroutines.** { *; }
-keep class kotlinx.coroutines.internal.** { *; }
-dontwarn kotlin.coroutines.**
-dontwarn kotlinx.coroutines.flow.**

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*
-dontwarn kotlin.Unit

#####################################
# ✅ GSON (DATA MODELS)
#####################################

# Keep Gson model classes and fields
-keep class com.ptsl.network_sdk.data_model.data_model.** { *; }
-keep class com.ptsl.network_sdk.data_model.BaseResponse { *; }

# Allow Gson to access @SerializedName fields via reflection
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

#####################################
# ✅ NETWORK SERVICE CORE
#####################################

-keep class com.ptsl.network_sdk.** { *; }
-keep class com.ptsl.network_sdk.NetworkDataUploader {
    public *;
}

#-keep class com.ptsl.rso.network_service.data_model.** { *; }
#-keep class com.ptsl.rso.network_service.NetworkDataUploader {
#    public *;
#}

#####################################
# ✅ RETROFIT & OKHTTP
#####################################

# Retrofit core
-keep interface retrofit2.* { *; }
-keep class retrofit2.** { *; }

# Retrofit response and converter
-keep class retrofit2.Call
-keep class retrofit2.Callback
-keep class retrofit2.Converter$Factory
-keep class retrofit2.converter.gson.** { *; }

# Retrofit reflection on generic parameters
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# Keep inherited services
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>

# Retrofit response (e.g., Response<T>)
-keep,allowobfuscation,allowshrinking class retrofit2.Response

# Retrofit method & parameter annotations
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# OkHttp and okio
-keep class okhttp3.** { *; }
-keep class okio.** { *; }

# Disable OkHttp debug logging interceptor
-dontwarn okhttp3.logging.**
-keep class okhttp3.logging.HttpLoggingInterceptor { *; }
-assumenosideeffects class okhttp3.logging.HttpLoggingInterceptor {
    public <init>(...);
    public *** intercept(...);
}

#####################################
# ✅ ROOM DATABASE
#####################################

-keep class androidx.room.** { *; }

-keepclassmembers class * {
    @androidx.room.* <methods>;
    @androidx.room.* <fields>;
}

-keep class * extends androidx.room.RoomDatabase { *; }
-keep class * implements androidx.room.RoomDatabase { *; }

# Keep Room DAO interfaces
-keep interface * {
    @androidx.room.Dao <methods>;
}

# For coroutines/Flow in Room
-dontwarn kotlin.coroutines.**

#####################################
# ✅ DAGGER / HILT
#####################################

-keepattributes Annotation

# Hilt core
-keep class dagger.hilt.** { *; }
-keep class dagger.hilt.internal.** { *; }
-keep class dagger.hilt.android.internal.** { *; }

# Dagger core
-keep class dagger.** { *; }
-keep interface dagger.** { *; }
-keep class dagger.internal.** { *; }

# javax.inject
-keep class javax.inject.** { *; }

# Keep classes with dagger/hilt annotations
-keep class * {
    @dagger.hilt.InstallIn <methods>;
    @dagger.Module <methods>;
    @dagger.hilt.EntryPoint <methods>;
    @dagger.Binds <methods>;
    @dagger.Provides <methods>;
    @javax.inject.Inject <fields>;
    @dagger.hilt.android.HiltAndroidApp <methods>;
    @dagger.hilt.android.AndroidEntryPoint <methods>;
}

# Required to prevent stripping of injected constructors, fields, and methods
-keepclasseswithmembers class * {
    @javax.inject.Inject <init>(...);
}
-keepclasseswithmembers class * {
    @javax.inject.Inject *;
}

#####################################
# ✅ PERMISSIONX
#####################################

-keep class com.guolindev.permissionx.** { *; }

#####################################
# ✅ OPTIONAL IGNORE/DON’T WARN RULES
#####################################

# BouncyCastle
-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider

# Conscrypt
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier

# OpenJSSE
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE

# Animal Sniffer plugin (used in Retrofit builds)
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# JSR 305 nullability annotations
-dontwarn javax.annotation.**


# Kotlin Parcelize
-keep class kotlinx.parcelize.Parcelize
-keep @kotlinx.parcelize.Parcelize class * implements android.os.Parcelable {
    <fields>;
    <methods>;
}

#----------------------------- uu---------------------------


# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE
-dontwarn org.bouncycastle.**
-dontwarn org.conscrypt.**
-dontwarn org.openjsse.**