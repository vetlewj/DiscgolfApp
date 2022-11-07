package no.hiof.discgolfapp.model

import no.hiof.discgolfapp.R

class Weather(
    private val symbolCode: String?,
    var temperature: Double?, var windspeed: Double?,
    private var windFromDirection: Float?,
    private var lat: Float?,
    private var lon: Float?,
    private var time: String?,
    val weatherDrawable: Int?,
    val windDrawable: Int?
) {

    companion object {

        fun getWeather(): List<Weather> {
            return listOf(
                Weather(
                    "clearsky_day",
                    14.6,
                    3.3,
                    215.6F,
                    59.939369F,
                    10.785842F,
                    "2022-10-14T13:00:00Z",
                    R.drawable._clearsky_day,
                    R.drawable.ic_baseline_north_east_24


                ),
                Weather(
                    "cloudy",
                    10.2,
                    4.2,
                    350.1F,
                    59.895178F,
                    10.787161F,
                    "2022-10-14T12:00:00Z",
                    R.drawable._cloudy,
                    R.drawable.ic_baseline_north_east_24
                )
            )
        }

//        fun getWeatherFromCoordinate(latitude: Float, longitude: Float): Weather {
//            // TODO: find a better way of doing this
//            for (weather in getWeather()) {
//                if (weather.lat == latitude && weather.lon == longitude) {
//                    return weather
//                }
//
//            }
//            return Weather(null, null, null, null, null, null, null)
//        }

    fun getWindDirectionDrawableFromWindDirection(windFromDirection: Float?): Int {
        if (windFromDirection == null) {
            return 0
        } else {
            val windDirectionFavIcon = when {
                (windFromDirection >= WindDirection.N.direction1 && windFromDirection <= 360) || (windFromDirection >= 0F && windFromDirection <= WindDirection.N.direction2) -> R.drawable.ic_baseline_north_24
                windFromDirection >= WindDirection.NE.direction1 && windFromDirection <= WindDirection.NE.direction2 -> R.drawable.ic_baseline_north_east_24
                windFromDirection >= WindDirection.E.direction1 && windFromDirection <= WindDirection.E.direction2 -> R.drawable.ic_baseline_east_24
                windFromDirection >= WindDirection.SE.direction1 && windFromDirection <= WindDirection.SE.direction2 -> R.drawable.ic_baseline_south_east_24
                windFromDirection >= WindDirection.S.direction1 && windFromDirection <= WindDirection.S.direction2 -> R.drawable.ic_baseline_south_24
                windFromDirection >= WindDirection.SW.direction1 && windFromDirection <= WindDirection.SW.direction2 -> R.drawable.ic_baseline_south_west_24
                windFromDirection >= WindDirection.W.direction1 && windFromDirection <= WindDirection.W.direction2 -> R.drawable.ic_baseline_west_24
                windFromDirection >= WindDirection.NW.direction1 && windFromDirection <= WindDirection.NW.direction2 -> R.drawable.ic_baseline_north_west_24
                else -> 0
            }
            return windDirectionFavIcon
        }
    }

    enum class WindDirection(val direction1: Float, val direction2: Float) {
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

    fun getWeatherDrawableFromSymbolCode(symbolCode: String): Int {
        val weatherFavIcon = when (symbolCode) {
            WeatherSymbol.clearsky_day.symbolCode -> R.drawable._clearsky_day
            WeatherSymbol.clearsky_polartwilight.symbolCode -> R.drawable._clearsky_polartwilight
            WeatherSymbol.clearsky_night.symbolCode -> R.drawable._clearsky_night
            WeatherSymbol.fair_day.symbolCode -> R.drawable._fair_day
            WeatherSymbol.fair_polartwilight.symbolCode -> R.drawable._fair_polartwilight
            WeatherSymbol.fair_night.symbolCode -> R.drawable._fair_night
            WeatherSymbol.partlycloudy_day.symbolCode -> R.drawable._partlycloudy_day
            WeatherSymbol.partlycloudy_polartwilight.symbolCode -> R.drawable._partlycloudy_polartwilight
            WeatherSymbol.CLOUDY.symbolCode -> R.drawable._cloudy
            WeatherSymbol.rainshowers_day.symbolCode -> R.drawable._rainshowers_day
            WeatherSymbol.rainshowers_polartwilight.symbolCode -> R.drawable._rainshowers_polartwilight
            WeatherSymbol.rainshowers_night.symbolCode -> R.drawable._rainshowers_night
            WeatherSymbol.rainshowersandthunder_day.symbolCode -> R.drawable._rainshowersandthunder_day
            WeatherSymbol.rainshowersandthunder_polartwilight.symbolCode -> R.drawable._rainshowersandthunder_polartwilight
            WeatherSymbol.rainshowersandthunder_night.symbolCode -> R.drawable._rainshowersandthunder_night
            WeatherSymbol.sleetshowers_day.symbolCode -> R.drawable._sleetshowers_day
            WeatherSymbol.sleetshowers_polartwilight.symbolCode -> R.drawable._sleetshowers_polartwilight
            WeatherSymbol.sleetshowers_night.symbolCode -> R.drawable._sleetshowers_night
            WeatherSymbol.snowshowers_day.symbolCode -> R.drawable._snowshowers_day
            WeatherSymbol.snowshowers_polartwilight.symbolCode -> R.drawable._snowshowers_polartwilight
            WeatherSymbol.snowshowers_night.symbolCode -> R.drawable._snowshowers_night
            WeatherSymbol.rain.symbolCode -> R.drawable._rain
            WeatherSymbol.heavyrain.symbolCode -> R.drawable._heavyrain
            WeatherSymbol.heavyrainandthunder.symbolCode -> R.drawable._heavyrainandthunder
            WeatherSymbol.sleet.symbolCode -> R.drawable._sleet
            WeatherSymbol.snow.symbolCode -> R.drawable._snow
            WeatherSymbol.snowandthunder.symbolCode -> R.drawable._snowandthunder
            WeatherSymbol.fog.symbolCode -> R.drawable._fog
            WeatherSymbol.sleetshowersandthunder_day.symbolCode -> R.drawable._sleetshowersandthunder_day
            WeatherSymbol.sleetshowersandthunder_polartwilight.symbolCode -> R.drawable._sleetshowersandthunder_polartwilight
            WeatherSymbol.sleetshowersandthunder_night.symbolCode -> R.drawable._sleetshowersandthunder_night
            WeatherSymbol.snowshowersandthunder_day.symbolCode -> R.drawable._snowshowersandthunder_day
            WeatherSymbol.snowshowersandthunder_polartwilight.symbolCode -> R.drawable._snowshowersandthunder_polartwilight
            WeatherSymbol.snowshowersandthunder_night.symbolCode -> R.drawable._snowshowersandthunder_night
            WeatherSymbol.rainandthunder.symbolCode -> R.drawable._rainandthunder
            WeatherSymbol.sleetandthunder.symbolCode -> R.drawable._sleetandthunder
            WeatherSymbol.lightrainshowersandthunder_day.symbolCode -> R.drawable._lightrainshowersandthunder_day
            WeatherSymbol.lightrainshowersandthunder_polartwilight.symbolCode -> R.drawable._lightrainshowersandthunder_polartwilight
            WeatherSymbol.lightrainshowersandthunder_night.symbolCode -> R.drawable._lightrainshowersandthunder_night
            WeatherSymbol.heavyrainshowersandthunder_day.symbolCode -> R.drawable._heavyrainshowersandthunder_day
            WeatherSymbol.heavyrainshowersandthunder_polartwilight.symbolCode -> R.drawable._heavyrainshowersandthunder_polartwilight
            WeatherSymbol.heavyrainshowersandthunder_night.symbolCode -> R.drawable._heavyrainshowersandthunder_night
            WeatherSymbol.lightssleetshowersandthunder_day.symbolCode -> R.drawable._lightssleetshowersandthunder_day
            WeatherSymbol.lightssleetshowersandthunder_polartwilight.symbolCode -> R.drawable._lightssleetshowersandthunder_polartwilight
            WeatherSymbol.lightssleetshowersandthunder_night.symbolCode -> R.drawable._lightssleetshowersandthunder_night
            WeatherSymbol.heavysleetshowersandthunder_day.symbolCode -> R.drawable._heavysleetshowersandthunder_day
            WeatherSymbol.heavysleetshowersandthunder_polartwilight.symbolCode -> R.drawable._heavysleetshowersandthunder_polartwilight
            WeatherSymbol.heavysleetshowersandthunder_night.symbolCode -> R.drawable._heavysleetshowersandthunder_night
            WeatherSymbol.lightssnowshowersandthunder_day.symbolCode -> R.drawable._lightssnowshowersandthunder_day
            WeatherSymbol.lightssnowshowersandthunder_polartwilight.symbolCode -> R.drawable._lightssnowshowersandthunder_polartwilight
            WeatherSymbol.lightssnowshowersandthunder_night.symbolCode -> R.drawable._lightssnowshowersandthunder_night
            WeatherSymbol.heavysnowshowersandthunder_day.symbolCode -> R.drawable._heavysnowshowersandthunder_day
            WeatherSymbol.heavysnowshowersandthunder_polartwilight.symbolCode -> R.drawable._heavysnowshowersandthunder_polartwilight
            WeatherSymbol.heavysnowshowersandthunder_night.symbolCode -> R.drawable._heavysnowshowersandthunder_night
            WeatherSymbol.lightrainandthunder.symbolCode -> R.drawable._lightrainandthunder
            WeatherSymbol.lightsleetandthunder.symbolCode -> R.drawable._lightsleetandthunder
            WeatherSymbol.heavysleetandthunder.symbolCode -> R.drawable._heavysleetandthunder
            WeatherSymbol.lightsnowandthunder.symbolCode -> R.drawable._lightsnowandthunder
            WeatherSymbol.heavysnowandthunder.symbolCode -> R.drawable._heavysnowandthunder
            WeatherSymbol.lightrainshowers_day.symbolCode -> R.drawable._lightrainshowers_day
            WeatherSymbol.lightrainshowers_polartwilight.symbolCode -> R.drawable._lightrainshowers_polartwilight
            WeatherSymbol.lightrainshowers_night.symbolCode -> R.drawable._lightrainshowers_night
            WeatherSymbol.heavyrainshowers_day.symbolCode -> R.drawable._heavyrainshowers_day
            WeatherSymbol.heavyrainshowers_polartwilight.symbolCode -> R.drawable._heavyrainshowers_polartwilight
            WeatherSymbol.heavyrainshowers_night.symbolCode -> R.drawable._heavyrainshowers_night
            WeatherSymbol.lightsleetshowers_day.symbolCode -> R.drawable._lightsleetshowers_day
            WeatherSymbol.lightsleetshowers_polartwilight.symbolCode -> R.drawable._lightsleetshowers_polartwilight
            WeatherSymbol.lightsleetshowers_night.symbolCode -> R.drawable._lightsleetshowers_night
            WeatherSymbol.heavysleetshowers_day.symbolCode -> R.drawable._heavysleetshowers_day
            WeatherSymbol.heavysleetshowers_polartwilight.symbolCode -> R.drawable._heavysleetshowers_polartwilight
            WeatherSymbol.heavysleetshowers_night.symbolCode -> R.drawable._heavysleetshowers_night
            WeatherSymbol.lightsnowshowers_day.symbolCode -> R.drawable._lightsnowshowers_day
            WeatherSymbol.lightsnowshowers_polartwilight.symbolCode -> R.drawable._lightsnowshowers_polartwilight
            WeatherSymbol.lightsnowshowers_night.symbolCode -> R.drawable._lightsnowshowers_night
            WeatherSymbol.heavysnowshowers_day.symbolCode -> R.drawable._heavysnowshowers_day
            WeatherSymbol.heavysnowshowers_polartwilight.symbolCode -> R.drawable._heavysnowshowers_polartwilight
            WeatherSymbol.lightrain.symbolCode -> R.drawable._lightrain
            WeatherSymbol.lightsleet.symbolCode -> R.drawable._lightsleet
            WeatherSymbol.heavysleet.symbolCode -> R.drawable._heavysleet
            WeatherSymbol.lightsnow.symbolCode -> R.drawable._lightsnow
            WeatherSymbol.heavysnow.symbolCode -> R.drawable._heavysnow
            else -> R.drawable.ic_baseline_question_mark_24
        }
        return weatherFavIcon

    }

}

    enum class WeatherSymbol(val symbolCode: String) {
        clearsky_day("clearsky_day"),
        clearsky_polartwilight("clearsky_polartwilight"),
        clearsky_night("clearsky_polartwilight"),
        fair_day("fair_day"),
        fair_polartwilight("fair_polartwilight"),
        fair_night("fair_night"),
        partlycloudy_day("partlycloudy_day"),
        partlycloudy_polartwilight("partlycloudy_polartwilight"),
        CLOUDY("cloudy"),
        rainshowers_day("rainshowers_day"),
        rainshowers_polartwilight("rainshowers_polartwilight"),
        rainshowers_night("rainshowers_night"),
        rainshowersandthunder_day("rainshowersandthunder_day"),
        rainshowersandthunder_polartwilight("rainshowersandthunder_polartwilight"),
        rainshowersandthunder_night("rainshowersandthunder_night"),
        sleetshowers_day("sleetshowers_day"),
        sleetshowers_polartwilight("sleetshowers_polartwilight"),
        sleetshowers_night("sleetshowers_night"),
        snowshowers_day("snowshowers_day"),
        snowshowers_polartwilight("snowshowers_polartwilight"),
        snowshowers_night("snowshowers_night"),
        rain("rain"),
        heavyrain("heavyrain"),
        heavyrainandthunder("heavyrainandthunder"),
        sleet("sleet"),
        snow("snow"),
        snowandthunder("snowandthunder"),
        fog("fog"),
        sleetshowersandthunder_day("sleetshowersandthunder_day"),
        sleetshowersandthunder_polartwilight("sleetshowersandthunder_polartwilight"),
        sleetshowersandthunder_night("sleetshowersandthunder_night"),
        snowshowersandthunder_day("snowshowersandthunder_day"),
        snowshowersandthunder_polartwilight("snowshowersandthunder_polartwilight"),
        snowshowersandthunder_night("snowshowersandthunder_night"),
        rainandthunder("rainandthunder"),
        sleetandthunder("sleetandthunder"),
        lightrainshowersandthunder_day("lightrainshowersandthunder_day"),
        lightrainshowersandthunder_polartwilight("lightrainshowersandthunder_polartwilight"),
        lightrainshowersandthunder_night("lightrainshowersandthunder_night"),
        heavyrainshowersandthunder_day("heavyrainshowersandthunder_day"),
        heavyrainshowersandthunder_polartwilight("heavyrainshowersandthunder_polartwilight"),
        heavyrainshowersandthunder_night("heavyrainshowersandthunder_night"),
        lightssleetshowersandthunder_day("lightssleetshowersandthunder_day"),
        lightssleetshowersandthunder_polartwilight("lightssleetshowersandthunder_polartwilight"),
        lightssleetshowersandthunder_night("lightssleetshowersandthunder_night"),
        heavysleetshowersandthunder_day("heavysleetshowersandthunder_day"),
        heavysleetshowersandthunder_polartwilight("heavysleetshowersandthunder_polartwilight"),
        heavysleetshowersandthunder_night("heavysleetshowersandthunder_night"),
        lightssnowshowersandthunder_day("lightssnowshowersandthunder_day"),
        lightssnowshowersandthunder_polartwilight("lightssnowshowersandthunder_polartwilight"),
        lightssnowshowersandthunder_night("lightssnowshowersandthunder_night"),
        heavysnowshowersandthunder_day("heavysnowshowersandthunder_day"),
        heavysnowshowersandthunder_polartwilight("heavysnowshowersandthunder_polartwilight"),
        heavysnowshowersandthunder_night("heavysnowshowersandthunder_night"),
        lightrainandthunder("lightrainandthunder"),
        lightsleetandthunder("lightsleetandthunder"),
        heavysleetandthunder("heavysleetandthunder"),
        lightsnowandthunder("lightsnowandthunder"),
        heavysnowandthunder("heavysnowandthunder"),
        lightrainshowers_day("lightrainshowers_day"),
        lightrainshowers_polartwilight("lightrainshowers_polartwilight"),
        lightrainshowers_night("lightrainshowers_night"),
        heavyrainshowers_day("heavyrainshowers_day"),
        heavyrainshowers_polartwilight("heavyrainshowers_polartwilight"),
        heavyrainshowers_night("heavyrainshowers_night"),
        lightsleetshowers_day("lightsleetshowers_day"),
        lightsleetshowers_polartwilight("lightsleetshowers_polartwilight"),
        lightsleetshowers_night("lightsleetshowers_night"),
        heavysleetshowers_day("heavysleetshowers_day"),
        heavysleetshowers_polartwilight("heavysleetshowers_polartwilight"),
        heavysleetshowers_night("heavysleetshowers_night"),
        lightsnowshowers_day("lightsnowshowers_day"),
        lightsnowshowers_polartwilight("lightsnowshowers_polartwilight"),
        lightsnowshowers_night("lightsnowshowers_night"),
        heavysnowshowers_day("heavysnowshowers_day"),
        heavysnowshowers_polartwilight("heavysnowshowers_polartwilight"),
        lightrain("lightrain"),
        lightsleet("lightsleet"),
        heavysleet("heavysleet"),
        lightsnow("lightsnow"),
        heavysnow("heavysnow")
    }

}