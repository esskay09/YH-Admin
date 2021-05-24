package com.terrranulius.yellowheart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.terrranulius.yellowheart.data.Initiative
import com.terrranulius.yellowheart.ui.theme.YellowHeartTheme
import com.terrranulius.yellowheart.ui.theme.secondaryColor

@Composable
fun InitiativeScreen(
    modifier: Modifier = Modifier,
    initiative: Initiative = Initiative(
        title = "Save Kriti",
        description = "Thanks for this great code!\n" +
                "however,am facing a slight problem . i have a dark background on my login and so if user enters no password while signing up, it shows a red color text saying \"this field cannot be empty\" the problem is the shade of red is too dark to be clearly visible, so i wanted to change it.I tried",
        imgRes = R.drawable.b,
    )
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.Start) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                shape = RoundedCornerShape(15.dp),
                elevation = 5.dp
            ) {
                Image(
                    painter = painterResource(id = initiative.imgRes),
                    contentScale = ContentScale.Crop,
                    contentDescription = ""
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = initiative.title,
                style = MaterialTheme.typography.h3
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = initiative.description,
                style = MaterialTheme.typography.body1
            )
        }
        HelpButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 12.dp), showText = false
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    YellowHeartTheme {
        Surface(color = MaterialTheme.colors.primaryVariant) {
            InitiativeScreen()
        }
    }
}