package no.hiof.discgolfapp.helper.response.discgolfmetrix

data class GetListOfCoursesByCountryCodeResponse(
    val courses: List<CourseCountryCodeResponse>
)