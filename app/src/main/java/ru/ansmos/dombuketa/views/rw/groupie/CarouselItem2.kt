package ru.ansmos.dombuketa.views.rw.groupie

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.Group
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.viewbinding.BindableItem
import com.xwray.groupie.viewbinding.GroupieViewHolder
import io.reactivex.rxjava3.core.Observable
import ru.ansmos.dombuketa.R
import ru.ansmos.dombuketa.databinding.ItemCarousel2Binding

/**
 * Горизонтальная прокрутка, которая используется в вертикальном RV.
 */
class CarouselItem2(private val carouselDecoration: RecyclerView.ItemDecoration?,
                 private val adapter: GroupieAdapter,
                 private val onScroll: (state: CarouselRVState, tagId: Int) -> Unit,
                 private val tagId: Int)
    : BindableItem<ItemCarousel2Binding?>(), OnItemClickListener, Group {

    init {
        adapter.setOnItemClickListener(this)
    }

    override fun createViewHolder(itemView: View): GroupieViewHolder<ItemCarousel2Binding?> {

        val viewHolder: GroupieViewHolder<ItemCarousel2Binding?> = super.createViewHolder(itemView)

        val recyclerView: RecyclerView
        if (viewHolder.binding != null) {
            recyclerView = viewHolder.binding!!.recyclerView
            if (carouselDecoration != null) {
                recyclerView.addItemDecoration(carouselDecoration)
            }
            recyclerView.layoutManager = LinearLayoutManager(
                recyclerView.context,
                LinearLayoutManager.HORIZONTAL,false
            )
            // Определяем скроллер
            Observable.create({ state ->
                recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (dx > 0){  //Движемся враво
                            val visibleItems = recyclerView.layoutManager!!.childCount
                            val totalItemsCount = recyclerView.layoutManager!!.itemCount
                            val pastVisibleItemCount = (recyclerView.layoutManager as LinearLayoutManager)
                                .findFirstVisibleItemPosition()
                            state.onNext(
                                CarouselRVState(this@CarouselItem2,
                                    pastVisibleItemCount,
                                    visibleItems,
                                    totalItemsCount
                                )
                            )
                            //Log.i("Scroll", "Scroll ${dx} items ${visibleItems}/${totalItemsCount} past ${pastVisibleItemCount}")
                        }
                    }
                })
            })
                .distinctUntilChanged()  //, оптимизируем вывод, чтоб не "шумел"
                .subscribe({
                    onScroll(it, tagId)
                },{
                    it.printStackTrace()
                })
        }
        return viewHolder
    }

    override fun bind(binding: ItemCarousel2Binding, position: Int) {
        binding.recyclerView.setAdapter(adapter)
    }

    override fun onItemClick(item: Item<*>, view: View) {
        // Закладка на дальнейшую функциональность
        adapter.remove(item);
    }

    override fun getLayout(): Int = R.layout.item_carousel_2

    override fun initializeViewBinding(view: View): ItemCarousel2Binding = ItemCarousel2Binding.bind(view)

    //Метод очистки адаптера
    fun clearItems(){
        adapter.clear()
    }

    //Метод для добавления объектов в наш список
    fun addProducts(list: List<Group>){
        adapter.addAll(list)
        adapter.notifyDataSetChanged()  //если без DiffUtils
    }

    //Решил передавать состояние RV в HomrFragment, пусть логика Paging будет там
    data class CarouselRVState(
        val carouselItem: CarouselItem2, //Передаем ссылку на себя, чтобы отвязаться здесь от Интерактора, а вьюМодель не знает, где произошла прокрутка
        val visibleItemPos: Int,
        val visibleItemsCount: Int,
        val totalItemCount: Int
    )
}