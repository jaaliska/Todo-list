package com.example.todo_list.domain.usecases

import com.example.todo_list.domain.model.Note
import com.example.todo_list.domain.repository.NotesRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetNoteByIdUseCase @Inject constructor(
    private val repository: NotesRepository
) {

    operator fun invoke(noteId: Int) : Single<Note> {
        return repository.getById(noteId)
    }
}