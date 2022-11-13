package no.hiof.discgolfapp.screens.courses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.controller.CourseInfoEpoxyController
import no.hiof.discgolfapp.databinding.FragmentCourseInfoBinding
import no.hiof.discgolfapp.services.SharedViewModel


class CourseInfoFragment : Fragment() {
    private val args: CourseInfoFragmentArgs by navArgs()
    private var fragmentBinding: FragmentCourseInfoBinding? = null

    val viewModel: SharedViewModel by lazy {
        ViewModelProvider(this).get(SharedViewModel::class.java)
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

        if(args.type == 1) {
            viewModel.fetchAllType2ConnectedToType1Courses("NO", args.uid)
            // skaffed id, leter gjennom alle banene med den id
            // fetchAllType2ConnectedToType1Courses(args.uid) : returnerer en liste med alle baner med ekstra info
            //  deretter må jeg gjøre en del requests for å hente ut nødvendig informasjon basert på
            //fetch list of all
            // foreach i listen,
        //          gjør en viewModel.FetchCourse

        } else {
            viewModel.fetchCourse(args.uid.toString())
        }

        viewModel.fetchWeather(String.format("%.4f",args.latitude), String.format("%.4f",args.longitude))
        viewModel.courseByIDLiveData.observe(viewLifecycleOwner) { course ->
            epoxyController.courseResponse = course
            if(course == null) {
                Toast.makeText(view.context, "course network call was unsuccessful", Toast.LENGTH_SHORT).show()
                return@observe
            }
        }
        viewModel.weatherByCoordinatesLiveData.observe(viewLifecycleOwner) { weatherReport ->
            epoxyController.weatherResponse = weatherReport
            if(weatherReport == null) {
                Toast.makeText(view.context, "weather network call was unsuccessful", Toast.LENGTH_SHORT).show()
                return@observe
            }
        }

        val epoxyRecyclerView = binding.epoxyCourseInfoRecyclerView
        epoxyRecyclerView.setControllerAndBuildModels(epoxyController)

    }
}