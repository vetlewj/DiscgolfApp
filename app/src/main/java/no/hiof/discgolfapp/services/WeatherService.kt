package no.hiof.discgolfapp.services

import no.hiof.discgolfapp.helper.response.discgolfmetrix.GetListOfCoursesByCountryCodeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("compact?")
    suspend fun getCoursesByCountryCode(
        @Query("lat") latitude: Float,
        @Query("lon") longitude: Float
    ): Response<GetListOfCoursesByCountryCodeResponse>



}