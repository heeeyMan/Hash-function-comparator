package com.example.hfc.service

import android.app.Service
import android.content.Intent
import com.example.hfc.ITimeMeterHashFunctionCppInterface

class CalculateTimeCppService: Service() {
    override fun onCreate() {
        super.onCreate()
        System.loadLibrary("native-lib")
    }
    override fun onBind(intent: Intent?) =  object : ITimeMeterHashFunctionCppInterface.Stub() {
        override fun measureRunningTimeHashFunction(message: String?, numberIterations: Int): Double {
            return getDataSpeedHashFunctionViaCpp(message ?: "", numberIterations)
        }
    }
    private external fun getDataSpeedHashFunctionViaCpp(message: String?, numberIterations: Int): Double
}