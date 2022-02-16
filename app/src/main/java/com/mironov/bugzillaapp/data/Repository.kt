package com.mironov.bugzillaapp.data

import com.mironov.bugzillaapp.data.retrofit.ApiResponse
import com.mironov.bugzillaapp.data.retrofit.NetworkService
import com.mironov.bugzillaapp.data.room.BugsDatabase
import com.mironov.bugzillaapp.domain.Bug
import retrofit2.Call
import java.util.*
import javax.inject.Inject

class Repository @Inject constructor(
    protected val dataShared: Any, protected  val bugsDb: BugsDatabase) {

    fun getBugsFromNetworkByDate(date:String): Call<ApiResponse> {
        return NetworkService
            .getJSONApi().getBugsByDate(date)
    }

    fun saveBugsToDb(bugs: ArrayList<Bug>)
    {
        bugsDb.bugsDao().insertAllBugs(bugs)
    }
}