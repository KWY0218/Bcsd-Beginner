package com.bcsd.bcsd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bcsd.bcsd.databinding.ActivityMainBinding
import com.bcsd.bcsd.databinding.LapItemBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val lapList = mutableListOf<Pair<Int, String>>()
    private var time = 0
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainAdapter: LapListAdapter
    private lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdapter()
        onClickStartBtn()
        onClickStopBtn()
        onClickLapBtn()
    }

    private fun setTime() {
        if (time / 100 == 60) {
            time = 0
            val min = "%02d".format(Integer.parseInt(binding.mainMinTv.text.toString()) + 1)
            binding.mainMinTv.text = min
        }
        val milli = "%02d".format((time % 100))
        val sec = "%02d".format((time / 100))
        binding.mainMilliTv.text = milli
        binding.mainSecTv.text = sec
    }

    private fun onClickStartBtn() {
        with(binding) {
            mainStartBtn.setOnClickListener {
                mainStartBtn.isSelected = !mainStartBtn.isSelected
                if (mainStartBtn.isSelected) {
                    mainStartBtn.text = getString(R.string.main_pause)
                    job = lifecycleScope.launch {
                        while (true) {
                            time += 1
                            setTime()
                            delay(10)
                        }
                    }
                } else {
                    mainStartBtn.text = getString(R.string.main_start)
                    job.cancel()
                }
            }
        }
    }

    private fun onClickStopBtn() {
        with(binding) {
            mainStopBtn.setOnClickListener {
                job.cancel()
                mainStartBtn.isSelected = false
                mainStartBtn.text = getString(R.string.main_start)
                time = 0
                setTime()
                lapList.clear()
                mainAdapter.submitList(lapList.toList())
            }
        }
    }

    private fun onClickLapBtn() {
        with(binding) {
            mainLapBtn.setOnClickListener {
                if (mainStartBtn.isSelected) {
                    val lap = "${mainMinTv.text} : ${mainSecTv.text} . ${mainMilliTv.text}"
                    lapList.add(Pair(lapList.size, lap))
                    mainAdapter.submitList(lapList.toList())
                }
            }
        }
    }

    private fun initAdapter() {
        mainAdapter = LapListAdapter()
        binding.mainLapRv.adapter = mainAdapter
    }
}

class LapListAdapter :
    ListAdapter<Pair<Int, String>, LapListAdapter.LapViewHolder>(LAP_COMPARATOR) {

    class LapViewHolder(private val binding: LapItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(lap: Pair<Int, String>) {
            binding.lapItemTv.text = lap.second
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LapViewHolder {
        val view = LapItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LapViewHolder(view)
    }

    override fun onBindViewHolder(holder: LapViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    companion object {
        private val LAP_COMPARATOR = object : DiffUtil.ItemCallback<Pair<Int, String>>() {
            override fun areItemsTheSame(
                oldItem: Pair<Int, String>,
                newItem: Pair<Int, String>
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: Pair<Int, String>,
                newItem: Pair<Int, String>
            ): Boolean {
                return oldItem.first == newItem.first
            }
        }
    }
}