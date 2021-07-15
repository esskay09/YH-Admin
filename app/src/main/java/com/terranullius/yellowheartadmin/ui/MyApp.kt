package com.terranullius.yellowheartadmin.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.terranullius.yellowheartadmin.data.Initiative
import com.terranullius.yellowheartadmin.data.ShareLinks
import com.terranullius.yellowheartadmin.other.Constants.RT_DETAIL
import com.terranullius.yellowheartadmin.other.Constants.RT_FEED
import com.terranullius.yellowheartadmin.other.Constants.RT_SPLASH
import com.terranullius.yellowheartadmin.utils.Result

@ExperimentalPagerApi
@Composable
fun MyApp(
    isSignedIn: Boolean,
    navController: NavHostController,
    initiatives: State<Result<List<Initiative>>>,
    onBottomBarClicked: (String) -> Unit,
    onShareDialogClicked: (link: String) -> Unit,
    onHelpClicked: (isPayable: Boolean, link: String?, amount: Int?) -> Unit
    ) {
    Surface(color = MaterialTheme.colors.primary) {

        val selectedInitiative = remember {
            mutableStateOf(
                Initiative(
                    name = "",
                    descriptions = emptyList(),
                    isPayable = true,
                    images = emptyList(),
                    order = 0,
                    shareLinks = ShareLinks("", "", "")
                )
            )
        }

        if (isSignedIn) {
            NavHost(navController = navController, startDestination = RT_SPLASH) {
                composable(RT_SPLASH) {
                    SplashScreen(
                        modifier = Modifier.fillMaxSize(),
                        navController = navController
                    )
                }
                composable(RT_FEED) {
                    Feed(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        onInitiativeClicked = {
                            selectedInitiative.value = it
                            navController.navigate(RT_DETAIL)
                        },
                        initiatives = initiatives.value
                    )
                }
                composable(RT_DETAIL) {
                    InitiativeDetail(
                        modifier = Modifier.fillMaxSize(),
                        initiative = selectedInitiative.value,
                        onBottomBarItemClicked = {
                            onBottomBarClicked(it)
                        },
                        onShareDialogClicked = onShareDialogClicked,
                        onHelpClicked = { link: String?, isPayable: Boolean, amount: Int? ->
                            onHelpClicked(
                                isPayable, link, amount
                            )
                        })

                }
            }
        }
    }
}
