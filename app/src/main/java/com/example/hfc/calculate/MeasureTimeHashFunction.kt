package com.example.hfc.calculate

import com.example.hfc.calculate.HashFunction.calculateFunction
import kotlin.time.measureTimedValue

object MeasureTimeHashFunction {

    fun measureRunningTimeHashFunctionNanos(message: String, numberIterations: Int): Long {
        val timeValue = measureTimedValue {
            for(iteration in 0..numberIterations) {
                calculateFunction(message)
            }
        }
        return timeValue.duration.inWholeNanoseconds / numberIterations
    }
}