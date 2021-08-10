package com.terranullius.yhadmin.ui.components.edior

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
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
    var isVideoChosen by remember {
    mutableStateOf(false)
}
    Column() {
        Surface(shape = dialogShape) {
            var newVideoLink by remember {
                mutableStateOf("")
            }

            Column(
                modifier = modifier
                    .padding(dialogPadding)
                    .animateContentSize()
            ) {

                ImageVideoDialogItem(
                    text = "Image",
                    onClick = {
                        isVideoChosen = false },
                    isSelected = !isVideoChosen)

                Spacer(Modifier.height(dividerDialogItemHeight))

                ImageVideoDialogItem(
                    text = "Video",
                    onClick = {
                        isVideoChosen = true },
                    isSelected = isVideoChosen)

                Spacer(Modifier.height(dividerDialogItemHeight))

                if(isVideoChosen) {
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

                else{
                    Spacer(Modifier.height(dividerDialogItemHeight.times(4)))
                }

                Box(Modifier.fillMaxWidth()) {
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = secondaryColor),
                        modifier = Modifier.align(Alignment.TopEnd),
                        onClick = {
                            onApplyClick(isVideoChosen, newVideoLink)
                        }) {
                        if (isVideoChosen){
                            Text(text = "Apply")
                        } else {if (isImageSelected) Text(text = "Apply")
                        else Text(text = "Select Image")}
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(dividerDialogItemHeight.times(4)))
    }

}

@Composable
fun ImageVideoDialogItem(text: String, onClick: () -> Unit, isSelected: Boolean) {
    Row(
        Modifier
            .padding(vertical = 2.dp)
            .clickable {
                onClick()
            }
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick
        )
        Divider(Modifier.width(4.dp))
        Text(text = text)
    }
}
