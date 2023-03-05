package com.example.todo_list.app.di

import android.app.Application
import com.example.todo_list.app.TodoListApp
import com.example.todo_list.app.di.modules.DatabaseModule
import com.example.todo_list.app.di.modules.RepositoryModule
import com.example.todo_list.presentation.ui.main.NoteListPresenter
import com.example.todo_list.presentation.ui.note_editing.EditNotePresenter
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component( modules = [RepositoryModule::class, DatabaseModule::class])
interface ApplicationComponent : AndroidInjector<TodoListApp> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }

    fun editNotePresenter(): EditNotePresenter
    fun noteListPresenter(): NoteListPresenter

}

