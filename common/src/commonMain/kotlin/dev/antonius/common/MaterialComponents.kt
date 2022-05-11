package dev.antonius.common

import androidx.compose.runtime.Composable

// TODO: Move to nonJsMain
@Composable
actual fun Button(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) = androidx.compose.material3.Button(onClick) { content() }