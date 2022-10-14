package no.hiof.discgolfapp.model

class Hole(
    val holeNumber: Int,
    val par: Int,
    val distance: Int?,
    val startLatitute: Double?,
    val startLongitude: Double?,
    val endLatitude: Double?,
    val endLongitude: Double?
) {
    companion object {
        fun getHoles(): ArrayList<Hole> {
            val holes = ArrayList<Hole>()
            holes.add(Hole(1, 3, 100, 59.0, 10.0, 59.0, 10.0))
            holes.add(Hole(2, 3, 100, 59.0, 10.0, 59.0, 10.0))
            holes.add(Hole(3, 3, 100, 59.0, 10.0, 59.0, 10.0))
            return holes
        }
    }
}
