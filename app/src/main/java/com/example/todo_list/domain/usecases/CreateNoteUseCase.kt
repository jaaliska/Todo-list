package com.example.todo_list.domain.usecases

import com.example.todo_list.domain.model.Note
import com.example.todo_list.domain.repository.NoteRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class CreateNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {

    operator fun invoke(text: String, isReminderActive: Boolean): Single<Note> {
        return repository.createNote(text, isReminderActive)
    }
}