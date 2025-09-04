package com.ptsl.network_sdk


import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ptsl.network_sdk.data_model.entity.AuthEntity
import com.ptsl.network_sdk.db.NetworkDao
import com.ptsl.network_sdk.utils.CheckPermissionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import javax.inject.Inject


@FlowPreview
@ExperimentalCoroutinesApi
class NetworkDataUploader @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val databaseDao: NetworkDao,
    ) {
    private lateinit var checkPermissionHandler: CheckPermissionHandler
    private lateinit var context: Context

    fun init(activity: AppCompatActivity) {
            checkPermissionHandler = CheckPermissionHandler(activity)
            context = activity.applicationContext
    }


    fun startUploading(
        userId: Long,
        integratedAppVersion: String,
        sdkInitiateTimeStamp: String,
        integratedAppEventName: String,
        userLatitude: Double = 0.0,
        userLongitude: Double = 0.0,
        callback: (Boolean) -> Unit
    ) {
        if (this::checkPermissionHandler.isInitialized) {
            Log.d("userID", "--------> \n $userId \n <----------")
            checkPermissionHandler.requestPermission{
                coroutineScope.launch {
                    val isLocationEnabled = checkPermissionHandler.isLocationPermissionGranted()
                    val isPhoneStateEnabled = checkPermissionHandler.isPhoneStatePermissionGranted()

                    val auth = AuthEntity(userId = userId,
                        integratedAppVersion = integratedAppVersion,
                        sdkInitiateTimeStamp = sdkInitiateTimeStamp,
                        integratedAppEventName = integratedAppEventName,
                        sdkVersion = BuildConfig.SdkVersion,
                        isSdkInitialized = this@NetworkDataUploader::checkPermissionHandler.isInitialized,
                        isLocationEnabled = isLocationEnabled,
                        isPhoneStateEnabled = isPhoneStateEnabled,
                        userLatitude = userLatitude,
                        userLongitude = userLongitude
                    )
                    databaseDao.insertAuthData(auth)
                    enqueueNetworkDataWork()
                    callback(true)
                }
            }

        } else {
            callback(false)
        }
    }

    private fun enqueueNetworkDataWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()
        val workRequest = OneTimeWorkRequestBuilder<NetworkDataWorker>().setConstraints(constraints).build()
        WorkManager.getInstance(context).enqueue(workRequest)
    }
}