package com.tecsup.productmanager_medrano

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.tecsup.productmanager_medrano.core.AppContainer
import com.tecsup.productmanager_medrano.ui.auth.LoginViewModel
import com.tecsup.productmanager_medrano.ui.auth.RegisterViewModel
import com.tecsup.productmanager_medrano.ui.navigation.AppNavHost
import com.tecsup.productmanager_medrano.ui.products.ProductFormViewModel
import com.tecsup.productmanager_medrano.ui.products.ProductListViewModel
import com.tecsup.productmanager_medrano.ui.theme.ProductManager_MedranoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val container = AppContainer
        enableEdgeToEdge()
        setContent {
            ProductManager_MedranoTheme {
                val navController = rememberNavController()

                // ViewModels simples creados a mano
                val loginViewModel = remember {
                    LoginViewModel(container.authRepository)
                }
                val registerViewModel = remember {
                    RegisterViewModel(container.authRepository)
                }
                val listViewModel = remember {
                    ProductListViewModel(
                        productRepository = container.productRepository,
                        authRepository = container.authRepository
                    )
                }
                val formViewModel = remember {
                    ProductFormViewModel(
                        productRepository = container.productRepository,
                        authRepository = container.authRepository
                    )
                }

                AppNavHost(
                    navController = navController,
                    loginViewModel = loginViewModel,
                    registerViewModel = registerViewModel,
                    listViewModel = listViewModel,
                    formViewModel = formViewModel
                )
            }
            }
        }
    }


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
}