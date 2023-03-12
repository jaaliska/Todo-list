package com.example.todo_list.presentation.ui.notes_list

import com.example.todo_list.data.repository.UserPreferencesRepository
import com.example.todo_list.domain.repository.NotesRepository
import com.example.todo_list.presentation.ui.base.BasePresenter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class NotesListPresenter @Inject constructor(
    private val userPreferences: UserPreferencesRepository,
    private val notesRepository: NotesRepository
) : BasePresenter<NotesListView>() {

    private var isCompletedNotesPanelOpen = BehaviorSubject.createDefault(
        userPreferences.isShowCompletedNotes()
    )
    private val notesObserver = notesRepository.observeAll()

    private val completedNotes = notesObserver.observable.map {
            list -> list.filter { it.isCompleted }
    }
    private val uncompletedNotes = notesObserver.observable.map {
            list -> list.filter { !it.isCompleted }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        isCompletedNotesPanelOpen.subscribeByPresenter {
            userPreferences.setShowCompletedNotes(it)
            viewState.setCompletedNotesPanelState(it)
        }

        uncompletedNotes
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeByPresenter {
                viewState.setUncompletedNotes(it.map(NoteItemMapper::map))
            }

        Observable.combineLatest(isCompletedNotesPanelOpen, completedNotes) { isOpen, notes ->
            Pair(
                if (isOpen) notes.map(NoteItemMapper::map) else listOf(),
                notes.isNotEmpty()
            )}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeByPresenter {
                viewState.setCompletedNotes(it.first, it.second)
            }

        refreshNotes(false)
    }

    fun onNoteCheckboxClicked(id: Int, isChecked: Boolean) {
        notesRepository.update(id, isCompleted = isChecked)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeByPresenter({
                viewState.showErrorToast()
            })
    }

    fun onCompletedNotesPanelClicked() {
        isCompletedNotesPanelOpen.onNext(!isCompletedNotesPanelOpen.value!!)
    }

    fun onNoteClicked(id: Int) {
        notesRepository.getById(id)
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

    private fun refreshNotes(forceRefresh: Boolean) {
        viewState.showProgressDialog()
        notesObserver.startLoading(forceRefresh)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally(viewState::hideProgressDialog)
            .subscribeByPresenter({
                viewState.showErrorToast()
            })
    }
}