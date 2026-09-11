package com.example.appmovil_hu11.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.appmovil_hu11.data.CartUI
import com.example.appmovil_hu11.data.UserUI

sealed interface MainUiState {
    object Loading : MainUiState
    data class Success(
        val users: List<UserUI>,
        val carts: List<CartUI>
    ) : MainUiState
    object Error : MainUiState
}

class MainViewModel : ViewModel() {
    var uiState: MainUiState by mutableStateOf(MainUiState.Loading)
        private set

    // Pestaña seleccionada (0 = Usuarios HU11, 1 = Carritos HU12)
    var selectedTab by mutableStateOf(0)
        private set

    fun setTab(index: Int) {
        selectedTab = index
    }
}