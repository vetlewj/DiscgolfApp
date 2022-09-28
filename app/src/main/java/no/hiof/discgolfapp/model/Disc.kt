package no.hiof.discgolfapp.model

data class Disc (val manufacturer: String, val name: String, val speed: Int, val glide: Int, val turn: Int, val fade: Int, val type: DiscType, val plastic: String?, val weight: Int?, val color: String?) {
    enum class DiscType (val type: String) {
        PUTTER("Putter"),
        MID_RANGE("Mid Range"),
        FAIRWAY_DRIVER("Fairway Driver"),
        DISTANCE_DRIVER("Distance Driver")
    }
    companion object {
        fun getDiscs() : List<Disc> {
            return listOf(
                Disc("Innova", "Thunderbird", 9,5,0,2, DiscType.DISTANCE_DRIVER, "Champion", 175, "Red"),
                Disc("Innova", "Shryke", 13,6,-2,2, DiscType.DISTANCE_DRIVER, "Star", 175, "Yellow"),
                Disc("Innova", "Sidewinder", 9,5,-3,1, DiscType.DISTANCE_DRIVER, "Champion", 174, "Pink"),
                Disc("Prodigy", "D Model S", 13,6,0,2, DiscType.DISTANCE_DRIVER, null, 174, "Green"),
                Disc("Innova", "Leopard3", 7,5,-2,1, DiscType.FAIRWAY_DRIVER, "Star", 174, "Pink"),
                Disc("Innova", "Mako3", 5,4,0,0, DiscType.MID_RANGE, "Star", 174, "Yellow"),
                Disc("Kastaplast", "Berg", 1,1,0,2, DiscType.PUTTER, "K3", null, "Pink"),
                Disc("Latitude64", "Pure", 1,1,0,2, DiscType.PUTTER,  null, null, "white" ),
            )
        }
    }
}