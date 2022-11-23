package no.hiof.discgolfapp.services

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import kotlinx.coroutines.tasks.await
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.helper.CourseType
import no.hiof.discgolfapp.helper.network.NetworkConnectionHelper
import no.hiof.discgolfapp.model.Course
import no.hiof.discgolfapp.model.Weather
import no.hiof.discgolfapp.services.api.NetworkLayer
import no.hiof.discgolfapp.services.api.cache.CoursesCache
import no.hiof.discgolfapp.services.api.mappers.CourseMapper
import no.hiof.discgolfapp.services.api.mappers.WeatherMapper
import no.hiof.discgolfapp.services.api.response.discgolfmetrix.CourseCountryCodeResponse
import no.hiof.discgolfapp.services.api.response.discgolfmetrix.GetCourseByIDResponse
import no.hiof.discgolfapp.services.api.response.discgolfmetrix.GetListOfCoursesByCountryCodeResponse

class SharedRepository {
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun getCoursesByCountryCode(
        courseCode: String,
        courseType: CourseType,
        context: Context
    ): ArrayList<Course>? {
        return fetchCoursesByCountryCode(courseCode, courseType, context, true)
    }

    suspend fun getCoursesByCountryCodeAndWithSameParentID(
        courseCode: String,
        parentID: Int,
        context: Context
    ): ArrayList<Course>? {
        return fetchCoursesByCountryCode(courseCode, CourseType.TYPE2, context, false, parentID)
    }

    private suspend fun fetchCoursesByCountryCode(
        courseCode: String,
        courseType: CourseType,
        context: Context,
        allCourses: Boolean,
        parentID: Int = 0
    ): ArrayList<Course>? {
        val cachedCourses = CoursesCache.listOfCourseMap[courseCode]
        if (cachedCourses != null) {
            Log.d(
                "SharedRepository",
                "getCoursesByCountryCode: Found cached courses for $courseCode"
            )
            return if (allCourses) {
                CourseMapper.buildFromListOFCoursesResponse(cachedCourses, courseType)
            } else {
                CourseMapper.buildListOfType2WithParentIDFromType1(cachedCourses, parentID)
            }
        }
        var courses = arrayListOf<Course>()
        if (NetworkConnectionHelper.isNetworkConnected(context)) {
            Log.d(
                "SharedRepository",
                "getCoursesByCountryCode: No cached courses for $courseCode, fetching from API"
            )
            val request = NetworkLayer.apiClient.getCoursesByCountryCode(courseCode)
            if (request.isSuccessful) {
                CoursesCache.listOfCourseMap[courseCode] = request.body()!!
                courses = if (allCourses) {
                    CourseMapper.buildFromListOFCoursesResponse(request.body()!!, courseType)
                } else {
                    CourseMapper.buildListOfType2WithParentIDFromType1(
                        request.body()!!,
                        parentID
                    )
                }
                for (course in request.body()!!.courses) {
                    firestore.collection("coursesByCountryCode").document(course.ID.toString())
                        .set(course)
                }
                return courses
            }
        }
        // Getting data from local Firestore cache is based on this article: https://firebase.google.com/docs/firestore/manage-data/enable-offline
        val data =
            firestore.collection("coursesByCountryCode").whereEqualTo("countryCode", courseCode)
        data.addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, e ->
            if (e != null) {
                Log.w("SharedRepository", "Listen error:", e)
                return@addSnapshotListener
            }
            if (snapshot != null && !snapshot.isEmpty) {
                val coursesResponse = snapshot.toObjects(CourseCountryCodeResponse::class.java)
                val coursesResponseList =
                    GetListOfCoursesByCountryCodeResponse(coursesResponse)
                CoursesCache.listOfCourseMap[courseCode] = coursesResponseList
                courses = CourseMapper.buildFromListOFCoursesResponse(
                    coursesResponseList,
                    courseType
                )
                return@addSnapshotListener
            } else {
                Log.d("SharedRepository", "Current data: null")
            }
        }
        if (data.get().await().isEmpty) {
            Toast.makeText(
                context,
                context.getString(R.string.connect_to_internet_to_get_courses),
                Toast.LENGTH_SHORT
            ).show()
        }
        return courses
    }

    suspend fun getCourseByID(courseID: String, context: Context): Course? {
        var course: Course? = null
        if (NetworkConnectionHelper.isNetworkConnected(context)) {
            val request = NetworkLayer.apiClient.getCourseByID(courseID)
            if (request.isSuccessful) {
                val responseBody = request.body()!!
                course = CourseMapper.buildFromCourseResponse(responseBody)
                firestore.collection("courseIDResponse").document(course.uid.toString())
                    .set(responseBody)
                return course
            }
        }
        val data =
            firestore.collection("course").whereEqualTo("uid", courseID)
        data.addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, e ->
            if (e != null) {
                Log.w("SharedRepository", "Listen error:", e)
                return@addSnapshotListener
            }
            if (snapshot != null && !snapshot.isEmpty) {
                val courseResponse = snapshot.toObjects(GetCourseByIDResponse::class.java)
                course = CourseMapper.buildFromCourseResponse(courseResponse[0])
                return@addSnapshotListener
            } else {
                Log.d("SharedRepository", "Current data: null")
            }
        }
        if (data.get().await().isEmpty) {
            Toast.makeText(
                context,
                context.getString(R.string.connect_to_internet_to_get_course),
                Toast.LENGTH_SHORT
            ).show()
        }
        return course
    }

    suspend fun getWeatherByCoordinates(lat: String, lon: String): Weather? {
        try {
            val request = NetworkLayer.apiClient.getWeatherByCoordinates(lat, lon)

            if (request.isSuccessful) {
                return WeatherMapper.buildFromWeatherResponse(request.body()!!)
            } else {
                Log.w("SharedRepository", "getWeatherByCoordinates: ${request.errorBody()}")
            }
        } catch (e: Exception) {
            Log.d("SharedRepository", "getWeatherByCoordinates: ${e.message}")
        }
        return null
    }

}