package com.example.todo_list.presentation.ui.notes_list

import com.example.todo_list.domain.model.Note
import com.example.todo_list.presentation.ui.base.BaseMvpView
import moxy.viewstate.strategy.SingleStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(SingleStateStrategy::class)
interface NotesListView: BaseMvpView {

    fun showUncompletedNotes(list: List<Note>)
    fun showCompletedNotes(list: List<Note>)
    fun goEditNoteScreen(note: Note?)
    fun showEmptyScreen()
}