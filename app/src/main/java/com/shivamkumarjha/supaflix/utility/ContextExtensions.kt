package com.shivamkumarjha.supaflix.utility

import android.content.Context
import android.widget.Toast
import androidx.core.content.ContextCompat

fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun Context.getColorById(id: Int) = ContextCompat.getColor(this, id)