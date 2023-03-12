package com.example.todo_list.domain.usecases

import com.example.todo_list.domain.model.Note
import com.example.todo_list.domain.repository.NotesRepository
import com.example.todo_list.domain.model.TriggeredObservable
import javax.inject.Inject

class ObserveNotesUseCase @Inject constructor(
    private val repository: NotesRepository
) {

    operator fun invoke(): TriggeredObservable<List<Note>> {
        return repository.observeAll()
    }
}