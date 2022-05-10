package dev.antonius.common

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
actual fun DemoArcSlider() {
    var progress by remember { mutableStateOf(0.1f) }

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