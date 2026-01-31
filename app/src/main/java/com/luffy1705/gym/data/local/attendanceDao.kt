package com.luffy1705.gym.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.luffy1705.gym.data.model.MonthlySummary
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceDao {

    // ---------- INSERT / UPDATE ----------

    @Insert
    suspend fun insert(attendanceEntity: AttendanceEntity)

    @Update
    suspend fun update(attendanceEntity: AttendanceEntity)

    // ---------- ONE-TIME QUERY (ACTIONS) ----------

    @Query(
        """
        SELECT * FROM attendance 
        WHERE memberId = :id AND date = :date 
        LIMIT 1
    """
    )
    suspend fun getTodayAttendance(
        id: Int,
        date: String
    ): AttendanceEntity?

    // ---------- OBSERVE (UI / COMPOSE) ----------

    @Query(
        """
        SELECT * FROM attendance 
        WHERE memberId = :id AND date = :date 
        LIMIT 1
    """
    )
    fun observeTodayAttendance(
        id: Int,
        date: String
    ): Flow<AttendanceEntity?>

    @Query(
        """
    SELECT * FROM attendance
    WHERE memberId = :memberId
    AND date LIKE :month || '%'
    ORDER BY date DESC
"""
    )
   suspend fun getMonthlyAttendance(
        memberId: Int,
        month: String   // format: "2026-01"
    ): List<AttendanceEntity>

    @Query("""
    SELECT 
        COUNT(*) as totalDays,
        IFNULL(SUM(totalMinutes), 0) as totalMinutes
    FROM attendance
    WHERE memberId = :memberId
    AND date LIKE :month || '%'
""")
//    fun getMonthlySummary(
//        memberId: Int,
//        month: String
//    ): Flow<MonthlySummary>
   suspend fun getMonthlySummary(
        memberId: Int,
        month: String
    ): MonthlySummary

    @Query("DELETE FROM attendance WHERE memberId = :id")
    suspend fun deleteByMemberId(id: Int)
}
