package com.example.todo_list.data.repository.text_data

import com.example.todo_list.data.model.RoomNote

object DatabaseTestData {

    val newNote1 = RoomNote(
        text = "to buy milk",
        isCompleted = false,
        isReminderActive = false
    )
    val newNote2 = RoomNote(
        text = "to cook dinner",
        isCompleted = false,
        isReminderActive = true
    )

    val newNote3 = RoomNote(
        text = "to pet a cat",
        isCompleted = false,
        isReminderActive = true
    )

    val createdNote1 = RoomNote(
        id = 1,
        text = "to buy milk",
        isCompleted = false,
        isReminderActive = false
    )
    val createdNote2 = RoomNote(
        id = 2,
        text = "to cook dinner",
        isCompleted = false,
        isReminderActive = true
    )

    val createdNote3 = RoomNote(
        id = 3,
        text = "to pet a cat",
        isCompleted = false,
        isReminderActive = true
    )

    val listCreatedNotes = listOf(createdNote1, createdNote2, createdNote3)


}