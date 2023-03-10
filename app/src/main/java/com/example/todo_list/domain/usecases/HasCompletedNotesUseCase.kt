package com.example.todo_list.domain.usecases

import com.example.todo_list.domain.repository.NoteRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class HasCompletedNotesUseCase @Inject constructor(
    private val repository: NoteRepository
) {

    operator fun invoke(): Single<Boolean> {
        return repository.hasCompletedNotes()
    }
}