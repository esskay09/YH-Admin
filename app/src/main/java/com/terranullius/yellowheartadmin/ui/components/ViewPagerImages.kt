package com.terranullius.yellowheartadmin.ui.components

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerState
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@ExperimentalPagerApi
@Composable
fun ViewPagerImages(
    modifier: Modifier = Modifier,
    images: List<String>,
    pagerState: PagerState,
    onYoutubePlayerClicked: () -> Unit = {},
    isVideoPlaying: MutableState<Boolean>
) {

    val youtubePlayerList = remember {
        mutableSetOf<YoutubePlayerIndexed>()
    }

    var currentPlayingVideoIndex by remember {
        mutableStateOf(15)
    }

    var imageSize by remember {
        mutableStateOf(1f)
    }

    LaunchedEffect(key1 = pagerState.currentPage) {
        launch {
            while (imageSize <= 1.4f) {

                if (!pagerState.isScrollInProgress) imageSize += 0.0015f
                else imageSize = 1f

                if (imageSize > 1.4f) imageSize = 1f

                delay(50L)
            }
        }
        val currentPage = pagerState.currentPage
        val currentYoutubePlayer = youtubePlayerList.find {
            it.index == currentPage
        }
        //Check if it's an image
        if (!images[currentPage].contains("youtubeID=")) {
            //Image
            scrollPage(pagerState)
            youtubePlayerList.forEach {
                it.pause()
            }
        } else { //Video
            currentYoutubePlayer?.let { currentPlayer ->
                if (isVideoPlaying.value) {
                    if (currentPlayingVideoIndex == currentPage) {
                        scrollPage(pagerState)
                    } else onBuffered(currentPlayer) {
                        if (isVideoPlaying.value) {
                            if (currentPlayingVideoIndex != currentPage) scrollPage(pagerState)
                        } else currentPlayer.play()
                    }
                } else {
                    onBuffered(currentPlayer) {
                        if (isVideoPlaying.value) {
                            if (currentPlayingVideoIndex != currentPage) scrollPage(pagerState)
                        } else currentPlayer.play()
                    }
                }
            }
        }
    }

    HorizontalPager(state = pagerState, modifier = modifier, reverseLayout = false) { page ->

        Box(modifier = Modifier.fillMaxSize()) {
            if (images[page].contains("youtubeID")) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize(),
                    factory = { context ->
                        val youTubePlayerView = YouTubePlayerView(context)
                        (context as AppCompatActivity).lifecycle.addObserver(youTubePlayerView)
                        val videoId = images[page].substringAfter("=")
                        youTubePlayerView.apply {
                            getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
                                override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                                    youTubePlayer.cueVideo(videoId, 0f)
                                    youtubePlayerList.find {
                                        it.index == page
                                    }?.let {
                                        it.youTubePlayer = youTubePlayer
                                    } ?: youtubePlayerList.add(
                                        YoutubePlayerIndexed(
                                            youTubePlayer,
                                            page
                                        )
                                    )
                                }
                            })
                        }
                    }) {
                    it.setOnClickListener {
                        onYoutubePlayerClicked()
                    }
                    it.addYouTubePlayerListener(object : YouTubePlayerListener {
                        override fun onApiChange(youTubePlayer: YouTubePlayer) {

                        }

                        override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {

                        }

                        override fun onError(
                            youTubePlayer: YouTubePlayer,
                            error: PlayerConstants.PlayerError
                        ) {
                            setPlaying(false)
                        }

                        private fun setPlaying(value: Boolean) {
                            if (currentPlayingVideoIndex == page) {
                                isVideoPlaying.value = value
                            }
                        }

                        override fun onPlaybackQualityChange(
                            youTubePlayer: YouTubePlayer,
                            playbackQuality: PlayerConstants.PlaybackQuality
                        ) {

                        }

                        override fun onPlaybackRateChange(
                            youTubePlayer: YouTubePlayer,
                            playbackRate: PlayerConstants.PlaybackRate
                        ) {

                        }

                        override fun onReady(youTubePlayer: YouTubePlayer) {

                        }

                        override fun onStateChange(
                            youTubePlayer: YouTubePlayer,
                            state: PlayerState
                        ) {

                            val currentPlayer = youtubePlayerList.find { playerIndexed ->
                                playerIndexed.index == page
                            }

                            when (state) {
                                PlayerState.PAUSED -> {
                                    currentPlayingVideoIndex = 15
                                    currentPlayer?.isBuffering = false
                                }
                                PlayerState.ENDED -> {
                                    setPlaying(false)
                                    currentPlayer?.isBuffering = false
                                }
                                PlayerState.PLAYING -> {
                                    currentPlayingVideoIndex = page
                                    isVideoPlaying.value = true
                                    currentPlayer?.isBuffering = false
                                }
                                PlayerState.BUFFERING -> currentPlayer?.isBuffering = true
                                PlayerState.VIDEO_CUED -> currentPlayer?.isBuffering = false
                                PlayerState.UNKNOWN -> currentPlayer?.isBuffering = false
                                else -> {
                                }
                            }
                        }

                        override fun onVideoDuration(
                            youTubePlayer: YouTubePlayer,
                            duration: Float
                        ) {

                        }

                        override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {

                        }

                        override fun onVideoLoadedFraction(
                            youTubePlayer: YouTubePlayer,
                            loadedFraction: Float
                        ) {

                        }
                    })
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

                Box(modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxHeight(0.89f)
                    .fillMaxWidth(0.4f)
                    .clickable {
                        onYoutubePlayerClicked()
                    })
                Box(modifier = Modifier
                    .align(Alignment.TopEnd)
                    .fillMaxHeight(0.89f)
                    .fillMaxWidth(0.4f)
                    .clickable {
                        onYoutubePlayerClicked()
                    })
                Box(modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f)
                    .clickable {
                        onYoutubePlayerClicked()
                    })
                Box(modifier = Modifier
                    .align(Alignment.TopEnd)
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f)
                    .clickable {
                        onYoutubePlayerClicked()
                    })

            } else {
                val painter = rememberCoilPainter(request = images[page], fadeIn = true)

                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                if (pagerState.targetPage == page || pagerState.currentPage == page) {
                                    scaleY = imageSize
                                    scaleX = imageSize
                                } else {
                                    scaleY = 1f
                                    scaleX = 1f
                                }
                            },
                        painter = painter,
                        contentScale = ContentScale.FillBounds,
                        contentDescription = ""
                    )
                }
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

@OptIn(ExperimentalPagerApi::class)
fun CoroutineScope.scrollPage(pagerState: PagerState) {
    launch {
        val randomDelayMillis = Random.nextInt(4, 11) * 1000L
        delay(randomDelayMillis)
        pagerState.animateScrollToPage(pagerState.currentPage + 1)
    }
}

private fun CoroutineScope.onBuffered(youTubePlayer: YoutubePlayerIndexed, onBuffered: () -> Unit) {
    if (!youTubePlayer.isBuffering) onBuffered()
    else {
        launch {
            while (true) {
                if (!youTubePlayer.isBuffering) {
                    onBuffered()
                    return@launch
                } else delay(150L)
            }
        }
    }
}

class YoutubePlayerIndexed(
    var youTubePlayer: YouTubePlayer,
    val index: Int,
    var isBuffering: Boolean = false
) {
    fun play() {
        youTubePlayer.play()
    }

    fun pause() {
        youTubePlayer.pause()
    }
}