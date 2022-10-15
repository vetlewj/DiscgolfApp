package no.hiof.discgolfapp.model

import android.graphics.drawable.Drawable
import no.hiof.discgolfapp.R

class Weather(private val symbolCode: WeatherSymbol?, var temperature: Double?, var windspeed: Double?, private var windFromDirection: Double?, private var lat: Float?, private var lon: Float?, private var time: String?) {

    companion object {

        fun getWeather(): List<Weather> {
            return listOf(
                Weather(WeatherSymbol.SUNNY, 14.6, 3.3, 215.4, 59.939369F, 10.785842F, "2022-10-14T13:00:00Z"),
                Weather(WeatherSymbol.CLOUDY, 10.2, 4.2, 213.2, 59.895178F, 10.787161F, "2022-10-14T12:00:00Z")
            )
        }

        fun getWeatherFromCoordinate(latitude: Float, longitude: Float): Weather {
            // TODO: find a better way of doing this
            for (weather in getWeather()) {
                if (weather.lat == latitude && weather.lon == longitude) {
                    return weather
                }

            }
            return Weather(null, 2.2, null, null, null, null, null)
        }

    }


    fun getWeatherSymbol() {
        val weatherFavIcon = when (this.symbolCode) {
            WeatherSymbol.SUNNY -> R.drawable.ic_baseline_wb_sunny_24
            WeatherSymbol.CLOUDY -> R.drawable.ic_baseline_wb_cloudy_24
            else -> R.drawable.ic_baseline_question_mark_24
        }

    }

    // TODO: Find all symbols.
    enum class WeatherSymbol {
        SUNNY, CLOUDY, LIGHTRAIN, RAIN, HEAVYRAIN
    }

}