package com.example.scheduleapp.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.scheduleapp.UI.ScheduleFragment
import com.example.scheduleapp.UI.SettingsFragment

class MainScreenAdapter(fragment: Fragment):
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return PAGE_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return when(position)
        {
            0-> ScheduleFragment.newInstance()
            1-> SettingsFragment.newInstance()
            else -> ScheduleFragment.newInstance()
        }
    }

    companion object{
        const val PAGE_COUNT = 2
    }
}