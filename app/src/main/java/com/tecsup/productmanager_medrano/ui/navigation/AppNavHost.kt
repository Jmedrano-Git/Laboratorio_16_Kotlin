package com.tecsup.productmanager_medrano.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tecsup.productmanager_medrano.data.model.Product
import com.tecsup.productmanager_medrano.ui.auth.LoginScreen
import com.tecsup.productmanager_medrano.ui.auth.LoginViewModel
import com.tecsup.productmanager_medrano.ui.auth.RegisterScreen
import com.tecsup.productmanager_medrano.ui.auth.RegisterViewModel
import com.tecsup.productmanager_medrano.ui.navigation.AppRoutes.AppRoutes
import com.tecsup.productmanager_medrano.ui.products.ProductFormScreen
import com.tecsup.productmanager_medrano.ui.products.ProductFormViewModel
import com.tecsup.productmanager_medrano.ui.products.ProductListScreen
import com.tecsup.productmanager_medrano.ui.products.ProductListViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    registerViewModel: RegisterViewModel,
    listViewModel: ProductListViewModel,
    formViewModel: ProductFormViewModel
) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.LOGIN
    ) {
        composable(AppRoutes.LOGIN) {
            LoginScreen(
                viewModel = loginViewModel,
                irARegistro = { navController.navigate(AppRoutes.REGISTER) },
                irAProductos = {
                    navController.navigate(AppRoutes.PRODUCTS) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(AppRoutes.REGISTER) {
            RegisterScreen(
                viewModel = registerViewModel,
                volverALogin = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.PRODUCTS) {
            ProductListScreen(
                viewModel = listViewModel,
                irAFormulario = { product ->
                    formViewModel.cargarProducto(product)
                    navController.navigate(AppRoutes.PRODUCT_FORM)
                },
                cerrarSesion = {
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(AppRoutes.PRODUCT_FORM) {
            ProductFormScreen(
                viewModel = formViewModel,
                volverALista = { navController.popBackStack() }
            )
        }
    }
}