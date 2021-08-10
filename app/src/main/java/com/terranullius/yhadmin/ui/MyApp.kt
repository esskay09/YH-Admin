package com.terranullius.yhadmin.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.terranullius.yhadmin.data.AdminDto
import com.terranullius.yhadmin.data.Initiative
import com.terranullius.yhadmin.firebase.FirebaseAuthUtils
import com.terranullius.yhadmin.other.Constants.RT_DETAIL
import com.terranullius.yhadmin.other.Constants.RT_FEED
import com.terranullius.yhadmin.other.Constants.RT_SPLASH
import com.terranullius.yhadmin.ui.components.CircularProgress
import com.terranullius.yhadmin.ui.components.ErrorComposable
import com.terranullius.yhadmin.utils.Result
import com.terranullius.yhadmin.viewmodels.MainViewModel

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

        var isAdmin by remember {
            mutableStateOf(false)
        }

        var selectedInitiativeId by remember {
            mutableStateOf("")
        }

        val selectedInitiative = remember(initiatives, initiatives.value) {
            val initiative = if (initiatives.value is Result.Success) {
                (initiatives.value as Result.Success<List<Initiative>>).data.find {
                    it.id == selectedInitiativeId
                } ?: Initiative()
            } else Initiative()
            mutableStateOf(initiative)
        }

        val admins = viewModel.adminsFlow.collectAsState()

        when (admins.value) {
            is Result.Success -> {
                val admin = (admins.value as Result.Success<List<AdminDto>>).data.find {
                    it.email == getCurrentUserEmail()
                }
                if (admin != null) {
                    isAdmin = true
                } else {
                    Box(Modifier.fillMaxSize()) {
                        ErrorComposable(
                            modifier = Modifier.align(Alignment.Center),
                            errorMsg = "Oops! You aren't an admin"
                        )
                    }
                }
            }
            is Result.Error -> {
                Box(Modifier.fillMaxSize()) {
                    ErrorComposable(
                        modifier = Modifier.align(Alignment.Center),
                        errorMsg = "Oops! You aren't an admin"
                    )
                }
            }
            is Result.Loading -> CircularProgress(modifier = Modifier.fillMaxSize())
        }

        if (isSignedIn && isAdmin) {
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

fun getCurrentUserEmail() = FirebaseAuthUtils.getUser().email

