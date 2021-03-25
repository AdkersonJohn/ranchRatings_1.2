package com.example.ranchratings_12.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ranchratings_12.dtos.Institution
import com.example.ranchratings_12.service.InstitutionService

class MainViewModel : ViewModel() {


    var institutions: MutableLiveData<ArrayList<Institution>> = MutableLiveData<ArrayList<Institution>>()
    var institutionService: InstitutionService = InstitutionService()

    init {
        fetchInstitutions("e")
    }

    fun fetchInstitutions(institutionName: String) {
        institutions = institutionService.fetchInstitutions(institutionName)
    }
    // TODO: Implement the ViewModel
}