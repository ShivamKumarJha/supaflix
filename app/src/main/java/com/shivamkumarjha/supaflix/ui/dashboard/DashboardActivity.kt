package com.shivamkumarjha.supaflix.ui.dashboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.shivamkumarjha.supaflix.ui.theme.SupaflixTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SupaflixTheme {
                BottomNavigation()
            }
        }
    }
}