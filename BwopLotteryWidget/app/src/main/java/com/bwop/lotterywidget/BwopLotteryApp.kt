package com.bwop.lotterywidget

import android.app.Application

class BwopLotteryApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize lottery engine with time-based seed
        LotteryEngine.initialize(System.currentTimeMillis())
    }
}
