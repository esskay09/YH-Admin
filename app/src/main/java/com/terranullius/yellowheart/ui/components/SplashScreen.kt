package com.terranullius.yellowheart.ui.components

import android.animation.Animator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.airbnb.lottie.LottieAnimationView
import com.terranullius.yellowheart.other.Constants.RT_FEED
import terranullius.yellowheart.R


@Composable
fun SplashScreen(modifier: Modifier = Modifier, navController: NavController) {

        Surface(modifier = modifier, color = MaterialTheme.colors.secondary) {
            val visibility = remember { mutableStateOf(0) }
            val context = LocalContext.current
            val customView = remember { LottieAnimationView(context) }

            //TODO SIDE EFFECT
            AndroidView({ customView }, modifier = modifier) { view ->

                customView.addAnimatorListener(object: Animator.AnimatorListener{
                    override fun onAnimationStart(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        navController.navigate(RT_FEED)
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