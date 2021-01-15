package com.testdemo

import com.testdemo.model.UserModel

sealed class States {
    class Success(val users: List<UserModel>) : States()
    class Loading : States()
    class Error(val message: String) : States()
}