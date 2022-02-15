package com.mironov.bugzillaapp.retrofit

import com.google.gson.annotations.SerializedName

class Bug {
    @SerializedName("id")
    val id: String? = null

    @SerializedName("classification")
    val classification: String? = null

    @SerializedName("platform")
    val platform: String? = null

    @SerializedName("op_sys")
    val opSys: String? = null

    @SerializedName("product")
    val product: String? = null

    @SerializedName("summary")
    val summary: String? = null

    @SerializedName("creation_time")
    val creationTime: String? = null

    @SerializedName("status")
    val status: String? = null

}