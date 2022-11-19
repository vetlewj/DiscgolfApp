package no.hiof.discgolfapp.epoxy.models

import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.databinding.CourseInfoWeatherBinding
import no.hiof.discgolfapp.helper.epoxy.ViewBindingKotlinModel

data class WeatherEpoxyModel(
    val weatherSymbol: Int,
    val temperature: Double?,
    val windSpeed: Double?,
    val windDirectionSymbol: Int
): ViewBindingKotlinModel<CourseInfoWeatherBinding>(R.layout.course_info_weather) {

    override fun CourseInfoWeatherBinding.bind() {
        weatherSymbolInfoImageView.setImageResource(weatherSymbol)
        temperatureTextView.text = "${temperature.toString()} ºC"
        windSpeedTextView.text = "${windSpeed.toString()} m/s"
        windDirectionInfoImageView.setImageResource(windDirectionSymbol)
    }
}