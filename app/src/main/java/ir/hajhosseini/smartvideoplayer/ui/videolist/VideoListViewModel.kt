package ir.hajhosseini.smartvideoplayer.ui.videolist

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hajhosseini.smartvideoplayer.model.retrofit.responsemodels.DataState
import ir.hajhosseini.smartvideoplayer.model.room.videolist.VideoListCacheEntity
import ir.hajhosseini.smartvideoplayer.repository.VideoListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoListViewModel
@Inject
constructor(
    private val videoListRepository: VideoListRepository,
) : ViewModel() {
    private val _dataState: MutableLiveData<DataState<List<VideoListCacheEntity>>> =
        MutableLiveData()

    val dataState: LiveData<DataState<List<VideoListCacheEntity>>>
        get() = _dataState

    private val _updateDataState: MutableLiveData<Int> =
        MutableLiveData()

    val updateDataState: LiveData<Int>
        get() = _updateDataState

    fun setStateEvent(mainStateEvent: VideoListStateEvent, workOffline: Boolean) {
        viewModelScope.launch() {
            when (mainStateEvent) {
                is VideoListStateEvent.GetVideos -> {
                    videoListRepository.getVideos(workOffline)
                        .onEach { dataState ->
                            _dataState.value = dataState
                        }
                        .launchIn(viewModelScope)
                }
                else -> {}
            }
        }
    }

    fun setUpdateLikeAndViewStateEvent(
        mainStateEvent: VideoListStateEvent,
        videoListCacheEntity: VideoListCacheEntity
    ) {
        viewModelScope.launch() {
            when (mainStateEvent) {

                is VideoListStateEvent.UpdateLikes -> {
                    videoListRepository.updateLikes(
                        videoListCacheEntity.likes!!,
                        videoListCacheEntity.videoUrl
                    )
                        .onEach { dataState ->
                            _updateDataState.value = dataState
                        }
                        .launchIn(viewModelScope)
                }
                is VideoListStateEvent.UpdateViews -> {
                    videoListRepository.updateViews(
                        videoListCacheEntity.views!!,
                        videoListCacheEntity.videoUrl
                    )
                        .onEach { dataState ->
                            _updateDataState.value = dataState
                        }
                        .launchIn(viewModelScope)
                }
                else -> {}
            }
        }
    }
}

sealed class VideoListStateEvent {
    object GetVideos : VideoListStateEvent()
    object UpdateLikes : VideoListStateEvent()
    object UpdateViews : VideoListStateEvent()
}

