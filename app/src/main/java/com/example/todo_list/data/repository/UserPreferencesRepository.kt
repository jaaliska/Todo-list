package com.example.todo_list.data.repository

import android.content.SharedPreferences
import com.example.todo_list.domain.repository.PreferencesRepository
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val preferences: SharedPreferences
) : PreferencesRepository {

    override fun isShowCompletedNotes(): Boolean {
        return preferences.getBoolean(SHOW_COMPLETED_NOTES_KEY, false)
    }

    override fun setShowCompletedNotes(value: Boolean) {
        return preferences.edit().putBoolean(SHOW_COMPLETED_NOTES_KEY, value).apply()
    }

    companion object {
        const val SHOW_COMPLETED_NOTES_KEY = "show_completed_notes"
    }
}