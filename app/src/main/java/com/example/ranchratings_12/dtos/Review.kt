package com.example.ranchratings_12.dtos

data class Review (var userID: Int = 0, var institutionName: String = "", var latitude: String = "", var longitutde: String = "", var reviewText: String = "", var rating: Double = 0.0){
    override fun toString(): String {
        return "$institutionName $rating $latitude $longitutde"
    }
}