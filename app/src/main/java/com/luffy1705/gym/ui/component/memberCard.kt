package com.luffy1705.gym.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luffy1705.gym.data.local.MemberEntity


@Composable
fun MemberCard(
    member: MemberEntity,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

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

            // Plan Status
            Text(
                text = if (isActive) "🟢 Active Plan" else "🔴 Plan Expired",
                color = if (isActive) Color(0xFF2E7D32) else Color.Red,
                fontSize = 13.sp
            )
        }
    }
}
