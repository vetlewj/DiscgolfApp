package no.hiof.discgolfapp.services

import no.hiof.discgolfapp.helper.response.yr.GetWeatherReportFromCoordinatesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Headers

interface WeatherService {
    @GET("compact?")
    suspend fun getCoursesByCountryCode(
        @Query("lon") longitude: String,
        @Query("lat") latitude: String
    ): Response<GetWeatherReportFromCoordinatesResponse>

}