package com.terranullius.yellowheart.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.paykun.sdk.eventbus.Events
import com.paykun.sdk.helper.PaykunHelper
import com.terranullius.yellowheart.data.Initiative
import com.terranullius.yellowheart.firebase.FirebaseAuthUtils
import com.terranullius.yellowheart.other.Constants.AB_HELP
import com.terranullius.yellowheart.other.Constants.AB_JOIN
import com.terranullius.yellowheart.other.Constants.AB_SHARE
import com.terranullius.yellowheart.other.Constants.JOIN_LINK
import com.terranullius.yellowheart.other.Constants.RT_DETAIL
import com.terranullius.yellowheart.other.Constants.RT_FEED
import com.terranullius.yellowheart.other.Constants.RT_SPLASH
import com.terranullius.yellowheart.payment.PaymentUtils
import com.terranullius.yellowheart.ui.components.*
import com.terranullius.yellowheart.ui.theme.YellowHeartTheme
import com.terranullius.yellowheart.viewmodels.MainViewModel
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import terranullius.yellowheart.R


class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO SIDE EFFECT USE ACCOMPANIST

        FirebaseAuthUtils.registerListeners(this)
        PaymentUtils.registerListeners(this)

        setContent {
            val navController = rememberNavController()
            val context = LocalContext.current
            val isSignedIn = viewModel.isSignedInFlow.collectAsState()
            val initiativesFlow = viewModel.initiativesFlow.collectAsState()
            val scaffoldState = rememberScaffoldState()

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
                        RT_SPLASH -> setStatusBarColor(R.color.secondaryLightColor, context)
                        else -> setStatusBarColor(R.color.primaryLightColor, context)
                    }
                }
            }

            if (isSignedIn.value) YellowHeartTheme {

                ViewPagerImages(modifier = Modifier.fillMaxWidth().height(200.dp))

                /*Surface(color = MaterialTheme.colors.primaryVariant) {
                    val selectedInitiative = remember {
                        mutableStateOf(
                            Initiative(
                                name = "",
                                description = "",
                                isPayable = true,
                                imgUrl = "",
                                order = 0
                            )
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
                            Feed(
                                navController = navController,
                                onHelpClick = {
                                },
                                onInitiativeClicked = {
                                    selectedInitiative.value = it
                                },
                                initiatives = initiativesFlow.value
                            )
                        }
                        composable(RT_DETAIL) {
                            InitiativeDetail(
                                initiative = selectedInitiative.value,
                                onBottomBarItemClicked = {
                                    onBottomBarClicked(it)
                                })
                        }
                    }
                }*/
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (FirebaseAuthUtils.isSignedIn()) {
            viewModel.onSignedIn()
        }
    }

    private fun onBottomBarClicked(id: String) {
        when (id) {
            AB_HELP -> {
                val currentUser = FirebaseAuthUtils.getUser()
                PaymentUtils.initiatePayment(this@MainActivity, currentUser)
            }
            AB_SHARE -> {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("https://www.instagram.com/p/CPYAiboD8fb/")
                }
                startActivity(intent)
            }
            AB_JOIN -> {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(JOIN_LINK)
                }
                startActivity(intent)
            }
        }
    }

    private fun setStatusBarColor(@ColorRes colorRes: Int, context: Context) {
        window.statusBarColor = ContextCompat.getColor(
            context, colorRes
        )
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun getResults(message: Events.PaymentMessage) {
        if (message.results == PaykunHelper.MESSAGE_SUCCESS) {
            // do your stuff here
            // message.getTransactionId() will return your failed or succeed transaction id
            /* if you want to get your transaction detail call message.getTransactionDetail()
        *  getTransactionDetail return all the field from server and you can use it here as per your need
        *  For Example you want to get Order id from detail use message.getTransactionDetail().order.orderId */
            if (!TextUtils.isEmpty(message.transactionId)) {
                Toast.makeText(
                    this,
                    "Your Transaction is succeed with transaction id : " + message.transactionId,
                    Toast.LENGTH_SHORT
                ).show()
                Log.v(
                    " order id ",
                    " getting order id value : " + message.transactionDetail.order.orderId
                )
            }
        } else if (message.results == PaykunHelper.MESSAGE_FAILED) {
            // do your stuff here
            Toast.makeText(this, "Your Transaction is failed", Toast.LENGTH_SHORT)
                .show()
        } else if (message.results == PaykunHelper.MESSAGE_SERVER_ISSUE) {
            // do your stuff here
            Toast.makeText(this, PaykunHelper.MESSAGE_SERVER_ISSUE, Toast.LENGTH_SHORT)
                .show()
        } else if (message.results == PaykunHelper.MESSAGE_ACCESS_TOKEN_MISSING
        ) {
            // do your stuff here
            Toast.makeText(this, "Access Token missing", Toast.LENGTH_SHORT).show()
        } else if (message.results == PaykunHelper.MESSAGE_MERCHANT_ID_MISSING
        ) {
            // do your stuff here
            Toast.makeText(this, "Merchant Id is missing", Toast.LENGTH_SHORT).show()
        } else if (message.results == PaykunHelper.MESSAGE_INVALID_REQUEST) {
            Toast.makeText(this, "Invalid Request", Toast.LENGTH_SHORT).show()
        } else if (message.results == PaykunHelper.MESSAGE_NETWORK_NOT_AVAILABLE
        ) {
            Toast.makeText(this, "Network is not available", Toast.LENGTH_SHORT).show()
        }
    }
}
