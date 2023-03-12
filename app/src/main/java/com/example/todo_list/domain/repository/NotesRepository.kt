package com.example.todo_list.domain.repository

import com.example.todo_list.domain.model.Note
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface NotesRepository {
    fun getById(id: Int): Single<Note>

    fun observeById(id: Int): TriggeredObservable<Note>
    fun observeAll(): TriggeredObservable<List<Note>>

    fun create(text: String, isReminderActive: Boolean): Completable
    fun update(
        id: Int,
        text: String? = null,
        isCompleted: Boolean? = null,
        isReminderActive : Boolean? = null
    ): Completable
    fun deleteNote(id: Int): Completable
}