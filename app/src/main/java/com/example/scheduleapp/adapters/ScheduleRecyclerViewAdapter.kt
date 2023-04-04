package com.example.scheduleapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduleapp.data.ScheduleTest
import com.example.scheduleapp.databinding.ItemScheduleBinding


class ScheduleRecyclerViewAdapter(
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var binding: ItemScheduleBinding

    class ItemViewHolder(private val binding: ItemScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setSchedule(item: ScheduleTest) {
            binding.apply {
                pair.text = item.pair.toString()
                discipline.text = item.discipline
                cabinet.text = item.cabinet.toString()
                teacher.text = item.teacher
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemViewHolder).setSchedule(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val differCallback = object : DiffUtil.ItemCallback<ScheduleTest>() {
        override fun areItemsTheSame(oldItem: ScheduleTest, newItem: ScheduleTest): Boolean {
            return oldItem.pair == newItem.pair
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: ScheduleTest, newItem: ScheduleTest): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}