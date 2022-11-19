package no.hiof.discgolfapp.epoxy.controller

import androidx.fragment.app.Fragment
import com.airbnb.epoxy.*
import no.hiof.discgolfapp.epoxy.models.*
import no.hiof.discgolfapp.model.Course
import no.hiof.discgolfapp.model.Weather


class CourseInfoEpoxyController : EpoxyController() {

    companion object {
        const val DONE_LOADING = 5
    }

    var avgScore: Int? = null
        set(value) {
            field = value
            checkIfLoadingIsDone(field)
        }
    var bestScore: Int? = null
        set(value) {
            field = value
            checkIfLoadingIsDone(field)
        }
    var lastScore: Int? = null
        set(value) {
            field = value
            checkIfLoadingIsDone(field)
        }

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
            avgScore = avgScore,
            lastScore = lastScore
        ).id("stats").addTo(this)
        // add more we want in the info frag

    }

    // variables
    var fragment: Fragment? = null

    var isLoading: Int = 0
        set(value) {
            field = value
            if (field == 0) requestModelBuild()

        }

    var listOfCoursesWithSameParentID: ArrayList<Course>? = null
        set(value) {
            field = value
            checkIfLoadingIsDone(field)
        }

    var courseResponse: Course? = null
        set(value) {
            field = value
            checkIfLoadingIsDone(field)
        }

    var weatherResponse: Weather? = null
        set(value) {
            field = value
            checkIfLoadingIsDone(field)
        }

    private fun <T> checkIfLoadingIsDone(value: T) {
            if(value != null) {
                isLoading++
            }
            if(isLoading == DONE_LOADING) {
                requestModelBuild()
            }
        }
    }

