package com.ptsl.network_sdk.data_model.entity

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.ptsl.network_sdk.BuildConfig
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
@Keep
data class AuthEntity(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("userId") var userId: Long = 0,
    @SerializedName("apiKey") var apiKey: String = BuildConfig.TOKEN,
    @SerializedName("userType") var userType: Int = 101,
    @SerializedName("sdkInitiateTimeStamp") var sdkInitiateTimeStamp: String = "",
    @SerializedName("integratedAppVersion") var integratedAppVersion: String = "",
    @SerializedName("integratedAppEventName") var integratedAppEventName: String = "Event",
    @SerializedName("sdkVersion") var sdkVersion: String = "",
    @SerializedName("isSdkInitialized") var isSdkInitialized: Boolean = false,
    @SerializedName("isLocationEnabled") var isLocationEnabled: Boolean = false,
    @SerializedName("isPhoneStateEnabled") var isPhoneStateEnabled: Boolean = false,
    @SerializedName("userLatitude") var userLatitude: Double = 0.0,
    @SerializedName("userLongitude") var userLongitude: Double = 0.0
) : Parcelable
