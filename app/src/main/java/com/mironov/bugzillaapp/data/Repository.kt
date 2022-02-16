package com.mironov.bugzillaapp.data

import androidx.lifecycle.LiveData
import com.mironov.bugzillaapp.data.retrofit.ApiResponse
import com.mironov.bugzillaapp.data.retrofit.NetworkService
import com.mironov.bugzillaapp.data.room.BugsDatabase
import com.mironov.bugzillaapp.domain.Bug
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import java.util.*
import javax.inject.Inject

class Repository @Inject constructor(
    protected val dataShared: Any, protected  val bugsDb: BugsDatabase):BugsWebRepository {

    override fun getBugsFromNetworkByDate(date:String): Call<ApiResponse> {
        return NetworkService
            .getJSONApi().getBugsByDate(date)
    }

    fun saveBugsToDb(bugs: ArrayList<Bug>)
    {
        bugsDb.bugsDao().insertAllBugs(bugs)
    }

    fun getAllBugsFromDb(): Flow<List<Bug>> {
       return bugsDb.bugsDao().readAllBugs()
    }

    fun getAllBugsFromDbByOs(opSys:String): Flow<List<Bug>> {
        return bugsDb.bugsDao().readBugsByOs(opSys)
    }
}