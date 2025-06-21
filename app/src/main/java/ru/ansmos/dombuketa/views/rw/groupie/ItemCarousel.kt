package ru.ansmos.dombuketa.views.rw.groupie

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.viewbinding.BindableItem
import io.reactivex.rxjava3.core.Observable
import ru.ansmos.dombuketa.R
import ru.ansmos.dombuketa.databinding.ItemCarouselBinding


class ItemCarousel(private val content: CarouselContent,
                   private val onClick: (name: String, id: Int) -> Unit,
                   private val onScroll: (state: CarouselRVState, tagId: Int) -> Unit,
                   private val items: MutableList<BindableItem<*>>) : BindableItem<ItemCarouselBinding>(){

    override fun bind(binding: ItemCarouselBinding, position: Int) {
        binding.apply {
            tagTitle.text = content.title
            tagDescription.text = content.description
            itemsContainer.adapter = GroupieAdapter().apply {
                addAll(items)
            }
            //Устанавливае слушатель на callback из Home
            carouselContainer.setOnClickListener{
                onClick(tagDescription.text as String, content.id)
            }
        }
        Observable.create({state ->
            binding.itemsContainer.addOnScrollListener(object: RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dx > 0){  //Движемся враво
                        val visibleItems = recyclerView.layoutManager!!.childCount
                        val totalItemsCount = recyclerView.layoutManager!!.itemCount
                        val pastVisibleItemCount = (recyclerView.layoutManager as LinearLayoutManager)
                            .findFirstVisibleItemPosition()
                        state.onNext(CarouselRVState(pastVisibleItemCount, visibleItems, totalItemsCount))
                        Log.i("Scroll", "Scroll ${dx} items ${visibleItems}/${totalItemsCount} past ${pastVisibleItemCount}")
                    }
                }
            })
        })
        .distinctUntilChanged()
        .subscribe(){
            //onScroll(Observable.just(it), content.id)
            onScroll(it, content.id)
        }
    }

    override fun getLayout(): Int = R.layout.item_carousel

    override fun initializeViewBinding(view: View): ItemCarouselBinding = ItemCarouselBinding.bind(view)

    //Чтобы не передавать все в контструктор, передам ав одном объекте
    data class CarouselContent(
        val id: Int,
        val title: String?,
        val description: String?
    )
    //Решил передавать состояние RV в HomrFragment, пусть логика Paging будет там
    data class CarouselRVState(
        val visibleItemPos: Int,
        val visibleItemsCount: Int,
        val totalItemCount: Int
    )
}

