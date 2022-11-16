package no.hiof.discgolfapp.controller

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.epoxy.*
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.databinding.*
import no.hiof.discgolfapp.helper.DistanceMeasure
import no.hiof.discgolfapp.helper.epoxy.ViewBindingKotlinModel
import no.hiof.discgolfapp.model.Course
import no.hiof.discgolfapp.model.Hole
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

    var listOfCoursesWithSameParentID: ArrayList<Course>? = null
        set(value) {
            field = value
            if(field != null) {
                isLoading++
            }
            if(isLoading == DONE_LOADING) {
                requestModelBuild()
            }
        }

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

    var avgScore: Int = 0
    var bestScore: Int = 0

    override fun buildModels() {
        if (isLoading < DONE_LOADING) {
            LoadingEpoxyModel().id("Loading").addTo(this)
            return
        }

        if(!listOfCoursesWithSameParentID.isNullOrEmpty()) {
            listOfCoursesWithSameParentID!!.forEach{
                HeaderEpoxyModel(
                    courseName = it.name,
                    rating = it.rating
                ).id("header").addTo(this)

            }

        } else {
            HeaderEpoxyModel(
                courseName = courseResponse!!.name,
                rating = courseResponse!!.rating
            ).id("header").addTo(this)


        }

        WeatherEpoxyModel(
            weatherSymbol = weatherResponse!!.weatherDrawable,
            temperature = weatherResponse!!.temperature,
            windSpeed = weatherResponse!!.windspeed,
            windDirectionSymbol = weatherResponse!!.windDrawable
        ).id("weather").addTo(this)

        if(!listOfCoursesWithSameParentID.isNullOrEmpty()) {

            listOfCoursesWithSameParentID!!.forEach {
                CreateScoreCardButtonEpoxyModel(
                    context = fragment,
                    uid = it.uid

                ).id("CreateScoreCardButton").addTo(this)

            }

        } else {

            CreateScoreCardButtonEpoxyModel(
                context = fragment,
                uid = courseResponse!!.uid

            ).id("CreateScoreCardButton").addTo(this)
        }


        // Holes carousel
        if(!listOfCoursesWithSameParentID.isNullOrEmpty()) {

            listOfCoursesWithSameParentID!!.forEach { course ->
                try {
                    if(course.holes!!.isNotEmpty()) {
                        val hole = course.holes.map {
                            HoleCarouselItemEpoxyModel(it).id("${course.uid} ${it!!.holeNumber}")
                        }
                        CarouselModel_()
                            .id("HoleCarousel ${course.uid} ")
                            .models(hole)
                            .numViewsToShowOnScreen(3.5F)
                            .addTo(this)

                    }

                } catch (e: NullPointerException) {}

            }
            try {
                if(courseResponse!!.holes!!.isNotEmpty()) {
                    val hole = courseResponse!!.holes!!.map {
                        HoleCarouselItemEpoxyModel(it).id(it!!.holeNumber)
                    }
                    CarouselModel_()
                        .id("HoleCarousel")
                        .models(hole)
                        .numViewsToShowOnScreen(3.5F)
                        .addTo(this)

                }

            } catch (e: NullPointerException) {}


        } else {
            try {
                if(courseResponse!!.holes!!.isNotEmpty()) {
                    val hole = courseResponse!!.holes!!.map {
                        HoleCarouselItemEpoxyModel(it).id(it!!.holeNumber)
                    }
                    CarouselModel_()
                        .id("HoleCarousel")
                        .models(hole)
                        .numViewsToShowOnScreen(3.5F)
                        .addTo(this)

                }

            } catch (e: NullPointerException) {}

        }

        StatsItemEpoxyModel(
            bestScore = bestScore,
            avgScore = avgScore
        ).id("stats").addTo(this)
        // add more we want in the info frag

    }

    data class HeaderEpoxyModel(
        val courseName: String,
        val rating: Double?
    ): ViewBindingKotlinModel<CourseInfoHeaderBinding>(R.layout.course_info_header) {

        override fun CourseInfoHeaderBinding.bind() {
            courseNameInfoTextView.text = courseName
            parRating.text = if (rating != null) "Rating \n ${String.format("%.1f",rating)}" else null
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

    data class HoleCarouselItemEpoxyModel(
        val hole: Hole?,
    ): ViewBindingKotlinModel<CourseInfoHolesBinding>(R.layout.course_info_holes)  {

        override fun CourseInfoHolesBinding.bind() {

            val distance = if(hole!!.startLat != null && hole.startLon != null && hole.endLat != null && hole.endLon != null) {
                 DistanceMeasure.getDistanceToPositionInMeters(
                    hole.startLat!!.toDouble(),
                    hole.startLon.toDouble(),
                    hole.endLat.toDouble(),
                    hole.endLon.toDouble()
                )
            } else {
                0
            }

            val holesDetailsSentence = if(hole.distance != null) {
                "Par ${hole.par} \n ${hole.distance} m"
            } else if (distance > 0) {
                "Par ${hole.par} \n ${distance} m"
            } else {
                "Par ${hole.par}"
            }

            holeNumberTextView.text = "Hull \n  ${hole.holeNumber.toString()}"
            holeDetailsTextView.text = holesDetailsSentence

        }
    }

    data class StatsItemEpoxyModel(
        val bestScore: Int,
        val avgScore: Int
    ): ViewBindingKotlinModel<CourseInfoStatsBinding>(R.layout.course_info_stats) {

        override fun CourseInfoStatsBinding.bind() {
            bestRoundStatValueInfoTextView.text = bestScore.toString()
            averageValueInfoTextView.text = avgScore.toString()
        }
    }

}
