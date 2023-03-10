package com.example.todo_list.presentation.ui.notes_list

import com.example.todo_list.data.repository.UserPreferencesRepository
import com.example.todo_list.domain.usecases.GetNoteByIdUseCase
import com.example.todo_list.domain.usecases.GetNotesUseCase
import com.example.todo_list.domain.usecases.HasCompletedNotesUseCase
import com.example.todo_list.domain.usecases.UpdateNoteCompleteStateUseCase
import com.example.todo_list.presentation.ui.base.BasePresenter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class NotesListPresenter @Inject constructor(
    private val getNoteById: GetNoteByIdUseCase,
    private val getNotes: GetNotesUseCase,
    private val hasCompletedNotes: HasCompletedNotesUseCase,
    private val updateNoteCompleteState: UpdateNoteCompleteStateUseCase,
    private val userPreferences: UserPreferencesRepository
) : BasePresenter<NotesListView>() {
    private var isCompletedNotesPanelOpen = userPreferences.isShowCompletedNotes()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        updateNotes()
        viewState.setCompletedNotesPanelState(isCompletedNotesPanelOpen)
    }

    private fun updateNotes() {
        updateUncompletedNotes()
        updateCompletedNotes()
    }

    private fun updateCompletedNotes() {
        viewState.showProgressDialog()
        if (isCompletedNotesPanelOpen) {
            getNotes(true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    viewState.showErrorToast()
                }
                .doFinally(viewState::hideProgressDialog)
                .subscribeByPresenter {
                    viewState.setCompletedNotes(it.map(NoteItemMapper::map), it.isNotEmpty())
                }
        } else {
            hasCompletedNotes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    viewState.showErrorToast()
                }
                .doFinally(viewState::hideProgressDialog)
                .subscribeByPresenter {
                    viewState.setCompletedNotes(listOf(), it)
                }
        }
    }

    private fun updateUncompletedNotes() {
        viewState.showProgressDialog()
        getNotes(false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                viewState.showErrorToast()
            }
            .doFinally(viewState::hideProgressDialog)
            .subscribeByPresenter {
                viewState.setUncompletedNotes(it.map(NoteItemMapper::map))
            }
    }

    fun onNoteCheckboxClicked(id: Int, isChecked: Boolean) {
        updateNoteCompleteState(id, isChecked)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                viewState.showErrorToast()
            }
            .subscribeByPresenter {
                updateNotes()
            }
    }

    fun onCompletedNotesPanelClicked() {
        isCompletedNotesPanelOpen = !isCompletedNotesPanelOpen
        userPreferences.setShowCompletedNotes(isCompletedNotesPanelOpen)
        viewState.setCompletedNotesPanelState(isCompletedNotesPanelOpen)
        if (isCompletedNotesPanelOpen) {
            updateCompletedNotes()
        } else {
            viewState.setCompletedNotes(listOf(), true)
        }
    }

    fun onNoteClicked(id: Int) {
        getNoteById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                viewState.showErrorToast()
            }
            .subscribeByPresenter {
                viewState.goToEditNoteScreen(it)
            }
    }

    fun onButtonAddNoteClicked() {
        viewState.goToEditNoteScreen(null)
    }
}