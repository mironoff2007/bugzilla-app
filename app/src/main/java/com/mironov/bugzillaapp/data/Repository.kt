package com.mironov.bugzillaapp.data

import com.mironov.bugzillaapp.data.retrofit.ApiResponse
import com.mironov.bugzillaapp.data.retrofit.NetworkService
import retrofit2.Call

class Repository {

    fun getBugsFromNetworkByDate(date:String): Call<ApiResponse> {
        return NetworkService
            .getJSONApi().getBugsByDate(date)
    }
}