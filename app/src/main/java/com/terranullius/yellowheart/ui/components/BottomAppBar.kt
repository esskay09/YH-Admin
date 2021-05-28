package com.terranullius.yellowheart.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import terranullius.yellowheart.R

@Composable
fun BottomBar(modifier: Modifier = Modifier) {
    //TODO REMOVE

       BottomAppBar(modifier = modifier) {
           BottomNavigation(modifier = Modifier.fillMaxWidth()) {
               BottomNavigationItem(
                   icon = {
                       Icon(painter = painterResource(id = R.drawable.ic_heart_filled), contentDescription = "")
                   },
                   label = { Text(text = "Download")},
                   selected = false,
//                   selectedItem.value == "download",
                   onClick = {
//                       result.value = "Download icon clicked"
//                       selectedItem.value = "download"
                   },
                   alwaysShowLabel = false
               )
           }
       }
}