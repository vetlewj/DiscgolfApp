package no.hiof.discgolfapp.screens.courses

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.epoxy.controller.CourseInfoEpoxyController
import no.hiof.discgolfapp.databinding.FragmentCourseInfoBinding
import no.hiof.discgolfapp.helper.CourseType
import no.hiof.discgolfapp.services.SharedViewModel
import no.hiof.discgolfapp.services.StoredStatisticsViewModel


class CourseInfoFragment : Fragment() {
    private val args: CourseInfoFragmentArgs by navArgs()
    private var fragmentBinding: FragmentCourseInfoBinding? = null

    val viewModel: SharedViewModel by lazy {
        ViewModelProvider(this).get(SharedViewModel::class.java)
    }
    val viewModelStats: StoredStatisticsViewModel by lazy {
        ViewModelProvider(this).get(StoredStatisticsViewModel::class.java)
    }

    private val epoxyController = CourseInfoEpoxyController()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCourseInfoBinding.bind(view)
        fragmentBinding = binding

        epoxyController.fragment = this
        epoxyController.courseName = args.courseName

        viewModel.fetchWeather(
            String.format("%.4f", args.latitude),
            String.format("%.4f", args.longitude)
        )
        viewModel.weatherByCoordinatesLiveData.observe(viewLifecycleOwner) { weatherReport ->
            epoxyController.weatherResponse = weatherReport
            if (weatherReport == null) {
                Toast.makeText(
                    view.context,
                    "weather network call was unsuccessful",
                    Toast.LENGTH_SHORT
                ).show()
                return@observe
            }
        }
        if (args.type == CourseType.TYPE1_AND_TYPE2_WITH_NO_PARENT.type.toInt()) {

            viewModelStats.fetchCourseScoreCardsFromFireStore(args.uid)
            viewModelStats.scoreCards.observe(viewLifecycleOwner) { scoreCards ->
                if (scoreCards.isNullOrEmpty()) {
                    Log.d("CourseInfoFrag", "Could not retrieve scorecards")
                }
                epoxyController.bestScore = viewModelStats.getBestScoreForCourse(args.uid)
                epoxyController.avgScore = viewModelStats.getAvgScoreForCourse(args.uid)
                epoxyController.lastScore = viewModelStats.getLastScoreForCourse(args.uid)
            }
            viewModel.fetchAdditionalInfoFromCoursesWithSameParentID(
                "NO",
                args.uid,
                viewLifecycleOwner,
                requireContext()
            )
            viewModel.coursesByCountryCodeAndWithSameParentIDWithHoles.observe(viewLifecycleOwner) { listOfCoursesWithSameParentID ->
                if (listOfCoursesWithSameParentID.isNullOrEmpty()) {
                    Log.d("CourseInfoFrag", " the list of calues is null")
                    Toast.makeText(
                        view.context,
                        "course network call was unsuccessful",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@observe
                }

                epoxyController.listOfCoursesWithSameParentID = listOfCoursesWithSameParentID
                val listOfUidsOfCoursesWithSameParrentId: ArrayList<Int> = ArrayList()

                listOfCoursesWithSameParentID.forEach {
                    listOfUidsOfCoursesWithSameParrentId.add(it.uid)
                }

                viewModelStats.fetchScoreCourseListMap(listOfUidsOfCoursesWithSameParrentId)
                viewModelStats.childCoursesScoreCardsMap.observe(viewLifecycleOwner) { scoreCardsMap ->
                    if (scoreCardsMap.isNullOrEmpty()) {
                        Log.d("CourseInfoFrag", "Could not retrieve scorecards")
                    }
                    epoxyController.avgScoreMap =
                        viewModelStats.getAverageScoreMap(scoreCardsMap)
                    epoxyController.bestScoreMap =
                        viewModelStats.getBestScoreMap(scoreCardsMap)
                    epoxyController.lastScoreMap =
                        viewModelStats.getLastScoreMap(scoreCardsMap)
                }
            }

        } else {
            viewModelStats.fetchCourseScoreCardsFromFireStore(args.uid)
            viewModelStats.scoreCards.observe(viewLifecycleOwner) { scoreCards ->
                if (scoreCards.isNullOrEmpty()) {
                    Log.d("CourseInfoFrag", "Could not retrieve scorecards")
                }
                epoxyController.bestScore = viewModelStats.getBestScoreForCourse(args.uid)
                epoxyController.avgScore = viewModelStats.getAvgScoreForCourse(args.uid)
                epoxyController.lastScore = viewModelStats.getLastScoreForCourse(args.uid)
            }
            viewModel.fetchCourse(args.uid.toString(), requireContext())
            viewModel.courseByIDLiveData.observe(viewLifecycleOwner) { course ->
                epoxyController.courseResponse = course

                if (course == null) {
                    Toast.makeText(
                        view.context,
                        "course network call was unsuccessful",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@observe
                }
            }
        }

        val epoxyRecyclerView = binding.epoxyCourseInfoRecyclerView
        epoxyRecyclerView.setControllerAndBuildModels(epoxyController)

    }
}