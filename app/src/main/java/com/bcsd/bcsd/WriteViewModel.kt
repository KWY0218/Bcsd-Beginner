package com.bcsd.bcsd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WriteViewModel(private val repository: BoardRepository) : ViewModel() {
    private val _finish = MutableStateFlow(false)
    val finish = _finish.asStateFlow()

    val title = MutableStateFlow("")
    val writer = MutableStateFlow("")
    val content = MutableStateFlow("")

    fun writeBoard() {
        val currentDate: LocalDate = LocalDate.now()
        repository.addBoard(
            Board(
                id = repository.getSize(),
                title = title.value,
                writer = writer.value,
                content = content.value,
                date = currentDate.toString()
            )
        )
        finishActivity()
    }

    fun finishActivity() {
        viewModelScope.launch { _finish.emit(true) }
    }
}

class WriteViewModelFactory(private val repository: BoardRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WriteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WriteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}