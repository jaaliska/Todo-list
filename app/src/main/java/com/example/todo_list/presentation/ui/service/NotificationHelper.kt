package com.example.todo_list.presentation.ui.service


import android.annotation.TargetApi
import android.app.Notification
import android.app.Notification.*
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.core.app.NotificationCompat
import android.app.PendingIntent
import android.content.Intent
import com.example.todo_list.R
import com.example.todo_list.domain.model.Note
import com.example.todo_list.presentation.ui.MainActivity


class NotificationHelper(base: Context?, note: Note) : ContextWrapper(base) {

    private var mManager: NotificationManager? = null
    private val contentText = StringBuffer()
        .append(note.text.take(20))
        .append("...")


    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel =
            NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)
        manager!!.createNotificationChannel(channel)
    }

    val manager: NotificationManager?
        get() {
            if (mManager == null) {
                mManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            return mManager
        }

    fun buildChannelNotification(): Notification {
        val confirmIntent = Intent(applicationContext, ConfirmReceiver::class.java).apply {
            // action = ACTION_SNOOZE
            // putExtra(EXTRA_NOTIFICATION_ID, 0)
        }
        val openAppIntent = Intent(applicationContext, MainActivity::class.java)
        val openAppPendingIntent: PendingIntent =
            PendingIntent.getActivity(
                applicationContext,
                0,
                openAppIntent,
                PendingIntent.FLAG_IMMUTABLE
            )

        val confirmPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(
                applicationContext,
                0,
                confirmIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
        return NotificationCompat.Builder(applicationContext, channelID)
            .setSmallIcon(R.drawable.ic_bell)
            .setContentTitle(getString(R.string.notification_tittle))
            .setContentText(contentText)
            .setDefaults(DEFAULT_SOUND or DEFAULT_VIBRATE)
            .setCategory(CATEGORY_CALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(openAppPendingIntent)
            .addAction(
                R.drawable.ic_bell,
                getString(R.string.notification_complete),
                confirmPendingIntent
            )
            .build()
    }

    companion object {
        const val channelID = "1"
        const val channelName = "Notificationss"
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }
}