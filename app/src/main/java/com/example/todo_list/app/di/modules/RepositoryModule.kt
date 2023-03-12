package com.example.todo_list.app.di.modules

import com.example.todo_list.data.repository.CachedNotesRepository
import com.example.todo_list.data.repository.NoteRepositoryImpl
import com.example.todo_list.domain.repository.NoteRepository
import com.example.todo_list.domain.repository.NotesRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNoteRepository(repository: NoteRepositoryImpl): NoteRepository

    @Binds
    @Singleton
    abstract fun bindNotesRepository(repository: CachedNotesRepository): NotesRepository
}