package no.hiof.discgolfapp.services

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.helper.CourseType
import no.hiof.discgolfapp.helper.network.NetworkConnectionHelper
import no.hiof.discgolfapp.services.api.mappers.CourseMapper
import no.hiof.discgolfapp.services.api.mappers.WeatherMapper
import no.hiof.discgolfapp.model.Course
import no.hiof.discgolfapp.model.Weather
import no.hiof.discgolfapp.services.api.NetworkLayer
import no.hiof.discgolfapp.services.api.cache.CoursesCache

class SharedRepository {
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun getCoursesByCountryCode(
        courseCode: String,
        courseType: CourseType,
        context: Context
    ): ArrayList<Course>? {
        // TODO: Check if the courses exists in cache, if not try to fetch all courses from API and update firestore cache
        val cachedCourses = CoursesCache.listOfCourseMap[courseCode]
        if (cachedCourses != null) {
            Log.d(
                "SharedRepository",
                "getCoursesByCountryCode: Found cached courses for $courseCode"
            )
            return CourseMapper.buildFromListOFCoursesResponse(cachedCourses, courseType)
        }
        if (NetworkConnectionHelper.isNetworkConnected(context)) {
            Log.d(
                "SharedRepository",
                "getCoursesByCountryCode: No cached courses for $courseCode, fetching from API"
            )
            val request = NetworkLayer.apiClient.getCoursesByCountryCode(courseCode)
            if (request.isSuccessful) {
                CoursesCache.listOfCourseMap[courseCode] = request.body()!!
                val courses =
                    CourseMapper.buildFromListOFCoursesResponse(request.body()!!, courseType)
                for (course in courses) {
                    firestore.collection("courses").document(course.uid.toString()).set(course)
                }
                return courses
            }
        }
        Toast.makeText(
            context,
            context.getString(R.string.connect_to_internet_to_get_courses),
            Toast.LENGTH_SHORT
        ).show()
        return null
    }

    suspend fun getCoursesByCountryCodeAndWithSameParentID(
        courseCode: String,
        parentID: Int,
        context: Context
    ): ArrayList<Course>? {
        // TODO: Check if the courses exists in cache, if not try to fetch all courses from API and update firestore cache
        val cachedCourses = CoursesCache.listOfCourseMap[courseCode]
        if (cachedCourses != null) {
            return CourseMapper.buildListOfType2WithParentIDFromType1(cachedCourses, parentID)
        }
        if (NetworkConnectionHelper.isNetworkConnected(context)) {
            val request = NetworkLayer.apiClient.getCoursesByCountryCode(courseCode)
            if (request.isSuccessful) {
                CoursesCache.listOfCourseMap[courseCode] = request.body()!!
                val courses =
                    CourseMapper.buildListOfType2WithParentIDFromType1(request.body()!!, parentID)
                for (course in courses) {
                    firestore.collection("courses").document(course.uid.toString()).set(course)
                }
            }
        }
        Toast.makeText(
            context,
            context.getString(R.string.connect_to_internet_to_get_courses),
            Toast.LENGTH_SHORT
        ).show()
        return null
    }

    suspend fun getCourseByID(courseID: String, context: Context): Course? {
        // TODO: Check if course exists in cache, if not try to get course from API and update firestore cache
        if (NetworkConnectionHelper.isNetworkConnected(context)) {
            val request = NetworkLayer.apiClient.getCourseByID(courseID)
            if (request.isSuccessful) {
                val course = CourseMapper.buildFromCourseResponse(request.body()!!)
                firestore.collection("course").document(course.uid.toString()).set(course)
                return course
            }
        }
        Toast.makeText(
            context,
            context.getString(R.string.connect_to_internet_to_get_course),
            Toast.LENGTH_SHORT
        ).show()
        return null
    }

    suspend fun getWeatherByCoordinates(lat: String, lon: String): Weather? {
        // TODO: Try to get weather, if not possible (e.g. not connected to internet), return null
        try {
            val request = NetworkLayer.apiClient.getWeatherByCoordinates(lat, lon)

            if (request.isSuccessful) {
                return WeatherMapper.buildFromWeatherResponse(request.body()!!)
            }
            else{
                Log.w("SharedRepository", "getWeatherByCoordinates: ${request.errorBody()}")
            }
        } catch (e: Exception) {
            Log.d("SharedRepository", "getWeatherByCoordinates: ${e.message}")
        }
        return null
    }

}