package dev.antonius.common

import androidx.compose.runtime.*

@Composable
fun App() {
    DemoArcSlider()
}

@Composable
expect fun DemoArcSlider()

@Composable
expect fun Button(onClick: () -> Unit, content: @Composable () -> Unit)

@Composable
expect fun Text(text: String)

@Composable
expect fun ArcSlider(progress: Float, onProgressChange: (Float) -> Unit, sweepAngle: Float = 270f)