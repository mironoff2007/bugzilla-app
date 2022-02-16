package com.mironov.bugzillaapp.domain

import java.util.ArrayList

sealed class Status {
    object LOADING : Status()
    data class ERROR(var message:String) : Status()
    class DATA(val articles:ArrayList<Bug>?): Status()
    object EMPTY : Status()
}
