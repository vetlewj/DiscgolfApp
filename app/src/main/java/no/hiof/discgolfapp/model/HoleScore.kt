package no.hiof.discgolfapp.model

data class HoleScore(
    val holeNumber: Int = 0,
    var score: Int = 0,
    val par: Int = 0,
    var discThrows: MutableList<Throw>? = mutableListOf()
) {
    companion object {
        fun getHoleScores(): List<HoleScore> {
            return listOf(
                HoleScore(1, Throw.getThrows().size, 3, Throw.getThrows().toMutableList()),
                HoleScore(2, Throw.getThrows().size, 4, Throw.getThrows().toMutableList()),
                HoleScore(3, Throw.getThrows().size, 3, Throw.getThrows().toMutableList()),
            )
        }
    }
}
