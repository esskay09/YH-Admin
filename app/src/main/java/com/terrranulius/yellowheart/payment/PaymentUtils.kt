package com.terrranulius.yellowheart.payment

import android.app.Activity
import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.Lifecycle.Event.ON_STOP
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.paykun.sdk.PaykunApiCall
import com.paykun.sdk.eventbus.GlobalBus
import org.json.JSONException
import org.json.JSONObject

object PaymentUtils {

    private lateinit var jsonObject: JSONObject

    fun registerPaymentListeners(
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

    fun initiatePayment(context: Activity) {
        createPaykunObject()
        PaykunApiCall.Builder(context).sendJsonObject(jsonObject)
    }

    //TODO ADD PARAMS
    fun createPaykunObject() {
        jsonObject = JSONObject()
        try {
            jsonObject.put("merchant_id", "005181070659984")
            jsonObject.put("access_token", "0A156FBC050CAAA38C2723A8192C8B93")
            jsonObject.put("customer_name", "Shaique Khan")
            jsonObject.put("customer_email", "esskay099@gmail.com")
            jsonObject.put("customer_phone", "9334805466")
            jsonObject.put("product_name", "Heart")
            jsonObject.put(
                "order_no",
                System.currentTimeMillis()
            ) // order no. should have 10 to 30 character in numeric format
            jsonObject.put("amount", "10") // minimum amount should be 10
            jsonObject.put("isLive", false) // need to send false if you are in sandbox mode
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

}