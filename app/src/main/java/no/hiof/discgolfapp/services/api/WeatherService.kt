package no.hiof.discgolfapp.services.api

import no.hiof.discgolfapp.services.api.response.yr.GetWeatherReportFromCoordinatesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("compact?")
    suspend fun getCoursesByCountryCode(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String
    ): Response<GetWeatherReportFromCoordinatesResponse>

}