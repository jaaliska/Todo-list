package com.example.todo_list.domain.repository

import com.example.todo_list.domain.model.Note
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface NoteRepository {

    fun createNote(text: String, hasRemind: Boolean): Single<Note>
    fun getAllNotes(): Single<List<Note>>
    fun updateNote(
        updatedId: Int,
        text: String? = null,
        isCompleted: Boolean? = null,
        hasReminder : Boolean? = null
    ): Completable
    fun deleteNote(noteId: Int): Completable
}