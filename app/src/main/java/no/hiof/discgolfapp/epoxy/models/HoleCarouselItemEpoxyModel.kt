package no.hiof.discgolfapp.epoxy.models

import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.databinding.CourseInfoHolesBinding
import no.hiof.discgolfapp.helper.DistanceMeasure
import no.hiof.discgolfapp.helper.epoxy.ViewBindingKotlinModel
import no.hiof.discgolfapp.model.Hole

data class HoleCarouselItemEpoxyModel(
    val hole: Hole?,
): ViewBindingKotlinModel<CourseInfoHolesBinding>(R.layout.course_info_holes)  {

    override fun CourseInfoHolesBinding.bind() {

        val distance = if(hole!!.startLat != null && hole.startLon != null && hole.endLat != null && hole.endLon != null) {
            DistanceMeasure.getDistanceToPositionInMeters(
                hole.startLat!!.toDouble(),
                hole.startLon.toDouble(),
                hole.endLat.toDouble(),
                hole.endLon.toDouble()
            )
        } else {
            0
        }

        val holesDetailsSentence = if(hole.distance != null) {
            "Par ${hole.par} \n ${hole.distance} m"
        } else if (distance > 0) {
            "Par ${hole.par} \n ${distance} m"
        } else {
            "Par ${hole.par}"
        }

        holeNumberTextView.text = "Hull \n  ${hole.holeNumber.toString()}"
        holeDetailsTextView.text = holesDetailsSentence

    }
}