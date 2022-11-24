package no.hiof.discgolfapp.model

import no.hiof.discgolfapp.helper.DistanceMeasure

class Hole(
    val holeNumber: Int = 0,
    val par: Int = 0,
    var distance: Int? = null,
    val startLat: Double? = null,
    val startLon: Double? = null,
    val endLat: Double? = null,
    val endLon: Double? = null,
    val unit: String? = null
) {
    companion object {
        fun getHoles(): ArrayList<Hole> {
            val holes = ArrayList<Hole>()
            holes.add(Hole(1, 3, 100, 59.130288, 11.342264, 59.130450, 11.341638, "m"))
            holes.add(Hole(2, 3, 100, 59.130420, 11.341160, 59.131252, 11.341034, "m"))
            holes.add(Hole(3, 3, 100, 59.131711, 11.341542, 59.132493, null, "m"))

            for (hole in holes) {
                if (hole.startLat != null && hole.startLon != null && hole.endLat != null && hole.endLon != null) {
                    hole.distance = DistanceMeasure.getDistanceToPositionInMeters(hole.startLat, hole.startLon, hole.endLat, hole.endLon)
                }
            }
            return holes
        }
    }
}
