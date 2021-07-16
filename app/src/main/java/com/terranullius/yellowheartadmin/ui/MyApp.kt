package com.terranullius.yellowheartadmin.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.terranullius.yellowheartadmin.data.Initiative
import com.terranullius.yellowheartadmin.other.Constants.RT_DETAIL
import com.terranullius.yellowheartadmin.other.Constants.RT_FEED
import com.terranullius.yellowheartadmin.other.Constants.RT_SPLASH
import com.terranullius.yellowheartadmin.utils.Result
import com.terranullius.yellowheartadmin.viewmodels.MainViewModel

@ExperimentalPagerApi
@Composable
fun MyApp(
    isSignedIn: Boolean,
    navController: NavHostController,
    initiatives: State<Result<List<Initiative>>>,
    onBottomBarClicked: (String) -> Unit,
    onShareDialogClicked: (link: String) -> Unit,
    onHelpClicked: (isPayable: Boolean, link: String?, amount: Int?) -> Unit,
    viewModel: MainViewModel
) {
    Surface(color = MaterialTheme.colors.primary) {

        var selectedInitiativeId by remember{
            mutableStateOf("")
        }

        val selectedInitiative = remember(initiatives, initiatives.value) {
            val initiative = if (initiatives.value is Result.Success){
                (initiatives.value as Result.Success<List<Initiative>>).data.find {
                    it.id == selectedInitiativeId
                } ?: Initiative()
            } else Initiative()
            mutableStateOf(initiative)
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
                            selectedInitiativeId = it.id
                            navController.navigate(RT_DETAIL)
                        },
                        initiatives = initiatives.value
                    )
                }
                composable(RT_DETAIL) {
                    InitiativeDetail(
                        modifier = Modifier.fillMaxSize(),
                        initiative = selectedInitiative,
                        viewModel = viewModel,
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
