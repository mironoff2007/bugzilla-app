package com.mironov.bugzillaapp.data.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkService {

    private val BASE_URL = "https://bugzilla.mozilla.org/rest/"
    private var mRetrofit: Retrofit

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