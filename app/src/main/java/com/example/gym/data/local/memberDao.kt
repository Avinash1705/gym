package com.example.gym.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {
    @Insert
    suspend fun insertMember(memberEntity: MemberEntity)

    @Query("SELECT * FROM members")
    fun getAllMembers(): Flow<List<MemberEntity>>

    @Query("SELECT * FROM members WHERE memberId=:id")
    suspend fun getMember(id: Int): MemberEntity

    @Query("UPDATE members SET entryTime = :time, exitTime = NULL WHERE memberId = :id")
    suspend fun markEntry(id: Int, time: Long)

    @Query("UPDATE members SET exitTime = :time WHERE memberId = :id")
    suspend fun markExit(id: Int, time: Long)
}