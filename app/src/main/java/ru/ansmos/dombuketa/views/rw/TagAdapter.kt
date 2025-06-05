package ru.ansmos.dombuketa.views.rw

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.ansmos.dombuketa.databinding.ItemCarouselBinding
import ru.ansmos.dombuketa.models_bll.Tag

class TagAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var tags = mutableListOf<Tag>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TagViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TagViewHolder).bind(tags[position])
    }

    override fun getItemCount(): Int = tags.size

    fun clearItems() = tags.clear()

    fun addTags(list: List<Tag>){
        tags.addAll(list)
        notifyDataSetChanged()
    }
}