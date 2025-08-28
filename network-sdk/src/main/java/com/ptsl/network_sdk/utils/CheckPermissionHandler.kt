package com.ptsl.network_sdk.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import com.permissionx.guolindev.PermissionX

class CheckPermissionHandler(private val activity: AppCompatActivity) {
    private var callback: ((Boolean) -> Unit)? = null
    fun isPermissionGranted(): Boolean {
        val permissions = listOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        val isAllPermissionsGranted = permissions.all { permission ->
            ContextCompat.checkSelfPermission(
                activity, permission
            ) == PackageManager.PERMISSION_GRANTED
        }
        return isGpsEnabled() && isAllPermissionsGranted
    }
fun requestPermission(callback: (Boolean) -> Unit) {
    this.callback = callback

    val requiredPermissions = listOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    val missingPermissions = requiredPermissions.filter { permission ->
        ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED
    }

    if (missingPermissions.isNotEmpty()) {
        // First time install or denied previously — request once
        permissionLauncher.launch(missingPermissions.toTypedArray())
        return
    }

    if (!isGpsEnabled()) {
        Log.d("PermissionHandler", "GPS is disabled")
        requestGPSPermission()
        return
    }

    callback(true)
}
    private val permissionLauncher =
        activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            // Callback with true if all permissions granted
            val allGranted = permissions.all { it.value }
            callback?.invoke(isPermissionGranted())
        }


    private fun requestGPSPermission() {
        if (!isGpsEnabled()) {
            val locationRequest =
                LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 1000).build()
            val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
            val locationSettingsRequest = builder.build()
            val settingsClient: SettingsClient = LocationServices.getSettingsClient(activity)
            val task: Task<LocationSettingsResponse> =
                settingsClient.checkLocationSettings(locationSettingsRequest)
            task.addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        return@addOnCompleteListener
                    }
                } else {
                    try {
                        val intentSender =
                            (result.exception as ResolvableApiException).resolution.intentSender
                        val intentSenderRequest = IntentSenderRequest.Builder(intentSender).build()
                        gpsLauncher.launch(intentSenderRequest)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        requestRuntimePermission()
                    }
                }
            }
        } else {
            requestRuntimePermission()
        }

    }


    private fun requestRuntimePermission() {
        val permissions = arrayListOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        PermissionX.init(activity).permissions(permissions)
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList, getPermissionError(deniedList), "OK", "Cancel"
                )
            }.onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList, "Please allow necessary permissions.", "OK", "Cancel"
                )
            }.request { allGranted, grantedList, deniedList ->
                callback?.let { it(isPermissionGranted()) }
            }
    }


    private val gpsLauncher =
        activity.registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            requestRuntimePermission()
        }


    private fun isGpsEnabled(): Boolean {
        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun getPermissionError(deniedList: List<String>): String {
        return "The core functionalities of the app rely on the following permissions. Please ensure they are enabled for optimal performance."
    }
}
