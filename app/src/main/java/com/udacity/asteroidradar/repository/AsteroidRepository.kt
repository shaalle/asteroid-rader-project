package com.udacity.asteroidradar.repository

import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.asDomainModel
import com.udacity.asteroidradar.api.getSevenDay
import com.udacity.asteroidradar.api.getToday
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.db.AsteroidDatabase
import com.udacity.asteroidradar.models.Asteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import java.util.ArrayList

class AsteroidRepository(private val database: AsteroidDatabase) {

    suspend fun refreshAsteroids(
        startDate: String = getToday(),
        endDate: String = getSevenDay()
    ){
        var asteroidList: ArrayList<Asteroid>
        withContext(Dispatchers.IO) {
            val responseBody: ResponseBody = Network.service.getAsteroidsAsync(
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
            database.asteroidDao.deletePreviousDayAsteroids(getToday())
        }
    }

}