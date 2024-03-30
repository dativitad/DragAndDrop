package com.example.draganddroptest.di

import android.content.Context
import com.example.draganddroptest.data.AppPrefs
import com.example.draganddroptest.data.AppPrefsImpl
import com.example.draganddroptest.data.XYRepositoryImpl
import com.example.draganddroptest.domain.XYRepository

class AppModuleImpl(private val appContext: Context): AppModule {
    override val appPrefs: AppPrefs get() = AppPrefsImpl(appContext)
    override val xyRepository: XYRepository get() = XYRepositoryImpl(appPrefs)
}