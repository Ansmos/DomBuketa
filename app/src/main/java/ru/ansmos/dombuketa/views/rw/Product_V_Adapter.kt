package ru.ansmos.dombuketa.views.rw

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.ansmos.dombuketa.R
import ru.ansmos.dombuketa.databinding.ItemSimpleHBinding
import ru.ansmos.dombuketa.models_bll.Product
import ru.dombuketa.filmslocaror.view.rv_adapters.ProductDiff

class Product_V_Adapter(private val clickListener: IOnItemClixkListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items = mutableListOf<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemSimpleHBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Product_V_ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as Product_V_ViewHolder).bind(items[position])
        holder.itemView.findViewById<CardView>(R.id.item_container).setOnClickListener{
            clickListener.click(items[position])
        }
    }

    override fun getItemCount(): Int = items.size

    fun clearItems() = items.clear()

    fun addTags(list: List<Product>){
        //items.clear()  //TODO оптимизация требуется
        val productDiffUtilCallback = ProductDiff(this.items, list)
        val productDiffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(productDiffUtilCallback)

        items.addAll(list)
        productDiffResult.dispatchUpdatesTo(this)
//        notifyDataSetChanged()
    }
    fun removeItemFavorite(position: Int) : Int {
        val product_id = items[position].id
        items.removeAt(position)
        //notifyItemRemoved(position)
        return product_id //Возвращаем для удаления из БД
    }
    //Интерфейс для обработки кликов
    interface IOnItemClixkListener{
        fun click(product: Product)
    }
}