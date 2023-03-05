package com.example.todo_list.domain.usecases

import com.example.todo_list.domain.repository.NoteRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class UpdateNoteCompleteStateUseCase @Inject constructor(
    private val repository: NoteRepository
) {

    operator fun invoke(noteId: Int, isCompleted: Boolean): Completable {
        return repository.updateNoteById(id = noteId, isCompleted = isCompleted)

    }
}