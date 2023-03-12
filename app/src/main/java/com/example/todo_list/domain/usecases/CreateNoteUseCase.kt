package com.example.todo_list.domain.usecases

import com.example.todo_list.domain.repository.NotesRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class CreateNoteUseCase @Inject constructor(
    private val repository: NotesRepository
) {

    operator fun invoke(text: String, isReminderActive: Boolean): Completable {
        return repository.create(text, isReminderActive)
    }
}