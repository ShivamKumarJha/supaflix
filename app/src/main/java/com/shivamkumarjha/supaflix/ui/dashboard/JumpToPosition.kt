/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.shivamkumarjha.supaflix.ui.dashboard

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shivamkumarjha.supaflix.R

private enum class Visibility {
    VISIBLE,
    GONE
}

/**
 * Shows a button that lets the user scroll to the bottom.
 */
@Composable
fun JumpToPosition(
    enabled: Boolean,
    scrollUp: Boolean,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Show Jump to Bottom button
    val transition = updateTransition(
        if (enabled) Visibility.VISIBLE else Visibility.GONE,
        label = ""
    )
    val bottomOffset by transition.animateDp(label = "") {
        if (it == Visibility.GONE) {
            (-32).dp
        } else {
            32.dp
        }
    }

    val imageVector = if (scrollUp)
        Icons.Filled.ArrowUpward
    else
        Icons.Filled.ArrowDownward
    val textMessage = if (scrollUp)
        stringResource(id = R.string.jumpTop)
    else
        stringResource(id = R.string.jumpBottom)
    if (bottomOffset > 0.dp) {
        ExtendedFloatingActionButton(
            icon = {
                Icon(
                    imageVector = imageVector,
                    modifier = Modifier.height(18.dp),
                    contentDescription = null
                )
            },
            text = {
                Text(text = textMessage)
            },
            onClick = onClicked,
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.primary,
            modifier = modifier
                .offset(x = 0.dp, y = -bottomOffset)
                .height(36.dp)
        )
    }
}

@Preview
@Composable
fun JumpToPositionPreview() {
    JumpToPosition(enabled = true, scrollUp = true, onClicked = {})
}
