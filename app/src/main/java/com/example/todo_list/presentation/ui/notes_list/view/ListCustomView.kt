package com.example.todo_list.presentation.ui.notes_list.view

import android.view.View
import android.view.ViewGroup

abstract class ListCustomView<T> {

    abstract fun build(parent: ViewGroup): View
    abstract fun submitList(list: List<T>)
}