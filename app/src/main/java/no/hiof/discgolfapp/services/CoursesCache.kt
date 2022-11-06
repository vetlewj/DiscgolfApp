package no.hiof.discgolfapp.services

import no.hiof.discgolfapp.model.Course

object CoursesCache {

    val listOfCourseMap = mutableMapOf<String, ArrayList<Course>>()
    val courseMap = mutableMapOf<String, Course>()

}