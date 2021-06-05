package com.shivamkumarjha.supaflix.ui.dashboard

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.shivamkumarjha.supaflix.R
import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.model.db.Download
import com.shivamkumarjha.supaflix.model.xmovies.Property
import com.shivamkumarjha.supaflix.ui.search.SearchType
import com.shivamkumarjha.supaflix.ui.theme.SupaflixTheme
import com.shivamkumarjha.supaflix.utility.toast
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
@ExperimentalMaterialApi
class DashboardFragment : Fragment() {

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

            setContent {
                SupaflixTheme {
                    BottomNavigation(interactionEvents = {
                        handleInteractionEvents(it)
                    })
                }
            }
        }
    }

    private fun handleInteractionEvents(interactionEvents: DashboardInteractionEvents) {
        when (interactionEvents) {
            is DashboardInteractionEvents.OpenMovieDetail -> {
                val bundle = Bundle().apply {
                    putString(Constants.HASH, interactionEvents.hash)
                }
                findNavController().navigate(
                    R.id.action_dashboardFragment_to_detailFragment,
                    bundle
                )
            }
            is DashboardInteractionEvents.SearchMovie -> {
                openSearch(SearchType.SEARCH_MOVIE, null, interactionEvents.keyWord)
            }
            is DashboardInteractionEvents.SearchYear -> {
                openSearch(SearchType.SEARCH_YEAR, null, interactionEvents.year)
            }
            is DashboardInteractionEvents.SearchGenre -> {
                openSearch(SearchType.SEARCH_GENRE, interactionEvents.property, null)
            }
            is DashboardInteractionEvents.ResumePlayback -> {
                val bundle = Bundle().apply {
                    putParcelable(Constants.HISTORY, interactionEvents.history)
                }
                findNavController().navigate(
                    R.id.action_dashboardFragment_to_playerFragment,
                    bundle
                )
            }
            is DashboardInteractionEvents.OpenDownloadedFile -> {
                playLocalVideo(interactionEvents.download)
            }
        }
    }

    private fun openSearch(searchType: SearchType, property: Property?, keyWord: String?) {
        val bundle = Bundle().apply {
            putString(Constants.KEYWORD, keyWord)
            putParcelable(Constants.PROPERTY, property)
            putSerializable(Constants.SEARCH_MOVIE, searchType)
        }
        findNavController().navigate(R.id.action_dashboardFragment_to_searchFragment, bundle)
    }

    private fun playLocalVideo(download: Download) {
        val filePath = download.filePath ?: return
        try {
            val uri = FileProvider.getUriForFile(
                requireContext(),
                requireActivity().applicationContext.packageName.toString() + ".provider",
                File(filePath)
            )
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "video/*")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(intent)
        } catch (exception: ActivityNotFoundException) {
            requireContext().toast(getString(R.string.no_application_found_to_open_file))
        } catch (exception: Exception) {
            Log.e(Constants.TAG, exception.localizedMessage ?: "")
        }
    }
}