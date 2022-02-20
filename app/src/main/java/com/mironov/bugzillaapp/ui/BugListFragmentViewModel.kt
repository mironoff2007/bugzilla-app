package com.mironov.bugzillaapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mironov.bugzillaapp.data.BaseRepository
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
    protected lateinit var repository: BaseRepository

    var status = MutableLiveData<Status>()

    var filterSortParam = MutableLiveData<FilterSortParams>()

    var isNewBugs = MutableLiveData<Boolean>()

    private fun getTodayBugsWeb(filterOs: String, orderBy: SortBy, date: String) {
        status.postValue(Status.LOADING)
        repository.getBugsFromNetworkByDate(date)
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
                                getBugs(filterOs, orderBy, date)
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

    fun getBugs(opSys: String, orderBy: SortBy, date: String) {
        viewModelScope.launch(Dispatchers.Main) {
            if (opSys.isBlank()) {
                repository.getAllBugsFromDbByDate(date).collect { bugs ->
                    if (bugs.isEmpty()) {
                        getTodayBugsWeb(opSys, orderBy, date)
                    } else {
                        sortBugs(orderBy, bugs as ArrayList<Bug>)
                        status.postValue(Status.DATA(bugs))
                    }
                }
            } else {
                repository.getAllBugsFromDbByOsAndDate(opSys, date).collect { bugs ->
                    if (bugs.isEmpty()) {
                        status.postValue(Status.EMPTY)
                    } else {
                        sortBugs(orderBy, bugs as ArrayList<Bug>)
                        status.postValue(Status.DATA(bugs))
                    }
                }
            }
        }
    }

    private fun sortBugs(orderBy: SortBy, bugs: ArrayList<Bug>) {
        when (orderBy) {
            SortBy.PRODUCT -> {
                bugs.sortBy { bug -> bug.product }
            }

            SortBy.STATUS -> {
                bugs.sortBy { bug -> bug.status }
            }

            SortBy.TIME -> {

            }
        }
    }

    fun getFilterParam() {
        val filterOption=repository.getFilterOption()
        val sortOption=repository.getSortOption()
        filterSortParam.postValue(FilterSortParams(sortOption,filterOption))
    }

    fun checkNewBugs(numberBugs: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.getAllBugsFromDbByDate(DateUtil.getTodayDate()).collect { bugs ->
                if (bugs.size>numberBugs){
                    isNewBugs.postValue(true)
                }
            }
        }
    }



}

