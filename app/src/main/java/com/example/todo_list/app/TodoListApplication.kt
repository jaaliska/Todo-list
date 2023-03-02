package com.example.todo_list.app

import android.app.Application
import com.example.todo_list.app.di.DaggerApplicationComponent

class TodoListApplication : Application() {

    val appComponent = DaggerApplicationComponent.create()
}