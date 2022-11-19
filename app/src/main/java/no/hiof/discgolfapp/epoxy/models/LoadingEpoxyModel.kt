package no.hiof.discgolfapp.epoxy.models

import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.databinding.CourseInfoLoadingBinding
import no.hiof.discgolfapp.helper.epoxy.ViewBindingKotlinModel

class LoadingEpoxyModel: ViewBindingKotlinModel<CourseInfoLoadingBinding>(R.layout.course_info_loading) {
    override fun CourseInfoLoadingBinding.bind() {
        // Have nothing to do here, just load
    }
}