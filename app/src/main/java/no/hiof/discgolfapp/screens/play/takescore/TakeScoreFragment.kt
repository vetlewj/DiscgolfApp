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
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.databinding.FragmentTakeScoreBinding
import no.hiof.discgolfapp.helper.DistanceMeasure
import no.hiof.discgolfapp.model.Course
import no.hiof.discgolfapp.model.ScoreCard
import no.hiof.discgolfapp.services.SharedViewModel
import no.hiof.discgolfapp.services.StoredStatisticsViewModel

class TakeScoreFragment : Fragment() {

    private var firebaseAuth = FirebaseAuth.getInstance()
    private var firestore = FirebaseFirestore.getInstance()

    private var _binding: FragmentTakeScoreBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TakeScoreViewModel by viewModels()
    private val sharedViewModel: SharedViewModel = SharedViewModel()
    private val statisticsViewModel = StoredStatisticsViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTakeScoreBinding.inflate(inflater, container, false)

        val args =
            TakeScoreFragmentArgs.fromBundle(requireArguments())
        sharedViewModel.fetchCourse(args.courseId.toString(), requireContext())
        sharedViewModel.courseByIDLiveData.observe(viewLifecycleOwner) { course ->
            if (course == null) {
                Log.w("ChooseCourseFragment", "courses is null")
                return@observe
            }

            if (!args.scorecardId.isNullOrEmpty()) {
                viewModel.fetchScoreCard(args.scorecardId!!, requireContext())
                viewModel.storedScoreCard.observe(viewLifecycleOwner) { scorecard ->
                    viewModel.scoreCard = scorecard
                    viewModel.par = scorecard.getHoleScore(1)?.par ?: 0
                    viewModel.score = scorecard.getHoleScore(1)?.score ?: 0
                }
            } else {

                var scoreCardType =
                    ScoreCard.ScoreCardCreationType.valueOf(args.scoreCardType.uppercase())

                if (viewModel.scoreCard == null) {
                    val playerId = firebaseAuth.currentUser?.uid

                    if (scoreCardType == ScoreCard.ScoreCardCreationType.PAR) {
                        viewModel.scoreCard =
                            course.let {
                                ScoreCard.createEmptyScoreCard(
                                    playerId,
                                    it,
                                    scoreCardType
                                )
                            }
                        createScoreCardInFirestore(viewModel.scoreCard)
                    } else {
                        statisticsViewModel.fetchCourseScoreCardsFromFireStore(course.uid)
                        statisticsViewModel.scoreCards.observe(viewLifecycleOwner) { scoreCards ->
                            if (scoreCards == null) {
                                Log.w(
                                    "ChooseCourseFragment",
                                    "No existing scorecards found, creating based on par"
                                )
                                scoreCardType = ScoreCard.ScoreCardCreationType.PAR
                            }
                            if (scoreCardType == ScoreCard.ScoreCardCreationType.PERSONAL_BEST) {
                                val bestScoreCard = scoreCards.minByOrNull { it.totalScore }
                                viewModel.scoreCard = ScoreCard.createScoreCardFromHoleScoresAsPar(
                                    playerId,
                                    course,
                                    bestScoreCard?.holeScores ?: mutableListOf()
                                )
                                Log.d(
                                    "ChooseCourseFragment",
                                    "Creating scorecard based on personal best, id: ${bestScoreCard?.id}"
                                )
                            }
                            if (scoreCardType == ScoreCard.ScoreCardCreationType.PAR) {
                                viewModel.scoreCard =
                                    course.let {
                                        ScoreCard.createEmptyScoreCard(
                                            playerId,
                                            it,
                                            scoreCardType
                                        )
                                    }
                            }

                            createScoreCardInFirestore(viewModel.scoreCard)
                            Log.d("TakeScoreFragment", "Scorecard created in firestore")
                            loadLayout(course)
                        }
                    }
                }
            }
            loadLayout(course)
        }
        return binding.root
    }

    private fun createScoreCardInFirestore(scoreCard: ScoreCard?) {
        val docData = HashMap<String, Any>()
        scoreCard?.course?.let { docData.put("course", it) }
        scoreCard?.course?.uid?.let { docData.put("courseId", it) }
        scoreCard?.playerId?.let { docData.put("playerId", it) }
        scoreCard?.scoreCardType?.let { docData.put("scoreCardType", it) }
        scoreCard?.id?.let { docData.put("id", it) }
        scoreCard?.holeScores?.let { docData.put("holeScores", it) }
        scoreCard?.score?.let { docData.put("totalScore", it) }
        scoreCard?.par?.let { docData.put("totalPar", it) }
        scoreCard?.date?.let { docData.put("date", it) }
        docData["finished"] = false

        viewModel.scoreCard!!.id?.let {
            firestore.collection("scorecards").document(it).set(docData)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getDistance(course: Course, holeNumber: Int): Int {
        return course.holes?.get(holeNumber - 1)?.distance
            ?: DistanceMeasure.getDistanceToPositionInMeters(
                course.holes?.get(holeNumber - 1)?.startLat,
                course.holes?.get(holeNumber - 1)?.startLon,
                course.holes?.get(holeNumber - 1)?.endLat,
                course.holes?.get(holeNumber - 1)?.endLon
            )
    }

    private fun loadLayout(course: Course) {
        // TODO: Refactor this (use databinding)
        if (viewModel.holeNumber == 0) {
            viewModel.holeNumber = 1
        }
        viewModel.par = viewModel.scoreCard?.getHoleScore(viewModel.holeNumber)?.par ?: 0
        viewModel.score = viewModel.scoreCard?.score ?: 0
        viewModel.distance = getDistance(course, viewModel.holeNumber)

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
            viewModel.scoreCard?.addHoleScore(
                viewModel.holeNumber,
                viewModel.score,
                viewModel.par
            )
            viewModel.totalScore += viewModel.score
            viewModel.totalPar += viewModel.par

            firestore.collection("scorecards").document(viewModel.scoreCard?.id.toString())
                .update(
                    "holeScores", viewModel.scoreCard?.holeScores,
                    "totalPar", viewModel.totalPar,
                    "totalScore", viewModel.totalScore
                )

            if (viewModel.holeNumber < (course.holes?.size ?: 0)) {
                viewModel.holeNumber++
                val holeScore = viewModel.scoreCard?.getHoleScore(viewModel.holeNumber)
                if (holeScore != null) {
                    viewModel.score = holeScore.score
                } else {
                    viewModel.score = 0
                }

                viewModel.distance = getDistance(course, viewModel.holeNumber)
                viewModel.par = viewModel.scoreCard?.getHoleScore(viewModel.holeNumber)?.par ?: 0

                binding.currentHoleNumberTextView.text = viewModel.holeNumber.toString()
                binding.currentScoreForHole.text = viewModel.score.toString()
                binding.parForHoleTextView.text = viewModel.par.toString()
                binding.distanceForCurrentHoleTextView.text =
                    viewModel.distance.toString()
                if (viewModel.holeNumber == (course.holes?.size ?: 0)) {
                    binding.nextHoleBtn.text = getString(R.string.finish_round)
                }
                Log.d(
                    "TakeScoreFragment",
                    "scorecard updated ${viewModel.scoreCard?.holeScores}"
                )

            } else {
                firestore.collection("scorecards")
                    .document(viewModel.scoreCard?.id.toString())
                    .update(
                        "finished", true
                    )
                val action =
                    TakeScoreFragmentDirections.actionTakeScoreFragmentToScoreBoardFragment(
                        viewModel.scoreCard?.id.toString(),
                        viewModel.scoreCard?.course?.uid!!
                    )
                action.courseName = viewModel.scoreCard?.course?.name
                    ?: resources.getString(R.string.scoreboard_course_name_placeholder)
                Log.d(
                    "TakeScoreFragment",
                    "scorecard finished ${viewModel.scoreCard?.holeScores}"
                )
                binding.root.findNavController().navigate(action)
            }
        }

        binding.prevHoleBtn.setOnClickListener {
            if (viewModel.holeNumber > 1) {
                binding.nextHoleBtn.text = getString(R.string.next_hole)
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

                firestore.collection("scorecards")
                    .document(viewModel.scoreCard?.id.toString())
                    .update(
                        "holeScores", viewModel.scoreCard?.holeScores,
                        "par", viewModel.totalScore,
                        "score", viewModel.totalPar
                    )
                Log.d(
                    "TakeScoreFragment",
                    "scorecard updated ${viewModel.scoreCard?.holeScores}"
                )

                viewModel.holeNumber--
                val holeScore = viewModel.scoreCard?.getHoleScore(viewModel.holeNumber)
                if (holeScore != null) {
                    viewModel.score = holeScore.score
                } else {
                    viewModel.score = 0
                }

                viewModel.distance = getDistance(course, viewModel.holeNumber)
                viewModel.par = viewModel.scoreCard?.getHoleScore(viewModel.holeNumber)?.par ?: 0

                binding.currentHoleNumberTextView.text = viewModel.holeNumber.toString()
                binding.currentScoreForHole.text = viewModel.score.toString()
                binding.parForHoleTextView.text = viewModel.par.toString()
                binding.distanceForCurrentHoleTextView.text =
                    viewModel.distance.toString()
            }

        }
    }
}

