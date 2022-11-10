package no.hiof.discgolfapp.screens.play.scoreboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.databinding.FragmentScoreBoardBinding
import no.hiof.discgolfapp.model.ScoreCard
import no.hiof.discgolfapp.services.StoredStatisticsViewModel


class ScoreBoardFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private var _binding: FragmentScoreBoardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ScoreBoardViewModel by viewModels()
    private val storedStatisticsViewModel: StoredStatisticsViewModel = StoredStatisticsViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScoreBoardBinding.inflate(inflater, container, false)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        val args = ScoreBoardFragmentArgs.fromBundle(
            requireArguments()
        )

        val scoreCardId = args.scoreCardId
        // TODO: Replace collection scorecardsv1 with scorecards when ready
        val storedCard =
            firestore.collection("scorecardsv1").document(scoreCardId).get()
        val layout = binding.scoreBoardLinearLayout
        storedCard.addOnSuccessListener { document ->
            if (document != null) {
                val scoreCard = document.toObject<ScoreCard>()
                viewModel.scoreCard = scoreCard
                for (holeScore in viewModel.scoreCard?.holeScores!!) {
                    val textView = TextView(context)
                    textView.text = resources.getString(
                        R.string.scoreboard_text,
                        holeScore.holeNumber,
                        holeScore.par,
                        holeScore.score
                    )
                    layout.addView(textView)
                }
                val totalPar = viewModel.scoreCard?.totalPar ?: 0
                val totalScore = viewModel.scoreCard?.totalScore ?: 0

                binding.totalScoreTextView.text = resources.getString(
                    R.string.scoreboard_text_total,
                    totalScore,
                    (totalScore.minus(totalPar))
                )
                binding.totalParTextView.text = resources.getString(
                    R.string.scoreboard_text_total_par,
                    totalPar
                )
            } else {
                println("Could not find scorecard")
            }
        }
        storedStatisticsViewModel.getCourseScoreCardsFromFireStore(args.courseId)
        storedStatisticsViewModel.scoreCards.observe(viewLifecycleOwner) {
            val bestScore =
                storedStatisticsViewModel.getBestScoreForCourse(args.courseId)

            binding.bestScoreTextView.text = resources.getString(
                R.string.scoreboard_text_best_score,
                bestScore, (bestScore.minus(viewModel.scoreCard?.course?.par ?: 0))
            )
            val avgScore =
                storedStatisticsViewModel.getAvgScoreForCourse(args.courseId)
            binding.avgScoreTextView.text = resources.getString(
                R.string.scoreboard_text_average_score,
                avgScore, (avgScore.minus(viewModel.scoreCard?.course?.par ?: 0))
            )
        }


        val continueButton = binding.finishScoreBoardButton
        continueButton.setOnClickListener {
            val action =
                ScoreBoardFragmentDirections.actionScoreBoardFragmentToCoursesOverviewListFragment()
            it.findNavController().navigate(action)
        }

        val seePrevResultsButton = binding.showPrevResultsScoreBoardButton
        seePrevResultsButton.setOnClickListener {
            val action =
                ScoreBoardFragmentDirections.actionScoreBoardFragmentToCourseResultsFragment(
                    args.courseId
                )
            it.findNavController().navigate(action)
        }
        return binding.root
    }
}