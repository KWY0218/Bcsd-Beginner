package com.bcsd.bcsd

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bcsd.bcsd.databinding.BoardFrameBinding

class BoardAdapter : ListAdapter<Board, BoardAdapter.BoardViewHolder>(BOARD_COMPARATOR) {

    class BoardViewHolder(
        private val binding: BoardFrameBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(board: Board) {
            binding.board = board
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val view = BoardFrameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BoardViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    companion object {
        private val BOARD_COMPARATOR = object : DiffUtil.ItemCallback<Board>() {
            override fun areItemsTheSame(oldItem: Board, newItem: Board): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Board, newItem: Board): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}