package com.example.todo_list.presentation.ui.main

import com.example.todo_list.domain.usecases.GetAllNotesUseCase
import com.example.todo_list.domain.usecases.UpdateNoteCompleteStateUseCase
import com.example.todo_list.presentation.ui.BasePresenter
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class NoteListPresenter @Inject constructor(
    getAllNotes: GetAllNotesUseCase,
    updateNoteCompleteState: UpdateNoteCompleteStateUseCase,
): BasePresenter<NoteListView>() {


    fun onNoteClicked(id: Int) {

    }

    fun onCheckboxClicked(id: Int, isChecked:Boolean) {

    }

    fun onButtonAddNoteClicked() {

    }

}