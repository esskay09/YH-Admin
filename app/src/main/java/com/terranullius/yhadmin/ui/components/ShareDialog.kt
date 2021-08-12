package com.terranullius.yhadmin.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.terranullius.yhadmin.data.ShareLinks
import com.terranullius.yhadmin.other.Constants.DIALOG_FB
import com.terranullius.yhadmin.other.Constants.DIALOG_INSTA
import com.terranullius.yhadmin.other.Constants.DIALOG_TWITTER
import com.terranullius.yhadmin.ui.components.edior.Editable
import terranullius.yhadmin.R

@Composable
fun ShareDialog(
    modifier: Modifier = Modifier,
    isEditing: Boolean = false,
    onShareClicked: (id: String) -> Unit,
    shareLinks: ShareLinks,
    onShareLinksChanged: (ShareLinks) -> Unit = {}
) {
    var prevShareLinks by remember {
        mutableStateOf(shareLinks)
    }

    Surface(color = MaterialTheme.colors.secondaryVariant) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ShareItem(
                name = "Facebook",
                isEditing = isEditing,
                imgRes = R.drawable.facebook,
                initialLink = prevShareLinks.fb,
                onShareClicked = onShareClicked
            ) {
                prevShareLinks = prevShareLinks.copy(fb = it)
                onShareLinksChanged(prevShareLinks)
            }
            ShareItem(
                name = "Instagram",
                imgRes = R.drawable.instagram,
                isEditing = isEditing,
                initialLink = prevShareLinks.insta,
                onShareClicked = onShareClicked
            ) {
                prevShareLinks = prevShareLinks.copy(insta = it)
                onShareLinksChanged(prevShareLinks)
            }
            ShareItem(
                name = "Twitter",
                imgRes = R.drawable.twitter,
                isEditing = isEditing,
                initialLink = prevShareLinks.twitter,
                onShareClicked = onShareClicked
            ) {
                prevShareLinks = prevShareLinks.copy(twitter = it)
                onShareLinksChanged(prevShareLinks)
            }
        }
    }

}

@Composable
fun ShareItem(
    name: String,
    initialLink: String,
    isEditing: Boolean,
    @DrawableRes imgRes: Int,
    modifier: Modifier = Modifier,
    onShareClicked: (String) -> Unit,
    onLinkChange: (String) -> Unit,

) {
    val clickableModifier =  modifier
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
        }

    val unClickableModifier = modifier
        .fillMaxWidth(0.9f)
        .height(60.dp)

    Row(
        modifier = if (!isEditing) clickableModifier else unClickableModifier , verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imgRes),
            contentDescription = "",
            modifier = Modifier.size(50.dp)
        )
        Spacer(
            Modifier
                .width(8.dp)
        )
        Editable(isEditing = isEditing, initialText = if (isEditing) initialLink else name, updatedText = onLinkChange)
    }
}