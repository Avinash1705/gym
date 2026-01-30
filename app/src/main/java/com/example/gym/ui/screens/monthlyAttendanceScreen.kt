package com.example.gym.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gym.data.model.BarUiModel
import com.example.gym.ui.component.MonthDropdown
import com.example.gym.ui.components.MonthlyBarChart
import com.example.gym.ui.viewModel.MemberViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@ExperimentalMaterial3Api
@Composable
fun MonthlyAttendanceScreen(
    memberId: Int,
    vm: MemberViewModel
) {
    val months = remember { vm.getLast12Months() }
    var selectedMonth by remember { mutableStateOf(months.first()) }

    val summary by vm.monthlySummary.collectAsState()
    val records by vm.monthlyAttendanceList.collectAsState()

    LaunchedEffect(memberId, selectedMonth.value) {
        vm.loadMonthlySummary(memberId, selectedMonth.value)
        vm.loadMonthlyAttendance(memberId, selectedMonth.value)
    }

    val bars = remember(records) {
        records.map {
            BarUiModel(
                day = it.date.takeLast(2).toInt(),
                minutes = (it.totalMinutes ?: 0).coerceAtLeast(5)
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        // 🔽 MONTH PICKER
        MonthDropdown(
            selected = selectedMonth,
            months = months,
            onSelect = { selectedMonth = it }
        )

        Spacer(Modifier.height(12.dp))

        Text("🗓 Days Attended: ${summary.totalDays}")
        Text("⏱ Total Time: ${summary.totalMinutes} mins")

        Divider(Modifier.padding(vertical = 12.dp))

        MonthlyBarChart(bars)

        Spacer(Modifier.height(12.dp))

        records.forEach {
            Text("${it.date} → ${it.totalMinutes ?: 0} mins")
        }
    }
}
