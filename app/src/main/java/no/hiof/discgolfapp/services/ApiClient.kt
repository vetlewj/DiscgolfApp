package no.hiof.discgolfapp.services

import no.hiof.discgolfapp.helper.response.discgolfmetrix.GetCourseByIDResponse
import no.hiof.discgolfapp.helper.response.discgolfmetrix.GetListOfCoursesByCountryCodeResponse
import retrofit2.Response

class ApiClient(
    private val coursesService: CoursesService
) {
    suspend fun getCoursesByCountryCode(countryCode: String): Response<GetListOfCoursesByCountryCodeResponse> {
        return coursesService.getCoursesByCountryCode(countryCode)
    }

    suspend fun getCourseByID(courseID: String): Response<GetCourseByIDResponse> {
        return coursesService.getCourseByID(courseID)
    }

}