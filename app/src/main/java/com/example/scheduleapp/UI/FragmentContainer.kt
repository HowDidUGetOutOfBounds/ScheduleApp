package com.example.scheduleapp.UI

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.scheduleapp.R
import com.example.scheduleapp.adapters.MainScreenAdapter
import com.example.scheduleapp.databinding.ActivityMainBinding
import com.example.scheduleapp.databinding.FragmentContainerBinding
import com.google.android.material.tabs.TabLayoutMediator

class FragmentContainer: Fragment() {
    lateinit var mPreferences: SharedPreferences
    private lateinit var mainScreenAdapter: MainScreenAdapter
    private lateinit var binding: FragmentContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mPreferences = (activity as MainActivity).mPreferences

        // Inflate the layout for this fragment
        binding = FragmentContainerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager2()
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).title = mPreferences.getString("APP_PREFERENCES_GROUP", resources.getString(R.string.app_name))
    }

    private fun setupViewPager2() {
        mainScreenAdapter = MainScreenAdapter(this)
        binding.fragmentViewPager2.adapter = mainScreenAdapter

        TabLayoutMediator(binding.tabLayout, binding.fragmentViewPager2){ tab, position ->
            tab.text = position.toString()
        }.attach()
    }

    companion object {
        fun newInstance() =
            FragmentContainer()
    }
}