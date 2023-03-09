package com.example.todo_list.domain.usecases

import com.example.todo_list.domain.model.Note
import com.example.todo_list.domain.repository.NoteRepository
import io.reactivex.rxjava3.core.Single

class GetNoteByIdUseCase(
    private val repository: NoteRepository
) {

    operator fun invoke(noteId: Int) : Single<Note> {
        return repository.getNoteById(noteId)
    }
}