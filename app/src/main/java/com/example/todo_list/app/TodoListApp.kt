package com.example.todo_list.app

import com.example.todo_list.app.di.ApplicationComponent
import com.example.todo_list.app.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class TodoListApp : DaggerApplication() {

    companion object {
        const val PERMISSION_POST_NOTIFICATIONS = "android.permission.POST_NOTIFICATIONS"

        lateinit var appComponent:  ApplicationComponent
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        appComponent = DaggerApplicationComponent
        .builder()
            .application(this)
            .build()
        return appComponent
    }

}