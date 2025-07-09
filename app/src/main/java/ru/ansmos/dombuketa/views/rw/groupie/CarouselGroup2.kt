package ru.ansmos.dombuketa.views.rw.groupie

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.xwray.groupie.*

class CarouselGroup2(itemDecoration: ItemDecoration?, adapter: GroupieAdapter,
        mainScrollListener: (state: CarouselItem2.CarouselRVState, tagId: Int) -> Unit,
        tagId: Int) : Group {
    private var isEmpty = true
    val adapter: RecyclerView.Adapter<GroupieViewHolder>
    private var groupDataObserver: GroupDataObserver? = null
    private val carouselItem: CarouselItem2

    /*private val adapterDataObserver: AdapterDataObserver = object : AdapterDataObserver() {
        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            val empty = adapter.itemCount == 0
            if (empty && !isEmpty) {
                isEmpty = empty
                groupDataObserver!!.onItemRemoved(carouselItem, 0)
            }
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            val empty = adapter.itemCount == 0
            if (isEmpty && !empty) {
                isEmpty = empty
                groupDataObserver!!.onItemInserted(carouselItem, 0)
            }
        }
    }*/

    init {
        this.adapter = adapter
        //carouselItem = ItemCarousel(itemDecoration, adapter)
        carouselItem = CarouselItem2(itemDecoration, adapter, mainScrollListener, tagId)
        isEmpty = adapter.itemCount == 0
        //adapter.registerAdapterDataObserver(adapterDataObserver)
    }

    override fun getItemCount(): Int {
        return if (isEmpty) 0 else 1
    }

    override fun getItem(position: Int): Item<*> {
        return if (position == 0 && !isEmpty) carouselItem else throw IndexOutOfBoundsException()
    }

    override fun getPosition(item: Item<*>): Int {
        return if (item === carouselItem && !isEmpty) 0 else -1
    }

    override fun registerGroupDataObserver(groupDataObserver: GroupDataObserver) {
        this.groupDataObserver = groupDataObserver
    }

    override fun unregisterGroupDataObserver(groupDataObserver: GroupDataObserver) {
        this.groupDataObserver = null
    }
}