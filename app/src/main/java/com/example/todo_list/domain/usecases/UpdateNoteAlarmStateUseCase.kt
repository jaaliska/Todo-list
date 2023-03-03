package com.example.todo_list.domain.usecases

import com.example.todo_list.domain.repository.NoteRepository
import io.reactivex.rxjava3.core.Completable

class UpdateNoteAlarmStateUseCase(private val repository: NoteRepository) {

    operator fun invoke(noteId: Int, hasReminder: Boolean): Completable {
        // TODO set alarm
        return repository.updateNote(noteId, hasReminder = hasReminder)
    }
}