package com.terranullius.yellowheart.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.terranullius.yellowheart.data.Initiative

@ExperimentalPagerApi
@Composable
fun InitiativeDetail(
    initiative: Initiative,
    modifier: Modifier = Modifier,
    onBottomBarItemClicked: (String) -> Unit
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(modifier = modifier, scaffoldState = scaffoldState,
        bottomBar = {
            BottomBar(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(15.dp)
                    ),
                onBottomBarItemClicked = {
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