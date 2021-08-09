package com.terranullius.yhadmin.ui.components.edior

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.terranullius.yhadmin.ui.theme.dividerDialogItemHeight

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ImageVideoLinkDialog(
    modifier: Modifier = Modifier,
    onApplyClick: (isVideo: Boolean, link: String) -> Unit,
    onImageClick: () -> Unit,
    initialVideoLink: String = ""
) {

    Column(
        modifier = modifier
    ) {
        var newVideoLink by remember {
            mutableStateOf("")
        }
        var isVideoChoosen by remember {
            mutableStateOf(false)
        }
        TextButton(onClick = {
            isVideoChoosen = false
            onImageClick()
        }) {
            Text(text = "Image")
        }
        Divider(Modifier.height(dividerDialogItemHeight))
        TextButton(onClick = {
            isVideoChoosen = true
        }) {
            Text(text = "Video")
        }
        Divider(Modifier.height(dividerDialogItemHeight))
        AnimatedVisibility(visible = isVideoChoosen) {
            Editable(
                isEditing = true,
                initialText = initialVideoLink,
                label = {
                    Text(text = "videoID = ")
                },
                updatedText = {
                    newVideoLink = it
                })
        }
        Divider(Modifier.height(dividerDialogItemHeight))
        Divider(Modifier.height(dividerDialogItemHeight))

        Button(
            modifier = Modifier.align(Alignment.End),
            onClick = {
                onApplyClick(isVideoChoosen, newVideoLink)
            }) {
            Text(text = "Apply")
        }
    }
}
