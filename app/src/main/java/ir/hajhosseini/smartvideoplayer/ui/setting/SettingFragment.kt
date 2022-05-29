package ir.hajhosseini.smartvideoplayer.ui.setting

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.hajhosseini.smartvideoplayer.R
import ir.hajhosseini.smartvideoplayer.databinding.FragmentVideoListBinding
import ir.hajhosseini.smartvideoplayer.model.retrofit.responsemodels.DataState
import ir.hajhosseini.smartvideoplayer.model.room.videolist.VideoListCacheEntity
import ir.hajhosseini.smartvideoplayer.ui.MainActivity
import ir.hajhosseini.smartvideoplayer.ui.videolist.VideoListStateEvent
import ir.hajhosseini.smartvideoplayer.ui.videolist.VideoListViewModel

/**
 * SettingFragment
 */
@AndroidEntryPoint
class SettingFragment : Fragment(R.layout.fragment_video_list),
    SettingRecyclerAdapter.Interaction {

    private val viewModel: VideoListViewModel by viewModels()
    var videoListRecyclerAdapter: SettingRecyclerAdapter? = null
    private var _binding: FragmentVideoListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setActionbar(activity as MainActivity, getString(R.string.settings_title))
        subscribeObservers()
        viewModel.setStateEvent(VideoListStateEvent.GetVideos, false)
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<List<VideoListCacheEntity>> -> {
                    displayProgressBar(false)
                    initOrUpdateRecyclerView(dataState.data as ArrayList<VideoListCacheEntity>)
                }
                is DataState.Error -> {
                    displayProgressBar(false)
                    displayError(dataState.exception.toString())
                }

                is DataState.Loading -> {
                    displayProgressBar(true)
                }
            }
        })
        viewModel.updateDataState.observe(viewLifecycleOwner, Observer {
            viewModel.setStateEvent(VideoListStateEvent.GetVideos, true)
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initOrUpdateRecyclerView(videoList: List<VideoListCacheEntity>) {
        if (binding.recyclerView.adapter != null) {
            binding.recyclerView.adapter?.notifyDataSetChanged()
        } else {
            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(this@SettingFragment.context)
                videoListRecyclerAdapter =
                    SettingRecyclerAdapter(this@SettingFragment, ArrayList(videoList))
                adapter = videoListRecyclerAdapter
            }
        }
    }

    private fun displayError(message: String?) {
        Toast.makeText(context, "Internet and cached data are not available", Toast.LENGTH_LONG)
            .show()
        Log.d("LOG", message ?: "")
    }

    private fun displayProgressBar(shouldDisplay: Boolean) {
        view?.findViewById<ProgressBar>(R.id.prg_loading)!!.visibility =
            if (shouldDisplay) View.VISIBLE else View.GONE
    }

    private fun setActionbar(activity: MainActivity, title: String) {
        activity.setToolbarTitle(title)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPlusLikeClicked(position: Int, item: VideoListCacheEntity) {
        viewModel.setUpdateLikeAndViewStateEvent(VideoListStateEvent.UpdateLikes, item)
    }

    override fun onMinusLikeClicked(position: Int, item: VideoListCacheEntity) {
        viewModel.setUpdateLikeAndViewStateEvent(VideoListStateEvent.UpdateLikes, item)
    }

    override fun onPlusViewClicked(position: Int, item: VideoListCacheEntity) {
        viewModel.setUpdateLikeAndViewStateEvent(VideoListStateEvent.UpdateViews, item)
    }

    override fun onMinusViewClicked(position: Int, item: VideoListCacheEntity) {
        viewModel.setUpdateLikeAndViewStateEvent(VideoListStateEvent.UpdateViews, item)
    }
}