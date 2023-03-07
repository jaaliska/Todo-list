package com.example.todo_list.presentation.ui.notes_list

import androidx.recyclerview.widget.DiffUtil
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
    @StateStrategyType(SkipStrategy ::class)
    fun goToEditNoteScreen(note: Note?)
    fun showEmptyScreen()

    data class Item(
        val id: Int,
        val text: String,
        val isChecked: Boolean
    ) {
        companion object {
            val diffCallback = object : DiffUtil.ItemCallback<Item>() {
                override fun areItemsTheSame(
                    oldItem: Item,
                    newItem: Item
                ): Boolean {
                    return oldItem.id == newItem.id &&
                            oldItem.text == newItem.text &&
                            oldItem.isChecked == newItem.isChecked
                }

                override fun areContentsTheSame(
                    oldItem: Item,
                    newItem: Item
                ): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }
}