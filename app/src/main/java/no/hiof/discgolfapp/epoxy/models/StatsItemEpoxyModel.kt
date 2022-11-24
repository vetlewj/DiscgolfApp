package no.hiof.discgolfapp.epoxy.models

import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.databinding.CourseInfoStatsBinding
import no.hiof.discgolfapp.helper.epoxy.ViewBindingKotlinModel
import no.hiof.discgolfapp.model.Course
import no.hiof.discgolfapp.services.StoredStatisticsViewModel


data class StatsItemEpoxyModel(
    val bestScore: Int?,
    val avgScore: Int?,
    val lastScore: Int?,
    val course: Course?
): ViewBindingKotlinModel<CourseInfoStatsBinding>(R.layout.course_info_stats) {

    override fun CourseInfoStatsBinding.bind() {

        val courseRating = course?.rating
        val sumPar = course?.par
        val numberOfHoles = course?.numberOfHoles
        val distance = course?.distance

        val avgRating = course?.let {
            if (avgScore != null) {
                StoredStatisticsViewModel().getRatingForRound(avgScore, it)
            }
        }

        val bestScoreComparedWithPar: Int? = bestScore?.minus(sumPar!!)
        val avgScoreComparedWithPar: Int? = avgScore?.minus(sumPar!!)
        val lastScoreComparedWithPar: Int? = lastScore?.minus(sumPar!!)

        //Individual stats:
        bestRoundInfoTextView.text = if(bestScore == 0 || bestScore == null) "Beste runde: -" else "Beste runde: ${bestScore} (${bestScoreComparedWithPar})"
        lastRoundInfoTextView.text = if(avgScore == 0 || bestScore == null) "Forrige runde: -"    else "Forrige runde: ${lastScore} (${lastScoreComparedWithPar})"
        averageScoreInfoTextView.text = if(lastScore == 0 || bestScore == null) "Gjennomsnitt: -"  else "Gjennomsnitt: ${avgScore} (${avgScoreComparedWithPar})"
        //avgRatingOnCourseValue.text = if(avgRating == 0 || bestScore == null) "Gjennomsnitt rating: -"  else "Gjennomsnitt rating: ${avgScore} (${avgScoreComparedWithPar})"

        // Course information
        parRatingTextView.text = if (courseRating != null) "Rating: ${String.format("%.1f",courseRating)}" else "Rating: -"
        parOnCourseTextView.text = if (sumPar != null) "Par: ${sumPar}" else "Par: -"
        numberOfHolesTextView.text = if (numberOfHoles != null) "Antall hull: ${numberOfHoles}" else "Antall hull: -"
        courseDistance.text = if(distance == 0 || distance == null) "Distanse: -" else "Distanse: ${distance} m"
    }
}