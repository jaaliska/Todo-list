package com.example.todo_list.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")
data class RoomNote(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val text: String,
    val isCompleted: Boolean,
    val isReminderActive: Boolean
)