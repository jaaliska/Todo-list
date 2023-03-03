package com.example.todo_list.domain.usecases

import com.example.todo_list.domain.repository.NoteRepository
import io.reactivex.rxjava3.core.Completable

class DeleteNoteUseCase(private val repository: NoteRepository) {

    operator fun invoke(noteId: Int): Completable {
        return repository.deleteNote(noteId)
    }
}