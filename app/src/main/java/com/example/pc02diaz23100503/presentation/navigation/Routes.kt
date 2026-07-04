package com.example.pc02diaz23100503.presentation.navigation

sealed class Routes(val route: String) {
    object Login : Routes("login")
    object Home : Routes("home")
    object Converter : Routes("converter")
    object History : Routes("history")
}
