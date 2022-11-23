package no.hiof.discgolfapp.services.api.response.discgolfmetrix

import com.google.firebase.firestore.PropertyName

data class CourseIDResponse(
    @PropertyName("area")
    val Area: String? = null,
    @PropertyName("city")
    val City: String? = null,
    @PropertyName("countryCode")
    val CountryCode: String? = null,
    @PropertyName("enddate")
    val Enddate: String? = null,
    @PropertyName("fullname")
    val Fullname: String? = null,
    @PropertyName("uid")
    val ID: String? = null,
    @PropertyName("latitude")
    val Lat: String? = null,
    @PropertyName("longitude")
    val Lng: String? = null,
    @PropertyName("location")
    val Location: String? = null,
    @PropertyName("name")
    val Name: String? = null,
    @PropertyName("parentID")
    val ParentID: String? = null,
    @PropertyName("ratingResult1")
    val RatingResult1: String? = null,
    @PropertyName("ratingResult2")
    val RatingResult2: String? = null,
    @PropertyName("ratingValue1")
    val RatingValue1: String? = null,
    @PropertyName("ratingValue2")
    val RatingValue2: String? = null,
    @PropertyName("type")
    val Type: String? = null
)