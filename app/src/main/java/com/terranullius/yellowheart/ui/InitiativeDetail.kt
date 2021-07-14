package com.terranullius.yellowheart.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.terranullius.yellowheart.data.Initiative
import com.terranullius.yellowheart.other.Constants.AB_HELP
import com.terranullius.yellowheart.other.Constants.AB_SHARE
import com.terranullius.yellowheart.other.Constants.DIALOG_FB
import com.terranullius.yellowheart.other.Constants.DIALOG_INSTA
import com.terranullius.yellowheart.other.Constants.DIALOG_TWITTER
import com.terranullius.yellowheart.ui.InitiativeTitle
import com.terranullius.yellowheart.ui.components.BottomBar
import com.terranullius.yellowheart.ui.components.HelpDialog
import com.terranullius.yellowheart.ui.components.ShareDialog
import com.terranullius.yellowheart.ui.components.ViewPagerImages
import terranullius.yellowheart.R
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

    val pagerState = rememberPagerState(
        initialPage = initiative.initialPage,
        pageCount = min(initiative.images.size,initiative.descriptions.size),
        initialOffscreenLimit = 1
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
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = it.calculateBottomPadding())
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
                                pagerState = pagerState
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
                    InitiativeTitle(
                        text = initiative.name,

                    )
                    Spacer(Modifier.height(14.dp))
                    Box(modifier = Modifier.fillMaxHeight()){
                        Text(
                            modifier = Modifier.verticalScroll(state= scrollState),
                            text = initiative.descriptions[pagerState.currentPage] ,
                            style = MaterialTheme.typography.body1.copy(
                                color = Color.Black,
                                fontSize = integerResource(id = R.integer.initiative_detail_description).sp
                            )
                        )
                    }
                }
            }
        }
    }
}