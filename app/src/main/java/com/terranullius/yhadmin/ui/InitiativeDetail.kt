package com.terranullius.yhadmin.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.terranullius.yhadmin.data.Initiative
import com.terranullius.yhadmin.other.Constants.AB_HELP
import com.terranullius.yhadmin.other.Constants.AB_SHARE
import com.terranullius.yhadmin.other.Constants.DIALOG_FB
import com.terranullius.yhadmin.other.Constants.DIALOG_INSTA
import com.terranullius.yhadmin.other.Constants.DIALOG_TWITTER
import com.terranullius.yhadmin.other.UpdateImageProperties
import com.terranullius.yhadmin.ui.components.*
import com.terranullius.yhadmin.ui.components.edior.Editable
import com.terranullius.yhadmin.ui.components.edior.ImageVideoLinkDialog
import com.terranullius.yhadmin.utils.Result
import com.terranullius.yhadmin.viewmodels.MainViewModel
import terranullius.yhadmin.R
import kotlin.math.min

@ExperimentalPagerApi
@Composable
fun InitiativeDetail(
    initiative: MutableState<Initiative>,
    modifier: Modifier = Modifier,
    onBottomBarItemClicked: (String) -> Unit,
    onShareDialogClicked: (link: String) -> Unit,
    onHelpClicked: (link: String?, isPayable: Boolean, amount: Int?) -> Unit,
    viewModel: MainViewModel
) {
    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()
    val isShareClicked = remember {
        mutableStateOf(false)
    }
    val isHelpClicked = remember {
        mutableStateOf(false)
    }

    var isImageSelected by remember {
        mutableStateOf(false)
    }

    var isImageVideoPickerShown by remember {
        mutableStateOf(false)
    }

    var updatedInitiative = remember(initiative, initiative.value) {
        mutableStateOf<Initiative>(initiative.value)
    }

    val isVideoPlaying = remember {
        mutableStateOf(false)
    }

    var isEditing by remember {
        mutableStateOf(false)
    }

    val uploadStatus = viewModel.imageUploadStatus.collectAsState()

    var isUploadingDialogShown by remember(uploadStatus.value){
        mutableStateOf(uploadStatus.value == Result.Loading)
    }

    val pagerState = rememberPagerState(
        initialPage = updatedInitiative.value.initialPage,
        pageCount = min(
            updatedInitiative.value.images.size,
            updatedInitiative.value.descriptions.size
        ),
        initialOffscreenLimit = 1,
        infiniteLoop = true
    )

    if(isUploadingDialogShown){
        Dialog(onDismissRequest = { }, properties = DialogProperties(dismissOnClickOutside = false)) {
             CircularProgressIndicator(color = MaterialTheme.colors.secondary)
        }
    }

    if (isShareClicked.value) {
        Dialog(onDismissRequest = {
            isShareClicked.value = false
        }) {
            ShareDialog(
                modifier = Modifier.fillMaxWidth(),
                isEditing = isEditing,
                shareLinks = updatedInitiative.value.shareLinks,
                onShareClicked = {
                    if (!isEditing) {
                        when (it) {
                            DIALOG_INSTA -> onShareDialogClicked(updatedInitiative.value.shareLinks.insta)
                            DIALOG_FB -> onShareDialogClicked(updatedInitiative.value.shareLinks.fb)
                            DIALOG_TWITTER -> onShareDialogClicked(updatedInitiative.value.shareLinks.twitter)
                        }
                    }
                isShareClicked.value = false
            }){
                updatedInitiative.value = updatedInitiative.value.copy(shareLinks = it)
            }
        }
    }
    if (isHelpClicked.value) {
        Dialog(onDismissRequest = {
            isHelpClicked.value = false
        }) {
            HelpDialog(
                modifier = Modifier,
                isEditing = isEditing,
                updatedInitative = updatedInitiative,
                onHelpClicked = { amount ->
                    isHelpClicked.value = false
                    onHelpClicked(
                        updatedInitiative.value.helpLink,
                        updatedInitiative.value.isDonatable,
                        amount
                    )
                }
            )
        }
    }

    if (isImageVideoPickerShown) {
        Dialog(
            onDismissRequest = {
                isImageVideoPickerShown = false
                isImageSelected = false
            }) {
            ImageVideoLinkDialog(
                modifier = Modifier.fillMaxWidth(0.9f),
                onApplyClick = { isVideo: Boolean, videoId: String ->
                    if (isVideo) {
                        updatedInitiative.value.images[pagerState.currentPage] = "youtubeID=$videoId"
                        viewModel.updateInitiative(updatedInitiative.value)
                        isImageSelected = false
                        isImageVideoPickerShown = false
                    } else {
                        if (!isImageSelected) {
                            viewModel.getImage(
                                UpdateImageProperties(
                                    isUpdating = true,
                                    initiative = updatedInitiative.value,
                                    imagePosition = pagerState.currentPage
                                )
                            )
                        } else {
                            viewModel.uploadImage()
                            isImageVideoPickerShown = false
                        }
                        isImageSelected = !isImageSelected
                    }
                },
                isImageSelected = isImageSelected
            )
        }
    }

    Scaffold(modifier = modifier, scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                isHelpClicked.value = true
                onBottomBarItemClicked(AB_HELP)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_heart_filled),
                    contentDescription = "Help",
                    tint = MaterialTheme.colors.primary
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        bottomBar = {
            BottomBar(
                onBottomBarItemClicked = {
                    when (it) {
                        AB_SHARE -> isShareClicked.value = true
                    }
                    onBottomBarItemClicked(it)
                }
            )
        }) {

        Surface(color = MaterialTheme.colors.primary) {
            Box(
                modifier = modifier
                    .padding(
                        start = 8.dp,
                        end = 8.dp,
                        top = 8.dp,
                        bottom = it.calculateBottomPadding().plus(6.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.4f),
                        shape = RoundedCornerShape(15.dp),
                        elevation = 18.dp
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            val images = remember(updatedInitiative, updatedInitiative.value) {
                                mutableStateOf(updatedInitiative.value.images as List<String>)
                            }
                            ViewPagerImages(
                                modifier = if (isEditing) Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        isImageVideoPickerShown = true
                                    } else Modifier.fillMaxSize(),
                                images = images,
                                pagerState = pagerState,
                                isVideoPlaying = isVideoPlaying,
                                onYoutubePlayerClicked = {
                                    if(isEditing){
                                        isImageVideoPickerShown = true
                                    }
                                }
                            )
                            HorizontalPagerIndicator(
                                pagerState = pagerState, modifier = Modifier
                                    .align(
                                        Alignment.BottomEnd
                                    )
                                    .padding(6.dp)
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Editable(
                        isEditing,
                        initialText = updatedInitiative.value.title,
                        textStyle = MaterialTheme.typography.h4.copy(
                            color = Color(0xFFF8E4E6),
                            background = Color(0xFFA22B3E),
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        updatedInitiative.value.title = it
                    }
                    Spacer(Modifier.height(14.dp))
                    Box(modifier = Modifier.fillMaxHeight()) {
                        Editable(
                            initialText = updatedInitiative.value.descriptions[pagerState.currentPage],
                            isEditing = isEditing,
                            textStyle = MaterialTheme.typography.body1.copy(
                                color = Color.Black,
                                fontSize = integerResource(id = R.integer.initiative_detail_description).sp
                            ),
                            modifier = Modifier.verticalScroll(state = scrollState)
                        ) {
                            updatedInitiative.value.descriptions[pagerState.currentPage] = it
                        }
                    }
                }

                ExtendedFloatingActionButton(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    backgroundColor = Color.Cyan,
                    text = { Text(text = if (isEditing) "Save" else "Edit") },
                    icon = {
                        if (!isEditing) Icon(Icons.Default.Edit, "")
                        else Icon(Icons.Filled.Done, "")
                    },
                    onClick = {
                        isEditing = !isEditing
                        if (!isEditing) {
                            viewModel.updateInitiative(updatedInitiative.value)
                        }
                    }
                )
            }
        }
    }
}