package com.luffy1705.gym.ui.component


import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp


fun Modifier.gravityFall(
    trigger: Boolean,
    onFinished: () -> Unit
): Modifier = composed {

    val density = LocalDensity.current

    val fallOffsetY = remember { Animatable(0f) }
    val rotationAnim = remember { Animatable(0f) }
    val alphaAnim = remember { Animatable(1f) }

    LaunchedEffect(trigger) {
        if (trigger) {
            // 🔥 Fast fall
            fallOffsetY.animateTo(
                targetValue = with(density) { 650.dp.toPx() },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearEasing
                )
            )

            // 🔥 Small bounce
            fallOffsetY.animateTo(
                targetValue = with(density) { 700.dp.toPx() },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
    }

    LaunchedEffect(trigger) {
        if (trigger) {
            rotationAnim.animateTo(-18f, tween(350))
            alphaAnim.animateTo(0f, tween(350))
            onFinished()
        }
    }

    graphicsLayer {
        translationY = fallOffsetY.value
        rotationZ = rotationAnim.value
        alpha = alphaAnim.value
    }
}

