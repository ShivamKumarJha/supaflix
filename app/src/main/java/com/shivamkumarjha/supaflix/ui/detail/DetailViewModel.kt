package com.shivamkumarjha.supaflix.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shivamkumarjha.supaflix.model.xmovies.Content
import com.shivamkumarjha.supaflix.model.xmovies.EmbedsResponse
import com.shivamkumarjha.supaflix.network.Resource
import com.shivamkumarjha.supaflix.repository.XmoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val xmoviesRepository: XmoviesRepository
) : ViewModel() {
    val content = MutableLiveData<Resource<Content?>>()
    val embeds = MutableLiveData<Resource<EmbedsResponse?>>()
    val isFavourite = MutableLiveData<Boolean>()

    init {
        isFavourite.value = false
    }

    fun checkFavourite(hash: String) {
        //TODO
    }

    fun watch(hash: String) {
        viewModelScope.launch(Dispatchers.IO) {
            xmoviesRepository.content(hash).collect {
                content.postValue(it)
            }
        }
    }

    fun embeds(contentHash: String, episodeHash: String) {
        viewModelScope.launch(Dispatchers.IO) {
            xmoviesRepository.embeds(contentHash, episodeHash).collect {
                embeds.postValue(it)
            }
        }
    }
}