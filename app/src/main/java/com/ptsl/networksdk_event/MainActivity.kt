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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        networkDataUploader.init(this)

        val startButton = findViewById<Button>(R.id.btn)

        startButton.setOnClickListener {
            Log.d("userID", "--------> \n click \n <----------")
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
}