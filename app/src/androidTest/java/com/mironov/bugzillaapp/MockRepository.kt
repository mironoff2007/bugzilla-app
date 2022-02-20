package com.mironov.bugzillaapp

import com.mironov.bugzillaapp.data.BaseRepository
import com.mironov.bugzillaapp.data.BugsWebRepository
import com.mironov.bugzillaapp.data.retrofit.ApiResponse
import com.mironov.bugzillaapp.domain.Bug
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class MockRepository : BaseRepository {

    companion object {
        var bugsCount = 0
    }

    class MockCall : Call<ApiResponse> {
        override fun clone(): Call<ApiResponse> {
            return this
        }

        override fun execute(): Response<ApiResponse> {
            val response = ApiResponse()
            val bugs = ArrayList<Bug>()
            for (i in 0..bugsCount) {
                bugs.add(
                    Bug(
                        i,
                        "class",
                        "platf",
                        "opSys",
                        "prod",
                        "summary",
                        "time",
                        "status"
                    )
                )
            }

            response.bugs=bugs

            //bugsCount++

            return Response.success(
                response
            )
        }

        override fun enqueue(callback: Callback<ApiResponse>) {
            callback.onResponse(clone(), execute())
        }

        override fun isExecuted(): Boolean {
            TODO("Not yet implemented")
        }

        override fun cancel() {
            TODO("Not yet implemented")
        }

        override fun isCanceled(): Boolean {
            TODO("Not yet implemented")
        }

        override fun request(): Request {
            TODO("Not yet implemented")
        }

        override fun timeout(): Timeout {
            TODO("Not yet implemented")
        }

    }

    override fun getBugsFromNetworkByDate(date: String): Call<ApiResponse> {
        return MockCall()
    }

    override fun saveFilterOption(filter: String) {
        TODO("Not yet implemented")
    }

    override fun getFilterOption(): String {
        TODO("Not yet implemented")
    }

    override fun getAllBugsFromDbByDate(date: String): Flow<List<Bug>> {
        TODO("Not yet implemented")
    }

    override fun getAllBugsFromDbByOsAndDate(opSys: String, date: String): Flow<List<Bug>> {
        TODO("Not yet implemented")
    }

    override fun saveBugsToDb(bugs: ArrayList<Bug>) {

    }

}