package com.example.todo_list.presentation.ui.note_editing

import com.example.todo_list.domain.model.Note
import com.example.todo_list.domain.usecases.CreateNoteUseCase
import com.example.todo_list.domain.usecases.DeleteNoteUseCase
import com.example.todo_list.domain.usecases.UpdateNoteReminderStateUseCase
import com.example.todo_list.domain.usecases.UpdateNoteTextUseCase
import com.example.todo_list.presentation.model.EditableNote
import com.example.todo_list.presentation.ui.base.BasePresenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import moxy.InjectViewState

@InjectViewState
class EditNotePresenter @AssistedInject constructor(
    @Assisted private val originalNote: Note?,
    private val createNote: CreateNoteUseCase,
    private val updateNoteText: UpdateNoteTextUseCase,
    private val deleteNote: DeleteNoteUseCase,
    private val updateNoteReminderState: UpdateNoteReminderStateUseCase,
) : BasePresenter<EditNoteView>() {

    private val note: EditableNote = EditableNote(
        id = originalNote?.id,
        text = originalNote?.text ?: "",
        isCompleted = originalNote?.isCompleted ?: false,
        isReminderActive = originalNote?.isReminderActive ?: false
    )

    override fun attachView(view: EditNoteView?) {
        super.attachView(view)
        if (originalNote == null) {
            viewState.setToolbar(
                isNewNote = true,
                isReminderActive = note.isReminderActive,
                isDeletingAvailable = false
            )
        } else {
            viewState.setToolbar(
                isNewNote = false,
                isReminderActive = note.isReminderActive,
                isDeletingAvailable = true
            )
            viewState.setText(note.text)
        }
    }

    fun onNotificationPermissionRequestResult(granted: Boolean) {
        if (granted) {
            note.isReminderActive = !note.isReminderActive
            viewState.setReminderState(note.isReminderActive)
            updateNoteReminderState(
                note.id!!, //TODO
                note.isReminderActive
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    viewState.showErrorToast()
                }
                .subscribeByPresenter()
        }
    }

    fun onTextChanged(text: String) {
        note.text = text
        viewState.setText(text)
    }

    fun onReminderClicked() {
        viewState.requestNotificationPermission()
    }

    fun onDeleteButtonClicked() {
        deleteNote(originalNote!!.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                viewState.showErrorToast()
            }
            .subscribeByPresenter {
                viewState.goBack()
            }
    }

    fun onSaveButtonClicked() {
        if (originalNote == null) {
            createNote(note.text, note.isReminderActive)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    viewState.showErrorToast()
                }
                .subscribeByPresenter {
                    viewState.goBack()
                }
        } else {
            if (note.text != originalNote.text) {
                updateNoteText(originalNote.id, note.text)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError {
                        viewState.showErrorToast()
                    }
                    .subscribeByPresenter {
                        viewState.goBack()
                    }
            }
        }
    }
}
