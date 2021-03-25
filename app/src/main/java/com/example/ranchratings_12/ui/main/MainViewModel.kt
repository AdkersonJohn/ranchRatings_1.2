package com.example.ranchratings_12.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ranchratings_12.dtos.Institution
import com.example.ranchratings_12.service.InstitutionService

class MainViewModel : ViewModel() {


    private var _institutions: MutableLiveData<ArrayList<Institution>> = MutableLiveData<ArrayList<Institution>>()
    var institutionService: InstitutionService = InstitutionService()

    init {
        fetchInstitutions("e")
    }

    fun fetchInstitutions(institutionName: String) {
        _institutions = institutionService.fetchInstitutions(institutionName)
    }

    var institutions:MutableLiveData<ArrayList<Institution>>
        get(){return _institutions}
        set(value) {_institutions = value}
}