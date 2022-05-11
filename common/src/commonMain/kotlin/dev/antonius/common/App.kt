package dev.antonius.common

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.antonius.common.example.ThermostatScreen

@Composable
fun App() {
    ThermostatScreen()
}

@Composable
fun DemoArcSlider() {
    var progress by remember { mutableStateOf(0.2f) }

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        ArcSlider(
            progress,
            { progress = it },
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            8.dp
        )
    }
}

@Composable
expect fun Button(onClick: () -> Unit, content: @Composable () -> Unit)

@Composable
expect fun ArcSlider(progress: Float, onProgressChange: (Float) -> Unit, sweepAngle: Float = 270f, content: @Composable () -> Unit = {})