package com.example.gameplanet.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
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
fun DetalleGameScreen(paddingValues: PaddingValues, idGame: Int){
    val platformIcons = mapOf(
        "Pc" to R.drawable.pc,
        "Xbox" to R.drawable.xbox,
        "Playstation" to R.drawable.playstation,
        "Switch" to R.drawable.nintendo,
        "PC" to R.drawable.pc,
        "XBOX" to R.drawable.xbox,
        "PLAYSTATION" to R.drawable.playstation,
    )

    var game by remember {
        mutableStateOf(Game(
            nombre = "",
            imagen = "",
            desarrollador = "",
            plataforma = "",
            clasificacion = "",
            descripcion = "",
            id = 0
        ))
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = true) {
        scope.launch {
            val BASE_URL = "http://157.230.89.111:8000/"
            val gameService = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GameService::class.java)
            val response = gameService.getGameById(idGame)
            isLoading = false
            game = response
        }
    }

    if(isLoading){
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }
    }else{
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .padding(paddingValues)
                .padding(start = 16.dp, end = 16.dp, top = 25.dp, bottom = 15.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient( // Degradado
                            colors = listOf(Color(0xFFFAFAFA), Color(0xFFECECEC))
                        )
                    )
            ) {
                AsyncImage(
                    model = game.imagen,
                    contentDescription = game.nombre,
                    modifier = Modifier
                        .height(250.dp),
                    placeholder = painterResource(id = R.drawable.image_not_found),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(20.dp))
                Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp)
                ) {
                    // Nombre centrado en la parte superior
                    Text(
                        text = game.nombre,
                        fontSize = 24.sp,
                        modifier = Modifier.align(Alignment.TopCenter),
                        fontWeight = FontWeight.Bold
                    )

                    // Contenido principal en la parte superior izquierda
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 50.dp), // Deja espacio para el nombre
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = game.descripcion,
                            fontSize = 18.sp,
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        Text(
                            text = "Desarrollador: ${game.desarrollador}",
                            fontSize = 18.sp
                        )
                    }

                    // Contenido en la parte inferior izquierda
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp, start = 15.dp)
                            .align(Alignment.BottomStart),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Rated: ${game.clasificacion}",
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Disponible en plataformas:",
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            game.plataforma.split(", ").forEach { platform ->
                                val iconRes = platformIcons[platform.trim()]
                                if (iconRes != null) {
                                    Image(
                                        painter = painterResource(id = iconRes),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(15.dp))
                                }
                            }
                        }
                    }
                }


            }
        }
    }
}