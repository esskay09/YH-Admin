package com.terranullius.yhadmin.ui

import android.animation.Animator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.airbnb.lottie.LottieAnimationView
import com.terranullius.yhadmin.other.Constants.RT_FEED
import com.terranullius.yhadmin.other.Constants.RT_SPLASH
import terranullius.yhadmin.R


@Composable
fun SplashScreen(modifier: Modifier = Modifier, navController: NavController) {

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