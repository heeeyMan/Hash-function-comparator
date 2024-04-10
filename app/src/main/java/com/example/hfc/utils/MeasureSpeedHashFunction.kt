package com.example.hfc.utils

import android.util.Log
import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.algorithms.digest.SHA256
import kotlinx.coroutines.runBlocking
import kotlin.time.TimedValue
import kotlin.time.measureTimedValue

object MeasureSpeedHashFunction {

    fun measureRunningTimeHashFunctionNanos(message: String, numberIterations: Int): Long {
        var timeValue: TimedValue<Unit>? = null
        runBlocking {
            timeValue = measureTimedValue {
                for(iteration in 0..numberIterations) {
                    encryptThisStringSHA256(message)
                }
            }
        }
        return if(timeValue != null) {
            timeValue!!.duration.inWholeNanoseconds / numberIterations
        } else {
            0
        }
    }

    // The SHA (Secure Hash Algorithm) is one of a number of cryptographic hash functions
    private suspend fun encryptThisStringSHA256(input: String) {
        try {
            CryptographyProvider.Default
                .get(SHA256)
                .hasher()
                .hash(input.encodeToByteArray())
        } catch (e: Exception) {
            Log.d("alex", "encryptThisStringSHA256 error $e")
        }
    }
}