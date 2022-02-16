package com.mironov.bugzillaapp

import com.mironov.bugzillaapp.data.retrofit.ApiResponse
import com.mironov.bugzillaapp.data.retrofit.NetworkService
import com.mironov.bugzillaapp.domain.DateUtil
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Call
import retrofit2.Response

class ApiTest {

    var responseObj: ApiResponse? = null

    @Test
    fun getYesterdayBugs() {

        val call: Call<ApiResponse> = NetworkService.getJSONApi().getBugsByDate(DateUtil.getPreviousDayDate(1))

        val response: Response<ApiResponse> = call!!.execute()
        responseObj = response.body()
        assertEquals(responseObj?.bugs?.size != 0, true)
    }

}