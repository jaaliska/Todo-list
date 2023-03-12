package com.example.todo_list.domain.usecases

import com.example.todo_list.domain.repository.NotesRepository
import com.example.todo_list.presentation.ui.notification.AlarmService
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class UpdateNoteReminderStateUseCase @Inject constructor(
    private val notesRepository: NotesRepository,
    private val alarmService: AlarmService
) {

    operator fun invoke(noteId: Int, isReminderActive: Boolean): Completable {
        if (isReminderActive) {
            alarmService.startAlarm(noteId)
        } else {
            alarmService.cancelAlarm(noteId)
        }
        return notesRepository.update(noteId, isReminderActive = isReminderActive)
    }
}