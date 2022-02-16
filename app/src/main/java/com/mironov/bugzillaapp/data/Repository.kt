package com.mironov.bugzillaapp.data

import com.mironov.bugzillaapp.data.retrofit.ApiResponse
import com.mironov.bugzillaapp.data.retrofit.NetworkService
import com.mironov.bugzillaapp.data.room.BugsDatabase
import retrofit2.Call

class Repository(dataShared: Any, bugsDB: BugsDatabase) {

    fun getBugsFromNetworkByDate(date:String): Call<ApiResponse> {
        return NetworkService
            .getJSONApi().getBugsByDate(date)
    }
}