package com.example.todo_list.presentation.ui.note_editing

import moxy.MvpView
import moxy.viewstate.strategy.SingleStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(SingleStateStrategy::class)
interface EditNoteView: MvpView {

    fun setText(text: String)
    fun setReminderState(isReminderActive: Boolean)
    fun showDeleteIcon()
    fun goBack()
}