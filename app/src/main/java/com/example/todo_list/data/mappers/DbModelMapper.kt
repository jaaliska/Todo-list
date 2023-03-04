package com.example.todo_list.data.mappers

import com.example.todo_list.data.model.RoomNote
import com.example.todo_list.domain.model.Note

object DbModelMapper {

    fun map(note: Note): RoomNote = RoomNote(
        id = note.id,
        text = note.text,
        isCompleted = note.isCompleted,
        isReminderActive = note.isReminderActive
    )

    fun map(note: RoomNote): Note = Note(
        id = note.id ?: throw IllegalStateException("Note ID can't be null. Note text is ${note.text}"),
        text = note.text,
        isCompleted = note.isCompleted,
        isReminderActive = note.isReminderActive
    )

}