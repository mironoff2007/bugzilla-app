package com.mironov.bugzillaapp.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BugApi {


    @GET("bug")
    fun getBugsByDate( @Query("creation_time") date: String): Call<ApiResponse>

}
