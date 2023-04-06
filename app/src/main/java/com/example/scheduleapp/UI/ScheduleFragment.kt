package com.example.scheduleapp.UI

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scheduleapp.adapters.ScheduleRecyclerViewAdapter
import com.example.scheduleapp.data.Day
import com.example.scheduleapp.data.GroupArray
import com.example.scheduleapp.data.TestScheduleData
import com.example.scheduleapp.databinding.FragmentScheduleBinding
import com.example.scheduleapp.viewmodels.ScheduleFragmentViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleFragment : Fragment() {
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
        var groups: ArrayList<Day>
        viewModel.getAll().addOnCompleteListener { task ->
            viewModel.fillAll(task.result.value.toString())

            val group = viewModel.getGroup()

//            scheduleRecyclerViewAdapter.differ.submitList(TestScheduleData().loadSchedule())

            Log.d("TAG", "sch priner ${group?.schedule?.get(pos!!)?.dayschedule}")
            Log.d("TAG", "current group $group")
            scheduleRecyclerViewAdapter.differ.submitList(group?.schedule?.get(pos!!)?.dayschedule)
            binding.apply {
                schedulesRecyclerView.apply {
                    layoutManager = LinearLayoutManager(activity)
                    adapter = scheduleRecyclerViewAdapter
                }
            }
        }
    }

    companion object {
        var pos: Int? = null
        fun newInstance(position: Int): ScheduleFragment {
            pos=position
            return ScheduleFragment()
        }
    }
}