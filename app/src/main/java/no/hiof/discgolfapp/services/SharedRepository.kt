package no.hiof.discgolfapp.services

import no.hiof.discgolfapp.helper.CourseType
import no.hiof.discgolfapp.services.api.mappers.CourseMapper
import no.hiof.discgolfapp.services.api.mappers.WeatherMapper
import no.hiof.discgolfapp.model.Course
import no.hiof.discgolfapp.model.Weather
import no.hiof.discgolfapp.services.api.NetworkLayer
import no.hiof.discgolfapp.services.api.cache.CoursesCache

class SharedRepository {

    suspend fun getCoursesByCountryCode(courseCode: String, courseType: CourseType): ArrayList<Course>? {

        val cachedCourses = CoursesCache.listOfCourseMap[courseCode]
        if (cachedCourses != null) {
            return CourseMapper.buildFromListOFCoursesResponse(cachedCourses, courseType)
        }

        val request = NetworkLayer.apiClient.getCoursesByCountryCode(courseCode)
        if(request.isSuccessful) {
            // Updating the cache
            CoursesCache.listOfCourseMap[courseCode] = request.body()!!

            return CourseMapper.buildFromListOFCoursesResponse(request.body()!!, courseType)
        }

        return null
    }

    suspend fun getCourseByID(courseID: String): Course? {
        val request = NetworkLayer.apiClient.getCourseByID(courseID)

        if(request.isSuccessful) {
            return  CourseMapper.buildFromCourseResponse(request.body()!!)
        }

        return null
    }

    suspend fun getWeatherByCoordinates(lat: String, lon: String): Weather? {
        val request = NetworkLayer.apiClient.getWeatherByCoordinates(lat, lon)

        if(request.isSuccessful) {
            return WeatherMapper.buildFromWeatherResponse(request.body()!!)
        }

        return null
    }


}