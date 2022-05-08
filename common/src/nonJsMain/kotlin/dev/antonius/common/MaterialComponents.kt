package dev.antonius.common

import androidx.compose.runtime.Composable

@Composable
actual fun Button(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) = androidx.compose.material3.Button(onClick) { content() }

@Composable
actual fun Text(text: String) = androidx.compose.material3.Text(text)