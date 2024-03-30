package com.example.draganddroptest.data

import com.example.draganddroptest.domain.XYRepository

class XYRepositoryImpl(private val appPrefs: AppPrefs) : XYRepository {

    override fun getX(): Float = appPrefs.getX()
    override fun getY(): Float = appPrefs.getY()
    override fun saveXY(x: Float, y: Float) {
        appPrefs.saveXY(x, y)
    }
}