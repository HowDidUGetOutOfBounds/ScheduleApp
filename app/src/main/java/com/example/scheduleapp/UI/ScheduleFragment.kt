package com.example.scheduleapp.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scheduleapp.adapters.ScheduleRecyclerViewAdapter
import com.example.scheduleapp.databinding.FragmentScheduleBinding
import com.example.scheduleapp.viewmodels.ScheduleFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

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


        viewModel.getAll()
        initObservers()


        val args = arguments
        index = args?.let { it.getInt("index", 0) }
        index?.let { viewModel.getDayWithOffset(it) }
    }

    private fun initObservers() {
        viewModel.appGroupArray.observe(viewLifecycleOwner) { groupList ->
            //create progress bar
            if (groupList.size > 0) {
                if (index != null) {
                    val group = groupList[0]
                    scheduleRecyclerViewAdapter.differ.submitList(group.schedule?.get(index!!)?.dayschedule)
                    binding.apply {
                        schedulesRecyclerView.apply {
                            layoutManager = LinearLayoutManager(activity)
                            adapter = scheduleRecyclerViewAdapter
                        }
                    }
                }
            }
            //create progress bar
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