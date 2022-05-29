package ir.hajhosseini.smartvideoplayer.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.hajhosseini.smartvideoplayer.model.room.VideoDatabase
import ir.hajhosseini.smartvideoplayer.model.room.videolist.VideoListDao
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object RoomModule {
    @Singleton
    @Provides
    fun provideVideoDb(@ApplicationContext context: Context): VideoDatabase {
        return Room.databaseBuilder(
            context, VideoDatabase::class.java,
            VideoDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideVideoDAO(videoDatabase: VideoDatabase): VideoListDao {
        return videoDatabase.videoListDao()
    }
}