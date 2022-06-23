package com.bcsd.bcsd

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bcsd.bcsd.databinding.ActivityWriteBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class WriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWriteBinding
    private val viewModel: WriteViewModel by viewModels {
        WriteViewModelFactory((application as App).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView<ActivityWriteBinding?>(this, R.layout.activity_write)
                .apply {
                    lifecycleOwner = this@WriteActivity
                    vm = viewModel
                }

        viewModel.finish
            .flowWithLifecycle(lifecycle)
            .onEach { if (it) finish() }
            .launchIn(lifecycleScope)
    }
}