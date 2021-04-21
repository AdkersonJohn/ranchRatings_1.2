package com.example.ranchratings_12.dtos

import java.util.*
/*//this is a DTO that outlines what attributes will be associated with a "photo" object*/
data class Photo (var localUri : String = "", var remoteUri : String = "", var description : String = "", var dateTaken : Date = Date(), var id : String = ""){
}