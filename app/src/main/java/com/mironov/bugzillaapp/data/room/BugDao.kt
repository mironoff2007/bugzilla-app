package com.mironov.bugzillaapp.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.mironov.bugzillaapp.domain.Bug
import io.reactivex.Single

@Dao
interface BugDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addArticle(bug: Bug)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertAllBugs(list: List<Bug>)

    @Update
    suspend fun updateBug(bug: Bug)

    @Delete
    suspend fun deleteBug(bug: Bug)

    @Query("DELETE FROM Bug")
    fun resetTable( )

    @RawQuery(observedEntities = [Bug::class])
    fun readBugsByDate(query: SupportSQLiteQuery): LiveData<List<Bug>>

    @Query("SELECT * FROM Bug")
    fun readAllBugs(): Single<List<Bug>>

}