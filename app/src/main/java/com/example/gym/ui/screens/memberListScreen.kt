package com.example.gym.ui.screens

import android.R.attr.translationY
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gym.ui.component.MemberSearchBar
import com.example.gym.ui.viewModel.MemberViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MemberListScreen(
    nav: NavController,
    vm: MemberViewModel
) {
    val members by vm.member.collectAsState()

    var query by remember { mutableStateOf("") }

    // 🔍 Filtered list (memoized)
    val filteredMembers = remember(members, query) {
        if (query.isBlank()) {
            members
        } else {
            members.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.phone.contains(query)
            }
        }
    }

    Column(Modifier.fillMaxSize()) {

        // 🔍 Search bar
        MemberSearchBar(
            query = query,
            onQueryChange = { query = it }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(
                items = filteredMembers,
                key = { it.memberId } // 🔑 REQUIRED for smooth animation
            ) { member ->

                val isActive = System.currentTimeMillis() < member.planEnd

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        // ✨ animated placement when filtering
                        .animateItemPlacement(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                        .clickable {
                            nav.navigate("detail/${member.memberId}")
                        },
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {

                        Text(
                            text = member.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(4.dp))

                        Text("📞 ${member.phone}", fontSize = 14.sp)

                        Spacer(Modifier.height(6.dp))

                        Text(
                            text = if (isActive) "🟢 Active Plan" else "🔴 Plan Expired",
                            color = if (isActive) Color(0xFF2E7D32) else Color.Red,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}


fun Modifier.pressScale(): Modifier = composed {
    val scale = remember { Animatable(1f) }

    this.pointerInput(Unit) {
        detectTapGestures(
            onPress = {
                scale.animateTo(0.96f, tween(120))
                tryAwaitRelease()
                scale.animateTo(1f, tween(120))
            }
        )
    }.graphicsLayer {
        scaleX = scale.value
        scaleY = scale.value
    }
}



