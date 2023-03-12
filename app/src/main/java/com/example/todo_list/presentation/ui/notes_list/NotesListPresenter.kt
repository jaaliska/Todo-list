package com.example.todo_list.presentation.ui.notes_list

import com.example.todo_list.domain.repository.PreferencesRepository
import com.example.todo_list.domain.usecases.GetNoteByIdUseCase
import com.example.todo_list.domain.usecases.ObserveNotesUseCase
import com.example.todo_list.domain.usecases.UpdateNoteCompleteStateUseCase
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
    private val updateNoteCompleteState: UpdateNoteCompleteStateUseCase,
) : BasePresenter<NotesListView>() {

    private var isCompletedNotesPanelOpen = BehaviorSubject.createDefault(
        userPreferences.isShowCompletedNotes()
    )
    private val notesObserver = observeNotes()

    private val completedNotes = notesObserver.observable.map { list ->
        list.filter { it.isCompleted }
    }
    private val uncompletedNotes = notesObserver.observable.map { list ->
        list.filter { !it.isCompleted }
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
            )
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeByPresenter {
                viewState.setCompletedNotes(it.first, it.second)
            }

        refreshNotes(false)
    }

    fun onNoteCheckboxClicked(id: Int, isChecked: Boolean) {
        updateNoteCompleteState(id, isCompleted = isChecked)
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