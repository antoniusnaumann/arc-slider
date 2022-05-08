package dev.antonius.common

import androidx.compose.runtime.Composable

@Composable
actual fun Button(onClick: () -> Unit, content: @Composable () -> Unit) {
    org.jetbrains.compose.web.dom.Button(attrs = { onClick { onClick() } }) { content() }
}
