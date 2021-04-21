package com.example.ranchratings_12.service

import androidx.lifecycle.MutableLiveData
import com.example.ranchratings_12.RetrofitClientInstance
import com.example.ranchratings_12.daos.IInstitutionDAO
import com.example.ranchratings_12.dtos.Institution
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InstitutionService {
//this service class helps point our autocomplete text view to the necessary JSON file hosted on Github

    fun fetchInstitutions(institutionName: String): MutableLiveData<ArrayList<Institution>>{
        //create a array to hold the json data
        var institutions = MutableLiveData<ArrayList<Institution>>()
//call the retrofit to access target site hosting JSON
        val service = RetrofitClientInstance.retrofitInstance?.create(IInstitutionDAO::class.java)
        val call = service?.getAllInstitutions()
        call?.enqueue(object: Callback<ArrayList<Institution>>{
            override fun onFailure(call: Call<ArrayList<Institution>>, t: Throwable) {
                val j = 1 + 1
                val i = 1 + 1
            }

            override fun onResponse(
                call: Call<ArrayList<Institution>>,
                response: Response<ArrayList<Institution>>
            ) {
                institutions.value = response.body()
            }

        })



//returns arraylist populated with institution data
        return institutions
    }

}