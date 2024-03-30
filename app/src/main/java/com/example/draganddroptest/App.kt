package com.example.draganddroptest

import android.app.Application
import com.example.draganddroptest.di.AppModule
import com.example.draganddroptest.di.AppModuleImpl

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(this@App)
    }
    companion object {
       lateinit var appModule: AppModule
    }
}