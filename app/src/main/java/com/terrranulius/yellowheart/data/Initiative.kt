package com.terrranulius.yellowheart.data

import androidx.annotation.DrawableRes
import java.util.*

data class Initiative(
    val id: String = UUID.randomUUID().toString(),
    var title: String,
    var description: String,
    @DrawableRes var imgRes: Int
)
