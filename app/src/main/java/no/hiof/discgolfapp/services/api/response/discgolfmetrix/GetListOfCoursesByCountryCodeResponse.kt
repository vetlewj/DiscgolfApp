package no.hiof.discgolfapp.services.api.response.discgolfmetrix

data class GetListOfCoursesByCountryCodeResponse(
    val courses: List<CourseCountryCodeResponse>
)