package com.udacity.asteroidradar.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.AsteroidClient
import com.udacity.asteroidradar.api.asDomainModel
import com.udacity.asteroidradar.api.getTodaysDate
import com.udacity.asteroidradar.api.getWeekDate
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.db.AsteroidDB
import com.udacity.asteroidradar.models.Asteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import java.util.ArrayList

class AsteroidRepository(private val database: AsteroidDB) {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun refreshAsteroids(
        startDate: String = getTodaysDate(),
        endDate: String = getWeekDate()
    ){
        var asteroidList: ArrayList<Asteroid>
        withContext(Dispatchers.IO) {
            val responseBody: ResponseBody = AsteroidClient.service.getAsteroids(
                startDate, endDate,
                Constants.API_KEY
            )
                .await()
            asteroidList = parseAsteroidsJsonResult(JSONObject(responseBody.string()))
            database.asteroidDao.insertAll(*asteroidList.asDomainModel())
        }

    }

    suspend fun deletePreviousDayAsteroids() {
        withContext(Dispatchers.IO) {
            database.asteroidDao.deletePreviousDayAsteroids(getTodaysDate())
        }
    }

}