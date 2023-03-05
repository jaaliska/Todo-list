package com.example.todo_list.presentation.ui.main

import com.example.todo_list.domain.model.Note
import moxy.MvpView
import moxy.viewstate.strategy.SingleStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(SingleStateStrategy::class)
interface NoteListView: MvpView {

    fun showUncompletedNotes(list: List<Note>)
    fun showCompletedNotes(list: List<Note>)
    fun goEditNoteScreen(note: Note?)
}