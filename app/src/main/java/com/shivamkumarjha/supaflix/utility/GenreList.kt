package com.shivamkumarjha.supaflix.utility

import com.shivamkumarjha.supaflix.R
import com.shivamkumarjha.supaflix.model.app.Genre

object GenreList {
    private val genres: ArrayList<Genre> = arrayListOf()

    private val actionGenre = Genre("J7HHcRU7", "action")
    private val adventureGenre = Genre("BOhohUUh", "adventure")
    private val animationGenre = Genre("I8UxM9cV", "animation")
    private val biographyGenre = Genre("DxtYHrRQ", "biography")
    private val costumeGenre = Genre("Ooy47EnD", "costume")
    private val comedyGenre = Genre("wnq50Wt2", "comedy")
    private val crimeGenre = Genre("8gT29yYO", "crime")
    private val documentaryGenre = Genre("jrgZDu4c", "documentary")
    private val familyGenre = Genre("02R3oJj7", "family")
    private val fantasyGenre = Genre("DVTJoVmF", "fantasy")
    private val gameShowGenre = Genre("lwxxvXvm", "game-show")
    private val historyGenre = Genre("i5IGpR5x", "history")
    private val horrorGenre = Genre("JQJPb64x", "horror")
    private val kungfuGenre = Genre("t8FZTE2E", "kungfu")
    private val musicGenre = Genre("iVs02T2f", "music")
    private val mysteryGenre = Genre("4dRk5f6B", "mystery")
    private val realityTvGenre = Genre("fEbGIjFI", "reality-tv")
    private val romanceGenre = Genre("Fe2rfiBq", "romance")
    private val sciFiGenre = Genre("D1NvOmlA", "sci-fi")
    private val sportGenre = Genre("6nM1oJXH", "sport")
    private val thrillerGenre = Genre("AIKQberR", "thriller")
    private val tvShowGenre = Genre("7fBKjHmk", "tv-show")
    private val warGenre = Genre("flBwy9lF", "war")
    private val westernGenre = Genre("sYITdTAG", "western")

    fun getAllGenres(): ArrayList<Genre> {
        genres.clear()
        genres.add(actionGenre)
        genres.add(adventureGenre)
        genres.add(animationGenre)
        genres.add(biographyGenre)
        genres.add(costumeGenre)
        genres.add(comedyGenre)
        genres.add(crimeGenre)
        genres.add(documentaryGenre)
        genres.add(familyGenre)
        genres.add(fantasyGenre)
        genres.add(gameShowGenre)
        genres.add(historyGenre)
        genres.add(horrorGenre)
        genres.add(kungfuGenre)
        genres.add(musicGenre)
        genres.add(mysteryGenre)
        genres.add(realityTvGenre)
        genres.add(romanceGenre)
        genres.add(sciFiGenre)
        genres.add(sportGenre)
        genres.add(thrillerGenre)
        genres.add(tvShowGenre)
        genres.add(warGenre)
        genres.add(westernGenre)
        return genres
    }

    fun getGenreName(genre: Genre): Int {
        return when (genre) {
            actionGenre -> R.string.action
            adventureGenre -> R.string.adventure
            animationGenre -> R.string.animation
            biographyGenre -> R.string.biography
            costumeGenre -> R.string.costume
            comedyGenre -> R.string.comedy
            crimeGenre -> R.string.crime
            documentaryGenre -> R.string.documentary
            familyGenre -> R.string.family
            fantasyGenre -> R.string.fantasy
            gameShowGenre -> R.string.game_show
            historyGenre -> R.string.history
            horrorGenre -> R.string.horror
            kungfuGenre -> R.string.kung_fu
            musicGenre -> R.string.music
            mysteryGenre -> R.string.mystery
            realityTvGenre -> R.string.reality_tv
            romanceGenre -> R.string.romance
            sciFiGenre -> R.string.scifi
            sportGenre -> R.string.sport
            thrillerGenre -> R.string.thriller
            tvShowGenre -> R.string.tv_show
            warGenre -> R.string.war
            westernGenre -> R.string.western
            else -> R.string.action
        }
    }
}