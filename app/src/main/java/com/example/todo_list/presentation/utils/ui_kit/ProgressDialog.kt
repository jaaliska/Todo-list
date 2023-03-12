package com.example.todo_list.presentation.utils.ui_kit

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.example.todo_list.R

object ProgressDialog {
    private var dialog: Dialog? = null
    private var numberOfUsers: Int = 0

    fun showProgress(context: Context, cancelable: Boolean = false) {
        numberOfUsers += 1
        if (dialog != null) return
        dialog = createDialog(context, cancelable)
    }

    private fun createDialog(context: Context, cancelable: Boolean): Dialog {
        return Dialog(context).apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(R.layout.layout_progress_dialog)
            setCancelable(cancelable)
            setCanceledOnTouchOutside(cancelable)
            show()
        }
    }

    fun hideProgress() {
        numberOfUsers -= 1
        if (numberOfUsers == 0) {
            dialog?.run {
                dialog = null
                dismiss()
            }
        }
    }
}