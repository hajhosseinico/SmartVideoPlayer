package ir.hajhosseini.smartvideoplayer.model.room.videolist

/**
 * Movie list entity mapper interface
 */
interface VideoListEntityMapper<Entity, DomainModel> {
    fun mapVideoListFromEntity(entity:Entity) : DomainModel
    fun mapVideoListToEntity(domainModel: DomainModel) : Entity
}