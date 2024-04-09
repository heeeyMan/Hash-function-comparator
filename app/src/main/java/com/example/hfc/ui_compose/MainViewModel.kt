package com.example.hfc.ui_compose

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hfc.ITimeMeterHashFunctionInterface
import com.example.hfc.models.HashFunctionTimeDataModel
import com.example.hfc.service.CalculateService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText

    private val _workingTimeText = MutableStateFlow(HashFunctionTimeDataModel())
    val workingTimeText: StateFlow<HashFunctionTimeDataModel> = _workingTimeText

    private val _isShowProgressBar = MutableStateFlow(false)
    val isShowProgressBar: StateFlow<Boolean> = _isShowProgressBar

    private var calculator: ITimeMeterHashFunctionInterface? = null

    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            calculator = ITimeMeterHashFunctionInterface.Stub.asInterface(service)
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            calculator = null
        }
    }

    fun changeInputText(newText: String) {
        _inputText.value = newText
    }

    fun createExplicitIntent(context: Context): Intent = Intent(context, CalculateService::class.java)

    fun getDataSpeedHashFunction(numberIterations: Int = 100000) {
        viewModelScope.launch(Dispatchers.IO) {
            _isShowProgressBar.emit(true)
            val workingTimeCppFunc = async(Dispatchers.Default) {
                getDataSpeedHashFunctionViaCppService(numberIterations)
            }
            val workingTimeKotlinFunc = async(Dispatchers.Default) {
                getDataSpeedHashFunctionViaKotlinService(numberIterations)
            }
            val result = HashFunctionTimeDataModel(
                workingTimeCppFunc = workingTimeCppFunc.await(),
                workingTimeKotlinFunc = workingTimeKotlinFunc.await()
            )
            _workingTimeText.emit(result)
            _isShowProgressBar.emit(false)
        }
    }

    private fun getDataSpeedHashFunctionViaKotlinService(numberIterations: Int): String {
        val message = inputText.value
        val result = calculator?.measureRunningTimeHashFunction(message, numberIterations)
        return result.toString()
    }
    private fun getDataSpeedHashFunctionViaCppService(numberIterations: Int): String {
        return "0"
    }
}