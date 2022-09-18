package ir.codingwithsaeed.android_broadcast_service.src.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import io.flutter.plugin.common.EventChannel
import ir.codingwithsaeed.android_broadcast_service.src.ServiceTracker
import ir.codingwithsaeed.android_broadcast_service.src.broadcasts.Broadcasts.Companion.TIME_TICK
import ir.codingwithsaeed.android_broadcast_service.src.broadcasts.Broadcasts.Companion.TIME_ZONE_CHANGED
import ir.codingwithsaeed.android_broadcast_service.src.toast

class ServiceBroadcastReceiver : BroadcastReceiver(), EventChannel.StreamHandler {
    companion object {
        private var instance: ServiceBroadcastReceiver? = null
        fun getInstance(): ServiceBroadcastReceiver {
            if (instance == null) {
                instance = ServiceBroadcastReceiver()
            }
            return instance!!
        }
    }

    private var events: EventChannel.EventSink? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null && intent == null) return
        val action = intent?.action
        context?.toast("Broadcast Received: $action")
        val handler = ServiceTracker.getBroadCastHandler(context!!)
        if (handler == 0L) return
        when (action) {
            Intent.ACTION_TIME_TICK -> {
                events?.success(Gson().toJson(BroadcastData(TIME_TICK, handler)))
            }
            Intent.ACTION_TIMEZONE_CHANGED -> {
                events?.success(
                    Gson().toJson(
                        BroadcastData(
                            TIME_ZONE_CHANGED, handler
                        )
                    )
                )

            }
        }

    }

    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        this.events = events
    }

    override fun onCancel(arguments: Any?) {
        this.events = null
    }
}