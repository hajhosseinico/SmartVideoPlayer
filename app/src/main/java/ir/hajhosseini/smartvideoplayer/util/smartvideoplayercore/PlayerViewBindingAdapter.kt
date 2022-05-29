package ir.hajhosseini.smartvideoplayer.util.smartvideoplayercore

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.BindingAdapter
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import ir.hajhosseini.smartvideoplayer.App

class PlayerViewAdapter {

    companion object {
        private val simpleCache: SimpleCache = App.simpleCache
        private lateinit var cacheDataSourceFactory: DataSource.Factory

        // for hold all players generated
        private var playersMap: MutableMap<Int, SimpleExoPlayer> = mutableMapOf()

        // for hold current player
        private var currentPlayingVideo: Pair<Int, SimpleExoPlayer>? = null
        fun releaseAllPlayers() {
            playersMap.map {
                it.value.release()
            }
        }

        // call when item recycled to improve performance
        fun releaseRecycledPlayers(index: Int) {
            playersMap[index]?.release()
        }

        private fun pauseCurrentPlayingVideo() {
            if (currentPlayingVideo != null) {
                // the exo player that was playing the video
                currentPlayingVideo?.second?.playWhenReady = false
            }
        }

        fun pauseCurrentPlayerThenPlayIndex(index: Int) {
            if (playersMap[index]?.playWhenReady == false) {
                pauseCurrentPlayingVideo()
                playersMap[index]?.playWhenReady = true
                currentPlayingVideo = Pair(index, playersMap[index]!!)
            }
        }

        private fun setCacheDataSourceFactory(context: Context) {
            val httpDataSourceFactory = DefaultHttpDataSource.Factory()
                .setAllowCrossProtocolRedirects(true)

            DefaultDataSourceFactory(
                context, httpDataSourceFactory
            )

            cacheDataSourceFactory = CacheDataSource.Factory()
                .setCache(simpleCache)
                .setUpstreamDataSourceFactory(httpDataSourceFactory)
                .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
        }

        /*
        *  url is a url of stream video
        *  progressbar for show when start buffering stream
        * thumbnail for show before video start
        * */
        @JvmStatic
        @BindingAdapter(
            value = ["video_url", "on_state_change", "progressbar", "thumbnail", "item_index", "autoPlay"],
            requireAll = false
        )
        fun PlayerView.loadVideo(
            url: String,
            callback: PlayerStateCallback,
            progressbar: ProgressBar,
            thumbnail: ImageView,
            item_index: Int? = null,
            autoPlay: Boolean = false
        ) {
            if (url.isEmpty()) return
            val player = SimpleExoPlayer.Builder(context).build()

            player.playWhenReady = autoPlay
            player.repeatMode = Player.REPEAT_MODE_ALL
            // When changing track, retain the latest frame instead of showing a black screen
            setKeepContentOnPlayerReset(true)
            // We'll show the controller, change to true if want controllers as pause and start
            this.useController = false

            setCacheDataSourceFactory(context)

            val mediaItem = MediaItem.fromUri(Uri.parse(url))
            val mediaSource =
                ProgressiveMediaSource.Factory(cacheDataSourceFactory).createMediaSource(mediaItem)
            player.setMediaSource(mediaSource, true)
            player.prepare()

            this.player = player

            if (playersMap.containsKey(item_index))
                playersMap.remove(item_index)
            if (item_index != null)
                playersMap[item_index] = player

            this.player?.addListener(object : Player.EventListener {
                override fun onPlayerError(error: ExoPlaybackException) {
                    super.onPlayerError(error)
                    this@loadVideo.context.toast("Video in index '$item_index' is not fully cached and Internet is not available")
                }

                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    super.onPlayWhenReadyChanged(playWhenReady, playbackState)

                    if (playbackState == Player.STATE_BUFFERING) {
                        callback.onVideoBuffering(player)
                        thumbnail.visibility = View.VISIBLE
                        progressbar.visibility = View.VISIBLE
                    }

                    if (playbackState == Player.STATE_READY) {
                        progressbar.visibility = View.GONE
                        thumbnail.visibility = View.GONE
                        callback.onVideoDurationRetrieved(this@loadVideo.player!!.duration, player)
                    }

                    if (playbackState == Player.STATE_READY && player.playWhenReady) {
                        callback.onStartedPlaying(player)
                    }
                }
            })
        }
    }
}

fun Context.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}