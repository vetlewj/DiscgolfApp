package no.hiof.discgolfapp.helper.response.discgolfmetrix

data class GetCourseByIDResponse(
    val baskets: List<Basket>?,
    val course: CourseIDResponse
)