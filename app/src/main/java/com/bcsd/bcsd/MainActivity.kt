package com.bcsd.bcsd

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bcsd.bcsd.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: BoardAdapter
    private val viewModel: MainViewModel by viewModels{
        MainViewModelFactory((application as App).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdapter()

        viewModel.boardList
            .flowWithLifecycle(lifecycle)
            .onEach(this::handleEvent)
            .launchIn(lifecycleScope)

        binding.mainWriteBtn.setOnClickListener { startActivity(Intent(this,WriteActivity::class.java)) }
    }

    private fun initAdapter() {
        adapter = BoardAdapter()
        binding.mainRv.adapter = adapter
    }

    private fun handleEvent(uiState: UiState) {
        when (uiState) {
            is UiState.Loading -> binding.mainLoadingLl.visibility = View.VISIBLE
            is UiState.Success -> {
                binding.mainLoadingLl.visibility = View.GONE
                adapter.submitList(uiState.data.toList())
            }
            is UiState.Fail -> {
                binding.mainLoadingLl.visibility = View.GONE
                Toast.makeText(this, "불러오기 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }
}