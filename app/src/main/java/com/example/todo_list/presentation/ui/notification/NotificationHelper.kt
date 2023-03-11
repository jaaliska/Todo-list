package com.example.todo_list.presentation.ui.notification


import android.annotation.TargetApi
import android.app.*
import android.app.Notification.*
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.core.app.NotificationCompat
import android.content.Intent
import com.example.todo_list.R
import com.example.todo_list.domain.model.Note
import com.example.todo_list.presentation.ui.MainActivity
import com.example.todo_list.presentation.ui.notification.CompleteNoteReceiver.Companion.KEY_COMPLETE_NOTE_ID


class NotificationHelper(base: Context?, private val note: Note) : ContextWrapper(base) {

    val manager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        manager.createNotificationChannel(channel)
    }

    fun buildChannelNotification(): Notification {
        val openAppIntent = Intent(applicationContext, MainActivity::class.java)
        val openAppPendingIntent: PendingIntent =
            PendingIntent.getActivity(
                applicationContext,
                note.id,
                openAppIntent,
                PendingIntent.FLAG_IMMUTABLE
            )

        val confirmIntent = Intent(applicationContext, CompleteNoteReceiver::class.java)
            .putExtra(KEY_COMPLETE_NOTE_ID, note.id)

        val confirmPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(
                applicationContext,
                note.id,
                confirmIntent,
                PendingIntent.FLAG_IMMUTABLE
            )

        return NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_bell)
            .setContentTitle(getString(R.string.notification_tittle))
            .setContentText(validateText(note.text))
            .setDefaults(DEFAULT_SOUND or DEFAULT_VIBRATE)
            .setCategory(CATEGORY_CALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(openAppPendingIntent)
            .addAction(
                R.drawable.ic_bell,
                getString(R.string.notification_complete),
                confirmPendingIntent
            )
            .setAutoCancel(true)
            .build()
    }

    private fun validateText(text: String): String {
        return if (text.length > MAX_TEXT_LENGTH) {
            StringBuffer()
                .append(text.take(MAX_TEXT_LENGTH))
                .append("...")
                .toString()
        } else {
            text
        }
    }

    companion object {
        const val CHANNEL_ID = "1"
        const val CHANNEL_NAME = "Reminder"
        const val MAX_TEXT_LENGTH = 20
    }
}