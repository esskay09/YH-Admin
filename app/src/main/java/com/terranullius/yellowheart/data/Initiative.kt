package com.terranullius.yellowheart.data

import androidx.annotation.DrawableRes
import java.util.*

data class Initiative(
    val id: String = "",
    var name: String,
    var description: String,
    var imgUrl: String,
    val isPayable: Boolean = true
)
