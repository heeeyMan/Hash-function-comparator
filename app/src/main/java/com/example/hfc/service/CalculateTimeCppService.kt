package com.example.hfc.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class CalculateTimeCppService: Service() {
    override fun onBind(intent: Intent?): IBinder? = null
}