package no.hiof.discgolfapp.services.api.response.discgolfmetrix

data class GetCourseByIDResponse(
    val baskets: List<Basket>?,
    val course: CourseIDResponse
)