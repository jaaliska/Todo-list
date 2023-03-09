package com.example.todo_list.presentation.ui.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.todo_list.app.TodoListApp
import java.util.*

class AlarmService {

    val context = TodoListApp.appComponent.context()

    fun startAlarm(noteId: Int) {
        cancelAlarm(noteId)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis() + TEN_MINUTES_IN_MILLIS
        val alarmManager = ContextCompat.getSystemService(context, AlarmManager::class.java)
        alarmManager!!.setExact(AlarmManager.RTC, calendar.timeInMillis, createPendingIntent(noteId))
        Log.i("MyCheck", "startAlarm ${calendar.time}")
    }

    fun cancelAlarm(noteId: Int) {
        // todo alarm manager cancel intent matching putextra extra data
        val alarmManager = ContextCompat.getSystemService(context, AlarmManager::class.java)
        alarmManager!!.cancel(createPendingIntent(noteId))
    }

    private fun createPendingIntent(noteId: Int): PendingIntent {
        val resultIntent =
            Intent(context, AlertReceiver::class.java).putExtra("NotificationNoteId", noteId)
        return PendingIntent.getBroadcast(context, 1, resultIntent, PendingIntent.FLAG_IMMUTABLE)
    }

    companion object {
        private const val TEN_MINUTES_IN_MILLIS = 2000//10 * 60 * 1000
    }

}