package com.example.todo_list.app.di.modules

import android.app.Application
import androidx.room.Room
import com.example.todo_list.app.TodoListDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(application: Application): TodoListDatabase = Room
        .databaseBuilder(application, TodoListDatabase::class.java, TodoListDatabase.DB_NAME)
        .build()

}