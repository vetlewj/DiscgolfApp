package no.hiof.discgolfapp.services.api.cache

import no.hiof.discgolfapp.model.Course
import no.hiof.discgolfapp.services.api.response.discgolfmetrix.GetListOfCoursesByCountryCodeResponse

object CoursesCache {

    val listOfCourseMap = mutableMapOf<String, GetListOfCoursesByCountryCodeResponse>()
    val courseMap = mutableMapOf<String, Course>()

}