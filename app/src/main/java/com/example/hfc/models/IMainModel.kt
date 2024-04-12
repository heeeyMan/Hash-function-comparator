package com.example.hfc.models

import android.content.ServiceConnection

interface IMainModel {
    val kotlinServiceConnection: ServiceConnection
    val cppServiceConnection: ServiceConnection
    fun getDataSpeedHashFunctionViaKotlinService(text: String, numberIterations: Int): String
    fun getDataSpeedHashFunctionViaCppService(text: String, numberIterations: Int): String

}