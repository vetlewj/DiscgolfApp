package no.hiof.discgolfapp.helper.response

data class GetCourseByIDResponse(
    val baskets: List<Basket>?,
    val course: CourseIDResponse
)