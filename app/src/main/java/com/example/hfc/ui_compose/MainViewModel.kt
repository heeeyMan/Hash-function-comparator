package com.example.hfc.ui_compose

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

    private var _currentServiceType: MutableStateFlow<ServiceTypes> = MutableStateFlow(ServiceTypes.KOTLIN_AND_CPP)
    val currentServiceType: StateFlow<ServiceTypes> = _currentServiceType
    val serviceTypes = ServiceTypes.entries.toTypedArray()

    fun changeServiceType(type: ServiceTypes) {
        _currentServiceType.value = type
    }

    fun changeInputText(newText: String) {
        _inputText.value = newText
    }

    fun getTimeHashFunction(
        numberIterations: Int = 100000
    ) {
        when(currentServiceType.value) {
            ServiceTypes.CPP -> getTimeCppHashFunction(numberIterations)
            ServiceTypes.KOTLIN -> getTimeKotlinHashFunction(numberIterations)
            ServiceTypes.KOTLIN_AND_CPP -> getTimeAllHashFunction(numberIterations)
        }
    }

    private fun getTimeAllHashFunction(numberIterations: Int = 100000) {
        getTimeKotlinHashFunction(numberIterations)
        getTimeCppHashFunction(numberIterations)
    }

    private fun getTimeKotlinHashFunction(numberIterations: Int = 100000) {
        viewModelScope.launch(Dispatchers.IO) {
            _isShowKotlinProgressBar.emit(true)
            val workingTimeKotlinFunc = async {
                model.getDataSpeedHashFunctionViaKotlinService(
                    text = inputText.value,
                    numberIterations = numberIterations
                )
            }
            _workingKotlinTimeText.emit(workingTimeKotlinFunc.await())
            _isShowKotlinProgressBar.emit(false)
        }
    }

    private fun getTimeCppHashFunction(numberIterations: Int = 100000) {
        viewModelScope.launch(Dispatchers.IO) {
            _isShowCppProgressBar.emit(true)
            val workingTimeCppFunc = async {
                model.getDataSpeedHashFunctionViaCppService(
                    text = inputText.value,
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