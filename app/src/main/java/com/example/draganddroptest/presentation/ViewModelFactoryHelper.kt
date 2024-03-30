package com.example.draganddroptest.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

fun <ViewModelType: ViewModel>viewModelFactory(init: () -> ViewModelType): ViewModelProvider.Factory =
    object: ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = init() as T
    }