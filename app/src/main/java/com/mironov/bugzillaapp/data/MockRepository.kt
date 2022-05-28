package com.mironov.bugzillaapp.data

import com.mironov.bugzillaapp.data.retrofit.ApiResponse
import com.mironov.bugzillaapp.domain.Bug
import com.mironov.bugzillaapp.domain.SortBy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class MockRepository: BaseRepository {

    companion object {
        var bugsCount = 1
    }

    class MockCall : Call<ApiResponse> {
        override fun clone(): Call<ApiResponse> {
            return this
        }

        override fun execute(): Response<ApiResponse> {
            val response = ApiResponse()
            val bugs = ArrayList<Bug>()
            for (i in 1..bugsCount) {
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

            response.bugs = bugs

            bugsCount++

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

    override fun saveSortOption(sortOption: SortBy) {
        TODO("Not yet implemented")
    }

    override fun getSortOption(): SortBy {
        TODO("Not yet implemented")
    }

    override fun getAllBugsFromDbByDate(date: String): Flow<List<Bug>> {
        return flowOf(
            listOf(
                Bug(
                    1,
                    "class",
                    "platf",
                    "opSys",
                    "prod",
                    "summary",
                    "time",
                    "status"
                )
            )
        )
    }

    override fun getAllBugsFromDbByOsAndDate(opSys: String, date: String): Flow<List<Bug>> {
        TODO("Not yet implemented")
    }

    override fun saveBugsToDb(bugs: ArrayList<Bug>) {

    }

}