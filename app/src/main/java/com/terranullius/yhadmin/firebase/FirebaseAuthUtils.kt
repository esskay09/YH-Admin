package com.terranullius.yhadmin.firebase

import android.app.Activity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.terranullius.yhadmin.data.User
import com.terranullius.yhadmin.other.Constants.RC_SIGN_IN
import terranullius.yhadmin.R
import java.lang.IllegalArgumentException

object FirebaseAuthUtils {

    private lateinit var auth: FirebaseAuth

    fun registerListeners(context: Activity) {

        auth = FirebaseAuth.getInstance()

        if (context is LifecycleOwner) {
            context.lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_START)
                fun onStart() {
                    onSignIn(context)
                }
            })
        }
    }

    fun signOut(context: Activity) {
        AuthUI.getInstance()
            .signOut(context)
            .addOnCompleteListener {
                onSignIn(context)
            }
    }

    fun isSignedIn() = auth.currentUser != null

    fun getUser() = if (isSignedIn()) User(
        name = auth.currentUser?.displayName ?: "Enter Name",
        email = auth.currentUser?.email ?: "Enter Email",
        phone = auth.currentUser?.phoneNumber ?: "Enter Phone Number"
    ) else throw IllegalArgumentException()

    fun onSignIn(context: Activity) {
        if (!isSignedIn()) {
            context.startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setTheme(R.style.LoginTheme)
                    .setLogo(R.drawable.ic_heart_filled_firebaselogo)
                    .build(),
                RC_SIGN_IN
            )
        }
    }

    private val providers = arrayListOf(
        AuthUI.IdpConfig.GoogleBuilder().build(),
        AuthUI.IdpConfig.FacebookBuilder().build()
    )
}