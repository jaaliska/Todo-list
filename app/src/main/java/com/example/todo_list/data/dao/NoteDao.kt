package com.example.todo_list.data.dao

import androidx.room.*
import com.example.todo_list.data.model.RoomNote
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(value: RoomNote): Single<Long>

    @Query("DELETE FROM note WHERE id = :id")
    fun delete(id: Int): Completable

    @Query("SELECT * FROM note WHERE id = :id")
    fun getNoteById(id: Int): Single<RoomNote>

    @Query("SELECT * FROM note")
    fun getAll(): Single<List<RoomNote>>

    @Query("UPDATE note SET text = :text WHERE id = :id")
    fun updateText(id: Int, text: String): Completable

    @Query("UPDATE note SET isCompleted = :isCompleted WHERE id = :id")
    fun updateCompleteState(id: Int, isCompleted: Boolean): Completable

    @Query("UPDATE note SET isReminderActive = :isReminderActive WHERE id = :id")
    fun updateReminderState(id: Int, isReminderActive: Boolean): Completable

}