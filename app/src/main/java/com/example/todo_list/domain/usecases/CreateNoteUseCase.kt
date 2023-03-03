package com.example.todo_list.domain.usecases

import com.example.todo_list.domain.model.Note
import com.example.todo_list.domain.repository.NoteRepository
import io.reactivex.rxjava3.core.Single

class CreateNoteUseCase(private val repository: NoteRepository) {

    operator fun invoke(text: String, hasReminder: Boolean): Single<Note> {
        return repository.createNote(text, hasReminder)
    }
}