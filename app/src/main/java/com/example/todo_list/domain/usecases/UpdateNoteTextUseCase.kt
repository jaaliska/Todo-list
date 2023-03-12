package com.example.todo_list.domain.usecases

import com.example.todo_list.domain.repository.NotesRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class UpdateNoteTextUseCase @Inject constructor(
    private val repository: NotesRepository
) {

    operator fun invoke(updatedId: Int, text: String): Completable {
        return repository.update(updatedId, text)
    }

}