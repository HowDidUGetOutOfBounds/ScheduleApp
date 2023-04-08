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
import com.example.scheduleapp.databinding.FragmentScheduleBinding
import com.example.scheduleapp.viewmodels.ScheduleFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ScheduleFragment(val data: Int) : Fragment() {
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

        val c = Calendar.getInstance()

        val date = c.get(Calendar.DATE)
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        Log.d("TAG", "Calendar $c")
        Log.d("TAG", "date $date")
        Log.d("TAG", "Year $year")
        Log.d("TAG", "month $month")
        Log.d("TAG", "day $day")



        viewModel.getAll().addOnCompleteListener { task ->
            viewModel.fillAll(task.result.value.toString())
            val group = viewModel.getGroup()
            Log.d("TAG", "sch priner ${group?.schedule?.get(data)?.dayschedule}")
            Log.d("TAG", "current group $group")
            scheduleRecyclerViewAdapter.differ.submitList(group?.schedule?.get(data)?.dayschedule)
            binding.apply {
                schedulesRecyclerView.apply {
                    layoutManager = LinearLayoutManager(activity)
                    adapter = scheduleRecyclerViewAdapter
                }
            }
        }


    }

    companion object {
        fun newInstance(position: Int): ScheduleFragment {
            return ScheduleFragment(position)
        }
    }
}