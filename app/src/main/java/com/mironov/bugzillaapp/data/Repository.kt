package com.mironov.bugzillaapp.data

import com.mironov.bugzillaapp.data.retrofit.ApiResponse
import com.mironov.bugzillaapp.data.retrofit.NetworkService
import com.mironov.bugzillaapp.data.room.BugsDatabase
import com.mironov.bugzillaapp.domain.Bug
import com.mironov.bugzillaapp.domain.SortBy
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import java.util.*
import javax.inject.Inject

class Repository @Inject constructor(
    protected val dataShared: DataShared, protected val bugsDb: BugsDatabase
) : BaseRepository {

    override fun getBugsFromNetworkByDate(date: String): Call<ApiResponse> {
        return NetworkService
            .getJSONApi().getBugsByDate(date)
    }

    override fun saveBugsToDb(bugs: ArrayList<Bug>) {
        bugsDb.bugsDao().insertAllBugs(bugs)
    }

    override fun getAllBugsFromDbByDate(date: String): Flow<List<Bug>> {
        return bugsDb.bugsDao().readAllBugsByDate("%$date%")
    }

    override fun getAllBugsFromDbByOsAndDate(opSys: String, date: String): Flow<List<Bug>> {
        return bugsDb.bugsDao().readBugsByOsAndDate("$opSys%", "%$date%")
    }

    override fun saveFilterOption(filter: String) {
        dataShared.saveOsFilter(filter)
    }

    override fun getFilterOption(): String {
        return dataShared.getOsFilter()
    }

    override fun saveSortOption(sortOption: SortBy) {
        val sortId = when (sortOption) {
            SortBy.STATUS -> 0
            SortBy.PRODUCT -> 1
            SortBy.TIME -> 2
        }
        dataShared.saveSortOption(sortId)
    }

    override fun getSortOption(): SortBy {
        return when (dataShared.getSortOption()) {
            0 -> SortBy.STATUS
            1 -> SortBy.PRODUCT
            2 -> SortBy.TIME
            else -> SortBy.TIME
        }
    }
}