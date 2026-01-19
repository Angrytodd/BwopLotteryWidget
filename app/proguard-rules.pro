# BWOP Lottery Widget ProGuard Rules

# Keep Glance classes
-keep class androidx.glance.** { *; }

# Keep lottery engine
-keep class com.bwop.lotterywidget.** { *; }

# Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
