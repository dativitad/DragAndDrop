package com.example.draganddroptest.domain

interface XYRepository {
    fun getX(): Float
    fun getY(): Float
    fun saveXY(x: Float, y: Float)
}