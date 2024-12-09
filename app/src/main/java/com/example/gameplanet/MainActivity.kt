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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gameplanet.ui.screens.DetalleGameScreen
import com.example.gameplanet.ui.screens.HomeScreen
import com.example.gameplanet.ui.screens.LoginScreen
import com.example.gameplanet.ui.screens.RegistroScreen
import com.example.gameplanet.ui.theme.GamePlanetTheme
import com.example.gameplanet.utils.Screens
import com.example.gameplanet.utils.SharedPreference

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GamePlanetTheme {
                val navController = rememberNavController()
                val sharedPref = SharedPreference(LocalContext.current)
                val isLogged = sharedPref.getisLoggedSharedPref()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(navController = navController, startDestination = if(isLogged) Screens.Home.route else Screens.Login.route) {
                        composable(route = Screens.Login.route){
                            LoginScreen(innerPadding, navController)
                        }
                        composable(route = Screens.Registro.route){
                            RegistroScreen(innerPadding, navController)
                        }
                        composable(route = Screens.Home.route){
                            HomeScreen(innerPadding, navController)
                        }
                        composable(
                            route = Screens.DetalleGame.route+"/{idGame}",
                            arguments = listOf(navArgument("idGame"){
                                type = NavType.IntType
                                nullable = false
                            })
                        ){
                            val idGame = it.arguments?.getInt("idGame") ?: 0
                            DetalleGameScreen(innerPadding, idGame)
                        }
                    }
                }
            }
        }
    }
}