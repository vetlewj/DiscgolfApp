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
    var finished: Boolean = false,
    var courseId: Int? = course?.uid,

) {
    //TODO: Implement custom class mapper to get course based on courseId from firestore
    // [CustomClassMapper]: No setter/field for courseId found on class no.hiof.discgolfapp.model.ScoreCard
    companion object {

        fun createEmptyScoreCard(
            playerId: String?,
            course: Course,
            scoreCardCreationType: ScoreCardCreationType
        ): ScoreCard {
            val holeScores = mutableListOf<HoleScore>()
            if (course.holes != null) {
                if (scoreCardCreationType == ScoreCardCreationType.PAR) {
                    for (hole in course.holes) {
                        if (hole != null) {
                            holeScores.add(HoleScore(hole.holeNumber, 0, hole.par, null))
                        }
                    }

                }
                // TODO: Add rest of scorecard creation types
                else {
                    for (hole in course.holes) {
                        if (hole != null) {
                            holeScores.plus(HoleScore(1, 0, 0, null))
                        }
                    }
                }
            }
            val par = holeScores.sumOf { it.par }

            if (playerId != null) {
                return ScoreCard(playerId, course, par, 0, Date(), holeScores)
            }

            return ScoreCard(null, course, par, 0, Date(), holeScores)
        }

        fun createScoreCardFromHoleScoresAsPar(
            playerId: String?,
            course: Course,
            holeScores: MutableList<HoleScore>
        ): ScoreCard {
            var par = 0
            if (playerId != null) {
                val holeScoresReset = mutableListOf<HoleScore>()
                for (holeScore in holeScores) {
                    holeScoresReset.add(HoleScore(holeScore.holeNumber, 0, holeScore.score, null))
                    par += holeScore.score
                }
                return ScoreCard(playerId, course, par, 0, Date(), holeScoresReset)
            }
            return createEmptyScoreCard(playerId ?: "0", course, ScoreCardCreationType.PAR)
        }

        fun createScoreCardFromScoreCardsList(
            playerId: String?,
            course: Course,
            playerScoreCards: MutableList<ScoreCard>,
            scoreCardCreationType: ScoreCardCreationType
        ): ScoreCard {
            val holeScores = mutableListOf<HoleScore>()
            val par = 0
            val score = 0

            if (course.holes != null) {
                val holeNum = 0
                for (hole in course.holes) {
                    if (hole != null) {
                        val holeScore = HoleScore(holeNum, 0, hole.par)
                        if (scoreCardCreationType == ScoreCardCreationType.PERSONAL_BEST) {
                            val bestScoreCard =
                                playerScoreCards.minByOrNull { it.holeScores[holeNum].score }
                            return createScoreCardFromHoleScoresAsPar(
                                playerId,
                                course,
                                bestScoreCard?.holeScores ?: holeScores
                            )
                        }
                        holeScores.plus(holeScore)
                    }
                    holeNum.plus(1)
                }
            }
            return ScoreCard(null, course, par, score, Date(), holeScores)
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
        PAR, PERSONAL_BEST, PERSONAL_AVERAGE, PERSONAL_WORST, PERSONAL_LAST, LEADERBOARD_RECORD, CUSTOM, PERSONAL_IDEAL
    }

}

