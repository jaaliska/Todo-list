package com.example.todo_list.app.di

import com.example.todo_list.app.di.modules.DatabaseModule
import com.example.todo_list.app.di.modules.RepositoryModule
import dagger.Component

@Component( modules = [RepositoryModule::class, DatabaseModule::class])
interface ApplicationComponent {

}

