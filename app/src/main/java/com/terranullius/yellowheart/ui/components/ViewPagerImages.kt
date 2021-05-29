package com.terranullius.yellowheart.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.layout.lerp
import com.bumptech.glide.RequestBuilder
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.glide.rememberGlidePainter
import com.google.accompanist.imageloading.LoadPainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import kotlin.math.absoluteValue

@ExperimentalPagerApi
@Composable
fun ViewPagerImages(modifier: Modifier = Modifier, images: List<String>) {

    val pagesState = rememberPagerState(pageCount = images.size, initialOffscreenLimit = 2)

    HorizontalPager(state = pagesState, modifier = modifier, reverseLayout = false) {page->

        Image(
            modifier = Modifier.fillMaxSize(),
            painter = rememberCoilPainter(request = images[page]),
            contentScale = ContentScale.Crop,
            contentDescription = ""
        )
    }
}