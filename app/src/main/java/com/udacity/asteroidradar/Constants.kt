package com.udacity.asteroidradar

import com.udacity.asteroidradar.api.Env

object Constants {
    const val IMAGE_MEDIA_TYPE = "image"
    const val API_QUERY_DATE_FORMAT = "yyyy-MM-dd"
    const val DEFAULT_END_DATE_DAYS = 7
    const val BASE_URL = "https://api.nasa.gov/"
    const val API_KEY = Env.NASA_API_KEY
    const val DATABASE_NAME = "asteroids"
    const val REFRESH_ASTEROIDS_WORK_NAME = "AsteroidRefreshDataWorker"
    const val DELETE_ASTEROIDS_WORK_NAME = "AsteroidDeleteDataWorker"
}