package com.luffy1705.gym.ui.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SplashViewModel : ViewModel() {

    var showSplash by mutableStateOf(true)


    fun onAnimationFinished() {
        showSplash = false
    }
}