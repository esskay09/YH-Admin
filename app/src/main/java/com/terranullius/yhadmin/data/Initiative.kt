package com.terranullius.yhadmin.data

data class Initiative(
    val id: String = "",
    var title: String = "Title",
    var descriptions: MutableList<String> = mutableListOf<String>(),
    var images: MutableList<String>  = mutableListOf<String>(),
    var isDonatable: Boolean = true,
    val order: Long? = 3,
    val shareLinks: ShareLinks = ShareLinks("", "", ""),
    val initialPage: Int = 0,
    var helpLink: String? = null,
    var helpDescription: String? = null
)

fun Initiative.toInitiativeDto() = InitiativeDto(
    id = this.id,
    name = this.title,
    description = this.descriptions,
    isPayable = this.isDonatable,
    images = this.images,
    order = this.order,
    share_fb = this.shareLinks.fb,
    share_twitter = this.shareLinks.twitter,
    share_insta = this.shareLinks.insta,
    help_link = this.helpLink,
    help_description = this.helpDescription
)
