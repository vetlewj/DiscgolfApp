package no.hiof.discgolfapp.services.api.response.discgolfmetrix

import com.google.firebase.firestore.PropertyName

data class Basket(
    @PropertyName("basketLat")
    val BasketLat: String? = null,
    @PropertyName("basketLng")
    val BasketLng: String? = null,
    @PropertyName("length")
    val Length: String? = null,
    @PropertyName("number")
    val Number: String? = null,
    @PropertyName("numberAlt")
    val NumberAlt: Any? = null,
    @PropertyName("par")
    val Par: String? = null,
    @PropertyName("teeLat")
    val TeeLat: String? = null,
    @PropertyName("teeLng")
    val TeeLng: String? = null,
    @PropertyName("unit")
    val Unit: String? = null
)