package com.example.ranchratings_12.dtos

/**
 * Contains the review itself, storing the [userID], [institutionName],
 * location ([latitude] and [longitutde]), [reviewText] and [rating].
 */
data class Review (var reviewID : String = "", var userID: Int = 0, var institutionName: String = "", var latitude: String = "", var longitutde: String = "", var reviewText: String = "", var rating: Double = 0.0, var events : ArrayList<Event> = ArrayList<Event>()){
    override fun toString(): String {
        return "$institutionName $rating $latitude $longitutde"
    }
}