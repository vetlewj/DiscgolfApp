package no.hiof.discgolfapp.screens.play.createscorecard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.databinding.FragmentCreateScoreCardBinding
import no.hiof.discgolfapp.helper.CourseType
import no.hiof.discgolfapp.model.Course
import no.hiof.discgolfapp.services.SharedViewModel

class CreateScoreCardFragment : Fragment() {
    private val args: CreateScoreCardFragmentArgs by navArgs()
    private var fragmentBinding: FragmentCreateScoreCardBinding? = null
    private var sharedViewModel = SharedViewModel()

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

        sharedViewModel.fetchCourses("NO", CourseType.TYPE2)
        sharedViewModel.coursesByCountryCodeLiveData.observe(viewLifecycleOwner) { courseList ->
            val course = courseList?.find { it.uid == args.courseId }

            binding.createScoreCardCourseNameTextView.text =
                course?.name ?: resources.getString(R.string.no_course_selected)

            val action =
                CreateScoreCardFragmentDirections.actionCreateScoreCardFragmentToTakeScoreFragment(
                    course?.uid ?: 0
                )
            binding.createScorecardBtn.setOnClickListener {
                Log.d("CreateScoreCardFragment", "Create scorecard button clicked")
                action.scoreCardType = "PAR"
                NavHostFragment.findNavController(this).navigate(action)
            }
            // TODO: Add other scorecard types for other buttons
            binding.createScorecardBtn2.setOnClickListener {
                Log.d("CreateScoreCardFragment", "Create scorecard button clicked")
                Toast.makeText(
                    view.context,
                    getString(R.string.not_implemented_yet),
                    Toast.LENGTH_SHORT
                ).show()
            }
            binding.createScorecardBtn3.setOnClickListener {
                Log.d("CreateScoreCardFragment", "Create scorecard button clicked")
                Toast.makeText(
                    view.context,
                    getString(R.string.not_implemented_yet),
                    Toast.LENGTH_SHORT
                ).show()
            }
            binding.createScorecardBtn4.setOnClickListener {
                Log.d("CreateScoreCardFragment", "Create scorecard button clicked")
                Toast.makeText(
                    view.context,
                    getString(R.string.not_implemented_yet),
                    Toast.LENGTH_SHORT
                ).show()
            }
            binding.createScorecardBtn5.setOnClickListener {
                Log.d("CreateScoreCardFragment", "Create scorecard button clicked")
                Toast.makeText(
                    view.context,
                    getString(R.string.not_implemented_yet),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }

}