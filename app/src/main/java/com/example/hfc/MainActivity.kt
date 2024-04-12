package com.example.hfc

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.hfc.models.MainModel
import com.example.hfc.service.CalculateTimeCppService
import com.example.hfc.service.CalculateTimeKotlinService
import com.example.hfc.ui.theme.HFCTheme
import com.example.hfc.ui_screens.MainScreen
import com.example.hfc.ui_screens.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by lazy { MainViewModel(MainModel()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
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
            Intent(this, CalculateTimeKotlinService::class.java),
            viewModel.getKotlinServiceConnection(),
            Context.BIND_AUTO_CREATE
        )
        bindService(
            Intent(this, CalculateTimeCppService::class.java),
            viewModel.getCppServiceConnection(),
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(viewModel.getKotlinServiceConnection())
        unbindService(viewModel.getCppServiceConnection())
    }
}