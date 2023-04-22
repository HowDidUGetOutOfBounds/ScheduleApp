package com.example.scheduleapp.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.scheduleapp.UI.ScheduleFragment
import com.example.scheduleapp.data.Constants.APP_PREFERENCE_DAY_OF_WEEK

class MainScreenAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return PAGE_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return ScheduleFragment.newInstance(position)
    }

    companion object {
        const val PAGE_COUNT = 15
    }
}