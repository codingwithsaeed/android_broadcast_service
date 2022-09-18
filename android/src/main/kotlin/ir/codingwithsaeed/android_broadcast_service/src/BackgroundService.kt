package ir.codingwithsaeed.android_broadcast_service.src

import android.annotation.SuppressLint
import android.app.*
import android.app.Notification.Builder
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.os.SystemClock
import ir.codingwithsaeed.android_broadcast_service.src.broadcasts.ServiceBroadcastReceiver

class BackgroundService : Service() {
    private var wakeLock: PowerManager.WakeLock? = null
    private var serviceReceiver: ServiceBroadcastReceiver? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()

        val notification = createNotification("Android Broadcast Service", "Service is running")
        startForeground(1, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            when (intent.action) {
                Actions.START.name -> startService()
                Actions.STOP.name -> stopService()
                else -> startService()
            }
        } else {
            if (!ServiceTracker.getServiceIsManuallyStopped(this)) {
                startService()
            } else {
                stopService()
            }
        }
        return START_STICKY
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onTaskRemoved(rootIntent: Intent?) {
        val restartServiceIntent = Intent(applicationContext, BackgroundService::class.java).also {
            it.setPackage(packageName)
            it.action = Actions.START.name
        };
        val restartServicePendingIntent: PendingIntent =
            PendingIntent.getService(this, 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        applicationContext.getSystemService(Context.ALARM_SERVICE);
        val alarmService: AlarmManager =
            applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager;
        alarmService.set(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime() + 500,
            restartServicePendingIntent
        );
        this.toast("onTaskRemoved Called")
    }

    override fun onDestroy() {
        this.toast("onDestroy Called")
        if (!ServiceTracker.getServiceIsManuallyStopped(this)) {
            ServiceTracker.startService(this)
            return
        }

        if (serviceReceiver != null) {
            unregisterReceiver(serviceReceiver)
        }
        super.onDestroy()
    }

    private fun startService() {

        wakeLock =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "EndlessService::lock").apply {
                    acquire(10 * 60 * 1000L /*10 minutes*/)
                }
            }

        serviceReceiver = ServiceBroadcastReceiver.getInstance()
        registerReceiver(serviceReceiver, IntentFilter(Intent.ACTION_TIME_TICK))
        registerReceiver(serviceReceiver, IntentFilter(Intent.ACTION_TIMEZONE_CHANGED))
    }

    private fun stopService() {
        try {
            wakeLock?.let {
                if (it.isHeld) {
                    it.release()
                }
            }
            stopForeground(true)
            stopSelf()
        } catch (e: Exception) {
            log("Service stopped without being started: ${e.message}")
        }

        if (serviceReceiver != null) {
            unregisterReceiver(serviceReceiver)
        }
    }

    private fun createNotification(
        title: String,
        desc: String,
    ): Notification {
        val notificationChannelId = "1122"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                notificationChannelId,
                "Service notifications channel",
                NotificationManager.IMPORTANCE_LOW
            ).let {
                it.description = "Service channel"
                it
            }
            notificationManager.createNotificationChannel(channel)
        }

        val builder: Builder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Builder(
                this,
                notificationChannelId
            ) else Builder(this)

        return builder
            .setContentTitle(title)
            .setContentText(desc)
            .setOngoing(true)
            .build()
    }
}