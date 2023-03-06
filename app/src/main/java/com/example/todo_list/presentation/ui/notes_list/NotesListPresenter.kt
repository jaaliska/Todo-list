package com.example.todo_list.presentation.ui.notes_list

import com.example.todo_list.domain.model.Note
import com.example.todo_list.domain.usecases.GetAllNotesUseCase
import com.example.todo_list.domain.usecases.UpdateNoteCompleteStateUseCase
import com.example.todo_list.presentation.ui.base.BasePresenter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class NotesListPresenter @Inject constructor(
    private val getAllNotes: GetAllNotesUseCase,
    updateNoteCompleteState: UpdateNoteCompleteStateUseCase,
): BasePresenter<NotesListView>() {

    lateinit var listNotes: List<Note>


    override fun attachView(view: NotesListView?) {
        super.attachView(view)
        getAllNotes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                viewState.showErrorToast()
            }
            .subscribeByPresenter {
                listNotes = it
                setListNote()
            }

    }

    private fun setListNote() {
        if (listNotes.isEmpty()) {
            viewState.showEmptyScreen()
        } else {

        }
    }

    fun onNoteClicked(id: Int) {

    }

    fun onCheckboxClicked(id: Int, isChecked:Boolean) {

    }

    fun onButtonAddNoteClicked() {

    }

}