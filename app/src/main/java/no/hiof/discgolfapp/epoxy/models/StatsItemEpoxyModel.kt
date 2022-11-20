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
    val numberOfHoles: Int?
): ViewBindingKotlinModel<CourseInfoStatsBinding>(R.layout.course_info_stats) {

    override fun CourseInfoStatsBinding.bind() {

        val bestScoreComparedWithPar: Int? = bestScore?.minus(sumPar!!)
        val avgScoreComparedWithPar: Int? = avgScore?.minus(sumPar!!)
        val lastScoreComparedWithPar: Int? = lastScore?.minus(sumPar!!)

        //Individual stats:
        bestRoundInfoTextView.text = "Beste runde: ${bestScore} (${bestScoreComparedWithPar})"
        lastRoundInfoTextView.text = "Forrige runde: ${lastScore} (${lastScoreComparedWithPar})"
        averageScoreInfoTextView.text = "Gjennomsnitt: ${avgScore} (${avgScoreComparedWithPar})"

        //avgRatingOnCourseValue.text = "This needs a new value"

        // Course information
        parRatingTextView.text = if (courseRating != null) "Rating: ${String.format("%.1f",courseRating)}" else "Rating: -"
        parOnCourseTextView.text = "Par: ${sumPar}"
        numberOfHolesTextView.text = "Antall hull: ${numberOfHoles}"
    }
}