package com.example.hfc.ui_compose

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.ViewModel
import com.example.hfc.ITimeMeterHashFunctionInterface
import com.example.hfc.service.CalculateService

class MainViewModel: ViewModel() {
    private var calculator: ITimeMeterHashFunctionInterface? = null

    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            calculator = ITimeMeterHashFunctionInterface.Stub.asInterface(service)
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            calculator = null
        }
    }

    fun createExplicitIntent(context: Context): Intent = Intent(context, CalculateService::class.java)
}