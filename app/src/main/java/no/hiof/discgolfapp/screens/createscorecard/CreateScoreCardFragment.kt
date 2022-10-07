package no.hiof.discgolfapp.screens.createscorecard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.databinding.FragmentCreateScoreCardBinding
import no.hiof.discgolfapp.model.Course

class CreateScoreCardFragment : Fragment() {
    private val args: CreateScoreCardFragmentArgs by navArgs()
    private var fragmentBinding: FragmentCreateScoreCardBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_score_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCreateScoreCardBinding.bind(view)
        fragmentBinding = binding

        // TODO: Should be replaced by CourseId, but requires change in Model class
        val course = Course.getCourses().find { it.name == args.courseName }
       //  val course = Course.getCourses()[args.courseNum]

        binding.createScoreCardCourseNameTextView.text = course?.name ?: resources.getString(R.string.no_course_selected)

    }

}