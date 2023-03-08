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
    private val updateNoteCompleteState: UpdateNoteCompleteStateUseCase,
) : BasePresenter<NotesListView>() {

    lateinit var notes: List<Note>
    private val uncompletedNotes = mutableListOf<NotesListView.Item>()
    private val completedNotes = mutableListOf<NotesListView.Item>()
    private var isFoldingPanelOpen = false


    override fun attachView(view: NotesListView?) {
        super.attachView(view)
        refreshView()
    }

    private fun refreshView() {
        getAllNotes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                viewState.showErrorToast()
            }
            .subscribeByPresenter {
                notes = it
                setNotesList()
            }
    }

    private fun setNotesList() {
        if (notes.isEmpty()) {
            viewState.showEmptyScreen()
        } else {
            completedNotes.clear()
            uncompletedNotes.clear()
            notes.map {
                val note = NotesListView.Item(
                    id = it.id,
                    text = it.text,
                    isChecked = it.isCompleted
                )
                if (note.isChecked) {
                    completedNotes.add(note)
                } else {
                    uncompletedNotes.add(note)
                }
            }
            viewState.showUncompletedNotes(uncompletedNotes.toList())
            if (completedNotes.isNotEmpty()) {
                viewState.showCompletedNotes(completedNotes.toList(), isFoldingPanelOpen)
            } else {
                viewState.hideCompletedNotes()
            }
        }
    }

    fun onNoteClicked(id: Int) {
        viewState.goToEditNoteScreen(notes.find {
            it.id == id
        })
    }

    fun onNoteCheckboxClicked(id: Int, isChecked: Boolean) {
        updateNoteCompleteState(id, isChecked)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                viewState.showErrorToast()
            }
            .subscribeByPresenter {
                refreshView()
            }
    }

    fun onFoldingPanelCLicked(isOpen: Boolean) {
        isFoldingPanelOpen = isOpen
        //TODO saving state to prefs
    }

    fun onButtonAddNoteClicked() {
        viewState.goToEditNoteScreen(null)
    }

}