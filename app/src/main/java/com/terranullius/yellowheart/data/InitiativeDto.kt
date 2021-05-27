package com.terranullius.yellowheart.data

import com.google.firebase.firestore.DocumentSnapshot
import com.terranullius.yellowheart.other.Constants.FB_FIELD_DESCRIPTION
import com.terranullius.yellowheart.other.Constants.FB_FIELD_IMG_URL
import com.terranullius.yellowheart.other.Constants.FB_FIELD_IS_PAYABLE
import com.terranullius.yellowheart.other.Constants.FB_FIELD_NAME
import java.util.*

data class InitiativeDto(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    @field:JvmField
    val isPayable: Boolean = true,
    val imgUrl: String,
)

fun InitiativeDto.toInitiative(): Initiative = Initiative(
    id = this.id,
    name = this.name,
    description = this.description,
    isPayable = this.isPayable,
    imgUrl = this.imgUrl
)

fun DocumentSnapshot.toInitiativeDto(): InitiativeDto? {
    return try {
        InitiativeDto(
            id = this.id,
            name = this.getString(FB_FIELD_NAME)!!,
            description = this.getString(FB_FIELD_DESCRIPTION)!!,
            imgUrl = this.getString(FB_FIELD_IMG_URL)!!,
            isPayable = this.getBoolean(FB_FIELD_IS_PAYABLE)!!
        )
    } catch (e: Exception) {
        return null
    }
}
