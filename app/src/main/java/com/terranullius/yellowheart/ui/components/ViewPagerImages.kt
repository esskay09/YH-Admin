package com.terranullius.yellowheart.ui.components

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@ExperimentalPagerApi
@Composable
fun ViewPagerImages(modifier: Modifier = Modifier, images: List<String>, pagerState: PagerState) {

    val youtubePlayerList = remember {
        mutableStateListOf<YoutubePlayerIndexed>()
    }

    LaunchedEffect(key1 = pagerState.currentPage){

        val currentPage = pagerState.currentPage

        if (!images[currentPage].contains("youtubeID=")){

            //Page doesn't contain a video
            youtubePlayerList.forEach {
                it.pause()
            }
            val delayMillis = Random.nextInt(3, 7) * 1000L
            launch {
                delay(delayMillis)
                pagerState.animateScrollToPage(currentPage+1)
            }
        } else {
            youtubePlayerList.find { it.index == currentPage }?.play()
        }
    }

    HorizontalPager(state = pagerState, modifier = modifier, reverseLayout = false) { page ->

        Box(modifier = Modifier.fillMaxSize()) {
            if (images[page].contains("youtubeID")){
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize(),
                    factory = {context ->
                        val youTubePlayerView = YouTubePlayerView(context)
                        (context as AppCompatActivity).lifecycle.addObserver(youTubePlayerView)
                        val videoId = images[page].substringAfter("=")
                        youTubePlayerView.apply {
                            getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
                                override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                                    youTubePlayer.loadVideo(videoId, 0f)
                                    youtubePlayerList.find {
                                        it.index == page
                                    }?.let {
                                        it.youTubePlayer = youTubePlayer
                                    } ?: youtubePlayerList.add(YoutubePlayerIndexed(youTubePlayer, page))
                                }
                            })
                        }
                    }) {
                    it.enterFullScreen()
                    it.enableBackgroundPlayback(false)
                    it.getPlayerUiController().apply {
                        showFullscreenButton(false)
                        showVideoTitle(false)
                        showYouTubeButton(false)
                        showSeekBar(true)
                        showMenuButton(false)
                    }

                }
            }
            else {
                val painter = rememberCoilPainter(request = images[page], fadeIn = true)
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painter,
                    contentScale = ContentScale.FillBounds,
                    contentDescription = ""
                )
                when (painter.loadState) {
                    is ImageLoadState.Loading -> CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize(0.5f)
                            .align(Alignment.Center)
                            .offset(x = 0.dp, y = (-30).dp),
                        color = MaterialTheme.colors.secondary
                    )
                    else -> {
                    }
                }
            }
        }
    }
}

class YoutubePlayerIndexed(var youTubePlayer: YouTubePlayer, val index: Int){
    fun play(){
        youTubePlayer.play()
    }
    fun pause(){
        youTubePlayer.pause()
    }
}