package com.example.scheduleapp.UI

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduleapp.R
import com.example.scheduleapp.adapters.ScheduleRecyclerViewAdapter
import com.example.scheduleapp.data.TestScheduleData
import com.example.scheduleapp.databinding.FragmentScheduleBinding
import com.example.scheduleapp.viewmodels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleFragment : Fragment() {
    private val exampleViewModel: MainActivityViewModel by activityViewModels()
    private var layoutManager: RecyclerView.LayoutManager?=null
    private var adapter: RecyclerView.Adapter<ScheduleRecyclerViewAdapter.ItemViewHolder>?=null


    private lateinit var binding: FragmentScheduleBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentScheduleBinding.inflate(layoutInflater)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        val myScheduleTest = TestScheduleData().loadSchedule()
        Log.d("TAG", myScheduleTest.toString())

        super.onViewCreated(itemView, savedInstanceState)

        binding.schedulesRecyclerView.apply {
            layoutManager=LinearLayoutManager(activity)
            adapter=ScheduleRecyclerViewAdapter(myScheduleTest)
        }

     //   binding.schedulesRecyclerView.adapter = ScheduleRecyclerViewAdapter(myScheduleTest)
    }

    companion object {
        fun newInstance() = ScheduleFragment()
    }
}