package com.example.todo_list.domain.usecases

import com.example.todo_list.domain.model.Note
import com.example.todo_list.domain.model.TriggeredObservable
import com.example.todo_list.domain.repository.NotesRepository
import javax.inject.Inject

class ObserveNoteUseCase @Inject constructor(
    private val repository: NotesRepository
) {

    operator fun invoke(noteId: Int): TriggeredObservable<Note> {
        return repository.observeById(noteId)
    }
}