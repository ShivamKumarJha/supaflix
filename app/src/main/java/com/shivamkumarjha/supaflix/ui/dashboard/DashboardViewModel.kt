package com.shivamkumarjha.supaflix.ui.dashboard

import androidx.lifecycle.*
import com.shivamkumarjha.supaflix.model.db.Download
import com.shivamkumarjha.supaflix.model.db.History
import com.shivamkumarjha.supaflix.model.xmovies.ContentsResponse
import com.shivamkumarjha.supaflix.model.xmovies.Home
import com.shivamkumarjha.supaflix.network.Resource
import com.shivamkumarjha.supaflix.persistence.PreferenceManager
import com.shivamkumarjha.supaflix.repository.DatabaseRepository
import com.shivamkumarjha.supaflix.repository.XmoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val databaseRepository: DatabaseRepository,
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

    private val _topIMBDMovies = MutableLiveData<Resource<ContentsResponse?>>()
    val topIMBDMovies: LiveData<Resource<ContentsResponse?>> = _topIMBDMovies

    private val _recentSeries = MutableLiveData<Resource<ContentsResponse?>>()
    val recentSeries: LiveData<Resource<ContentsResponse?>> = _recentSeries

    private val _mostViewedSeries = MutableLiveData<Resource<ContentsResponse?>>()
    val mostViewedSeries: LiveData<Resource<ContentsResponse?>> = _mostViewedSeries

    private val _topIMBDSeries = MutableLiveData<Resource<ContentsResponse?>>()
    val topIMBDSeries: LiveData<Resource<ContentsResponse?>> = _topIMBDSeries

    val favourites = liveData(Dispatchers.IO) {
        emitSource(databaseRepository.getFavourites())
    }
    val downloads = liveData(Dispatchers.IO) {
        emitSource(databaseRepository.getDownload())
    }
    val watchHistory = liveData(Dispatchers.IO) {
        emitSource(databaseRepository.getHistory())
    }

    init {
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
            xmoviesRepository.topIMBDSeries().collect {
                _topIMBDSeries.postValue(it)
            }
        }
    }

    fun clearFavourites() {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.clearFavourites()
        }
    }

    fun clearDownloads() {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.clearDownload()
        }
    }

    fun removeFromDownload(download: Download) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.removeFromDownload(download)
        }
    }

    fun clearHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.clearHistory()
        }
    }

    fun removeFromHistory(history: History) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.removeFromHistory(history)
        }
    }

    fun getServerSwitch() = preferenceManager.autoServerPick

    fun setServerSwitch(value: Boolean) {
        preferenceManager.autoServerPick = value
    }

    fun getSubtitleSwitch() = preferenceManager.showSubtitles

    fun setSubtitleSwitch(value: Boolean) {
        preferenceManager.showSubtitles = value
    }

    fun getLandscapePlayer() = preferenceManager.landscapePlayer

    fun setLandscapePlayer(value: Boolean) {
        preferenceManager.landscapePlayer = value
    }
}