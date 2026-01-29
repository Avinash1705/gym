package com.example.gym.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.gym.data.local.AppDatabase
import com.example.gym.data.local.MemberEntity
import com.example.gym.data.repositary.GymRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GymRepositoryTest {

    private lateinit var db: AppDatabase
    private lateinit var repository: GymRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        repository = GymRepository(db)
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertMember_and_readMembers() = runBlocking {
        val member = MemberEntity(
            name = "Avinash",
            phone = "9999999999",
            planStart = 0L,
            planEnd = 1000L
        )

        repository.addMember(member)

        val members = repository.getMembers().first()

        //expectation
        Assert.assertEquals(1, members.size)
        Assert.assertEquals("Avinash", members[0].name)
    }
}