package no.hiof.discgolfapp.services

import no.hiof.discgolfapp.helper.response.yr.GetWeatherReportFromCoordinatesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Headers

interface WeatherService {
    @GET("compact?")
    suspend fun getCoursesByCountryCode(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String
    ): Response<GetWeatherReportFromCoordinatesResponse>

}