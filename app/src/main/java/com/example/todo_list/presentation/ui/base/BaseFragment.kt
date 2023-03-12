package com.example.todo_list.presentation.ui.base

import android.widget.Toast
import com.example.todo_list.R
import com.example.todo_list.presentation.utils.ui_kit.ProgressDialog
import moxy.MvpAppCompatFragment

open class BaseFragment : MvpAppCompatFragment(), BaseMvpView {

    private val progressDialog = ProgressDialog()

    override fun showProgressDialog() {
        progressDialog.showProgress(requireContext())
    }

    override fun hideProgressDialog() {
        progressDialog.hideProgress()
    }

    override fun showErrorToast(error: String?) {
        val message = error ?: getString(R.string.something_went_wrong)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}