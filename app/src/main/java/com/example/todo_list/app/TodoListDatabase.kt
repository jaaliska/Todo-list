package com.example.todo_list.app

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todo_list.app.TodoListDatabase.Companion.VERSION
import com.example.todo_list.data.dao.NoteDao
import com.example.todo_list.data.model.RoomNote

@Database(entities = [RoomNote::class], version = VERSION)
abstract class TodoListDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        const val DB_NAME = "todolist.db"
        const val VERSION = 1
    }
}