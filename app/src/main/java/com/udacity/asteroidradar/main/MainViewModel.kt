package com.udacity.asteroidradar.main

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.api.getPictureOfDay
import com.udacity.asteroidradar.api.getTodaysDate
import com.udacity.asteroidradar.api.getWeekDate
import com.udacity.asteroidradar.db.AsteroidDB
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.models.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import java.lang.Exception

@RequiresApi(Build.VERSION_CODES.O)
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AsteroidDB.provideDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)

    private val _navigateToDetailFragment = MutableLiveData<Asteroid>()
    val navigateToDetailFragment: LiveData<Asteroid>
        get() = _navigateToDetailFragment

    private var _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    private val _displaySnackbarEvent = MutableLiveData<Boolean>()
    val displaySnackbarEvent: LiveData<Boolean>
        get() = _displaySnackbarEvent

    init {
        onViewWeekMenuClicked()
        viewModelScope.launch {
            try {
                asteroidRepository.refreshAsteroids()
                refreshPictureOfDay()
            } catch (e: Exception) {
                println("Exception refreshing data: $e.message")
                _displaySnackbarEvent.value = true
            }
        }
    }

    private suspend fun refreshPictureOfDay() {
        _pictureOfDay.value = getPictureOfDay()
    }

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToDetailFragment.value = asteroid
    }

    fun doneNavigating() {
        _navigateToDetailFragment.value = null
    }

    fun doneDisplayingSnackbar() {
        _displaySnackbarEvent.value = false
    }

    fun onViewWeekMenuClicked() {

        Log.d("Except", getTodaysDate())
        viewModelScope.launch {
            database.asteroidDao.getAsteroidsByCloseApproachDate(getTodaysDate(), getWeekDate())
                .collect { asteroids ->
                    _asteroids.value = asteroids
                }
        }
    }

    fun onTodayMenuClicked() {
        viewModelScope.launch {
            database.asteroidDao.getAsteroidsByCloseApproachDate(getTodaysDate(), getWeekDate())
                .collect { asteroids ->
                    _asteroids.value = asteroids
                }
        }
    }

    fun onSavedMenuClicked() {
        viewModelScope.launch {
            database.asteroidDao.getAllAsteroids().collect { asteroids ->
                _asteroids.value = asteroids
            }
        }
    }
}