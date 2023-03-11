package com.example.todo_list.app.di.modules

import com.example.todo_list.presentation.ui.notification.ReminderReceiver
import com.example.todo_list.presentation.ui.notification.CompleteNoteReceiver
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BroadcastReceiverModule {

    @ContributesAndroidInjector
    abstract fun bindAlertReceiver(): ReminderReceiver

    @ContributesAndroidInjector
    abstract fun bindConfirmReceiver(): CompleteNoteReceiver

}