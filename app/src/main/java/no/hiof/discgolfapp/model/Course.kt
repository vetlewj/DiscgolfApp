package no.hiof.discgolfapp.model

import java.util.*

data class Course(
    val uid: Int,
    val name: String,
    val holes: List<Hole?>?,
    val rating: Double?,
    val area: String?,
    val city: String?,
    val location: String?,
    val latitude: Float?,
    val longitude: Float?,
    val type: Int?,
    val par: Int? = 0,
    val ratingValue1: Double?,
    val ratingResult1: Double?,
    val ratingValue2: Double?,
    val ratingResult2: Double?
) {
    //TODO: Finish class

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
                    null
                ),
                Course(2, "Frogner", Hole.getHoles(), null, "", null, null, null, null, null, null, null, null, null, null)

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
