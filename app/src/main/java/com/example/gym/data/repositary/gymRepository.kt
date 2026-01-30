package com.example.gym.data.repository

import android.util.Log
import com.example.gym.data.local.AppDatabase
import com.example.gym.data.local.AttendanceEntity
import com.example.gym.data.local.MemberEntity
import com.example.gym.data.model.MonthlySummary
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class GymRepository(private val db: AppDatabase) {

    fun getMembers() = db.memberDao().getAllMembers()

    suspend fun addMember(memberEntity: MemberEntity) {
        db.memberDao().insertMember(memberEntity)
    }
    fun getTodayAttendance(memberId: Int): Flow<AttendanceEntity?> {
        val today = today()
        return db.attendanceDao().observeTodayAttendance(memberId, today)
    }
    suspend fun markEntry(memberId: Int) {
        val today = today()
        val existing = db.attendanceDao().getTodayAttendance(memberId, today)

        if (existing == null) {
            db.attendanceDao().insert(
                AttendanceEntity(
                    memberId = memberId,
                    date = today,
                    entryTime = System.currentTimeMillis()
                )
            )
            Log.d("GymRepository", "Entry marked for $memberId on $today")
        }
    }

    suspend fun markExit(memberId: Int) {
        val today = today()
        val attendance =
            db.attendanceDao().getTodayAttendance(memberId, today) ?: return

        val exitTime = System.currentTimeMillis()
        val minutes = ((exitTime - attendance.entryTime) / 60000).toInt()

        db.attendanceDao().update(
            attendance.copy(
                exitTime = exitTime,
                totalMinutes = minutes
            )
        )

        Log.d("GymRepository", "Exit marked for $memberId, minutes=$minutes")
    }

    private fun today(): String {
        return SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        ).format(Date())
    }

    suspend fun getMonthlyAttendance(
        memberId: Int,
        month: String
    ):List<AttendanceEntity> {
        return    db.attendanceDao().getMonthlyAttendance(memberId,month)

    }
    suspend fun getMonthlySummary(
        memberId: Int,
        month: String
    ): MonthlySummary =
        db.attendanceDao().getMonthlySummary(memberId, month)

    suspend fun insertDummyAttendance(memberId: Int) {

        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        for (i in 0 until 10) {

            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_MONTH, -i)

            val date = formatter.format(calendar.time)

            val minutes = (30..120).random()   // variable time (30–120 mins)

            db.attendanceDao().insert(
                AttendanceEntity(
                    memberId = memberId,
                    date = date,
                    entryTime = System.currentTimeMillis() - minutes * 60 * 1000,
                    exitTime = System.currentTimeMillis(),
                    totalMinutes = minutes
                )
            )
        }

        Log.d("DummyData", "Inserted 10 days dummy attendance")
    }


}

