package com.sultonuzdev.dailydo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DailyDoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize ThreeTenABP for date handling
        com.jakewharton.threetenabp.AndroidThreeTen.init(this)
    }
}