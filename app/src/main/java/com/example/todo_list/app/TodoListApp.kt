package com.example.todo_list.app


import android.app.Application
import com.example.todo_list.app.di.ApplicationComponent
import com.example.todo_list.app.di.DaggerApplicationComponent

class TodoListApp : Application() {

    val appComponent: ApplicationComponent = DaggerApplicationComponent
        .builder()
        .application(this)
        .build()

}