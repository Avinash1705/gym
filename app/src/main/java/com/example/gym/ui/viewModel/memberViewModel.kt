package com.example.gym.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gym.data.local.AppDatabase
import com.example.gym.data.local.MemberEntity
import com.example.gym.data.repositary.GymRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MemberViewModel( application: Application) : AndroidViewModel(application) {
    private val repo = GymRepository(AppDatabase.getDatabase(application))

    val member =
        repo.getMembers().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun addMember(name: String, phone: String) {
        viewModelScope.launch {
            repo.addMember(
                MemberEntity(
                    name = name,
                    phone = phone,
                    planStart = System.currentTimeMillis(),
                    planEnd = System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000
                )
            )
        }
    }
    fun markEntry(id: Int) {
        viewModelScope.launch { repo.markEntry(id) }
    }

    fun markExit(id: Int) {
        viewModelScope.launch { repo.markExit(id) }
    }
}