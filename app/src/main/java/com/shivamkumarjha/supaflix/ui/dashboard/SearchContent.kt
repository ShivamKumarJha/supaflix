package com.shivamkumarjha.supaflix.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.shivamkumarjha.supaflix.R
import com.shivamkumarjha.supaflix.model.app.Genre
import com.shivamkumarjha.supaflix.model.app.GenreList
import com.shivamkumarjha.supaflix.ui.common.StaggeredVerticalGrid
import com.shivamkumarjha.supaflix.ui.theme.ColorUtility
import com.shivamkumarjha.supaflix.ui.theme.GraySurface
import com.shivamkumarjha.supaflix.utility.Utility

@Composable
fun SearchScreen(navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorUtility.surfaceBackground(isSystemInDarkTheme()))
            .padding(bottom = 60.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(32.dp))
            SearchBar()
            SearchByGenre()
            SearchByYear()
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun SearchBar() {
    val searchLayoutHeightDp = 70.dp
    val background = if (isSystemInDarkTheme()) GraySurface else Color.White.copy(alpha = 0.8f)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .graphicsLayer(translationY = 0F)
            .height(searchLayoutHeightDp)
            .padding(8.dp)
            .shadow(8.dp, shape = RoundedCornerShape(8.dp), clip = false)
            .background(background, shape = RoundedCornerShape(8.dp))

    ) {
        val textState = remember { mutableStateOf(TextFieldValue()) }
        TextField(
            value = textState.value,
            placeholder = { Text(stringResource(id = R.string.search_info)) },
            onValueChange = { textState.value = it },
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = background,
                cursorColor = MaterialTheme.colors.onSurface,
                focusedIndicatorColor = background,
                disabledIndicatorColor = background
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            maxLines = 1,
            textStyle = typography.body2
        )
        IconButton(onClick = {}) {
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
        }
    }
}

@Composable
fun SearchByGenre() {
    Text(
        text = stringResource(id = R.string.search_genres),
        style = typography.body1,
        color = ColorUtility.textColor(isSystemInDarkTheme()),
        modifier = Modifier.padding(8.dp)
    )
    StaggeredVerticalGrid(
        maxColumnWidth = 150.dp,
        modifier = Modifier.padding(4.dp)
    ) {
        GenreList.getAllGenres().forEach { genre ->
            GenreButton(genre = genre)
        }
    }
}

@Composable
fun GenreButton(genre: Genre) {
    Button(modifier = Modifier.padding(4.dp), onClick = {
    }) {
        Text(
            text = genre.slug,
            style = typography.body2
        )
    }
}

@Composable
fun SearchByYear() {
    Text(
        text = stringResource(id = R.string.search_years),
        style = typography.body1,
        color = ColorUtility.textColor(isSystemInDarkTheme()),
        modifier = Modifier.padding(8.dp)
    )
    // Logic to show our desired UI
    var flag = true
    var year = Utility.getCurrentYear()
    val remainder = year % 10
    year += (10 - remainder) // round off to next decade, doest affect list as we add year less that or equal to current year
    while (flag) {
        val yearSubList: ArrayList<Int> = arrayListOf()
        if (year % 10 == 0) {
            Text(
                text = "${year - 10}'s",
                style = typography.body2,
                color = ColorUtility.textColor(isSystemInDarkTheme()),
                modifier = Modifier.padding(4.dp)
            )
            year--
            if (year <= Utility.getCurrentYear())
                yearSubList.add(year)
            while (year % 10 != 0) {
                year--
                if (year <= Utility.getCurrentYear())
                    yearSubList.add(year)
            }
        }
        LazyRow {
            items(yearSubList.reversed()) { year ->
                YearButton(year)
            }
        }
        if (year <= 1949)
            flag = false
    }
}

@Composable
fun YearButton(year: Int) {
    Button(modifier = Modifier.padding(4.dp), onClick = {
    }) {
        Text(
            text = year.toString(),
            style = typography.body2
        )
    }
}