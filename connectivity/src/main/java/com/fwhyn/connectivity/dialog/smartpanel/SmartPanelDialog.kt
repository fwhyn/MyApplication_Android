package com.fwhyn.connectivity.dialog.smartpanel

import android.content.DialogInterface
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.fwhyn.connectivity.dialog.CustomDialog
import com.fwhyn.connectivity.dialog.CustomDialog.ClickListener

class SmartPanelDialog(private val mFragmentActivity: FragmentActivity) : ClickListener {
    fun createDialog(): CustomDialog.Builder {
        return CustomDialog.Builder()
            .setTitle("test title")
            .setMessage("test message")
            .setPositiveButton("Ok", this)
            .setNegativeButton("Cancel", this)
            .setNotDefaultButtonStyle(true)
    }

    override fun onClickDialog(tag: String?, whichButton: Int) {
        if (tag == DIALOG_GUIDE_TO_SMARTPANEL) {
            when (whichButton) {
                DialogInterface.BUTTON_POSITIVE ->
                    // send data to GA
                    Toast.makeText(mFragmentActivity, "Ok Clicked", Toast.LENGTH_SHORT).show()
                DialogInterface.BUTTON_NEGATIVE ->
                    // send data to GA
                    Toast.makeText(mFragmentActivity, "Cancel Clicked", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val DIALOG_GUIDE_TO_SMARTPANEL = "dialog_guide_to_smartpanel"
    }
}