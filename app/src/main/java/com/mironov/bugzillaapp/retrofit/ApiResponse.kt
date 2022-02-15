package com.mironov.bugzillaapp.retrofit

import com.google.gson.annotations.SerializedName
import java.util.*

class ApiResponse {
    @SerializedName("bugs")
    val bugs : ArrayList<Bug>? = null
}