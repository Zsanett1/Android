package com.example.activity.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.activity.model.AuthResponse
import com.example.activity.repository.AuthRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class AuthViewModel(application: Application) :
    AndroidViewModel(application) {
    private val authRepository = AuthRepository(application)
    private val _authResult = MutableLiveData<Result<AuthResponse>>()
    val authResult: LiveData<Result<AuthResponse>> = _authResult
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun login(email: String, password: String) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = authRepository.login(email, password)

                if (response.isSuccessful && response.body() != null) {
                    _authResult.postValue(Result.success(response.body()!!))
                } else {
                    _authResult.postValue(
                        Result.failure(Exception("Login failed: ${response.code()}"))
                    )
                }

            } catch (e: Exception) {
                _authResult.postValue(Result.failure(e))
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

}
