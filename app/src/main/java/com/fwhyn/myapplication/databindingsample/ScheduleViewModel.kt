package com.fwhyn.myapplication.databindingsample

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fwhyn.myapplication.Util.Companion.TAG
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ScheduleViewModel : ViewModel() {

    private val _username = MutableStateFlow("init")
    val username: StateFlow<String> = _username

    init {
        viewModelScope.launch {
            var tempString = ""

//            delay(1000)
            tempString = "1 test"
            _username.value = tempString
            Log.d(TAG, tempString)

            delay(1000)
            tempString = "2 test"
            _username.value = tempString
            Log.d(TAG, tempString)
//
//            delay(1000)
//            tempString = "2 test"
//            _username.value = tempString
//            Log.d(TAG, tempString)
        }
    }
}