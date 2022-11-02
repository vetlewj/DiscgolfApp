package no.hiof.discgolfapp.screens.scoreboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.databinding.FragmentScoreBoardBinding
import no.hiof.discgolfapp.model.ScoreCard


class ScoreBoardFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private var _binding: FragmentScoreBoardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ScoreBoardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScoreBoardBinding.inflate(inflater, container, false)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        val args = ScoreBoardFragmentArgs.fromBundle(requireArguments())

        val scoreCardId = args.scoreCardId
        val storedCard =
            firestore.collection("scorecards").document(scoreCardId).get()
        val layout = binding.scoreBoardLinearLayout
        storedCard.addOnSuccessListener { document ->
            if (document != null) {
                val scoreCard = document.toObject<ScoreCard>()
                viewModel.scoreCard = scoreCard
                for (holeScore in viewModel.scoreCard?.holeScores!!) {
                    val textView = TextView(context)
                    textView.text = "Hole ${holeScore.holeNumber}: par: ${holeScore.par}, score: ${holeScore.score}"
                    layout.addView(textView)
                }
            } else {
                println("Could not find scorecard")
            }
        }

        val continueButton = binding.finishScoreBoardButton
        continueButton.setOnClickListener {
            val action = ScoreBoardFragmentDirections.actionScoreBoardFragmentToCoursesOverviewListFragment()
            it.findNavController().navigate(action)
        }
        return binding.root
    }
}