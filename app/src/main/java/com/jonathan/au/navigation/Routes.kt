package com.jonathan.au.navigation

sealed class StockRoutes(val route: String) {
    data object Home : StockRoutes("home")
    data object Display : StockRoutes("display")
}