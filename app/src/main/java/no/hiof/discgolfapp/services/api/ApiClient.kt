package no.hiof.discgolfapp.services.api

import no.hiof.discgolfapp.services.api.response.discgolfmetrix.GetCourseByIDResponse
import no.hiof.discgolfapp.services.api.response.discgolfmetrix.GetListOfCoursesByCountryCodeResponse
import no.hiof.discgolfapp.services.api.response.yr.GetWeatherReportFromCoordinatesResponse
import retrofit2.Response

class ApiClient(
    private val coursesService: CoursesService,
    private val weatherService: WeatherService
) {
    suspend fun getCoursesByCountryCode(countryCode: String): Response<GetListOfCoursesByCountryCodeResponse> {
        return coursesService.getCoursesByCountryCode(countryCode)
    }

    suspend fun getCourseByID(courseID: String): Response<GetCourseByIDResponse> {
        return coursesService.getCourseByID(courseID)
    }

    suspend fun getWeatherByCoordinates(lat: String, lon: String): Response<GetWeatherReportFromCoordinatesResponse> {
        return weatherService.getCoursesByCountryCode(lat, lon)
    }

}