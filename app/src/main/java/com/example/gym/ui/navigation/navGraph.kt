package com.example.gym.ui.navigation

import DashboardScreen
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gym.ui.screens.AddMemberScreen
import com.example.gym.ui.screens.MemberDetailScreen
import com.example.gym.ui.screens.MemberListScreen
import com.example.gym.ui.screens.MonthlyAttendanceScreen
import com.example.gym.ui.viewModel.MemberViewModel

@ExperimentalMaterial3Api
@Composable
fun NavGraph(vm: MemberViewModel){
    val nav = rememberNavController()


    NavHost(nav, startDestination = "dashboard") {
        composable("dashboard") { DashboardScreen(nav, vm) }
        composable("add") { AddMemberScreen(nav, vm) }
        composable("list") { MemberListScreen(nav, vm) }
        composable("detail/{id}") {
            MemberDetailScreen(it.arguments!!.getString("id")!!.toInt(), vm,nav)
        }
        composable(route = "monthly/{id}", arguments = listOf(navArgument("id"){type = NavType.IntType})) {
            MonthlyAttendanceScreen(memberId = it.arguments!!.getInt("id"),vm)
        }
    }
}