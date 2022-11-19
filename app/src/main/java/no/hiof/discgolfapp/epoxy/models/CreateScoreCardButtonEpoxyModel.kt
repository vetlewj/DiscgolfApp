package no.hiof.discgolfapp.epoxy.models

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.databinding.CourseInfoCreateScorecardButtonBinding
import no.hiof.discgolfapp.helper.epoxy.ViewBindingKotlinModel
import no.hiof.discgolfapp.screens.courses.CourseInfoFragmentDirections

data class CreateScoreCardButtonEpoxyModel(
    val context: Fragment?,
    val uid: Int
): ViewBindingKotlinModel<CourseInfoCreateScorecardButtonBinding>(R.layout.course_info_create_scorecard_button) {

    override fun CourseInfoCreateScorecardButtonBinding.bind() {
        createScoreCardInfobutton.setOnClickListener() {
            val navController = context!!.findNavController()

            val action =
                CourseInfoFragmentDirections.actionCourseInfoFragmentToCreateScoreCardFragment(
                    uid
                )

            navController.navigate(action)
        }
    }
}