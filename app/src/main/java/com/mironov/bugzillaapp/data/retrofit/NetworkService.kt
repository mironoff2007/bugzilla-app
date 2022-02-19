package com.mironov.bugzillaapp.data.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object NetworkService {

    //private var mInstance: NetworkService? = null
    //https://bugzilla.mozilla.org/rest/bug?creation_time=2022-02-15
    private val BASE_URL = "https://bugzilla.mozilla.org/rest/"
    private val apiKey = "Qry2P0jEMVbJxnxuBz9JJrk5A6keESQYYYs63c5B"
    private lateinit var mRetrofit: Retrofit

    init {
        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        mRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }


    fun getJSONApi(): BugApi {
        return mRetrofit.create<BugApi>(BugApi::class.java)
    }
}