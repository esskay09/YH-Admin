package com.terranullius.yellowheart.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.terranullius.yellowheart.other.Constants.AB_HELP
import com.terranullius.yellowheart.other.Constants.AB_JOIN
import com.terranullius.yellowheart.other.Constants.AB_SHARE
import terranullius.yellowheart.R

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    colorBg: Color = MaterialTheme.colors.secondary,
    onBottomBarItemClicked: (id: String) -> Unit
) {
    BottomAppBar(modifier = modifier, backgroundColor = colorBg, elevation = 20.dp) {
        BottomNavigation(modifier = Modifier.fillMaxWidth(), backgroundColor = colorBg) {
            BottomNavigationItem(
                icon = {
                    Icon(
                        Icons.Filled.ThumbUp,
                        contentDescription = ""
                    )
                },
                label = { Text(text = "Join us") },
                selected = false,
                onClick = {
                    onBottomBarItemClicked(AB_JOIN)
                },
                alwaysShowLabel = true
            )
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_heart_filled),
                        contentDescription = "Help"
                    )
                },
                label = { Text(text = "Help") },
                selected = false,
                onClick = {
                    onBottomBarItemClicked(AB_HELP)
                },
                alwaysShowLabel = true
            )
            BottomNavigationItem(
                icon = {
                    Icon(
                        Icons.Filled.Share,
                        contentDescription = ""
                    )
                },
                label = { Text(text = "Share") },
                selected = false,
                onClick = {
                    onBottomBarItemClicked(AB_SHARE)
                },
                alwaysShowLabel = true
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun Psreview() {
    BottomBar(modifier = Modifier.fillMaxWidth(), onBottomBarItemClicked = {

    })
}

