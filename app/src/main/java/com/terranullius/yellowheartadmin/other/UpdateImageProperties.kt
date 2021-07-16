package com.terranullius.yellowheartadmin.other

import com.terranullius.yellowheartadmin.data.Initiative

data class UpdateImageProperties(
    var isUpdating: Boolean = false,
    var initiative: Initiative,
    var imagePosition: Int
) {
}