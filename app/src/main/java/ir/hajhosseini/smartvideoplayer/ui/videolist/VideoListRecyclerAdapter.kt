package ir.hajhosseini.smartvideoplayer.ui.videolist


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.google.android.exoplayer2.Player
import ir.hajhosseini.smartvideoplayer.R
import ir.hajhosseini.smartvideoplayer.databinding.ItemVideoBinding
import ir.hajhosseini.smartvideoplayer.model.room.videolist.VideoListCacheEntity
import ir.hajhosseini.smartvideoplayer.util.smartvideoplayercore.PlayerStateCallback
import ir.hajhosseini.smartvideoplayer.util.smartvideoplayercore.PlayerViewAdapter.Companion.releaseRecycledPlayers


class VideoListRecyclerAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), PlayerStateCallback {

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<VideoListCacheEntity>() {

        override fun areItemsTheSame(
            oldItem: VideoListCacheEntity,
            newItem: VideoListCacheEntity
        ): Boolean {
            return oldItem.videoUrl == newItem.videoUrl
        }

        override fun areContentsTheSame(
            oldItem: VideoListCacheEntity,
            newItem: VideoListCacheEntity
        ): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemVideoBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.item_video, parent, false
        )
        return VideoPostViewHolder(binding)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        val position = holder.adapterPosition
        releaseRecycledPlayers(position)
        super.onViewRecycled(holder)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is VideoPostViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<VideoListCacheEntity>) {
        differ.submitList(list)
    }

    inner class VideoPostViewHolder
    constructor(
        private val binding: ItemVideoBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: VideoListCacheEntity) = with(itemView) {

            Glide.with(itemView.context)
                .load(item.thumbnail)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.img_thumbnail)
                .into(binding.imgThumbnail)

            binding.txtNumberRow.text =
                "${context.getString(R.string.number_row)} ${adapterPosition + 1}"

            binding.txtLikes.text = item.likes.toString()
            binding.txtViewers.text = item.views.toString()

            binding.apply {
                dataModel = item
                callback = this@VideoListRecyclerAdapter
                index = adapterPosition
                executePendingBindings()
            }
        }
    }
    override fun onVideoDurationRetrieved(duration: Long, player: Player) {
        Log.d("video_log", "onVideoDurationRetrieved " + duration)
    }

    override fun onVideoBuffering(player: Player) {
        Log.d("video_log", "onVideoBuffering " + player.contentDuration)
    }

    override fun onStartedPlaying(player: Player) {
        Log.d("video_log", "playvideo " + player.contentDuration)
    }

    override fun onFinishedPlaying(player: Player) {
        Log.d("video_log", "onFinishedPlaying " + player.contentDuration)
    }
}


