package com.example.todo_list.data.repository

import com.example.todo_list.data.dao.NoteDao
import com.example.todo_list.data.mappers.DbModelMapper
import com.example.todo_list.data.model.RoomNote
import com.example.todo_list.domain.model.Note
import com.example.todo_list.domain.repository.NoteRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val db: NoteDao
) : NoteRepository {

    private val mapper = DbModelMapper

    override fun createNote(text: String, isReminderActive: Boolean): Single<Note> {
        return db.save(
                RoomNote(
                    text = text,
                    isCompleted = false,
                    isReminderActive = isReminderActive
                )
            ).flatMap {
            db.getNoteById(it.toInt())
                .map {
                    mapper.map(it)
                }
        }
    }

    override fun getNoteById(id: Int): Single<Note> {
        return db.getNoteById(id).map { mapper.map(it) }
    }

    override fun getAllNotes(): Single<List<Note>> {
        return db.getAll().map { list ->
            list.map {
                mapper.map(it)
            }
        }
    }

    override fun getAllByCompletionStatus(completed: Boolean): Single<List<Note>> {
        return db.getAllByCompletionStatus(completed).map { list ->
            list.map {
                mapper.map(it)
            }
        }.delay(700, TimeUnit.MILLISECONDS) // emulate long load
    }

    override fun hasCompletedNotes(): Single<Boolean> {
        return db.hasCompletedNotes().delay(300, TimeUnit.MILLISECONDS) // emulate long load
    }

    override fun updateNoteById(
        id: Int,
        text: String?,
        isCompleted: Boolean?,
        isReminderActive: Boolean?
    ): Completable {
        val listActions = mutableListOf<Completable>()
        if (text != null) {
            listActions.add(
                db.updateText(id, text)
            )
        }
        if (isCompleted != null) {
            listActions.add(
                db.updateCompleteState(id, isCompleted)
            )
        }
        if (isReminderActive != null) {
            listActions.add(
                db.updateReminderState(id, isReminderActive)
            )
        }

        return Completable.merge(listActions)
    }

    override fun deleteNote(noteId: Int): Completable {
        return db.delete(noteId)
    }
}