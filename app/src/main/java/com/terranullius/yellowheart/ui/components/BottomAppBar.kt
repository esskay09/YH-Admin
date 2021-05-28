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
import terranullius.yellowheart.R

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    colorBg: Color = MaterialTheme.colors.secondary,
    onBottomBarItemClicked: (id: String) -> Unit
) {
    BottomAppBar(modifier = modifier, backgroundColor = colorBg) {
        BottomNavigation(modifier = Modifier.fillMaxWidth(), backgroundColor = colorBg) {
            BottomNavigationItem(
                icon = {
                    Icon(
                        Icons.Filled.ThumbUp,
                        contentDescription = ""
                    )
                },
                label = { Text(text = "Download") },
                selected = false,
                onClick = {

                },
                alwaysShowLabel = false
            )
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_heart_filled),
                        contentDescription = ""
                    )
                },
                label = { Text(text = "Download") },
                selected = false,
                onClick = {

                },
                alwaysShowLabel = false
            )
            BottomNavigationItem(
                icon = {
                    Icon(
                        Icons.Filled.Share,
                        contentDescription = ""
                    )
                },
                label = { Text(text = "Download") },
                selected = false,
                onClick = {

                },
                alwaysShowLabel = false
            )
        }
    }
}

