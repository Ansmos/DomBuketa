package ru.ansmos.dombuketa.views.rw.groupie

import android.view.View
import androidx.core.content.contentValuesOf
import com.bumptech.glide.Glide
import com.xwray.groupie.Group
import com.xwray.groupie.viewbinding.BindableItem
import ru.ansmos.dombuketa.R
import ru.ansmos.dombuketa.databinding.ItemCarouselProductBinding
import ru.ansmos.dombuketa.models_bll.Product
import ru.ansmos.dombuketa.net_module.ApiConstants

class ProductItem(private val product: Product,
                  private val onClick: (product: Product, pos: Int) -> Unit)
    : BindableItem<ItemCarouselProductBinding>(), Group {

    override fun bind(binding: ItemCarouselProductBinding, position: Int) {
        binding.tovar = product
        binding.price.text = product.price.priceTotal.toString()
        if (product.imageCart != null){
            Glide.with(binding.root.context)
                .load(ApiConstants.IMAGES_URL + product.imageCart.path + product.imageCart.fileName)
                .centerCrop()
                .into(binding.poster)
        }
        //Устанавливае слушатель на callback из Home
        binding.poster.setOnClickListener {
            onClick(product, position)
        }
    }

    override fun getLayout(): Int = R.layout.item_carousel_product

    override fun initializeViewBinding(view: View): ItemCarouselProductBinding = ItemCarouselProductBinding.bind(view)
}