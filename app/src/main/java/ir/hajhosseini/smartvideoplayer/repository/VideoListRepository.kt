package ir.hajhosseini.smartvideoplayer.repository

import ir.hajhosseini.smartvideoplayer.model.retrofit.VideoRetrofitInterface
import ir.hajhosseini.smartvideoplayer.model.retrofit.responsemodels.DataState
import ir.hajhosseini.smartvideoplayer.model.room.videolist.VideoListCacheEntity
import ir.hajhosseini.smartvideoplayer.model.room.videolist.VideoListCacheMapper
import ir.hajhosseini.smartvideoplayer.model.room.videolist.VideoListDao
import ir.hajhosseini.smartvideoplayer.util.NetworkListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class VideoListRepository
constructor(
    private val videoDao: VideoListDao,
    private val videoRetrofitInterface: VideoRetrofitInterface,
    private val cacheMapper: VideoListCacheMapper,
    private val networkListener: NetworkListener,
) {
    suspend fun getVideos(
        workOffline: Boolean
    ): Flow<DataState<List<VideoListCacheEntity>>> =
        flow {
            emit(DataState.Loading)

            if (networkListener.isConnected and !workOffline) {
                val baseNetworkMovies = videoRetrofitInterface.getVideoList()
                for (video in baseNetworkMovies) {
                    videoDao.insert(cacheMapper.mapVideoListToEntity(video))
                }
                val cachedVideos = videoDao.get()
                emit(DataState.Success(cachedVideos))

            } else {
                // retry from cache
                val dataState = DataState.Success(videoDao.get())
                    dataState.isFromCache = dataState.data.isNotEmpty()
                emit(dataState)
            }
        }

    suspend fun updateLikes(likes: Int, videoUrl: String): Flow<Int> =
        flow {
            if (likes < 0)
                emit(0)
            else {
                videoDao.updateLikes(likes, videoUrl)
                emit(likes)
            }
        }

    suspend fun updateViews(views: Int, videoUrl: String): Flow<Int> =
        flow {
            if (views < 0)
                emit(0)
            else {
                videoDao.updateViews(views, videoUrl)
                emit(views)
            }
        }
}
