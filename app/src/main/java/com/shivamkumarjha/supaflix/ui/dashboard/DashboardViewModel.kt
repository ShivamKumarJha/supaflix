package com.shivamkumarjha.supaflix.ui.dashboard

import androidx.lifecycle.ViewModel
import com.shivamkumarjha.supaflix.repository.XmoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val xmoviesRepository: XmoviesRepository
) : ViewModel() {
}