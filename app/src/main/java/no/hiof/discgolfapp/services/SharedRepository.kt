package no.hiof.discgolfapp.services

import no.hiof.discgolfapp.helper.response.GetListOfCoursesByCountryCodeResponse

class SharedRepository {

    suspend fun getCoursesByCountryCode(courseCode: String): GetListOfCoursesByCountryCodeResponse? {
        val request = NetworkLayer.apiClient.getCoursesByCountryCode(courseCode)

        if(request.isSuccessful) {
            return request.body()!!
        }

        return null
    }
}