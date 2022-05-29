package ir.hajhosseini.smartvideoplayer.model.retrofit.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VideoListNetworkEntity(
    @SerializedName("thumbnail")
    @Expose
    var thumbnail: String,

    @SerializedName("video_url")
    @Expose
    var videoUrl: String,
)