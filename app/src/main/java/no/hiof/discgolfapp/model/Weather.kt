package no.hiof.discgolfapp.model

import android.graphics.drawable.Drawable
import no.hiof.discgolfapp.R

class Weather(private val symbolCode: WeatherSymbol?, var temperature: Double?, var windspeed: Double?, private var windFromDirection: Float?, private var lat: Float?, private var lon: Float?, private var time: String?) {

    companion object {

        fun getWeather(): List<Weather> {
            return listOf(
                Weather(WeatherSymbol.SUNNY, 14.6, 3.3, 215.6F, 59.939369F, 10.785842F, "2022-10-14T13:00:00Z"),
                Weather(WeatherSymbol.CLOUDY, 10.2, 4.2, 306.1F, 59.895178F, 10.787161F, "2022-10-14T12:00:00Z")
            )
        }

        fun getWeatherFromCoordinate(latitude: Float, longitude: Float): Weather {
            // TODO: find a better way of doing this
            for (weather in getWeather()) {
                if (weather.lat == latitude && weather.lon == longitude) {
                    return weather
                }

            }
            return Weather(null, null, null, null, null, null, null)
        }

    }

    fun getWeatherSymbol(): Int{
        val weatherFavIcon = when (this.symbolCode) {
            WeatherSymbol.SUNNY -> R.drawable.ic_baseline_wb_sunny_24
            WeatherSymbol.CLOUDY -> R.drawable.ic_baseline_wb_cloudy_24
            else -> R.drawable.ic_baseline_question_mark_24
        }
        return weatherFavIcon

    }

    fun getWindDirectionSymbol(): Int {
        if(this.windFromDirection == null) {
            return 0
        } else {
            val windDirection = this.windFromDirection!!
            val windDirectionFavIcon = when {
                // TODO something with north direction is wrong
                ((windDirection >= WindDirection.N.direction1 && windDirection <= 0F) || (windDirection >= 0F && windDirection <= WindDirection.N.direction2)) -> R.drawable.ic_baseline_north_24
                windDirection >= WindDirection.NE.direction1 && windDirection <= WindDirection.NE.direction2 -> R.drawable.ic_baseline_north_east_24
                windDirection >= WindDirection.E.direction1 && windDirection <= WindDirection.E.direction2 -> R.drawable.ic_baseline_east_24
                windDirection >= WindDirection.SE.direction1 && windDirection <= WindDirection.SE.direction2 -> R.drawable.ic_baseline_south_east_24
                windDirection >= WindDirection.S.direction1 && windDirection <= WindDirection.S.direction2 -> R.drawable.ic_baseline_south_24
                windDirection >= WindDirection.SW.direction1 &&windDirection <= WindDirection.SW.direction2 -> R.drawable.ic_baseline_south_west_24
                windDirection >= WindDirection.W.direction1 && windDirection <= WindDirection.W.direction2 -> R.drawable.ic_baseline_west_24
                windDirection >= WindDirection.NW.direction1 && windDirection <= WindDirection.NW.direction2 -> R.drawable.ic_baseline_north_west_24
                else -> 0
            }
            return windDirectionFavIcon
        }
    }

    enum class WindDirection(val direction1: Float, val direction2: Float ) {
        // TODO fjern nne, ene osv, om vi ikke skal legge til flere piler
        N(345.5F, 15.5F),
        NNE(15.5F, 35.5F),
        NE(35.5F, 55.5F),
        ENE(55.5F, 75.5F),
        E(75.5F, 105.5F),
        ESE(105.5F, 125.5F),
        SE(125.5F, 145.5F),
        SSE(145.5F, 165.5F),
        S(165.5F, 195.5F),
        SSW(195.5F, 215.5F),
        SW(215.5F, 235.5F),
        WSW(235.5F, 255.5F),
        W(255.5F, 285.5F),
        WNW(285.5F, 305.5F),
        NW(305.5F, 325.5F),
        NNW(325.5F, 345.5F)
    }

    // TODO: Find all symbols.
    enum class WeatherSymbol {
        SUNNY, CLOUDY, LIGHTRAIN, RAIN, HEAVYRAIN
    }

}