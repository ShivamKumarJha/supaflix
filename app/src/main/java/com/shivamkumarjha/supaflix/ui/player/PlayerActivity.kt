package com.shivamkumarjha.supaflix.ui.player

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.shivamkumarjha.supaflix.R
import com.shivamkumarjha.supaflix.model.db.History
import com.shivamkumarjha.supaflix.ui.theme.SupaflixTheme
import com.shivamkumarjha.supaflix.utility.Utility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerActivity : ComponentActivity() {
    private var history: History? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        history = intent.getParcelableExtra(HISTORY) as History?
        if (history != null) {
            setContent {
                SupaflixTheme {
                    PlayContent(history!!, interactionEvents = {
                        handleInteractionEvents(it)
                    })
                }
            }
        }
    }

    private fun handleInteractionEvents(interactionEvents: PlayerInteractionEvents) {
        when (interactionEvents) {
            is PlayerInteractionEvents.NavigateUp -> {
                Toast.makeText(this, getString(R.string.server_error), Toast.LENGTH_LONG).show()
                onBackPressed()
            }
            is PlayerInteractionEvents.OpenBrowser -> {
                Utility.openLinkInBrowser(interactionEvents.url, this)
                onBackPressed()
            }
            is PlayerInteractionEvents.OpenPlayer -> {
                setContent {
                    SupaflixTheme {
                        PlayerContent(
                            url = interactionEvents.url,
                            type = interactionEvents.type,
                            subtitleUrl = interactionEvents.subtitle
                        )
                    }
                }
            }
        }
    }

    companion object {
        const val HISTORY = "history"
        fun newIntent(context: Context, history: History) =
            Intent(context, PlayerActivity::class.java).apply {
                putExtra(HISTORY, history)
            }
    }
}