package no.hiof.discgolfapp.model

import android.location.Location
import java.util.*

data class Throw(
    val start: Location,
    val end: Location,
    val dateTime: Date
) {
    companion object {
        fun fromLocation(startLocation: Location, endLocation: Location): Throw {
            return Throw(
                startLocation, endLocation, Date()
            )
        }

        fun getThrows(): List<Throw> {
            return listOf(
                Throw(
                    Location("start").apply {
                        latitude = 59.130195
                        longitude = 11.342336
                    },
                    Location("end").apply {
                        latitude = 59.130635
                        longitude = 11.342174
                    },
                    Date()
                ),
                Throw(
                    Location("start").apply {
                        latitude = 59.130635
                        longitude = 11.342174
                    },
                    Location("end").apply {
                        latitude = 59.130531
                        longitude = 11.341848
                    },
                    Date()
                ),
                Throw(
                    Location("start").apply {
                        latitude = 59.130531
                        longitude = 11.341848
                    },
                    Location("end").apply {
                        latitude = 59.130416
                        longitude = 11.341877
                    },
                    Date()
                )
            )
        }

    }
}

