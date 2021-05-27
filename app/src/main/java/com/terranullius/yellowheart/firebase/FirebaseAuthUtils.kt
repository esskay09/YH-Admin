package com.terranullius.yellowheart.firebase

import android.app.Activity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.terranullius.yellowheart.data.User
import com.terranullius.yellowheart.other.Constants.RC_SIGN_IN
import terranullius.yellowheart.R
import java.lang.IllegalArgumentException

object FirebaseAuthUtils {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun registerListeners(context: Activity) {

        auth.addAuthStateListener {
            if (!isSignedIn()) onSignedOut(context)
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

    fun getUser() = if (isSignedIn()) User(
        name = auth.currentUser?.displayName ?: "Enter Name",
        email = auth.currentUser?.email ?: "Enter Email",
        phone = auth.currentUser?.phoneNumber ?: "Enter Phone Number"
    ) else throw IllegalArgumentException()

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