import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gym.ui.component.AnimatedActionButton
import com.example.gym.ui.viewModel.MemberViewModel


@Composable
fun DashboardScreen(
    nav: NavController,
    vm: MemberViewModel
) {
    val members by vm.member.collectAsState()

    // 🔥 Master animation progress
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .graphicsLayer {
                alpha = progress.value
                scaleX = 0.8f + (0.2f * progress.value)
                scaleY = 0.8f + (0.2f * progress.value)
                translationY = (1f - progress.value) * 120f
            },
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        // 🔹 HEADER (SLIDE IN)
        Text(
            text = "🔥 Gym Dashboard",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.graphicsLayer {
                translationX = (1f - progress.value) * -80f
            }
        )

        // 🔹 PULSING STAT CARD
        val pulse by rememberInfiniteTransition().animateFloat(
            initialValue = 1f,
            targetValue = 1.06f,
            animationSpec = infiniteRepeatable(
                animation = tween(700),
                repeatMode = RepeatMode.Reverse
            )
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
                .graphicsLayer {
                    scaleX = pulse
                    scaleY = pulse
                },
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF0D47A1)
            ),
            elevation = CardDefaults.cardElevation(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "TOTAL MEMBERS",
                    color = Color.White,
                    fontSize = 14.sp
                )

                // 💥 BOUNCE COUNTER
                val count by animateIntAsState(
                    targetValue = members.size,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy
                    )
                )

                Text(
                    text = count.toString(),
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }
        }

        // 🔹 STAGGERED BUTTONS
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            AnimatedActionButton(
                delay = 0,
                progress = progress.value,
                text = "➕ ADD MEMBER",
                onClick = { nav.navigate("add") }
            )

            AnimatedActionButton(
                delay = 120,
                progress = progress.value,
                text = "📋 VIEW MEMBERS",
                outlined = true,
                onClick = { nav.navigate("list") }
            )
        }
    }
}

