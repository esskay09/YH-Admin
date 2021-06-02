package com.terranullius.yellowheart.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState

@ExperimentalPagerApi
@Composable
fun ViewPagerImages(modifier: Modifier = Modifier, images: List<String>, pagerState: PagerState) {

    HorizontalPager(state = pagerState, modifier = modifier, reverseLayout = false) { page ->

        Box(modifier = Modifier.fillMaxSize()) {
            val painter = rememberCoilPainter(request = images[page], fadeIn = true)
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painter,
                contentScale = ContentScale.FillBounds,
                contentDescription = ""
            )
            when (painter.loadState) {
                is ImageLoadState.Loading -> CircularProgressIndicator(
                    modifier = Modifier.fillMaxSize(0.5f).align(Alignment.Center).offset(x = 0.dp, y = (-30).dp),
                    color = MaterialTheme.colors.secondary
                )
                else -> {
                }
            }
        }
    }
}