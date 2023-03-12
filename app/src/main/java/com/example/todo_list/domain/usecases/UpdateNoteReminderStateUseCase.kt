package com.example.todo_list.domain.usecases

import com.example.todo_list.domain.repository.NotesRepository
import com.example.todo_list.presentation.ui.notification.AlarmService
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class UpdateNoteReminderStateUseCase @Inject constructor(
    private val repository: NotesRepository
) {

    operator fun invoke(noteId: Int, isReminderActive: Boolean): Completable {

        if (isReminderActive) {
            AlarmService().startAlarm(noteId)
        } else {
            AlarmService().cancelAlarm(noteId)
        }
        return repository.update(noteId, isReminderActive = isReminderActive)
    }
}