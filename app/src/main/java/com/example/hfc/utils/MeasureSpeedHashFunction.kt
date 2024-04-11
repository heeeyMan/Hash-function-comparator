package com.example.hfc.utils

import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.time.measureTimedValue

object MeasureSpeedHashFunction {

    fun measureRunningTimeHashFunctionNanos(message: String, numberIterations: Int): Long {
        val timeValue = measureTimedValue {
            for(iteration in 0..numberIterations) {
                encryptThisString(message)
            }
        }
        return timeValue.duration.inWholeNanoseconds / numberIterations
    }

    private fun encryptThisString(input: String): String {
        return try {
            val md = MessageDigest.getInstance("SHA-256")
            val messageDigest = md.digest(input.toByteArray())
            val no = BigInteger(1, messageDigest)
            var hashtext = no.toString(16)
            while (hashtext.length < 32) {
                hashtext = "0$hashtext"
            }
            hashtext
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }
    }
}