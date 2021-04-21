package com.example.ranchratings_12.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
// view model used by the locational data functions
class LocationViewModel(application: Application): AndroidViewModel (application){
    private val locationLiveData = LocationLiveData(application)
    fun getLocationLiveData() = locationLiveData
}