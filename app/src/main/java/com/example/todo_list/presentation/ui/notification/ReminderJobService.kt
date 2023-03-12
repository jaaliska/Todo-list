package com.example.todo_list.presentation.ui.notification

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import com.example.todo_list.app.TodoListApp
import com.example.todo_list.domain.usecases.GetNoteByIdUseCase
import com.example.todo_list.domain.usecases.UpdateNoteReminderStateUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class ReminderJobService : JobService() {

    @Inject
    lateinit var getNoteById: GetNoteByIdUseCase

    @Inject
    lateinit var updateNoteReminderState: UpdateNoteReminderStateUseCase
    lateinit var disposable: Disposable

    override fun onStartJob(params: JobParameters): Boolean {
        TodoListApp.appComponent.inject(this)
        val noteId = params.extras.getInt(KEY_REMINDER_NOTE_ID)

        disposable = getNoteById(noteId)
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                try {
                    val notificationHelper = NotificationHelper(baseContext, it)
                    notificationHelper.manager.notify(
                        noteId,
                        notificationHelper.buildChannelNotification()
                    )
                } catch (t: Throwable) {
                    Log.e(
                        this.javaClass.name,
                        "Problem with showing notification: ${t.message}"
                    )
                }
            }
            .flatMapCompletable {
                updateNoteReminderState(it.id, false)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                jobFinished(params, false)
            }
            .subscribe()
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    companion object {
        const val KEY_REMINDER_NOTE_ID = "ReminderNoteId"
    }
}
