package com.terranullius.yellowheart.ui.components

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CircularProgress(
    modifier: Modifier = Modifier
) {
    CircularProgressIndicator(
        modifier = modifier,
        color = MaterialTheme.colors.secondary
    )
}