package com.terranullius.yhadmin.ui

import android.animation.Animator
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.airbnb.lottie.LottieAnimationView
import com.terranullius.yhadmin.data.AdminDto
import com.terranullius.yhadmin.other.Constants.RT_FEED
import com.terranullius.yhadmin.other.Constants.RT_SPLASH
import com.terranullius.yhadmin.ui.components.CircularProgress
import com.terranullius.yhadmin.ui.components.ErrorComposable
import com.terranullius.yhadmin.utils.Result
import com.terranullius.yhadmin.viewmodels.MainViewModel
import terranullius.yhadmin.R


@Composable
fun SplashScreen(modifier: Modifier = Modifier, navController: NavController, viewModel: MainViewModel) {

    Surface(color = MaterialTheme.colors.primary) {

        var isAdmin by remember {
            mutableStateOf(false)
        }

        val admins = viewModel.adminsFlow.collectAsState()

        when (admins.value) {
            is Result.Success -> {
                val admin = (admins.value as Result.Success<List<AdminDto>>).data.find {
                    it.email == getCurrentUserEmail()
                }
                Log.d("fuck", "adminList : ${admins.value} \n current: ${getCurrentUserEmail()}")
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

       if (isAdmin) {
           Surface(modifier = modifier, color = MaterialTheme.colors.secondaryVariant) {
               val context = LocalContext.current
               val customView = remember { LottieAnimationView(context) }

               AndroidView({ customView }, modifier = modifier) { view ->

                   customView.addAnimatorListener(object : Animator.AnimatorListener {
                       override fun onAnimationStart(animation: Animator?) {
                       }

                       override fun onAnimationEnd(animation: Animator?) {
                           navController.navigate(RT_FEED) {
                               this.popUpTo(RT_SPLASH) {
                                   inclusive = true
                               }
                           }
                       }

                       override fun onAnimationCancel(animation: Animator?) {

                       }

                       override fun onAnimationRepeat(animation: Animator?) {

                       }
                   })

                   with(view) {
                       setAnimation(R.raw.heart_splash)
                       repeatCount = 0
                       playAnimation()
                   }
               }
           }
       }

    }
}