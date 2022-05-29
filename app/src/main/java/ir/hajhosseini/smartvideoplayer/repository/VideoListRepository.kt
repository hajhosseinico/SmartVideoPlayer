package ir.hajhosseini.smartvideoplayer.repository

import ir.hajhosseini.smartvideoplayer.model.retrofit.VideoRetrofitInterface
import ir.hajhosseini.smartvideoplayer.model.retrofit.responsemodels.DataState
import ir.hajhosseini.smartvideoplayer.model.retrofit.responsemodels.VideoListNetworkEntity
import ir.hajhosseini.smartvideoplayer.model.room.videolist.VideoListCacheEntity
import ir.hajhosseini.smartvideoplayer.model.room.videolist.VideoListCacheMapper
import ir.hajhosseini.smartvideoplayer.model.room.videolist.VideoListDao
import ir.hajhosseini.smartvideoplayer.util.InternetStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class VideoListRepository
constructor(
    private val videoDao: VideoListDao,
    private val videoRetrofitInterface: VideoRetrofitInterface,
    private val cacheMapper: VideoListCacheMapper,
    private val internetStatus: InternetStatus,
) {
    suspend fun getVideos(
        workOffline: Boolean
    ): Flow<DataState<List<VideoListCacheEntity>>> =
        flow {
            emit(DataState.Loading)

            withContext(Dispatchers.IO) {
                if (internetStatus.isInternetAvailable() and !workOffline) {
                    try {
                        val baseNetworkMovies = videoRetrofitInterface.getVideoList()
                        for (video in baseNetworkMovies) {
                            videoDao.insert(cacheMapper.mapVideoListToEntity(video))
                        }
                        val cachedVideos = videoDao.get()
                        withContext(Dispatchers.Main.immediate) {
                            emit(DataState.Success(cachedVideos))
                        }

                    } catch (e: Exception) {
                        withContext(Dispatchers.Main.immediate) {
                            emit(DataState.Error(e))
                        }
                    }
                } else {
                    try {
                        val dataState = DataState.Success(videoDao.get())
                        dataState.isFromCache = true
                        withContext(Dispatchers.Main.immediate) {
                            emit(dataState)
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main.immediate) {
                            emit(DataState.Error(e))
                        }
                    }
                }
            }
        }

    suspend fun updateLikes(likes: Int, videoUrl: String): Flow<Int> =
        flow {
            withContext(Dispatchers.IO) {
                try {
                    if (likes < 0)
                        withContext(Dispatchers.Main.immediate) {
                            emit(0)
                        }
                    else {
                        videoDao.updateLikes(likes, videoUrl)
                        withContext(Dispatchers.Main.immediate) {
                            emit(likes)
                        }
                    }

                } catch (e: Exception) {
                    withContext(Dispatchers.Main.immediate) {
                        emit(-1)
                    }
                }
            }
        }

    suspend fun updateViews(views: Int, videoUrl: String): Flow<Int> =
        flow {
            withContext(Dispatchers.IO) {
                try {
                    if (views < 0)
                        withContext(Dispatchers.Main.immediate) {
                            emit(0)
                        }
                    else {
                        videoDao.updateViews(views, videoUrl)
                        withContext(Dispatchers.Main.immediate) {
                            emit(views)
                        }
                    }

                } catch (e: Exception) {
                    withContext(Dispatchers.Main.immediate) {
                        emit(-1)
                    }
                }
            }
        }
}
