package no.hiof.discgolfapp.model

data class Disc(
    var playerId: String? = null,
    var name: String = "",
    var speed: Int? = null,
    var glide: Int? = null,
    var turn: Int? = null,
    var fade: Int? = null,
    var type: String? = null,
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
                     "PuPVED2kZ8W58jyJ0QflZqeWhrf2",
                     "Thunderbird",
                     9,
                     5,
                     0,
                     2,
                     "Distance Driver",
                     "Innova",
                     "Champion",
                     175,
                     "Red"
                 ),
                 Disc(
                     "PuPVED2kZ8W58jyJ0QflZqeWhrf2",
                     "Shryke",
                     13,
                     6,
                     -2,
                     2,
                     "Distance Driver",
                     "Innova",
                     "Star",
                     175,
                     "Yellow"
                 ),
                 Disc(
                     "PuPVED2kZ8W58jyJ0QflZqeWhrf2",
                     "Sidewinder",
                     9,
                     5,
                     -3,
                     1,
                     "Distance Driver",
                     "Innova",
                     "Champion",
                     174,
                     "Pink"
                 ),
                 Disc(
                     "PuPVED2kZ8W58jyJ0QflZqeWhrf2",
                     "D Model S",
                     13,
                     6,
                     0,
                     2,
                     "Distance Driver",
                     "Prodigy",
                     null,
                     174,
                     "Green"
                 ),
                 Disc(
                     "PuPVED2kZ8W58jyJ0QflZqeWhrf2",
                     "Leopard3",
                     7,
                     5,
                     -2,
                     1,
                     "Distance Driver",
                     "Innova",
                     "Star",
                     174,
                     "Pink"
                 ),
                 Disc("PuPVED2kZ8W58jyJ0QflZqeWhrf2", "Mako3", 5, 4, 0, 0, "Mid Range", "Innova", "Star", 174, "Yellow"),
                 Disc("PuPVED2kZ8W58jyJ0QflZqeWhrf2", "Berg", 1, 1, 0, 2, "Putter", "Kastaplast", "K3", null, "Pink"),
                 Disc("PuPVED2kZ8W58jyJ0QflZqeWhrf2", "Pure", 1, 1, 0, 2, "Putter", "Latitude64",null, null, "white"),
             ) as MutableList<Disc>
         }
     }
}