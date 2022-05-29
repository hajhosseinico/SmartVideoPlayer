package ir.hajhosseini.smartvideoplayer.model.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.hajhosseini.smartvideoplayer.model.room.videolist.VideoListCacheEntity
import ir.hajhosseini.smartvideoplayer.model.room.videolist.VideoListDao

/**
 * Instantiating room database and setting database name
 * Used by Provider Modules
 */
@Database(entities = [VideoListCacheEntity::class],version = 1)
abstract class VideoDatabase : RoomDatabase (){
    abstract fun videoListDao(): VideoListDao

    companion object{
        const val DATABASE_NAME = "video_db"
    }
}