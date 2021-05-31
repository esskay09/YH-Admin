package com.terranullius.yellowheart.data

data class Initiative(
    val id: String = "",
    var name: String,
    var descriptions: List<String>,
    var images: List<String>,
    val isPayable: Boolean = true,
    val order: Long?,
    val shareLinks: ShareLinks,
    val initialPage: Int = 0,
    val helpLink: String? = null,
    val helpDescription: String? = null
)
