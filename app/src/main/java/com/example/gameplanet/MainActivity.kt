package com.example.gameplanet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gameplanet.ui.screens.HomeScreen
import com.example.gameplanet.ui.screens.LoginScreen
import com.example.gameplanet.ui.screens.RegistroScreen
import com.example.gameplanet.ui.theme.GamePlanetTheme
import com.example.gameplanet.utils.Screens

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            GamePlanetTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(navController = navController, startDestination = Screens.Login.route) {
                        composable(route = Screens.Login.route){
                            LoginScreen(innerPadding)
                        }
                        composable(route = Screens.Registro.route){
                            RegistroScreen(innerPadding)
                        }
                        composable(route = Screens.Home.route){
                            HomeScreen(innerPadding)
                        }
                    }
                }
            }
        }
    }
}