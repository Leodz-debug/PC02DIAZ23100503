package com.example.pc02diaz23100503.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pc02diaz23100503.presentation.auth.LoginScreen
import com.example.pc02diaz23100503.presentation.auth.LoginViewModel
import com.example.pc02diaz23100503.presentation.converter.ConverterScreen
import com.example.pc02diaz23100503.presentation.history.HistoryScreen

@Composable
fun AppNavigation(startDestination: String) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.Home.route) {
            HomeScreen(
                onLogout = {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(Routes.Home.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.Converter.route) {
            ConverterScreen()
        }
        composable(Routes.History.route) {
            HistoryScreen()
        }
    }
}
