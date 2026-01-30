package com.example.gym.ui.components


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gym.data.model.BarUiModel

@Composable
fun MonthlyBarChart(
    bars: List<BarUiModel>,
    modifier: Modifier = Modifier
) {
    if (bars.isEmpty()) return

    val maxValue = remember(bars) {
        bars.maxOf { it.minutes }.coerceAtLeast(1)
    }

    val animatedBars = remember {
        bars.map { Animatable(0f) }
    }

    // Tooltip state
    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(bars) {
        bars.forEachIndexed { index, bar ->
            animatedBars[index].animateTo(
                bar.minutes.toFloat(),
                tween(durationMillis = 600)
            )
        }
    }

    Column(modifier) {

        // 🔹 TOOLTIP
        selectedIndex?.let { index ->
            Text(
                text = "${bars[index].minutes} mins",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 4.dp),
                fontSize = 14.sp
            )
        }

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(horizontal = 16.dp).pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val barWidth = size.width / (bars.size * 1.6f)
                        val space = barWidth / 2

                        val index =
                            (offset.x / (barWidth + space)).toInt()

                        if (index in bars.indices) {
                            selectedIndex = index
                        }
                    }
                }

        ) {
            val barWidth = size.width / (bars.size * 1.6f)
            val space = barWidth / 2

            bars.forEachIndexed { index, bar ->
                val barHeight =
                    (animatedBars[index].value / maxValue) * size.height

                val x = index * (barWidth + space)
                val y = size.height - barHeight

                drawRect(
                    color = if (index == selectedIndex)
                        Color(0xFF0D47A1)
                    else
                        Color(0xFF1976D2),
                    topLeft = Offset(x, y),
                    size = Size(barWidth, barHeight)
                )
            }
        }

        Spacer(Modifier.height(6.dp))

        // 🔹 DAY LABELS
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            bars.forEach {
                Text(
                    text = it.day.toString(),
                    fontSize = 12.sp
                )
            }
        }
    }
}


