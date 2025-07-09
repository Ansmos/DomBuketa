package ru.ansmos.dombuketa.views.rw

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.ansmos.dombuketa.databinding.ItemSimpleHBinding
import ru.ansmos.dombuketa.models_bll.Product

class Product_H_Adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items = mutableListOf<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemSimpleHBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Product_H_ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as Product_H_ViewHolder).bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun clearItems() = items.clear()

    fun addTags(list: List<Product>){
        items.addAll(list)
        notifyDataSetChanged()
    }
}