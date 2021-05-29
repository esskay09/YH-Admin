package com.terranullius.yellowheart.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.rememberCoilPainter
import com.terranullius.yellowheart.data.Initiative

@Composable
fun InitiativeDetail(
    initiative: Initiative,
    modifier: Modifier = Modifier,
    onBottomBarItemClicked: (String) -> Unit
) {
    val scaffoldState = rememberScaffoldState()

    val currentColor = remember { mutableStateOf(0xffe9bf34) }
    val bgColor = Color(currentColor.value)

    val animatedColor = animateColorAsState(targetValue = bgColor)

    val colorList = remember {
        listOf(
            0xffe9bf34,
            0xffeac338
        )
    }
    Scaffold(modifier = modifier, scaffoldState = scaffoldState) {
        Surface(color = animatedColor.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.Start) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(270.dp),
                        shape = RoundedCornerShape(15.dp),
                        elevation = 5.dp
                    ) {
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            painter = rememberCoilPainter(initiative.images[0]),
                            contentScale = ContentScale.Crop,
                            contentDescription = ""
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = initiative.name,
                        style = MaterialTheme.typography.h4.copy(
                            fontFamily = FontFamily.Cursive,
                            color = Color.Black
                        )
                    )
                    Spacer(Modifier.height(48.dp))
                    Text(
                        text = initiative.description,

                        style = MaterialTheme.typography.body1.copy(
//                    fontFamily = FontFamily
                            color = Color.Black
                        )
                    )
                    Text(
                        text = currentColor.value.toString(),
                        modifier = Modifier.align(Alignment.End)
                    )
                }

                BottomBar(
                    modifier = Modifier.align(Alignment.BottomStart),
                    onBottomBarItemClicked = {
                        onBottomBarItemClicked(it)
                    }
                )
            }
        }
    }
}