package com.terrranulius.yellowheart.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.terrranulius.yellowheart.R
import com.terrranulius.yellowheart.ui.theme.secondaryColor

@Composable
fun HelpButton(
    modifier: Modifier = Modifier, showText: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = { /*TODO*/ }, modifier = modifier.clickable {
            onClick()
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = secondaryColor
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (showText) {
                Text(text = "Help", modifier = Modifier.padding(end = 2.dp))
            }
            Icon(
                modifier = Modifier.padding(start = 2.dp),
                painter = painterResource(id = R.drawable.ic_heart_filled),
                contentDescription = "",
                tint = MaterialTheme.colors.primary
            )
        }
    }
}