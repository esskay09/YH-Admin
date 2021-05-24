package com.terrranulius.yellowheart.data

import androidx.annotation.DrawableRes

data class Initiative(
    var title: String,
    var description: String,
    @DrawableRes var imgRes: Int
)
