package com.terranullius.yellowheart.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.terranullius.yellowheart.data.Initiative
import com.terranullius.yellowheart.other.Constants
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
    val isShareClicked = remember{
        mutableStateOf(false)
    }

    if (isShareClicked.value){
        Dialog(onDismissRequest = {
            isShareClicked.value = false
        }) {
            ShareDialog(modifier = Modifier.fillMaxWidth(), onShareClicked = {
                when(it){
                   Constants.DIALOG_INSTA -> onShareDialogClicked(initiative.shareLinks.insta)
                    Constants.DIALOG_FB -> onShareDialogClicked(initiative.shareLinks.fb)
                    Constants.DIALOG_TWITTER -> onShareDialogClicked(initiative.shareLinks.twitter)
                }
            }) }
        }

    Scaffold(modifier = modifier, scaffoldState = scaffoldState,
        bottomBar = {
            BottomBar(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(15.dp)
                    ),
                onBottomBarItemClicked = {
                    if (it == AB_SHARE){
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
                            images = initiative.images
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