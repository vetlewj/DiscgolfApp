package no.hiof.discgolfapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import no.hiof.discgolfapp.databinding.FragmentCourseInfoBinding
import no.hiof.discgolfapp.model.Weather


class CourseInfoFragment : Fragment() {
    private val args: CourseInfoFragmentArgs by navArgs()
    private var fragmentBinding: FragmentCourseInfoBinding? = null

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

         val weather = Weather.getWeatherFromCoordinate(args.latitude, args.longitude)

        binding.courseNameInfoTextView.text = args.courseName
        // Weather binding
        //TODO find out how to bind drawable dynamically and change m/s and C to string values in xml
        binding.weatherSymbolInfoImageView.setImageResource(weather.getWeatherSymbol())
        binding.windDirectionInfoImageView.setImageResource(weather.getWindDirectionSymbol())
        binding.temperatureTextView.text = "${weather.temperature.toString()} ºC"
        binding.windSpeedTextView.text = "${weather.windspeed.toString()} m/s"

        binding.createScoreCardInfobutton.setOnClickListener() {
            val navController = this.findNavController()

            val action = CourseInfoFragmentDirections.actionCourseInfoFragmentToCreateScoreCardFragment(args.courseName)

            navController.navigate(action)
        }




    }
}