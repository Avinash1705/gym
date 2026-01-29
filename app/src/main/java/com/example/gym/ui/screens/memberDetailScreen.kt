package com.example.gym.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gym.ui.viewModel.MemberViewModel

@Composable
fun MemberDetailScreen(id: Int, vm: MemberViewModel) {
    Column(Modifier.padding(16.dp)) {
        Button(onClick = { vm.markEntry(id) }) {
            Text("Mark Entry")
        }

        Button(onClick = { vm.markExit(id) }) {
            Text("Mark Exit")
        }
    }
}
