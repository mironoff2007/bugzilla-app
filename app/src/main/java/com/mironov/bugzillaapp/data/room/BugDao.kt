package com.mironov.bugzillaapp.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.mironov.bugzillaapp.domain.Bug

@Dao
interface BugDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addArticle(bug: Bug)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllBugs(list: List<Bug>)

    @Update
    fun updateBug(bug: Bug)

    @Delete
    fun deleteBug(bug: Bug)

    @Query("DELETE FROM Bug")
    fun resetTable( )

    @RawQuery(observedEntities = [Bug::class])
    fun readBugsByDate(query: SupportSQLiteQuery): LiveData<List<Bug>>

    @Query("SELECT * FROM Bug")
    fun readAllBugs(): LiveData<List<Bug>>

}