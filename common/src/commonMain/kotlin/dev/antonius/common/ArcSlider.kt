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
actual fun ArcSlider() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        ArcSlider(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            8.dp
        )
    }
}

@Composable
fun ArcSlider(
    modifier: Modifier = Modifier,
    width: Dp = 8.dp,
) = ArcSlider(
    Stroke(width = with(LocalDensity.current) { width.toPx() }, cap = StrokeCap.Round),
    modifier
)

@Composable
fun ArcSlider(
    style: Stroke,
    modifier: Modifier = Modifier) {
    var size by remember { mutableStateOf(Size.Zero) }
    var progress by remember { mutableStateOf(0.1f) }
    val sweepAngle = 270f

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

        ArcHandle(progress, size, sweepAngle) { progress = it }

        Text("${progress}")
    }
}

@Composable
private fun ArcHandle(progress: Float, size: Size, sweepAngle: Float, onProgressChange: (Float) -> Unit) {
    val radius = 12.dp

    val angle = degToRad(90 + (360 - sweepAngle) / 2 + progress * sweepAngle)
    val circleRadius = size.width / 2

    val x = circleRadius * cos(angle)
    val y = circleRadius * sin(angle)

    // TODO: Bug: Handle stops instantly because changing progress alters update which triggers a recomposition of pointerInput
    val update = { (unprocessedX, unprocessedY): Offset ->
        val rawX = x + unprocessedX
        val rawY = y + unprocessedY

        val lower = (+(360 - sweepAngle) / 2).normalizedDegrees()
        val upper = (-(360 - sweepAngle) / 2).normalizedDegrees()

        val rawTheta = (radToDeg(atan2(rawY, rawX))).normalizedDegrees()
        val theta = min(max(rawTheta, lower), upper)

        onProgressChange((theta.normalizedDegrees() - 90 - (360 - sweepAngle) / 2 ) / sweepAngle)
    }

    Box(Modifier
        .offset { IntOffset(x.roundToInt(), y.roundToInt()) }
        .size(radius * 2)
        .background(MaterialTheme.colorScheme.primary, CircleShape)
        .pointerInput(update) {
            detectDragGestures { change, dragAmount ->
                change.consumeAllChanges()

                update(dragAmount)
            }
        })
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