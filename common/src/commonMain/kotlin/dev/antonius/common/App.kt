package dev.antonius.common

import androidx.compose.runtime.Composable

@Composable
fun App() {
    ArcSlider()
}

@Composable
expect fun Button(onClick: () -> Unit, content: @Composable () -> Unit)

@Composable
expect fun Text(text: String)

@Composable
expect fun ArcSlider()