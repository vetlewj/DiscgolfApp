package no.hiof.discgolfapp.model

import java.util.*


data class ScoreCard(
    val course: Course,
    val par: Int,
    val score: Int,
    val date: Date,
    val holeScores: ArrayList<HoleScore>
) {
    // TODO: Finish class
    //TODO: Add sample data
}

