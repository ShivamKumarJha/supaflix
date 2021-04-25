package com.shivamkumarjha.supaflix.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.shivamkumarjha.supaflix.model.xmovies.Contents
import com.shivamkumarjha.supaflix.model.xmovies.Property
import com.shivamkumarjha.supaflix.repository.XmoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val xmoviesRepository: XmoviesRepository
) : ViewModel() {

    fun getActor(property: Property): Flow<PagingData<Contents>> =
        Pager(PagingConfig(pageSize = 10)) {
            xmoviesRepository.actor(property.hash, property.slug)
        }.flow.cachedIn(viewModelScope)

    fun getCountry(property: Property): Flow<PagingData<Contents>> =
        Pager(PagingConfig(pageSize = 10)) {
            xmoviesRepository.country(property.hash, property.slug)
        }.flow.cachedIn(viewModelScope)

    fun getDirector(property: Property): Flow<PagingData<Contents>> =
        Pager(PagingConfig(pageSize = 10)) {
            xmoviesRepository.director(property.hash, property.slug)
        }.flow.cachedIn(viewModelScope)

    fun getGenre(property: Property): Flow<PagingData<Contents>> =
        Pager(PagingConfig(pageSize = 10)) {
            xmoviesRepository.genre(property.hash, property.slug)
        }.flow.cachedIn(viewModelScope)

    fun getMovie(keyword: String): Flow<PagingData<Contents>> = Pager(PagingConfig(pageSize = 10)) {
        xmoviesRepository.search(keyword)
    }.flow.cachedIn(viewModelScope)

    fun getRelease(keyword: String): Flow<PagingData<Contents>> =
        Pager(PagingConfig(pageSize = 10)) {
            xmoviesRepository.release(keyword)
        }.flow.cachedIn(viewModelScope)
}