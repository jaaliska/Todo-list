package com.example.todo_list.presentation.ui.notes_list.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_list.R
import com.example.todo_list.databinding.ItemNoteBinding
import com.example.todo_list.presentation.ui.notes_list.NotesListView.Item

class NotesListAdapter(
    private val onItemClick: (item: Item) -> Unit,
    private val onCheckboxClick: (item: Item, isCheck: Boolean) -> Unit,
) : ListAdapter<Item, NotesListAdapter.NotesListViewHolder>(
    diffCallback
) {
    class NotesListViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: Item,
            onItemClick: (item: Item) -> Unit,
            onCheckboxClick: (item: Item, isCheck: Boolean) -> Unit
        ) {
            binding.text.apply {
                text = item.text
                if (item.isChecked) {
                    paintFlags = binding.text.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
                setTextAppearance(
                    if (item.isChecked) R.style.Core_Style_Text_Disabled
                    else R.style.Core_Style_Text_Emphasis
                )
                setOnClickListener { onItemClick(item) }
            }
            binding.checkbox.apply {
                isChecked = item.isChecked
                setOnClickListener {
                    isChecked = !isChecked
                    onCheckboxClick(item, !item.isChecked)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesListViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotesListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: NotesListViewHolder, position: Int) {
        holder.bind(currentList[position], onItemClick, onCheckboxClick)
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem == newItem
            }
        }
    }
}

