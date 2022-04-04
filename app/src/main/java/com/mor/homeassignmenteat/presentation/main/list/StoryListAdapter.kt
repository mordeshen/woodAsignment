package com.mor.homeassignmenteat.presentation.main.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.mor.homeassignmenteat.R
import com.mor.homeassignmenteat.buisness.domain.model.Story
import com.mor.homeassignmenteat.databinding.LayoutListItemBinding

class StoryListAdapter(
    private val interaction: Interaction? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val requestOptions = RequestOptions
        .placeholderOf(R.drawable.default_image)
        .error(R.drawable.default_image)

    private val TAG: String = "AppDebug"

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {

        override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem.imageUrl == newItem.imageUrl
        }

        override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem == newItem
        }

    }
    private val differ =
        AsyncListDiffer(
            StoryRecyclerChangeCallback(this),
            AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
        )


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(
            LayoutListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            requestOptions = requestOptions,
            interaction = interaction,
        )
    }

    internal inner class StoryRecyclerChangeCallback(
        private val adapter: StoryListAdapter
    ) : ListUpdateCallback {

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(position, count, payload)
        }

        override fun onInserted(position: Int, count: Int) {
            adapter.notifyItemRangeChanged(position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapter.notifyDataSetChanged()
        }

        override fun onRemoved(position: Int, count: Int) {
            adapter.notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Story>?, ){
        val newList = list?.toMutableList()
        differ.submitList(newList)
    }

    class ItemViewHolder
    constructor(
        private val binding: LayoutListItemBinding,
        private val requestOptions: RequestOptions,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Story) {
            binding.root.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            Glide.with(binding.root)
                .setDefaultRequestOptions(requestOptions)
                .load(item.imageUrl)
                .transition(withCrossFade())
                .into(binding.itemImage)
            binding.itemTitle.text = item.title
            binding.itemAuthor.text = item.subtitle
        }
    }

    interface Interaction {

        fun onItemSelected(position: Int, item: Story)

    }
}
