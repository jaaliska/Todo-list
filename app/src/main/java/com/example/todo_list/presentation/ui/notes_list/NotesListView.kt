package com.example.todo_list.presentation.ui.notes_list

import com.example.todo_list.domain.model.Note
import com.example.todo_list.presentation.ui.base.BaseMvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import moxy.viewstate.strategy.alias.AddToEndSingle

interface NotesListView: BaseMvpView {
    @AddToEndSingle
    fun setCompletedNotesPanelState(isOpen: Boolean)

    @AddToEndSingle
    fun setNotes(
        uncompleted: List<Item>,
        completed: List<Item>,
        isThereCompletedNotes: Boolean
    )

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun goToEditNoteScreen(note: Note?)

    data class Item(
        val id: Int,
        val text: String,
        val isChecked: Boolean
    )
}