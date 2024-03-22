package com.company.velogsharedviewmodeloncomposablescreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    private val _sharedState by mutableStateOf(0)
    var sharedState = _sharedState

    fun updateState() {
        sharedState  += 1
    }

}

