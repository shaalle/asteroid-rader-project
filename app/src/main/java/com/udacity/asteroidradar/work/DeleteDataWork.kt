package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.db.AsteroidDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException

class DeleteDataWork(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): ListenableWorker.Result {
        val database = AsteroidDatabase.getDatabase(applicationContext)
        val repository = AsteroidRepository(database)

        return try {
            repository.deletePreviousDayAsteroids()
            ListenableWorker.Result.success()
        } catch (exception: HttpException) {
            ListenableWorker.Result.retry()
        }
    }

}