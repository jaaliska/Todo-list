package com.example.todo_list.domain.usecases

import com.example.todo_list.domain.repository.NoteRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class UpdateNoteTextUseCase @Inject constructor(
    private val repository: NoteRepository
) {

    operator fun invoke(updatedId: Int, text: String): Completable {
        return repository.updateNoteById(updatedId, text)
    }

}