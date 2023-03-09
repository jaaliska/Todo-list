package com.example.todo_list.presentation.ui.notes_list.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_list.R
import com.example.todo_list.databinding.LayoutFoldingListBinding

class FoldingListCustomView<T, E : RecyclerView.ViewHolder, V : ListAdapter<T, E>>(
    isPanelOpenInitially: Boolean,
    private val onStateChanging: (Boolean) -> Unit,
    private val adapter: V
) : ListCustomView<T>() {

    private lateinit var binding: LayoutFoldingListBinding
    private var isPanelOpen = isPanelOpenInitially

    override fun build(parent: ViewGroup): View {
        binding =
            LayoutFoldingListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        Log.i("MyCheck", "build: $isPanelOpen")
        return binding.root
            .render()
            .setupState()
    }

    override fun submitList(list: List<T>) {
        adapter.submitList(list)
    }

    private fun View.render() = apply {
        binding.itemsContainer.layoutManager = LinearLayoutManager(context)
        binding.itemsContainer.adapter = adapter

        setOnClickListener {
            Log.i("MyCheck", "old: $isPanelOpen")
            isPanelOpen = !isPanelOpen
            onStateChanging(isPanelOpen)
            setupState()
            Log.i("MyCheck", "new: $isPanelOpen")
        }
    }

    private fun View.setupState() = apply {
        with(binding) {
            Log.i("MyCheck", "setup: $isPanelOpen")
            if (isPanelOpen) {
                arrowImage.setImageResource(R.drawable.ic_arrow_down)
                foldingPanelText.setText(R.string.hide_notes)
                itemsContainer.isVisible = true
            } else {
                arrowImage.setImageResource(R.drawable.ic_arrow_up)
                foldingPanelText.setText(R.string.open_notes)
                itemsContainer.isVisible = false
            }
        }
    }
}