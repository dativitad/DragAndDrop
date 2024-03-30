package com.example.draganddroptest.data

interface AppPrefs {
    fun saveXY(x: Float, y: Float)
    fun getX(): Float
    fun getY(): Float
}