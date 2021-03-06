package com.testdemo.presenter

import android.annotation.SuppressLint
import com.testdemo.MainFragmentView
import com.testdemo.api.ApiService
import com.testdemo.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenterScope
import org.koin.core.KoinComponent
import org.koin.core.inject

@InjectViewState
class MainPresenter : MvpPresenter<MainFragmentView>(), KoinComponent {
    private val cache = arrayListOf<UserModel>()
    private val apiService: ApiService by inject()
    private var itemsLoaded = 0
    private var pageSize = 0
    private var isLoading = false
    private var isEnd = false
    private var since = 0

    @SuppressLint("CheckResult")
    fun getUsers(offset: Int) {

        if (cache.isNotEmpty() && offset < itemsLoaded) {
            viewState.onDataReceived(cache)
            return
        }

        if (isLoading || isEnd) return

        if (cache.isEmpty()) viewState.showLoading()

        isLoading = true

        fetchUsers()
    }

    private fun fetchUsers() {
        presenterScope.launch(Dispatchers.IO) {
            try {
                val result = apiService.fetchData(since)

                if (result.isEmpty() || result.size < pageSize) isEnd = true
                pageSize = result.size
                itemsLoaded += pageSize
                isLoading = false

                if (result.isNotEmpty()) {
                    since = result.last().id
                }

                cache.addAll(result)
                launch(Dispatchers.Main) {
                    viewState.onDataReceived(ArrayList(result))
                }
            } catch (e: Exception) {
                isEnd = true
                launch(Dispatchers.Main) {
                    viewState.showError(e.message ?: "")
                }
            }
        }
    }
}