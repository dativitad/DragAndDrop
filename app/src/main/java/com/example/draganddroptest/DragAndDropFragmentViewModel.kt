package com.example.draganddroptest

import android.graphics.PointF
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DragAndDropFragmentViewModel: ViewModel() {

    private val _initialPositionLiveData = MutableLiveData(PointF(0f, 0f))
    val initialPositionLiveData = _initialPositionLiveData

    fun updateXY(fractionX: Float, fractionY: Float) {
        _initialPositionLiveData.value = PointF(fractionX, fractionY)
    }
}