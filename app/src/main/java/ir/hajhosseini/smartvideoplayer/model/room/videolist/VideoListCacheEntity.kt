package ir.hajhosseini.smartvideoplayer.model.room.videolist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "video_list")
data class VideoListCacheEntity(

    @ColumnInfo(name = "thumbnail")
    var thumbnail: String,

    @PrimaryKey
    @ColumnInfo(name = "video_url")
    var videoUrl: String,

    @ColumnInfo(name = "likes")
    var likes: Int?,

    @ColumnInfo(name = "views")
    var views: Int?,
)