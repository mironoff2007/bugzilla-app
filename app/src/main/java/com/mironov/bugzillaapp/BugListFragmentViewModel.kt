package com.mironov.bugzillaapp


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mironov.bugzillaapp.domain.Status
import javax.inject.Inject

class BugListFragmentViewModel @Inject constructor() : ViewModel() {

    companion object {

    }

   //protected lateinit var repository: Repository

    var statusNewsByDate = MutableLiveData<Status>()

    fun getNews(date:String) {
            //getBugsByDate(daysBack)

    }


}