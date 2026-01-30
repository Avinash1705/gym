package com.example.gym.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gym.ui.viewModel.MemberViewModel

@Composable
fun MemberListScreen(nav: NavController, vm: MemberViewModel) {
    val members by vm.member.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(members) { member ->

            val isActive = System.currentTimeMillis() < member.planEnd

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable {
                        nav.navigate("detail/${member.memberId}")
                    },
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(Modifier.padding(16.dp)) {

                    // Name
                    Text(
                        text = member.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(4.dp))

                    // Phone
                    Text(
                        text = "📞 ${member.phone}",
                        fontSize = 14.sp
                    )

                    Spacer(Modifier.height(6.dp))

                    // Plan status
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

