package com.example.hfc.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.hfc.ITimeMeterHashFunctionCppInterface

class CalculateTimeCppService: Service() {
    override fun onBind(intent: Intent?) =  object : ITimeMeterHashFunctionCppInterface.Stub() {
        override fun measureRunningTimeHashFunction(message: String?, numberIterations: Int): Double {
            System.loadLibrary("native-lib")
            return getDataSpeedHashFunctionViaCpp(message ?: "", numberIterations)
        }
    }
    private external fun getDataSpeedHashFunctionViaCpp(message: String?, numberIterations: Int): Double
}