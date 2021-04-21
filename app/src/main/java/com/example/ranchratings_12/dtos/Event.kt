package com.example.ranchratings_12.dtos
//this is the DTO for events that will populate the veent details fragment
data class Event (var type: String = "", var date: String = "", var quantity: Double? = 0.0, var units : String = "", var description: String = ""){
}