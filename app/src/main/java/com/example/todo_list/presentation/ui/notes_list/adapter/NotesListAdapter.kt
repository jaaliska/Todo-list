package com.example.todo_list.presentation.ui.notes_list.adapter

import android.text.Html
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
            itemView.apply {
                if (item.isChecked) {
                    val strikeText = Html.fromHtml("<s>" + item.text + "</s>")
                    binding.text.text = strikeText
                    binding.text.setTextAppearance(R.style.Core_Style_Text_Disabled)
                } else {
                    binding.text.text = item.text
                    binding.text.setTextAppearance(R.style.Core_Style_Text_Emphasis)
                }
                binding.text.setOnClickListener {
                    onItemClick(item)
                }
                binding.checkbox.isChecked = item.isChecked
                binding.checkbox.setOnClickListener {
                    onCheckboxClick(item, !item.isChecked)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesListViewHolder {
        val binding =
            ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotesListViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: NotesListViewHolder, position: Int) {
        holder.bind(currentList[position], onItemClick, onCheckboxClick)
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Item>() {

            override fun areItemsTheSame(
                oldItem: Item,
                newItem: Item
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Item,
                newItem: Item
            ): Boolean {
                return oldItem.id == newItem.id &&
                        oldItem.text == newItem.text &&
                        oldItem.isChecked == newItem.isChecked
            }
        }
    }
}

