package com.terranullius.yellowheart.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.terranullius.yellowheart.other.Constants.DIALOG_FB
import com.terranullius.yellowheart.other.Constants.DIALOG_INSTA
import com.terranullius.yellowheart.other.Constants.DIALOG_TWITTER
import terranullius.yellowheart.R


//TODO CREATE CLASS FOR THIS

@Composable
fun ShareDialog(
    modifier: Modifier = Modifier,
    onShareClicked: (id: String) -> Unit
) {
    Surface(color = MaterialTheme.colors.secondaryVariant) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ShareItem(name = "Facebook", imgRes = R.drawable.facebook, onShareClicked = onShareClicked)
            ShareItem(name = "Instagram" , imgRes = R.drawable.instagram, onShareClicked = onShareClicked)
            ShareItem(name = "Twitter", imgRes = R.drawable.twitter, onShareClicked = onShareClicked)
        }
    }

}

@Composable
fun ShareItem(
    name: String,
    @DrawableRes imgRes: Int,
    modifier: Modifier = Modifier,
    onShareClicked: (String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .height(60.dp)
            .clickable {
                val id = when (name) {
                    "Facebook" -> DIALOG_FB
                    "Twitter" -> DIALOG_TWITTER
                    "Instagram" -> DIALOG_INSTA
                    else -> ""
                }
                onShareClicked(id)
            }, verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imgRes),
            contentDescription = "",
            modifier = Modifier.size(50.dp)
        )
        Divider(
            Modifier
                .height(0.dp)
                .width(8.dp)
        )
        Text(text = name)
    }
}