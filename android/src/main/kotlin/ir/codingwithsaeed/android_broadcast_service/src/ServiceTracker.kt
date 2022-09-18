package ir.codingwithsaeed.android_broadcast_service.src

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences


object ServiceTracker {
    private const val name = "ANDROID_BROADCAST_SERVICE"
    private fun getPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(name, Context.MODE_PRIVATE)

    fun setBroadCastHandler(context: Context, handle: Long) {
        val pref = getPreferences(context)
        pref.edit().putLong("broadcast_handle", handle).apply()
    }

    fun getBroadCastHandler(context: Context): Long {
        val pref = getPreferences(context)
        return pref.getLong("broadcast_handle", 0)
    }

    private fun setServiceIsManuallyStopped(context: Context, isManuallyStopped: Boolean) {
        val pref = getPreferences(context)
        pref.edit().putBoolean("is_manually_stopped", isManuallyStopped).apply()
    }

    fun getServiceIsManuallyStopped(context: Context): Boolean {
        val pref = getPreferences(context)
        return pref.getBoolean("is_manually_stopped", false)
    }

    fun startService(context: Context) {
        if (isServiceRunning(context)) return
        setServiceIsManuallyStopped(context, false)
        Intent(context, BackgroundService::class.java).also {
            it.action = Actions.START.name
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                context.startForegroundService(it)

            } else
                context.startService(it)
        }
    }

    fun stopService(context: Context) {
        if (!isServiceRunning(context)) return
        setServiceIsManuallyStopped(context, true)
        Intent(context, BackgroundService::class.java).also {
            it.action = Actions.STOP.name
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                context.startForegroundService(it)

            } else
                context.startService(it)
        }
    }

    fun isServiceRunning(context: Context): Boolean {
        val manager: ActivityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (BackgroundService::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }
}



