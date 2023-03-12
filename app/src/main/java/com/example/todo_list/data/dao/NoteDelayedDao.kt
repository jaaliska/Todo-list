package com.example.todo_list.data.dao

import com.example.todo_list.data.model.RoomNote
import java.util.concurrent.TimeUnit
import javax.inject.Inject

// This wrapper emulates a delay on a remote data source to demonstrate how this delay can
// be hidden from the user by caching data in the repository.
class NoteDelayedDao @Inject constructor(private val original: NoteDao): NoteDao {

    override fun save(value: RoomNote) = original.save(value)
        .delay(DELAY_MS, TimeUnit.MILLISECONDS)

    override fun delete(id: Int) = original.delete(id)
        .delay(DELAY_MS, TimeUnit.MILLISECONDS)

    override fun getNoteById(id: Int) = original.getNoteById(id)
        .delay(DELAY_MS, TimeUnit.MILLISECONDS)

    override fun getAll() = original.getAll()
        .delay(DELAY_MS, TimeUnit.MILLISECONDS)

    override fun updateText(id: Int, text: String) = original.updateText(id, text)
        .delay(DELAY_MS, TimeUnit.MILLISECONDS)

    override fun updateCompletionState(id: Int, isCompleted: Boolean) =
       original.updateCompletionState(id, isCompleted).delay(DELAY_MS, TimeUnit.MILLISECONDS)

    override fun updateReminderState(id: Int, isReminderActive: Boolean) =
        original.updateReminderState(id, isReminderActive).delay(DELAY_MS, TimeUnit.MILLISECONDS)

    companion object {
        const val DELAY_MS: Long = 1000
    }
}