package com.example.ranchratings_12.daos

import com.example.ranchratings_12.dtos.Institution
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IInstitutionDAO {
    @GET("/directoryLocation")
    fun getAllInstitutions(): Call<ArrayList<Institution>>

    @GET("/directoryLocation")
    fun getInstitutions(@Query("Combined_Name")institutionName:String) : Call<ArrayList<Institution>>


}