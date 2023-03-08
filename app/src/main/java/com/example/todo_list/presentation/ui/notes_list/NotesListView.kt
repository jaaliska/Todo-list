package com.example.todo_list.presentation.ui.notes_list

import com.example.todo_list.domain.model.Note
import com.example.todo_list.presentation.ui.base.BaseMvpView
import moxy.viewstate.strategy.SingleStateStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(SingleStateStrategy::class)
interface NotesListView: BaseMvpView {

    fun showUncompletedNotes(listItems: List<Item>)
    fun showCompletedNotes(listItems: List<Item>, isPanelOpen: Boolean)
    fun hideCompletedNotes()

    @StateStrategyType(SkipStrategy::class)
    fun goToEditNoteScreen(note: Note?)
    fun showEmptyScreen()

    data class Item(
        val id: Int,
        val text: String,
        val isChecked: Boolean
    )
}