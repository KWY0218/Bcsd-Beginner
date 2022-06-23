package com.bcsd.bcsd

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: BoardRepository) : ViewModel() {
    private val _boardList: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val boardList = _boardList.asStateFlow()

    init {
        viewModelScope.launch { getBoardList() }
    }

    suspend fun getBoardList() {
        val data = repository.getBoardList()
        _boardList.emit(UiState.Loading)
        delay(1000)
        _boardList.emit(UiState.Success(data))
    }
}

class MainViewModelFactory(private val repository: BoardRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

sealed class UiState {
    object Loading : UiState()
    data class Success(val data: List<Board>) : UiState()
    object Fail : UiState()
}