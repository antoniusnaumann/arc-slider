package dev.antonius.common.example

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.antonius.common.ArcSlider
import kotlin.math.min
import kotlin.math.roundToInt

val lightThermostatColors = listOf(
    Color(0xFF0369a1),
    Color(0xFF0e7490),
    Color(0xFF047857),
    Color(0xFF65a30d),
    Color(0xFFca8a04),
    Color(0xFFdc2626)
)

val darkThermostatColors = listOf(
    Color(0xFF60a5fa),
    Color(0xFF2dd4bf),
    Color(0xFF34d399),
    Color(0xFFa3e635),
    Color(0xFFfacc15),
    Color(0xFFf87171),
)

@Composable
fun ThermostatScreen() {
    var uiState by remember { mutableStateOf(ThermostatScreenState(10f, 32f, 16f)) }

    val colors = if (isSystemInDarkTheme()) darkThermostatColors else lightThermostatColors
    val color = interpolateColor(colors, uiState.progress)

    val backgroundColors = colors.map { it.copy(alpha = 0.2f) }
    val backgroundColor = interpolateColor(backgroundColors, uiState.progress)

    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        ArcSlider(
            uiState.progress,
            { uiState = uiState.update(it) },
            Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .padding(horizontal = 16.dp),
            width = 8.dp,
            color = color,
            backgroundColor = backgroundColor,
        ) {
            Text(
                uiState.display.run {
                    val first = split(".").first()
                    val fraction = split(".").last()

                    AnnotatedString(first,
                        spanStyle = SpanStyle(fontWeight = FontWeight.Bold)
                    ) + AnnotatedString(".$fraction",
                        spanStyle = SpanStyle(fontWeight = FontWeight.Light)
                    ) + AnnotatedString(" ℃",
                        spanStyle = SpanStyle(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), fontWeight = FontWeight.Light)
                    )
                },
                style = MaterialTheme.typography.displayLarge,
                color = color)
        }
    }
}

fun interpolateColor(colors: List<Color>, progress: Float): Color {
    val inRange = progress * (colors.size - 1)
    val mixRatio = inRange - inRange.toInt()
    val first = colors[inRange.toInt()]
    val second = colors[min(inRange.toInt() + 1, colors.size - 1)]
    return Color(
        first.red * (1 - mixRatio) + second.red * mixRatio,
        first.green * (1 - mixRatio) + second.green * mixRatio,
        first.blue * (1 - mixRatio) + second.blue * mixRatio,
        first.alpha * (1 - mixRatio) + second.alpha * mixRatio
    )
}

data class ThermostatScreenState(val min: Float, val max: Float, val temperature: Float) {
    val progress = (temperature - min) / (max - min)
    val display = "${(temperature * 10f).roundToInt() / 10f}"

    fun update(progress: Float): ThermostatScreenState {
        check(progress in 0f..1f) { "Progress must be between 0 and 1" }
        return copy(temperature = min + (max - min) * progress)
    }
}