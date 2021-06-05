package com.shivamkumarjha.supaflix.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.shivamkumarjha.supaflix.R
import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.model.xmovies.Property
import com.shivamkumarjha.supaflix.ui.search.SearchType
import com.shivamkumarjha.supaflix.ui.theme.SupaflixTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalMaterialApi
class DetailFragment : Fragment() {

    //Bundle args
    private val hash by lazy {
        arguments?.getString(Constants.HASH)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            if (hash != null) {
                setContent {
                    SupaflixTheme {
                        DetailScreen(hash!!, interactionEvents = {
                            handleInteractionEvents(it)
                        })
                    }
                }
            } else {
                findNavController().navigateUp()
            }
        }
    }

    private fun handleInteractionEvents(
        interactionEvents: DetailInteractionEvents
    ) {
        when (interactionEvents) {
            is DetailInteractionEvents.NavigateUp -> findNavController().navigateUp()
            is DetailInteractionEvents.OpenMovieDetail -> {
                val bundle = Bundle().apply {
                    putString(Constants.HASH, interactionEvents.similarContents.hash)
                }
                findNavController().navigate(R.id.action_detailFragment_self, bundle)
            }
            is DetailInteractionEvents.OpenEpisode -> {
                val bundle = Bundle().apply {
                    putParcelable(Constants.HISTORY, interactionEvents.history)
                }
                findNavController().navigate(
                    R.id.action_detailFragment_to_playerFragment,
                    bundle
                )
            }
            is DetailInteractionEvents.SearchGenre -> {
                openSearch(SearchType.SEARCH_GENRE, interactionEvents.property, null)
            }
            is DetailInteractionEvents.SearchActor -> {
                openSearch(SearchType.SEARCH_ACTOR, interactionEvents.property, null)
            }
            is DetailInteractionEvents.SearchDirector -> {
                openSearch(SearchType.SEARCH_DIRECTOR, interactionEvents.property, null)
            }
            is DetailInteractionEvents.SearchCountry -> {
                openSearch(SearchType.SEARCH_COUNTRY, interactionEvents.property, null)
            }
        }
    }

    private fun openSearch(searchType: SearchType, property: Property?, keyWord: String?) {
        val bundle = Bundle().apply {
            putString(Constants.KEYWORD, keyWord)
            putParcelable(Constants.PROPERTY, property)
            putSerializable(Constants.SEARCH_MOVIE, searchType)
        }
        findNavController().navigate(R.id.action_detailFragment_to_searchFragment, bundle)
    }
}