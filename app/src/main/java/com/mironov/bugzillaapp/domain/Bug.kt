package com.mironov.bugzillaapp.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
class Bug(
    @SerializedName("id")
    val id: String,

    @SerializedName("classification")
    val classification: String,

    @SerializedName("platform")
    val platform: String,

    @SerializedName("op_sys")
    val opSys: String,

    @SerializedName("product")
    val product: String,

    @SerializedName("summary")
    val summary: String,

    @SerializedName("creation_time")
    @PrimaryKey
    val creationTime: String,

    @SerializedName("status")
    val status: String
) : Parcelable {}