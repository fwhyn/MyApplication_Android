package com.fwhyn.view.databinding

import android.util.Log
import android.view.View
import android.widget.Toast
import com.fwhyn.view.databinding.Util.Companion.TAG

class Presenter {
    fun onSaveClick(view: View) {
        Log.d(TAG, "onSaveClick")
        Toast.makeText(view.context, "onSaveClick", Toast.LENGTH_SHORT).show()
    }
}