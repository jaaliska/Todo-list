package com.example.todo_list.domain.repository


interface PreferencesRepository {

    fun isShowCompletedNotes(): Boolean
    fun setShowCompletedNotes(value: Boolean)

}