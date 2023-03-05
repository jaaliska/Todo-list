package com.example.todo_list.app.di.modules

import com.example.todo_list.data.repository.NoteRepositoryImpl
import com.example.todo_list.domain.repository.NoteRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(repository: NoteRepositoryImpl): NoteRepository

}