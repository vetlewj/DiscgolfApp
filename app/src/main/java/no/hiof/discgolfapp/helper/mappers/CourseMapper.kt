package no.hiof.discgolfapp.helper.mappers

import no.hiof.discgolfapp.helper.response.GetListOfCoursesByCountryCodeResponse
import no.hiof.discgolfapp.model.Course

object CourseMapper {


    fun buildFromListOFCoursesResponse(response: GetListOfCoursesByCountryCodeResponse): ArrayList<Course> {
        val listOfCourses = ArrayList<Course>()
        response.courses.forEach { course ->
            val courseObj = Course(
                uid = course.ID!!.toInt(),
                name = course.Fullname.toString(),
                holes = null,
                rating = null,
                area = course.Area,
                city = course.City,
                location = course.Location,
                latitude = if(course.Y.equals("")) { null } else course.Y!!.toFloat(),
                longitude = if(course.X.equals("")) { null } else course.X!!.toFloat(),
                endDate = null,
                type = null
            )
            listOfCourses.add(courseObj)
        }
        return listOfCourses
    }
}