package no.hiof.discgolfapp.helper.response.yr

data class GetWeatherReportFromCoordinatesResponse(
    val geometry: Geometry,
    val properties: Properties,
    val type: String
)