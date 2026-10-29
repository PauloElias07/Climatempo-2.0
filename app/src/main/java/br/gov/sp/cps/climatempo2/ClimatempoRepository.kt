package br.gov.sp.cps.climatempo2

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("v1/forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: Double = 52.52,
        @Query("longitude") longitude: Double = 13.41,
        @Query("current") current: String = "temperature_2m,wind_speed_10m",
        @Query("hourly") hourly: String = "temperature_2m,relative_humidity_2m,wind_speed_10m"
    ): WeatherResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://api.open-meteo.com/"

    val apiService: WeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }
}

class ClimatempoRepository(
    private val apiService: WeatherApiService = RetrofitClient.apiService
) {
    suspend fun getWeatherData(
        latitude: Double = 52.52,
        longitude: Double = 13.41
    ): Result<WeatherResponse> {
        return try {
            val response = apiService.getWeather(latitude = latitude, longitude = longitude)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
