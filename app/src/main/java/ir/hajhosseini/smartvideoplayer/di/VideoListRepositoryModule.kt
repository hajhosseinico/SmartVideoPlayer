package ir.hajhosseini.smartvideoplayer.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.hajhosseini.smartvideoplayer.model.retrofit.VideoRetrofitInterface
import ir.hajhosseini.smartvideoplayer.model.room.videolist.VideoListDao
import ir.hajhosseini.smartvideoplayer.model.room.videolist.VideoListCacheMapper
import ir.hajhosseini.smartvideoplayer.repository.VideoListRepository
import ir.hajhosseini.smartvideoplayer.util.NetworkListener
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object VideoListRepositoryModule {
    @Singleton
    @Provides
    fun provideMovieListRepository(
        movieDao: VideoListDao,
        retrofitInterface: VideoRetrofitInterface,
        cacheMapper: VideoListCacheMapper,
        networkListener: NetworkListener
    ): VideoListRepository {
        return VideoListRepository(movieDao,retrofitInterface,cacheMapper,networkListener)
    }
}