package com.terranullius.yellowheart.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.terranullius.yellowheart.data.Initiative
import com.terranullius.yellowheart.other.Constants.RT_DETAIL
import com.terranullius.yellowheart.utils.Result

@Composable
fun Feed(
    navController: NavController,
    onHelpClick: () -> Unit,
    onChildClicked: (initiative: Initiative) -> Unit,
    initiatives: Result<List<Initiative>>
) {
    when (initiatives) {
        is Result.Success -> LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
        ) {
            itemsIndexed(initiatives.data) { pos: Int, initiative: Initiative ->
                Spacer(modifier = Modifier.height(8.dp))
                FeedImageCard(
                    initiative = initiative, modifier = Modifier.clickable {
                        onChildClicked(initiative)
                        navController.navigate(RT_DETAIL)
                    },
                    onHelpClick = onHelpClick
                )
            }
        }
        is Result.Error -> TODO()
        Result.Loading -> CircularProgress()
    }
}