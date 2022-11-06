package no.hiof.discgolfapp.helper.mappers

import no.hiof.discgolfapp.helper.response.GetCourseByIDResponse
import no.hiof.discgolfapp.helper.response.GetListOfCoursesByCountryCodeResponse
import no.hiof.discgolfapp.model.Course
import no.hiof.discgolfapp.model.Hole

object CourseMapper {

    const val EMPTY_STRING = ""

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
                latitude = if(course.Y.equals(EMPTY_STRING)) null else course.Y!!.toFloat(),
                longitude = if(course.X.equals(EMPTY_STRING)) null else course.X!!.toFloat(),
                endDate = null,
                type = null
            )
            listOfCourses.add(courseObj)
        }
        return listOfCourses
    }
    fun buildFromCourseResponse(response: GetCourseByIDResponse): Course? {

        val holes: ArrayList<Hole> = ArrayList()
        response.baskets?.forEach { basket ->

            val hole = Hole(
                holeNumber = basket.Number!!.toInt(),
                par = basket.Par!!.toInt(),
                distance = if(basket.Length != null) basket.Length.toInt() else null,
                startLat = if(!basket.TeeLat.equals(EMPTY_STRING)) basket.TeeLat!!.toDouble() else null,
                startLon = if(!basket.TeeLng.equals(EMPTY_STRING)) basket.TeeLng!!.toDouble() else null,
                endLat = if(!basket.BasketLat.equals(EMPTY_STRING)) basket.BasketLat!!.toDouble() else null,
                endLon = if (!basket.BasketLng.equals(EMPTY_STRING)) basket.BasketLng!!.toDouble() else null,
                unit = basket.Unit
            )
                holes.add(hole)
        }

        return Course(
            uid = response.course.ID!!.toInt(),
            name = response.course.Fullname.toString(),
            holes = if(holes.size == 0) null else holes,
            rating = null,
            area = response.course.Area,
            city = response.course.City,
            location = response.course.Location,
            latitude = if(response.course.Lat.equals(EMPTY_STRING)) null else response.course.Lat!!.toFloat(),
            longitude = if(response.course.Lng.equals(EMPTY_STRING)) null else response.course.Lng!!.toFloat(),
            endDate = null,
            type = null
        )
    }

}