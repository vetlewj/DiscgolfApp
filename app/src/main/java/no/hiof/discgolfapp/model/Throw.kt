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
            // TODO: Add sample values for throws
            return listOf(
                Throw(
                    Location("start").apply {
                        latitude = 59.00001
                        longitude = 10.00001
                    },
                    Location("end").apply {
                        latitude = 59.100101
                        longitude = 10.10001
                    },
                    Date()
                )
            )
        }

    }
}

