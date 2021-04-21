package com.example.ranchratings_12.ui.main

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ranchratings_12.dtos.Institution
import com.example.ranchratings_12.dtos.Review
import com.example.ranchratings_12.service.InstitutionService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class MainViewModel : ViewModel() {

//this serves a model of data via which the main fragment will derive some of its information from
    private var _institutions: MutableLiveData<ArrayList<Institution>> = MutableLiveData<ArrayList<Institution>>()
    var institutionService: InstitutionService = InstitutionService()
    private var _reviews: MutableLiveData<ArrayList<Review>> = MutableLiveData<ArrayList<Review>>()
    private var firestore : FirebaseFirestore = FirebaseFirestore.getInstance()


    init {
        fetchInstitutions("e")
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    fun fetchInstitutions(institutionName: String) {
        _institutions = institutionService.fetchInstitutions(institutionName)
    }



    var institutions:MutableLiveData<ArrayList<Institution>>
        get(){return _institutions}
        set(value) {_institutions = value}

    internal var reviews:MutableLiveData<ArrayList<Review>>
        get() {return _reviews}
        set(value) {_reviews = value}

    private fun listenToReviews(){
        firestore.collection("reviews").addSnapshotListener{
                snapshot, e ->
            //if there is an exception we want to skip
            if(e != null){
                Log.w(ContentValues.TAG, "Listen Failed", e)
                return@addSnapshotListener
            }

            if (snapshot != null){
                val allReviews = ArrayList<Review>()
                val documents = snapshot.documents
                documents.forEach{
                    val review = it.toObject(Review::class.java)
                    if(review != null){
                        allReviews.add(review!!)
                    }
                }
                _reviews.value = allReviews
            }
        }
    }
}