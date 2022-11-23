package no.hiof.discgolfapp.services.api.response.discgolfmetrix

import com.google.firebase.firestore.PropertyName

data class CourseCountryCodeResponse(
    @PropertyName("area")
    val Area: String? = null,
    @PropertyName("city")
    val City: String? = null,
    @PropertyName("countryCode")
    val CountryCode: String? = null,
    @PropertyName("enddate")
    val Enddate: Any? = null,
    @PropertyName("fullname")
    val Fullname: String? = null,
    @PropertyName("id")
    val ID: String? = null,
    @PropertyName("location")
    val Location: String? = null,
    @PropertyName("name")
    val Name: String? = null,
    @PropertyName("parentID")
    val ParentID: String? = null,
    @PropertyName("type")
    val Type: String? = null,
    @PropertyName("x")
    val X: String? = null,
    @PropertyName("y")
    val Y: String? = null
)