package com.terranullius.yhadmin.data

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.terranullius.yhadmin.other.Constants.FB_FIELD_DESCRIPTION
import com.terranullius.yhadmin.other.Constants.FB_FIELD_HELP_DESCRIPTION
import com.terranullius.yhadmin.other.Constants.FB_FIELD_HELP_LINK
import com.terranullius.yhadmin.other.Constants.FB_FIELD_IMAGES
import com.terranullius.yhadmin.other.Constants.FB_FIELD_IS_PAYABLE
import com.terranullius.yhadmin.other.Constants.FB_FIELD_NAME
import com.terranullius.yhadmin.other.Constants.FB_FIELD_ORDER
import com.terranullius.yhadmin.other.Constants.FB_FIELD_SHARE_FB
import com.terranullius.yhadmin.other.Constants.FB_FIELD_SHARE_INSTA
import com.terranullius.yhadmin.other.Constants.FB_FIELD_SHARE_TWITTER
import java.util.*

data class InitiativeDto(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: List<String>,
    @field:JvmField
    val isPayable: Boolean = true,
    val images: List<String>,
    val order: Long?,
    val share_insta: String,
    val share_twitter: String,
    val share_fb: String,
    val help_link: String? = null,
    val help_description: String? = null
)

fun InitiativeDto.toInitiative(): Initiative = Initiative(
    id = this.id,
    title = this.name,
    descriptions = this.description as MutableList<String>,
    isDonatable = this.isPayable,
    images = this.images as MutableList<String>,
    order = this.order,
    shareLinks = ShareLinks(
        fb = this.share_fb,
        twitter = this.share_twitter,
        insta = this.share_insta
    ),
    helpLink = this.help_link,
    helpDescription = this.help_description
)

fun DocumentSnapshot.toInitiativeDto(): InitiativeDto? {

    return try {
        InitiativeDto(
            id = this.id,
            name = this.getString(FB_FIELD_NAME)!!,
            description = this.get(FB_FIELD_DESCRIPTION) as List<String>,
            images = this.get(FB_FIELD_IMAGES) as List<String>,
            isPayable = this.getBoolean(FB_FIELD_IS_PAYABLE)!!,
            order = this.getLong(FB_FIELD_ORDER),
            share_fb = this.getString(FB_FIELD_SHARE_FB)!!,
            share_insta = this.getString(FB_FIELD_SHARE_INSTA)!!,
            share_twitter = this.getString(FB_FIELD_SHARE_TWITTER)!!,
            help_description = this.getString(FB_FIELD_HELP_DESCRIPTION)!!,
            help_link = this.getString(FB_FIELD_HELP_LINK)
        )
    } catch (e: Exception) {
        Log.d("fuck","toInitiativeDtoError: ${e.message}")
        return null
    }
}
