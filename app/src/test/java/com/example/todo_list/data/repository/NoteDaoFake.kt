package com.example.todo_list.data.repository

import com.example.todo_list.data.dao.NoteDao
import com.example.todo_list.data.model.RoomNote
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.lang.Exception
import java.util.TreeMap
import java.util.concurrent.TimeUnit

open class NoteDaoFake(private val delayMs: Long): NoteDao {
    val notes = TreeMap<Int, RoomNote>()
    private var lastId = 0L

    fun saveDirect(value: RoomNote): Long {
        lastId++
        notes[lastId.toInt()] = RoomNote(
            id = lastId.toInt(),
            text = value.text,
            isCompleted = value.isCompleted,
            isReminderActive = value.isReminderActive
        )
        return lastId
    }

    override fun save(value: RoomNote): Single<Long> {
        return Single.just(saveDirect(value)).delay(delayMs, TimeUnit.MILLISECONDS)
    }

    override fun delete(id: Int): Completable {
        notes.remove(id)
        return Completable.complete().delay(delayMs, TimeUnit.MILLISECONDS)
    }

    override fun getNoteById(id: Int): Single<RoomNote> {
        val note = notes[id] ?: return Single.error(Exception("not found"))
        return Single.just(note).delay(delayMs, TimeUnit.MILLISECONDS)
    }

    override fun getAll(): Single<List<RoomNote>> {
        return Single.just(notes.values.toList()).delay(delayMs, TimeUnit.MILLISECONDS)
    }

    override fun updateText(id: Int, text: String): Completable {
        val note = notes[id] ?: return Completable.error(Exception("not found"))
        notes[id] = RoomNote(
            id = id,
            text = text,
            isCompleted = note.isCompleted,
            isReminderActive = note.isReminderActive
        )
        return Completable.complete().delay(delayMs, TimeUnit.MILLISECONDS)
    }

    override fun updateCompletionState(id: Int, isCompleted: Boolean): Completable {
        val note = notes[id] ?: return Completable.error(Exception("not found"))
        notes[id] = RoomNote(
            id = id,
            text = note.text,
            isCompleted = isCompleted,
            isReminderActive = note.isReminderActive
        )
        return Completable.complete().delay(delayMs, TimeUnit.MILLISECONDS)
    }

    override fun updateReminderState(id: Int, isReminderActive: Boolean): Completable {
        val note = notes[id] ?: return Completable.error(Exception("not found"))
        notes[id] = RoomNote(
            id = id,
            text = note.text,
            isCompleted = note.isCompleted,
            isReminderActive = isReminderActive
        )
        return Completable.complete().delay(delayMs, TimeUnit.MILLISECONDS)
    }
}