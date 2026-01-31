package com.luffy1705.gym.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.luffy1705.gym.ui.component.SuccessCelebration
import com.luffy1705.gym.ui.viewModel.MemberViewModel


@Composable
fun AddMemberScreen(
    nav: NavController,
    vm: MemberViewModel
) {

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var months by remember { mutableStateOf("") }
    var showSuccess by remember { mutableStateOf(false) }

    val isValid by remember(name, phone, months) {
        derivedStateOf {
            name.isNotBlank() &&
                    phone.length >= 10 &&
                    months.toIntOrNull()?.let { it > 0 } == true
        }
    }

    // 🔥 Entry animation
    val offsetY = remember { Animatable(40f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        offsetY.animateTo(0f, tween(500))
        alpha.animateTo(1f, tween(500))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .graphicsLayer {
                translationY = offsetY.value
                this.alpha = alpha.value
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Add New Member", fontSize = 22.sp)

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Member Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it.filter { c -> c.isDigit() } },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = months,
            onValueChange = { months = it.filter { c -> c.isDigit() } },
            label = { Text("Plan Duration (months)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        // 🔥 Animated button
        val scale by animateFloatAsState(
            targetValue = if (isValid) 1f else 0.95f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        )
        Button(
            onClick = {
                vm.addMember(name, phone, months.toInt())
                showSuccess = true
            },
            enabled = isValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Member")
        }
        if (showSuccess) {
            SuccessCelebration(
                message = "Member Added Successfully!"
            ) {
                nav.popBackStack()
            }
        }

//        Button(
//            onClick = {
//                vm.addMember(name, phone, months.toInt())
//                nav.popBackStack()
//            },
//            enabled = isValid,
//            modifier = Modifier
//                .fillMaxWidth()
//                .graphicsLayer {
//                    scaleX = scale
//                    scaleY = scale
//                }
//        ) {
//            Text("Save Member")
//        }
    }
}
