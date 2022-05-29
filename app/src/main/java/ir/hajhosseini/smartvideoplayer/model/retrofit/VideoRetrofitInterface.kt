package ir.hajhosseini.smartvideoplayer.model.retrofit

import ir.hajhosseini.smartvideoplayer.model.retrofit.responsemodels.VideoListNetworkEntity
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Api queries
 * used by retrofit
 */
interface VideoRetrofitInterface {
    @GET("/s/11hex4wrxwzd3pv/videos.json")
    suspend fun getVideoList(): ArrayList<VideoListNetworkEntity>
}