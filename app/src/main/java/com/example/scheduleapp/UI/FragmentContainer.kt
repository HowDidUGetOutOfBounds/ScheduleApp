package com.example.scheduleapp.UI

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scheduleapp.R
import com.example.scheduleapp.adapters.MainScreenAdapter
import com.example.scheduleapp.data.Constants
import com.example.scheduleapp.data.DownloadStatus
import com.example.scheduleapp.data.Group
import com.example.scheduleapp.databinding.FragmentContainerBinding
import com.example.scheduleapp.viewmodels.MainActivityViewModel

import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentContainer: Fragment() {
    private val viewModel: MainActivityViewModel by activityViewModels()
    private lateinit var mainScreenAdapter: MainScreenAdapter
    private lateinit var binding: FragmentContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentContainerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.UpdateGroups()
        initObservers()
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).title = viewModel.getPreference(Constants.APP_PREFERENCES_GROUP+"_"+viewModel.getCurrentUser()?.email.toString(), resources.getString(R.string.app_name))
    }

    private fun setupViewPager2() {
        mainScreenAdapter = MainScreenAdapter(this)
        binding.fragmentViewPager2.adapter = mainScreenAdapter
        binding.fragmentViewPager2.currentItem = 2

        TabLayoutMediator(binding.tabLayout, binding.fragmentViewPager2){ tab, position ->
            tab.text = position.toString()
        }.attach()
    }

    private fun initObservers() {
        viewModel.downloadState.observe(viewLifecycleOwner) { downloadStatus ->

            when (downloadStatus) {
                is DownloadStatus.Progress -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is DownloadStatus.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(activity, "Failed to download Schedule: ${downloadStatus.message}", Toast.LENGTH_LONG)
                        .show()
                }
                is DownloadStatus.Success -> {
                    binding.progressBar.visibility = View.GONE
                    setupViewPager2()
                }
            }
        }
    }

    fun getGroupList(): ArrayList<Group> {
        return viewModel.getGroupList()
    }

    companion object {
        fun newInstance() =
            FragmentContainer()
    }
}