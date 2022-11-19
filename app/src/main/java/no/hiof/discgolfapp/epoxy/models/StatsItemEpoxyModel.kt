package no.hiof.discgolfapp.epoxy.models

import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.databinding.CourseInfoStatsBinding
import no.hiof.discgolfapp.helper.epoxy.ViewBindingKotlinModel


data class StatsItemEpoxyModel(
    val bestScore: Int?,
    val avgScore: Int?,
    val lastScore: Int?
): ViewBindingKotlinModel<CourseInfoStatsBinding>(R.layout.course_info_stats) {

    override fun CourseInfoStatsBinding.bind() {
        bestRoundStatValueInfoTextView.text = bestScore.toString()
        averageValueInfoTextView.text = avgScore.toString()
        lastRoundValueInfoTextView.text = lastScore.toString()
    }
}