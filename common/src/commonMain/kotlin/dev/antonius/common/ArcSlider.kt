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
    val size by remember { mutableStateOf(SizeContainer(Size.Zero)) }
    val progress = .8f
    val sweepAngle = 270f

    Box(modifier, contentAlignment = Alignment.Center) {
        Arc(progress,
            Modifier
                .aspectRatio(1f)
                .fillMaxWidth()
                .padding(16.dp)
                .onSizeChanged { size.size = it.toSize() },
            sweepAngle,
            style = style
        )

        ArcHandle(progress, size, sweepAngle)
    }
}

@Composable
private fun ArcHandle(progress: Float, size: SizeContainer, sweepAngle: Float) {
    val radius = 12.dp

    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    size.observe { old, new ->
        if (old.width == 0f || old.height == 0f) return@observe

        offsetX *= new.width / old.width
        offsetY *= new.height / old.height
    }

    Box(Modifier
        .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
        .size(radius * 2)
        .background(MaterialTheme.colorScheme.primary, CircleShape)
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                change.consumeAllChanges()
                val rawX = offsetX + dragAmount.x
                val rawY = offsetY + dragAmount.y

                val upper = (+(360 - sweepAngle) / 2).deg
                val lower = (-(360 - sweepAngle) / 2).deg

                val rawTheta = atan2(rawY, rawX) - 90.deg
                val theta = (if (rawTheta < 0) min(rawTheta, lower) else max(rawTheta, upper)) + 90.deg

                val circleRadius = size.width / 2

                offsetX = circleRadius * cos(theta)
                offsetY = circleRadius * sin(theta)
            }
        })
}

private fun radToDeg(rad: Float) = rad * 180 / PI.toFloat()
private fun degToRad(deg: Float) = (deg * PI / 180f).toFloat()
/** Converts a degree to a radian angle */
private val Float.deg get() = degToRad(this)
private val Int.deg get() = degToRad(this.toFloat())

private class SizeContainer(size: Size) {
    var size: Size = size
        get() = field
        set(value) {
            observer(field, value)
            field = value
        }

    val height get() = size.height
    val width get() = size.width

    private var observer: (old: Size, new: Size) -> Unit = { _, _ ->  }

    fun observe(observer: (old: Size, new: Size) -> Unit) {
        this.observer = observer
    }
}

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
    check(progress in 0f..1f) { "Progress should be between 0 and 1" }

    Canvas(modifier) {
        drawArc(
            color = backgroundColor,
            startAngle = 135f,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = style
        )
        drawArc(
            color = color,
            startAngle = 135f,
            sweepAngle = 270f * progress,
            useCenter = false,
            style = style
        )
    }
}