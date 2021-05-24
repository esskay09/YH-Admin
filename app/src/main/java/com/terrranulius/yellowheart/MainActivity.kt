package com.terrranulius.yellowheart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.terrranulius.yellowheart.data.Initiative
import com.terrranulius.yellowheart.ui.theme.YellowHeartTheme
import androidx.compose.material.ButtonDefaults.buttonColors
import com.terrranulius.yellowheart.firebase.FirebaseAuthUtils
import com.terrranulius.yellowheart.ui.theme.secondaryColor


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseAuthUtils.registerListeners(this)

        setContent {
            YellowHeartTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.primaryVariant) {
                    InitiativesMain()
                }
            }
        }
    }
}

@Composable
fun ImageCard(
    initiative: Initiative,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun InitiativesMain() {
    val initiatives = listOf(
        Initiative(
            title = "Help India read",
            description = "Education is a human right. Help us make it avaialable to everyone",
            imgRes = R.drawable.image1
        ),
        Initiative(
            title = "Save Kriti",
            description = "Going Through Cancer and Shit",
            imgRes = R.drawable.a
        ),
        Initiative(
            title = "Save Kriti",
            description = "Going Through Cancer and Shit",
            imgRes = R.drawable.b
        ),
        Initiative(
            title = "Save Kriti",
            description = "Going Through Cancer and Shit",
            imgRes = R.drawable.c
        ),
        Initiative(
            title = "Save Kriti",
            description = "Going Through Cancer and Shit",
            imgRes = R.drawable.d
        ),

        )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
    ) {
        itemsIndexed(initiatives) { pos: Int, initiative: Initiative ->
            Spacer(modifier = Modifier.height(8.dp))
            ImageCard(initiative = initiative)
        }
    }
}

@Composable
fun HelpButton(modifier: Modifier = Modifier) {
    Button(
        onClick = { /*TODO*/ }, modifier = modifier,
        colors = buttonColors(
            backgroundColor = secondaryColor
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Help", modifier = Modifier.padding(end = 2.dp))
            Icon(
                modifier = Modifier.padding(start = 2.dp),
                painter = painterResource(id = R.drawable.ic_heart_filled),
                contentDescription = "",
                tint = MaterialTheme.colors.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    YellowHeartTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.primaryVariant)
        {
            InitiativesMain()
        }
    }
}