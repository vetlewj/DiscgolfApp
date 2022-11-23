package no.hiof.discgolfapp.services

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
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

        val request = NetworkLayer.apiClient.getCoursesByCountryCode(courseCode)
        if (request.isSuccessful) {
            // Updating the cache
            CoursesCache.listOfCourseMap[courseCode] = request.body()!!

            return CourseMapper.buildFromListOFCoursesResponse(request.body()!!, courseType)
        }

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

        val request = NetworkLayer.apiClient.getCoursesByCountryCode(courseCode)
        if (request.isSuccessful) {
            // Updating the cache
            CoursesCache.listOfCourseMap[courseCode] = request.body()!!

            return CourseMapper.buildListOfType2WithParentIDFromType1(request.body()!!, parentID)
        }

        return null
    }

    suspend fun getCourseByID(courseID: String, context: Context): Course? {
        // TODO: Check if course exists in cache, if not try to get course from API and update firestore cache
        val request = NetworkLayer.apiClient.getCourseByID(courseID)

        if (request.isSuccessful) {
            return CourseMapper.buildFromCourseResponse(request.body()!!)
        }
        return null
    }

    suspend fun getWeatherByCoordinates(lat: String, lon: String): Weather? {
        // TODO: Try to get weather, if not possible (e.g. not connected to internet), return null
        val request = NetworkLayer.apiClient.getWeatherByCoordinates(lat, lon)

        if (request.isSuccessful) {
            return WeatherMapper.buildFromWeatherResponse(request.body()!!)
        }

        return null
    }

}