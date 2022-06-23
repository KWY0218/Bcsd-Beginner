package com.bcsd.bcsd

import kotlinx.coroutines.flow.MutableStateFlow

class BoardRepository {
    private val boardList: MutableStateFlow<MutableList<Board>> = MutableStateFlow(mutableListOf())

    fun getBoardList() = boardList.value

    fun addBoard(board: Board) {
        boardList.value.add(board)
    }

    fun getSize() = boardList.value.size
}