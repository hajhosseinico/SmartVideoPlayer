package ir.hajhosseini.smartvideoplayer.model.room.videolist

import androidx.room.*

/**
 * Movie list room Dao
 */
@Dao
interface VideoListDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(videoListEntity: VideoListCacheEntity): Long

    @Query("SELECT * FROM video_list")
    suspend fun get(): List<VideoListCacheEntity>

    @Query("UPDATE video_list SET views = :views WHERE video_url = :viewUrl")
    suspend fun updateViews(views: Int, viewUrl: String)

    @Query("UPDATE video_list SET likes = :likes WHERE video_url = :viewUrl")
    suspend fun updateLikes(likes: Int, viewUrl: String)
}