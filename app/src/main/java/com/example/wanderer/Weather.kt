package com.example.wanderer

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Keep
@Serializable
data class WeatherResults(
    @SerialName("list")
    val results: List<Weather>,
    @SerialName("city")
    val city: City
)

@Keep
@Serializable
data class Weather (
    @SerialName("dt_txt")
    val date: String,
    @SerialName("main")
    val weatherComponents: WeatherComponents,
    @SerialName("weather")
    val weatherType: List<WeatherType>,
    @SerialName("wind")
    val windComponents: WindComponents
): java.io.Serializable

@Keep
@Serializable
data class WeatherComponents (
    @SerialName("temp")
    val temp: Double,
    @SerialName("humidity")
    val humidity: Int
)

@Keep
@Serializable
data class WeatherType (
    @SerialName("description")
    val desc: String,
    @SerialName("icon")
    val imgIcon: String
) {
    val formatDesc: String
        get() = desc.capitalize()
}

@Keep
@Serializable
data class WindComponents (
    @SerialName("speed")
    val speed: Double
)

@Keep
@Serializable
data class City (
    @SerialName("name")
    val name: String,
    @SerialName("sunrise")
    val sunrise: Long,
    @SerialName("sunset")
    val sunset: Long
) {
    val sunriseTime: String
        get() = epochTime(sunrise)
    val sunsetTime: String
        get() = epochTime(sunset)

    private fun epochTime(epoch: Long): String {
        val date = Date(epoch * 1000)
        val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
        formatter.timeZone = TimeZone.getDefault()
        return formatter.format(date)
    }
}