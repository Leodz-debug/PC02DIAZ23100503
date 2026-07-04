package com.example.pc02diaz23100503

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.pc02diaz23100503.presentation.navigation.AppNavigation
import com.example.pc02diaz23100503.presentation.navigation.Routes
import com.example.pc02diaz23100503.ui.theme.PC02DIAZ23100503Theme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PC02DIAZ23100503Theme {
                val startDestination = if (FirebaseAuth.getInstance().currentUser != null) {
                    Routes.Home.route
                } else {
                    Routes.Login.route
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(startDestination = startDestination)
                }
            }
        }
    }
}
