package com.example.todo_list.presentation.ui.note_editing

import com.example.todo_list.R
import com.example.todo_list.domain.model.Note
import com.example.todo_list.domain.usecases.*
import com.example.todo_list.presentation.model.EditableNote
import com.example.todo_list.presentation.ui.base.BasePresenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import moxy.InjectViewState

@InjectViewState
class EditNotePresenter @AssistedInject constructor(
    @Assisted private val originalNote: Note?,
    private val createNote: CreateNoteUseCase,
    private val updateNoteText: UpdateNoteTextUseCase,
    private val deleteNote: DeleteNoteUseCase,
    private val updateNoteReminderState: UpdateNoteReminderStateUseCase,
    private val observeNote: ObserveNoteUseCase,
) : BasePresenter<EditNoteView>() {

    private val note: EditableNote = EditableNote(
        id = originalNote?.id,
        text = originalNote?.text ?: DEFAULT_TEXT,
        isCompleted = originalNote?.isCompleted ?: false,
        isReminderActive = originalNote?.isReminderActive ?: false
    )

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.setToolbar(
            isNewNote = originalNote == null,
            isReminderActive = note.isReminderActive,
            isDeletingAvailable = originalNote != null
        )
        viewState.setText(note.text)

        if (originalNote != null) {
            observeNote(originalNote.id).observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeByPresenter {
                    if (note.isReminderActive != it.isReminderActive) {
                        note.isReminderActive = it.isReminderActive
                        viewState.setReminderState(it.isReminderActive)
                    }
                }
        }
    }

    fun onTextChanged(text: String) {
        note.text = text
        viewState.setText(text)
    }

    fun onReminderClicked() {
        viewState.requestNotificationPermission()
    }

    fun onNotificationPermissionRequestResult(granted: Boolean) {
        if (granted) {
            note.isReminderActive = !note.isReminderActive
            viewState.setReminderState(note.isReminderActive)
        } else {
            viewState.showErrorToast(R.string.edit_note_need_permission)
        }
    }

    fun onDeleteButtonClicked() {
        if (originalNote != null) {
            deleteNote(originalNote.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showProgressDialog() }
                .doOnError {
                    viewState.showErrorToast()
                }
                .doFinally { viewState.hideProgressDialog() }
                .subscribeByPresenter {
                    viewState.goBack()
                }
        }
    }

    fun onBackButtonClicked() {
        if ((originalNote != null &&
            (note.text != originalNote.text || note.isReminderActive != originalNote.isReminderActive)) ||
            (originalNote == null && note.text != DEFAULT_TEXT)
        ) {
            viewState.showExitConfirmationDialog()
        } else {
            viewState.goBack()
        }
    }

    fun onExitWithoutSavingConfirmed(isConfirmed: Boolean) {
        if (isConfirmed) {
            viewState.goBack()
        } else {
            viewState.hideExitConfirmationDialog()
        }
    }

    fun onSaveButtonClicked() {
        Completable.merge(
            listOfNotNull(
                if (originalNote == null)
                    createNote(note.text, note.isReminderActive)
                else null,

                if (originalNote !== null && note.text != originalNote.text)
                    updateNoteText(originalNote.id, note.text)
                else null,

                if (originalNote !== null && note.isReminderActive != originalNote.isReminderActive)
                    updateNoteReminderState(originalNote.id, note.isReminderActive)
                else null
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { viewState.showProgressDialog() }
            .doOnError {
                viewState.showErrorToast()
            }
            .doFinally { viewState.hideProgressDialog() }
            .subscribeByPresenter {
                viewState.goBack()
            }
    }

    companion object {
        const val DEFAULT_TEXT = ""
    }
}
