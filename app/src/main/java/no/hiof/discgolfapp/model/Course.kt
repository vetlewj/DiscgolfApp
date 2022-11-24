package no.hiof.discgolfapp.model

import java.util.*

data class Course(
    val uid: Int = 0,
    val name: String = "",
    val holes: List<Hole?>? = null,
    val rating: Double? = null,
    val area: String? = null,
    val city: String? = null,
    val location: String? = null,
    val latitude: Float? = null,
    val longitude: Float? = null,
    val type: Int? = null,
    val par: Int? = 0,
    val ratingValue1: Double? = null,
    val ratingResult1: Double? = null,
    val ratingValue2: Double? = null,
    val ratingResult2: Double? = null,
    val parentID: Int? = null,
    val numberOfHoles: Int? = null,
    val distance: Int? = null
) {


    companion object {
        private val courses: ArrayList<Course?> = ArrayList()

        fun getCourses(): List<Course> {
            return listOf(
                Course(
                    0,
                    "Muselunden",
                    Hole.getHoles(),
                    null,
                    "Oslo",
                    "Oslo",
                    null,
                    59.939369F,
                    10.785842F,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
                ),
                Course(
                    1,
                    "Ekeberg skole",
                    Hole.getHoles(),
                    null,
                    "Oslo",
                    "Oslo",
                    null,
                    59.895178F,
                    10.787161F,
                    null,
                    null,
                    null,
                null,
                    null,
                    null,
                    null,
                    null,
                    null
                ),
                Course(2, "Frogner", Hole.getHoles(), null, "", null, null, null, null, null, null, null, null, null, null, null, null, null)

            )
        }

        fun addCourseToAllCourses(course: Course) {
            courses.add(course)
        }

        fun getAllCourses(): ArrayList<Course?> {
            return courses
        }
    }
}
