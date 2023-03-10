package com.example.todo_list.data.repository

import android.content.SharedPreferences
import javax.inject.Inject

// todo: where should we place an interface?
class UserPreferencesRepository @Inject constructor(
    private val preferences: SharedPreferences
){
    companion object {
        const val SHOW_COMPLETED_NOTES_KEY = "show_completed_notes"
    }

    fun isShowCompletedNotes(): Boolean {
        return preferences.getBoolean(SHOW_COMPLETED_NOTES_KEY, false)
    }

    fun setShowCompletedNotes(value: Boolean) {
        return preferences.edit().putBoolean(SHOW_COMPLETED_NOTES_KEY, value).apply()
    }
}