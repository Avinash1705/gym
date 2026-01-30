package com.example.gym.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
//import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gym.data.local.MemberEntity
import com.example.gym.ui.component.AnimatedDeleteDialog
import com.example.gym.ui.component.DeleteExplosion
import com.example.gym.ui.component.MemberCard
import com.example.gym.ui.component.MemberSearchBar
import com.example.gym.ui.viewModel.MemberViewModel


@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun MemberListScreen(
    nav: NavController,
    vm: MemberViewModel
) {
    val members by vm.member.collectAsState()
    var query by remember { mutableStateOf("") }
    var explode by remember { mutableStateOf(false) }

    // 🔍 Search filter
    val filteredMembers = remember(members, query) {
        if (query.isBlank()) members
        else members.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.phone.contains(query)
        }
    }

    var showDialog by remember { mutableStateOf(false) }
    var selectedMember by remember { mutableStateOf<MemberEntity?>(null) }
//    LaunchedEffect(Unit) {
//        vm.insertDummyData()
//    }
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
                key = { it.memberId }
            ) { member ->

                val isActive = System.currentTimeMillis() < member.planEnd

                val dismissState = rememberDismissState(
                    confirmStateChange = {
                        if (it == DismissValue.DismissedToStart) {
                            selectedMember = member
                            showDialog = true
                        }
                        false
                    }
                )

                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(DismissDirection.EndToStart),

                    // 🔥 Swipe background
                    background = {
                        val progress = dismissState.progress.fraction
                        val pulse by rememberInfiniteTransition().animateFloat(
                            initialValue = 1f,
                            targetValue = 1.1f,
                            animationSpec = infiniteRepeatable(
                                tween(500),
                                RepeatMode.Reverse
                            )
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Red)
                                .graphicsLayer {
                                    scaleX =
                                        if (dismissState.dismissDirection != null) pulse else 1f
                                }
                                .padding(end = 24.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Text(
                                text = "🗑️",
                                fontSize = (22 + progress * 10).sp
                            )
                        }
                    },

                    // 🔥 Card animation
                    dismissContent = {
                        val scale = 1f - dismissState.progress.fraction * 0.08f
                        val rotation = -dismissState.progress.fraction * 6f

                        Box(
                            Modifier
                                .animateItemPlacement(
                                    spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                )
                                .graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                    rotationZ = rotation
                                }
                        ) {
                            androidx.compose.animation.AnimatedVisibility(
                                visible = !explode,
                                exit = shrinkVertically(animationSpec = tween(300)) + fadeOut()
                            ) {
                                MemberCard(
                                    member = member,
                                    isActive = isActive,
                                    onClick = {
                                        nav.navigate("detail/${member.memberId}")
                                    }
                                )
                            }
                        }
                    }
                )
            }
        }
    }

    // 🧨 Delete confirmation dialog
    if (showDialog && selectedMember != null) {
        AnimatedDeleteDialog(
            memberName = selectedMember!!.name,
            onConfirm = {
                vm.deleteMember(selectedMember!!)
                showDialog = false
                explode = true
            },
            onDismiss = {
                showDialog = false
            }
        )
    }
    DeleteExplosion(
        visible = explode,
        onFinished = {
            vm.deleteMember(selectedMember!!)
            explode = false
            showDialog = false
        }
    )
}

/**
 * 🔥 Press scale animation (optional utility)
 */
fun Modifier.pressScale(): Modifier = composed {
    val scale = remember { Animatable(1f) }

    this
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = {
                    scale.animateTo(0.96f, tween(120))
                    tryAwaitRelease()
                    scale.animateTo(1f, tween(120))
                }
            )
        }
        .graphicsLayer {
            scaleX = scale.value
            scaleY = scale.value
        }
}
