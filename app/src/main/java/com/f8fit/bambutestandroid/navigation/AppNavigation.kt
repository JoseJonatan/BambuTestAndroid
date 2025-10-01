package com.f8fit.bambutestandroid.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.f8fit.bambutestandroid.presentation.cartModule.view.CartScreen
import com.f8fit.bambutestandroid.presentation.productsModule.view.HomeScreen
import com.f8fit.bambutestandroid.presentation.productsModule.view.ProductDetailScreen
import com.f8fit.bambutestandroid.presentation.profileModule.view.ProfileScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route
    ) {
        composable(NavRoutes.Home.route) {
            HomeScreen(navController)
        }
        composable(NavRoutes.ProductDetail.route) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            if (productId != null) {
                ProductDetailScreen(productId, navController)
            }
        }
        composable(NavRoutes.Cart.route) {
            CartScreen(navController)
        }

        composable(NavRoutes.Profile.route) {
            ProfileScreen(navController)
        }

    }
}

