package com.skylextournament.app.repository

import android.content.Context

class SessionRepository(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)

    fun saveUsername(username: String) {
        sharedPreferences.edit().putString(USERNAME_KEY, username).apply()
    }

    fun getUsername() = sharedPreferences.getString(USERNAME_KEY, null) ?: ""

    fun saveEmail(email: String) {
        sharedPreferences.edit().putString(EMAIL_KEY, email).apply()
    }

    fun getEmail() = sharedPreferences.getString(EMAIL_KEY, null) ?: ""

    fun saveIsAdmin(isAdmin: Boolean) {
        sharedPreferences.edit().putBoolean(IS_ADMIN_KEY, isAdmin).apply()
    }

    fun getIsAdmin() = sharedPreferences.getBoolean(IS_ADMIN_KEY, false)

    private companion object {
        const val USERNAME_KEY = "username"
        const val EMAIL_KEY = "email"
        const val IS_ADMIN_KEY = "isAdmin"
    }
}