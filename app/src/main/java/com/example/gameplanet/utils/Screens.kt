package com.example.gameplanet.utils

sealed class Screens (val route: String){
    data object Home : Screens("home")
    data object Registro : Screens("registro")
    data object Login : Screens("login")
}