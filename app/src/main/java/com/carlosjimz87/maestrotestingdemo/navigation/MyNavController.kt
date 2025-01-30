package com.carlosjimz87.maestrotestingdemo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.carlosjimz87.maestrotestingdemo.ui.screens.DelayedOperationScreen
import com.carlosjimz87.maestrotestingdemo.ui.screens.DetailsScreen
import com.carlosjimz87.maestrotestingdemo.ui.screens.HomeScreen
import com.carlosjimz87.maestrotestingdemo.ui.screens.LoginScreen

@Composable
fun MyNavController(navController: NavHostController) {
    NavHost(navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("details/{item}") { backStackEntry ->
            val item = backStackEntry.arguments?.getString("item") ?: "Unknown"
            DetailsScreen(item)
        }
        composable("delayed") { DelayedOperationScreen() }
    }
}