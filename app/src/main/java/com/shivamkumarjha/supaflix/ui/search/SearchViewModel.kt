package com.shivamkumarjha.supaflix.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.shivamkumarjha.supaflix.model.xmovies.Property
import com.shivamkumarjha.supaflix.repository.XmoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val xmoviesRepository: XmoviesRepository
) : ViewModel() {

    private fun getActor(property: Property) = Pager(PagingConfig(pageSize = 10)) {
        xmoviesRepository.actor(property.hash, property.slug)
    }.flow.cachedIn(viewModelScope)

    private fun getCountry(property: Property) = Pager(PagingConfig(pageSize = 10)) {
        xmoviesRepository.country(property.hash, property.slug)
    }.flow.cachedIn(viewModelScope)

    private fun getDirector(property: Property) = Pager(PagingConfig(pageSize = 10)) {
        xmoviesRepository.director(property.hash, property.slug)
    }.flow.cachedIn(viewModelScope)

    private fun getGenre(property: Property) = Pager(PagingConfig(pageSize = 10)) {
        xmoviesRepository.genre(property.hash, property.slug)
    }.flow.cachedIn(viewModelScope)

    private fun getMovie(keyword: String) = Pager(PagingConfig(pageSize = 10)) {
        xmoviesRepository.search(keyword)
    }.flow.cachedIn(viewModelScope)

    private fun getRelease(keyword: String) = Pager(PagingConfig(pageSize = 10)) {
        xmoviesRepository.release(keyword)
    }.flow.cachedIn(viewModelScope)

    fun search(searchType: SearchType, property: Property?, keyword: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            when (searchType) {
                SearchType.SEARCH_ACTOR -> getActor(property!!)
                SearchType.SEARCH_COUNTRY -> getCountry(property!!)
                SearchType.SEARCH_DIRECTOR -> getDirector(property!!)
                SearchType.SEARCH_GENRE -> getGenre(property!!)
                SearchType.SEARCH_MOVIE -> getMovie(keyword!!)
                SearchType.SEARCH_YEAR -> getRelease(keyword!!)
            }
        }
    }
}