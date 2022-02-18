package com.mironov.bugzillaapp.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.mironov.bugzillaapp.domain.Bug
import kotlinx.coroutines.flow.Flow

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

    @Query("SELECT * FROM Bug Where Bug.creationTime Like :date")
    fun readAllBugsByDate(date: String): Flow<List<Bug>>

    @Query("SELECT * FROM Bug Where Bug.opSys Like :os AND Bug.creationTime Like :date")
    fun readBugsByOsAndDate(os:String,date: String): Flow<List<Bug>>

}