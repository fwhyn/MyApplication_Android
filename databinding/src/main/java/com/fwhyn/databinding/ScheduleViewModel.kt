package com.fwhyn.databinding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope

class ScheduleViewModel : ViewModel() {

//    private val _username = MutableStateFlow("init")
//    val username: StateFlow<String> = _username

    private val _username = MutableLiveData("init")
    val username: LiveData<String> = _username

    init {
//        viewModelScope.launch {
//            var tempString = ""
//
//            delay(1000)
//            tempString = "1 test"
//            _username.value = tempString
//            Log.d(TAG, tempString)
//
//            delay(1000)
//            tempString = "2 test"
//            _username.value = tempString
//            Log.d(TAG, tempString)
////
////            delay(1000)
////            tempString = "2 test"
////            _username.value = tempString
////            Log.d(TAG, tempString)
//        }
    }
}