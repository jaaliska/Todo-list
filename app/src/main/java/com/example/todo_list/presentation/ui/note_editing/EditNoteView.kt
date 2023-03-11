package com.example.todo_list.presentation.ui.note_editing

import com.example.todo_list.presentation.ui.base.BaseMvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

interface EditNoteView : BaseMvpView {
    @AddToEndSingle
    fun setText(text: String)

    @AddToEndSingle
    fun setReminderState(isReminderActive: Boolean)

    @AddToEndSingle
    fun setToolbar(
        isNewNote: Boolean,
        isReminderActive: Boolean,
        isDeletingAvailable: Boolean
    )

    @AddToEndSingle
    fun setExitConfirmationDialogState(isShowing: Boolean)

    @OneExecution
    fun requestNotificationPermission()
    @OneExecution
    fun goBack()
}