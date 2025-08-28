package com.ptsl.networksdk_event

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ptsl.network_sdk.NetworkDataUploader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.text.SimpleDateFormat
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var networkDataUploader: NetworkDataUploader


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        networkDataUploader.init(this)
//
//        findViewById<Button>(R.id.btn).setOnClickListener {
//            Log.d("userID", "--------> \n click \n <----------")
//            networkDataUploader.requestPermission { isAllPermissionGranted ->
//                if (isAllPermissionGranted){
//                    repeat(5000) { index ->
//                        Log.d("StartUpload", "Calling upload iteration: $index")
//                        startUploading()
//                    }
////                    startUploading()
//                }
//                else {
//                    Log.e(this.javaClass.name, "sdk : permission rejected by user")
//                }
//            }
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        networkDataUploader.init(this)

        val inputField = findViewById<EditText>(R.id.inputLoopCount)
        val startButton = findViewById<Button>(R.id.btn)

        startButton.setOnClickListener {
            Log.d("userID", "--------> \n click \n <----------")

            networkDataUploader.requestPermission { isAllPermissionGranted ->
                if (isAllPermissionGranted) {
                    val inputText = inputField.text.toString()
                    val repeatCount = inputText.toIntOrNull()

                    if (repeatCount != null && repeatCount > 0) {
                        repeat(repeatCount) { index ->
                            Log.d("StartUpload", "Calling upload iteration: $index")
                            startUploading()
                        }
                    } else {
                        Toast.makeText(this, "Please enter a valid positive number", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e(this.javaClass.name, "sdk : permission rejected by user")
                }
            }
        }
    }


    private fun startUploading() {
        val currentDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(System.currentTimeMillis())
        networkDataUploader.startUploading(31270, "8.5.9", currentDate,   "Test App") { success ->
            if (success) {
                Log.e(this.javaClass.name, "sdk : started")
            } else {
                Log.e(this.javaClass.name, "sdk : not started")
            }
        }
    }
}