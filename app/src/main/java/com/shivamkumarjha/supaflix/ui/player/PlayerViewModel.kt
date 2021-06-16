package com.shivamkumarjha.supaflix.ui.player

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.model.db.History
import com.shivamkumarjha.supaflix.model.fcdn.Fcdn
import com.shivamkumarjha.supaflix.model.gocdn.Gocdn
import com.shivamkumarjha.supaflix.model.movcloud.MovCloud
import com.shivamkumarjha.supaflix.model.vidcloud.LinkFrame
import com.shivamkumarjha.supaflix.model.vidcloud.VidCloud
import com.shivamkumarjha.supaflix.model.xmovies.Embeds
import com.shivamkumarjha.supaflix.network.Resource
import com.shivamkumarjha.supaflix.network.Status
import com.shivamkumarjha.supaflix.persistence.PreferenceManager
import com.shivamkumarjha.supaflix.repository.*
import com.shivamkumarjha.supaflix.utility.UrlResolver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val databaseRepository: DatabaseRepository,
    private val fcdnCloudRepository: FcdnCloudRepository,
    private val gocdnCloudRepository: GocdnCloudRepository,
    private val movCloudRepository: MovCloudRepository,
    private val urlResolver: UrlResolver,
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

    private val _serverList = MutableLiveData<List<Embeds>>()
    val serverList: LiveData<List<Embeds>> = _serverList

    private val _resolverLink = MutableLiveData<String?>()
    val resolverLink: LiveData<String?> = _resolverLink

    init {
        _browserLink.postValue(null)
        _error.postValue(false)
    }

    fun embeds(history: History) {
        viewModelScope.launch(Dispatchers.IO) {
            xmoviesRepository.embeds(history.hash, history.episodeHash).collect {
                if (it.status == Status.SUCCESS) {
                    if (!it.data?.embeds.isNullOrEmpty()) {
                        if (it.data?.embeds?.size == 1) {
                            server(history, it.data.embeds.first().hash)
                        } else {
                            if (preferenceManager.autoServerPick) {
                                callBestServer(history, it.data!!.embeds)
                            } else {
                                _serverList.postValue(it.data?.embeds)
                            }
                        }
                    }
                } else if (it.status == Status.ERROR) {
                    _error.postValue(true)
                }
            }
        }
    }

    fun server(history: History, serverHash: String) {
        viewModelScope.launch(Dispatchers.IO) {
            xmoviesRepository.server(history.hash, history.episodeHash, serverHash).collect {
                if (it.status == Status.SUCCESS) {
                    if (it.data != null) {
                        callHost(it.data.url)
                    }
                } else if (it.status == Status.ERROR) {
                    _error.postValue(true)
                }
            }
        }
    }

    private fun callHost(url: String) {
        when {
            url.contains("https://vidcloud9.com/") -> getVidCloudLink(url)
            url.contains("https://vidnext.net/") -> getVidCloudLink(url)
            url.contains("https://fcdn.stream") -> getFcdnCloudLink(url)
            url.contains("https://movcloud.net/") -> getMovCloudLink(url)
            url.contains("https://play.gocdn.icu/") -> getGocdnCloudLink(url)
            else -> {
                if (urlResolver.isSupportedHost(url)) {
                    viewModelScope.launch {
                        val link = urlResolver.getFinalURL(viewModelScope, url)
                        Log.d(Constants.TAG, "Url Resolver link $link")
                        _resolverLink.postValue(link)
                        _error.postValue(link == null)
                    }
                } else {
                    _browserLink.postValue(url)
                }
            }
        }
    }

    private fun callBestServer(history: History, embeds: List<Embeds>) {
        for (embed in embeds) {
            when {
                embed.part_of.equals("vidcloud", ignoreCase = true) -> {
                    server(history, embed.hash)
                    return
                }
                embed.part_of.equals("fcdn", ignoreCase = true) -> {
                    server(history, embed.hash)
                    return
                }
                embed.part_of.equals("movcloud", ignoreCase = true) -> {
                    server(history, embed.hash)
                    return
                }
                embed.part_of.equals("mega", ignoreCase = true) -> {
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

    fun updateHistory(history: History) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.removeFromHistory(history)
            databaseRepository.addToHistory(history)
        }
    }

    fun isSupportedHost(url: String): Boolean = urlResolver.isSupportedHost(url)
}