package com.testdemo


import com.testdemo.model.UserModel
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = OneExecutionStateStrategy::class)
interface MainFragmentView : MvpView {
    fun showError(message: String)
    fun onDataReceived(data: ArrayList<UserModel>)
    fun showLoading()
}