package com.terranullius.yellowheart.data

import com.google.firebase.firestore.DocumentSnapshot
import com.terranullius.yellowheart.other.Constants.FB_FIELD_DESCRIPTION
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
    val description: String,
    @field:JvmField
    val isPayable: Boolean = true,
    val images: List<String>,
    val order: Long?,
    val shareLinkFb: String,
    val shareLinkInsta: String,
    val shareLinkTwitter: String
)

fun InitiativeDto.toInitiative(): Initiative = Initiative(
    id = this.id,
    name = this.name,
    description = this.description,
    isPayable = this.isPayable,
    images = this.images,
    order = this.order,
    shareLinkFb = this.shareLinkFb,
    shareLinkInsta = this.shareLinkInsta,
    shareLinkTwitter = this.shareLinkTwitter
)

fun DocumentSnapshot.toInitiativeDto(): InitiativeDto? {
    return try {
        InitiativeDto(
            id = this.id,
            name = this.getString(FB_FIELD_NAME)!!,
            description = this.getString(FB_FIELD_DESCRIPTION)!!,
            images = this.get(FB_FIELD_IMAGES) as List<String>,
            isPayable = this.getBoolean(FB_FIELD_IS_PAYABLE)!!,
            order = this.getLong(FB_FIELD_ORDER),
            shareLinkFb = this.getString(FB_FIELD_SHARE_FB)!!,
            shareLinkInsta = this.getString(FB_FIELD_SHARE_INSTA)!!,
            shareLinkTwitter = this.getString(FB_FIELD_SHARE_TWITTER)!!
        )
    } catch (e: Exception) {
        return null
    }
}
