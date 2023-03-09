package com.example.todo_list.presentation.ui.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.todo_list.domain.model.Note
import com.example.todo_list.domain.usecases.GetNoteByIdUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject


class AlertReceiver : BroadcastReceiver() {

    @Inject
    lateinit var getNoteById: GetNoteByIdUseCase
    lateinit var note: Note

    override fun onReceive(context: Context, intent: Intent) {
        val noteId = intent.getIntExtra("NotificationNoteId", DEFAULT_VALUE)
        if (noteId == DEFAULT_VALUE) throw NoSuchElementException("AlertReceiver haven't received Note id")

        val disposable = getNoteById(noteId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                Log.i("MyCheck", "AlertReceiver haven't received Note")
            }
            .doOnSuccess {
                note = it
            }
            .subscribe()

        val notificationHelper = NotificationHelper(context, note)
        notificationHelper.manager?.notify(1, notificationHelper.buildChannelNotification())
        disposable.dispose()
    }

    companion object {
        const val DEFAULT_VALUE = 0
    }

}
