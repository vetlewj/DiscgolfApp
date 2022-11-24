package no.hiof.discgolfapp.screens.courses

import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.divider.MaterialDivider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.databinding.FragmentCourseResultsBinding
import no.hiof.discgolfapp.services.SharedViewModel
import no.hiof.discgolfapp.services.StoredStatisticsViewModel

class CourseResultsFragment : Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private var _binding: FragmentCourseResultsBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel = SharedViewModel()
    private val storedStatisticsViewModel: StoredStatisticsViewModel = StoredStatisticsViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseResultsBinding.inflate(inflater, container, false)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val args = CourseResultsFragmentArgs.fromBundle(requireArguments())

        binding.proceedBtn.setOnClickListener {
            it.findNavController()
                .navigate(R.id.action_courseResultsFragment_to_coursesOverviewListFragment)
        }

        sharedViewModel.fetchCourse(args.courseId.toString(), requireContext())
        sharedViewModel.courseByIDLiveData.observe(viewLifecycleOwner) { course ->
            if (course == null) {
                Log.w("ChooseCourseFragment", "courses is null")
                return@observe
            }
            storedStatisticsViewModel.fetchCourseScoreCardsFromFireStore(course.uid)
            storedStatisticsViewModel.scoreCards.observe(viewLifecycleOwner) { scoreCards ->
                val bestScore = storedStatisticsViewModel.getBestScoreForCourse(args.courseId)
                val avgScore = storedStatisticsViewModel.getAvgScoreForCourse(args.courseId)

                binding.bestScoreTextView.text = resources.getString(
                    R.string.scoreboard_text_best_score,
                    bestScore,
                    (bestScore.minus(course.par ?: 0))
                )
                binding.avgScoreTextView.text = resources.getString(
                    R.string.scoreboard_text_average_score,
                    avgScore,
                    (avgScore.minus(course.par ?: 0))
                )
                val sortedScoreCards = scoreCards.sortedByDescending { it.date }

                for (scoreCard in sortedScoreCards) {
                    val textView = TextView(requireContext())
                    val formattedDate = DateFormat.format("dd.MM.yy HH:mm", scoreCard.date)
                    textView.text =
                        resources.getString(
                            R.string.score_date,
                            scoreCard.totalScore,
                            formattedDate
                        )
                    textView.setPadding(24)
                    textView.textSize = 18f
                    textView.setOnClickListener { view ->
                        Log.d("CourseResultsFragment", "ScoreCard ${scoreCard.id} clicked")
                        val action =
                            CourseResultsFragmentDirections.actionCourseResultsFragmentToScoreBoardFragment(
                                scoreCard.id!!,
                                course.uid
                            )
                        action.courseName = course.name
                        view.findNavController().navigate(
                            action
                        )
                    }
                    val divider = MaterialDivider(requireContext())
                    divider.dividerInsetEnd
                    divider.dividerInsetStart
                    binding.scoresLinearLayout.addView(textView)
                    binding.scoresLinearLayout.addView(divider)
                }
            }
        }


        return binding.root
    }

}