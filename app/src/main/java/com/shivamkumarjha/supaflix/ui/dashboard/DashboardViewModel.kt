package com.shivamkumarjha.supaflix.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shivamkumarjha.supaflix.model.xmovies.Home
import com.shivamkumarjha.supaflix.network.Resource
import com.shivamkumarjha.supaflix.repository.XmoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val xmoviesRepository: XmoviesRepository
) : ViewModel() {
    private val _home = MutableLiveData<Resource<Home?>>()
    val home: LiveData<Resource<Home?>> = _home

    fun initialize() {
        viewModelScope.launch(Dispatchers.IO) {
            xmoviesRepository.home().collect {
                _home.postValue(it)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            xmoviesRepository.trending().collect { }
        }
        viewModelScope.launch(Dispatchers.IO) {
            xmoviesRepository.featured().collect { }
        }
        viewModelScope.launch(Dispatchers.IO) {
            xmoviesRepository.recentMovies().collect { }
        }
        viewModelScope.launch(Dispatchers.IO) {
            xmoviesRepository.mostViewedMovies().collect { }
        }
        viewModelScope.launch(Dispatchers.IO) {
            xmoviesRepository.topRatedMovies().collect { }
        }
        viewModelScope.launch(Dispatchers.IO) {
            xmoviesRepository.topIMBDMovies().collect { }
        }
        viewModelScope.launch(Dispatchers.IO) {
            xmoviesRepository.recentSeries().collect { }
        }
        viewModelScope.launch(Dispatchers.IO) {
            xmoviesRepository.mostViewedSeries().collect { }
        }
        viewModelScope.launch(Dispatchers.IO) {
            xmoviesRepository.topRatedSeries().collect { }
        }
        viewModelScope.launch(Dispatchers.IO) {
            xmoviesRepository.topIMBDSeries().collect { }
        }
    }
}