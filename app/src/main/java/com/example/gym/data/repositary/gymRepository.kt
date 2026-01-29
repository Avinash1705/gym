package com.example.gym.data.repositary

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.gym.data.local.AppDatabase
import com.example.gym.data.local.AttendanceEntity
import com.example.gym.data.local.MemberEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalTime

class GymRepository(private val db: AppDatabase) {
    fun getMembers() = db.memberDao().getAllMembers()

    suspend fun addMember(memberEntity: MemberEntity) {
        db.memberDao().insertMember(memberEntity)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun markEntry(memberId: Int) {
        val today = LocalDate.now().toString()
        val existing = db.attendanceDao().getTodayAttendance(memberId, today)
        if (existing == null) {
            db.attendanceDao().insert(
                AttendanceEntity(
                    memberId = memberId,
                    date = today,
                    entryTime = System.currentTimeMillis()
                )
            )
        }
        print("entry");
        Log.d("rawat", "markEntry: $today")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun markExit(memberId: Int){
        val today = LocalDate.now().toString()
        val attendance = db.attendanceDao().getTodayAttendance(memberId,today)?:return
        val exitTime = System.currentTimeMillis()
        val min = ((exitTime - attendance.entryTime)/60000).toInt()

        db.attendanceDao().update(attendance.copy(exitTime=exitTime, totalMinutes = min))
    }
}