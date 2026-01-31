package com.luffy1705.gym.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "attendance", foreignKeys = [
        ForeignKey(
            entity = MemberEntity::class,
            parentColumns = ["memberId"],
            childColumns = ["memberId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class AttendanceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val memberId: Int,
    val date: String,
    val entryTime: Long,
    val exitTime: Long? = null,
    val totalMinutes: Int? = null
)