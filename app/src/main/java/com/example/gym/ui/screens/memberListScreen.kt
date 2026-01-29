package com.example.gym.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gym.ui.viewModel.MemberViewModel

@Composable
fun MemberListScreen(nav: NavController, vm: MemberViewModel) {
    val members by vm.member.collectAsState()

    LazyColumn {
        items(members) { member ->
            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        nav.navigate("detail/${member.memberId}")
                    }
            ) {
                Text(member.name, Modifier.padding(16.dp))
            }
        }
    }
}
