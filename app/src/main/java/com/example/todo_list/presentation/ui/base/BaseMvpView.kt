package com.example.todo_list.presentation.ui.base

import moxy.MvpView
import moxy.viewstate.strategy.SingleStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(SingleStateStrategy::class)
interface BaseMvpView: MvpView {

    fun showProgressDialog()
    fun hideProgressDialog()
    fun showErrorToast(error: String? = null)

}