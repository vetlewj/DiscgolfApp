package no.hiof.discgolfapp.helper.mappers

import no.hiof.discgolfapp.helper.response.discgolfmetrix.GetListOfCoursesByCountryCodeResponse
import no.hiof.discgolfapp.helper.response.yr.GetWeatherReportFromCoordinatesResponse
import no.hiof.discgolfapp.model.Course
import no.hiof.discgolfapp.model.Weather

object WeatherMapper {

    fun buildFromWeatherResponse(response: GetWeatherReportFromCoordinatesResponse): Weather {

        // Todo: må på et vis få systemtid slik at jeg kan finne ut hva slags verdi jeg skal hente i timeseries array i API
        // - må også konvertere tiden til zulu tid.
        // - Sammenligne systemtiden, med riktig tid for riktig værmelding i timeserien i en løkke.

        // hent ut fra nex1 hour. Om den er null, hent fra next 6 hours, om den er null hent fra next12 hours.
        val weatherData = response.properties.timeseries[0].data
        val symbolCode = weatherData.next_1_hours!!.summary.symbol_code
        val temperature = weatherData.instant.details.air_temperature
        val windSpeed = weatherData.instant.details.wind_speed
        val windFromDirection = weatherData.instant.details.wind_from_direction.toFloat()

        return Weather(
            symbolCode = symbolCode,
            temperature = temperature,
            windspeed = windSpeed,
            windFromDirection = windFromDirection,
            lat = response.geometry.coordinates[0].toFloat(),
            lon = response.geometry.coordinates[1].toFloat(),
            time = null,
            weatherDrawable = Weather.getWeatherDrawableFromSymbolCode(symbolCode),
            windDrawable = Weather.getWindDirectionDrawableFromWindDirection(windFromDirection)
        )
    }
}