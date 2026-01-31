package com.luffy1705.gym.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members")
data class MemberEntity(
    @PrimaryKey(autoGenerate = true) val memberId: Int = 0,
    val name: String,
    val phone: String,
    val planStart: Long,
    val planEnd: Long,

    val entryTime: Long? = null,
    val exitTime: Long? = null
)
