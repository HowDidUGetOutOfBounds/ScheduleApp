package com.example.scheduleapp.UI

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.scheduleapp.R
import com.example.scheduleapp.UI.MainActivity.Companion.REQUEST_CODE_LOC_NOTIFICATION_PERMISSION
import com.example.scheduleapp.adapters.MainScreenAdapter
import com.example.scheduleapp.adapters.MainScreenAdapter.Companion.PAGE_COUNT
import com.example.scheduleapp.data.Constants.APP_PREFERENCES_GROUP
import com.example.scheduleapp.data.Constants.APP_PREFERENCES_SCHEDULE_VERSION
import com.example.scheduleapp.data.Constants.APP_TOAST_SCHEDULE_DOWNLOAD_FAILED
import com.example.scheduleapp.data.DownloadStatus
import com.example.scheduleapp.data.FlatScheduleDetailed
import com.example.scheduleapp.databinding.FragmentContainerBinding
import com.example.scheduleapp.viewmodels.MainActivityViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentContainer : Fragment() {
    private val viewModel: MainActivityViewModel by activityViewModels()
    private lateinit var mainScreenAdapter: MainScreenAdapter
    private lateinit var binding: FragmentContainerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentContainerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_CODE_LOC_NOTIFICATION_PERMISSION
            )
        }

        initObservers()
        viewModel.downloadSchedule()
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).title = viewModel.getPreference(
            APP_PREFERENCES_GROUP + "_" + viewModel.getUserEmail(),
            resources.getString(R.string.app_name)
        )
    }

    private fun setupViewPager2() {
        mainScreenAdapter = MainScreenAdapter(this)
        binding.fragmentViewPager2.adapter = mainScreenAdapter
        binding.fragmentViewPager2.currentItem = PAGE_COUNT / 2

        TabLayoutMediator(binding.tabLayout, binding.fragmentViewPager2) { tab, position ->
            tab.text = position.toString()
        }.attach()

        for (i in 0 until PAGE_COUNT) {
            binding.tabLayout.getTabAt(i)?.text = viewModel.getDayToTab(i)
        }
    }

    private fun initObservers() {
        viewModel.resetDownloadState(false)
        viewModel.scheduleDownloadState.observe(viewLifecycleOwner) { downloadStatus ->

            when (downloadStatus) {
                is DownloadStatus.Progress -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is DownloadStatus.WeakProgress -> {
                    Toast.makeText(
                        activity,
                        downloadStatus.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
                is DownloadStatus.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        activity,
                        "$APP_TOAST_SCHEDULE_DOWNLOAD_FAILED: ${downloadStatus.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
                is DownloadStatus.Success<FlatScheduleDetailed> -> {
                    binding.progressBar.visibility = View.GONE
                    viewModel.editPreferences(
                        APP_PREFERENCES_SCHEDULE_VERSION,
                        viewModel.getSchedule().version
                    )
                    setupViewPager2()
                }
                else -> {
                    throw IllegalStateException()
                }
            }
        }
    }

    fun reloadSchedule() {
        when (viewModel.scheduleDownloadState.value) {
            is DownloadStatus.Success, is DownloadStatus.Error -> {
                viewModel.downloadSchedule()
            }
            else -> {
                Log.d("TAG", "Not reloading current fragment because of an unfinished process.")
            }
        }
    }
}