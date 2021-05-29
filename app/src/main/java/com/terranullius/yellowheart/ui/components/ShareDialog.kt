package com.terranullius.yellowheart.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import terranullius.yellowheart.R

@Composable
fun ShareDialog(
    modifier: Modifier = Modifier
) {
    Surface(color = MaterialTheme.colors.secondaryVariant){
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ShareItem(iconRes = R.drawable.facebook, name = "FaceBook")
            ShareItem(iconRes = R.drawable.instagram, name = "Instagram")
            ShareItem(iconRes = R.drawable.twitter, name = "Twitter")
        }
    }
}

@Composable
fun ShareItem(
    @DrawableRes iconRes: Int,
    name: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .fillMaxWidth(0.9f)
        .height(30.dp), verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = "",
            modifier = Modifier.size(25.dp)
        )
        Divider(Modifier.width(8.dp))
        Text(text = name, )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ShareDialog(
        modifier = Modifier.fillMaxSize()
    )
}