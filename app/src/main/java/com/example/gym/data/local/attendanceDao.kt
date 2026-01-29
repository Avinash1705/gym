package com.example.gym.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AttendanceDao{

    @Insert
    suspend fun insert(attendanceEntity: AttendanceEntity)

    @Update
    suspend fun update(attendanceEntity: AttendanceEntity)

    @Query("SELECT * FROM attendance WHERE memberId=:id AND date=:date LIMIT 1")
    suspend fun getTodayAttendance(id:Int ,date :String ) : AttendanceEntity?
}