package dev.antonius.common

import androidx.compose.runtime.Composable

@Composable
actual fun Button(onClick: () -> Unit, content: @Composable () -> Unit) {
    org.jetbrains.compose.web.dom.Button(attrs = { onClick { onClick() } }) { content() }
}

@Composable
actual fun DemoArcSlider() {
    ArcSlider(progress = 0.5f, onProgressChange = {}, sweepAngle = 270f)
}

@Composable
actual fun ArcSlider(
    progress: Float,
    onProgressChange: (Float) -> Unit,
    sweepAngle: Float
) {
    Text("Not implemented. progress: $progress, sweepAngle: $sweepAngle")
}