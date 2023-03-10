package com.example.todo_list.presentation.ui.notes_list

import com.example.todo_list.domain.model.Note

object NoteItemMapper {

    fun map(note: Note) = NotesListView.Item(
        id = note.id,
        text = note.text,
        isChecked = note.isCompleted,
    )
}