package com.terranullius.yellowheart.payment

import android.app.Activity
import androidx.lifecycle.Lifecycle.Event.*
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.paykun.sdk.PaykunApiCall
import com.paykun.sdk.eventbus.GlobalBus
import com.terranullius.yellowheart.data.User
import org.json.JSONException
import org.json.JSONObject

object PaymentUtils {

    private lateinit var jsonObject: JSONObject

    fun registerListeners(
        context: Activity
    ) {
        (context as LifecycleOwner).lifecycle.addObserver(
            object : LifecycleObserver {

                @OnLifecycleEvent(ON_START)
                fun onStarted() {
                    GlobalBus.getBus().register(context)
                }

                @OnLifecycleEvent(ON_STOP)
                fun onStop() {
                    GlobalBus.getBus().unregister(context)
                }
            }
        )
    }

    fun initiatePayment(context: Activity, user: User, amount: Int) {
        createPaykunObject(user, amount)
        PaykunApiCall.Builder(context).sendJsonObject(jsonObject)
    }

    //TODO ADD PARAMS & SECURE ACCESS TOKEN
    private fun createPaykunObject(user: User, amount: Int) {
        jsonObject = JSONObject()
        try {
            jsonObject.put("merchant_id", "005181070659984")
            jsonObject.put("access_token", "5CCFD788E37A7582623662F1969F3E60")
            jsonObject.put("customer_name", user.name)
            jsonObject.put("customer_email", user.email)
            jsonObject.put("customer_phone", user.phone)
            jsonObject.put("product_name", "Heart")
            jsonObject.put(
                "order_no",
                System.currentTimeMillis()
            ) // order no. should have 10 to 30 character in numeric format
            jsonObject.put("amount", amount.toString()) // minimum amount should be 10
            jsonObject.put("isLive", false) // need to send false if you are in sandbox mode
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

}