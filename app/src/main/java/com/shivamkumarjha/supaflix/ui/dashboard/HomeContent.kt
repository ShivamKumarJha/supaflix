package com.shivamkumarjha.supaflix.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.shivamkumarjha.supaflix.model.xmovies.Home
import com.shivamkumarjha.supaflix.network.Resource
import com.shivamkumarjha.supaflix.network.Status
import com.shivamkumarjha.supaflix.ui.theme.GraySurface

@Composable
fun HomeContent(navController: NavController, viewModel: DashboardViewModel) {
    viewModel.initialize()
    val showLoading = remember { mutableStateOf(true) }
    val background =
        if (isSystemInDarkTheme()) GraySurface else MaterialTheme.colors.background
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        val home: Resource<Home?> by viewModel.home.observeAsState(Resource.loading(null))
        showLoading.value = home.status == Status.LOADING

        if (showLoading.value) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}