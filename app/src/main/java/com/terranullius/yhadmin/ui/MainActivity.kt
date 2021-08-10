package com.terranullius.yhadmin.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.paykun.sdk.eventbus.Events
import com.paykun.sdk.helper.PaykunHelper
import com.terranullius.yhadmin.firebase.FirebaseAuthUtils

import com.terranullius.yhadmin.other.Constants.AB_HELP
import com.terranullius.yhadmin.other.Constants.AB_JOIN
import com.terranullius.yhadmin.other.Constants.AB_SHARE
import com.terranullius.yhadmin.other.Constants.JOIN_LINK
import com.terranullius.yhadmin.other.Constants.RT_SPLASH
import com.terranullius.yhadmin.payment.PaymentUtils
import com.terranullius.yhadmin.ui.theme.YellowHeartTheme
import com.terranullius.yhadmin.viewmodels.MainViewModel
import kotlinx.coroutines.flow.collect
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import terranullius.yhadmin.R


//TODO REFRACTOR LENGTHY CALLBACKS INTO VIEWMODEL

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel>()

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Array<String>>

    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseAuthUtils.registerListeners(this)
        PaymentUtils.registerListeners(this)

        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()){
            if (it!=null) viewModel.onGetImage(it)
        }

        lifecycleScope.launchWhenCreated{
            viewModel.pickImage.collect {
                it?.let {
                    if (it.isUpdating){
                        imagePickerLauncher.launch(arrayOf("image/*"))
                    }
                }
            }
        }

        setContent {
            val navController = rememberNavController()
            val context = LocalContext.current
            val isSignedIn = viewModel.isSignedInFlow.collectAsState()
            val initiatives = viewModel.initiativesFlow.collectAsState()

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
                        else -> setStatusBarColor(R.color.primaryColor, context)
                    }
                }
            }

            YellowHeartTheme {
                MyApp(
                    isSignedIn = isSignedIn.value,
                    navController = navController,
                    initiatives = initiatives,
                    viewModel = viewModel,
                    onBottomBarClicked = {
                        onBottomBarClicked(it)
                    },
                    onShareDialogClicked = { link ->
                        onShareDialogClicked(link)
                    },
                    onHelpClicked = { isPayable: Boolean, link: String?, amount: Int? ->
                        onHelpDialogCLicked(isPayable, link, amount)
                    })
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
            }
            AB_SHARE -> {
            }
            AB_JOIN -> {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(JOIN_LINK)
                }
                startActivity(intent)
            }
        }
    }

    private fun onHelpDialogCLicked(isPayable: Boolean, link: String?, amount: Int?) {
        if (isPayable && amount != null) {
            initiatePayment(amount)
        } else if (!isPayable && link != null) {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(link)
            }
            startActivity(intent)
        }
    }

    private fun initiatePayment(amount: Int) {
        val currentUser = FirebaseAuthUtils.getUser()
        PaymentUtils.initiatePayment(this@MainActivity, currentUser, amount)
    }

    private fun onShareDialogClicked(link: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(link)
        }
        try {
            startActivity(intent)
        } catch (e: Exception){
            Toast.makeText(this, "No app found to share", Toast.LENGTH_SHORT).show()
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
