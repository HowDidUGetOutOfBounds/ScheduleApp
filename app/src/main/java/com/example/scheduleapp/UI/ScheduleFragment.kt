package com.example.scheduleapp.UI

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scheduleapp.adapters.ScheduleRecyclerViewAdapter

import com.example.scheduleapp.data.Day
import com.example.scheduleapp.data.DownloadStatus
import com.example.scheduleapp.data.Schedule
import com.example.scheduleapp.databinding.FragmentScheduleBinding
import com.example.scheduleapp.viewmodels.ScheduleFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleFragment() : Fragment() {
    var index: Int? = null
    private val scheduleRecyclerViewAdapter by lazy { ScheduleRecyclerViewAdapter() }
    private lateinit var binding: FragmentScheduleBinding
    val viewModel: ScheduleFragmentViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentScheduleBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        /*
        viewModel.getAll()
        initObservers()

        val args = arguments
        index = args?.getInt("index", 0)
    }

    private fun initObservers() {
        viewModel.downloadStatus.observe(viewLifecycleOwner) { downloadStatus ->

            when (downloadStatus) {
                is DownloadStatus.Progress -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is DownloadStatus.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(activity, "Failed to download Schedule", Toast.LENGTH_LONG)
                        .show()
                }
                is DownloadStatus.Success -> {
                    val groupList = downloadStatus.result
                    binding.progressBar.visibility = View.GONE
                    if (groupList.size > 0) {
                        if (index != null) {
                            val currentGroup = viewModel.getGroup(groupList)
                            if (currentGroup != null) {
                                val currentSchedule = viewModel.checkDate(index!!, currentGroup)
                                if (currentSchedule != null) {
                                    scheduleRecyclerViewAdapter.differ.submitList(currentSchedule.dayschedule)
                                    binding.apply {
                                        schedulesRecyclerView.apply {
                                            layoutManager = LinearLayoutManager(activity)
                                            adapter = scheduleRecyclerViewAdapter
                                        }
                                    }
                                }
                            }
    */

        val args = arguments
        index = args?.getInt("index", 0)

        if (index != null) {
            var currentGroup = viewModel.getGroup((parentFragment as FragmentContainer).getGroupList())
            if (currentGroup != null) {
                var currentSchedule = viewModel.checkDate(index!!, currentGroup)
                if (currentSchedule != null) {
                    scheduleRecyclerViewAdapter.differ.submitList(currentSchedule.dayschedule)
                    binding.apply {
                        schedulesRecyclerView.apply {
                            layoutManager = LinearLayoutManager(activity)
                            adapter = scheduleRecyclerViewAdapter
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance(position: Int): ScheduleFragment {
            val fragment = ScheduleFragment()
            val args = Bundle()
            args.putInt("index", position)
            fragment.arguments = args
            return fragment
        }
    }
}