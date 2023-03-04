package com.jaaliska.todo_list.repository

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.todo_list.app.TodoListDatabase
import com.example.todo_list.data.dao.NoteDao
import com.example.todo_list.data.model.RoomNote
import com.jaaliska.todo_list.repository.test_data.DatabaseTestData
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var noteDao: NoteDao
    private lateinit var db: TodoListDatabase
    private val testDate = DatabaseTestData

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, TodoListDatabase::class.java
        ).build()
        noteDao = db.noteDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun testSaveAndGetNotes() {
        noteDao.save(testDate.newNote1)
        noteDao.save(testDate.newNote2)
        noteDao.save(testDate.newNote3)
        noteDao.getAll().test()
            .assertValue {
                Log.i("MyCheck", it.toString())
                it == testDate.listCreatedNotes
            }
    }

    @Test
    fun testGetNoteById() {
        val expectedNote = testDate.createdNote1
        noteDao.save(testDate.newNote1)
        noteDao.save(testDate.newNote2)
        noteDao.save(testDate.newNote3)
        noteDao.getNoteById(expectedNote.id!!).test()
            .assertValue {
                Log.i("MyCheck", it.toString())
                it == expectedNote
            }
    }

    @Test
    fun testSaveNewDuplicatedNotes() {
        noteDao.save(testDate.newNote1)
        noteDao.save(testDate.newNote2)
        noteDao.save(testDate.newNote3)
        noteDao.save(testDate.createdNote2)
        noteDao.getAll().test()
            .assertValue {
                Log.i("MyCheck", it.toString())
                it == testDate.listCreatedNotes
            }
    }

    @Test
    fun testDeleteNote() {
        noteDao.save(testDate.newNote1)
        noteDao.save(testDate.newNote2)
        noteDao.save(testDate.newNote3)
        noteDao.delete(2).test().onComplete()
        val expectedList = listOf<RoomNote>(testDate.createdNote1, testDate.createdNote3)
        noteDao.getAll().test()
            .assertValue {
                Log.i("MyCheck", it.toString())
                it == expectedList
            }
    }

    @Test
    fun testDeleteFirstNote() {
        noteDao.save(testDate.newNote1)
        noteDao.save(testDate.newNote2)
        noteDao.save(testDate.newNote3)
        noteDao.delete(1).test().onComplete()
        val expectedList = listOf<RoomNote>(testDate.createdNote2, testDate.createdNote3)
        noteDao.getAll().test()
            .assertValue {
                Log.i("MyCheck", it.toString())
                it == expectedList
            }
    }

    @Test
    fun testUpdateNoteText() {
        val note = testDate.newNote1
        val updateText = "updated text"
        noteDao.save(note)
        noteDao.updateText(1, updateText).test().onComplete()

        val expectedNote = RoomNote(
            1,
            updateText,
            note.isCompleted,
            note.isReminderActive
        )
        noteDao.getAll().test()
            .assertValue {
                Log.i("MyCheck", it.toString())
                it[0] == expectedNote
            }
    }

    @Test
    fun testUpdateNoteCompleteState() {
        val note = testDate.newNote1
        val updateCompleteState = true
        noteDao.save(note)
        noteDao.updateCompleteState(1, updateCompleteState).test().onComplete()

        val expectedNote = RoomNote(
            1,
            note.text,
            updateCompleteState,
            note.isReminderActive
        )
        noteDao.getAll().test()
            .assertValue {
                Log.i("MyCheck", it.toString())
                it[0] == expectedNote
            }
    }

    @Test
    fun testUpdateNoteReminderState() {
        val note = testDate.newNote1
        val updateReminderState = true
        noteDao.save(note)
        noteDao.updateReminderState(1, updateReminderState).test().onComplete()

        val expectedNote = RoomNote(
            1,
            note.text,
            note.isCompleted,
            updateReminderState
        )
        noteDao.getAll().test()
            .assertValue {
                Log.i("MyCheck", it.toString())
                it[0] == expectedNote
            }
    }

}