package com.terranullius.yhadmin.other

import com.terranullius.yhadmin.data.Initiative

data class UpdateImageProperties(
    var isUpdating: Boolean = false,
    var initiative: Initiative,
    var imagePosition: Int
) {
}