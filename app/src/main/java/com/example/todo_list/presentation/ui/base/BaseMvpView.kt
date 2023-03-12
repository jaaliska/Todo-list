package com.example.todo_list.presentation.ui.base

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleTagStrategy
import moxy.viewstate.strategy.StateStrategyType
import moxy.viewstate.strategy.alias.OneExecution

interface BaseMvpView : MvpView {
    @StateStrategyType(value = AddToEndSingleTagStrategy::class, tag = "show_hide_progress")
    fun showProgressDialog()

    @StateStrategyType(value = AddToEndSingleTagStrategy::class, tag = "show_hide_progress")
    fun hideProgressDialog()

    @OneExecution
    fun showErrorToast(error: String? = null)

    @OneExecution
    fun showErrorToast(errorRes: Int)
}