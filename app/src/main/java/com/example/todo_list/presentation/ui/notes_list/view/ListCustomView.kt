package com.example.todo_list.presentation.ui.notes_list.view

import android.view.View
import android.view.ViewGroup

interface ListCustomView<T> {
    var isPanelOpen: Boolean

    fun build(parent: ViewGroup): View
    fun submitList(list: List<T>)
}