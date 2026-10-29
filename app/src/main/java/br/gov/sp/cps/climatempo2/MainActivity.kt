package br.gov.sp.cps.climatempo2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import br.gov.sp.cps.climatempo2.ui.theme.Climatempo2Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Climatempo2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WeatherScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherScreen(modifier: Modifier = Modifier) {
    var weatherResponse by remember { mutableStateOf<WeatherResponse?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    var selectedCityName by remember { mutableStateOf("São Paulo") }
    var latitude by remember { mutableStateOf(-23.5505) }
    var longitude by remember { mutableStateOf(-46.6333) }

    val repository = remember { ClimatempoRepository() }

    LaunchedEffect(latitude, longitude) {
        isLoading = true
        errorMessage = null
        val result = withContext(Dispatchers.IO) {
            repository.getWeatherData(latitude = latitude, longitude = longitude)
        }
        result.onSuccess {
            weatherResponse = it
            isLoading = false
        }.onFailure {
            errorMessage = it.localizedMessage ?: "Erro desconhecido"
            isLoading = false
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Climatempo - Cidades",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CityButton("São Paulo", -23.5505, -46.6333, selectedCityName) { name, lat, lon ->
                    selectedCityName = name
                    latitude = lat
                    longitude = lon
                }
                CityButton("Rio de Janeiro", -22.9068, -43.1729, selectedCityName) { name, lat, lon ->
                    selectedCityName = name
                    latitude = lat
                    longitude = lon
                }
                CityButton("Nova York", 40.7128, -74.0060, selectedCityName) { name, lat, lon ->
                    selectedCityName = name
                    latitude = lat
                    longitude = lon
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CityButton("Tóquio", 35.6762, 139.6503, selectedCityName) { name, lat, lon ->
                    selectedCityName = name
                    latitude = lat
                    longitude = lon
                }
                CityButton("Londres", 51.5074, -0.1278, selectedCityName) { name, lat, lon ->
                    selectedCityName = name
                    latitude = lat
                    longitude = lon
                }
                CityButton("Berlim", 52.52, 13.41, selectedCityName) { name, lat, lon ->
                    selectedCityName = name
                    latitude = lat
                    longitude = lon
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isLoading -> {
                        CircularProgressIndicator()
                    }
                    errorMessage != null -> {
                        Text(
                            text = "Erro: $errorMessage",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    weatherResponse != null -> {
                        val weather = weatherResponse!!
                        val current = weather.current
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = selectedCityName,
                                style = MaterialTheme.typography.headlineLarge
                            )
                            Text(
                                text = "Lat: $latitude | Lon: $longitude",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            if (current != null) {
                                Text(
                                    text = "${current.temperature2m}°C",
                                    style = MaterialTheme.typography.displayLarge
                                )
                                Text(
                                    text = "Vento: ${current.windSpeed10m} km/h",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "Horário: ${current.time}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            } else {
                                Text(text = "Dados atuais indisponíveis")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CityButton(
    cityName: String,
    lat: Double,
    lon: Double,
    selectedCity: String,
    onClick: (String, Double, Double) -> Unit
) {
    val isSelected = cityName == selectedCity
    Button(
        onClick = { onClick(cityName, lat, lon) },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
        )
    ) {
        Text(text = cityName)
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherScreenPreview() {
    Climatempo2Theme {
        WeatherScreen()
    }
}
