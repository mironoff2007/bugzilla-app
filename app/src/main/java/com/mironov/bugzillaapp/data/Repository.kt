package com.mironov.bugzillaapp.data

import com.mironov.bugzillaapp.data.retrofit.ApiResponse
import com.mironov.bugzillaapp.data.retrofit.NetworkService
import com.mironov.bugzillaapp.data.room.BugsDatabase
import com.mironov.bugzillaapp.domain.Bug
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import java.util.*
import javax.inject.Inject

class Repository @Inject constructor(
    protected val dataShared: DataShared, protected  val bugsDb: BugsDatabase):BugsWebRepository {

    override fun getBugsFromNetworkByDate(date:String): Call<ApiResponse> {
        return NetworkService
            .getJSONApi().getBugsByDate(date)
    }

    fun saveBugsToDb(bugs: ArrayList<Bug>)
    {
        bugsDb.bugsDao().insertAllBugs(bugs)
    }

    fun getAllBugsFromDbByDate(date: String): Flow<List<Bug>> {
       return bugsDb.bugsDao().readAllBugsByDate("%$date%")
    }

    fun getAllBugsFromDbByOsAndDate(opSys:String, date: String): Flow<List<Bug>> {
        return bugsDb.bugsDao().readBugsByOsAndDate("$opSys%", "%$date%")
    }

    fun saveFilterOption(filter:String){
        dataShared.saveOsFilter(filter)
    }

    fun getFilterOption():String{
       return dataShared.getOsFilter()
    }
}