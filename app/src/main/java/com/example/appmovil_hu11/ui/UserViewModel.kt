package com.example.appmovil_hu11.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmovil_hu11.data.AppRepository
import com.example.appmovil_hu11.data.UserUI
import kotlinx.coroutines.launch

sealed interface UserUiState {
    object Loading : UserUiState
    data class Success(val users: List<UserUI>) : UserUiState
    object Error : UserUiState
}

class UserViewModel(
    private val repository: AppRepository = AppRepository()
) : ViewModel() {

    var uiState: UserUiState by mutableStateOf(UserUiState.Loading)
        private set

    init {
        getUsers()
    }

    fun getUsers() {
        viewModelScope.launch {
            uiState = UserUiState.Loading
            uiState = try {
                val list = repository.getUsersUI()
                UserUiState.Success(list)
            } catch (e: Exception) {
                e.printStackTrace() // Para inspección en Logcat
                UserUiState.Error
            }
        }
    }
}