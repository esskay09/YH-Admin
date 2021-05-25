package com.terrranulius.yellowheart.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.terrranulius.yellowheart.Constants
import com.terrranulius.yellowheart.R
import com.terrranulius.yellowheart.data.Initiative

@Composable
fun InitiativeDetail(
    initiativeId: String?,
    modifier: Modifier = Modifier,
    onHelpClick: () -> Unit
) {
    val initiative = Constants.initiatives.find {
        it.id == initiativeId
    } ?: Initiative(
        title = "error",
        description = "error",
        imgRes = R.drawable.b
    )
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
                .padding(bottom = 12.dp), showText = false,
            onClick = onHelpClick
        )
    }
}