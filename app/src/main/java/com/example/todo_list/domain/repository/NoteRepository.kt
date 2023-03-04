package com.example.todo_list.domain.repository

import com.example.todo_list.domain.model.Note
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface NoteRepository {

    fun createNote(text: String, isReminderActive: Boolean): Single<Note>
    fun getAllNotes(): Single<List<Note>>
    fun updateNoteById(
        id: Int,
        text: String? = null,
        isCompleted: Boolean? = null,
        isReminderActive : Boolean? = null
    ): Completable
    fun deleteNote(noteId: Int): Completable
}