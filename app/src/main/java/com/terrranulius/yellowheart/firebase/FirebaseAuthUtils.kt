package com.terrranulius.yellowheart.firebase

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.terrranulius.yellowheart.Constants.RC_SIGN_IN
import com.terrranulius.yellowheart.R

object FirebaseAuthUtils {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun registerListeners(context: Activity) {

        auth.addAuthStateListener {
            if (!isSignedIn()) onSignedOut(context)
        }

        registerLifecycleOwners(context)
    }

    private fun registerLifecycleOwners(context: Activity) {
        val lifecycleObserver = object : LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun onResumed() {
                if (!isSignedIn()) onSignedOut(context)
            }
        }

        if (context is ComponentActivity) {
            context.lifecycle.addObserver(
                lifecycleObserver
            )
        }
    }

    fun signOut(context: Activity) {
        AuthUI.getInstance()
            .signOut(context)
            .addOnCompleteListener {
                onSignedOut(context)
            }
    }

    fun isSignedIn() = auth.currentUser != null


    private fun onSignedOut(context: Activity) {
        context.startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.LoginTheme)
                .build(),
            RC_SIGN_IN
        )
    }

    private val providers = arrayListOf(
        AuthUI.IdpConfig.GoogleBuilder().build(),
        AuthUI.IdpConfig.FacebookBuilder().build()
    )
}