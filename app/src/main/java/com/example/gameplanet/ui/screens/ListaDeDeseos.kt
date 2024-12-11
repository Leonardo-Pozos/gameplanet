package com.example.gameplanet.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.gameplanet.R
import com.example.gameplanet.models.Game
import com.example.gameplanet.models.ListaDeseo
import com.example.gameplanet.services.GameService
import com.example.gameplanet.utils.Screens
import com.example.gameplanet.utils.SharedPreference
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun ListaDeDeseoScreen(paddingValues: PaddingValues){
    val sharedPref = SharedPreference(LocalContext.current)
    val userId = sharedPref.getUserIdSharedPref()
    var listaDeseo by remember { mutableStateOf(listOf<ListaDeseo>()) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(15.dp)
    ){
        Text(
            text = "LISTA DE DESEOS",
            fontSize = 25.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 10.dp),
            fontWeight = FontWeight.Bold
        )
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
                val response = gameService.getListaDeseo(userId)
                isLoading = false
                listaDeseo = response
                Log.i("RESPONSE", response.toString())
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
            LazyColumn {
                items(listaDeseo){
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .height(170.dp),
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
                                model = it.videojuego.imagen,
                                contentDescription = it.videojuego.nombre,
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
                                text = it.videojuego.nombre,
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