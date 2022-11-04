package no.hiof.discgolfapp.services

import no.hiof.discgolfapp.helper.data.GetListOfCoursesByCountryCodeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CourseService {

    @GET("api.php?content=courses_list")
    fun getCoursesByCountryCode(
        @Query("country_code") countryCode: String
    ): Call<GetListOfCoursesByCountryCodeResponse>
}