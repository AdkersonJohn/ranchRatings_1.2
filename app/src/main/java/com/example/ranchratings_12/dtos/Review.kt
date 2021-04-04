package com.example.ranchratings_12.dtos

/**
 * Stores the review itself, detailing the user, location, institution and rating.
 */
data class Review (var reviewID: Int = 0, var userID: Int = 0, var institutionID: Int = 0, var reviewText: String = "", var rating: Double = 0.0){
}