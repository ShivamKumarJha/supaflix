package com.shivamkumarjha.supaflix.ui.detail

import androidx.lifecycle.*
import com.shivamkumarjha.supaflix.model.db.Favourite
import com.shivamkumarjha.supaflix.model.db.History
import com.shivamkumarjha.supaflix.model.xmovies.Content
import com.shivamkumarjha.supaflix.model.xmovies.Episode
import com.shivamkumarjha.supaflix.persistence.PreferenceManager
import com.shivamkumarjha.supaflix.persistence.XmoviesDao
import com.shivamkumarjha.supaflix.repository.XmoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val xmoviesDao: XmoviesDao,
    private val xmoviesRepository: XmoviesRepository
) : ViewModel() {

    private val _isFavourite = MutableLiveData<Boolean>()
    val isFavourite: LiveData<Boolean> = _isFavourite

    init {
        _isFavourite.value = false
    }

    fun content(hash: String) {
        viewModelScope.launch(Dispatchers.IO) {
            xmoviesRepository.content(hash).collect { }
        }
    }

    fun watch(hash: String) = liveData(Dispatchers.IO) {
        emitSource(xmoviesDao.getContent(hash))
    }

    fun getHistory(episode: Episode, content: Content): History {
        return History(
            preferenceManager.getHistoryId(),
            content.hash,
            content.name,
            content.poster_path,
            content.released,
            content.imdb_rating,
            content.description,
            episode.name,
            episode.episode_hash
        )
    }

    fun checkFavourite(hash: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isFavourite.postValue(xmoviesDao.isFavourite(hash))
        }
    }

    fun toggleFavourites(content: Content, isFavourite: Boolean) {
        this._isFavourite.value = isFavourite
        val favourite = Favourite(
            content.hash,
            content.name,
            content.poster_path,
            content.released,
            content.imdb_rating,
            content.description
        )
        viewModelScope.launch((Dispatchers.IO)) {
            if (isFavourite) {
                xmoviesDao.addToFavourites(favourite)
            } else {
                xmoviesDao.removeFromFavourites(favourite)
            }
        }
    }
}