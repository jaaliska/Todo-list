package com.example.todo_list.presentation.model

data class EditableNote(
    val id: Int?,
    var text: String,
    var isCompleted: Boolean,
    var isReminderActive : Boolean
)
