package com.example.todo_list.domain.usecases

import com.example.todo_list.domain.repository.NotesRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class UpdateNoteCompleteStateUseCase @Inject constructor(
    private val repository: NotesRepository
) {

    operator fun invoke(noteId: Int, isCompleted: Boolean): Completable {
        return repository.update(id = noteId, isCompleted = isCompleted)

    }
}