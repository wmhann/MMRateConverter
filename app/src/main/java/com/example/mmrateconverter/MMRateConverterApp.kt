package com.example.mmrateconverter

import android.app.Application
import com.google.firebase.FirebaseApp

class MMRateConverterApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}