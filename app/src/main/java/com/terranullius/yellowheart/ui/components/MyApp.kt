package com.terranullius.yellowheart.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.terranullius.yellowheart.data.Initiative
import com.terranullius.yellowheart.data.ShareLinks
import com.terranullius.yellowheart.other.Constants
import com.terranullius.yellowheart.utils.Result

@ExperimentalPagerApi
@Composable
fun MyApp(
    isSignedIn: Boolean,
    navController: NavHostController,
    initiatives: State<Result<List<Initiative>>>,
    onBottomBarClicked: (String) -> Unit,
    onShareDialogClicked: (link: String) -> Unit,
) {
        Surface(color = MaterialTheme.colors.primary) {

        val selectedInitiative = remember {
            mutableStateOf(
                Initiative(
                    name = "",
                    description = "",
                    isPayable = true,
                    images = emptyList(),
                    order = 0,
                    shareLinks = ShareLinks("","","")
                )
            )
        }

        if (isSignedIn){
            NavHost(navController = navController, startDestination = Constants.RT_SPLASH) {
                composable(Constants.RT_SPLASH) {
                    SplashScreen(
                        modifier = Modifier.fillMaxSize(),
                        navController = navController
                    )
                }
                composable(Constants.RT_FEED) {
                    Feed(
                        navController = navController,
                        onHelpClick = {
                        },
                        onInitiativeClicked = {
                            selectedInitiative.value = it
                        },
                        initiatives = initiatives.value
                    )
                }
                composable(Constants.RT_DETAIL) {
                    InitiativeDetail(
                        initiative = selectedInitiative.value,
                        onBottomBarItemClicked = {
                            onBottomBarClicked(it)
                        },
                    onShareDialogClicked = onShareDialogClicked)
                }
            }
        }
    }
    }
