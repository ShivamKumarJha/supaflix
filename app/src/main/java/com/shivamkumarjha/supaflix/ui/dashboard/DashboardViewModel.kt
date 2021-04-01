package com.shivamkumarjha.supaflix.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shivamkumarjha.supaflix.model.xmovies.ContentsResponse
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

    private val _trending = MutableLiveData<Resource<ContentsResponse?>>()
    val trending: LiveData<Resource<ContentsResponse?>> = _trending

    private val _featured = MutableLiveData<Resource<ContentsResponse?>>()
    val featured: LiveData<Resource<ContentsResponse?>> = _featured

    private val _recentMovies = MutableLiveData<Resource<ContentsResponse?>>()
    val recentMovies: LiveData<Resource<ContentsResponse?>> = _recentMovies

    private val _mostViewedMovies = MutableLiveData<Resource<ContentsResponse?>>()
    val mostViewedMovies: LiveData<Resource<ContentsResponse?>> = _mostViewedMovies

    private val _topRatedMovies = MutableLiveData<Resource<ContentsResponse?>>()
    val topRatedMovies: LiveData<Resource<ContentsResponse?>> = _topRatedMovies

    private val _topIMBDMovies = MutableLiveData<Resource<ContentsResponse?>>()
    val topIMBDMovies: LiveData<Resource<ContentsResponse?>> = _topIMBDMovies

    private val _recentSeries = MutableLiveData<Resource<ContentsResponse?>>()
    val recentSeries: LiveData<Resource<ContentsResponse?>> = _recentSeries

    private val _mostViewedSeries = MutableLiveData<Resource<ContentsResponse?>>()
    val mostViewedSeries: LiveData<Resource<ContentsResponse?>> = _mostViewedSeries

    private val _topRatedSeries = MutableLiveData<Resource<ContentsResponse?>>()
    val topRatedSeries: LiveData<Resource<ContentsResponse?>> = _topRatedSeries

    private val _topIMBDSeries = MutableLiveData<Resource<ContentsResponse?>>()
    val topIMBDSeries: LiveData<Resource<ContentsResponse?>> = _topIMBDSeries

    fun initialize() {
        viewModelScope.launch(Dispatchers.IO) {
            xmoviesRepository.home().collect {
                _home.postValue(it)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            xmoviesRepository.trending().collect {
                _trending.postValue(it)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            xmoviesRepository.featured().collect {
                _featured.postValue(it)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            xmoviesRepository.recentMovies().collect {
                _recentMovies.postValue(it)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            xmoviesRepository.mostViewedMovies().collect {
                _mostViewedMovies.postValue(it)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            xmoviesRepository.topRatedMovies().collect {
                _topRatedMovies.postValue(it)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            xmoviesRepository.topIMBDMovies().collect {
                _topIMBDMovies.postValue(it)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            xmoviesRepository.recentSeries().collect {
                _recentSeries.postValue(it)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            xmoviesRepository.mostViewedSeries().collect {
                _mostViewedSeries.postValue(it)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            xmoviesRepository.topRatedSeries().collect {
                _topRatedSeries.postValue(it)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            xmoviesRepository.topIMBDSeries().collect {
                _topIMBDSeries.postValue(it)
            }
        }
    }
}