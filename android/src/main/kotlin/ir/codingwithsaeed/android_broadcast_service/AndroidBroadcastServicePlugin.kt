package ir.codingwithsaeed.android_broadcast_service

import android.content.Context
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.*
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import ir.codingwithsaeed.android_broadcast_service.src.ServiceTracker
import ir.codingwithsaeed.android_broadcast_service.src.broadcasts.ServiceBroadcastReceiver
import org.json.JSONObject


class AndroidBroadcastServicePlugin : FlutterPlugin, MethodCallHandler {
    private lateinit var channel: MethodChannel
    private lateinit var eventChannel: EventChannel
    private lateinit var context: Context
    private lateinit var receiver: ServiceBroadcastReceiver

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        setupChannels(
            flutterPluginBinding.binaryMessenger,
            flutterPluginBinding.applicationContext
        )
    }

    private fun setupChannels(binaryMessenger: BinaryMessenger, context: Context) {
        this.context = context
        channel = MethodChannel(
            binaryMessenger,
            "android_broadcast_service",
            JSONMethodCodec.INSTANCE
        )
        eventChannel = EventChannel(
            binaryMessenger,
            "android_broadcast_service_event",
            JSONMethodCodec.INSTANCE
        )
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        val method = call.method
        val args = call.arguments as JSONObject

        when (method) {
            "configure" -> {
                val broadcastHandle = args.getLong("on_broadcast")
                configure(broadcastHandle)
                result.success(true)
            }
            "start" -> {
                start()
                result.success(true)
            }
            "stop" -> {
                ServiceTracker.stopService(context)
                result.success(true)
            }
            "isRunning" -> {
                result.success(ServiceTracker.isServiceRunning(context))
            }
            else -> result.notImplemented()
        }
    }

    private fun start() {
        ServiceTracker.startService(context)
        receiver = ServiceBroadcastReceiver.getInstance()
        eventChannel.setStreamHandler(receiver)
    }

    private fun configure(broadcastHandle: Long) {
        ServiceTracker.setBroadCastHandler(context, broadcastHandle)
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
        eventChannel.setStreamHandler(null)
    }
}