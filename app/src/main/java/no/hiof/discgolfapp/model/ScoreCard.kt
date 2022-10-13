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
        fun createEmptyScoreCard(course: Course, scoreCardCreationType: ScoreCardCreationType): ScoreCard {
            val holeScores = emptyList<HoleScore>()

            if (scoreCardCreationType == ScoreCardCreationType.PAR){
                val holeNum = 0
                for (hole in course.holes){
                    if (hole != null) {
                        holeScores.plus(HoleScore(holeNum, 0, hole.par, null))
                    }
                    holeNum.plus(1)
                }
            }
            // TODO: Add rest of scorecard creation types
            else{
                for (hole in course.holes){
                    if (hole != null) {
                        holeScores.plus(HoleScore(1, 0, 0, null))
                    }
                }
            }
            val par = holeScores.sumOf { it.par }

            return ScoreCard(course, par, 0, Date(), holeScores)
        }
    }

    enum class ScoreCardCreationType {
        PAR, PERSONAL_BEST, PERSONAL_AVERAGE, PERSONAL_WORST, PERSONAL_LAST, LEADERBOARD_RECORD, CUSTOM
    }

}

