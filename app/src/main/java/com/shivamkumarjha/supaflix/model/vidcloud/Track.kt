package com.shivamkumarjha.supaflix.model.vidcloud

import com.google.gson.annotations.SerializedName


data class Track(@SerializedName("tracks") val tracks: ArrayList<Tracks>)