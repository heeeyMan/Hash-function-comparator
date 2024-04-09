package com.example.hfc

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.hfc.ui.theme.HFCTheme
import com.example.hfc.ui_compose.MainScreen
import com.example.hfc.ui_compose.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        System.loadLibrary("native-lib")
        setContent {
            HFCTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainScreen(viewModel)
                }
            }
        }
        Log.d("alex", "onCreate int equal = ${getInt()}")
    }

    override fun onStart() {
        super.onStart()
        bindService(
            viewModel.createExplicitIntent(this),
            viewModel.serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(viewModel.serviceConnection)
    }
    private external fun getInt(): Int
}