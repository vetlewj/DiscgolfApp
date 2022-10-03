package no.hiof.discgolfapp.model

data class HoleScore(
    val holeNumber: Int,
    val score: Int,
    val par: Int,
    val discThrows: List<Throw>
) {
    companion object {
        fun getHoleScores(): List<HoleScore> {
            return listOf(
                HoleScore(1, Throw.getThrows().size, 3, Throw.getThrows()),
                HoleScore(2, Throw.getThrows().size, 4, Throw.getThrows()),
                HoleScore(3, Throw.getThrows().size, 3, Throw.getThrows()),
            )
        }
    }
}
