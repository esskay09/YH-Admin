package com.terranullius.yhadmin.utils

import android.content.Intent
import android.widget.Toast
import com.terranullius.yhadmin.ui.MainActivity

fun MainActivity.startSafeActivity(intent: Intent){
    try {
        startActivity(intent)
    } catch (e: Exception){
        Toast.makeText(this, "No app found", Toast.LENGTH_SHORT).show()
    }
}