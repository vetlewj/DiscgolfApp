package no.hiof.discgolfapp.epoxy.models

import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.databinding.CourseInfoHeaderBinding
import no.hiof.discgolfapp.helper.epoxy.ViewBindingKotlinModel

data class HeaderEpoxyModel(
    val courseName: String,
    val rating: Double?
): ViewBindingKotlinModel<CourseInfoHeaderBinding>(R.layout.course_info_header) {

    override fun CourseInfoHeaderBinding.bind() {
        courseNameInfoTextView.text = courseName
        //parRating.text = if (rating != null) "Rating \n ${String.format("%.1f",rating)}" else null
    }
}