package com.example.hfc.models

import android.content.ServiceConnection
import com.example.hfc.ITimeMeterHashFunctionCppInterface
import com.example.hfc.ITimeMeterHashFunctionInterface

interface IMainModel {
    val kotlinServiceConnection: ServiceConnection
    val cppServiceConnection: ServiceConnection
    fun getDataSpeedHashFunctionViaKotlinService(text: String, numberIterations: Int): String
    fun getDataSpeedHashFunctionViaCppService(text: String, numberIterations: Int): String

}