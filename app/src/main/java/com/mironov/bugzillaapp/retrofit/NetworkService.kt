package com.mironov.bugzillaapp.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkService {

    //private var mInstance: NetworkService? = null
    //https://bugzilla.mozilla.org/rest/bug?creation_time=2022-02-15
    private val BASE_URL = "https://bugzilla.mozilla.org/rest/"
    private val apiKey="Qry2P0jEMVbJxnxuBz9JJrk5A6keESQYYYs63c5B"
    private lateinit var mRetrofit: Retrofit

   init {
       mRetrofit = Retrofit.Builder()
           .baseUrl(BASE_URL)
           .addConverterFactory(GsonConverterFactory.create())
           .build()
   }


    fun getJSONApi(): BugApi {
        return mRetrofit.create<BugApi>(BugApi::class.java)
    }
}