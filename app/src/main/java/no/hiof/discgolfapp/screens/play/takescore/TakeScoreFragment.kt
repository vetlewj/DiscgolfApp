package no.hiof.discgolfapp.screens.play.takescore

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import no.hiof.discgolfapp.databinding.FragmentTakeScoreBinding
import no.hiof.discgolfapp.model.Course
import no.hiof.discgolfapp.model.HoleScore
import no.hiof.discgolfapp.model.ScoreCard

class TakeScoreFragment : Fragment() {

    private var firebaseAuth = FirebaseAuth.getInstance()
    private var firestore = FirebaseFirestore.getInstance()

    private var _binding: FragmentTakeScoreBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TakeScoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTakeScoreBinding.inflate(inflater, container, false)

        val args =
            TakeScoreFragmentArgs.fromBundle(requireArguments())

        val course = Course.getCourses().find { it.name == args.courseName }

        val scoreCardType = ScoreCard.ScoreCardCreationType.valueOf(args.scoreCardType.uppercase())

        if (viewModel.scoreCard == null) {
            val playerId = firebaseAuth.currentUser?.uid
            viewModel.scoreCard =
                course?.let { ScoreCard.createEmptyScoreCard(playerId, it, scoreCardType) }
            val docData = HashMap<String, Any>()
            val scoreCard = viewModel.scoreCard
            scoreCard?.course?.uid?.let { docData.put("courseId", it) }
            scoreCard?.playerId?.let { docData.put("playerId", it) }
            scoreCard?.scoreCardType?.let { docData.put("scoreCardType", it) }
            scoreCard?.id?.let { docData.put("id", it) }
            scoreCard?.holeScores?.let { docData.put("holeScores", it) }
            scoreCard?.score?.let { docData.put("totalScore", it) }
            scoreCard?.par?.let { docData.put("totalPar", it) }
            scoreCard?.date?.let { docData.put("date", it) }

            viewModel.scoreCard!!.id?.let {
                firestore.collection("scorecardsv1").document(it).set(docData)
            }
            Log.d("TakeScoreFragment", "Scorecard created in firestore")
        }

        viewModel.par = course?.holes?.get(args.holeNumber - 1)?.par ?: 0
        viewModel.holeNumber = args.holeNumber
        viewModel.score = viewModel.scoreCard?.score ?: 0
        viewModel.distance = course?.holes?.get(args.holeNumber - 1)?.distance ?: 100

        binding.parForHoleTextView.text = viewModel.par.toString()
        binding.currentHoleNumberTextView.text = viewModel.holeNumber.toString()
        binding.currentScoreForHole.text = viewModel.score.toString()
        binding.distanceForCurrentHoleTextView.text = viewModel.distance.toString()

        binding.incrementScorebutton.setOnClickListener {
            viewModel.score++
            binding.currentScoreForHole.text = viewModel.score.toString()
        }
        binding.decrementScoreButton.setOnClickListener {
            if (viewModel.score > 0) {
                viewModel.score--
                binding.currentScoreForHole.text = viewModel.score.toString()
            }
        }
        binding.nextHoleBtn.setOnClickListener {
            viewModel.score = binding.currentScoreForHole.text.toString().toInt()
            viewModel.scoreCard?.addHoleScore(viewModel.holeNumber, viewModel.score, viewModel.par)
            viewModel.totalScore += viewModel.score
            viewModel.totalPar += viewModel.par

            firestore.collection("scorecardsv1").document(viewModel.scoreCard?.id.toString())
                .update(
                    "holeScores", viewModel.scoreCard?.holeScores,
                    "totalPar", viewModel.totalPar,
                    "totalScore", viewModel.totalScore
                )

            if (viewModel.holeNumber < (course?.holes?.size ?: 0)) {
                viewModel.holeNumber++
                val holeScore = viewModel.scoreCard?.getHoleScore(viewModel.holeNumber)
                if (holeScore != null) {
                    viewModel.score = holeScore.score
                } else {
                    viewModel.score = 0
                }
                binding.currentHoleNumberTextView.text = viewModel.holeNumber.toString()
                binding.currentScoreForHole.text = viewModel.score.toString()
                binding.parForHoleTextView.text =
                    course?.holes?.get(viewModel.holeNumber - 1)?.par.toString()
                binding.distanceForCurrentHoleTextView.text =
                    course?.holes?.get(viewModel.holeNumber - 1)?.distance.toString()

            } else {
                firestore.collection("scorecardsv1").document(viewModel.scoreCard?.id.toString())
                    .update(
                        "finished", true
                    )
                val action =
                    TakeScoreFragmentDirections.actionTakeScoreFragmentToScoreBoardFragment(
                        viewModel.scoreCard?.id.toString()
                    )
                binding.root.findNavController().navigate(action)
            }
        }
        binding.prevHoleBtn.setOnClickListener {
            if (viewModel.holeNumber > 1) {
                viewModel.score = binding.currentScoreForHole.text.toString().toInt()
                viewModel.scoreCard?.addHoleScore(
                    viewModel.holeNumber,
                    viewModel.score,
                    viewModel.par
                )
                viewModel.totalScore -= viewModel.scoreCard?.getHoleScore(viewModel.holeNumber - 1)?.score
                    ?: 0
                viewModel.totalPar -= viewModel.scoreCard?.getHoleScore(viewModel.holeNumber - 1)?.par
                    ?: 0

                firestore.collection("scorecardsv1").document(viewModel.scoreCard?.id.toString())
                    .update(
                        "holeScores", viewModel.scoreCard?.holeScores,
                        "par", viewModel.totalScore,
                        "score", viewModel.totalPar
                    )

                viewModel.holeNumber--
                val holeScore = viewModel.scoreCard?.getHoleScore(viewModel.holeNumber)
                if (holeScore != null) {
                    viewModel.score = holeScore.score
                } else {
                    viewModel.score = 0
                }
                binding.currentHoleNumberTextView.text = viewModel.holeNumber.toString()
                binding.currentScoreForHole.text = viewModel.score.toString()
                binding.parForHoleTextView.text =
                    course?.holes?.get(viewModel.holeNumber - 1)?.par.toString()
                binding.distanceForCurrentHoleTextView.text =
                    course?.holes?.get(viewModel.holeNumber - 1)?.distance.toString()
            }

        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}