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

    override fun buildModels() {
        if (isLoading < DONE_LOADING) {
            LoadingEpoxyModel().id("Loading").addTo(this)
            return
        }
        HeaderEpoxyModel(
            courseName = courseName
        ).id("header").addTo(this)

        WeatherEpoxyModel(
            weatherSymbol = weatherResponse!!.weatherDrawable,
            temperature = weatherResponse!!.temperature,
            windSpeed = weatherResponse!!.windspeed,
            windDirectionSymbol = weatherResponse!!.windDrawable
        ).id("weather").addTo(this)



        // if true make for type 1 with layouts else for type 2 with no parentID
        if(!listOfCoursesWithSameParentID.isNullOrEmpty()) {

            listOfCoursesWithSameParentID!!.forEach { course ->

                // header
                //TODO: Change to another model with smaller text size
                HeaderEpoxyModel(
                    courseName = course.name
                ).id("header").addTo(this)

                // button
                CreateScoreCardButtonEpoxyModel(
                    context = fragment,
                    uid = course.uid

                ).id("CreateScoreCardButton").addTo(this)

                // stats


                // holes
                createCarouselModelForHoles(course)

            }

        } else {

            // button
            CreateScoreCardButtonEpoxyModel(
                context = fragment,
                uid = courseResponse!!.uid

            ).id("CreateScoreCardButton").addTo(this)

            //stats
            StatsItemEpoxyModel(
                bestScore = bestScore,
                avgScore = avgScore,
                lastScore = lastScore,
                sumPar = courseResponse?.par,
                courseRating = courseResponse?.rating,
                numberOfHoles = courseResponse?.numberOfHoles,
                distance = courseResponse?.distance
            ).id("stats").addTo(this)

            //holes
            createCarouselModelForHoles(courseResponse)
        }
    }

    // variables
    var fragment: Fragment? = null
    var courseName: String? = null

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

    private fun <T> checkIfLoadingIsDone(value: T) {
            if(value != null) {
                isLoading++
            }
            if(isLoading == DONE_LOADING) {
                requestModelBuild()
            }
        }

    private fun createCarouselModelForHoles(course: Course?) {
        try {
            if(course!!.holes!!.isNotEmpty()) {
                val hole = course.holes!!.map {
                    HoleCarouselItemEpoxyModel(it).id("${course.uid} ${it!!.holeNumber}")
                }
                CarouselModel_()
                    .id("HoleCarousel ${course.uid} ")
                    .models(hole)
                    .numViewsToShowOnScreen(3.5F)
                    .addTo(this)

            }

        } catch (_: NullPointerException) {}
    }
    }

