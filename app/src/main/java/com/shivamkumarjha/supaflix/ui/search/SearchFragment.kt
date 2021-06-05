package com.shivamkumarjha.supaflix.ui.search

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
import com.shivamkumarjha.supaflix.ui.theme.SupaflixTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalMaterialApi
class SearchFragment : Fragment() {

    //Bundle args
    private val keyWord by lazy {
        arguments?.getString(Constants.KEYWORD)
    }
    private val property by lazy {
        arguments?.getParcelable(Constants.PROPERTY) as Property?
    }
    private val searchType by lazy {
        arguments?.getSerializable(Constants.SEARCH_MOVIE) as SearchType?
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

            if (searchType != null) {
                setContent {
                    SupaflixTheme {
                        SearchMovieContent(searchType!!, property, keyWord, interactionEvents = {
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
        interactionEvents: SearchInteractionEvents
    ) {
        when (interactionEvents) {
            is SearchInteractionEvents.NavigateUp -> findNavController().navigateUp()
            is SearchInteractionEvents.OpenDetail -> {
                val bundle = Bundle().apply {
                    putString(Constants.HASH, interactionEvents.contents.hash)
                }
                findNavController().navigate(R.id.action_searchFragment_to_detailFragment, bundle)
            }
        }
    }
}