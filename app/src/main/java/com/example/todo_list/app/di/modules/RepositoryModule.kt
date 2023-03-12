package com.example.todo_list.app.di.modules

import com.example.todo_list.data.repository.CachedNotesRepository
import com.example.todo_list.data.repository.UserPreferencesRepository
import com.example.todo_list.domain.repository.NotesRepository
import com.example.todo_list.domain.repository.PreferencesRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNotesRepository(repository: CachedNotesRepository): NotesRepository

    @Binds
    @Singleton
    abstract fun bindPreferencesRepository(repository: UserPreferencesRepository): PreferencesRepository
}