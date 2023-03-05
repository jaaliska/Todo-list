package com.example.todo_list.presentation.ui.base

import moxy.MvpView

interface BaseMvpView: MvpView {

    fun showProgressDialog()
    fun hideProgressDialog()
    fun showErrorToast(error: String)

}