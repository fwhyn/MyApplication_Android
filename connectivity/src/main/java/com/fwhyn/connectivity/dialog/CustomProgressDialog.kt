package com.fwhyn.connectivity.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.fwhyn.connectivity.R

/**
 * This class provide progress bar which has lifecycle by DialogFragment
 * used instance for initiate class
 * showProgress for showing progressbar, and dismissProgress for hide progressbar
 */
class CustomProgressDialog : DialogFragment() {
    // Prevent Fragment already added exception
    private var isShowing = false

    /**
     * initiate layout class, theme, and attribute of DialogFragment
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setStyle(STYLE_NO_TITLE, 0)
        if (dialog != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.setCanceledOnTouchOutside(false)
            dialog!!.setCancelable(false)
            try {
                dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return inflater.inflate(R.layout.view_progress_bar, container)
    }

    /**
     * showing progress when dialog has not been initiate
     */
    fun showProgress() {
        if (dialog == null && !isShowing) {
            isShowing = true
            show(parentFragmentManager, javaClass.name)
        }
    }

    /**
     * hide progressbar when dialog is showing state
     */
    fun dismissProgress() {
        if (dialog != null && isShowing) {
            isShowing = false
            dialog!!.dismiss()
        }
    }

    companion object {
        private lateinit var INSTANCE: CustomProgressDialog

        /**
         * create singleton class
         */
        val instance: CustomProgressDialog
            get() {
                return INSTANCE
            }
    }
}