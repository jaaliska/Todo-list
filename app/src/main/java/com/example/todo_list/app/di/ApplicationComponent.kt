package com.example.todo_list.app.di

import android.app.Application
import android.content.Context
import com.example.todo_list.app.TodoListApp
import com.example.todo_list.app.di.modules.ApplicationModule
import com.example.todo_list.app.di.modules.DatabaseModule
import com.example.todo_list.app.di.modules.RepositoryModule
import com.example.todo_list.presentation.ui.MainActivity
import com.example.todo_list.presentation.ui.notes_list.NotesListScreen
import com.example.todo_list.presentation.ui.note_editing.EditNoteScreen
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component( modules = [AndroidInjectionModule::class, RepositoryModule::class, DatabaseModule::class, ApplicationModule::class])
interface ApplicationComponent : AndroidInjector<TodoListApp> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }

    fun context(): Context

    fun inject(activity: MainActivity)
    fun inject(usernameFragment: NotesListScreen)
    fun inject(passwordFragment: EditNoteScreen)

}

