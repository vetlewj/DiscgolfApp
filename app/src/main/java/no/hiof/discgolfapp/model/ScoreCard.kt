package no.hiof.discgolfapp.model

import java.util.*


class ScoreCard(
    val playerId: String? = null,
    val course: Course? = null,
    val par: Int? = null,
    val score: Int = 0,
    val date: Date = Date(),
    val holeScores: MutableList<HoleScore> = mutableListOf(),
    val scoreCardType: ScoreCardCreationType = ScoreCardCreationType.PAR,
    val id: String? = UUID.randomUUID().toString(),
    var totalScore: Int = 0,
    var totalPar: Int = 0,
    var finished: Boolean = false

) {
    //TODO: Implement custom class mapper to get course based on courseId from firestore
    // [CustomClassMapper]: No setter/field for courseId found on class no.hiof.discgolfapp.model.ScoreCard
    companion object {
        fun getScoreCards(): List<ScoreCard> {
            return listOf(
                ScoreCard(
                    null,
                    Course.getCourses()[0],
                    54,
                    54,
                    Date(),
                    HoleScore.getHoleScores().toMutableList()
                )
            )
        }

        fun createEmptyScoreCard(
            playerId: String?,
            course: Course,
            scoreCardCreationType: ScoreCardCreationType
        ): ScoreCard {
            val holeScores = mutableListOf<HoleScore>()

            if (scoreCardCreationType == ScoreCardCreationType.PAR) {
                val holeNum = 0
                for (hole in course.holes!!) {
                    if (hole != null) {
                        holeScores.plus(HoleScore(holeNum, 0, hole.par, null))
                    }
                    holeNum.plus(1)
                }
            }
            // TODO: Add rest of scorecard creation types
            else {
                for (hole in course.holes!!) {
                    if (hole != null) {
                        holeScores.plus(HoleScore(1, 0, 0, null))
                    }
                }
            }
            val par = holeScores.sumOf { it.par }

            if (playerId != null) {
                return ScoreCard(playerId, course, par, 0, Date(), holeScores)
            }

            return ScoreCard(null, course, par, 0, Date(), holeScores)
        }
    }

    fun addHoleScore(holeNumber: Int, score: Int, par: Int) {
        val holeScore = holeScores.find { it.holeNumber == holeNumber }
        if (holeScore != null) {
            holeScore.score = score
        } else {
            holeScores.add(HoleScore(holeNumber, score, par))
        }
    }

    fun getHoleScore(holeNumber: Int): HoleScore? {
        return holeScores.find { it.holeNumber == holeNumber }
    }

    enum class ScoreCardCreationType {
        PAR, PERSONAL_BEST, PERSONAL_AVERAGE, PERSONAL_WORST, PERSONAL_LAST, LEADERBOARD_RECORD, CUSTOM
    }

}

