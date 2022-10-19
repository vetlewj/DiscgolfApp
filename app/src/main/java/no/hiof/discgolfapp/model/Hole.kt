package no.hiof.discgolfapp.model

import no.hiof.discgolfapp.helper.DistanceMeasure

class Hole(
    val holeNumber: Int,
    val par: Int,
    var distance: Int?,
    val startLatitute: Double?,
    val startLongitude: Double?,
    val endLatitude: Double?,
    val endLongitude: Double?
) {
    companion object {
        fun getHoles(): ArrayList<Hole> {
            val holes = ArrayList<Hole>()
            holes.add(Hole(1, 3, 100, 59.130288, 11.342264, 59.130450, 11.341638))
            holes.add(Hole(2, 3, 100, 59.130420, 11.341160, 59.131252, 11.341034))
            holes.add(Hole(3, 3, 100, 59.131711, 11.341542, 59.132493, null))

            for (hole in holes) {
                if (hole.startLatitute != null && hole.startLongitude != null && hole.endLatitude != null && hole.endLongitude != null) {
                    hole.distance = DistanceMeasure.getDistanceToPositionInMeters(hole.startLatitute, hole.startLongitude, hole.endLatitude, hole.endLongitude)
                }
            }
            return holes
        }
    }
}
