package com.terranullius.yellowheartadmin.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.terranullius.yellowheartadmin.data.Initiative
import com.terranullius.yellowheartadmin.other.Constants.AB_HELP
import com.terranullius.yellowheartadmin.other.Constants.AB_SHARE
import com.terranullius.yellowheartadmin.other.Constants.DIALOG_FB
import com.terranullius.yellowheartadmin.other.Constants.DIALOG_INSTA
import com.terranullius.yellowheartadmin.other.Constants.DIALOG_TWITTER
import com.terranullius.yellowheartadmin.ui.components.*
import com.terranullius.yellowheartadmin.ui.components.edior.Editable
import terranullius.yellowheartadmin.R
import kotlin.math.min

@ExperimentalPagerApi
@Composable
fun InitiativeDetail(
    initiative: Initiative,
    modifier: Modifier = Modifier,
    onBottomBarItemClicked: (String) -> Unit,
    onShareDialogClicked: (link: String) -> Unit,
    onHelpClicked: (link: String?, isPayable: Boolean, amount: Int?) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()
    val isShareClicked = remember {
        mutableStateOf(false)
    }
    val isHelpClicked = remember {
        mutableStateOf(false)
    }

    var updatedInitative by remember {
        mutableStateOf(initiative)
    }

    val isVideoPlaying = remember {
        mutableStateOf(false)
    }

    var isEditing by remember {
        mutableStateOf(false)
    }

    val pagerState = rememberPagerState(
        initialPage = initiative.initialPage,
        pageCount = min(initiative.images.size, initiative.descriptions.size),
        initialOffscreenLimit = 1,
        infiniteLoop = true
    )

    if (isShareClicked.value) {
        Dialog(onDismissRequest = {
            isShareClicked.value = false
        }) {
            ShareDialog(modifier = Modifier.fillMaxWidth(), onShareClicked = {
                when (it) {
                    DIALOG_INSTA -> onShareDialogClicked(initiative.shareLinks.insta)
                    DIALOG_FB -> onShareDialogClicked(initiative.shareLinks.fb)
                    DIALOG_TWITTER -> onShareDialogClicked(initiative.shareLinks.twitter)
                }
                isShareClicked.value = false
            })
        }
    }
    if (isHelpClicked.value) {
        Dialog(onDismissRequest = {
            isHelpClicked.value = false
        }) {
            HelpDialog(
                modifier = Modifier,
                isDonatable = initiative.isPayable,
                link = initiative.helpLink,
                description = initiative.helpDescription ?: "Help",
                onHelpClicked = { amount ->
                    isHelpClicked.value = false
                    onHelpClicked(
                        initiative.helpLink,
                        initiative.isPayable,
                        amount
                    )
                }
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
                        AB_SHARE -> isHelpClicked.value = true
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
                        bottom = it.calculateBottomPadding()
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
                            ViewPagerImages(
                                modifier = Modifier.fillMaxSize(),
                                images = initiative.images,
                                pagerState = pagerState,
                                isVideoPlaying = isVideoPlaying
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
                        initialText = initiative.title,
                        textStyle = MaterialTheme.typography.h4.copy(
                            color = Color(0xFFF8E4E6),
                            background = Color(0xFFA22B3E),
                            fontWeight = FontWeight.Bold
                        )
                    ){
                        updatedInitative.title = it
                    }
                    Spacer(Modifier.height(14.dp))
                    Box(modifier = Modifier.fillMaxHeight()) {
                        Editable(
                            initialText = initiative.descriptions[pagerState.currentPage],
                            isEditing = isEditing,
                            textStyle = MaterialTheme.typography.body1.copy(
                                color = Color.Black,
                                fontSize = integerResource(id = R.integer.initiative_detail_description).sp
                            ),
                            modifier = Modifier.verticalScroll(state = scrollState)
                        ){
                            updatedInitative.descriptions[pagerState.currentPage] = it
                        }
                    }
                }

                FloatingActionButton(
                    onClick = { isEditing = !isEditing }, modifier = Modifier.align(
                        Alignment.BottomEnd
                    )
                ) {
                    Row() {
                        Icon(Icons.Default.Edit, "")
                        Text(text = if (isEditing) "Save" else "Edit")
                    }
                }
            }
        }
    }
}