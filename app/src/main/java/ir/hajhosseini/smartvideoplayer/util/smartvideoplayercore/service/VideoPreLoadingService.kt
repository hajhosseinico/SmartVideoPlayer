package ir.hajhosseini.smartvideoplayer.util.smartvideoplayercore.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.upstream.cache.*
import ir.hajhosseini.smartvideoplayer.App
import ir.hajhosseini.smartvideoplayer.repository.DataStoreRepository
import ir.hajhosseini.smartvideoplayer.util.smartvideoplayercore.service.Constants.IS_PRE_LOAD_COMPLETED
import kotlinx.coroutines.*
import okhttp3.Dispatcher
import java.lang.Exception
import java.net.URL
import java.net.URLConnection

class VideoPreLoadingService :
    IntentService(VideoPreLoadingService::class.java.simpleName) {
    private val TAG = "VideoPreLoadingService"

    private lateinit var mContext: Context
    private var cachingJob: Job? = null
    private var videosList: ArrayList<String>? = null

    private lateinit var httpDataSourceFactory: HttpDataSource.Factory
    private lateinit var defaultDataSourceFactory: DefaultDataSourceFactory
    private lateinit var cacheDataSourceFactory: CacheDataSource
    private val simpleCache: SimpleCache = App.simpleCache

    override fun onHandleIntent(intent: Intent?) {
        mContext = applicationContext
        httpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setAllowCrossProtocolRedirects(true)

        defaultDataSourceFactory = DefaultDataSourceFactory(
            this, httpDataSourceFactory
        )

        cacheDataSourceFactory = CacheDataSource.Factory()
            .setCache(simpleCache)
            .setUpstreamDataSourceFactory(httpDataSourceFactory)
            .createDataSource()

        if (intent != null) {
            val extras = intent.extras
            videosList = extras?.getStringArrayList(Constants.VIDEO_LIST)

            if (!videosList.isNullOrEmpty()) {
                preCacheVideo(videosList)
            }
        }
    }

    private fun preCacheVideo(videosList: ArrayList<String>?) {
        var videoUrl: String? = null
        if (!videosList.isNullOrEmpty()) {
            videoUrl = videosList[0]
            videosList.removeAt(0)
        } else {
//            App.addBooleanToPrefs(applicationContext,IS_PRE_LOAD_COMPLETED,true)
            CoroutineScope(Dispatchers.IO).launch {
                DataStoreRepository.addBooleanToPrefs(
                    IS_PRE_LOAD_COMPLETED,
                    true,
                    applicationContext
                )
            }

            stopSelf()
        }
        if (!videoUrl.isNullOrBlank()) {
            try {
                val fileSize = getFileSizeByUrl(videoUrl)

                if (fileSize == 0)
                    stopSelf()

                val requiredSize = calculateRequiredSize(fileSize)
                val videoUri = Uri.parse(videoUrl)
                val dataSpec = DataSpec(videoUri, 0, requiredSize)

                val progressListener =
                    CacheWriter.ProgressListener { requestLength, bytesCached, newBytesCached ->
                        val downloadPercentage: Double = (bytesCached * 100.0 / requestLength)

                        Log.d(TAG, "downloadPercentage $downloadPercentage videoUri: $videoUri")
                    }

                cachingJob = CoroutineScope(Dispatchers.IO).launch {
                    cacheVideo(dataSpec, progressListener)
                    preCacheVideo(videosList)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun cacheVideo(
        dataSpec: DataSpec,
        progressListener: CacheWriter.ProgressListener
    ) {
        runCatching {
            CacheWriter(
                cacheDataSourceFactory,
                dataSpec,
                true,
                null,
                progressListener
            ).cache()
        }.onFailure {
            it.printStackTrace()
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        cachingJob?.cancel()
        return super.onUnbind(intent)
    }

    private fun getFileSizeByUrl(url: String): Int {
        return try {
            val myUrl = URL(url)
            val urlConnection: URLConnection = myUrl.openConnection()
            urlConnection.connect()
            urlConnection.contentLength
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }

    }

    private fun calculateRequiredSize(fileSize: Int): Long {
        var requiredSize = fileSize * 0.1   // try for 10% of the video first

        if (requiredSize < 300000)
            requiredSize = fileSize * 0.2

        if (requiredSize < 300000)
            requiredSize = fileSize * 0.5

        if (requiredSize < 300000)
            requiredSize = fileSize * 0.8

        return requiredSize.toLong()
    }
}