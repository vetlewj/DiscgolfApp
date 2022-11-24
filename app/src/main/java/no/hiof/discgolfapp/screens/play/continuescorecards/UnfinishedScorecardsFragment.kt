package no.hiof.discgolfapp.screens.play.continuescorecards

import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.databinding.FragmentCourseResultsBinding
import no.hiof.discgolfapp.databinding.FragmentUnfinishedScorecardsBinding
import no.hiof.discgolfapp.screens.courses.CourseResultsFragmentDirections

class UnfinishedScorecardsFragment : Fragment() {
    private var _binding: FragmentUnfinishedScorecardsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UnfinishedScorecardsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUnfinishedScorecardsBinding.inflate(inflater, container, false)

        viewModel.fetchUnfinishedScorecardsForCurrentPlayer()
        viewModel.scoreCards.observe(viewLifecycleOwner) { scorecards ->
            if (scorecards.isEmpty()) {
                Log.i("UnfinishedScorecards", "Could not find any unfinished scorecards")
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.could_not_find_unfinished_scorecards),
                    Toast.LENGTH_SHORT
                ).show()
            }
            for (scorecard in scorecards) {
                val textView = TextView(requireContext())
                val formattedDate = DateFormat.format("dd.MM.yy", scorecard.date)
                textView.text = buildString {
                    append(scorecard.course?.name)
                    append(", ")
                    append(formattedDate)
                }
                textView.setOnClickListener { view ->
                    Log.d("UnfinishedScorecards", "Scorecard ${scorecard.id} clicked")
                    val action = scorecard.courseId?.let {
                        UnfinishedScorecardsFragmentDirections.actionUnfinishedScorecardsFragmentToTakeScoreFragment(
                            it,
                        )
                    }
                    action?.scorecardId = scorecard.id
                    if (action != null) {
                        view.findNavController().navigate(action)
                    } else {
                        Log.w("UnfinishedScorecards", "Failed to navigate to take course")
                    }
                }
                binding.unfinishedScoreCardsLinear.addView(textView)
            }
        }
        return binding.root
    }

}