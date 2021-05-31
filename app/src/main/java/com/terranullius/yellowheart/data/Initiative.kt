package com.terranullius.yellowheart.data

data class Initiative(
    val id: String = "",
    var name: String,
    var description: String,
    var images: List<String>,
    val isPayable: Boolean = true,
    val order: Long?,
    val shareLinks: ShareLinks,
    val initialPage: Int = 0
)
