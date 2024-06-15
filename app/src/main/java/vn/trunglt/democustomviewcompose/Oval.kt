package vn.trunglt.democustomviewcompose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun Oval() {
    // Initialize animation state
    var isAnimating = remember { false }
    var isExpanded = remember { false }
    val radiusX = remember { Animatable(1 / 3f) }
    val radiusY = remember { Animatable(1 / 4f) }
    val coroutineScope = rememberCoroutineScope()
    Canvas(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures { tapOffset ->
                val centerX = size.width / 2
                val centerY = size.height / 2.25f
                val currentRadiusX = size.width * radiusX.value
                val currentRadiusY = size.height * radiusY.value
                val ovalRect = Rect(
                    topLeft = Offset(centerX - currentRadiusX, centerY - currentRadiusY),
                    bottomRight = Offset(centerX + currentRadiusX, centerY + currentRadiusY),
                )
                if (ovalRect.contains(tapOffset)) {
                    // Start animation to increase size
                    if (isExpanded && !isAnimating) {
                        isAnimating = true
                        coroutineScope.launch {
                            radiusX.animateTo(
                                targetValue = (radiusX.value / 1.3f),
                                animationSpec = tween(durationMillis = 1000)
                            )
                        }
                        coroutineScope
                            .launch {
                                radiusY.animateTo(
                                    targetValue = (radiusY.value / 1.3f),
                                    animationSpec = tween(durationMillis = 1000)
                                )
                            }
                            .invokeOnCompletion {
                                isExpanded = false
                                isAnimating = false
                            }

                    } else if (!isExpanded && !isAnimating){
                        isAnimating = true
                        coroutineScope.launch {
                            radiusX.animateTo(
                                targetValue = (radiusX.value * 1.3f),
                                animationSpec = tween(durationMillis = 1000)
                            )
                        }
                        coroutineScope
                            .launch {
                                radiusY.animateTo(
                                    targetValue = (radiusY.value * 1.3f),
                                    animationSpec = tween(durationMillis = 1000)
                                )
                            }
                            .invokeOnCompletion {
                                isExpanded = true
                                isAnimating = false
                            }
                    }
                }
            }
        }
    ) {
        val centerX = size.width / 2
        val centerY = size.height / 2.25f
        val currentRadiusX = size.width * radiusX.value
        val currentRadiusY = size.height * radiusY.value

        drawRect(
            color = Color.White,
            topLeft = Offset(0f, 0f),
            size = size,
            style = Fill
        )

        drawOval(
            color = Color.Transparent,
            topLeft = Offset(centerX - currentRadiusX, centerY - currentRadiusY),
            size = Size(currentRadiusX * 2, currentRadiusY * 1.8f),
            style = Fill,
            blendMode = androidx.compose.ui.graphics.BlendMode.Clear
        )

        drawOval(
            color = Color.Blue,
            topLeft = Offset(centerX - currentRadiusX, centerY - currentRadiusY),
            size = Size(currentRadiusX * 2, currentRadiusY * 1.8f),
            style = Stroke(width = 8.dp.toPx())
        )
    }
}