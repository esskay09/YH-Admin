package com.terranullius.yellowheart.data

import com.google.firebase.firestore.DocumentSnapshot
import com.terranullius.yellowheart.other.Constants.FB_FIELD_DESCRIPTION
import com.terranullius.yellowheart.other.Constants.FB_FIELD_HELP_DESCRIPTION
import com.terranullius.yellowheart.other.Constants.FB_FIELD_HELP_LINK
import com.terranullius.yellowheart.other.Constants.FB_FIELD_IMAGES
import com.terranullius.yellowheart.other.Constants.FB_FIELD_IMG_URL
import com.terranullius.yellowheart.other.Constants.FB_FIELD_IS_PAYABLE
import com.terranullius.yellowheart.other.Constants.FB_FIELD_NAME
import com.terranullius.yellowheart.other.Constants.FB_FIELD_ORDER
import com.terranullius.yellowheart.other.Constants.FB_FIELD_SHARE_FB
import com.terranullius.yellowheart.other.Constants.FB_FIELD_SHARE_INSTA
import com.terranullius.yellowheart.other.Constants.FB_FIELD_SHARE_TWITTER
import java.util.*

data class InitiativeDto(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val descriptions: List<String>,
    @field:JvmField
    val isPayable: Boolean = true,
    val images: List<String>,
    val order: Long?,
    val shareLinks: ShareLinks,
    val helpLink: String? = null,
    val helpDescription: String? = null
)

fun InitiativeDto.toInitiative(): Initiative = Initiative(
    id = this.id,
    name = this.name,
    descriptions = this.descriptions,
    isPayable = this.isPayable,
    images = this.images,
    order = this.order,
    shareLinks = this.shareLinks,
    helpLink = this.helpLink,
    helpDescription = this.helpDescription
)

fun DocumentSnapshot.toInitiativeDto(): InitiativeDto? {

    val shareLinks = ShareLinks(
        fb = this.getString(FB_FIELD_SHARE_FB)!!,
        insta = this.getString(FB_FIELD_SHARE_INSTA)!!,
        twitter = this.getString(FB_FIELD_SHARE_TWITTER)!!
    )

    return try {
        InitiativeDto(
            id = this.id,
            name = this.getString(FB_FIELD_NAME)!!,
            descriptions = this.get(FB_FIELD_DESCRIPTION) as List<String>,
            images = this.get(FB_FIELD_IMAGES) as List<String>,
            isPayable = this.getBoolean(FB_FIELD_IS_PAYABLE)!!,
            order = this.getLong(FB_FIELD_ORDER),
            shareLinks = shareLinks,
            helpDescription = this.getString(FB_FIELD_HELP_DESCRIPTION)!!,
            helpLink = this.getString(FB_FIELD_HELP_LINK)!!
        )
    } catch (e: Exception) {
        return null
    }
}
