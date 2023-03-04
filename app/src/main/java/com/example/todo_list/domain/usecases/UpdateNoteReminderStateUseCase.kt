package com.example.todo_list.domain.usecases

import com.example.todo_list.domain.repository.NoteRepository
import io.reactivex.rxjava3.core.Completable

class UpdateNoteReminderStateUseCase(private val repository: NoteRepository) {

    operator fun invoke(noteId: Int, isReminderActive: Boolean): Completable {
        // TODO set alarm
        return repository.updateNoteById(noteId, isReminderActive = isReminderActive)
    }
}