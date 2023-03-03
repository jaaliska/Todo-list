package com.example.todo_list.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(
    val id: Int,
    val text: String,
    val isCompleted: Boolean,
    val isReminderActive : Boolean
): Parcelable
