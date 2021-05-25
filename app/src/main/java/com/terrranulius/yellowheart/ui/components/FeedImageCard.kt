package com.terrranulius.yellowheart.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.terrranulius.yellowheart.data.Initiative

@Composable
fun FeedImageCard(
    initiative: Initiative,
    modifier: Modifier = Modifier,
    onHelpClick: ()-> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        elevation = 5.dp
    ) {
        Box(modifier = Modifier.height(240.dp)) {
            Image(
                painter = painterResource(id = initiative.imgRes),
                contentDescription = initiative.description,
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black
                            ),
                            startY = 300f
                        )
                    )
            )
            Text(
                text = initiative.title, modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp),
                style = TextStyle(color = Color.White)
            )
            HelpButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp),
                onClick = onHelpClick
            )
        }
    }
}