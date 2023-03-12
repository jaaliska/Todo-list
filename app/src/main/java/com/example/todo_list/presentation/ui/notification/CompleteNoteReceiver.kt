package com.example.todo_list.presentation.ui.notification

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.PersistableBundle
import com.example.todo_list.domain.usecases.UpdateNoteCompletionStateUseCase
import dagger.android.AndroidInjection
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CompleteNoteReceiver : BroadcastReceiver() {

    @Inject
    lateinit var updateNoteCompleteState: UpdateNoteCompletionStateUseCase

    override fun onReceive(context: Context, intent: Intent) {
        AndroidInjection.inject(this, context)
        val noteId = intent.getIntExtra(KEY_COMPLETE_NOTE_ID, DEFAULT_VALUE)
        if (noteId == DEFAULT_VALUE) {
            throw NoSuchElementException("${this.javaClass.name} haven't received Note ID")
        }

        val persistableBundle = PersistableBundle()
        persistableBundle.putInt(CompleteNoteJobService.KEY_COMPLETE_NOTE_ID, noteId)

        val serviceName = ComponentName(context, CompleteNoteJobService::class.java)
        val jobInfo = JobInfo.Builder(noteId, serviceName)
            .setExtras(persistableBundle)
            .setOverrideDeadline(TimeUnit.SECONDS.toMillis(5))
            .build()

        val scheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as (JobScheduler)
        scheduler.schedule(jobInfo)
    }

    companion object {
        const val KEY_COMPLETE_NOTE_ID = "CompleteNoteId"
        const val DEFAULT_VALUE = 0
    }
}