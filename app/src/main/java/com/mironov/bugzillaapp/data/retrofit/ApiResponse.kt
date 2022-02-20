package com.mironov.bugzillaapp.data.retrofit

import com.google.gson.annotations.SerializedName
import com.mironov.bugzillaapp.domain.Bug
import java.util.*

class ApiResponse {
    @SerializedName("bugs")
    var bugs : ArrayList<Bug>? = null
}