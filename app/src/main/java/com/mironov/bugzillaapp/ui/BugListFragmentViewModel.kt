package com.mironov.bugzillaapp.ui


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mironov.bugzillaapp.data.Repository
import com.mironov.bugzillaapp.data.retrofit.ApiResponse
import com.mironov.bugzillaapp.domain.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class BugListFragmentViewModel @Inject constructor() : ViewModel() {

    @Inject
    protected lateinit var repository: Repository

    var status = MutableLiveData<Status>()

    var filterParam = MutableLiveData<String>()

    fun getTodayBugs(filterOs:String,orderBy:SortBy) {
        status.postValue(Status.LOADING)
        repository.getBugsFromNetworkByDate(DateUtil.getPreviousDayDate(1))
            ?.enqueue(object : Callback<ApiResponse?> {
                override fun onResponse(
                    call: Call<ApiResponse?>,
                    response: Response<ApiResponse?>
                ) {
                    if (response.body() != null) {
                        if (response.body()?.bugs?.size != 0) {

                            val bugs = response.body()!!.bugs

                            viewModelScope.launch(Dispatchers.IO) {
                                repository.saveBugsToDb(bugs!!)

                              getBugs(filterOs,orderBy)
                            }
                        } else {
                            status.postValue(Status.EMPTY)
                        }
                    } else {
                        //тут нужно сообщение из error body из стрима перевести в стринг
                        status.postValue(Status.ERROR(response.errorBody().toString()))
                    }
                }

                override fun onFailure(call: Call<ApiResponse?>, t: Throwable) {
                    status.postValue(Status.ERROR(t.message.toString()))
                }
            })
    }

    fun getBugs(opSys: String, orderBy: SortBy) {
        viewModelScope.launch(Dispatchers.Main) {
            if (opSys.isBlank()) {
                repository.getAllBugsFromDb().collect{ bugs->
                    sortBugs(orderBy, bugs as ArrayList<Bug>)
                    status.postValue(Status.DATA(bugs))}
                }

        else {
                repository.getAllBugsFromDbByOs(opSys).collect{bugs->
                    sortBugs(orderBy, bugs as ArrayList<Bug>)
                    status.postValue(Status.DATA(bugs))
                }
            }
        }
    }

    private fun sortBugs(orderBy: SortBy,bugs:ArrayList<Bug>) {
        when (orderBy) {
           SortBy.PRODUCT ->{
               bugs.sortBy{bug->bug.product}
           }

            SortBy.STATUS ->{
                bugs.sortBy{bug->bug.status}
            }

            SortBy.TIME ->{

            }
        }
    }

    fun getFilterParam() {
       filterParam.postValue(repository.getFilterOption())
    }
}

