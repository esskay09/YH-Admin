package com.terrranulius.yellowheart.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.ColorRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.terrranulius.yellowheart.data.DummyData
import com.terrranulius.yellowheart.other.Constants.RT_DETAIL
import com.terrranulius.yellowheart.other.Constants.RT_FEED
import com.terrranulius.yellowheart.other.Constants.RT_SPLASH
import com.terrranulius.yellowheart.ui.components.*
import com.terrranulius.yellowheart.firebase.FirebaseAuthUtils
import com.terrranulius.yellowheart.ui.theme.YellowHeartTheme
import com.terrranulius.yellowheart.R


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseAuthUtils.registerListeners(this)


        setContent {
            val navController = rememberNavController()
            val context = LocalContext.current

            //TODO SIDE EFFECT
            navController.addOnDestinationChangedListener { navController: NavController, navDestination: NavDestination, bundle: Bundle? ->

                var window: Window? = null

                if (context is Activity) {
                    window = context.window
                    // clear FLAG_TRANSLUCENT_STATUS flag:
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                    // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    // finally change the color
                    // finally change the color
                    setStatusBarColor(R.color.secondaryColor, context)

                }

                if (window != null) {
                    when (navDestination.route) {
                        RT_SPLASH -> setStatusBarColor(R.color.secondaryColor, context)
                        else -> setStatusBarColor(R.color.primaryLightColor, context)
                    }
                }
            }

            YellowHeartTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.primaryVariant) {

                    val selectedInitiative = remember {
                        mutableStateOf(
                            DummyData.initiatives[2]
                        )
                    }

                    NavHost(navController = navController, startDestination = RT_SPLASH) {
                        composable(RT_SPLASH) {
                            SplashScreen(
                                modifier = Modifier.fillMaxSize(),
                                navController = navController
                            )
                        }
                        composable(RT_FEED) {
                            Feed(navController = navController, onHelpClick = { onHelpClick() },
                                onChildClicked = {
                                    selectedInitiative.value = it
                                })
                        }
                        composable(RT_DETAIL) {
                            InitiativeDetail(
                                initiative = selectedInitiative.value,
                                onHelpClick = { onHelpClick() })
                        }
                    }
                }
            }
        }
    }

    private fun onHelpClick() {
        //TODO
    }

    private fun setStatusBarColor(@ColorRes colorRes: Int, context: Context) {
        window.statusBarColor = ContextCompat.getColor(
            context, colorRes
        )
    }

}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    YellowHeartTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.primaryVariant)
        {
            Feed(rememberNavController(), onHelpClick = {}, onChildClicked = {})
        }
    }
}
