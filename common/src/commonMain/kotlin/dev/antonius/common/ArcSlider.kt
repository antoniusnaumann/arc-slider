package dev.antonius.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import kotlin.math.*

// TODO: Move to nonJsMain

@Composable
actual fun ArcSlider(
    progress: Float,
    onProgressChange: (Float) -> Unit,
    sweepAngle: Float
) = ArcSlider(progress, onProgressChange, Modifier)

/**
 * A circular slider that can be used to select a value between 0f and 1f.
 */
@Composable
fun ArcSlider(
    progress: Float,
    onProgressChange: (Float) -> Unit,
    // TODO: This a workaround to differ between nonJs ArcSlider and Common (Web + Dektop + Android) ArcSlider
    modifier: Modifier,
    width: Dp = 8.dp,
    sweepAngle: Float = 270f
) = ArcSlider(
    progress,
    onProgressChange,
    Stroke(width = with(LocalDensity.current) { width.toPx() }, cap = StrokeCap.Round),
    modifier,
    sweepAngle,
)

@Composable
fun ArcSlider(
    progress: Float,
    onProgressChange: (Float) -> Unit,
    style: Stroke,
    modifier: Modifier = Modifier,
    sweepAngle: Float = 270f
) {
    var size by remember { mutableStateOf(Size.Zero) }

    Box(modifier, contentAlignment = Alignment.Center) {
        Arc(progress,
            Modifier
                .aspectRatio(1f)
                .fillMaxWidth()
                .padding(16.dp)
                .onSizeChanged { size = it.toSize() },
            sweepAngle,
            style = style
        )

        ArcHandle(progress, onProgressChange, size, sweepAngle)
    }
}

@Composable
private fun ArcHandle(progress: Float, onProgressChange: (Float) -> Unit, size: Size, sweepAngle: Float) {
    val handleRadius = 12.dp

    val angle = degToRad(90 + (360 - sweepAngle) / 2 + progress * sweepAngle)

    val circleRadius = size.width / 2

    val x = circleRadius * cos(angle)
    val y = circleRadius * sin(angle)

    var proxyOffsetX by remember { mutableStateOf(x) }
    var proxyOffsetY by remember { mutableStateOf(y) }

    var dragEnded by remember { mutableStateOf(0) }

    LaunchedEffect(dragEnded, size) {
        proxyOffsetX = x
        proxyOffsetY = y
    }

    Box(Modifier
        .offset { IntOffset(proxyOffsetX.roundToInt(), proxyOffsetY.roundToInt()) }
        .size(handleRadius * 2)
        .pointerInput(size, dragEnded) {
            detectDragGestures(
                onDragEnd = {
                    dragEnded++
                },
                onDrag = { change, dragAmount ->
                    change.consumeAllChanges()
                    proxyOffsetX += dragAmount.x
                    proxyOffsetY += dragAmount.y

                    onProgressChange(calculateProgress(Offset(proxyOffsetX, proxyOffsetY), sweepAngle))
                })
        }
    )

    Box(Modifier.offset { IntOffset(x.roundToInt(), y.roundToInt()) }) {
        Box(Modifier.size(handleRadius * 2).background(MaterialTheme.colorScheme.primary, CircleShape))
    }
}

private fun calculateProgress(drag: Offset, sweepAngle: Float): Float {
    // TODO prevent jumps from 1f to 0f
    val theta = (radToDeg(atan2(drag.y, drag.x))).normalizedDegrees()
    val rawProgress = (theta - 90 - (360 - sweepAngle) / 2 ).normalizedDegrees() / sweepAngle

    // The first is a hack to estimate if the progress did underflow
    return if (rawProgress > 1.1f) 0f else if (rawProgress > 1f) 1f else rawProgress
}

private fun radToDeg(rad: Float) = rad * 180 / PI.toFloat()
private fun degToRad(deg: Float) = (deg * PI / 180f).toFloat()
private fun Float.normalizedDegrees() = this.mod(360f)

/**
 * @param progress Relative progress to display. Should be between 0 and 1.
 */
@Composable
internal fun Arc(
    progress: Float,
    modifier: Modifier = Modifier,
    sweepAngle: Float = 270f,
    color: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    style: Stroke = Stroke(width = with(LocalDensity.current) { 8.dp.toPx() }, cap = StrokeCap.Round),
) {
    check(progress in 0f..1f) { "Progress should be between 0 and 1. Progress was $progress" }

    Canvas(modifier) {
        drawArc(
            color = backgroundColor,
            startAngle = 90f + (360 - sweepAngle) / 2,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = style
        )
        drawArc(
            color = color,
            startAngle = 90f + (360 - sweepAngle) / 2,
            sweepAngle = sweepAngle * progress,
            useCenter = false,
            style = style
        )
    }
}