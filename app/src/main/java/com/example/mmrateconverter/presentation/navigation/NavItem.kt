package com.example.mmrateconverter.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector


sealed class NavItem(val route: String, val label: String, val icon: ImageVector) {
    data object Rates : NavItem("rates", "Rates", Icons.Filled.AttachMoney)
    data object Gold : NavItem("gold", "Gold", Icons.Filled.Diamond) // Gold Icon
    data object Calculator : NavItem("calculator", "Calc", Icons.Filled.Calculate)
    data object SettingsRoute : NavItem("settings", "Settings", Icons.Filled.Settings)
}