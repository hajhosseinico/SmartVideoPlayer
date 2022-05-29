package ir.hajhosseini.smartvideoplayer.model.room.videolist

import ir.hajhosseini.smartvideoplayer.model.retrofit.responsemodels.VideoListNetworkEntity
import javax.inject.Inject


class VideoListCacheMapper
@Inject
constructor() :
    VideoListEntityMapper<VideoListCacheEntity, VideoListNetworkEntity> {

    override fun mapVideoListToEntity(domainModel: VideoListNetworkEntity): VideoListCacheEntity {
        return VideoListCacheEntity(
            videoUrl = domainModel.videoUrl,
            thumbnail = domainModel.thumbnail,
            likes = 0,
            views = 0,
        )
    }

    fun mapFromEntityList(entities: List<VideoListCacheEntity>): List<VideoListNetworkEntity> {
        return entities.map { movieList ->
            mapVideoListFromEntity(movieList)
        }
    }

    override fun mapVideoListFromEntity(entity: VideoListCacheEntity): VideoListNetworkEntity {
        return VideoListNetworkEntity(
            videoUrl = entity.videoUrl,
            thumbnail = entity.thumbnail,
        )
    }

}
