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

        bestRoundStatValueInfoTextView.text = "${bestScore} (${bestScoreComparedWithPar})"
        averageValueInfoTextView.text = "${avgScore} (${avgScoreComparedWithPar})"
        lastRoundValueInfoTextView.text = "${lastScore} (${lastScoreComparedWithPar})"
    }
}