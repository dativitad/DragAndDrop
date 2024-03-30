package com.example.draganddroptest.presentation

import android.graphics.PointF
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.draganddroptest.domain.XYRepository

class DragAndDropViewModel(private val xyRepository: XYRepository): ViewModel() {

    private val _positionLiveData = MutableLiveData(PointF(xyRepository.getX(), xyRepository.getY()))
    val positionLiveData: LiveData<PointF> get() = _positionLiveData

    fun updateXY(x: Float, y: Float) {
        xyRepository.saveXY(x, y)
        _positionLiveData.value = PointF(x, y)
    }
}