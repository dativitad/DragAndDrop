package com.example.draganddroptest.di

import com.example.draganddroptest.data.AppPrefs
import com.example.draganddroptest.domain.XYRepository

interface AppModule {
    val appPrefs: AppPrefs
    val xyRepository: XYRepository
}