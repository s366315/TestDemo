package com.testdemo.viewmodel

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.testdemo.States
import com.testdemo.api.ApiService
import com.testdemo.model.UserModel
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.coroutines.CoroutineContext

class MainViewModelImpl : ViewModel(), KoinComponent {
    val data = MutableLiveData<States>()
    private val cache = arrayListOf<UserModel>()
    private val apiService: ApiService by inject()
    var recyclerState: Parcelable? = null
    private var itemsLoaded = 0
    private var pageSize = 0
    private var isLoading = false
    private var isEnd = false
    private var since = 0

    @SuppressLint("CheckResult")
    fun getUsers(forSize: Int) {

        if (cache.isNotEmpty() && forSize < itemsLoaded) {
            data.value = States.Success(cache)
            return
        }

        if (isLoading || isEnd) return

        if (cache.isEmpty()) data.value = States.Loading()

        isLoading = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = apiService.fetchData(since)

                result.also {
                    if (it.isEmpty() || it.size < pageSize) isEnd = true
                    pageSize = it.size
                    itemsLoaded += pageSize
                    isLoading = false

                    if (it.isNotEmpty()) {
                        since = it.last().id
                    }

                    cache.addAll(it)
                    data.postValue(States.Success(it))
                }
            } catch (e: Exception) {
                isEnd = true
                data.postValue(States.Error(e.message ?: ""))
            }
        }
    }
}