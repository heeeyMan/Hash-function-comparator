package com.example.hfc.ui_screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hfc.data_models.ServiceTypes
import com.example.hfc.models.IMainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val model: IMainModel
): ViewModel() {
    val multipliers = arrayOf(1, 10, 100, 1000)
    val serviceTypes = ServiceTypes.entries.toTypedArray()
    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText

    private val _workingKotlinTimeText = MutableStateFlow("")
    val workingKotlinTimeText: StateFlow<String> = _workingKotlinTimeText

    private val _workingCppTimeText = MutableStateFlow("")
    val workingCppTimeText: StateFlow<String> = _workingCppTimeText

    private val _isShowKotlinProgressBar = MutableStateFlow(false)
    val isShowKotlinProgressBar: StateFlow<Boolean> = _isShowKotlinProgressBar

    private val _isShowCppProgressBar = MutableStateFlow(false)
    val isShowCppProgressBar: StateFlow<Boolean> = _isShowCppProgressBar

    private var _currentServiceType: MutableStateFlow<ServiceTypes> = MutableStateFlow(serviceTypes[0])
    val currentServiceType: StateFlow<ServiceTypes> = _currentServiceType

    private var _currentMultiplier: MutableStateFlow<Int> = MutableStateFlow(multipliers[0])
    val currentMultiplier: StateFlow<Int> = _currentMultiplier

    private val _isPrepareState = MutableStateFlow(false)
    val isPrepareState: StateFlow<Boolean> = _isPrepareState

    fun changeMultiplier(value: Int) {
        _currentMultiplier.value = value
    }
    fun changeServiceType(type: ServiceTypes) {
        _currentServiceType.value = type
    }

    fun changeInputText(newText: String) {
        _inputText.value = newText
    }

    fun getTimeHashFunction(
        numberIterations: Int = 100000
    ) {
        viewModelScope.launch {
            val message = prepareMessage()
            when(currentServiceType.value) {
                ServiceTypes.CPP -> getTimeCppHashFunction(message, numberIterations)
                ServiceTypes.KOTLIN -> getTimeKotlinHashFunction(message, numberIterations)
                ServiceTypes.KOTLIN_AND_CPP -> getTimeAllHashFunction(message, numberIterations)
            }
        }
    }

    private suspend fun prepareMessage(): String {
        _isPrepareState.emit(true)
        if(currentMultiplier.value == 1) {
            _isPrepareState.emit(false)
            return inputText.value
        }
        var message = inputText.value
        return viewModelScope.async(Dispatchers.IO) {
            for(index in 0..currentMultiplier.value) {
                message += inputText.value
            }
            _isPrepareState.emit(false)
            message
        }.await()
    }

    private fun getTimeAllHashFunction(
        message: String,
        numberIterations: Int = 100000
    ) {
        getTimeKotlinHashFunction(message, numberIterations)
        getTimeCppHashFunction(message, numberIterations)
    }

    private fun getTimeKotlinHashFunction(
        message: String,
        numberIterations: Int = 100000
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _isShowKotlinProgressBar.emit(true)
            val workingTimeKotlinFunc = async {
                model.getDataSpeedHashFunctionViaKotlinService(
                    text = message,
                    numberIterations = numberIterations
                )
            }
            _workingKotlinTimeText.emit(workingTimeKotlinFunc.await())
            _isShowKotlinProgressBar.emit(false)
        }
    }

    private fun getTimeCppHashFunction(
        message: String,
        numberIterations: Int = 100000
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _isShowCppProgressBar.emit(true)
            val workingTimeCppFunc = async {
                model.getDataSpeedHashFunctionViaCppService(
                    text = message,
                    numberIterations = numberIterations
                )
            }
            _workingCppTimeText.emit(workingTimeCppFunc.await())
            _isShowCppProgressBar.emit(false)
        }
    }

    fun getKotlinServiceConnection() = model.kotlinServiceConnection
    fun getCppServiceConnection() = model.cppServiceConnection

}