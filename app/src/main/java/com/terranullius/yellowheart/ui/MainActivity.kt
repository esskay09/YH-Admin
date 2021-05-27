package com.terranullius.yellowheart.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.ColorRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.paykun.sdk.eventbus.Events
import com.paykun.sdk.helper.PaykunHelper
import com.terranullius.yellowheart.data.Initiative
import com.terranullius.yellowheart.data.toInitiative
import com.terranullius.yellowheart.firebase.FirebaseAuthUtils
import com.terranullius.yellowheart.firebase.FirestoreUtils
import com.terranullius.yellowheart.other.Constants.RT_DETAIL
import com.terranullius.yellowheart.other.Constants.RT_FEED
import com.terranullius.yellowheart.other.Constants.RT_SPLASH
import com.terranullius.yellowheart.payment.PaymentUtils
import com.terranullius.yellowheart.ui.components.*
import com.terranullius.yellowheart.ui.theme.YellowHeartTheme
import com.terranullius.yellowheart.utils.Result
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import terranullius.yellowheart.R


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO CREATE RESOURCE WRAPPER
        //TODO SIDE EFFECT USE ACCOMPANIST

        //TODO REMOVE
        var dummydata: Result<> = Result.Loading
        dummydata = Result.Success()

        lifecycleScope.launchWhenCreated {
            dummydata = FirestoreUtils.getInitiatives().map {
                it.toInitiative()
            }
        }

        FirebaseAuthUtils.registerListeners(this)
        PaymentUtils.registerListeners(this)

        setContent {
            val navController = rememberNavController()
            val context = LocalContext.current


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

            YellowHeartTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.primaryVariant) {

                    val selectedInitiative = remember {
                        mutableStateOf(
                            Initiative(
                                name = "",
                                description = "",
                                isPayable = true,
                                imgUrl = ""
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
                                navController = navController, onHelpClick = { onHelpClick() },
                                onChildClicked = {
                                    selectedInitiative.value = it
                                },
                                initiatives = dummydata)
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
        val currentUser = FirebaseAuthUtils.getUser()
        PaymentUtils.initiatePayment(this@MainActivity, currentUser)
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
