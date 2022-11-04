package no.hiof.discgolfapp.services

import no.hiof.discgolfapp.helper.data.GetCoursesByCountryCodeResponse
import retrofit2.Call
import retrofit2.http.GET

interface CourseService {

    @GET("api.php?content=courses_list&country_code=NO")
    fun getCoursesByCountryCode(): Call<GetCoursesByCountryCodeResponse>
}