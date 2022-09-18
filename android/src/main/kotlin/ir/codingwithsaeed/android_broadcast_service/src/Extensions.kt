package ir.codingwithsaeed.android_broadcast_service.src

import android.content.Context
import android.util.Log
import android.widget.Toast


const val tag = "AndroidBroadcastService"

fun log(message: String) = Log.d(tag, message)

fun Context.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()