package no.hiof.discgolfapp.helper

import kotlin.math.*

class DistanceMeasure {
    companion object {
        fun getDistanceToPositionInMeters(
            lat1: Double,
            lon1: Double,
            lat2: Double,
            lon2: Double
        ): Int {
            // Based on Haversine formula and JS example: http://www.movable-type.co.uk/scripts/latlong.html
            val earthRadius = 6371000.0
            val deltaLat = Math.toRadians(lat2 - lat1)
            val deltaLon = Math.toRadians(lon2 - lon1)
            val a = sin(deltaLat / 2) * sin(deltaLat / 2) +
                    cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                    sin(deltaLon / 2) * sin(deltaLon / 2)
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))
            return (earthRadius * c).roundToInt()
        }

    }

}