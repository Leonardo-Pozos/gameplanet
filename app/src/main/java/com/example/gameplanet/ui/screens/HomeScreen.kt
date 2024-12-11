package com.example.gameplanet.ui.screens

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.gameplanet.R
import com.example.gameplanet.models.Game
import com.example.gameplanet.services.GameService
import com.example.gameplanet.utils.Earth
import com.example.gameplanet.utils.Logout
import com.example.gameplanet.utils.Screens
import com.example.gameplanet.utils.SharedPreference
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun HomeScreen(innerPadding: PaddingValues, navController: NavController) {
    val sharedPreference = SharedPreference(LocalContext.current)
    val name = sharedPreference.getUserNameSharedPref() ?: ""
    var games by remember { mutableStateOf(listOf<Game>()) }
    var allGames by remember { mutableStateOf(listOf<Game>()) }
    var isLoading by remember { mutableStateOf(true) }
    var query by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchBarVisible by remember { mutableStateOf(false) }
    var filterType by remember { mutableStateOf("Clasificación") } // Nuevo: Tipo de filtro
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = searchQuery, key2 = filterType) {
        scope.launch {
            val BASE_URL = "http://157.230.89.111:8000/"
            val gameService = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GameService::class.java)

            try {
                isLoading = true
                val response = when {
                    searchQuery.isEmpty() -> gameService.getGames()
                    filterType == "Clasificación" -> gameService.getGameByClasificacion(searchQuery)
                    else -> gameService.getGameByDesarrollador(searchQuery)
                }
                isLoading = false
                allGames = response
                games = allGames
            } catch (e: Exception) {
                Log.e("Error", e.toString())
                isLoading = false
            }
        }
    }

    fun filterGames() {
        games = if (searchQuery.isEmpty()) {
            allGames
        } else {
            allGames.filter {
                if (filterType == "Clasificación") {
                    it.clasificacion.contains(searchQuery, ignoreCase = true)
                } else {
                    it.desarrollador.contains(searchQuery, ignoreCase = true)
                }
            }
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Earth,
                    contentDescription = "Earth",
                    modifier = Modifier.size(60.dp).weight(1f)
                )
                Column(
                    modifier = Modifier.weight(3f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Bienvenido",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = name,
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 3.dp, start = 10.dp)
                    )
                }
                IconButton(onClick = {
                    sharedPreference.removeUserSheredPref()
                    navController.navigate(Screens.Login.route) {
                        popUpTo(Screens.Login.route) { inclusive = true }
                    }
                }) {
                    Icon(
                        imageVector = Logout,
                        contentDescription = "Logout",
                        modifier = Modifier.size(30.dp).weight(1f)
                    )
                }
            }
            if (isSearchBarVisible) {
                Column(
                    modifier = Modifier
                        .animateContentSize()
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    TextField(
                        value = query,
                        onValueChange = { query = it },
                        label = {
                            Text(
                                "Buscar por ${filterType.lowercase()} (Ej. R18 o Ubisoft)"
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFEFEFEF), shape = RoundedCornerShape(8.dp)),
                        singleLine = true,
                        trailingIcon = {
                            if (query.isNotEmpty()) {
                                IconButton(onClick = { query = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear search"
                                    )
                                }
                            }
                        }
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                filterType = "Clasificación"
                                searchQuery = query
                                filterGames()
                            },
                            modifier = Modifier.weight(1f).padding(end = 4.dp)
                        ) {
                            Text("Clasificación")
                        }
                        Button(
                            onClick = {
                                filterType = "Desarrollador"
                                searchQuery = query
                                filterGames()
                            },
                            modifier = Modifier.weight(1f).padding(start = 4.dp)
                        ) {
                            Text("Desarrollador")
                        }
                    }
                }
            }
            IconButton(
                onClick = { isSearchBarVisible = !isSearchBarVisible },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Icon(
                    imageVector = if (isSearchBarVisible) Icons.Default.Clear else Icons.Default.Search,
                    contentDescription = "Toggle Search"
                )
            }
            Text(
                text = "EXPLORA TODOS NUESTROS VIDEOJUEGOS",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
            )
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5)),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(games) { game ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .height(170.dp)
                            .clickable { navController.navigate(Screens.DetalleGame.route + "/${game.id}") },
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color(0xFFFAFAFA), Color(0xFFECECEC))
                                    )
                                )
                                .padding(8.dp)
                        ) {
                            AsyncImage(
                                model = game.imagen,
                                contentDescription = game.nombre,
                                modifier = Modifier
                                    .height(120.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .align(Alignment.CenterHorizontally),
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(id = R.drawable.image_not_found)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = game.nombre,
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = Color(0xFF333333)
                            )
                        }
                    }
                }
            }
        }
    }
}
