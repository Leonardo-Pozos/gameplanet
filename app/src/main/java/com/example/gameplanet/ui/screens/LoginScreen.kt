package com.example.gameplanet.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gameplanet.R
import com.example.gameplanet.models.Auth
import com.example.gameplanet.services.AuthService
import com.example.gameplanet.utils.Screens
import com.example.gameplanet.utils.SharedPreference
import com.example.gameplanet.utils.Visibility
import com.example.gameplanet.utils.Visibility_off
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun LoginScreen(innerPadding: PaddingValues, navController: NavController){
    var email by remember{ mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val emailFocusRequest = remember { FocusRequester() }
    val passwordFocusRequest = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var errorMessage by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    var isPasswordVisible by remember {
        mutableStateOf(false)
    }

    val sharedPreference = SharedPreference(LocalContext.current)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(innerPadding)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        AnimatedVisibility(visible = showError) {
            Box(
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .width(400.dp)
                    .height(90.dp)
                    .background(MaterialTheme.colorScheme.error),
                contentAlignment = Alignment.Center
            ) {
                Row (
                    horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
                ){
                    Image(imageVector = Icons.Default.Warning, contentDescription = "error", modifier = Modifier.padding(10.dp), colorFilter = ColorFilter.tint(Color.White))
                    Text(
                        modifier = Modifier.padding(end = 10.dp),
                        text = errorMessage,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }
            }
            // Efecto de desvanecimiento después de 3 segundos
            LaunchedEffect(showError) {
                delay(3000) // Esperar 3 segundos
                showError = false // Ocultar el mensaje
            }
        }
        Image(
            painter = painterResource(id = R.drawable.logo_gameplanet),
            contentDescription = null,
            modifier = Modifier.size(400.dp)
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text(text = "Nombre de usuario") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(emailFocusRequest),
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Gray
            ),
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = "username")
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    passwordFocusRequest.requestFocus()
                }
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text(text = "Contraseña") },
            visualTransformation = if(!isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(passwordFocusRequest),
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Gray
            ),
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = "password")
            },
            trailingIcon = {
                IconButton(onClick = {
                    isPasswordVisible = !isPasswordVisible
                }) {
                    val icon = if(isPasswordVisible) Visibility else Visibility_off
                    Icon(imageVector = icon, contentDescription = "visibility")
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if(email.isEmpty() || password.isEmpty()){
                    showError = true
                    errorMessage = "Ingrese el nombre de usuario y contraseña que registro para poder ingresar."
                }else{
                    scope.launch(Dispatchers.IO){
                        val authService = Retrofit.Builder()
                            .baseUrl("http://157.230.89.111:8000/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                            .create(AuthService::class.java)
                        val response = authService.login(
                            grantType = "password",
                            username = email,
                            password = password
                        )
                        Log.i("LOGIN", response.body()?.access_token.toString())
                        if(response.body()?.access_token != null){
                            withContext(Dispatchers.Main){
                                sharedPreference.saveUserSharedPref(
                                    userName = email,
                                    isLogged = true,
                                    userId = response.body()?.user_id ?: 0
                                )
                                navController.navigate(Screens.Home.route){
                                    popUpTo(Screens.Home.route) { inclusive = true }
                                }
                            }
                        }else{
                            showError = true
                            errorMessage = "Nombre de usuario o contraseña incorrectas."
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar sesion")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "¿No tienes cuenta? Crea una aquí",
            color = Color.Gray,
            modifier = Modifier.clickable {
                navController.navigate(Screens.Registro.route)
            }
        )
    }
}