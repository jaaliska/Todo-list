package com.example.todo_list.app.di

import com.example.todo_list.app.di.modules.RepositoryModule
import dagger.Component

@Component( modules = [RepositoryModule::class])
interface ApplicationComponent {

}

