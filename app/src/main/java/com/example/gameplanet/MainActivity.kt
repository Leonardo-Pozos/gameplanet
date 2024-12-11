package com.example.gameplanet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gameplanet.ui.screens.DetalleGameScreen
import com.example.gameplanet.ui.screens.HomeScreen
import com.example.gameplanet.ui.screens.ListaDeDeseoScreen
import com.example.gameplanet.ui.screens.LoginScreen
import com.example.gameplanet.ui.screens.RegistroScreen
import com.example.gameplanet.ui.theme.GamePlanetTheme
import com.example.gameplanet.utils.Screens
import com.example.gameplanet.utils.SharedPreference
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GamePlanetTheme {
                val navController = rememberNavController()
                val sharedPref = SharedPreference(LocalContext.current)
                val isLogged = sharedPref.getisLoggedSharedPref()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet {
                            Text("GamePlanet", modifier = Modifier.padding(16.dp), fontSize = 24.sp)
                            HorizontalDivider()
                            NavigationDrawerItem(
                                label = { Text(text = "Inicio", fontSize = 20.sp) },
                                selected = false,
                                onClick = { navController.navigate(Screens.Home.route) }
                            )
                            NavigationDrawerItem(
                                label = { Text(text = "Mi lista de deseos", fontSize = 20.sp) },
                                selected = false,
                                onClick = { navController.navigate(Screens.ListaDeseo.route) }
                            )
                        }
                    },
                ){
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        floatingActionButton = {
                            val routeActual = navController.currentBackStackEntryAsState().value?.destination?.route
                            if(routeActual != "login" && routeActual != "registro"){
                                ExtendedFloatingActionButton(
                                    text = { Text("Menu") },
                                    icon = { Icon(Icons.Filled.Menu, contentDescription = "") },
                                    onClick = {
                                        scope.launch {
                                            drawerState.apply {
                                                if (isClosed) open() else close()
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    ) { innerPadding ->
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
                                DetalleGameScreen(innerPadding, idGame, navController)
                            }
                            composable(route = Screens.ListaDeseo.route){
                                ListaDeDeseoScreen(innerPadding)
                            }
                        }
                    }
                }
            }
        }
    }
}