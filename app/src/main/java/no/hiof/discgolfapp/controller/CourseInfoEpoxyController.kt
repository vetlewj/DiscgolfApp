package no.hiof.discgolfapp.controller

import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.airbnb.epoxy.EpoxyController
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.databinding.CourseInfoCreateScorecardButtonBinding
import no.hiof.discgolfapp.databinding.CourseInfoHeaderBinding
import no.hiof.discgolfapp.databinding.CourseInfoLoadingBinding
import no.hiof.discgolfapp.databinding.CourseInfoWeatherBinding
import no.hiof.discgolfapp.helper.epoxy.ViewBindingKotlinModel
import no.hiof.discgolfapp.model.Course
import no.hiof.discgolfapp.model.Weather
import no.hiof.discgolfapp.screens.courses.CourseInfoFragmentDirections

class CourseInfoEpoxyController : EpoxyController() {

    companion object {
        const val DONE_LOADING = 2
    }

    var isLoading: Int = 0
        set(value) {
            field = value
            if (field == 0) {
                requestModelBuild()
            }
        }

    var fragment: Fragment? = null

    var courseResponse: Course? = null
        set(value) {
            field = value
            if(field != null) {
                isLoading++
            }
            if(isLoading == DONE_LOADING) {
                requestModelBuild()
            }
        }

    var weatherResponse: Weather? = null
        set(value) {
            field = value
            if(field != null) {
                isLoading++
            }
            if(isLoading == DONE_LOADING) {
                requestModelBuild()
            }
        }

    override fun buildModels() {
        if (isLoading < DONE_LOADING) {
            LoadingEpoxyModel().id("Loading").addTo(this)
            return
        }

        HeaderEpoxyModel(
            courseName = courseResponse!!.name
        ).id("header").addTo(this)

        WeatherEpoxyModel(
            weatherSymbol = weatherResponse!!.weatherDrawable,
            temperature = weatherResponse!!.temperature,
            windSpeed = weatherResponse!!.windspeed,
            windDirectionSymbol = weatherResponse!!.windDrawable
        ).id("weather").addTo(this)

        CreateScoreCardButtonEpoxyModel(
            context = fragment,
            uid = courseResponse!!.uid

        ).id("CreateScoreCardButton").addTo(this)

        // add holes
        // add stats
        // add more we want in the info frag

    }

    data class HeaderEpoxyModel(
        val courseName: String
    ): ViewBindingKotlinModel<CourseInfoHeaderBinding>(R.layout.course_info_header) {

        override fun CourseInfoHeaderBinding.bind() {
            courseNameInfoTextView.text = courseName
        }
    }

    data class WeatherEpoxyModel(
        val weatherSymbol: Int,
        val temperature: Double?,
        val windSpeed: Double?,
        val windDirectionSymbol: Int
    ): ViewBindingKotlinModel<CourseInfoWeatherBinding>(R.layout.course_info_weather) {

        override fun CourseInfoWeatherBinding.bind() {
            weatherSymbolInfoImageView.setImageResource(weatherSymbol)
            temperatureTextView.text = "${temperature.toString()} ºC"
            windSpeedTextView.text = "${windSpeed.toString()} m/s"
            windDirectionInfoImageView.setImageResource(windDirectionSymbol)
        }
    }

    data class CreateScoreCardButtonEpoxyModel(
        val context: Fragment?,
        val uid: Int
    ): ViewBindingKotlinModel<CourseInfoCreateScorecardButtonBinding>(R.layout.course_info_create_scorecard_button) {

        override fun CourseInfoCreateScorecardButtonBinding.bind() {
            createScoreCardInfobutton.setOnClickListener() {
            val navController = context!!.findNavController()

            val action =
                CourseInfoFragmentDirections.actionCourseInfoFragmentToCreateScoreCardFragment(
                    uid
                )

            navController.navigate(action)
        }
        }
    }

   class LoadingEpoxyModel: ViewBindingKotlinModel<CourseInfoLoadingBinding>(R.layout.course_info_loading) {
        override fun CourseInfoLoadingBinding.bind() {
            // Have nothing to do here, just load
        }
    }

}