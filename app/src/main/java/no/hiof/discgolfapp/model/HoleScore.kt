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
                // TODO: Add more sample data
            )
        }
    }
}
