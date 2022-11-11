package no.hiof.discgolfapp.services

import no.hiof.discgolfapp.services.api.mappers.CourseMapper
import no.hiof.discgolfapp.services.api.mappers.WeatherMapper
import no.hiof.discgolfapp.model.Course
import no.hiof.discgolfapp.model.Weather
import no.hiof.discgolfapp.services.api.NetworkLayer

class SharedRepository {

    suspend fun getCoursesByCountryCode(courseCode: String): ArrayList<Course>? {
        val request = NetworkLayer.apiClient.getCoursesByCountryCode(courseCode)

        if(request.isSuccessful) {
            return CourseMapper.buildFromListOFCoursesResponse(request.body()!!)
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