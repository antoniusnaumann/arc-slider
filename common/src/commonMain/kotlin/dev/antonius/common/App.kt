package dev.antonius.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    Button(onClick = {
        text = "Button clicked!"
    }) {
        Text(text)
    }
}

@Composable
expect fun Button(onClick: () -> Unit, content: @Composable () -> Unit)

@Composable
expect fun Text(text: String)