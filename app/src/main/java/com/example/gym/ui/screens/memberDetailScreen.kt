package com.example.gym.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.navArgument
import com.example.gym.ui.viewModel.MemberViewModel

@Composable
fun MemberDetailScreen(id: Int, vm: MemberViewModel,nav : NavController) {

    val members by vm.member.collectAsState()
    val member = members.firstOrNull { it.memberId == id }

    val todayAttendance by vm.getTodayAttendance(id)
        .collectAsState(initial = null)

    if (member == null) {
        Text("Member not found", Modifier.padding(16.dp))
        return
    }

    val isActive = System.currentTimeMillis() < member.planEnd
    val isInside = todayAttendance != null && todayAttendance?.exitTime == null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = member.name,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Text("📞 ${member.phone}", fontSize = 16.sp)

        Divider()

        // Plan status
        Text(
            text = if (isActive) "🟢 Plan Active" else "🔴 Plan Expired",
            color = if (isActive) Color(0xFF2E7D32) else Color.Red,
            fontSize = 14.sp
        )

        Spacer(Modifier.height(8.dp))

        // Entry / Exit Status
        Text(
            text = if (isInside) "🏋️ Member is inside gym"
            else "🚪 Member is outside",
            fontSize = 14.sp
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { vm.markEntry(id) },
            enabled = isActive && !isInside,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Mark Entry")
        }

        Button(
            onClick = { vm.markExit(id) },
            enabled = isInside,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Mark Exit")
        }
        Button(onClick = {
            nav.navigate("monthly/$id")
        }) {
            Text("View Monthly Attendance")
        }
    }
}


