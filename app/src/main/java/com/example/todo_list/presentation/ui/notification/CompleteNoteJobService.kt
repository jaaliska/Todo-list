package com.example.todo_list.presentation.ui.notification

import android.app.NotificationManager
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.ContextWrapper
import android.widget.Toast
import com.example.todo_list.app.TodoListApp
import com.example.todo_list.domain.usecases.UpdateNoteCompletionStateUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class CompleteNoteJobService : JobService() {

    @Inject
    lateinit var updateNoteCompletionState: UpdateNoteCompletionStateUseCase
    lateinit var disposable: Disposable

    override fun onStartJob(params: JobParameters): Boolean {
        TodoListApp.appComponent.inject(this)

        val noteId = params.extras.getInt(KEY_COMPLETE_NOTE_ID, CompleteNoteReceiver.DEFAULT_VALUE)
        disposable =
            updateNoteCompletionState(noteId, true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    Toast.makeText(baseContext, "Note complete state wasn't updated", Toast.LENGTH_LONG)
                        .show()
                }
                .doFinally {
                    val manager = ContextWrapper(baseContext)
                        .getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                    manager.cancel(noteId)
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
        const val KEY_COMPLETE_NOTE_ID = "CompleteNoteId"
    }
}