package com.shivamkumarjha.supaflix.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shivamkumarjha.supaflix.model.db.Favourite
import com.shivamkumarjha.supaflix.model.db.History
import com.shivamkumarjha.supaflix.model.xmovies.Content
import com.shivamkumarjha.supaflix.model.xmovies.Episode
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
class DetailViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val databaseRepository: DatabaseRepository,
    private val xmoviesRepository: XmoviesRepository
) : ViewModel() {

    val content = MutableLiveData<Resource<Content?>>()
    val isFavourite = MutableLiveData<Boolean>()

    init {
        isFavourite.value = false
    }

    fun watch(hash: String) {
        viewModelScope.launch(Dispatchers.IO) {
            xmoviesRepository.content(hash).collect {
                content.postValue(it)
            }
        }
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
            episode.episode_hash,
            null,
            null
        )
    }

    fun checkFavourite(hash: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isFavourite.postValue(databaseRepository.isFavourite(hash))
        }
    }

    fun toggleFavourites(content: Content, isFavourite: Boolean) {
        this.isFavourite.value = isFavourite
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
                databaseRepository.addToFavourites(favourite)
            } else {
                databaseRepository.removeFromFavourites(favourite)
            }
        }
    }
}