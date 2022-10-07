package no.hiof.discgolfapp.model

import java.util.*


class ScoreCard(
    val course: Course,
    val par: Int?,
    val score: Int,
    val date: Date,
    val holeScores: List<HoleScore>
) {
    companion object {
        fun getScoreCards(): List<ScoreCard> {
            return listOf(
                ScoreCard(
                    Course.getCourses().get(0),
                    54,
                    54,
                    Date(),
                    HoleScore.getHoleScores()
                )
            )
        }
        fun createScoreCard(course: Course, scoreCardCreationType: ScoreCardCreationType): ScoreCard {
            val holeScores = emptyList<HoleScore>()
            for (i in 1..9) {
                holeScores.plus(HoleScore(i, 0, 3, null))
            }

            var par = 0
            var score = 0
            for (holeScore in holeScores) {
                par += holeScore.par
                score += holeScore.score
            }
            return ScoreCard(course, par, score, Date(), holeScores)
        }
    }

    enum class ScoreCardCreationType {
        PAR, PERSONAL_BEST, PERSONAL_AVERAGE, PERSONAL_WORST, PERSONAL_LAST, LEADERBOARD_RECORD, CUSTOM
    }

}

