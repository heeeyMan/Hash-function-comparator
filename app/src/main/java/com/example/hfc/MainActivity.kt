package com.example.hfc

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.hfc.models.ServiceTypes
import com.example.hfc.ui.theme.HFCTheme
import com.example.hfc.ui_compose.MainScreen
import com.example.hfc.ui_compose.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HFCTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(viewModel)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        bindService(
            viewModel.createExplicitIntent(this, ServiceTypes.KOTLIN),
            viewModel.kotlinServiceConnection,
            Context.BIND_AUTO_CREATE
        )
        bindService(
            viewModel.createExplicitIntent(this, ServiceTypes.CPP),
            viewModel.cppServiceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(viewModel.kotlinServiceConnection)
        unbindService(viewModel.cppServiceConnection)
    }
}