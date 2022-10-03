package no.hiof.discgolfapp.model

import java.util.*


data class ScoreCard(
    val course: Course,
    val par: Int,
    val score: Int,
    val date: Date,
    val holeScores: List<HoleScore>
) {
    companion object{
        fun getScoreCards() : List<ScoreCard> {
            return listOf(ScoreCard(Course.getCourses().get(0), 54, 54, Date(), HoleScore.getHoleScores()))
        }
    }
}

