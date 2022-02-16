package com.mironov.bugzillaapp.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mironov.bugzillaapp.R
import com.mironov.bugzillaapp.domain.Bug

@Database(entities = [Bug::class], version = 1, exportSchema = false)
abstract class BugsDatabase : RoomDatabase() {

    abstract fun bugsDao(): BugDao

    companion object {
        @Volatile
        private var INSTANCE: BugsDatabase? = null

        fun getDatabase(context: Context): BugsDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BugsDatabase::class.java,
                    context.getString(R.string.bugs_db_name)
                ).setJournalMode(JournalMode.TRUNCATE).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}