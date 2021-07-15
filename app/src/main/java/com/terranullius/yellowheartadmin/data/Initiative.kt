package com.terranullius.yellowheartadmin.data

data class Initiative(
    val id: String = "",
    var title: String,
    var descriptions: MutableList<String>,
    var images: MutableList<String>,
    val isPayable: Boolean = true,
    val order: Long?,
    val shareLinks: ShareLinks,
    val initialPage: Int = 0,
    val helpLink: String? = null,
    val helpDescription: String? = null
)
