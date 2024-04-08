package com.example.hfc.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.hfc.ITimeMeterHashFunctionInterface
import com.example.hfc.utils.Utils

class CalculateService: Service() {
    override fun onBind(intent: Intent?) =  object : ITimeMeterHashFunctionInterface.Stub() {
        override fun measureRunningTimeHashFunction(message: String?, numberIterations: Int): Long {
            return Utils.measureRunningTimeHashFunction(message ?: "", numberIterations)
        }
    }
}