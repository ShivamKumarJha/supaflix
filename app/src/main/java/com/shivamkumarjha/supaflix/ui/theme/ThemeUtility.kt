package com.shivamkumarjha.supaflix.ui.theme

import androidx.compose.ui.graphics.Color

object ThemeUtility {

    fun textColor(isDark: Boolean) =
        if (isDark) Color.LightGray else Color.Black

    fun surfaceBackground(isDark: Boolean) =
        if (isDark) GraySurface else Color.White
}