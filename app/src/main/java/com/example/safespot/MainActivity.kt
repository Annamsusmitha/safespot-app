package com.example.safespot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.*
import com.example.safespot.ui.theme.SafeSpotTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.safespot.ui.PostLoginScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SafeSpotTheme {
                SafeSpotApp()
            }
        }
    }
}

@Composable
fun SafeSpotApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            val context = LocalContext.current
            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(context)
            )
            LoginScreen(
                onLoginSuccess = { navController.navigate("postLogin") },
                onRegisterClicked = { navController.navigate("register") { popUpTo("login") { inclusive = true } } },
                viewModel = loginViewModel
            )
        }
        composable("postLogin") {
            PostLoginScreen()
        }
        composable("register") {
            val context = LocalContext.current
            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(context)
            )
            RegisterScreen(
                onRegisterSuccess = { navController.navigate("login") { popUpTo("login") { inclusive = true } } },
                viewModel = loginViewModel
            )
        }
    }
}

