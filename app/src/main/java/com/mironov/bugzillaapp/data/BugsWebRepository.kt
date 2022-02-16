package com.mironov.bugzillaapp.data

import com.mironov.bugzillaapp.data.retrofit.ApiResponse
import retrofit2.Call

interface BugsWebRepository {
    fun getBugsFromNetworkByDate(date:String): Call<ApiResponse>
}