package no.hiof.discgolfapp.services

import no.hiof.discgolfapp.model.Course

object CoursesCache {

    public val courseMap = mutableMapOf<String, ArrayList<Course>>()
}