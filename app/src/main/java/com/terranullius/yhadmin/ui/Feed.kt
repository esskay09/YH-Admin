package com.terranullius.yhadmin.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.terranullius.yhadmin.data.Initiative
import com.terranullius.yhadmin.ui.components.CircularProgress
import com.terranullius.yhadmin.ui.components.FeedImageCard
import com.terranullius.yhadmin.utils.Result

@ExperimentalPagerApi
@Composable
fun Feed(
    modifier: Modifier = Modifier,
    onInitiativeClicked: (initiative: Initiative) -> Unit,
    initiatives: Result<List<Initiative>>
) {
    var isVideoPlaying = remember{
        mutableStateOf(false)
    }
    when (initiatives) {
        is Result.Success -> LazyColumn(
            modifier = modifier
        ) {
            itemsIndexed(initiatives.data) { pos: Int, initiative: Initiative ->
                Spacer(modifier = Modifier.height(8.dp))
                FeedImageCard(
                    initiative = initiative, modifier = Modifier.height(290.dp),
                    onInitiativeClicked = {
                        onInitiativeClicked(it)
                    },
                    isVideoPlaying = isVideoPlaying
                )
            }
        }
        is Result.Error -> TODO()
        is Result.Loading -> CircularProgress(modifier = Modifier.fillMaxSize())
    }
}