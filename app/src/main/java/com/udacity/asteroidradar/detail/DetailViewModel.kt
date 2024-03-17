package com.udacity.asteroidradar.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailViewModel: ViewModel() {
    private val _displayExplanationDialog = MutableLiveData<Boolean>()
    val displayExplanationDialog: LiveData<Boolean>
        get() = _displayExplanationDialog

    fun onExplanationImageTouched(){
        _displayExplanationDialog.value = true
    }

    fun closeExplanationDialog(){
        _displayExplanationDialog.value = false
    }
}