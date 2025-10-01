package com.f8fit.bambutestandroid.navigation


sealed class NavRoutes(val route: String) {
    object Home : NavRoutes("home")
    object ProductDetail : NavRoutes("product_detail/{id}") {
        fun createRoute(id: Int) = "product_detail/$id"
    }

    object Cart : NavRoutes("cart")

    object Profile : NavRoutes("profile")

}

