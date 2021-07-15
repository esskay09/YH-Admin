package com.terranullius.yellowheartadmin.data

data class Initiative(
    val id: String = "",
    var title: String,
    var descriptions: MutableList<String>,
    var images: MutableList<String>,
    var isDonatable: Boolean = true,
    val order: Long?,
    val shareLinks: ShareLinks,
    val initialPage: Int = 0,
    var helpLink: String? = null,
    var helpDescription: String? = null
)
