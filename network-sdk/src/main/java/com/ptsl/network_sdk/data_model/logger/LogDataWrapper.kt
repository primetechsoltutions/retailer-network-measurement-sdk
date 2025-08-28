package com.ptsl.network_sdk.data_model.logger

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.ptsl.network_sdk.data_model.entity.AuthEntity
import com.ptsl.network_sdk.data_model.logger.EventLogModel
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class LogDataWrapper(
    @SerializedName("networkRetailerUserModel") val auth: AuthEntity,
    @SerializedName("eventlog") val eventLog: List<EventLogModel>
) : Parcelable
