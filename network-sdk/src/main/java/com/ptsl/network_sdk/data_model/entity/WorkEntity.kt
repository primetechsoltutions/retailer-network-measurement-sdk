package com.ptsl.network_sdk.data_model.entity

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity
@Keep
data class WorkEntity internal constructor(
    @PrimaryKey(autoGenerate = false) val id: Int,
    @SerializedName("intervalTime") var intervalInMinutes: Int = 30,
    @SerializedName("startTime") var startTime: String = "08:00 AM",
    @SerializedName("endTime") var endTime: String = "10:00 PM",
    var precision: Boolean = false,
) : Parcelable