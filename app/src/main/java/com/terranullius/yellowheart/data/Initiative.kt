package com.terranullius.yellowheart.data

data class Initiative(
    val id: String = "",
    var name: String,
    var description: String,
    var imgUrl: String,
    val isPayable: Boolean = true,
    val order: Long?
)
