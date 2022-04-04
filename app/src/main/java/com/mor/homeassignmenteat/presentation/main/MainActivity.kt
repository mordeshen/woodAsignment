package com.mor.homeassignmenteat.presentation.main

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.mor.homeassignmenteat.R
import com.mor.homeassignmenteat.buisness.datasource.cache.worker.DeleteWorker
import com.mor.homeassignmenteat.buisness.datasource.network.main.ApiMainService
import dagger.hilt.android.AndroidEntryPoint
import com.mor.homeassignmenteat.presentation.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activateAutoDelete()
    }
    private fun activateAutoDelete() {
        val deleteWorker: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<DeleteWorker>(5, TimeUnit.SECONDS)
                .build()

        WorkManager.getInstance(this).enqueue(deleteWorker)
    }



    override fun displayProgressBar(isLoading: Boolean) {
        if (isLoading) {
            findViewById<ProgressBar>(R.id.progress_bar).visibility = View.VISIBLE
        } else {
            findViewById<ProgressBar>(R.id.progress_bar).visibility = View.GONE
        }
    }
}