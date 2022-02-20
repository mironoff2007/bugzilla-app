package com.mironov.bugzillaapp.data

import android.content.Context
import android.content.SharedPreferences

class DataShared(context: Context) {

    companion object {
        private const val KEY_FILTER_BUG_OS = "KEY_FILTER_BUG_OS"
        private const val KEY_SORT_BUG = "KEY_SORT_BUG"
        private const val SHARED_PREFERENCES_NAME = "SHARED_PREFERENCES_NAME"
    }

    private val pref: SharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pref.edit()

    fun saveOsFilter(filter:String){
        editor.putString(KEY_FILTER_BUG_OS,filter).apply()
    }

    fun getOsFilter(): String {
        return pref.getString(KEY_FILTER_BUG_OS,"")!!
    }

    fun saveSortOption(sortOption:Int) {
        editor.putInt(KEY_SORT_BUG,sortOption).apply()
    }

    fun getSortOption(): Int {
        return pref.getInt(KEY_SORT_BUG,0)!!
    }
}