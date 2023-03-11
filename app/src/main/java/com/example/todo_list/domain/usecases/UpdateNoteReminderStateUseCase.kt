package com.example.todo_list.domain.usecases

import com.example.todo_list.domain.repository.NoteRepository
import com.example.todo_list.presentation.ui.notification.AlarmService
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class UpdateNoteReminderStateUseCase @Inject constructor(
    private val repository: NoteRepository
) {

    operator fun invoke(noteId: Int, isReminderActive: Boolean): Completable {

        if (isReminderActive) {
            AlarmService().startAlarm(noteId)
        } else {
            AlarmService().cancelAlarm(noteId)
        }
        return repository.updateNoteById(noteId, isReminderActive = isReminderActive)
    }
}