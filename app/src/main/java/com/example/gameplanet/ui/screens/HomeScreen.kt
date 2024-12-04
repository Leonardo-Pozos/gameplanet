package com.example.gameplanet.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.gameplanet.R
import com.example.gameplanet.models.Game
import com.example.gameplanet.services.GameService
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun HomeScreen(innerPadding: PaddingValues){
    var games by remember {
        mutableStateOf(listOf<Game>())
    }
    var isLoading by remember{
        mutableStateOf(true)
    }
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = true){
        scope.launch {
            val BASE_URL = "http://157.230.89.111/"
            val gameService = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GameService::class.java)
            try {
                val response = gameService.getGames()
                isLoading = false
                games = response
                Log.i("Responsess", response.toString())
            } catch (e: Exception) {
                Log.e("Errorr", e.toString())
            }
        }
    }
    if(isLoading){
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }
    }else{
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Text(
                text = "EXPLORA TODO NUESTRO VIDEOJUEGOS",
                modifier = Modifier.fillMaxWidth().padding(top = 25.dp, start = 20.dp, end = 20.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )
            LazyVerticalGrid(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5)), // Fondo general
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(games) { game ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .height(170.dp),
                        shape = RoundedCornerShape(16.dp), // Bordes redondeados
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient( // Degradado
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
                                    .clip(RoundedCornerShape(12.dp)) // Imagen redondeada
                                    .align(Alignment.CenterHorizontally),
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(id = R.drawable.image_not_found)
                            )
                            Spacer(modifier = Modifier.height(8.dp)) // Separación entre imagen y texto
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