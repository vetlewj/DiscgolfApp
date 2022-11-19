package no.hiof.discgolfapp.epoxy.models

import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.databinding.CourseInfoStatsBinding
import no.hiof.discgolfapp.helper.epoxy.ViewBindingKotlinModel


data class StatsItemEpoxyModel(
    val bestScore: Int?,
    val avgScore: Int?,
    val lastScore: Int?,
    val sumPar: Int?
): ViewBindingKotlinModel<CourseInfoStatsBinding>(R.layout.course_info_stats) {

    override fun CourseInfoStatsBinding.bind() {

        val bestScoreComparedWithPar: Int? = bestScore?.minus(sumPar!!)
        val avgScoreComparedWithPar: Int? = avgScore?.minus(sumPar!!)
        val lastScoreComparedWithPar: Int? = lastScore?.minus(sumPar!!)

        //Individual stats:
        bestRoundInfoTextView.text = "${bestScore} (${bestScoreComparedWithPar})"
        averageScoreInfoTextView.text = "${avgScore} (${avgScoreComparedWithPar})"
        lastRoundInfoTextView.text = "${lastScore} (${lastScoreComparedWithPar})"
        //avgRatingOnCourseValue.text = "This needs a new value"

        // Course information
        //parRatingTextView.text = if (rating != null) "Rating \n ${String.format("%.1f",rating)}" else null
        //parOnCourseTextView.text =
        //numberOfHolesTextView.text =
    }
}