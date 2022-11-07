package no.hiof.discgolfapp.screens.courses

import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.databinding.FragmentCourseResultsBinding
import no.hiof.discgolfapp.model.Course
import no.hiof.discgolfapp.model.ScoreCard
import no.hiof.discgolfapp.services.SharedViewModel

class CourseResultsFragment : Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private var _binding: FragmentCourseResultsBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel = SharedViewModel()

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

        sharedViewModel.fetchCourse(args.courseId.toString())
        sharedViewModel.courseByIDLiveData.observe(viewLifecycleOwner) { course ->
            if (course == null) {
                Log.w("ChooseCourseFragment", "courses is null")
                return@observe
            }
            val storedScoreCards = firestore.collection("scorecardsv1")
                .whereEqualTo("playerId", firebaseAuth.currentUser?.uid)
                .whereEqualTo("finished", true)
                .whereEqualTo("courseId", args.courseId)
                .orderBy("date", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
            storedScoreCards.addOnSuccessListener { documents ->

                val scoreCards = documents.toObjects<ScoreCard>()

                val bestScore = scoreCards.minBy {
                    it.totalScore
                }
                binding.bestScoreTextView.text = resources.getString(
                    R.string.scoreboard_text_best_score,
                    bestScore.totalScore,
                    (bestScore.totalScore.minus(course.par ?: 0))
                )

                val avgScore = scoreCards.sumOf {
                    it.totalScore
                } / scoreCards.size
                binding.avgScoreTextView.text = resources.getString(
                    R.string.scoreboard_text_average_score,
                    avgScore,
                    (avgScore.minus(course.par ?: 0))
                )

                for (scoreCard in scoreCards) {
                    // TODO: Navigate to ScoreCard Details when clicking on a scorecard
                    val textView = TextView(context)
                    val formattedDate = DateFormat.format("dd.MM.yy", scoreCard.date)
                    textView.text =
                        resources.getString(
                            R.string.score_date,
                            scoreCard.totalScore,
                            formattedDate
                        )
                    binding.scoresLinearLayout.addView(textView)
                }
            }
        }

        return binding.root
    }

}