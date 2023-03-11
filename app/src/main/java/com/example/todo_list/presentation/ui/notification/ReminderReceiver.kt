package com.example.todo_list.presentation.ui.notification

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.PersistableBundle
import com.example.todo_list.domain.usecases.GetNoteByIdUseCase
import com.example.todo_list.domain.usecases.UpdateNoteReminderStateUseCase
import dagger.android.AndroidInjection
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ReminderReceiver : BroadcastReceiver() {

    @Inject
    lateinit var getNoteById: GetNoteByIdUseCase

    @Inject
    lateinit var updateNoteReminderState: UpdateNoteReminderStateUseCase

    override fun onReceive(context: Context, intent: Intent) {
        AndroidInjection.inject(this, context)
        val noteId = intent.getIntExtra(KEY_REMINDER_NOTE_ID, DEFAULT_VALUE)
        if (noteId == DEFAULT_VALUE) {
            throw NoSuchElementException("${this.javaClass.name} haven't received Note ID")
        }
        val persistableBundle = PersistableBundle()
        persistableBundle.putInt(ReminderJobService.KEY_REMINDER_NOTE_ID, noteId)

        val serviceName = ComponentName(context, ReminderJobService::class.java)
        val jobInfo = JobInfo.Builder(noteId, serviceName)
            .setExtras(persistableBundle)
            .setOverrideDeadline(TimeUnit.SECONDS.toMillis(5))
            .build()

        val scheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as (JobScheduler)
        scheduler.schedule(jobInfo)
    }

    companion object {
        const val KEY_REMINDER_NOTE_ID = "ReminderNoteId"
        const val DEFAULT_VALUE = 0
    }
}
