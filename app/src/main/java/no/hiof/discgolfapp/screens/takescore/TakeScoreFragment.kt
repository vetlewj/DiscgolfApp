package no.hiof.discgolfapp.screens.takescore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import no.hiof.discgolfapp.databinding.FragmentTakeScoreBinding
import no.hiof.discgolfapp.model.Course
import no.hiof.discgolfapp.model.ScoreCard

class TakeScoreFragment : Fragment() {

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
            // TODO: Change to use ScoreCardType
            viewModel.scoreCard = course?.let { ScoreCard.createEmptyScoreCard(it, scoreCardType) }
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
            if (viewModel.holeNumber < (course?.holes?.size ?: 0)) {
                viewModel.holeNumber++
                viewModel.score = 0
                binding.currentHoleNumberTextView.text = viewModel.holeNumber.toString()
                binding.currentScoreForHole.text = viewModel.score.toString()
                binding.parForHoleTextView.text = course?.holes?.get(viewModel.holeNumber - 1)?.par.toString()
                binding.distanceForCurrentHoleTextView.text = course?.holes?.get(viewModel.holeNumber - 1)?.distance.toString()
            }
            else{
                //TODO: Navigate to overview of round score
                binding.currentHoleNumberTextView.text = "Round over"
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}