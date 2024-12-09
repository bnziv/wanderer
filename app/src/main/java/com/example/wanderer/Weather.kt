package com.example.wanderer

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class WeatherResults(
    @SerialName("list")
    val results: List<Weather>
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