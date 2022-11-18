package no.hiof.discgolfapp.model

data class Disc(
    var playerId: String? = null,
    var name: String = "",
    var speed: Int? = null,
    var glide: Int? = null,
    var turn: Int? = null,
    var fade: Int? = null,
    var type: DiscType ? = null,
    var manufacturer: String = "",
    var plastic: String? = "",
    var weight: Int? = null,
    var color: String? = ""
) {
    enum class DiscType(val type: String) {
        PUTTER("Putter"),
        MID_RANGE("Mid Range"),
        FAIRWAY_DRIVER("Fairway Driver"),
        DISTANCE_DRIVER("Distance Driver")
    }

    companion object {
         fun getDiscs(): MutableList<Disc> {
             return listOf(
                 Disc(
                     "111",
                     "Thunderbird",
                     9,
                     5,
                     0,
                     2,
                     DiscType.DISTANCE_DRIVER,
                     "Innova",
                     "Champion",
                     175,
                     "Red"
                 )/*,
                 Disc(
                     "222",
                     "Shryke",
                     13,
                     6,
                     -2,
                     2,
                     DiscType.DISTANCE_DRIVER,
                     "Innova",
                     "Star",
                     175,
                     "Yellow"
                 ),
                 Disc(
                     "222",
                     "Sidewinder",
                     9,
                     5,
                     -3,
                     1,
                     DiscType.DISTANCE_DRIVER,
                     "Innova",
                     "Champion",
                     174,
                     "Pink"
                 ),
                 Disc(
                     "222",
                     "D Model S",
                     13,
                     6,
                     0,
                     2,
                     DiscType.DISTANCE_DRIVER,
                     "Prodigy",
                     null,
                     174,
                     "Green"
                 ),
                 Disc(
                     "222",
                     "Leopard3",
                     7,
                     5,
                     -2,
                     1,
                     DiscType.FAIRWAY_DRIVER,
                     "Innova",
                     "Star",
                     174,
                     "Pink"
                 ),
                 Disc("222", "Mako3", 5, 4, 0, 0, DiscType.MID_RANGE, "Innova", "Star", 174, "Yellow"),
                 Disc("222", "Berg", 1, 1, 0, 2, DiscType.PUTTER, "Kastaplast", "K3", null, "Pink"),
                 Disc("222", "Pure", 1, 1, 0, 2, DiscType.PUTTER, "Latitude64",null, null, "white"),
             */) as MutableList<Disc>
         }
     }
}