package com.mor.homeassignmenteat.buisness.datasource.cache.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mor.homeassignmenteat.buisness.datasource.cache.story.StoryDao
import javax.inject.Inject

class DeleteWorker (ctx: Context, params: WorkerParameters, dao: StoryDao) : CoroutineWorker(ctx, params) {
    private val TAG = "deleteDataWorker"

    private var _dao: StoryDao = dao

    override suspend fun doWork(): Result {
        Log.d(TAG, "doWork, start")

        val time = System.currentTimeMillis()
        _dao.deleteStoriesByTiming(time)

        return Result.success()
    }
}