package no.hiof.discgolfapp.helper.mappers

import no.hiof.discgolfapp.helper.response.discgolfmetrix.GetListOfCoursesByCountryCodeResponse
import no.hiof.discgolfapp.helper.response.yr.GetWeatherReportFromCoordinatesResponse
import no.hiof.discgolfapp.model.Course
import no.hiof.discgolfapp.model.Weather

object WeatherMapper {

    fun buildFromWeatherResponse(response: GetWeatherReportFromCoordinatesResponse): Weather {
        return Weather(null, null, null, null, null, null, null)
    }
}