package com.terranullius.yhadmin.ui.components.edior

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.terranullius.yhadmin.ui.theme.dialogPadding
import com.terranullius.yhadmin.ui.theme.dialogShape
import com.terranullius.yhadmin.ui.theme.dividerDialogItemHeight
import com.terranullius.yhadmin.ui.theme.secondaryColor

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ImageVideoLinkDialog(
    modifier: Modifier = Modifier,
    onApplyClick: (isVideo: Boolean, link: String) -> Unit,
    initialVideoLink: String = "",
    isImageSelected: Boolean = false
) {
    Surface(shape = dialogShape) {
        var newVideoLink by remember {
            mutableStateOf("")
        }
        var isVideoChosen by remember {
            mutableStateOf(false)
        }
        Column(
            modifier = modifier
                .padding(dialogPadding)
        ) {

            Row() {
                RadioButton(
                    selected = !isVideoChosen,
                    onClick = {
                        isVideoChosen = false
                    })
                Text(text = "Image")
            }

            Spacer(Modifier.height(dividerDialogItemHeight))

            Row() {
                RadioButton(
                    selected = isVideoChosen,
                    onClick = {
                        isVideoChosen = true
                    })
                Text(text = "Video")
            }

            Spacer(Modifier.height(dividerDialogItemHeight))


            AnimatedVisibility(visible = isVideoChosen) {
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
            if (!isVideoChosen) Spacer(Modifier.height(dividerDialogItemHeight.times(3)))

            Box(Modifier.fillMaxWidth()) {
                Button(
                    colors = ButtonDefaults.buttonColors(backgroundColor = secondaryColor),
                    modifier = Modifier.align(Alignment.TopEnd),
                    onClick = {
                        onApplyClick(isVideoChosen, newVideoLink)
                    }) {
                    if (isImageSelected) Text(text = "Apply")
                    else Text(text = "Select Image")
                }
            }

        }
    }
}
