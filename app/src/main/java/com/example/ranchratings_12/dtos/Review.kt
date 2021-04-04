package com.example.ranchratings_12.dtos

/**
 * Stores the review itself, detailing the user, location, institution and rating.
 */
data class Review (var userID: Int = 0, var institutionName: String = "", var latitude: String = "", var longitutde: String = "", var reviewText: String = "", var rating: Double = 0.0){
}