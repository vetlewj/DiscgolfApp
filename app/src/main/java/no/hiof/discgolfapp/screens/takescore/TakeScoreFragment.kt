package no.hiof.discgolfapp.screens.takescore

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        val args = TakeScoreFragmentArgs.fromBundle(requireArguments())

        val course = Course.getCourses().find { it.name == args.courseName }

        val scoreCardType = ScoreCard.ScoreCardCreationType.valueOf(args.scoreCardType.uppercase())

        if (viewModel.scoreCard == null) {
            val playerId = firebaseAuth.currentUser?.uid;
            viewModel.scoreCard =
                course?.let { ScoreCard.createEmptyScoreCard(playerId, it, scoreCardType) }
            val docData = HashMap<String, Any>()
            val scoreCard = viewModel.scoreCard
            scoreCard?.course?.uid?.let { docData.put("courseId", it) }
            scoreCard?.playerId?.let { docData.put("playerId", it) }
            scoreCard?.scoreCardType?.let { docData.put("scoreCardType", it) }
            scoreCard?.id?.let { docData.put("id", it) }
            scoreCard?.holeScores?.let { docData.put("holeScores", it) }
            scoreCard?.score?.let { docData.put("score", it) }
            scoreCard?.par?.let { docData.put("par", it) }
            scoreCard?.date?.let { docData.put("date", it) }

            viewModel.scoreCard!!.id?.let { firestore.collection("scorecards").document(it).set(docData) }
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
            viewModel.score--
            binding.currentScoreForHole.text = viewModel.score.toString()
        }
        binding.nextHoleBtn.setOnClickListener {
            viewModel.score = binding.currentScoreForHole.text.toString().toInt()
            // TODO: Create method for adding holeScore to scoreCard where it checks if holeNumber is already in holeScores
            viewModel.scoreCard?.holeScores?.add(HoleScore(viewModel.holeNumber, viewModel.score, viewModel.par))
            // TODO: update par and score in firestore
            firestore.collection("scorecards").document(viewModel.scoreCard?.id.toString())
                .update("holeScores", viewModel.scoreCard?.holeScores)

            if (viewModel.holeNumber < (course?.holes?.size ?: 0)) {
                viewModel.holeNumber++
                viewModel.score = 0
                binding.currentHoleNumberTextView.text = viewModel.holeNumber.toString()
                binding.currentScoreForHole.text = viewModel.score.toString()
                binding.parForHoleTextView.text =
                    course?.holes?.get(viewModel.holeNumber - 1)?.par.toString()
                binding.distanceForCurrentHoleTextView.text =
                    course?.holes?.get(viewModel.holeNumber - 1)?.distance.toString()

            } else {
                val action = TakeScoreFragmentDirections.actionTakeScoreFragmentToScoreBoardFragment(viewModel.scoreCard?.id.toString())
                binding.root.findNavController().navigate(action)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}