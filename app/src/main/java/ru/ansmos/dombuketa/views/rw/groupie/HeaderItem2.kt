package ru.ansmos.dombuketa.views.rw.groupie

import android.view.View
import androidx.annotation.DrawableRes
import com.xwray.groupie.viewbinding.BindableItem
import ru.ansmos.dombuketa.R
import ru.ansmos.dombuketa.databinding.ItemHeader2Binding

// Заголовок для горизонтальной группы
class HeaderItem2 @JvmOverloads constructor(
    val caption: CarouselCaption,
    private val onClick: (name: String, id: Int) -> Unit,
    //В заголовое группы вставим картинку с кликом впоследствии
    @field:DrawableRes @param:DrawableRes private val iconResId: Int = 0,
    private val onIconClickListener: View.OnClickListener? = null
) : BindableItem<ItemHeader2Binding>() {

    override fun bind(binding: ItemHeader2Binding, position: Int) {
        binding.title.setText(caption.title)
        //if (subtitleResId != 0) {
            binding.subtitle.setText(caption.description)
        //}
        binding.subtitle.setVisibility(if (caption.description != "") View.VISIBLE else View.GONE)
        if (iconResId != 0) {
            binding.icon.setImageResource(iconResId)
            binding.icon.setOnClickListener(onIconClickListener)
        }
        binding.icon.setVisibility(if (iconResId != 0) View.VISIBLE else View.GONE)
        binding.headerContainer.setOnClickListener {
            onClick(caption.title.toString(), caption.id)
        }
    }

    override fun getLayout(): Int = R.layout.item_header_2

    override fun initializeViewBinding(view: View): ItemHeader2Binding {
        return ItemHeader2Binding.bind(view)
    }

    //Чтобы не передавать все в контструктор, передам ав одном объекте
    data class CarouselCaption(
        val id: Int,
        val title: String?,
        val description: String?
    )
}