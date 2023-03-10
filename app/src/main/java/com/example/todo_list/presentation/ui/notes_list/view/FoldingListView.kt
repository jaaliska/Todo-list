package com.example.todo_list.presentation.ui.notes_list.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_list.R
import com.example.todo_list.databinding.LayoutFoldingListBinding

class FoldingListView<I, H : RecyclerView.ViewHolder, A : ListAdapter<I, H>>(
    private val onFoldingPanelClicked: () -> Unit,
    private val adapter: A
) : ListCustomView<I> {

    private lateinit var binding: LayoutFoldingListBinding

    override fun build(parent: ViewGroup): View {
        binding =
            LayoutFoldingListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return binding.root.render()
    }

    override fun submitList(list: List<I>) {
        adapter.submitList(list)
    }

    override var isPanelOpen: Boolean
        get() = binding.itemsContainer.isVisible
        set(value) {
            if (value != binding.itemsContainer.isVisible) {
                updatePanelState(value)
            }
        }

    private fun View.render() = apply {
        binding.itemsContainer.adapter = adapter
        updatePanelState(false)
        setOnClickListener {
            onFoldingPanelClicked()
        }
    }

    private fun updatePanelState(isVisible: Boolean) {
        with(binding) {
            itemsContainer.isVisible = isVisible
            if (isVisible) {
                arrowImage.setImageResource(R.drawable.ic_arrow_down)
                foldingPanelText.setText(R.string.hide_notes)
            } else {
                arrowImage.setImageResource(R.drawable.ic_arrow_up)
                foldingPanelText.setText(R.string.open_notes)
            }
        }
    }
}