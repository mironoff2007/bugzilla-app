package com.mironov.bugzillaapp.ui


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mironov.bugzillaapp.data.Repository
import com.mironov.bugzillaapp.data.retrofit.ApiResponse
import com.mironov.bugzillaapp.domain.DateUtil
import com.mironov.bugzillaapp.domain.Status
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class BugListFragmentViewModel @Inject constructor() : ViewModel() {

    @Inject
    protected lateinit var repository: Repository

    var status = MutableLiveData<Status>()

    fun getTodayBugs() {
           repository.getBugsFromNetworkByDate(DateUtil.getTodayDate()) ?.enqueue(object : Callback<ApiResponse?> {
               override fun onResponse(
                   call: Call<ApiResponse?>,
                   response: Response<ApiResponse?>
               ) {
                   if (response.body() != null) {

                       val bugs = response.body()!!.bugs

                       status.postValue(Status.DATA(bugs))
                   }
                   else{
                   //тут надо сообщение из error body из стрима перевести в стринг
                   status.postValue(Status.ERROR(response.errorBody().toString()))
                   }
               }

               override fun onFailure(call: Call<ApiResponse?>, t: Throwable) {
                   status.postValue(Status.ERROR(t.message.toString()))
               }
           })
    }

}