package com.example.todo_list.data.repository

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.todo_list.app.TodoListDatabase
import com.example.todo_list.data.dao.NoteDao
import com.example.todo_list.data.model.RoomNote
import com.example.todo_list.data.repository.text_data.DatabaseTestData
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var noteDao: NoteDao
    private lateinit var db: TodoListDatabase
    private val testData = DatabaseTestData

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
        noteDao.save(testData.newNote1).test()
            .assertValue {
                it == 1L
            }
        noteDao.save(testData.newNote2).test()
            .assertValue {
                it == 2L
            }
        noteDao.save(testData.newNote3).test()
            .assertValue {
                it == 3L
            }

        noteDao.getAll().test()
            .assertValue {
                Log.i("MyCheck", it.toString())
                it == testData.listCreatedNotes
            }
    }

    @Test
    fun testGetNoteById() {
        val expectedNote = testData.createdNote1
        noteDao.save(testData.newNote1).test().onComplete()
        noteDao.save(testData.newNote2).test().onComplete()
        noteDao.save(testData.newNote3).test().onComplete()
        noteDao.getNoteById(expectedNote.id!!).test()
            .assertValue {
                Log.i("MyCheck", it.toString())
                it == expectedNote
            }
    }

    @Test
    fun testSaveNewDuplicatedNotes() {
        noteDao.save(testData.newNote1).test().onComplete()
        noteDao.save(testData.newNote2).test().onComplete()
        noteDao.save(testData.newNote3).test().onComplete()
        noteDao.save(testData.createdNote2).test().onComplete()
        noteDao.getAll().test()
            .assertValue {
                Log.i("MyCheck", it.toString())
                it == testData.listCreatedNotes
            }
    }

    @Test
    fun testDeleteNote() {
        noteDao.save(testData.newNote1).test().onComplete()
        noteDao.save(testData.newNote2).test().onComplete()
        noteDao.save(testData.newNote3).test().onComplete()
        noteDao.delete(2).test().onComplete()
        val expectedList = listOf<RoomNote>(testData.createdNote1, testData.createdNote3)
        noteDao.getAll().test()
            .assertValue {
                Log.i("MyCheck", it.toString())
                it == expectedList
            }
    }

    @Test
    fun testDeleteFirstNote() {
        noteDao.save(testData.newNote1).test().onComplete()
        noteDao.save(testData.newNote2).test().onComplete()
        noteDao.save(testData.newNote3).test().onComplete()
        noteDao.delete(1).test().onComplete()
        val expectedList = listOf<RoomNote>(testData.createdNote2, testData.createdNote3)
        noteDao.getAll().test()
            .assertValue {
                Log.i("MyCheck", it.toString())
                it == expectedList
            }
    }

    @Test
    fun testUpdateNoteText() {
        val note = testData.newNote1
        val updateText = "updated text"
        noteDao.save(note).test().onComplete()
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
        val note = testData.newNote1
        val updateCompleteState = true
        noteDao.save(note).test().onComplete()
        noteDao.updateCompletionState(1, updateCompleteState).test().onComplete()

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
        val note = testData.newNote1
        val updateReminderState = true
        noteDao.save(note).test().onComplete()
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