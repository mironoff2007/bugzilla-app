package com.mironov.bugzillaapp.data

import android.content.Context
import android.content.SharedPreferences

class DataShared(context: Context) {

    companion object {
        private const val KEY_FILTER_BUG_STATUS = "KEY_FILTER_BUG_STATUS"
        private const val SHARED_PREFERENCES_NAME = "SHARED_PREFERENCES_NAME"
    }

    private val pref: SharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pref.edit()

    fun saveStatusFilter(){
        editor.putBoolean(KEY_FILTER_BUG_STATUS,false).apply()
    }

    fun getStatusFilter(): Boolean {
        return pref.getBoolean(KEY_FILTER_BUG_STATUS,true)
    }
}