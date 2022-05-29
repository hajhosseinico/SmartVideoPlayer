package ir.hajhosseini.smartvideoplayer.ui.setting


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ir.hajhosseini.smartvideoplayer.R
import ir.hajhosseini.smartvideoplayer.databinding.ItemSettingBinding
import ir.hajhosseini.smartvideoplayer.model.room.videolist.VideoListCacheEntity


class SettingRecyclerAdapter(
    private val interaction: Interaction? = null,
    private val videoList: ArrayList<VideoListCacheEntity>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemSettingBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.item_setting, parent, false
        )
        return SettingPostViewHolder(binding, interaction)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SettingPostViewHolder -> {
                holder.bind(videoList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    class SettingPostViewHolder
    constructor(
        private val binding: ItemSettingBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: VideoListCacheEntity) = with(itemView) {

            binding.txtNumberRow.text =
                "${context.getString(R.string.number_row)} ${adapterPosition + 1}"

            binding.txtLikes.text = item.likes.toString()
            binding.txtViewers.text = item.views.toString()

            binding.imgPlusLike.setOnClickListener {
                item.likes = item.likes?.plus(1)
                interaction?.onPlusLikeClicked(adapterPosition, item)
            }
            binding.imgPlusView.setOnClickListener {
                item.views = item.views?.plus(1)
                interaction?.onPlusViewClicked(adapterPosition, item)
            }
            binding.imgMinusLike.setOnClickListener {
                item.likes = item.likes?.minus(1)
                interaction?.onMinusLikeClicked(adapterPosition, item)
            }
            binding.imgMinusView.setOnClickListener {
                item.views = item.views?.minus(1)
                interaction?.onMinusViewClicked(adapterPosition, item)
            }
        }
    }

    interface Interaction {
        fun onPlusLikeClicked(position: Int, item: VideoListCacheEntity)
        fun onMinusLikeClicked(position: Int, item: VideoListCacheEntity)
        fun onPlusViewClicked(position: Int, item: VideoListCacheEntity)
        fun onMinusViewClicked(position: Int, item: VideoListCacheEntity)
    }
}


