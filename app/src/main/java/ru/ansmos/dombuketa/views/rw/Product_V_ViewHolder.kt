package ru.ansmos.dombuketa.views.rw

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.ansmos.dombuketa.databinding.ItemSimpleHBinding
import ru.ansmos.dombuketa.models_bll.Product
import ru.ansmos.dombuketa.net_module.ApiConstants.IMAGES_URL

class Product_V_ViewHolder(val binding: ItemSimpleHBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Product){
        binding.tovar = item
        if (item.imageCart != null) {
            Glide.with(itemView)
                .load(IMAGES_URL + item.imageCart.path + item.imageCart.fileName)
                .centerCrop()
                .into(binding.poster)
            if (item.isInFavorites) {
                binding.fav.visibility = View.VISIBLE
            }
        }
        binding.price.text = item.price.priceTotal.toString()
    }
}