package no.hiof.discgolfapp.screens.courses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.databinding.FragmentCourseInfoBinding
import no.hiof.discgolfapp.model.Weather
import no.hiof.discgolfapp.services.SharedViewModel


class CourseInfoFragment : Fragment() {
    private val args: no.hiof.discgolfapp.screens.courses.CourseInfoFragmentArgs by navArgs()
    private var fragmentBinding: FragmentCourseInfoBinding? = null

    val viewModel: SharedViewModel by lazy {
        ViewModelProvider(this).get(SharedViewModel::class.java)
    }

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

        viewModel.fetchCourse(args.uid.toString())
        viewModel.coursesByCountryCodeLiveData.observe(viewLifecycleOwner) { course ->
            if(course == null) {
                Toast.makeText(view.context, "course network call was unsuccessful", Toast.LENGTH_SHORT).show()
                return@observe
            }
            Toast.makeText(view.context, course.toString(), Toast.LENGTH_SHORT ).show()

        }
        viewModel.fetchWeather(String.format("%.4f",args.latitude), String.format("%.4f",args.longitude))
        viewModel.weatherByCoordinatesLiveData.observe(viewLifecycleOwner) { weatherReport ->
            if(weatherReport == null) {
                Toast.makeText(view.context, "weather network call was unsuccessful", Toast.LENGTH_SHORT).show()
                return@observe
            }
            Toast.makeText(view.context, weatherReport.properties.timeseries[0].data.next_1_hours!!.summary.toString(), Toast.LENGTH_SHORT).show()

//            //TODO find out how to bind drawable dynamically and change m/s and C to string values in xml
//            binding.weatherSymbolInfoImageView.setImageResource()
//            binding.windDirectionInfoImageView.setImageResource(weather.getWindDirectionDrawableFromWindDirection())
//            binding.temperatureTextView.text = "${weather.temperature.toString()} ºC"
//            binding.windSpeedTextView.text = "${weather.windspeed.toString()} m/s"

        }

        binding.courseNameInfoTextView.text = args.courseName

        binding.createScoreCardInfobutton.setOnClickListener() {
            val navController = this.findNavController()

            val action =
                CourseInfoFragmentDirections.actionCourseInfoFragmentToCreateScoreCardFragment(
                    args.courseName
                )

            navController.navigate(action)
        }




    }
}