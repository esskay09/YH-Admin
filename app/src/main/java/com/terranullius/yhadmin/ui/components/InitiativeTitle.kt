package com.terranullius.yhadmin.ui.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun InitiativeTitle(modifier: Modifier = Modifier, text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.h4.copy(
            color = Color(0xFFF8E4E6),
            background = Color(0xFFA22B3E),
            fontWeight = FontWeight.Bold
        ),
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    InitiativeTitle(text = "Enlightening the youth")
}