package com.example.ranchratings_12.dtos

/**
 * Stores details on the restaurant or other institution reviewed.
 */
class Institution (var institutionID: Int = 0, val name: String, val streetAddress: String, val phoneNumber: String, var locationID: Int = 0) {
}