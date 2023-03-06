package com.example.todo_list.app.di.factory

import com.example.todo_list.domain.model.Note
import com.example.todo_list.presentation.ui.note_editing.EditNotePresenter
import dagger.assisted.AssistedFactory

@AssistedFactory
interface EditNotePresenterFactory {
    fun create(originalNote: Note?): EditNotePresenter
}