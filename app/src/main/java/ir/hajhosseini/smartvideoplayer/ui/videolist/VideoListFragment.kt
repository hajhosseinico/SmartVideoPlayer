package ir.hajhosseini.smartvideoplayer.ui.videolist

import android.content.Intent
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import dagger.hilt.android.AndroidEntryPoint
import ir.hajhosseini.smartvideoplayer.App
import ir.hajhosseini.smartvideoplayer.R
import ir.hajhosseini.smartvideoplayer.databinding.FragmentVideoListBinding
import ir.hajhosseini.smartvideoplayer.model.retrofit.responsemodels.DataState
import ir.hajhosseini.smartvideoplayer.model.room.videolist.VideoListCacheEntity
import ir.hajhosseini.smartvideoplayer.ui.MainActivity
import ir.hajhosseini.smartvideoplayer.util.InternetStatus
import ir.hajhosseini.smartvideoplayer.util.smartvideoplayercore.PlayerViewAdapter.Companion.playIndexThenPausePreviousPlayer
import ir.hajhosseini.smartvideoplayer.util.smartvideoplayercore.PlayerViewAdapter.Companion.releaseAllPlayers
import ir.hajhosseini.smartvideoplayer.util.smartvideoplayercore.RecyclerViewScrollListener
import ir.hajhosseini.smartvideoplayer.util.smartvideoplayercore.service.Constants
import ir.hajhosseini.smartvideoplayer.util.smartvideoplayercore.service.Constants.IS_PRE_LOAD_COMPLETED
import ir.hajhosseini.smartvideoplayer.util.smartvideoplayercore.service.VideoPreLoadingService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class VideoListFragment : Fragment(R.layout.fragment_video_list) {

    private val viewModel: VideoListViewModel by viewModels()
    lateinit var videoListRecyclerAdapter: VideoListRecyclerAdapter
    private var _binding: FragmentVideoListBinding? = null
    private val binding get() = _binding!!
    private lateinit var scrollListener: RecyclerViewScrollListener

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
        setActionbar(activity as MainActivity, getString(R.string.home_title))
        initRecyclerView()

        subscribeObservers()
        getVideoList()

        binding.swipeRefresh.setOnRefreshListener(OnRefreshListener {
            getVideoList()
            binding.swipeRefresh.isRefreshing = false
        })
    }

    private fun getVideoList() {
        var isInternetAvailable: Boolean
        GlobalScope.launch(Dispatchers.IO) {
            isInternetAvailable = InternetStatus().isInternetAvailable()
            withContext(Dispatchers.Main) {
                if (isInternetAvailable)
                    viewModel.setStateEvent(VideoListStateEvent.GetVideos, false)
                else
                    Toast.makeText(
                        requireContext(),
                        "Pleas connect to internet and refresh the application",
                        Toast.LENGTH_LONG
                    ).show()
            }
        }
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<List<VideoListCacheEntity>> -> {
                    displayProgressBar(false)
                    videoListRecyclerAdapter.submitList(dataState.data)

                    if(!App.getBooleanPref(requireContext(),IS_PRE_LOAD_COMPLETED)){
                        // start pre load
                        val arrayList = ArrayList<String>()
                        for (data in dataState.data) {
                            arrayList.add(data.videoUrl)
                        }
                        startPreLoadingService(arrayList)
                        //
                    }

                    if (dataState.isFromCache)
                        notifyIfDataIsFromCache()
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
    }

    private fun notifyIfDataIsFromCache() =
        Toast.makeText(context, "Data is retrieved from cache", Toast.LENGTH_LONG)
            .show()

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@VideoListFragment.context)
            videoListRecyclerAdapter = VideoListRecyclerAdapter()
            adapter = videoListRecyclerAdapter
        }
        binding.recyclerView.setHasFixedSize(true)

        scrollListener = object : RecyclerViewScrollListener() {
            override fun onItemIsFirstVisibleItem(index: Int) {
                if (index != -1)
                    playIndexThenPausePreviousPlayer(index)
            }
        }
        binding.recyclerView.addOnScrollListener(scrollListener)
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

    override fun onPause() {
        super.onPause()
        releaseAllPlayers()
    }

    private fun startPreLoadingService(dataState: ArrayList<String>) {
        val preloadingServiceIntent = Intent(requireContext(), VideoPreLoadingService::class.java)
        preloadingServiceIntent.putStringArrayListExtra(Constants.VIDEO_LIST, dataState)
        requireActivity().startService(preloadingServiceIntent)
    }
}