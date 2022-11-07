package no.hiof.discgolfapp.services

import no.hiof.discgolfapp.helper.mappers.CourseMapper
import no.hiof.discgolfapp.helper.response.yr.GetWeatherReportFromCoordinatesResponse
import no.hiof.discgolfapp.model.Course

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

    suspend fun getWeatherByCoordinates(lat: String, lon: String): GetWeatherReportFromCoordinatesResponse? {
        val request = NetworkLayer.apiClient.getWeatherByCoordinates(lat, lon)

        if(request.isSuccessful) {
            return request.body()!!
        }

        return null
    }


}