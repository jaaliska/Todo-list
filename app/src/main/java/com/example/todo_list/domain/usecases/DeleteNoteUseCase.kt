package com.example.todo_list.domain.usecases

import com.example.todo_list.domain.repository.NotesRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val repository: NotesRepository
) {

    operator fun invoke(noteId: Int): Completable {
        return repository.deleteNote(noteId)
    }
}