package com.example.ranchratings_12.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ranchratings_12.dtos.Institution
import com.example.ranchratings_12.service.InstitutionService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class MainViewModel : ViewModel() {


    private var _institutions: MutableLiveData<ArrayList<Institution>> = MutableLiveData<ArrayList<Institution>>()
    private var institutionService: InstitutionService = InstitutionService()
//    private lateinit var firestore : FirebaseFirestore

    init {
        fetchInstitutions("e")
//        firestore = FirebaseFirestore.getInstance()
//        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

    }

    fun fetchInstitutions(institutionName: String) {
        _institutions = institutionService.fetchInstitutions(institutionName)
    }

//    fun save(review: Any) {
//        firestore.collection("reviews")
//            .document()
//            .set(review)
//            .addOnSuccessListener {
//                Log.d("Firebase", "Document saved")
//            }
//            .addOnFailureListener{
//                Log.d( "Firebase", "Save Failed")
//            }
//    }

    var institutions:MutableLiveData<ArrayList<Institution>>
        get(){return _institutions}
        set(value) {_institutions = value}
}