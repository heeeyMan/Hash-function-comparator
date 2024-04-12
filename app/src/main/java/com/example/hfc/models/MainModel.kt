package com.example.hfc.models

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.example.hfc.ITimeMeterHashFunctionCppInterface
import com.example.hfc.ITimeMeterHashFunctionInterface

class MainModel: IMainModel {
    private var _kotlinCalculator: ITimeMeterHashFunctionInterface? = null
    private var _cppCalculator: ITimeMeterHashFunctionCppInterface? = null
    override val kotlinServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            _kotlinCalculator = ITimeMeterHashFunctionInterface.Stub.asInterface(service)
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            _kotlinCalculator = null
        }
    }

    override val cppServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            _cppCalculator = ITimeMeterHashFunctionCppInterface.Stub.asInterface(service)
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            _cppCalculator = null
        }
    }
    override fun getDataSpeedHashFunctionViaKotlinService(text: String, numberIterations: Int): String {
        val result = try {
            _kotlinCalculator?.measureRunningTimeHashFunction(text, numberIterations)
        } catch (e: Exception) {
            Log.d("alex", "getDataSpeedHashFunctionViaKotlinService error $e")
            "-1"
        }
        return result.toString()
    }

    override fun getDataSpeedHashFunctionViaCppService(text: String, numberIterations: Int): String {
        val result = try {
            _cppCalculator?.measureRunningTimeHashFunction(text, numberIterations)?.toLong()
        } catch (e: Exception) {
            Log.d("alex", "getDataSpeedHashFunctionViaCppService error $e")
            "-1"
        }
        return result.toString()
    }
}