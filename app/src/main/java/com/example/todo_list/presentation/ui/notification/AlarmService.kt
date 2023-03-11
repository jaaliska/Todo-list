package com.example.todo_list.presentation.ui.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.todo_list.app.TodoListApp
import com.example.todo_list.presentation.ui.notification.ReminderReceiver.Companion.KEY_REMINDER_NOTE_ID
import java.util.*

class AlarmService {

    val context = TodoListApp.appComponent.context()

    fun startAlarm(noteId: Int) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis() + TEN_MINUTES_IN_MILLIS
        val alarmManager = ContextCompat.getSystemService(context, AlarmManager::class.java)
        alarmManager!!.setExact(AlarmManager.RTC, calendar.timeInMillis, createPendingIntent(noteId))
    }

    fun cancelAlarm(noteId: Int) {
        val alarmManager = ContextCompat.getSystemService(context, AlarmManager::class.java)
        alarmManager!!.cancel(createPendingIntent(noteId))
    }

    private fun createPendingIntent(noteId: Int): PendingIntent {
        val resultIntent =
            Intent(context, ReminderReceiver::class.java).putExtra(KEY_REMINDER_NOTE_ID, noteId)
        return PendingIntent.getBroadcast(context, noteId, resultIntent, PendingIntent.FLAG_IMMUTABLE)
    }

    companion object {
        private const val TEN_MINUTES_IN_MILLIS = 2000//10 * 60 * 1000
    }

}