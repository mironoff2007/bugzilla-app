package com.mironov.bugzillaapp

import com.mironov.bugzillaapp.data.retrofit.ApiResponse
import com.mironov.bugzillaapp.data.retrofit.NetworkService
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Call
import retrofit2.Response

class ApiTest {

    var responseObj: ApiResponse? = null

    @Test
    fun getRequestTest() {

        val call: Call<ApiResponse> = NetworkService.getJSONApi().getBugsByDate("2022-02-15")

        val response: Response<ApiResponse> = call!!.execute()
        responseObj = response.body()
        assertEquals(responseObj?.bugs?.size != 0, true)
    }

}