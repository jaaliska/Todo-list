package com.example.todo_list.presentation.ui.note_editing

import com.example.todo_list.presentation.ui.base.BaseMvpView
import moxy.viewstate.strategy.*

@StateStrategyType(SingleStateStrategy ::class)
interface EditNoteView : BaseMvpView {

    fun setText(text: String)
    fun setReminderState(isReminderActive: Boolean)
    fun setToolbar(
        isNewNote: Boolean,
        isReminderActive: Boolean,
        isDeletingAvailable: Boolean
    )
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun requestNotificationPermission()
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun goBack()
}