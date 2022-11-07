package no.hiof.discgolfapp.services

import no.hiof.discgolfapp.helper.response.discgolfmetrix.GetCourseByIDResponse
import no.hiof.discgolfapp.helper.response.discgolfmetrix.GetListOfCoursesByCountryCodeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CoursesService {

    @GET("api.php?content=courses_list")
    suspend fun getCoursesByCountryCode(
        @Query("country_code") countryCode: String
    ): Response<GetListOfCoursesByCountryCodeResponse>


    @GET("api.php?content=course&code=XXX")
    suspend fun getCourseByID(
        @Query("id") courseID: String
    ): Response<GetCourseByIDResponse>

}