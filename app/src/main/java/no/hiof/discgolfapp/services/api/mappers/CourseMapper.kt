package no.hiof.discgolfapp.services.api.mappers

import no.hiof.discgolfapp.helper.CourseType
import no.hiof.discgolfapp.services.api.response.discgolfmetrix.GetCourseByIDResponse
import no.hiof.discgolfapp.services.api.response.discgolfmetrix.GetListOfCoursesByCountryCodeResponse
import no.hiof.discgolfapp.model.Course
import no.hiof.discgolfapp.model.Hole

object CourseMapper {

    const val EMPTY_STRING = ""

    fun buildFromListOFCoursesResponse(response: GetListOfCoursesByCountryCodeResponse, courseType: CourseType): ArrayList<Course> {

        return fetchListOfCoursesBasedOnType(response, courseType)
    }

    fun buildFromCourseResponse(response: GetCourseByIDResponse): Course? {

        val course = response.course

        var sumPar = 0

        val holes: ArrayList<Hole> = ArrayList()
        response.baskets?.forEach { basket ->

            sumPar += basket.Par!!.toInt()

            val hole = Hole(
                holeNumber = basket.Number!!.toInt(),
                par = basket.Par.toInt(),
                distance = if (basket.Length != null) basket.Length.toInt() else null,
                startLat = if (!basket.TeeLat.equals(EMPTY_STRING)) basket.TeeLat!!.toDouble() else null,
                startLon = if (!basket.TeeLng.equals(EMPTY_STRING)) basket.TeeLng!!.toDouble() else null,
                endLat = if (!basket.BasketLat.equals(EMPTY_STRING)) basket.BasketLat!!.toDouble() else null,
                endLon = if (!basket.BasketLng.equals(EMPTY_STRING)) basket.BasketLng!!.toDouble() else null,
                unit = basket.Unit
            )
            holes.add(hole)
        }

        var parRating: Double? = null

        try {
            parRating = ((course.RatingValue2!!.toDouble() - course.RatingValue1!!.toDouble())*((sumPar - course.RatingResult1!!.toDouble())/(course.RatingResult2!!.toDouble() - course.RatingResult1.toDouble()))) + course.RatingValue1.toDouble()
        } catch (e: NullPointerException) { }

        return Course(
            uid = course.ID!!.toInt(),
            name = cleanCourseName(course.Fullname!!),
            holes = if (holes.size == 0) null else holes,
            rating = parRating,
            area = course.Area,
            city = course.City,
            location = course.Location,
            latitude = if (course.Lat.equals(EMPTY_STRING)) null else course.Lat!!.toFloat(),
            longitude = if (course.Lng.equals(EMPTY_STRING)) null else course.Lng!!.toFloat(),
            type = course.Type!!.toInt(),
            par = sumPar,
            ratingValue1 =  try {course.RatingValue1!!.toDouble()} catch (e:NullPointerException){ null},
            ratingResult1 = try {course.RatingResult1!!.toDouble()} catch (e:NullPointerException)  {null},
            ratingValue2 = try {course.RatingValue2!!.toDouble()} catch (e:NullPointerException) { null},
            ratingResult2 = try {course.RatingResult2!!.toDouble()} catch (e:NullPointerException) { null},
            parentID = try { course.ParentID!!.toInt()} catch (e:NullPointerException) {null}
        )
    }

    private fun fetchListOfCoursesBasedOnType(response: GetListOfCoursesByCountryCodeResponse, courseType: CourseType): ArrayList<Course>
    {
        val listOfCourses = ArrayList<Course>()

        response.courses.forEach { course ->

            if (course.Enddate == null )
            {
                if (!(course.X.isNullOrBlank() || course.Y.isNullOrBlank())) {
                    if(courseType.type.equals("1")) {
                        if (course.Type.equals(courseType.type) || course.ParentID == null) {
                            val courseObj = Course(
                                uid = course.ID!!.toInt(),
                                name = cleanCourseName(course.Fullname!!),
                                holes = null,
                                rating = null,
                                area = course.Area,
                                city = course.City,
                                location = course.Location,
                                latitude = course.X.toFloat(),
                                longitude = course.Y.toFloat(),
                                type = course.Type!!.toInt(),
                                par = null,
                                ratingValue1 = null,
                                ratingResult1 = null,
                                ratingValue2 = null,
                                ratingResult2 = null,
                                parentID = null
                            )
                            listOfCourses.add(courseObj)
                        }
                    } else {
                        if(course.Type.equals(courseType.type)) {
                            val courseObj = Course(
                                uid = course.ID!!.toInt(),
                                name = cleanCourseName(course.Fullname!!),
                                holes = null,
                                rating = null,
                                area = course.Area,
                                city = course.City,
                                location = course.Location,
                                latitude = course.X.toFloat(),
                                longitude = course.Y.toFloat(),
                                type = course.Type!!.toInt(),
                                par = null,
                                ratingValue1 = null,
                                ratingResult1 = null,
                                ratingValue2 = null,
                                ratingResult2 = null,
                                parentID = try {course.ParentID!!.toInt()} catch (e:NullPointerException) {null}
                            )
                            listOfCourses.add(courseObj)
                        }

                    }
                }
            }
        }
        return listOfCourses
    }

    private fun cleanCourseName(courseName: String): String {

        val regex = """(&rarr;)""".toRegex()

        return regex.replace(courseName, "-")
    }

}