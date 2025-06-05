package ru.ansmos.dombuketa.views.rw

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.ansmos.dombuketa.databinding.ItemCarouselBinding
import ru.ansmos.dombuketa.models_bll.Tag

class TagViewHolder(val binding: ItemCarouselBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(tag: Tag){
        binding.tagTitle.text = tag.name
        binding.tagDescription.text = tag.description
    }
}
