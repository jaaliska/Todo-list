package com.example.todo_list.presentation.ui.note_editing

import com.example.todo_list.presentation.ui.base.BaseMvpView
import moxy.viewstate.strategy.AddToEndSingleTagStrategy
import moxy.viewstate.strategy.StateStrategyType
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

    @StateStrategyType(value = AddToEndSingleTagStrategy::class, tag = "show_hide_exit_confirmation_dialog")
    fun showExitConfirmationDialog()

    @StateStrategyType(value = AddToEndSingleTagStrategy::class, tag = "show_hide_exit_confirmation_dialog")
    fun hideExitConfirmationDialog()

    @OneExecution
    fun requestNotificationPermission()
    @OneExecution
    fun goBack()
}