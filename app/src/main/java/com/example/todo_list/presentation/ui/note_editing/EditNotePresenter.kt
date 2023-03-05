package com.example.todo_list.presentation.ui.note_editing

import android.util.Log
import com.example.todo_list.domain.usecases.CreateNoteUseCase
import com.example.todo_list.domain.usecases.DeleteNoteUseCase
import com.example.todo_list.domain.usecases.UpdateNoteReminderStateUseCase
import com.example.todo_list.domain.usecases.UpdateNoteTextUseCase
import com.example.todo_list.presentation.ui.base.BasePresenter
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class EditNotePresenter @Inject constructor(
    createNote: CreateNoteUseCase,
    updateNoteText: UpdateNoteTextUseCase,
    updateNoteReminderState: UpdateNoteReminderStateUseCase,
    deleteNote: DeleteNoteUseCase,
) : BasePresenter<EditNoteView>() {

    init {


    }

    fun onTextChanged(text: String) {

    }

    fun onReminderClicked(isReminderActive: Boolean) {

    }

    fun onDeleteButtonClicked() {

    }

    fun onSaveButtonClicked() {

    }

    private fun logResult(result: Any) {
        Log.i("MyCheck", result.toString())
    }
}