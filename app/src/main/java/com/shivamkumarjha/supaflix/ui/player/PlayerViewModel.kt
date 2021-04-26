package com.shivamkumarjha.supaflix.ui.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shivamkumarjha.supaflix.model.db.History
import com.shivamkumarjha.supaflix.model.fcdn.Fcdn
import com.shivamkumarjha.supaflix.model.gocdn.Gocdn
import com.shivamkumarjha.supaflix.model.movcloud.MovCloud
import com.shivamkumarjha.supaflix.model.vidcloud.LinkFrame
import com.shivamkumarjha.supaflix.model.vidcloud.VidCloud
import com.shivamkumarjha.supaflix.model.xmovies.Embeds
import com.shivamkumarjha.supaflix.network.Resource
import com.shivamkumarjha.supaflix.network.Status
import com.shivamkumarjha.supaflix.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val fcdnCloudRepository: FcdnCloudRepository,
    private val gocdnCloudRepository: GocdnCloudRepository,
    private val movCloudRepository: MovCloudRepository,
    private val vidCloudRepository: VidCloudRepository,
    private val xmoviesRepository: XmoviesRepository
) : ViewModel() {

    private val _browserLink = MutableLiveData<String?>()
    val browserLink: LiveData<String?> = _browserLink

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private val _fcdn = MutableLiveData<Resource<Fcdn?>>()
    val fcdn: LiveData<Resource<Fcdn?>> = _fcdn

    private val _gocdn = MutableLiveData<Resource<Gocdn?>>()
    val gocdn: LiveData<Resource<Gocdn?>> = _gocdn

    private val _movCloud = MutableLiveData<Resource<MovCloud?>>()
    val movCloud: LiveData<Resource<MovCloud?>> = _movCloud

    private val _vidCloud = MutableLiveData<Resource<VidCloud?>>()
    val vidCloud: LiveData<Resource<VidCloud?>> = _vidCloud

    private val _linkFrame = MutableLiveData<Resource<LinkFrame?>>()
    val linkFrame: LiveData<Resource<LinkFrame?>> = _linkFrame

    init {
        _browserLink.postValue(null)
        _error.postValue(false)
    }

    fun embeds(history: History) {
        viewModelScope.launch(Dispatchers.IO) {
            xmoviesRepository.embeds(history.hash, history.episodeHash).collect {
                if (it.status == Status.SUCCESS) {
                    if (!it.data?.embeds.isNullOrEmpty()) {
                        callBestServer(history, it.data!!.embeds)
                    }
                } else if (it.status == Status.ERROR) {
                    _error.postValue(true)
                }
            }
        }
    }

    private fun server(history: History, serverHash: String) {
        viewModelScope.launch(Dispatchers.IO) {
            xmoviesRepository.server(history.hash, history.episodeHash, serverHash).collect {
                if (it.status == Status.SUCCESS) {
                    if (it.data != null) {
                        when {
                            it.data.url.contains("https://vidcloud9.com/") -> getVidCloudLink(it.data.url)
                            it.data.url.contains("https://vidnext.net/") -> getVidCloudLink(it.data.url)
                            it.data.url.contains("https://fcdn.stream") -> getFcdnCloudLink(it.data.url)
                            it.data.url.contains("https://movcloud.net/") -> getMovCloudLink(it.data.url)
                            it.data.url.contains("https://play.gocdn.icu/") -> getGocdnCloudLink(it.data.url)
                            else -> _browserLink.postValue(it.data.url)
                        }
                    }
                } else if (it.status == Status.ERROR) {
                    _error.postValue(true)
                }
            }
        }
    }

    private fun callBestServer(history: History, embeds: List<Embeds>) {
        for (embed in embeds) {
            when {
                embed.part_of.toLowerCase(Locale.ROOT).contains("vidcloud") -> {
                    server(history, embed.hash)
                    return
                }
                embed.part_of.toLowerCase(Locale.ROOT).contains("fcdn") -> {
                    server(history, embed.hash)
                    return
                }
                embed.part_of.toLowerCase(Locale.ROOT).contains("movcloud") -> {
                    server(history, embed.hash)
                    return
                }
                embed.part_of.toLowerCase(Locale.ROOT).contains("mega") -> {
                    server(history, embed.hash)
                    return
                }
            }
        }
        server(history, embeds.first().hash)
    }

    private fun getFcdnCloudLink(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val hash = getFcdnHash(url)
            fcdnCloudRepository.getLink(hash).collect {
                if (it.status == Status.ERROR) {
                    _error.postValue(true)
                } else {
                    _fcdn.postValue(it)
                }
            }
        }
    }

    private fun getGocdnCloudLink(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val hash = getGocdnHash(url)
            gocdnCloudRepository.getLink(hash).collect {
                if (it.status == Status.ERROR) {
                    _error.postValue(true)
                } else {
                    _gocdn.postValue(it)
                }
            }
        }
    }

    fun getMovCloudLink(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val hash = getMovCloudHash(url)
            movCloudRepository.getLink(hash).collect {
                if (it.status == Status.ERROR) {
                    _error.postValue(true)
                } else {
                    _movCloud.postValue(it)
                }
            }
        }
    }

    private fun getVidCloudLink(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val id = getVidCloudId(url)
            val sub = getVidCloudSub(url)

            vidCloudRepository.getLink(id, sub).collect { cloud ->
                if (cloud.status == Status.ERROR) {
                    vidCloudRepository.getLinkWithoutTrack(id, sub).collect { linkFrameData ->
                        if (linkFrameData.status == Status.ERROR) {
                            _error.postValue(true)
                        } else {
                            _linkFrame.postValue(linkFrameData)
                        }
                    }
                } else {
                    if (cloud.data?.linkiframe?.contains("https://movcloud.net/embed/") == true && cloud.data.source.isNullOrEmpty()) {
                        val hash = getMovCloudHash(url)
                        getMovCloudLink(hash)
                    } else {
                        if (cloud.status == Status.ERROR) {
                            _error.postValue(true)
                        } else {
                            _vidCloud.postValue(cloud)
                        }
                    }
                }
            }
        }
    }

    private fun getVidCloudId(url: String) = url.substringAfter("id=").substringBefore("&title")

    private fun getVidCloudSub(url: String) = url.substringAfter("&sub=").substringBefore("&cover")

    private fun getFcdnHash(url: String) = url.substringAfter("https://fcdn.stream/v/")

    private fun getMovCloudHash(url: String) =
        url.substringAfter("https://movcloud.net/embed/").substringBefore("?sub_en")

    private fun getGocdnHash(url: String) = url.substringAfter("https://play.gocdn.icu/video/")

    fun addToHistory(history: History) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.addToHistory(history)
        }
    }
}