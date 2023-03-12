package com.example.todo_list.presentation.ui.notes_list

import com.example.todo_list.domain.repository.PreferencesRepository
import com.example.todo_list.domain.usecases.GetNoteByIdUseCase
import com.example.todo_list.domain.usecases.ObserveNotesUseCase
import com.example.todo_list.domain.usecases.UpdateNoteCompletionStateUseCase
import com.example.todo_list.presentation.ui.base.BasePresenter
import com.example.todo_list.presentation.ui.notes_list.mapper.NoteItemMapper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class NotesListPresenter @Inject constructor(
    private val userPreferences: PreferencesRepository,
    observeNotes: ObserveNotesUseCase,
    private val getNoteById: GetNoteByIdUseCase,
    private val updateNoteCompletionState: UpdateNoteCompletionStateUseCase,
) : BasePresenter<NotesListView>() {

    private var isCompletedNotesPanelOpen = BehaviorSubject.createDefault(
        userPreferences.isShowCompletedNotes()
    )
    private val notesObserver = observeNotes()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        isCompletedNotesPanelOpen.subscribeByPresenter {
            viewState.setCompletedNotesPanelState(it)
        }

        Observable.combineLatest(isCompletedNotesPanelOpen, notesObserver.observable) { isOpen, notes ->
            val uncompleted = notes.filter { !it.isCompleted }.map(NoteItemMapper::map)

            val completed = if (isOpen)
                notes.filter { it.isCompleted }.map(NoteItemMapper::map)
            else
                listOf()

            val hasCompleted = if (isOpen)
                completed.isNotEmpty()
            else
                notes.any { it.isCompleted }

            Triple(uncompleted, completed, hasCompleted)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeByPresenter {
                viewState.setNotes(it.first, it.second, it.third)
            }

        refreshNotes(false)
    }

    fun onNoteCheckboxClicked(id: Int, isChecked: Boolean) {
        updateNoteCompletionState(id, isCompleted = isChecked)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeByPresenter({
                viewState.showErrorToast()
            })
    }

    fun onCompletedNotesPanelClicked() {
        val nextState = !isCompletedNotesPanelOpen.value!!
        userPreferences.setShowCompletedNotes(nextState)
        isCompletedNotesPanelOpen.onNext(nextState)
    }

    fun onNoteClicked(id: Int) {
        getNoteById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeByPresenter({
                viewState.showErrorToast()
            }, {
                viewState.goToEditNoteScreen(it)
            })
    }

    fun onButtonAddNoteClicked() {
        viewState.goToEditNoteScreen(null)
    }

    @Suppress("SameParameterValue")
    // For future swipe-refresh widget
    private fun refreshNotes(forceRefresh: Boolean) {
        notesObserver.startLoading(forceRefresh)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { viewState.showProgressDialog() }
            .doFinally { viewState.hideProgressDialog() }
            .subscribeByPresenter({
                viewState.showErrorToast()
            })
    }
}