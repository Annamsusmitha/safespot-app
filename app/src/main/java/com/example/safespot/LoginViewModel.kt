package com.example.safespot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val userDao: UserDao) : ViewModel() {

    suspend fun authenticateUser(username: String, password: String): User? {
        return userDao.authenticate(username, password)
    }

    fun addUser(username: String, password: String, email: String?, fullName: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.insertUser(
                User(
                    username = username,
                    password = password,
                    email = email,
                    fullName = fullName
                )
            )
        }
    }
}

