package com.luffy1705.gym.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.luffy1705.gym.data.local.AppDatabase
import com.luffy1705.gym.data.local.AttendanceEntity
import com.luffy1705.gym.data.local.MemberEntity
import com.luffy1705.gym.data.model.MonthItem
import com.luffy1705.gym.data.model.MonthlySummary
import com.luffy1705.gym.data.repository.GymRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.collections.emptyList

class MemberViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = GymRepository(
        AppDatabase.getDatabase(application)
    )
    private val _monthlySummary = MutableStateFlow(MonthlySummary(0,0))
    val monthlySummary : StateFlow<MonthlySummary> = _monthlySummary

    //monthly attendence at one
    private val _monthlyAttendanceList = MutableStateFlow<List<AttendanceEntity>>(emptyList())
    val monthlyAttendanceList = _monthlyAttendanceList
    // ---------------- MEMBERS ----------------

    val member =
        repo.getMembers()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    fun addMember(name: String, phone: String,months: Int) {
        val start = System.currentTimeMillis()
        val end = start + months * 30L * 24 * 60 * 60 * 1000
        viewModelScope.launch {
            repo.addMember(
                MemberEntity(
                    name = name,
                    phone = phone,
                    planStart = start,
                    planEnd = end,
                )
            )
        }
    }

    // ---------------- ATTENDANCE ----------------

    /** Observe today's attendance for a member */
    fun getTodayAttendance(memberId: Int): Flow<AttendanceEntity?> {
        return repo.getTodayAttendance(memberId)
    }

    fun markEntry(memberId: Int) {
        viewModelScope.launch {
            repo.markEntry(memberId)
        }
    }

    fun markExit(memberId: Int) {
        viewModelScope.launch {
            repo.markExit(memberId)
        }
    }

    fun loadMonthlyAttendance(
        memberId: Int,
        month: String
    ) = viewModelScope.launch {
        monthlyAttendanceList.value = repo.getMonthlyAttendance(memberId,month)
    }
//        repo.getMonthlyAttendance(memberId, month)
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun loadMonthlySummary(memberId: Int, month: String) =
//        repo.getMonthlySummary(memberId, month).stateIn(
//            viewModelScope,
//            SharingStarted.WhileSubscribed(5000), MonthlySummary(0, 0)
//        )
        viewModelScope.launch {
            _monthlySummary.value = repo.getMonthlySummary(memberId,month)
        }
    fun insertDummyData() {
        viewModelScope.launch {
            repo.insertDummyAttendance(1)
        }
    }
    fun getLast12Months(): List<MonthItem> {
        val list = mutableListOf<MonthItem>()
        val calendar = Calendar.getInstance()
        val labelFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        val valueFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())

        repeat(12) {
            list.add(
                MonthItem(
                    label = labelFormat.format(calendar.time),
                    value = valueFormat.format(calendar.time)
                )
            )
            calendar.add(Calendar.MONTH, -1)
        }
        return list
    }

    fun deleteMember(member: MemberEntity) {
        viewModelScope.launch {
            repo.deleteMember(member)
        }
    }
}
