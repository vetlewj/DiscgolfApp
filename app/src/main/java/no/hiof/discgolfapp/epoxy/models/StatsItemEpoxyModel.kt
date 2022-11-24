package no.hiof.discgolfapp.epoxy.models

import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.databinding.CourseInfoStatsBinding
import no.hiof.discgolfapp.helper.epoxy.ViewBindingKotlinModel


data class StatsItemEpoxyModel(
    val bestScore: Int?,
    val avgScore: Int?,
    val lastScore: Int?,
    val sumPar: Int?,
    val courseRating: Double?,
    val numberOfHoles: Int?,
    val distance: Int?
): ViewBindingKotlinModel<CourseInfoStatsBinding>(R.layout.course_info_stats) {

    override fun CourseInfoStatsBinding.bind() {

        val bestScoreComparedWithPar: Int? = bestScore?.minus(sumPar!!)
        val avgScoreComparedWithPar: Int? = avgScore?.minus(sumPar!!)
        val lastScoreComparedWithPar: Int? = lastScore?.minus(sumPar!!)

        //Individual stats:
        bestRoundInfoTextView.text = if(bestScore == 0 || bestScore == null) "Beste runde: -" else "Beste runde: ${bestScore} (${bestScoreComparedWithPar})"
        lastRoundInfoTextView.text = if(avgScore == 0 || bestScore == null) "Forrige runde: -"    else "Forrige runde: ${lastScore} (${lastScoreComparedWithPar})"
        averageScoreInfoTextView.text = if(lastScore == 0 || bestScore == null) "Gjennomsnitt: -"  else "Gjennomsnitt: ${avgScore} (${avgScoreComparedWithPar})"
        //avgRatingOnCourseValue.text = "This needs a new value"

        // Course information
        parRatingTextView.text = if (courseRating != null) "Rating: ${String.format("%.1f",courseRating)}" else "Rating: -"
        parOnCourseTextView.text = "Par: ${sumPar}"
        numberOfHolesTextView.text = "Antall hull: ${numberOfHoles}"
        courseDistance.text = if(distance == 0) "Distanse: -" else "Distanse: ${distance} m"
    }
}