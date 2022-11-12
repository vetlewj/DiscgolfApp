package no.hiof.discgolfapp.services.api.response.yr

data class GetWeatherReportFromCoordinatesResponse(
    val geometry: Geometry,
    val properties: Properties,
    val type: String
)