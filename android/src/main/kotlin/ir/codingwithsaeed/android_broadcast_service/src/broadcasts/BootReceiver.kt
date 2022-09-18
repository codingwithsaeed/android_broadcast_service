package ir.codingwithsaeed.android_broadcast_service.src.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ir.codingwithsaeed.android_broadcast_service.src.ServiceTracker

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED && !ServiceTracker.getServiceIsManuallyStopped(
                context
            )
        ) {
            ServiceTracker.startService(context)
        }
    }
}