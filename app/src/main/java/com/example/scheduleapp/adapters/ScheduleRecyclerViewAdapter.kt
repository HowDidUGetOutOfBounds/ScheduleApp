package com.example.scheduleapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduleapp.UI.ScheduleFragment
import com.example.scheduleapp.data.ScheduleTest
import com.example.scheduleapp.databinding.ItemScheduleBinding
import dagger.hilt.android.AndroidEntryPoint
import java.security.AccessControlContext


class ScheduleRecyclerViewAdapter(
    private val schedule: List<ScheduleTest>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var binding: ItemScheduleBinding


    class ItemViewHolder(private val binding: ItemScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setSchedule(pair: Int, discipline: String, cabinet: Int, teacher: String) {
            binding.pair.text = pair.toString()
            binding.discipline.text = discipline
            binding.cabinet.text = cabinet.toString()
            binding.teacher.text = teacher
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  ItemViewHolder(binding)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemViewHolder).setSchedule(
            schedule[position].pair,
            schedule[position].discipline,
            schedule[position].cabinet,
            schedule[position].teacher
        )
    }

    override fun getItemCount(): Int {
        return schedule.size
    }
}