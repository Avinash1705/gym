package com.example.gym.ui.component


import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun DeleteExplosion(
    visible: Boolean,
    onFinished: () -> Unit
) {
    if (!visible) return

    val particles = remember {
        List(18) {
            Particle(
                angle = Random.nextFloat() * 360f,
                speed = Random.nextInt(300, 700)
            )
        }
    }

    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.animateTo(
            1f,
            tween(450, easing = FastOutSlowInEasing)
        )
        onFinished()
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        particles.forEach { p ->
            val distance = p.speed * progress.value / 100
            val rad = Math.toRadians(p.angle.toDouble())

            drawCircle(
                color = Color.Red,
                radius = 6f * (1f - progress.value),
                center = Offset(
                    x = size.width / 2 + (distance * kotlin.math.cos(rad)).toFloat(),
                    y = size.height / 2 + (distance * kotlin.math.sin(rad)).toFloat()
                )
            )
        }
    }
}

private data class Particle(
    val angle: Float,
    val speed: Int
)
