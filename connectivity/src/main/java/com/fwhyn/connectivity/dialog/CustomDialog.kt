/*
 * Created by   : Yana Wahyuna
 * Update Date  : 2022/06/27
 *
 * CustomDialog class is a common class for creating dialog that using DialogFragment to show it
 */
package com.fwhyn.connectivity.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

class CustomDialog : DialogFragment() {
    private var mTag: String? = null
    private lateinit var fragmentActivity: FragmentActivity
    private lateinit var builder: Builder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentActivity = requireActivity()
        mTag = tag

        /* get DialogCallback implementation from targeted activity */
        val dialogCallback: DialogCallback? = try {
            fragmentActivity as DialogCallback?
        } catch (e: ClassCastException) {
            /*
             * if ClassCastException occurred, it means there is no implements CustomDialog.DialogCallback
             * then set null mActivity because there is no ClickListener implementation
             */
            null
        }

        dialogCallback?.let {
            builder = it.onCreateDialog(mTag)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title: String? = builder.title
        val message: String? = builder.message
        val positiveText: String? = builder.positiveText
        val neutralText: String? = builder.neutralText
        val negativeText: String? = builder.negativeText
        val positiveButtonListener: ClickListener? = builder.positiveButtonListener
        val neutralButtonListener: ClickListener? = builder.neutralButtonListener
        val negativeButtonListener: ClickListener? = builder.negativeButtonListener
        @StyleRes val themeResId = builder.themeResId
        val isNotDefaultButtonStyle = builder.isNotDefaultButtonStyle
        val view: View? = builder.view
        val customTitle: View? = builder.customTitle

        /* create dialog using AlertDialog and LayoutInflater */
        val builder: AlertDialog.Builder = if (themeResId != 0) {
            AlertDialog.Builder(fragmentActivity, themeResId)
        } else {
            AlertDialog.Builder(fragmentActivity)
        }

        title?.let {
            builder.setTitle(title)
        }

        message?.let {
            builder.setMessage(message)
        }

        positiveText?.let {
            builder.setPositiveButton(positiveText) { _: DialogInterface?, which: Int ->
                positiveButtonListener?.onClickDialog(mTag, which)
            }
        }

        neutralText.let {
            builder.setNeutralButton(neutralText) { _: DialogInterface?, which: Int ->
                neutralButtonListener?.onClickDialog(mTag, which)
            }
        }

        negativeText.let {
            builder.setNegativeButton(negativeText) { _: DialogInterface?, which: Int ->
                negativeButtonListener?.onClickDialog(mTag, which)
            }
        }


        view?.let {
            builder.setView(view)
        }


        customTitle?.let {
            builder.setCustomTitle(customTitle)
        }

        val alertDialog = builder.create()
        // when button need to same as localize string, we need to disable AllCaps setting
        if (isNotDefaultButtonStyle) {
            alertDialog.setOnShowListener { dialog: DialogInterface ->
                (dialog as AlertDialog).run {
                    getButton(DialogInterface.BUTTON_POSITIVE).isAllCaps = false
                    getButton(DialogInterface.BUTTON_NEGATIVE).isAllCaps = false
                    getButton(DialogInterface.BUTTON_NEUTRAL).isAllCaps = false
                }
            }
        }

        return alertDialog
    }

    override fun onCancel(dialog: DialogInterface) {
        // called when dialog is showing, dialog cancelable is true, user taps outside dialog or presses back button
        val cancelListener: CancelListener? = try {
            fragmentActivity as CancelListener?
        } catch (e: ClassCastException) {
            /*
             * if ClassCastException occurred, it means there is no implements CustomDialog.ClickListener
             * then set null mActivity because there is no ClickListener implementation
             */
            null
        }
        cancelListener?.onCancelDialog(mTag)

        super.onCancel(dialog)
    }

    override fun show(manager: FragmentManager, tag: String?) {
        dismissDialogFragment(manager, tag)

        super.showNow(manager, tag)
    }

    // interface
    interface DialogCallback {
        fun onCreateDialog(tag: String?): Builder
    }

    interface ClickListener {
        fun onClickDialog(tag: String?, whichButton: Int)
    }

    interface CancelListener {
        fun onCancelDialog(tag: String?)
    }

    // inner class
    class Builder {
        var positiveButtonListener: ClickListener? = null
        var neutralButtonListener: ClickListener? = null
        var negativeButtonListener: ClickListener? = null

        var title: String? = null
        var message: String? = null
        var positiveText: String? = null
        var neutralText: String? = null
        var negativeText: String? = null

        @StyleRes
        var themeResId = 0
        var isNotDefaultButtonStyle = false
        var view: View? = null
        var customTitle: View? = null

        constructor()

        constructor(@StyleRes themeResId: Int) {
            this.themeResId = themeResId
        }

        fun setTitle(title: String?): Builder {
            this.title = title
            return this
        }

        fun setMessage(message: String?): Builder {
            this.message = message
            return this
        }

        fun setPositiveButton(positiveText: String?, listener: ClickListener?): Builder {
            this.positiveText = positiveText
            positiveButtonListener = listener
            return this
        }

        fun setNeutralButton(neutralText: String?, listener: ClickListener?): Builder {
            this.neutralText = neutralText
            neutralButtonListener = listener
            return this
        }

        fun setNegativeButton(negativeText: String?, listener: ClickListener?): Builder {
            this.negativeText = negativeText
            negativeButtonListener = listener
            return this
        }

        fun setNotDefaultButtonStyle(notDefaultButtonStyle: Boolean): Builder {
            isNotDefaultButtonStyle = notDefaultButtonStyle
            return this
        }

        fun setView(view: View): Builder {
            this.view = view
            return this
        }

        fun setCustomTitle(customTitle: View): Builder {
            this.customTitle = customTitle
            return this
        }
    }

    companion object {
        const val TAG = "CustomDialog"

        /**
         * delete previous fragment with current tag if it exists
         */
        fun dismissDialogFragment(manager: FragmentManager, tag: String?) {
            val previous = manager.findFragmentByTag(tag) as CustomDialog? ?: return
            previous.dialog ?: return
            previous.dismiss()
        }
    }
}