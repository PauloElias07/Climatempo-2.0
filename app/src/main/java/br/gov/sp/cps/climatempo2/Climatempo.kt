package br.gov.sp.cps.climatempo2

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    val current: CurrentWeather?,
    val hourly: HourlyWeather?
)

data class CurrentWeather(
    val time: String,
    @SerializedName("temperature_2m")
    val temperature2m: Double,
    @SerializedName("wind_speed_10m")
    val windSpeed10m: Double
)

data class HourlyWeather(
    val time: List<String>,
    @SerializedName("temperature_2m")
    val temperature2m: List<Double>,
    @SerializedName("relative_humidity_2m")
    val relativeHumidity2m: List<Int>,
    @SerializedName("wind_speed_10m")
    val windSpeed10m: List<Double>
)
