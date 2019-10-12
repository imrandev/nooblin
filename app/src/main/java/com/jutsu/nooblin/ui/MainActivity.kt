package com.jutsu.nooblin.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.jutsu.nooblin.R
import com.jutsu.nooblin.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun getLayoutRes(): Int {
        return R.layout.activity_main
    }

    override fun onConnected(isOnline: Boolean) {
        runOnUiThread {
            tv_connectivity.text = String.format("%s", isOnline)
            sendNotification(
                if (isOnline) "Internet is connected!" else "Internet is disconnected!")
        }
    }

    // send push notification
    private fun sendNotification(message: String) {

        // Context objects are able to fetch or start system services.
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = 0
        val notificationChannel = "nooblin-channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(notificationChannel, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, notificationChannel)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(message)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        notificationManager.notify(notificationId, builder.build())
    }
}
