package com.terranullius.yellowheart.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.terranullius.yellowheart.data.Initiative
import com.terranullius.yellowheart.other.Constants.AB_SHARE
import com.terranullius.yellowheart.other.Constants.DIALOG_FB
import com.terranullius.yellowheart.other.Constants.DIALOG_INSTA
import com.terranullius.yellowheart.other.Constants.DIALOG_TWITTER

@ExperimentalPagerApi
@Composable
fun InitiativeDetail(
    initiative: Initiative,
    modifier: Modifier = Modifier,
    onBottomBarItemClicked: (String) -> Unit,
    onShareDialogClicked: (link: String) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val isShareClicked = remember {
        mutableStateOf(false)
    }
    val pagerState = rememberPagerState(
        initialPage = initiative.initialPage,
        pageCount = initiative.images.size,
        initialOffscreenLimit = 2
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
            })
        }
    }

    Scaffold(modifier = modifier, scaffoldState = scaffoldState,
        bottomBar = {
            BottomBar(
                onBottomBarItemClicked = {
                    if (it == AB_SHARE) {
                        isShareClicked.value = true
                    }
                    onBottomBarItemClicked(it)
                }
            )
        }) {

        Surface(color = MaterialTheme.colors.primary) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(270.dp),
                        shape = RoundedCornerShape(15.dp),
                        elevation = 12.dp
                    ) {
                        ViewPagerImages(
                            modifier = Modifier.fillMaxSize(),
                            images = initiative.images,
                            pagerState = pagerState
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = initiative.name,
                        style = MaterialTheme.typography.h4.copy(
                            fontFamily = FontFamily.Cursive,
                            color = Color.Black
                        )
                    )
                    Spacer(Modifier.height(14.dp))
                    Text(
                        text = initiative.description,

                        style = MaterialTheme.typography.body1.copy(
//                    fontFamily = FontFamily
                            color = Color.Black
                        )
                    )
                }
            }
        }
    }
}