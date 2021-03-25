package com.example.ranchratings_12.service

import androidx.lifecycle.MutableLiveData
import com.example.ranchratings_12.RetrofitClientInstance
import com.example.ranchratings_12.daos.IInstitutionDAO
import com.example.ranchratings_12.dtos.Institution
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InstitutionService {


    fun fetchInstitutions(institutionName: String): MutableLiveData<ArrayList<Institution>>{
        var _institutions = MutableLiveData<ArrayList<Institution>>()

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
                _institutions.value = response.body()
            }

        })




        return _institutions
    }

}