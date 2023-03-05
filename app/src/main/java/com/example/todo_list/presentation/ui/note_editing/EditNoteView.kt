package com.example.todo_list.presentation.ui.note_editing

import com.example.todo_list.presentation.ui.base.BaseMvpView
import moxy.viewstate.strategy.SingleStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(SingleStateStrategy::class)
interface EditNoteView: BaseMvpView {

    fun setText(text: String)
    fun setReminderState(isReminderActive: Boolean)
    fun showDeleteIcon()
    fun goBack()
}