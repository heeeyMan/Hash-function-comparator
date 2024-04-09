package com.example.hfc.utils

import android.util.Log
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.time.measureTimedValue

object MeasureSpeedHashFunction {

    fun measureRunningTimeHashFunctionNanos(message: String, numberIterations: Int = 100000): Long {
        val timeValue = measureTimedValue {
            for(iteration in 0..numberIterations) {
                encryptThisStringSHA256(message)
            }
        }
        Log.d("alex", "measureRunningTimeHashFunction ${timeValue.value}")
        return timeValue.duration.inWholeNanoseconds / numberIterations
    }

    // The SHA (Secure Hash Algorithm) is one of a number of cryptographic hash functions
    private fun encryptThisStringSHA256(input: String): String? {
        return try {
            val md = MessageDigest.getInstance("SHA-256")
            val messageDigest = md.digest(input.toByteArray())
            val no = BigInteger(1, messageDigest)
            var hashtext = no.toString(16)
            while (hashtext.length < 32) {
                hashtext = "0$hashtext"
            }
            hashtext
        } catch (e: Exception) {
            Log.d("alex", "encryptThisStringSHA256 error $e")
            null
        }
    }
}